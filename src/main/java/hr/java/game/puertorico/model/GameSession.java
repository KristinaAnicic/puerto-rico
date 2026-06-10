package hr.java.game.puertorico.model;

import hr.java.game.puertorico.PuertoRico;
import hr.java.game.puertorico.chat.ChatService;
import hr.java.game.puertorico.jndi.ConfigurationReader;
import hr.java.game.puertorico.thread.GetLastGameMoveThread;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.util.Duration;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class GameSession {
    private ChatService stub;
    private Label lastGameMoveLabel;
    private TextArea chatTextArea;

    public GameSession(Label lastGameMoveLabel, TextArea chatTextArea) {
        this.lastGameMoveLabel = lastGameMoveLabel;
        this.chatTextArea = chatTextArea;
    }
    public ChatService setupChatThread() {
        if (!PuertoRico.playerName.name().equals(PlayerName.SINGLE_PLAYER.name())) {
            try {
                Integer rmiPort = Integer.parseInt(ConfigurationReader.getValue(ConfigurationKey.RMI_PORT));
                String serverName = ConfigurationReader.getValue(ConfigurationKey.RMI_HOST);
                String remoteObjectName = ConfigurationReader.getValue(ConfigurationKey.REMOTE_OBJECT_NAME);

                Registry registry = LocateRegistry.getRegistry(serverName, rmiPort);
                stub = (ChatService) registry.lookup(remoteObjectName);
            } catch (RemoteException | NotBoundException e) {
                throw new RuntimeException(e);
            }

            Timeline chatRefreshTimeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> refreshChatTextArea()));
            chatRefreshTimeline.setCycleCount(Animation.INDEFINITE);
            chatRefreshTimeline.playFromStart();
        }
        return stub;
    }

    public void setupGameMoveThread(){
        Timeline gameMoveTimeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            Platform.runLater(new GetLastGameMoveThread(lastGameMoveLabel));
        }));
        gameMoveTimeline.setCycleCount(Animation.INDEFINITE);
        gameMoveTimeline.playFromStart();
    }

    private void refreshChatTextArea() {
        List<String> chatHistory = null;
        try {
            chatHistory = stub.returnChatHistory();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        StringBuilder sb = new StringBuilder();
        for(String chat : chatHistory)
            sb.append(chat).append("\n");

        chatTextArea.setText(sb.toString());
    }
}
