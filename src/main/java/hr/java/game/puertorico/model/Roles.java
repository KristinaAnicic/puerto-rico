package hr.java.game.puertorico.model;

import hr.java.game.puertorico.PuertoRicoController;
import hr.java.game.puertorico.util.GameUtils;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.Map;

import static hr.java.game.puertorico.PuertoRicoController.*;
import static hr.java.game.puertorico.model.Captain.checkIfPlayerCanLoadGoods;
import static hr.java.game.puertorico.model.Globals.*;
import static hr.java.game.puertorico.model.GameSetup.nextPlayer;
import static hr.java.game.puertorico.model.GameSetup.numOfQuarryTiles;
import static hr.java.game.puertorico.model.Plantation.haciendaBonus;
import static hr.java.game.puertorico.util.DialogUtils.confirmationDialog;

public class Roles {
    private Player[] playersList;
    private String role;
    private int numOfPlayers, governor;
    private Text turnInfoText;
    private Map<String, Text> bank;
    public Roles(String role, Player[] playersList, int numOfPlayers, int governor, Text turnInfoText, Map<String, Text> bank){
        this.role = role;
        this.playersList = playersList;
        this.numOfPlayers = numOfPlayers;
        this.governor = governor;
        this.turnInfoText = turnInfoText;
        this.bank = bank;
    }

    public void rolesBonus(String role, Text[] rolesBonusDoubloons){
        int roleBonus = rolesBonus.get(role);
        if (roleBonus > 0) {
            rolesBonus.replace(role, 0);
            playersList[governor].increaseDoubloon(bank.get("doubloons"), roleBonus);
            for (Text roleBonusText: rolesBonusDoubloons){
                if (roleBonusText.getId().equals(role)){
                    roleBonusText.setText("0");
                    break;
                }
            }
        }
    }

    public void settlerRoleFunction(Button quarryBtn){
        usedRoles.add("settler");
        turnInfoText.setText("Settler -> Choose a plantation!");
        quarryBtn.setDisable(numOfQuarryTiles < 1);
        haciendaBonus(playersList[governor], bank.get("colonists"));
    }

    public void builderRoleFunction(){
        usedRoles.add("builder");
        turnInfoText.setText("Builder -> Choose a building or end turn!");
        playersList[governor].setBuilderDiscount(1);
    }

    public void mayorRoleFunction(Text colonistShip){
        int playerTurn = governor;
        usedRoles.add("mayor");
        turnInfoText.setText("Mayor -> Distribute your colonists");
        playersList[governor].enableButtons("mayor");

        if (Integer.parseInt(bank.get("colonists").getText()) > 0){
            playersList[governor].increaseColonists(bank.get("colonists"), 1);
        }

        for (int i = 0; i < Integer.parseInt(colonistShip.getText()); i++){
            playersList[playerTurn].setColonistsValue(playersList[playerTurn].getColonistsIntegerValue() + 1);
            playerTurn = nextPlayer(playerTurn,numOfPlayers);
        }
        colonistShip.setText("0");
    }

    public void craftsmanRoleFunction(){
        usedRoles.add("craftsman");
        int playerTurn = governor;
        int num = numOfPlayers;
        while (num > 0){
            playersList[playerTurn].produceGoods(stockOfGoods,playerTurn);
            playerTurn = nextPlayer(playerTurn, numOfPlayers);
            num--;
        }
    }

    public void traderRoleFunction(){
        usedRoles.add("trader");
        turnInfoText.setText("Trader -> On your board choose a good you want to trade");
        playersList[governor].enableButtons("trader");
        playersList[governor].setTraderBonus(true);
    }

    public void captainRoleFunction(Map<Button, TextField[]> buttonToShipMap, Text playerTurnText){
        usedRoles.add("captain");
        turnInfoText.setText("Captain -> Select a ship");
        boolean canLoad = checkIfPlayerCanLoadGoods(playersList[governor], buttonToShipMap);
        if (!canLoad){
            playerTurnText.setText(playerTurnText.getText() + " (has to end turn)");
        }
    }

    public String roleClicked(Button roleButton, Button quarryBtn, Text colonistShip, Map<Button, TextField[]> buttonToShipMap, Text playerTurnText) {
        if (confirmationDialog("Are you sure you want this role?").equals("yes")) {
            rolesBonus(roleButton.getId(), rolesBonusDoubloons);
            publicEndTurnBtn.setDisable(false);
            endRound = false;
            roleButton.setDisable(true);
            switch (roleButton.getId()) {
                case "settlerBtn":
                    this.role = "settler";
                    settlerRoleFunction(quarryBtn);
                    break;
                case "builderBtn":
                    this.role = "builder";
                    builderRoleFunction();
                    break;
                case "mayorBtn":
                    this.role = "mayor";
                    mayorRoleFunction(colonistShip);
                    break;
                case "craftsmanBtn":
                    this.role = "craftsman";
                    craftsmanRoleFunction();
                    break;
                case "traderBtn":
                    this.role = "trader";
                    traderRoleFunction();
                    break;
                case "captainBtn":
                    this.role = "captain";
                    captainRoleFunction(buttonToShipMap, playerTurnText);
                    break;
            /*case "prospectorBtn":
                usedRoles.add("prospector");
                player1.increaseDoubloon(doubloonsBank,1);
                endRound = true;
                break;*/
            }
            Globals.role = this.role;
        }
        return role;
    }
}
