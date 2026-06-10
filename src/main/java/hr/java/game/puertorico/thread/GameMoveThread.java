package hr.java.game.puertorico.thread;

import hr.java.game.puertorico.model.GameMove;
import hr.java.game.puertorico.util.GameMoveUtils;

public abstract class GameMoveThread {
    private static Boolean gameMoveFIleAccessInProgess = false;

    protected synchronized void saveNewGameMoveToFile(GameMove gameMove){
        while(gameMoveFIleAccessInProgess){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        gameMoveFIleAccessInProgess = true;
        GameMoveUtils.saveNewGameMove(gameMove);
        gameMoveFIleAccessInProgess = false;
        notifyAll();
    }

    protected synchronized GameMove getLastGameMoveFromFile() {
        while (gameMoveFIleAccessInProgess) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        gameMoveFIleAccessInProgess = true;
        GameMove lastGameMove = GameMoveUtils.getLastGameMove();
        gameMoveFIleAccessInProgess = false;
        notifyAll();

        return lastGameMove;
    }
}
