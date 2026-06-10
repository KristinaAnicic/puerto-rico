package hr.java.game.puertorico.model;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static hr.java.game.puertorico.model.Colonist.setColonistValues;
import static hr.java.game.puertorico.model.Constants.*;
import static hr.java.game.puertorico.model.Craftsman.resetGoodsInWindrose;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameState implements Serializable {
    private String[][][] buildingBoardStateText = new String[numOfPlayers][BUILDING_ROWS][BUILDING_COLUMNS];
    private String[][][] buildingBoardStateBackground = new String[numOfPlayers][BUILDING_ROWS][BUILDING_COLUMNS];
    private String[][][] plantationBoardStateText = new String[numOfPlayers][PLANTATION_COLUMNS][PLANTATION_ROWS];
    private String[][][] plantationBoardStateId = new String[numOfPlayers][PLANTATION_COLUMNS][PLANTATION_ROWS];
    private String[][][] buildingColonistBoardStateId = new String[numOfPlayers][NUMOFBUILDINGS][NUM_OF_RADIO_BUTTONS_PER_BUILDING];
    private Boolean[][][] buildingColonistBoardStateSelected = new Boolean[numOfPlayers][NUMOFBUILDINGS][NUM_OF_RADIO_BUTTONS_PER_BUILDING];
    private Boolean[][][] buildingColonistBoardStateVisible = new Boolean[numOfPlayers][NUMOFBUILDINGS][NUM_OF_RADIO_BUTTONS_PER_BUILDING];
    private String[][] playerGoods = new String[numOfPlayers][NUM_OF_GOODS];
    private Integer[] playerDoubloons = new Integer[numOfPlayers];
    private Integer[] playerVP = new Integer[numOfPlayers];
    private Integer[] playerColonists = new Integer[numOfPlayers];

    private Integer[] builderDiscount = new Integer[numOfPlayers];
    private Boolean[] traderBonus = new Boolean[numOfPlayers];
    private Boolean[] soldGood = new Boolean[numOfPlayers];
    private Boolean[] captainShippedGoodTurn = new Boolean[numOfPlayers];
    private Boolean[] goodsRemoved = new Boolean[numOfPlayers];
    private Boolean[] didPlayerTrade = new Boolean[numOfPlayers];
    private Boolean[] settlerTurnOver = new Boolean[numOfPlayers];
    private Boolean[] haciendaBonusUsed = new Boolean[numOfPlayers];
    private Boolean[] governorImageVisibility = new Boolean[numOfPlayers];
    private Boolean[] didPlayerTakeQuarry = new Boolean[numOfPlayers];
    private String VPBank, doubloonsBank, colonistsBank;
    private String[] stockOfGoods = new String[NUM_OF_GOODS];
    private Map<String, Integer> islandTilesMap, discardedIslandTilesMap;
    private List<String> usedRoles;
    private String[] rolesBonusDoubloons = new String[NUM_OF_ROLES];
    private Boolean[] rolesStateDisabled = new Boolean[NUM_OF_ROLES];
    private Map<String, Integer> rolesBonus;
    private String[] tradeBtnsStateText = new String[TRADING_HOUSE];
    private String[] tradeBtnsStateId = new String[TRADING_HOUSE];
    private String[] tradeBtnsStateStyle = new String[TRADING_HOUSE];
    private List<String[]> captainShips = new ArrayList<>(3);
    private String[] captainShipBtns = new String[3];
    private Boolean quarryBtnDisabled, endTurnBtn;
    private String colonistShipValue;
    private String[] plantationsText = new String[NUM_OF_PLANTATIONS];
    private String[] plantationsId = new String[NUM_OF_PLANTATIONS];
    private Boolean[] plantationsDisabled = new Boolean[NUM_OF_PLANTATIONS];

    private String role, turnInfoText, playerTurnText;
    private Integer governor, playerTurn, numOfBuildingsAndPlantationsAPlayerCanSelect;
    private Boolean endRound, gameEnd;

    public GameState(Player[] playerList, String role, Integer governor, Integer playerTurn, Boolean endRound, Boolean gameEnd,
                     Integer numOfBuildingsAndPlantationsAPlayerCanSelect, ImageView[] governorImage, Map<String, Text> bank, Map<String, TextField> stockOfGoods, Map<String, Integer> islandTilesMap,
                     Map<String, Integer> discardedIslandTilesMap, List<String> usedRoles, Text[] rolesBonusDoubloons,
                     Button[] roles, Map<String, Integer> rolesBonus, Button[] tradeBtns, Button quarryBtn, Text colonistShip,
                     Map<Button, TextField[]> buttonToShipMap, Button endTurnBtn, Text turnInfoText, Text playerTurnText, Button[] plantations){

        convertPlayerValuesToString(playerList);
        this.role = role;
        this.governor = governor;
        this.playerTurn = playerTurn;
        this.endRound = endRound;
        this.gameEnd = gameEnd;
        this.numOfBuildingsAndPlantationsAPlayerCanSelect = numOfBuildingsAndPlantationsAPlayerCanSelect;
        this.VPBank = bank.get(BankMapKeys.VP.getBankString()).getText();
        this.doubloonsBank = bank.get(BankMapKeys.DOUBLOONS.getBankString()).getText();
        this.colonistsBank = bank.get(BankMapKeys.COLONISTS.getBankString()).getText();
        this.islandTilesMap = islandTilesMap;
        this.discardedIslandTilesMap = discardedIslandTilesMap;
        this.usedRoles = usedRoles;
        this.rolesBonus = rolesBonus;
        this.quarryBtnDisabled = quarryBtn.isDisabled();
        this.colonistShipValue = colonistShip.getText();
        this.endTurnBtn = endTurnBtn.isDisabled();
        this.turnInfoText = turnInfoText.getText();
        this.playerTurnText = playerTurnText.getText();

        for (int i = 0; i < governorImage.length; i++)
            governorImageVisibility[i] = governorImage[i].isVisible();

        List<String> goods = Arrays.asList("Coffee", "Tobacco", "Corn", "Sugar", "Indigo");
        for (int i = 0; i < stockOfGoods.size(); i++){
            this.stockOfGoods[i] = stockOfGoods.get(goods.get(i)).getText();
        }
        for (int i = 0; i < rolesBonusDoubloons.length; i++) {
            this.rolesBonusDoubloons[i] = rolesBonusDoubloons[i].getText();
            this.rolesStateDisabled[i] = roles[i].isDisabled();
        }
        for (int i = 0; i < tradeBtns.length; i++){
            this.tradeBtnsStateText[i] = tradeBtns[i].getText();
            this.tradeBtnsStateId[i] = tradeBtns[i].getId();
            this.tradeBtnsStateStyle[i] = tradeBtns[i].getStyle();
        }

        captainShips.add(new String[4]);
        captainShips.add(new String[5]);
        captainShips.add(new String[6]);

        int index = 0;
        for (Button button : buttonToShipMap.keySet()) {
            captainShipBtns[index] = button.getText();
            TextField[] textFields = buttonToShipMap.get(button);
            for (int i = 0; i < textFields.length; i++) {
                captainShips.get(index)[i] = textFields[i].getText();
            }
            index++;
        }
        for (int i = 0; i < plantations.length; i++){
            this.plantationsText[i] = plantations[i].getText();
            this.plantationsId[i] = plantations[i].getId();
            this.plantationsDisabled[i] = plantations[i].isDisabled();
        }
    }
    public void convertPlayerValuesToString(Player[] playersList) {
        int playerNumber = 0;
        for (Player p: playersList){
            for (int i = 0; i < BUILDING_COLUMNS; i++) {
                for (int j = 0; j < BUILDING_ROWS; j++) {
                    buildingBoardStateText[playerNumber][j][i] = p.getBuildingState()[j][i].getText();
                    buildingBoardStateBackground[playerNumber][j][i] = p.getBuildingState()[j][i].getStyle();
                }
            }
            for (int i = 0; i < PLANTATION_COLUMNS; i++){
                for (int j = 0; j < PLANTATION_ROWS; j++){
                    plantationBoardStateText[playerNumber][i][j] = p.getPlantationState()[i][j].getText();
                    plantationBoardStateId[playerNumber][i][j] = p.getPlantationState()[i][j].getId();
                }
            }
            for (int i = 0; i < NUMOFBUILDINGS; i++){
                for (int j = 0; j < NUM_OF_RADIO_BUTTONS_PER_BUILDING; j++){
                    buildingColonistBoardStateId[playerNumber][i][j] = p.getBuildingColonistState()[i][j].getId();
                    buildingColonistBoardStateSelected[playerNumber][i][j] = p.getBuildingColonistState()[i][j].isSelected();
                    buildingColonistBoardStateVisible[playerNumber][i][j] = p.getBuildingColonistState()[i][j].isVisible();
                }
            }
            for (int i = 0; i < NUM_OF_GOODS; i++){
                playerGoods[playerNumber][i] = p.getGoods()[i].getText();
            }
            playerDoubloons[playerNumber] = p.getDoubloonsValue();
            playerVP[playerNumber] = p.getVPIntegerValue();
            playerColonists[playerNumber] = p.getColonistsIntegerValue();

            builderDiscount[playerNumber] = p.getBuilderDiscount();
            traderBonus[playerNumber] = p.isTraderBonus();
            soldGood[playerNumber] = p.isSoldGood();
            captainShippedGoodTurn[playerNumber] = p.isCaptainShippedGoodTurn();
            goodsRemoved[playerNumber] = p.isGoodsRemoved();
            didPlayerTrade[playerNumber] = p.isDidPlayerTrade();
            settlerTurnOver[playerNumber] = p.isSettlerTurnOver();
            haciendaBonusUsed[playerNumber] = p.isHaciendaBonusUsed();
            didPlayerTakeQuarry[playerNumber] = p.isDidPlayerTakeQuarry();

            playerNumber++;
        }
    }

    public void convertStringValuesToPlayer(Player[] playersList){
        int playerNumber = 0;
        for (Player p: playersList){
            for (int i = 0; i < BUILDING_COLUMNS; i++) {
                for (int j = 0; j < BUILDING_ROWS; j++) {
                    p.getBuildingState()[j][i].setText(buildingBoardStateText[playerNumber][j][i]);
                    p.getBuildingState()[j][i].setStyle(buildingBoardStateBackground[playerNumber][j][i]);
                }
            }
            for (int i = 0; i < PLANTATION_COLUMNS; i++){
                for (int j = 0; j < PLANTATION_ROWS; j++){
                    p.getPlantationState()[i][j].setText(plantationBoardStateText[playerNumber][i][j]);
                    p.getPlantationState()[i][j].setId(plantationBoardStateId[playerNumber][i][j]);
                }
            }
            for (int i = 0; i < NUMOFBUILDINGS; i++){
                for (int j = 0; j < NUM_OF_RADIO_BUTTONS_PER_BUILDING; j++){
                    p.getBuildingColonistState()[i][j].setId(buildingColonistBoardStateId[playerNumber][i][j]);
                    p.getBuildingColonistState()[i][j].setSelected(buildingColonistBoardStateSelected[playerNumber][i][j]);
                    p.getBuildingColonistState()[i][j].setVisible(buildingColonistBoardStateVisible[playerNumber][i][j]);
                }
            }
            for (int i = 0; i < NUM_OF_GOODS; i++){
                p.getGoods()[i].setText(playerGoods[playerNumber][i]);
            }
            p.setDoubloonsValue(playerDoubloons[playerNumber]);
            p.setVPValue(playerVP[playerNumber]);
            p.setColonistsValue(playerColonists[playerNumber]);

            p.setBuilderDiscount(builderDiscount[playerNumber]);
            p.setTraderBonus(traderBonus[playerNumber]);
            p.setSoldGood(soldGood[playerNumber]);
            p.setCaptainShippedGoodTurn(captainShippedGoodTurn[playerNumber]);
            p.setGoodsRemoved(goodsRemoved[playerNumber]);
            p.setDidPlayerTrade(didPlayerTrade[playerNumber]);
            p.setSettlerTurnOver(settlerTurnOver[playerNumber]);
            p.setHaciendaBonusUsed(haciendaBonusUsed[playerNumber]);
            p.setDidPlayerTakeQuarry(didPlayerTakeQuarry[playerNumber]);

            setColonistValues(p);
            resetGoodsInWindrose(p);
            playerNumber++;
        }
    }
}
