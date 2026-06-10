package hr.java.game.puertorico.thread;

import hr.java.game.puertorico.model.GameMove;
import javafx.scene.control.Label;

import java.time.format.DateTimeFormatter;

public class GetLastGameMoveThread extends GameMoveThread implements Runnable{

    private Label label;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    public GetLastGameMoveThread(Label label){
        this.label = label;
    }

    @Override
    public void run() {
        GameMove lastGameMove = getLastGameMoveFromFile();

        StringBuilder text = new StringBuilder("Last game move: ");
        text.append(lastGameMove.getButtonType());

        Integer positionX = lastGameMove.getPositionX();
        Integer positionY = lastGameMove.getPositionY();

        if (positionX != null && positionX != -1) {
            text.append(" (Button ").append(positionX+1);

            if (positionY != null && positionY != -1) {
                text.append(", ").append(positionY+1);
            }
            text.append(")");
        }

        text.append(", ").append(lastGameMove.getLocalDateTime().format(dateTimeFormatter));

        label.setText(text.toString());

        /*label.setText("Last game move: "
                + lastGameMove.getButtonValue() + " "
                + lastGameMove.getPositionX() + ", "
                + lastGameMove.getPositionY() + ", "
                + lastGameMove.getLocalDateTime());*/
    }
}
