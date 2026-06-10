package hr.java.game.puertorico.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;

import java.util.List;
import java.util.Optional;

public class DialogUtils {
    public static String confirmationDialog(String text){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation of selection");
        alert.setHeaderText(text);
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        alert.getButtonTypes().setAll(yesButton, noButton);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == yesButton) return "yes";
            else if (result.get() == noButton) return "no";
        }
        return "";
    }

    public static String confirmationDialogHacienda(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation of selection");
        alert.setHeaderText("Do you want to take an additional face-down plantation (hacienda bonus)?");
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        alert.getButtonTypes().setAll(yesButton, noButton);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == yesButton) return "yes";
            else if (result.get() == noButton) return "no";
        }
        return "";
    }

    public static String chooseGoodToShipDialog(List<String> goodsNotOnShip) {
        if (goodsNotOnShip.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No goods");
            alert.setHeaderText(null);
            alert.setContentText("There is nothing to deliver");
            alert.showAndWait();
            return "";
        }
        ChoiceDialog<String> dialog = new ChoiceDialog<>(goodsNotOnShip.get(0), goodsNotOnShip);
        dialog.setTitle("Confirmation of selection");
        dialog.setHeaderText("Which good would you like to sell?");
        dialog.setContentText("Select a good:");
        Optional<String> result = dialog.showAndWait();
        return result.orElse("");
    }

    public static String chooseGoodToLeaveDialog(List<String> goodsNotOnShip) {
        if (goodsNotOnShip.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Remove goods");
            alert.setHeaderText(null);
            alert.setContentText("Choose a good you want to leave (only one)");
            alert.showAndWait();
            return "";
        }
        ChoiceDialog<String> dialog = new ChoiceDialog<>(goodsNotOnShip.get(0), goodsNotOnShip);
        dialog.setTitle("Confirmation of selection");
        dialog.setHeaderText("Choose a good you want to leave (only one)");
        dialog.setContentText("Select a good:");
        Optional<String> result = dialog.showAndWait();
        return result.orElse("");
    }

    public static String chooseBonusGoodDialog(List<String> availableGoods) {
        if (availableGoods.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Choose a good");
            alert.setHeaderText(null);
            alert.setContentText("Choose a good you want to get as a bonus");
            alert.showAndWait();
            return "";
        }
        ChoiceDialog<String> dialog = new ChoiceDialog<>(availableGoods.get(0), availableGoods);
        dialog.setTitle("Confirmation of selection");
        dialog.setHeaderText("Choose a good you want to get as a bonus (only one)");
        dialog.setContentText("Select a good:");
        Optional<String> result = dialog.showAndWait();
        return result.orElse("");
    }

    public static void producedGoodsInfo(String text, int playerturn){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Produced goods");
        alert.setHeaderText(null);
        alert.setContentText("Player " + playerturn + text);
        alert.showAndWait();
    }
    public static void producedBonusGoodInfo(String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Produced goods");
        alert.setHeaderText(null);
        alert.setContentText("Current governor got " + text + " as a bonus good for the craftsman role!");
        alert.showAndWait();
    }
    public static void noMoreRoomAlert(String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("No more space");
        alert.setHeaderText(null);
        alert.setContentText("There is no room for a " + text + " building");
        alert.showAndWait();
    }
    public static void alreadyTradedAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Can't trade anymore");
        alert.setHeaderText(null);
        alert.setContentText("You can only trade once per round");
        alert.showAndWait();
    }
    public static void cantTradeThisGoodAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Can't trade this good");
        alert.setHeaderText(null);
        alert.setContentText("You can't trade already traded good (office exception)");
        alert.showAndWait();
    }
    public static void cantTradeAnymoreAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Can't trade more goods");
        alert.setHeaderText(null);
        alert.setContentText("The trading ship is full, you can't trade anymore!");
        alert.showAndWait();
    }
    public static void haveToLoadAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("There is space for shipping");
        alert.setHeaderText(null);
        alert.setContentText("You have to load your goods!");
        alert.showAndWait();
    }
    public static void haveToDistributeColonistsAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("You have to distribute all your colonists");
        alert.setHeaderText(null);
        alert.setContentText("There are available spaces for your colonists. Please distribute them accordingly.");
        alert.showAndWait();
    }

    public static void noGoodToShipAlert(String good){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("No " + good);
        alert.setHeaderText(null);
        alert.setContentText("You don't have " + good + " available to ship");
        alert.showAndWait();
    }
    public static void fullShipAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("No more space");
        alert.setHeaderText(null);
        alert.setContentText("The ship is full");
        alert.showAndWait();
    }
    public static void notEnoughDoubloonsAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Can't purchase");
        alert.setHeaderText(null);
        alert.setContentText("You don't have enough doubloons to buy the building");
        alert.showAndWait();
    }
    public static void showBuildingLimitationAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Can't get this building");
        alert.setHeaderText(null);
        alert.setContentText("Each player may build each building only once");
        alert.showAndWait();
    }
    public static void cantSelectMoreAlert(int num, String type){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Can't select anymore");
        alert.setHeaderText(null);
        alert.setContentText("Each player can select only " + num + " " + type + (num > 1 ? "s " : " ") + "in a round");
        alert.showAndWait();
    }
    public static void winnerAnnouncement(String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("WINNERS");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
    public static void successDialog(String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Saved");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}