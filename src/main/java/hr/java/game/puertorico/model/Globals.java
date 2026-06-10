package hr.java.game.puertorico.model;

import java.util.HashMap;
import java.util.Map;


public class Globals {
    public static String role = "";
    public static int playerTurn = 0;
    public static int governor;
    public static int fastGameMultiplier = 2;
    public static boolean gameEnd, endRound;
    public static int numOfBuildingsAndPlantationsAPlayerCanSelect = fastGameMultiplier;

    public static Map<String, Integer> rolesBonus = new HashMap<>() {{
        put("settlerBtn", 0);
        put("builderBtn", 0);
        put("mayorBtn", 0);
        put("craftsmanBtn", 0);
        put("traderBtn", 0);
        put("captainBtn", 0);
        put("prospectorBtn", 0);
    }};
}
