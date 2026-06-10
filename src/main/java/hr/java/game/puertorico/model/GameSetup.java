package hr.java.game.puertorico.model;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import java.util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static hr.java.game.puertorico.model.Globals.fastGameMultiplier;

public class GameSetup {

    static public Integer numOfQuarryTiles = 8;
    public static Map<String, Integer> islandTilesMap = new HashMap<>();
    static {
        //islandTilesMap.put("Quarry", 8);
        islandTilesMap.put("Coffee", 8);
        islandTilesMap.put("Tobacco", 9);
        islandTilesMap.put("Corn", 10);
        islandTilesMap.put("Sugar", 11);
        islandTilesMap.put("Indigo", 12);
    }

    public static Map<String, Integer> discardedIslandTilesMap = new HashMap<>();
    static {
        //discardedIslandTilesMap.put("Quarry", 0);
        discardedIslandTilesMap.put("Coffee", 0);
        discardedIslandTilesMap.put("Tobacco", 0);
        discardedIslandTilesMap.put("Corn", 0);
        discardedIslandTilesMap.put("Sugar", 0);
        discardedIslandTilesMap.put("Indigo", 0);
    }
    private static int governor;
    public static int initializeGame(int numOfPlayers, Button[] plantations, Player[] players, Map<String, Text> bank, Text colonistShip, Map<String, TextField> stockOfGoods){
        islandTilesMap.replace("Coffee", 8);
        islandTilesMap.replace("Tobacco", 9);
        islandTilesMap.replace("Corn", 10);
        islandTilesMap.replace("Sugar", 11);
        islandTilesMap.replace("Indigo", 12);

        discardedIslandTilesMap.replace("Coffee", 0);
        discardedIslandTilesMap.replace("Tobacco", 0);
        discardedIslandTilesMap.replace("Corn", 0);
        discardedIslandTilesMap.replace("Sugar", 0);
        discardedIslandTilesMap.replace("Indigo", 0);

        numOfQuarryTiles = 8;
        governor = (int)(Math.random()*(numOfPlayers));
        int currentPlayer = governor;

        int remainingIndigo = numOfPlayers == 5 ? 3 : 2;

        for (int i = 0; i < numOfPlayers; i++) {
            if (remainingIndigo > 0){
                players[currentPlayer].getPlantationState()[0][0].setText("Indigo\n\n〇");
                remainingIndigo--;
                islandTilesMap.merge("Indigo",-1,Integer::sum);
                players[currentPlayer].getPlantationState()[0][0].setId("Indigo");
            }
            else{
                players[currentPlayer].getPlantationState()[0][0].setText("Corn\n\n〇");
                islandTilesMap.merge("Corn",-1,Integer::sum);
                players[currentPlayer].getPlantationState()[0][0].setId("Corn");
            }
            currentPlayer = nextPlayer(currentPlayer, numOfPlayers);
        }

        if(numOfPlayers == 3){
            bank.get(BankMapKeys.VP.getBankString()).setText("37");
            bank.get(BankMapKeys.DOUBLOONS.getBankString()).setText("100");
            bank.get(BankMapKeys.COLONISTS.getBankString()).setText("28");
            colonistShip.setText(Integer.toString(3*fastGameMultiplier));
        }

        stockOfGoods.get("Tobacco").setText("9");
        stockOfGoods.get("Coffee").setText("9");
        stockOfGoods.get("Corn").setText("10");
        stockOfGoods.get("Sugar").setText("11");
        stockOfGoods.get("Indigo").setText("11");
        generateIslandTiles(numOfPlayers, plantations, false);

        return governor;

    }

    public static void generateIslandTiles(int numOfPlayers, Button[] plantations, boolean refill) {
        if (refill)
            updateIslandTilesMap(plantations, numOfPlayers);

        for (int i = 0; i < (fastGameMultiplier * numOfPlayers) + 1; i++) {
            Map<String, Integer> tilesToUse = isAnyTileRemaining(islandTilesMap) ? islandTilesMap : discardedIslandTilesMap;

            if (!isAnyTileRemaining(tilesToUse)){
                plantations[i].setText("");
                plantations[i].setId("notChosen");
                plantations[i].setDisable(true);
                continue;
            }
            String randomTile = randomPlantationPicker();
            plantations[i].setText(randomTile + "\n\n〇");
            plantations[i].setId("notChosen");
            plantations[i].setDisable(false);
        }
        //System.out.println("IslandTilesMap: "+ islandTilesMap.toString());
    }

    private static void updateIslandTilesMap(Button[] plantations, int numOfPlayers) {
        for (int i = 0; i < (fastGameMultiplier * numOfPlayers) + 1; i++) {
            String[] lines = plantations[i].getText().split("\n");
            String plantationType = lines[0].trim();

            if (plantations[i].getId().equals("notChosen")) {
                discardedIslandTilesMap.merge(plantationType, 1, Integer::sum);
            }
        }
    }

    public static String randomPlantationPicker() {
        Map<String, Integer> tilesToUse = isAnyTileRemaining(islandTilesMap) ? islandTilesMap : discardedIslandTilesMap;
        List<String> plantationList = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : tilesToUse.entrySet()) {
            String plantation = entry.getKey();
            int weight = entry.getValue();
            for (int i = 0; i < weight; i++) {
                plantationList.add(plantation);
            }
        }
        Random rand = new Random();
        String randomPlantation = plantationList.get(rand.nextInt(plantationList.size()));
        tilesToUse.merge(randomPlantation,-1,Integer::sum);

        return randomPlantation;
    }

    public static boolean isAnyTileRemaining(Map<String, Integer> islandTilesMap) {
        for (int value : islandTilesMap.values()) {
            if (value > 0) {
                return true;
            }
        }
        return false;
    }

    public static int nextPlayer(int current, int numOfPlayers) {
        current++;
        if (current >= numOfPlayers) {
            current = 0;
        }
        return current;
    }

    public static void endTurn(int currentPlayer, int governor, int numOfPlayers, Player[] playersList, String role){
        playersList[currentPlayer].disableAllButtons();
        playersList[currentPlayer].setBuilderDiscount(0);

        int nextPlayer = nextPlayer(currentPlayer,numOfPlayers);
        if (governor != nextPlayer)
            playersList[nextPlayer].enableButtons(role);
    }
}
