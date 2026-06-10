package hr.java.game.puertorico.model;

import hr.java.game.puertorico.PuertoRico;
import hr.java.game.puertorico.util.GameUtils;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.Map;

import static hr.java.game.puertorico.PuertoRicoController.*;
import static hr.java.game.puertorico.model.Constants.*;
import static hr.java.game.puertorico.model.Globals.*;
import static hr.java.game.puertorico.model.GameReset.*;
import static hr.java.game.puertorico.model.GameSetup.*;
import static hr.java.game.puertorico.model.Plantation.haciendaBonus;
import static hr.java.game.puertorico.util.GameUtils.showWinners;

public class TurnManager{
    private Text[] rolesBonusDoubloons;
    private Button[] roles, plantations, tradeBtns;
    private Player[] playersList;
    private Map<String, Text> bank;
    private ImageView[] governorImage;
    private Text colonistShip, playerTurnText, turnInfoText;
    private Button quarryBtn, endTurnBtn;
    private Map<Button, TextField[]> buttonToShipMap;
    private TurnManagerCaptainPhase turnManagerCaptainPhase;

    public TurnManager(Text[] rolesBonusDoubloons, Button[] roles, Button[] plantations, Player[] playersList,
                Map<String, Text> bank, Map<Button, TextField[]> buttonToShipMap, Button[] tradeBtns, ImageView[] governorImage,
                Text colonistShip, Text playerTurnText, Text turnInfoText, Button quarryBtn, Button endTurnBtn){
        this.rolesBonusDoubloons = rolesBonusDoubloons;
        this.roles = roles;
        this.plantations = plantations;
        this.playersList = playersList;
        this.bank = bank;
        this.tradeBtns = tradeBtns;
        this.governorImage = governorImage;
        this.colonistShip = colonistShip;
        this.playerTurnText = playerTurnText;
        this.turnInfoText = turnInfoText;
        this.quarryBtn = quarryBtn;
        this.endTurnBtn = endTurnBtn;
        this.buttonToShipMap = buttonToShipMap;

        turnManagerCaptainPhase = new TurnManagerCaptainPhase(playersList, buttonToShipMap, bank.get("VP"));
    }

    public static void gameEnd(){
        showWinners();
        //gameRestart();
    }

    public void endRound(){
        if(usedRoles.size() == numOfPlayers){
            GameUtils.resetUsedRolesAndAddBonuses(roles, rolesBonusDoubloons);
        }
        switch (role) {
            case "captain" -> turnManagerCaptainPhase.endRoundCaptainPhase();
            case "trader" -> resetUsedValuesAfterTraderRole();
            case "craftsman" -> playersList[playerTurn].chooseGoodForBonus(stockOfGoods);
            case "settler" -> {
                for (Player p : playersList) {
                    p.setSettlerTurnOver(false);
                    p.setHaciendaBonusUsed(false);
                    p.setDidPlayerTakeQuarry(false);
                }
                generateIslandTiles(numOfPlayers, plantations, true);
            }
            case "mayor" -> fillCaptainShipAfterMayorPhase();
            case "builder" -> playersList[governor].setBuilderDiscount(0);
        }

        if (gameEnd){
            gameEnd();
            return;
        }
        governorImage[governor].setVisible(false);
        governor = nextPlayer(playerTurn,numOfPlayers);
        playerTurn = governor;
        endRound = true;
        playerTurnText.setText("Player "+ (governor+1));
        governorImage[governor].setVisible(true);

        role = "";
        turnInfoText.setText("Choose a role!");
        endTurnBtn.setDisable(true);
    }

    public boolean nextTurn(){
        boolean captainRoundOver = false;
        if (role.equals("captain")){
            if (!turnManagerCaptainPhase.nextTurnCaptain(playerTurnText))
                return false;
            captainRoundOver = turnManagerCaptainPhase.checkIfCaptainRoleIsFullyOver();
        }
        else if (role.equals("mayor") && playersList[playerTurn].haveToDistributeColonists())
            return false;

        resetValuesAndChangePlayerTurn();

        if (playerTurn == governor && (!role.equals("captain") || captainRoundOver)){
            if (role.equals("craftsman")) {
                playersList[playerTurn].chooseGoodForBonus(stockOfGoods);
            }
            endRound();
            //return true;
        }
        else{
            if (role.equals("settler")) {
                quarryBtn.setDisable(!playersList[playerTurn].isBuildingOccupied("construction_hut") || numOfQuarryTiles < 1);
                if(PuertoRico.playerName.name().equals(PlayerName.SINGLE_PLAYER.name()))
                    haciendaBonus(playersList[playerTurn],bank.get("colonists"));
            }
            playersList[playerTurn].enableButtons(role);
        }
        return true;
    }

    public void gameRestart(){
        resetGame(bank, playersList, plantations, colonistShip, governorImage, playerTurnText,turnInfoText, endTurnBtn,
                quarryBtn, roles, tradeBtns, rolesBonusDoubloons, buttonToShipMap);
    }
    public void resetValuesAndChangePlayerTurn(){
        endTurn(playerTurn, governor, numOfPlayers, playersList, role);
        playersList[playerTurn].disableAllButtons();
        quarryBtn.setDisable(true);
        playerTurn = nextPlayer(playerTurn,numOfPlayers);
        playerTurnText.setText("Player "+ (playerTurn+1));
        numOfBuildingsAndPlantationsAPlayerCanSelect = fastGameMultiplier;
    }

    public void resetUsedValuesAfterTraderRole(){
        for (Player p : playersList)
            p.setDidPlayerTrade(false);

        playersList[governor].setTraderBonus(false);
        int tradeBtnsLength = tradeBtns.length - 1;
        if (!tradeBtns[tradeBtnsLength].getText().isEmpty()){
            for (Button t:tradeBtns) {
                t.setText("");
                t.setId("");
                stockOfGoods.get(t.getId()).setText(Integer.toString(
                        Integer.parseInt(stockOfGoods.get(t.getId()).getText())+ 1));
            }
        }
    }
    public void fillCaptainShipAfterMayorPhase(){
        int count = 0;
        for (Player player : playersList)
            count = count + player.numOfBuildingsWithNoColonists();

        if (count < numOfPlayers)
            count = numOfPlayers;
        int availableColonists = Integer.parseInt(bank.get("colonists").getText()) - count;
        if (availableColonists >= 0){
            colonistShip.setText(Integer.toString(count));
            bank.get("colonists").setText(Integer.toString(Integer.parseInt(bank.get("colonists").getText()) - count));
        }
        else
            gameEnd = true;
    }
}