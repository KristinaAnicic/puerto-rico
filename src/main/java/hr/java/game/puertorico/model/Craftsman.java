package hr.java.game.puertorico.model;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static hr.java.game.puertorico.model.Globals.fastGameMultiplier;
import static hr.java.game.puertorico.util.DialogUtils.*;

public class Craftsman {
    public static void producePlayerGoods(Player p, Map<String, TextField> stockOfGoods, int playerTurn, int[] plantationArray, int[] buildingArray){
        String goodsProduced = "";
        String[] goodType = new String[]{"Indigo", "Tobacco", "Sugar", "Coffee"};
        int count = 0;
        int stockOfCorn = Integer.parseInt(stockOfGoods.get("Corn").getText());
        if (p.getCornPlantCol() > 0 & stockOfCorn > 0){
            int minGoods = Math.min((p.getCornPlantCol()*fastGameMultiplier), stockOfCorn);
            int currentGoods = Integer.parseInt(p.getGoods()[0].getText());
            p.setGoodValueByIndex(0, minGoods+currentGoods);
            //p.getGoods()[0].setText(Integer.toString(minGoods+currentGoods));
            goodsProduced = goodsProduced + "Corn: " + minGoods + "\n";
            stockOfGoods.get("Corn").setText(Integer.toString(stockOfCorn-minGoods));
            if (p.goodsInWindrose.containsKey("Corn"))
                p.goodsInWindrose.replace("Corn", /*p.goodsInWindrose.get("Corn") +*/ minGoods+currentGoods);
            else
                p.goodsInWindrose.put("Corn", minGoods+currentGoods);
            count++;
        }

        for (int i = 0; i < plantationArray.length; i++) {
            int stockOfGood = Integer.parseInt(stockOfGoods.get(goodType[i]).getText());
            if (buildingArray[i] > 0 & stockOfGood > 0 & plantationArray[i] > 0) {
                int minGoods = Math.min(buildingArray[i], plantationArray[i]);
                int availableGoods = Math.min(minGoods*fastGameMultiplier, stockOfGood);
                stockOfGoods.get(goodType[i]).setText(Integer.toString(stockOfGood-availableGoods));
                int currentGoods = p.getGoodValueByIndex(i+1);
                //int currentGoods = Integer.parseInt(p.getGoods()[i+1].getText());
                p.setGoodValueByIndex(i+1, availableGoods + currentGoods);
                //p.getGoods()[i+1].setText(Integer.toString(availableGoods + currentGoods));
                goodsProduced = goodsProduced + goodType[i] + ": " + availableGoods + "\n";
                if (p.goodsInWindrose.containsKey(goodType[i]))
                    p.goodsInWindrose.replace(goodType[i], /*p.goodsInWindrose.get(goodType[i]) + */availableGoods + currentGoods);
                else
                    p.goodsInWindrose.put(goodType[i], availableGoods+currentGoods);
                count++;
            }
        }
        //System.out.println(p.goodsInWindrose);

        if (p.buildingKeyValueMap.get("factory")){
            int[] extraDoubloons = {0, 0, 1, 2, 3, 5};
            p.setDoubloonsValue(p.getDoubloonsValue() + extraDoubloons[count]);
            //p.doubloons.setText(Integer.toString(Integer.parseInt(p.doubloons.getText()) + extraDoubloons[count]));
        }
        if (goodsProduced.isEmpty())
            goodsProduced = "\nThere were no goods to produce";
        else
            goodsProduced = " produced: \n" + goodsProduced;
        producedGoodsInfo(goodsProduced, playerTurn+1);
        //System.out.println(Arrays.toString(plantationArray));
        //System.out.println(Arrays.toString(buildingArray));
    }

    public static void chooseGoodForBonusCraftsmanRole(Player p, Map<String, TextField> stockOfGoods, int[] plantationArray, int[] buildingArray){
        List<String> availableGoods = goodsAvailableForBonus(p, stockOfGoods, plantationArray, buildingArray);
        String chosenGood;
        if (availableGoods.isEmpty()) {
            return;
        }
        else if(availableGoods.size() == 1){
            chosenGood = availableGoods.getFirst();
        }
        else {
            chosenGood = chooseBonusGoodDialog(availableGoods);
        }
        int stockOfGood = Integer.parseInt(stockOfGoods.get(chosenGood).getText());
        stockOfGoods.get(chosenGood).setText(Integer.toString(stockOfGood - 1));

        int numOfChosenPlayerGoods;
        for (Button good:p.getGoods()){
            if (good.getId().equals(chosenGood)){
                numOfChosenPlayerGoods = Integer.parseInt(good.getText());
                good.setText(Integer.toString(numOfChosenPlayerGoods + 1));

                if (p.goodsInWindrose.containsKey(chosenGood))
                    p.goodsInWindrose.replace(chosenGood, numOfChosenPlayerGoods + 1);
            }
        }
        if (!chosenGood.isEmpty())
            producedBonusGoodInfo(chosenGood);
    }

    public static List<String> goodsAvailableForBonus(Player p, Map<String, TextField> stockOfGoods, int[] plantationArray, int[] buildingArray) {
        List<String> goodsAvailableForBonusList = new ArrayList<>();
        String[] goodType = new String[]{"Indigo", "Tobacco", "Sugar", "Coffee"};
        int stockOfCorn = Integer.parseInt(stockOfGoods.get("Corn").getText());
        if (p.getCornPlantCol() > 0 & stockOfCorn > 0) {
            goodsAvailableForBonusList.add("Corn");
        }
        for (int i = 0; i < plantationArray.length; i++) {
            int stockOfGood = Integer.parseInt(stockOfGoods.get(goodType[i]).getText());
            if (buildingArray[i] > 0 & stockOfGood > 0 & plantationArray[i] > 0) {
                goodsAvailableForBonusList.add(goodType[i]);
            }
        }
        return goodsAvailableForBonusList;
    }
    public static int numberOfPlayerGoods(Player p){
        int count = 0;
        for(Button good: p.getGoods()){
            count += p.getGoodValue(good);
        }
        return count;
    }

    public static void resetGoodsInWindrose(Player p){
        p.goodsInWindrose.clear();
        for (Button good : p.getGoods()){
            if (p.getGoodValue(good) > 0) {
                p.goodsInWindrose.put(good.getId(), p.getGoodValue(good));
            }
        }
        //System.out.println(p.goodsInWindrose);
    }
}
