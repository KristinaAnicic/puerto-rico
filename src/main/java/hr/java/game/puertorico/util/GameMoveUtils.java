package hr.java.game.puertorico.util;

import hr.java.game.puertorico.PuertoRicoController;
import hr.java.game.puertorico.model.GameMove;
import hr.java.game.puertorico.thread.SaveNewGameMoveThread;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.RadioButton;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static hr.java.game.puertorico.PuertoRicoController.gameMoves;
import static hr.java.game.puertorico.model.Building.buildingType;
import static hr.java.game.puertorico.model.Constants.*;
import static hr.java.game.puertorico.model.Globals.playerTurn;
import static hr.java.game.puertorico.util.DialogUtils.successDialog;

public class GameMoveUtils {

    private static final String GAME_MOVE_HISTORY_FILE_NAME = "gameMoves/gameMoves.dat";
    private static List<GameMove> gameMoveList = new ArrayList<>();
    private static Labeled b;

    private static File getFile() {
        File file = new File(GAME_MOVE_HISTORY_FILE_NAME);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return file;
    }

    public static void saveNewGameMove(GameMove newGameMove){
        gameMoveList.add(newGameMove);
        File file = getFile();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file)))
        {
            oos.writeObject(gameMoveList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static GameMove getLastGameMove(){
        File file = getFile();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file)))
        {
            gameMoveList = (List<GameMove>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return gameMoveList.getLast();
    }

    public static void saveGameMove(Labeled b, String role){
        GameMove newGameMove = new GameMove();
        newGameMove.setLocalDateTime(LocalDateTime.now());
        newGameMove.setRole(role);
        newGameMove.setPlayerTurn(playerTurn);
        newGameMove.setSelected(false);

        if(role.equals("builder")) {
            newGameMove.setButtonType(buildingType(b.getText()));
            newGameMove.setPositionY(-1);
            for (int i = 0; i < PuertoRicoController.buildingsList.length; i++) {
                if (PuertoRicoController.buildingsList[i].equals(b)) {
                    newGameMove.setPositionX(i);
                    break;
                }
            }
            for(int i = 0; i < BUILDING_COLUMNS; i++){
                for(int j = 0; j < BUILDING_ROWS; j++){
                    if(PuertoRicoController.playersList[playerTurn].getBuildingState()[j][i].getText().equals(b.getText())) {
                        newGameMove.setButtonChangedText(b.getText());
                        newGameMove.setButtonChangedPositionX(j);
                        newGameMove.setButtonChangedPositionY(i);
                        break;
                    }
                }
            }
        }
        else if(role.equals("settler")){
            newGameMove.setPositionY(-1);
            String[] lines = b.getText().split("\n");
            newGameMove.setButtonType(lines[0]);
            for(int i = 0; i < PuertoRicoController.plantations.length; i++){
                if(PuertoRicoController.plantations[i].equals(b)){
                    newGameMove.setPositionX(i);
                    break;
                }
            }
            for(int i = 0; i < PLANTATION_COLUMNS; i++){
                for(int j = 0; j < PLANTATION_ROWS; j++){
                    if(PuertoRicoController.playersList[playerTurn].getPlantationState()[i][j].getText().equals(b.getText())) {
                        newGameMove.setButtonChangedText(b.getText());
                        newGameMove.setButtonChangedPositionX(i);
                        newGameMove.setButtonChangedPositionY(j);
                        break;
                    }
                }
            }
        }
        else if(role.equals("mayor") && b instanceof Button){
            newGameMove.setButtonType(b.getId());
            for(int i = 0; i < PLANTATION_COLUMNS; i++){
                for (int j = 0; j < PLANTATION_ROWS; j++){
                    if (PuertoRicoController.playersList[playerTurn].getPlantationState()[i][j].equals(b)){
                        newGameMove.setButtonChangedText(b.getText());
                        newGameMove.setPositionX(i);
                        newGameMove.setPositionY(j);
                        break;
                    }
                }
            }
        }
        else if(role.equals("mayor") && b instanceof RadioButton){
            newGameMove.setButtonType(b.getId());
            newGameMove.setButtonChangedText("radiobutton");
            for(int i = 0; i < NUMOFBUILDINGS; i++){
                for (int j = 0; j < NUM_OF_RADIO_BUTTONS_PER_BUILDING; j++){
                    if (PuertoRicoController.playersList[playerTurn].getBuildingColonistState()[i][j].equals(b)){
                        newGameMove.setPositionX(i);
                        newGameMove.setPositionY(j);
                        newGameMove.setSelected(((RadioButton) b).isSelected());
                        break;
                    }
                }
            }
        }
        else if(b.equals(PuertoRicoController.publicEndTurnBtn)){
            newGameMove.setRole("");
            newGameMove.setButtonType(b.getText());
            newGameMove.setPositionY(-1);
            newGameMove.setPositionX(-1);
        }

        else if(role.equals("trader")){
            newGameMove.setPositionY(-1);
            for(int i = 0; i < PuertoRicoController.playersList[playerTurn].getGoods().length; i++){
                if(PuertoRicoController.playersList[playerTurn].getGoods()[i].equals(b)){
                    newGameMove.setButtonType(PuertoRicoController.playersList[playerTurn].getGoods()[i].getId());
                    newGameMove.setButtonChangedText(PuertoRicoController.playersList[playerTurn].getGoods()[i].getText());
                    newGameMove.setPositionX(i);
                    newGameMove.setButtonChangedPositionX(i);
                    newGameMove.setButtonChangedPositionY(-1);
                    break;
                }
            }
        }
        else if(role.equals("captain")){
            newGameMove.setPositionY(-1);
            int i = 0;
            for(Button shipBtn : PuertoRicoController.buttonToShipMap.keySet()){
                if (shipBtn.equals(b)){
                    newGameMove.setButtonType("ship" + i);
                    newGameMove.setPositionX(i);
                    break;
                }
                i++;
            }
        }
        else if(role.isEmpty()){
            newGameMove.setPositionY(-1);
            String[] lines = b.getText().split("\n");
            newGameMove.setButtonType(lines[0]);
            for (int i = 0; i < PuertoRicoController.roles.length; i++){
                if(PuertoRicoController.roles[i].equals(b)){
                    newGameMove.setPositionX(i);
                    break;
                }
            }
        }
        gameMoves.add(newGameMove);
        XmlUtils.saveGameMovesToXml(gameMoves);

        SaveNewGameMoveThread saveNewGameMoveThread = new SaveNewGameMoveThread(newGameMove);
        Thread starter = new Thread(saveNewGameMoveThread);
        starter.start();
    }
}
