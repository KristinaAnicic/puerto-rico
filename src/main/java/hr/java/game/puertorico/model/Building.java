package hr.java.game.puertorico.model;

import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.text.Text;

import static hr.java.game.puertorico.model.Constants.*;
import static hr.java.game.puertorico.util.DialogUtils.*;

public class Building {
    private Button buildingToPlace;
    private Player player;
    private Text gameColonist;

    Building(Button _building, Player p, Text gameColonist){
        this.buildingToPlace = _building;
        this.player = p;
        this.gameColonist = gameColonist;
    }

    public int returnCost(Button b, int discount, int builderDiscount){
        String[] words = b.getText().split("\\s+");
        int costIndex = -1;
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals("Cost:")) {
                costIndex = i + 1;
                break;
            }
        }
        String costString = "";
        if (costIndex != -1 && costIndex < words.length) {
            costString = words[costIndex];
        }
        int cost = Integer.parseInt(costString);
        int VP = getVP(b);
        if (VP >= (discount + builderDiscount))
            cost = cost - (discount + builderDiscount);
        else
            cost = cost - VP;

        return cost;
    }

    public static int getVP(Button b){
        String[] words = b.getText().split("\\s+");
        int VPIndex = -1;
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals("VP:")) {
                VPIndex = i + 1;
                break;
            }
        }
        String VPString = "";
        if (VPIndex != -1 && VPIndex < words.length) {
            VPString = words[VPIndex];
        }
        return Integer.parseInt(VPString);
    }

    public int returnNumOfColonists(String text){
        char symbolToCount = '○';
        int count = 0;
        for (char c : text.toCharArray()) {
            if (c == symbolToCount) {
                count++;
            }
        }
        return count;
    }

    public static String buildingType(String text){
        String[] lines = text.split("\n");
        if (lines.length >= 2) {
            return lines[1];
        } else {
            //System.out.println("Nedovoljno linija.");
        }
        return "";
    }


    public boolean placeNewBuildingToPlayer(){
        int counter = -1;
        boolean found = false;
        boolean availableEmptySpace = false;

        for (int i = 0; i < BUILDING_COLUMNS; i++) {
            for (int j = 0; j < BUILDING_ROWS; j++) {
                counter++;
                if (player.getBuildingState()[j][i].getText().isEmpty()) {
                    availableEmptySpace = true;
                    if (isLargeBuilding(buildingToPlace) && j != 2) {
                        found = placeLargeBuilding(buildingToPlace,player,j,i,counter);
                        universityBonus(gameColonist, player, counter);
                        if (found)
                            return true;
                    }
                    else if (!isLargeBuilding(buildingToPlace)) {
                        placeNormalBuilding(buildingToPlace,player,j,i,counter);
                        universityBonus(gameColonist, player, counter);
                        return true;
                    }
                }
                else if(player.getBuildingState()[j][i].getText().equals(buildingToPlace.getText())){
                    showBuildingLimitationAlert();
                    return false;
                }
            }
        }
        if (availableEmptySpace & isLargeBuilding(buildingToPlace) & !found)
            noMoreRoomAlert("large");
        else
            noMoreRoomAlert("normal");
        return false;
    }

    public static boolean availableEmptyBuildingSpace(Button[][] buildingState){
        for (int i = 0; i < BUILDING_COLUMNS; i++) {
            for (int j = 0; j < BUILDING_ROWS; j++) {
                if (buildingState[j][i].getText().isEmpty())
                    return true;
            }
        }
        return false;
    }

    public static boolean availableEmptyColonistSpace(RadioButton[][] buildingColonistState, Button[][] plantationState){
        for (int i = 0; i < NUMOFBUILDINGS; i++) {
            for (int j = 0; j < NUM_OF_RADIO_BUTTONS_PER_BUILDING; j++) {
                if (buildingColonistState[i][j].isVisible() && !buildingColonistState[i][j].isSelected())
                    return true;
            }
        }
        for (int i = 0; i < PLANTATION_COLUMNS; i++) {
            for (int j = 0; j < PLANTATION_ROWS; j++) {
                if(plantationState[i][j].getText().contains("〇"))
                    return true;
            }
        }
        return false;
    }

    public static boolean isLargeBuilding(Button b) {
        return String.valueOf(b.getText().charAt(4)).equals("4");
    }

    public boolean placeLargeBuilding(Button b, Player p, int row, int column, int counter){
        if (p.getBuildingState()[row + 1][column].getText().isEmpty()) {
            p.getBuildingState()[row][column].setText(b.getText());
            p.getBuildingState()[row][column].setStyle(b.getStyle());
            p.getBuildingState()[row + 1][column].setStyle(b.getStyle());
            p.getBuildingState()[row + 1][column].setText("----------");

            p.getBuildingColonistState()[counter + 1][0].setVisible(true);
            p.getBuildingColonistState()[counter + 1][0].setId(buildingType(b.getText()).replaceAll("\\s", "_"));
            return true;
        }
        return false;
    }

    public boolean placeNormalBuilding(Button b, Player p, int row, int column, int counter){
        p.getBuildingState()[row][column].setText(b.getText());
        p.getBuildingState()[row][column].setStyle(b.getStyle());

        int num = returnNumOfColonists(b.getText());
        for (int z = 0; z < num; z++) {
            p.getBuildingColonistState()[counter][z].setVisible(true);
            p.getBuildingColonistState()[counter][z].setId(buildingType(b.getText()).replaceAll("\\s", "_"));
        }
        return true;
    }

    public void universityBonus(Text gameColonist, Player p, int counter){
        int colonistBankValue = Integer.parseInt(gameColonist.getText());
        if (p.buildingKeyValueMap.get("university") & colonistBankValue > 0) {
            p.increaseColonists(gameColonist, 1);
            p.getBuildingColonistState()[counter][0].setSelected(true);
            p.addRemoveBuildColonist(p.getBuildingColonistState()[counter][0]);
        }
    }

    public static int addVictoryPointsOfBuildings(Player[] playersList){
        int maxVP = 0;
        for (Player p : playersList){
            int numOfVP = 0;
            for (int i = 0; i < BUILDING_COLUMNS; i++) {
                for (int j = 0; j < BUILDING_ROWS; j++) {
                    if (!p.getBuildingState()[j][i].getText().isEmpty()) {
                        numOfVP += getVP(p.getBuildingState()[j][i]);
                    }
                }
            }
            p.setVPValue(p.getVPIntegerValue() + numOfVP);
            if (p.getVPIntegerValue() > maxVP) {
                maxVP = p.getVPIntegerValue();
            }
        }
        return maxVP;
    }
}