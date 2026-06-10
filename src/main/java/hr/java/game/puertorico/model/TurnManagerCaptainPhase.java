package hr.java.game.puertorico.model;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Map;

import static hr.java.game.puertorico.PuertoRicoController.stockOfGoods;
import static hr.java.game.puertorico.model.Captain.checkIfPlayerCanLoadGoods;
import static hr.java.game.puertorico.model.Globals.governor;
import static hr.java.game.puertorico.model.Globals.playerTurn;
import static hr.java.game.puertorico.util.DialogUtils.chooseGoodToLeaveDialog;
import static hr.java.game.puertorico.util.DialogUtils.haveToLoadAlert;

public class TurnManagerCaptainPhase {

    private Map<Button, TextField[]> buttonToShipMap;
    private Player[] playersList;
    private Text VPBank;

    TurnManagerCaptainPhase(Player[] playersList, Map<Button, TextField[]> buttonToShipMap, Text VPBank){
        this.playersList = playersList;
        this.buttonToShipMap = buttonToShipMap;
        this.VPBank = VPBank;
    }

    public void endRoundCaptainPhase(){
        for (Player p:playersList) {
            p.setSoldGood(false);
            p.setCaptainShippedGoodTurn(false);
            p.setGoodsRemoved(false);
        }
        for (Button ship : buttonToShipMap.keySet()) {
            int lastIndex = buttonToShipMap.get(ship).length - 1;
            if (!buttonToShipMap.get(ship)[lastIndex].getText().isEmpty()) {
                stockOfGoods.get(ship.getText()).setText(Integer.toString(
                        Integer.parseInt(stockOfGoods.get(ship.getText()).getText())
                        + buttonToShipMap.get(ship).length));
                ship.setText("???");
                for (TextField textField : buttonToShipMap.get(ship)) {
                    textField.setText("");
                    textField.setId("");
                }
            }
        }
    }
    public boolean nextTurnCaptain(Text playerTurnText){
        boolean playerCanLoadGood = checkIfPlayerCanLoadGoods(playersList[playerTurn], buttonToShipMap);
        List<String> goodsForSale = playersList[playerTurn].goodsForSale();
        boolean hasGoodsToLeave = doesPlayerHaveGoodsToLeave();
        //System.out.println("has Goods to leave: " + hasGoodsToLeave + "\nPlayerCanLoadGoods: " + playerCanLoadGood + "\nCaptain shipped: " + playersList[playerTurn].isCaptainShippedGoodTurn());

        if (!playerCanLoadGood){
            if (!playerTurnText.getText().endsWith("(has to end turn)"))
                playerTurnText.setText(playerTurnText.getText() + " (has to end turn)");
        }

        if (mustLoadGoods(goodsForSale, playerCanLoadGood)) {
            haveToLoadAlert();
            return false;
        }
        else if (hasGoodsToLeave && !playerCanLoadGood && !playersList[playerTurn].isCaptainShippedGoodTurn() && !playersList[playerTurn].isGoodsRemoved()) {
            String goodToLeave = chooseGoodToLeaveDialog(goodsForSale);
            if (!goodToLeave.isEmpty()){
                playersList[playerTurn].removeGoods(goodToLeave, stockOfGoods);
            }
            return false;
        }
        else if (!hasGoodsToLeave) {
            playersList[playerTurn].setCaptainShippedGoodTurn(true);
        }

        resetPlayerCaptainTurnValue(playerCanLoadGood);

        for (Button ship : buttonToShipMap.keySet()){
            ship.setDisable(false);
        }
        return true;
    }

    public boolean doesPlayerHaveGoodsToLeave(){
        Map.Entry<String, Integer> firstPair;
        if (!playersList[playerTurn].goodsInWindrose.isEmpty()) {
            firstPair = playersList[playerTurn].goodsInWindrose.entrySet().iterator().next();
        }
        else{
            firstPair = Map.entry("", 0);
        }
        return playersList[playerTurn].goodsInWindrose.size() > 1 || (playersList[playerTurn].goodsInWindrose.size() == 1 && firstPair.getValue() > 1);
    }

    public boolean mustLoadGoods(List<String> goodsForSale, boolean playerCanLoadGood){
        return !goodsForSale.isEmpty() && playerCanLoadGood && !playersList[playerTurn].isCaptainShippedGoodTurn();
    }
    public void resetPlayerCaptainTurnValue(boolean playerCanLoadGood){
        boolean playerCanLoad = checkIfPlayerCanLoadGoods(playersList[playerTurn], buttonToShipMap);
        if (!playersList[playerTurn].isGoodsRemoved() && (playerCanLoad || doesPlayerHaveGoodsToLeave())) {
            playersList[playerTurn].setCaptainShippedGoodTurn(false);
        }
    }

    public boolean checkIfCaptainRoleIsFullyOver(){
        boolean isCaptainRoundOver = true;
        for (Player p : playersList) {
            if (!p.finishedCaptainPhase()) {
                isCaptainRoundOver = false;
                break;
            }
        }
        if (isCaptainRoundOver){
            playersList[governor].captainExtraVP(VPBank);
            return true;
        }
        return false;
    }
}
