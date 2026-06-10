package hr.java.game.puertorico.thread;

import hr.java.game.puertorico.model.GameMove;
import hr.java.game.puertorico.util.GameMoveUtils;

public class SaveNewGameMoveThread extends GameMoveThread implements Runnable{

    private GameMove gameMove;
    public SaveNewGameMoveThread(GameMove newGameMove){
        this.gameMove = newGameMove;
    }

    @Override
    public void run() {
        saveNewGameMoveToFile(gameMove);
    }
}
