package hr.java.game.puertorico.thread;

import hr.java.game.puertorico.PuertoRico;
import hr.java.game.puertorico.PuertoRicoController;
import hr.java.game.puertorico.jndi.ConfigurationReader;
import hr.java.game.puertorico.model.ConfigurationKey;
import hr.java.game.puertorico.model.GameFileManager;
import hr.java.game.puertorico.model.GameState;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static hr.java.game.puertorico.model.Globals.endRound;
import static hr.java.game.puertorico.model.Globals.gameEnd;

public class PlayerOneServerThread implements Runnable{

    @Override
    public void run() {
        playerOneAcceptRequests();
    }

    private static void playerOneAcceptRequests() {
        Integer playerOnePort = Integer.parseInt(ConfigurationReader.getValue(ConfigurationKey.PLAYER_ONE_SERVER_PORT));

        try (ServerSocket serverSocket = new ServerSocket(playerOnePort)){
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.err.println("Client connected from port: " + clientSocket.getPort());
                Platform.runLater(() ->  processSerializableClient(clientSocket));
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processSerializableClient(Socket clientSocket) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());){
            GameState gameState = (GameState)ois.readObject();

            GameFileManager gameFileManager = new GameFileManager(PuertoRicoController.playersList, endRound, gameEnd,
                    PuertoRicoController.governorImage, PuertoRicoController.stockOfGoods,
                    PuertoRicoController.rolesBonusDoubloons, PuertoRicoController.roles, PuertoRicoController.tradeBtns,
                    PuertoRicoController.plantations, PuertoRicoController.publicQuarryBtn,
                    PuertoRicoController.publicEndTurnBtn, PuertoRicoController.buttonToShipMap,
                    PuertoRicoController.bank, PuertoRicoController.publicTurnInfoText,
                    PuertoRicoController.publicPlayerTurnText, PuertoRicoController.publicColonistShip);

            gameFileManager.loadCurrentGame(gameState);

            System.out.println("Player 1 recieved the game state! ");
            oos.writeObject("Player one recieved the game state - confirmation");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
