package hr.java.game.puertorico.model;

import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static hr.java.game.puertorico.model.Globals.fastGameMultiplier;
import static hr.java.game.puertorico.util.DialogUtils.*;
import static hr.java.game.puertorico.util.DialogUtils.fullShipAlert;

public class Captain {
    String goodToSell;
    TextField[] ships;
    int VPBank;
    Player p;

    Captain(String goodToSell, TextField[] ships, int VPBank, Player p){
        this.goodToSell = goodToSell;
        this.ships = ships;
        this.VPBank = VPBank;
        this.p = p;
    }
    public static int playerGoodsDelivery(String goodToSell, TextField[] ships, int VPBank, Player p){
        int extraVP = p.buildingKeyValueMap.getOrDefault("harbor", false) ? 1 : 0;
        int numOfGoods = 0;
        for (Button good : p.getGoods()) {
            if (good.getId().equals(goodToSell)) {
                numOfGoods = Integer.parseInt(good.getText());
                if (numOfGoods <= 0){
                    noGoodToShipAlert(goodToSell);
                    return 0;
                }
                int count = 0;
                for (int i = 0; i < ships.length && numOfGoods > 0; i++) {
                    if (ships[i].getText().isEmpty()){
                        //ships[i].setId(goodToSell);
                        ships[i].setText("⬤");
                        numOfGoods--;
                        count++;
                    }
                }
                p.setVPValue((p.getVPIntegerValue() + count * fastGameMultiplier + extraVP));
                good.setText(Integer.toString(numOfGoods));

                if (numOfGoods <= 0)
                    p.goodsInWindrose.remove(good.getId());
                else
                    p.goodsInWindrose.replace(good.getId(), numOfGoods);

                p.setCaptainShippedGoodTurn(true);
                p.setSoldGood(true);
                System.out.println(p.goodsInWindrose);
                return (count * fastGameMultiplier + extraVP);
            }
        }
        return 0;
    }

    public static void removePlayerGoods(String goodToLeave, Map<String, TextField> stockOfGoods, List<String> goodsString, Button[] goods, Map<String, Integer> goodsInWindrose) {
        for (String goodString : goodsString) {
            for (Button good : goods) {
                if (good.getId().equals(goodString)) {
                    int numOfGoods = Integer.parseInt(good.getText());
                    if (goodString.equals(goodToLeave)) {
                        good.setText("1");
                        int numStockOfGoods = Integer.parseInt(stockOfGoods.get(goodString).getText());
                        stockOfGoods.get(goodString).setText(Integer.toString(numStockOfGoods + (numOfGoods - 1)));
                        goodsInWindrose.replace(goodToLeave, 1);
                    } else {
                        good.setText("0");
                        int numStockOfGoods = Integer.parseInt(stockOfGoods.get(goodString).getText());
                        stockOfGoods.get(goodString).setText(Integer.toString(numStockOfGoods + numOfGoods));
                        goodsInWindrose.remove(goodString);

                    }
                    break;
                }
            }
        }
        System.out.println(goodsInWindrose);
    }

    public static boolean isCaptainPhaseCompleted(Map<String, Integer> goodsInWindrose, boolean captainShippedGoodTurn){
        Map.Entry<String, Integer> firstPair;
        if (!goodsInWindrose.isEmpty()) {
            firstPair = goodsInWindrose.entrySet().iterator().next();
        }
        else{
            firstPair = Map.entry("", 0);
        }
        if(!captainShippedGoodTurn && !goodsInWindrose.isEmpty())
            return false;
        else if (goodsInWindrose.size() > 1)
            return false;
        else if(goodsInWindrose.size() == 1 && firstPair.getValue() > 1)
            return false;
        return true;
    }

    public static boolean checkIfPlayerCanLoadGoods(Player p, Map<Button, TextField[]> buttonToShipMap) {
        List<String> goodsForSale = p.goodsForSale();
        List<String> shipsUsed = new ArrayList<>();;
        for (Button button : buttonToShipMap.keySet()) {
            shipsUsed.add(button.getText());
        }

        for (String good : goodsForSale) {
            for (Button button : buttonToShipMap.keySet()) {
                if (button.getText().equals(good) && Arrays.stream(buttonToShipMap.get(button)).anyMatch(s -> s.getText().isEmpty())) {
                    return true;
                }
            }
            if (!shipsUsed.contains(good) & shipsUsed.contains("???")) {
                return true;
            }
        }
        return false;
    }

    public static boolean clickedShip(Button clickedShip, Player player, Map<Button, TextField[]> buttonToShipMap,
                                      Text VPBank, Text playerTurnText, Button noShipBtn) {
        String goodToSell;
        int valuePoints = Integer.parseInt(VPBank.getText());
        int numOfShippedGoods = 0;
        TextField[] selectedShip;

        selectedShip = buttonToShipMap.getOrDefault(clickedShip, null);

        List<String> goodsForSale = player.goodsForSale();

        List<String> shipsUsed = new ArrayList<>();;
        for (Button button : buttonToShipMap.keySet()) {
            shipsUsed.add(button.getText());
        }

        if (clickedShip.getText().equals("???")) {
            goodsForSale.removeAll(shipsUsed);
            goodToSell = chooseGoodToShipDialog(goodsForSale);
            if (!goodToSell.isEmpty()) {
                numOfShippedGoods = player.goodsDeliveryCaptain(goodToSell, selectedShip, valuePoints);
                clickedShip.setText(goodToSell);
                //playersList[governor].soldGood = true;

                playerTurnText.setText(playerTurnText.getText() + " (has to end turn)");
            }
        } else if (Arrays.stream(selectedShip).anyMatch(s -> s.getText().isEmpty())) {
            if (confirmationDialog("Are you sure you want to sell " + clickedShip.getText() + "?").equals("yes")) {
                goodToSell = clickedShip.getText();
                numOfShippedGoods = player.goodsDeliveryCaptain(goodToSell, selectedShip, valuePoints);
                //playersList[governor].soldGood = true;

                if (numOfShippedGoods > 0) {
                    playerTurnText.setText(playerTurnText.getText() + " (has to end turn)");
                }
            }
        } else if (clickedShip != noShipBtn){
            fullShipAlert();
            return false;
        }

        if(valuePoints - numOfShippedGoods >= 0)
            VPBank.setText(Integer.toString(valuePoints - numOfShippedGoods));
        else
            VPBank.setText("0");
        if (numOfShippedGoods > 0){
            for (Button button : buttonToShipMap.keySet()) {
                button.setDisable(true);
            }
        }
        if ((valuePoints - numOfShippedGoods) <= 0) {
            return true;
        }
        return false;
    }
}
