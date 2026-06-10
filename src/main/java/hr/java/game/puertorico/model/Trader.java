package hr.java.game.puertorico.model;

import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

import static hr.java.game.puertorico.model.Constants.TRADING_HOUSE;
import static hr.java.game.puertorico.model.Constants.goodsPrice;
import static hr.java.game.puertorico.model.Globals.fastGameMultiplier;
import static hr.java.game.puertorico.util.DialogUtils.*;

public class Trader {
    Button b;
    Button[] tradeBtns;
    Player p;
    Text doubloonsBank;

    Trader(Button b, Button[] tradeBtns, Player p, Text doubloonsBank){
        this.b = b;
        this.tradeBtns = tradeBtns;
        this.p = p;
        this.doubloonsBank = doubloonsBank;
    }
    public boolean sellPlayerGood(){
        int value = Integer.parseInt(b.getText());
        int playerDoubloons = p.getDoubloonsValue();
        if (p.isDidPlayerTrade()){
            alreadyTradedAlert();
            return true;
        }
        List<String> tradedGoods = goodsThatWereTraded(tradeBtns);
        if (tradedGoods.size() == 4){
            cantTradeAnymoreAlert();
            return false;
        }
        if (value > 0){
            if (confirmationDialog("Are you sure you want to sell " + b.getId() + "?").equals("yes")) {
                for (int i = 0; i < TRADING_HOUSE; i++) {
                    if (tradeBtns[i].getText().isEmpty() & (p.buildingKeyValueMap.get("office") || !tradedGoods.contains(b.getId()))){
                        tradeBtns[i].setText("⬤");
                        tradeBtns[i].setId(b.getId());
                        tradeBtns[i].setStyle(tradeBtns[i].getStyle() + "-fx-text-fill: " + p.goodsColor.get(b.getId()) + ";");
                        int doubloonTraderBonus = p.isTraderBonus() ? 1 : 0;
                        p.increaseDoubloon(doubloonsBank, (goodsPrice.get(b.getId()) * fastGameMultiplier + p.getExtraDoubloonTrade() + doubloonTraderBonus));
                        //p.setDoubloonsValue(playerDoubloons + p.goodsPrice.get(b.getId()) + p.getExtraDoubloonTrade() + doubloonTraderBonus);
                        //p.doubloons.setText(Integer.toString(playerDoubloons + p.goodsPrice.get(b.getId()) + p.extraDoubloonTrade + doubloonTraderBonus));
                        b.setText(Integer.toString(value-1));
                        p.setSoldGood(true);
                        if (value-1 <= 0)
                            p.goodsInWindrose.remove(b.getId());
                        else
                            p.goodsInWindrose.replace(b.getId(), value-1);
                        return true;
                    }
                    else if (tradeBtns[i].getText().isEmpty() & (!p.buildingKeyValueMap.get("office") || tradedGoods.contains(b.getId()))){
                        cantTradeThisGoodAlert();
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public static List<String> goodsThatWereTraded(Button[] tradeBtns){
        List<String> tradedGoods = new ArrayList<>();
        for (Button tradeBtn : tradeBtns)
            if (!tradeBtn.getId().isEmpty() & !tradeBtn.getText().isEmpty())
                tradedGoods.add(tradeBtn.getId());
        return tradedGoods;
    }
}
