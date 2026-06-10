package hr.java.game.puertorico.model;

import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;


public class Constants {
    final static public String SAVE_GAME_PATH = "saveGame/gameSave.dat";
    final static public Integer NUMOFBUILDINGS = 6;
    final static public Integer NUM_OF_BUILDING_TYPES = 15;
    final static public Integer NUM_OF_RADIO_BUTTONS_PER_BUILDING = 3;
    final static public Integer PLANTATION_ROWS = 3;
    final static public Integer PLANTATION_COLUMNS = 3;
    final static public Integer BUILDING_COLUMNS = 2;
    final static public Integer BUILDING_ROWS = 3;
    final static public Integer TRADING_HOUSE = 4;
    final static public Integer NUM_OF_GOODS = 5;
    final static public Integer NUM_OF_ROLES = 6;
    final static public Integer NUM_OF_PLANTATIONS = 7;
    public static final int numOfPlayers = 3;
    public static final Map<String, Integer> goodsPrice = new HashMap<>() {{
        put("Corn", 0);
        put("Indigo", 1);
        put("Coffee", 4);
        put("Tobacco", 3);
        put("Sugar", 2);
    }};
}
