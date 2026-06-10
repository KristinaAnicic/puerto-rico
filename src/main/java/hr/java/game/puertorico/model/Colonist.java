package hr.java.game.puertorico.model;

import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;

import static hr.java.game.puertorico.model.Constants.*;
import static hr.java.game.puertorico.model.Constants.PLANTATION_ROWS;
import static hr.java.game.puertorico.model.GameReset.resetPlayerColonistValues;

public class Colonist {
    public static void distributeBuildColonist(RadioButton rb, Player p) {
        int changeValue = rb.isSelected() ? 1 : -1;

        if (changeValue == 1 && p.getColonistsIntegerValue() <= 0) {
            rb.setSelected(false);
            return;
        }
        p.setColonistsValue(p.getColonistsIntegerValue() - changeValue);
        //p.colonists.setText(Integer.toString(Integer.parseInt(p.colonists.getText()) - changeValue));

        if (rb.getId().equals("small_indigo_plant") || rb.getId().equals("indigo_plant"))
            p.setIndigoBuildCol(p.getIndigoBuildCol() + changeValue);
        else if (rb.getId().equals("small_sugar_mill") || rb.getId().equals("sugar_mill"))
            p.setSugarBuildCol(p.getSugarBuildCol() + changeValue);
        else if (rb.getId().equals("tobacco_barn"))
            p.setTobaccoBuildCol(p.getTobaccoBuildCol() + changeValue);
        else if (rb.getId().equals("coffee_roaster"))
            p.setCoffeeBuildCol(p.getCoffeeBuildCol() + changeValue);
        else if (rb.getId().equals("small_market"))
            p.setExtraDoubloonTrade(p.getExtraDoubloonTrade() + changeValue);
        else if (rb.getId().equals("large_market"))
            p.setExtraDoubloonTrade(p.getExtraDoubloonTrade() + (2 * changeValue));
        else if (p.buildingKeyValueMap.containsKey(rb.getId()))
            p.buildingKeyValueMap.put(rb.getId(), rb.isSelected());

        //System.out.println(rb.getId());
        //System.out.println("Indigo: " + indigoBuildCol + "\nsugar: " + sugarBuildCol + "\ntobacco: " + tobaccoBuildCol + "\ncoffee: "+coffeeBuildCol + "\nextraDoubloon: "+extraDoubloonTrade);
        //System.out.println("Indigo: " + p.getIndigoBuildCol() + "\nsugar: " + p.getSugarBuildCol() + "\ntobacco: " + p.getTobaccoBuildCol() + "\ncoffee: "+p.getCoffeeBuildCol() + "\nextraDoubloon: "+p.getExtraDoubloonTrade());
        //System.out.println(p.buildingKeyValueMap);
    }

    public static void distributePlantColonist(Button b, Player p) {
        int changeValue = b.getText().contains("〇") ? 1 : -1;
        if (changeValue == 1 && p.getColonistsIntegerValue() <= 0)
            return;
        else if (changeValue == 1 && p.getColonistsIntegerValue() > 0){
            b.setText(b.getText().replace('〇','⬤'));
        }
        else
            b.setText(b.getText().replace('⬤','〇'));

        p.setColonistsValue(p.getColonistsIntegerValue() - changeValue);

        switch (b.getId()) {
            case "Corn":
                p.setCornPlantCol(p.getCornPlantCol() + changeValue);
                break;
            case "Indigo":
                p.setIndigoPlantCol(p.getIndigoPlantCol() + changeValue);
                break;
            case "Sugar":
                p.setSugarPlantCol(p.getSugarPlantCol() + changeValue);
                break;
            case "Coffee":
                p.setCoffeePlantCol(p.getCoffeePlantCol() + changeValue);
                break;
            case "Tobacco":
                p.setTobaccoPlantCol(p.getTobaccoPlantCol() + changeValue);
                break;
            case "Quarry":
                p.setBuildingDiscount(p.getBuildingDiscount() + changeValue);
                break;
        }
        //System.out.println("Indigo: " + p.getIndigoPlantCol() + "\nsugar: " + p.getSugarPlantCol() + "\ntobacco: " + p.getTobaccoPlantCol() + "\ncoffee: "+p.getCoffeePlantCol() + "\nquarry: "+p.getBuildingDiscount());

    }

    public static int returnNumOfBuildingsAndPlantationsWithNoColonists(RadioButton[][] buildingColonistState, Button[][] plantationState){
        int count = 0;
        for (int i = 0; i < NUMOFBUILDINGS; i++) {
            for (int j = 0; j < NUM_OF_RADIO_BUTTONS_PER_BUILDING; j++) {
                if (buildingColonistState[i][j].isVisible() & !buildingColonistState[i][j].isSelected()){
                    count++;
                }
            }
        }
        for (int i = 0; i < PLANTATION_COLUMNS; i++){
            for (int j = 0; j < PLANTATION_ROWS; j++){
                if (plantationState[i][j].getText().contains("〇"))
                    count++;
            }
        }
        return count;
    }

    public static int returnNumOfBuildingsWithNoColonists(RadioButton[][] buildingColonistState){
        int count = 0;
        for (int i = 0; i < NUMOFBUILDINGS; i++) {
            for (int j = 0; j < NUM_OF_RADIO_BUTTONS_PER_BUILDING; j++) {
                if (buildingColonistState[i][j].isVisible() & !buildingColonistState[i][j].isSelected()){
                    count++;
                }
            }
        }
        return count;
    }

    public static void disableColonistButtons(RadioButton[][] buildingColonistState, Button[][] plantationState, boolean disable){
        for (int i = 0; i < NUMOFBUILDINGS; i++) {
            for (int j = 0; j < NUM_OF_RADIO_BUTTONS_PER_BUILDING; j++) {
                if (buildingColonistState[i][j].isVisible()){
                    buildingColonistState[i][j].setDisable(disable);
                }
            }
        }
        for (int i = 0; i < PLANTATION_COLUMNS; i++){
            for (int j = 0; j < PLANTATION_ROWS; j++){
                if (!plantationState[i][j].getText().isEmpty())
                    plantationState[i][j].setDisable(disable);
            }
        }
    }
    public static void disableUnselectedButtonsIfNoColonists(RadioButton[][] buildingColonistState, int numOfColonists){
        for (int i = 0; i < NUMOFBUILDINGS; i++) {
            for (int j = 0; j < NUM_OF_RADIO_BUTTONS_PER_BUILDING; j++) {
                if (buildingColonistState[i][j].isVisible() & numOfColonists > 0){
                    buildingColonistState[i][j].setDisable(false);
                }
                else if(!buildingColonistState[i][j].isSelected() & numOfColonists == 0){
                    buildingColonistState[i][j].setDisable(true);
                }
            }
        }
    }

    public static void setColonistValues(Player p){
        resetPlayerColonistValues(p);
        for (int i = 0; i < NUMOFBUILDINGS; i++) {
            for (int j = 0; j < NUM_OF_RADIO_BUTTONS_PER_BUILDING; j++){
                if (!p.getBuildingColonistState()[i][j].isVisible())
                    break;
                if(p.getBuildingColonistState()[i][j].isSelected()){
                    String buildingColonistType = p.getBuildingColonistState()[i][j].getId();
                    switch(buildingColonistType){
                        case "small_indigo_plant", "indigo_plant" -> p.setIndigoBuildCol(p.getIndigoBuildCol() + 1);
                        case "small_sugar_mill", "sugar_mill" -> p.setSugarBuildCol(p.getSugarBuildCol() + 1);
                        case "tobacco_barn" -> p.setTobaccoBuildCol(p.getTobaccoBuildCol() + 1);
                        case "coffee_roaster" -> p.setCoffeeBuildCol(p.getCoffeeBuildCol() + 1);
                        case "small_market" -> p.setExtraDoubloonTrade(p.getExtraDoubloonTrade() + 1);
                        case "large_market" -> p.setExtraDoubloonTrade(p.getExtraDoubloonTrade() + 2);
                        default -> {
                            if (p.buildingKeyValueMap.containsKey(buildingColonistType)) {
                                p.buildingKeyValueMap.put(p.getBuildingColonistState()[i][j].getId(), p.getBuildingColonistState()[i][j].isSelected());
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < PLANTATION_COLUMNS; i++){
            for (int j = 0; j < PLANTATION_ROWS; j++){
                if (p.getPlantationState()[i][j].getText().contains("⬤")){
                    switch (p.getPlantationState()[i][j].getId()) {
                        case "Corn" -> p.setCornPlantCol(p.getCornPlantCol() + 1);
                        case "Indigo" -> p.setIndigoPlantCol(p.getIndigoPlantCol() + 1);
                        case "Sugar" -> p.setSugarPlantCol(p.getSugarPlantCol() + 1);
                        case "Coffee" -> p.setCoffeePlantCol(p.getCoffeePlantCol() + 1);
                        case "Tobacco" -> p.setTobaccoPlantCol(p.getTobaccoPlantCol() + 1);
                        case "Quarry" -> p.setBuildingDiscount(p.getBuildingDiscount() + 1);
                    }
                }
            }
        }
    }
}
