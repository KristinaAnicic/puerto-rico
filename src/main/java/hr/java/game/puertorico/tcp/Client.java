package hr.java.game.puertorico.tcp;

import hr.java.game.puertorico.PuertoRico;
import hr.java.game.puertorico.PuertoRicoController;
import hr.java.game.puertorico.jndi.ConfigurationReader;
import hr.java.game.puertorico.model.ConfigurationKey;
import hr.java.game.puertorico.model.GameState;
import hr.java.game.puertorico.model.PlayerName;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static hr.java.game.puertorico.model.Globals.*;
import static hr.java.game.puertorico.model.GameSetup.discardedIslandTilesMap;
import static hr.java.game.puertorico.model.GameSetup.islandTilesMap;

public class Client {
    private static Integer playerOnePort;
    private static Integer playerTwoPort;
    private static Integer playerThreePort;
    private static String host;

    private static void initConfig() {
        if (host != null) return;

        host = ConfigurationReader.getValue(ConfigurationKey.HOST);

        playerOnePort = Integer.parseInt(
                ConfigurationReader.getValue(ConfigurationKey.PLAYER_ONE_SERVER_PORT)
        );

        playerTwoPort = Integer.parseInt(
                ConfigurationReader.getValue(ConfigurationKey.PLAYER_TWO_SERVER_PORT)
        );

        playerThreePort = Integer.parseInt(
                ConfigurationReader.getValue(ConfigurationKey.PLAYER_THREE_SERVER_PORT)
        );
    }

    private static void playerOneSendRequest(GameState gameState) {
        try {
            try (Socket clientSocket = new Socket(host, playerTwoPort)) {
                System.err.println("Client is connecting to " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
                sendSerializableRequestToPlayerTwo(clientSocket, gameState);
            }
            try (Socket clientSocket = new Socket(host, playerThreePort)) {
                System.err.println("Client is connecting to " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
                sendSerializableRequestToPlayerThree(clientSocket, gameState);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void playerTwoSendRequest(GameState gameState) {
        try {
            try (Socket clientSocket = new Socket(host, playerOnePort)) {
                System.err.println("Client is connecting to " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
                sendSerializableRequestToPlayerOne(clientSocket, gameState);
            }
            try (Socket clientSocket = new Socket(host, playerThreePort)) {
                System.err.println("Client is connecting to " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
                sendSerializableRequestToPlayerThree(clientSocket, gameState);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private static void playerThreeSendRequest(GameState gameState) {
        try {
            try (Socket clientSocket = new Socket(host, playerOnePort)) {
                System.err.println("Client is connecting to " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
                sendSerializableRequestToPlayerOne(clientSocket, gameState);
            }
            try (Socket clientSocket = new Socket(host, playerTwoPort)) {
                System.err.println("Client is connecting to " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
                sendSerializableRequestToPlayerTwo(clientSocket, gameState);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private static void sendSerializableRequestToPlayerOne(Socket client, GameState gameState) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
        oos.writeObject(gameState);
        System.out.println("Game state sent to Player one!");
    }

    private static void sendSerializableRequestToPlayerTwo(Socket client, GameState gameState) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
        oos.writeObject(gameState);
        System.out.println("Game state sent to Player two!");
    }

    private static void sendSerializableRequestToPlayerThree(Socket client, GameState gameState) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
        oos.writeObject(gameState);
        System.out.println("Game state sent to Player three!");
    }

    public static void sendGameState(){
        initConfig();

        if (PuertoRico.playerName == PlayerName.SINGLE_PLAYER) {
            return;
        }

        GameState gameState = new GameState(PuertoRicoController.playersList, role, governor, playerTurn, endRound, gameEnd,
                numOfBuildingsAndPlantationsAPlayerCanSelect, PuertoRicoController.governorImage, PuertoRicoController.bank,
                PuertoRicoController.stockOfGoods, islandTilesMap, discardedIslandTilesMap, PuertoRicoController.usedRoles,
                PuertoRicoController.rolesBonusDoubloons, PuertoRicoController.roles, rolesBonus,
                PuertoRicoController.tradeBtns, PuertoRicoController.publicQuarryBtn, PuertoRicoController.publicColonistShip,
                PuertoRicoController.buttonToShipMap, PuertoRicoController.publicEndTurnBtn,
                PuertoRicoController.publicTurnInfoText, PuertoRicoController.publicPlayerTurnText, PuertoRicoController.plantations);

        if(PuertoRico.playerName.name().equals(PlayerName.PLAYER_ONE.name()))
            playerOneSendRequest(gameState);
        else if(PuertoRico.playerName.name().equals(PlayerName.PLAYER_TWO.name()))
            playerTwoSendRequest(gameState);
        else if(PuertoRico.playerName.name().equals(PlayerName.PLAYER_THREE.name()))
            playerThreeSendRequest(gameState);
    }
}
