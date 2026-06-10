package hr.java.game.puertorico.model;

import hr.java.game.puertorico.PuertoRico;
import hr.java.game.puertorico.PuertoRicoController;
import hr.java.game.puertorico.util.XmlUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static hr.java.game.puertorico.PuertoRicoController.*;
import static hr.java.game.puertorico.model.Constants.*;
import static hr.java.game.puertorico.model.Globals.*;
import static hr.java.game.puertorico.model.GameSetup.*;
import static hr.java.game.puertorico.model.Plantation.haciendaBonus;
import static hr.java.game.puertorico.tcp.Client.sendGameState;
import static hr.java.game.puertorico.util.GameUtils.activateButtons;
import static hr.java.game.puertorico.util.DialogUtils.successDialog;

@AllArgsConstructor
@NoArgsConstructor
public class GameFileManager {
    private Player[] playersList;
    private Boolean endRound, gameEnd;
    private ImageView[] governorImage;
    private Map<String, TextField> stockOfGoods;
    private Text[] rolesBonusDoubloons;
    private Button[] roles, tradeBtns, plantations;
    private Button quarryBtn, endTurnBtn;
    private Map<Button, TextField[]> buttonToShipMap;
    private Map<String, Text> bank;
    private Text turnInfoText, playerTurnText, colonistShip;

    public void saveGame(){
        GameState gameStateToSave = new GameState(playersList, role, governor, playerTurn, endRound, gameEnd,
                numOfBuildingsAndPlantationsAPlayerCanSelect, governorImage, bank, stockOfGoods,
                islandTilesMap, discardedIslandTilesMap, usedRoles, rolesBonusDoubloons, roles, rolesBonus, tradeBtns,
                PuertoRicoController.publicQuarryBtn, PuertoRicoController.publicColonistShip, buttonToShipMap,
                PuertoRicoController.publicEndTurnBtn, PuertoRicoController.publicTurnInfoText,
                PuertoRicoController.publicPlayerTurnText, plantations);
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_GAME_PATH))){
            oos.writeObject(gameStateToSave);
            successDialog("Game was successfully saved!");
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void loadSavedGame(){

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_GAME_PATH))) {
            GameState gameStateToLoad = (GameState) ois.readObject();

            TurnManager turnManager = new TurnManager(rolesBonusDoubloons, roles, plantations, playersList, bank, buttonToShipMap, tradeBtns,
                    governorImage, colonistShip, playerTurnText, turnInfoText, quarryBtn, endTurnBtn);
            turnManager.gameRestart();

            loadGame(gameStateToLoad);

            //enableButtons();

            successDialog("Last save was successfully loaded!");

        }catch (IOException ex){
            ex.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void loadCurrentGame(GameState gameStateToLoad){

        loadGame(gameStateToLoad);

        if (role.equals("settler") && PuertoRico.playerName.ordinal() == playerTurn) {
            if (haciendaBonus(playersList[playerTurn],bank.get("colonists")))
                sendGameState();
        }
        if(gameStateToLoad.getGameEnd()){
            TurnManager.gameEnd();
        }
        /*Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            if (role.equals("settler") && PuertoRico.playerName.ordinal() == playerTurn) {
                Platform.runLater(() -> {
                    if (haciendaBonus(playersList[playerTurn], bank.get("colonists"))) {
                        sendGameState();
                    }
                });
            }
        }));
        timeline.setCycleCount(1);
        timeline.play();*/
    }
    public void loadGame(GameState gameStateToLoad) {

        gameStateToLoad.convertStringValuesToPlayer(PuertoRicoController.playersList);
        Globals.governor = gameStateToLoad.getGovernor();
        Globals.playerTurn = gameStateToLoad.getPlayerTurn();
        PuertoRicoController.bank.get("doubloons").setText(gameStateToLoad.getDoubloonsBank());
        PuertoRicoController.bank.get("VP").setText(gameStateToLoad.getVPBank());
        PuertoRicoController.bank.get("colonists").setText(gameStateToLoad.getColonistsBank());
        Globals.role = gameStateToLoad.getRole();
        GameSetup.islandTilesMap.putAll(gameStateToLoad.getIslandTilesMap());
        GameSetup.discardedIslandTilesMap.putAll(gameStateToLoad.getDiscardedIslandTilesMap());
        Globals.numOfBuildingsAndPlantationsAPlayerCanSelect = gameStateToLoad.getNumOfBuildingsAndPlantationsAPlayerCanSelect();
        Globals.rolesBonus = gameStateToLoad.getRolesBonus();
        Globals.endRound = gameStateToLoad.getEndRound();
        Globals.gameEnd = gameStateToLoad.getGameEnd();
        PuertoRicoController.usedRoles = gameStateToLoad.getUsedRoles();

        int index = 0;
        for (Button button : PuertoRicoController.buttonToShipMap.keySet()) {
            button.setText(gameStateToLoad.getCaptainShipBtns()[index]);
            TextField[] textFields = PuertoRicoController.buttonToShipMap.get(button);
            for (int i = 0; i < textFields.length; i++) {
                textFields[i].setText(gameStateToLoad.getCaptainShips().get(index)[i]);
            }
            index++;
        }
        PuertoRicoController.publicColonistShip.setText(gameStateToLoad.getColonistShipValue());
        PuertoRicoController.publicPlayerTurnText.setText(gameStateToLoad.getPlayerTurnText());
        PuertoRicoController.publicTurnInfoText.setText(gameStateToLoad.getTurnInfoText());

        for (int i = 0; i < PuertoRicoController.rolesBonusDoubloons.length; i++) {
            PuertoRicoController.rolesBonusDoubloons[i].setText(gameStateToLoad.getRolesBonusDoubloons()[i]);
        }

        for (Player p : PuertoRicoController.playersList) {
            p.disableAllButtons();
        }

        for (int i = 0; i < numOfPlayers; i++) {
            PuertoRicoController.governorImage[i].setVisible(gameStateToLoad.getGovernorImageVisibility()[i]);
        }
        List<String> goods = Arrays.asList("Coffee", "Tobacco", "Corn", "Sugar", "Indigo");
        for (int i = 0; i < NUM_OF_GOODS; i++) {
            PuertoRicoController.stockOfGoods.get(goods.get(i)).setText(gameStateToLoad.getStockOfGoods()[i]);
        }
        for (int i = 0; i < PuertoRicoController.tradeBtns.length; i++) {
            PuertoRicoController.tradeBtns[i].setText(gameStateToLoad.getTradeBtnsStateText()[i]);
            PuertoRicoController.tradeBtns[i].setId(gameStateToLoad.getTradeBtnsStateId()[i]);
            PuertoRicoController.tradeBtns[i].setStyle(gameStateToLoad.getTradeBtnsStateStyle()[i]);
        }
        for (int i = 0; i < PuertoRicoController.plantations.length; i++) {
            PuertoRicoController.plantations[i].setText(gameStateToLoad.getPlantationsText()[i]);
            PuertoRicoController.plantations[i].setId(gameStateToLoad.getPlantationsId()[i]);
        }
        activateButtons();
    }

    public static void replayGame(){
        List<GameMove> gameMovesList = XmlUtils.readGameMovesFromXml();
        AtomicInteger i = new AtomicInteger(0);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
            GameMove gameMove = gameMovesList.get(i.get());
            int playerTurn = gameMove.getPlayerTurn();
            if (gameMove.getRole().equals("settler")) {
                PuertoRicoController.playersList[playerTurn].getPlantationState()[gameMove.getPositionX()][gameMove.getPositionY()].setText(gameMove.getButtonChangedText());
            }
            else if(gameMove.getRole().equals("builder")){
                PuertoRicoController.playersList[playerTurn].getBuildingState()[gameMove.getPositionX()][gameMove.getPositionY()].setText(gameMove.getButtonChangedText());
            }
            else if(gameMove.getRole().equals("mayor") && gameMove.getButtonChangedText().equals("radiobutton")){
                PuertoRicoController.playersList[playerTurn].getBuildingColonistState()[gameMove.getPositionX()][gameMove.getPositionY()].setSelected(gameMove.getSelected());
                PuertoRicoController.playersList[playerTurn].getBuildingColonistState()[gameMove.getPositionX()][gameMove.getPositionY()].setVisible(true);
            }
            else if(gameMove.getRole().equals("mayor") && !gameMove.getButtonChangedText().equals("radiobutton")){
                PuertoRicoController.playersList[playerTurn].getPlantationState()[gameMove.getPositionX()][gameMove.getPositionY()].setText(gameMove.getButtonChangedText());
            }
            i.set(i.get() + 1);
        }));
        timeline.setCycleCount(gameMovesList.size());
        timeline.playFromStart();
    }
}
