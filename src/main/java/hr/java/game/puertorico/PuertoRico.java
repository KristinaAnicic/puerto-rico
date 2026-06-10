package hr.java.game.puertorico;

import hr.java.game.puertorico.exception.WrongPlayerNameException;
import hr.java.game.puertorico.jndi.ConfigurationReader;
import hr.java.game.puertorico.model.PlayerName;
import hr.java.game.puertorico.thread.PlayerOneServerThread;
import hr.java.game.puertorico.thread.PlayerThreeServerThread;
import hr.java.game.puertorico.thread.PlayerTwoServerThread;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PuertoRico extends Application {
    public static PlayerName playerName;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PuertoRico.class.getResource("puerto-rico.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle(playerName.name());
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            args = new String[]{"SINGLE_PLAYER"};
        }

        String firstArgument = args[0];

        if (PlayerName.valueOf(firstArgument).equals(PlayerName.PLAYER_ONE)) {
            playerName = PlayerName.PLAYER_ONE;
            Thread serverStarter = new Thread(new PlayerOneServerThread());
            serverStarter.start();
        }
        else if(PlayerName.valueOf(firstArgument).equals(PlayerName.PLAYER_TWO)) {
            playerName = PlayerName.PLAYER_TWO;
            Thread serverStarter = new Thread(new PlayerTwoServerThread());
            serverStarter.start();
        }
        else if(PlayerName.valueOf(firstArgument).equals(PlayerName.PLAYER_THREE)) {
            playerName = PlayerName.PLAYER_THREE;
            Thread serverStarter = new Thread(new PlayerThreeServerThread());
            serverStarter.start();
        }
        else if(PlayerName.valueOf(firstArgument).equals(PlayerName.SINGLE_PLAYER)){
            playerName = PlayerName.SINGLE_PLAYER;
        }
        else {
            throw new WrongPlayerNameException("The game was started with the player name: " +
                    firstArgument + ", but only PLAYER_ONE, PLAYER_TWO, PLAYER_THREE and SINGLE_PLAYER are supported");
        }
        launch();
    }
}