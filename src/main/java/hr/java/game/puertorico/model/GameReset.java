package hr.java.game.puertorico.model;

import hr.java.game.puertorico.util.GameUtils;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.Map;

import static hr.java.game.puertorico.PuertoRicoController.*;
import static hr.java.game.puertorico.model.Constants.*;
import static hr.java.game.puertorico.model.Globals.*;
import static hr.java.game.puertorico.model.GameSetup.initializeGame;

public class GameReset {

    public static void resetGame(Map<String, Text> bank, Player[] playersList, Button[] plantations, Text colonistShip,
                             ImageView[] governorImage, Text playerTurnText, Text turnInfoText, Button endTurnBtn,
                                 Button quarryBtn, Button[] roles, Button[] tradeBtns, Text[] rolesBonusDoubloons,
                                 Map<Button, TextField[]> buttonToShipMap){
        for (Player p : playersList){
            p.setValues(numOfPlayers);
            resetPlayerPlantations(p.getPlantationState());
            resetPlayerBuildings(p.getBuildingState());
            resetBuildingRadioButtons(p.getBuildingColonistState());
            for (Button good : p.getGoods()){
                p.setGoodValue(good, 0);
            }
            resetPlayerColonistValues(p);

            p.setBuilderDiscount(0);
            p.setSoldGood(false);
            p.setCaptainShippedGoodTurn(false);
            p.setGoodsRemoved(false);
            p.setDidPlayerTrade(false);
            p.setTraderBonus(false);
            p.setSettlerTurnOver(false);
            p.setHaciendaBonusUsed(false);
            p.goodsInWindrose.clear();

            GameUtils.disablePlayerColonistButtons(p);
        }
        for (int i = 0; i < numOfPlayers; i++)
            governorImage[i].setVisible(false);

        for (Button r: roles)
            r.setDisable(false);

        for (Map.Entry<String, Integer> entry : rolesBonus.entrySet()) {
            entry.setValue(0);
        }

        int tradeBtnsLength = tradeBtns.length - 1;
        if (!tradeBtns[tradeBtnsLength].getText().isEmpty()){
            for (Button t:tradeBtns) {
                t.setText("");
                t.setId("");
            }
        }
        numOfBuildingsAndPlantationsAPlayerCanSelect = fastGameMultiplier;
        resetRoles(roles, rolesBonusDoubloons);
        GameUtils.resetTraderShip(tradeBtns);
        GameUtils.resetCaptainShip(buttonToShipMap);

        quarryBtn.setDisable(true);
        governor = initializeGame(numOfPlayers, plantations, playersList, bank, colonistShip, stockOfGoods);
        playerTurn = governor;
        governorImage[governor].setVisible(true);
        playerTurnText.setText("Player "+ (governor+1));
        role = "";
        endRound = true;
        gameEnd = false;
        turnInfoText.setText("Choose a role!");
        endTurnBtn.setDisable(true);
    }

    public static void resetPlayerColonistValues(Player p){
        p.setCornPlantCol(0);
        p.setIndigoPlantCol(0);
        p.setCoffeePlantCol(0);
        p.setTobaccoPlantCol(0);
        p.setSugarPlantCol(0);
        p.setIndigoBuildCol(0);
        p.setCoffeeBuildCol(0);
        p.setTobaccoBuildCol(0);
        p.setSugarBuildCol(0);
        p.setExtraDoubloonTrade(0);
        p.setBuildingDiscount(0);

        //p.buildingKeyValueMap.replaceAll((k, v) -> false);

        for (Map.Entry<String, Boolean> building : p.buildingKeyValueMap.entrySet()) {
            building.setValue(false);
        }
    }

    public static void resetPlayerPlantations(Button[][] plantations){
        for (int i = 0; i < PLANTATION_COLUMNS; i++) {
            for (int j = 0; j < PLANTATION_ROWS; j++) {
                plantations[i][j].setText("");
                plantations[i][j].setId("");
            }
        }
    }
    public static void resetPlayerBuildings(Button[][] buildings){
        for (int i = 0; i < BUILDING_COLUMNS; i++) {
            for (int j = 0; j < BUILDING_ROWS; j++) {
                buildings[j][i].setId("");
                buildings[j][i].setText("");
                buildings[j][i].setStyle(
                        "-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
            }
        }
    }
    public static void resetBuildingRadioButtons(RadioButton[][] radioButtons){
        for (int i = 0; i < NUMOFBUILDINGS; i++){
            for (int j = 0; j < NUM_OF_RADIO_BUTTONS_PER_BUILDING; j++){
                radioButtons[i][j].setId("");
                radioButtons[i][j].setVisible(false);
                radioButtons[i][j].setDisable(true);
                radioButtons[i][j].setSelected(false);
            }
        }
    }

    public static void resetRoles(Button[] roles, Text[] rolesBonusDoubloons){
        for (Button r:roles){
            if (r.isDisabled())
                r.setDisable(false);

            rolesBonus.replace(r.getId(),0);
            for (Text roleBonus:rolesBonusDoubloons){
                if (roleBonus.getId().equals(r.getId())) {
                    roleBonus.setText(Integer.toString(0));
                    break;
                }
            }
        }
        usedRoles.clear();
    }
}
