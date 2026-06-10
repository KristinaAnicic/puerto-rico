package hr.java.game.puertorico.model;

import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import lombok.Data;

import java.util.*;

import static hr.java.game.puertorico.model.Building.*;
import static hr.java.game.puertorico.model.Captain.*;
import static hr.java.game.puertorico.model.Colonist.*;
import static hr.java.game.puertorico.model.Craftsman.*;
import static hr.java.game.puertorico.model.Globals.fastGameMultiplier;
import static hr.java.game.puertorico.model.Plantation.placePlantation;
import static hr.java.game.puertorico.util.DialogUtils.*;

@Data
public class Player {
    private Text doubloons, VP, colonists;
    private Button[][] buildingState, plantationState;
    private Button[] goods;
    private RadioButton[][] buildingColonistState;
    private Integer cornPlantCol = 0, indigoPlantCol = 0, coffeePlantCol = 0, tobaccoPlantCol = 0, sugarPlantCol = 0, buildingDiscount = 0, extraDoubloonTrade=0, useWharf = 0;
    private Integer indigoBuildCol = 0, coffeeBuildCol = 0, tobaccoBuildCol = 0, sugarBuildCol = 0;
    private Integer builderDiscount = 0;
    private boolean captainShippedGoodTurn = false, goodsRemoved = false;
    private boolean soldGood = false, didPlayerTakeQuarry = false;
    private boolean didPlayerTrade = false, traderBonus = false, settlerTurnOver = false, haciendaBonusUsed = false;

    Map<String, Boolean> buildingKeyValueMap = new HashMap<>() {{
        put("hacienda", false);
        put("construction_hut", false);
        put("small_warehouse", false);
        put("hospice", false);
        put("office", false);
        put("large_warehouse", false);
        put("factory", false);
        put("university", false);
        put("harbor", false);
    }};
    final Map<String, String> goodsColor = new HashMap<>() {{
        put("Corn", "yellow");
        put("Indigo", "cornflowerblue");
        put("Coffee", "black");
        put("Tobacco", "brown");
        put("Sugar", "#efefd6");
    }};

    public Map<String, Integer> goodsInWindrose = new HashMap<>();
    public String playerName;

    public Player(String num, Text db, Text VP, Text col, Button[][] buildState, Button[][] plantState, RadioButton[][] buildColState, Button[] _goods){
        playerName = "Player " + num;
        doubloons = db;
        this.VP = VP;
        colonists = col;
        buildingState = buildState;
        plantationState = plantState;
        buildingColonistState = buildColState;
        //[0]"Corn", [1]"Indigo", [2]"Tobacco", [3]"Sugar", [4]"Coffee"}
        goods = _goods;
        for (Button g:goods){
            if (Integer.parseInt(g.getText()) > 0)
                goodsInWindrose.put(g.getId(),Integer.parseInt(g.getText()));
        }
    }
    public void setValues(int numOfPlayers){
        if(numOfPlayers == 3){
            setDoubloonsValue(5 * fastGameMultiplier);
            setVPValue(0);
            setColonistsValue(0);
        }
    }
    public int getDoubloonsValue(){ return Integer.parseInt(doubloons.getText()); }
    public int getVPIntegerValue(){ return Integer.parseInt(VP.getText()); }
    public int getColonistsIntegerValue() { return Integer.parseInt(colonists.getText()); }
    public int getGoodValue(Button good) { return Integer.parseInt(good.getText()); }
    public int getGoodValueByIndex(int index) { return Integer.parseInt(goods[index].getText()); }
    public void setDoubloonsValue(int value) { doubloons.setText(Integer.toString(value));}
    public void setVPValue(int value) { VP.setText(Integer.toString(value));}
    public void setColonistsValue(int value) { colonists.setText(Integer.toString(value));}
    public void setGoodValue(Button good, int value) { good.setText(Integer.toString(value)); }
    public void setGoodValueByIndex(int index, int value) { goods[index].setText(Integer.toString(value)); }

    public void increaseDoubloon(Text bankDoubloon, int num){
        bankDoubloon.setText(Integer.toString(Integer.parseInt(bankDoubloon.getText()) - num));
        setDoubloonsValue(getDoubloonsValue() + num);
    }
    public void increaseVP(Text bankVP, int num){
        if (Integer.parseInt(bankVP.getText()) > 0)
            bankVP.setText(Integer.toString(Integer.parseInt(bankVP.getText()) - num));
        setVPValue(getVPIntegerValue() + num);
    }
    public void increaseColonists(Text bankColonists, int num){
        bankColonists.setText(Integer.toString(Integer.parseInt(bankColonists.getText()) - num));
        setColonistsValue(getColonistsIntegerValue() + num);
    }
    public boolean setBuilding(Button b, Text colonistBank) {
        Building buildingFunctions = new Building(b, this, colonistBank);
        if (confirmationDialog("Are you sure you want this building?").equals("yes")) {
            int playerDoubloons = getDoubloonsValue();
            int buildingCost = buildingFunctions.returnCost(b, buildingDiscount, builderDiscount);

            if (buildingCost <= playerDoubloons)
                setDoubloonsValue(playerDoubloons - (buildingCost));
            else {
                notEnoughDoubloonsAlert();
                return false;
            }
            return buildingFunctions.placeNewBuildingToPlayer();
        }
        return false;
    }
    public boolean setPlantation(Button b, Text gameColonists){
        if (confirmationDialog("Are you sure you want this plantation ?").equals("yes")) {
            settlerTurnOver = placePlantation(this, b, gameColonists);
            return settlerTurnOver;
        }
        return settlerTurnOver;
    }
    public boolean availableBuildingSpace(){ return availableEmptyBuildingSpace(buildingState); }

    public boolean haveToDistributeColonists(){
        boolean haveToDistribute = availableEmptyColonistSpace(buildingColonistState,plantationState);
        int numOfColonists = getColonistsIntegerValue();
        if(haveToDistribute && numOfColonists > 0){
            haveToDistributeColonistsAlert();
        }
        return (haveToDistribute && numOfColonists > 0);
    }
    private void enableDisableBuildColonist(){
        int numOfColonists = getColonistsIntegerValue();
        disableUnselectedButtonsIfNoColonists(buildingColonistState, numOfColonists);
    }
    public void addRemoveBuildColonist(RadioButton rb) {
        distributeBuildColonist(rb, this);
        enableDisableBuildColonist();
    }
    public void addRemovePlantColonist(Button b) {
        distributePlantColonist(b, this);
        //System.out.println("Corn: "+ cornPlantCol + "\nindigo: " + indigoPlantCol + "\nsugar: " + sugarPlantCol + "\ntobacco: " + tobaccoPlantCol + "\ncoffee: "+coffeePlantCol + "\nquarry: "+buildingDiscount);
        enableDisableBuildColonist();
    }
    public void produceGoods(Map<String, TextField> stockOfGoods, int playerTurn){
        int[] plantationArray = {indigoPlantCol, tobaccoPlantCol, sugarPlantCol, coffeePlantCol};
        int[] buildingArray = {indigoBuildCol, tobaccoBuildCol, sugarBuildCol, coffeeBuildCol};
        producePlayerGoods(this, stockOfGoods, playerTurn, plantationArray, buildingArray);
    }
    public void chooseGoodForBonus(Map<String, TextField> stockOfGoods){
        int[] plantationArray = {indigoPlantCol, tobaccoPlantCol, sugarPlantCol, coffeePlantCol};
        int[] buildingArray = {indigoBuildCol, tobaccoBuildCol, sugarBuildCol, coffeeBuildCol};
        chooseGoodForBonusCraftsmanRole(this,stockOfGoods,plantationArray,buildingArray);
    }
    public void captainExtraVP(Text VPBank){
        if (Integer.parseInt(VPBank.getText()) > 0 && soldGood){
            increaseVP(VPBank, 1);
            soldGood = false;
        }
    }
    public boolean finishedCaptainPhase(){ return isCaptainPhaseCompleted(goodsInWindrose, captainShippedGoodTurn); }

    public boolean sellGoodTrader(Button b, Button[] tradeBtns, Text doubloonsBank){
        Trader traderFunctions = new Trader(b, tradeBtns,this, doubloonsBank);
        didPlayerTrade = traderFunctions.sellPlayerGood();
        return didPlayerTrade;
    }
    public int goodsDeliveryCaptain(String goodToSell, TextField[] ships, int VPBank){
        return playerGoodsDelivery(goodToSell,ships, VPBank,this);
    }
    public void disableAllButtons(){
        disableColonistButtons(buildingColonistState,plantationState,true);
        for (Button good : goods) good.setDisable(true);
    }
    public void enableButtons(String role){
        if (role.equals("mayor")){
            disableColonistButtons(buildingColonistState,plantationState, false);
        }
        else if (role.equals("trader")){
            for (Button good : goods) good.setDisable(false);
        }
    }

    public boolean isBuildingOccupied(String buildingName){ return buildingKeyValueMap.get(buildingName); }
    public int numOfBuildingsWithNoColonists(){
        return returnNumOfBuildingsWithNoColonists(buildingColonistState);
    }
    public List<String> goodsForSale() {
        List<String> availableGoods = new ArrayList<>();
        for (Button good : goods) {
            if (Integer.parseInt(good.getText()) > 0)
                availableGoods.add(good.getId());
        }
        return availableGoods;
    }
    public void removeGoods(String goodToLeave, Map<String, TextField> stockOfGoods) {
        List<String> goodsString = goodsForSale();
        removePlayerGoods(goodToLeave,stockOfGoods,goodsString,goods,goodsInWindrose);
        captainShippedGoodTurn = true;
        goodsRemoved = true;
    }
}