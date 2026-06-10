package hr.java.game.puertorico.util;

import hr.java.game.puertorico.PuertoRico;
import hr.java.game.puertorico.PuertoRicoController;
import hr.java.game.puertorico.model.Player;
import hr.java.game.puertorico.model.PlayerName;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static hr.java.game.puertorico.PuertoRicoController.*;
import static hr.java.game.puertorico.PuertoRicoController.playersList;
import static hr.java.game.puertorico.model.Building.addVictoryPointsOfBuildings;
import static hr.java.game.puertorico.model.Constants.*;
import static hr.java.game.puertorico.model.Constants.PLANTATION_ROWS;
import static hr.java.game.puertorico.model.Craftsman.numberOfPlayerGoods;
import static hr.java.game.puertorico.model.GameSetup.numOfQuarryTiles;
import static hr.java.game.puertorico.model.Globals.*;

public class GameUtils {
    public static void resetUsedRolesAndAddBonuses(Button[] roles, Text[] rolesBonusDoubloons){
        for (Button r:roles){
            if (r.isDisabled())
                r.setDisable(false);
            else{
                int setBonus = rolesBonus.get(r.getId())+1;
                rolesBonus.replace(r.getId(),setBonus);
                for (Text roleBonus:rolesBonusDoubloons){
                    if (roleBonus.getId().equals(r.getId())) {
                        roleBonus.setText(Integer.toString(setBonus));
                        break;
                    }
                }
            }
        }
        usedRoles.clear();
    }

    public static void resetTraderShip(Button[] tradeBtns){
        for (Button t:tradeBtns) {
            t.setText("");
            t.setId("");
        }
    }

    public static void resetCaptainShip(Map<Button, TextField[]> buttonToShipMap){
        for (Button ship : buttonToShipMap.keySet()) {
            ship.setText("???");
            for (TextField s : buttonToShipMap.get(ship)) {
                s.setText("");
                s.setId("");
            }
        }
    }

    public static void disablePlayerColonistButtons(Player p){
        for (int i = 0; i < NUMOFBUILDINGS; i++) {
            for (int j = 0; j < NUM_OF_RADIO_BUTTONS_PER_BUILDING; j++) {
                p.getBuildingColonistState()[i][j].setDisable(true);
            }
        }
        for (int i = 0; i < PLANTATION_COLUMNS; i++){
            for (int j = 0; j < PLANTATION_ROWS; j++){
                p.getPlantationState()[i][j].setDisable(true);
            }
        }
    }

    public static void disableAllButtons(){
        if(!PuertoRico.playerName.name().equals(PlayerName.SINGLE_PLAYER.name())) {
            for (Player p : PuertoRicoController.playersList) {
                p.disableAllButtons();
            }
            for (Button plantation : PuertoRicoController.plantations)
                plantation.setDisable(true);

            for (Button role : PuertoRicoController.roles)
                role.setDisable(true);

            for (Button ship : PuertoRicoController.buttonToShipMap.keySet())
                ship.setDisable(true);

            for (Button building : PuertoRicoController.buildingsList) {
                building.setDisable(true);
            }
            PuertoRicoController.publicEndTurnBtn.setDisable(true);
            PuertoRicoController.publicQuarryBtn.setDisable(true);
        }
    }

    public static void activateButtons(){
        if(PuertoRico.playerName.ordinal() != playerTurn && !PuertoRico.playerName.name().equals(PlayerName.SINGLE_PLAYER.name())) {
            GameUtils.disableAllButtons();
            return;
        }

        publicQuarryBtn.setDisable(!((playersList[playerTurn].isBuildingOccupied("construction_hut") ||
                playerTurn == governor) && numOfQuarryTiles > 1 && !playersList[playerTurn].isDidPlayerTakeQuarry()));

        publicEndTurnBtn.setDisable(role.isEmpty());
        for (int i = 0; i < plantations.length; i++) {
            plantations[i].setDisable(plantations[i].getId().equals("chosen"));
        }
        for (int i = 0; i < roles.length; i++) {
            String role = roles[i].getId().replace("Btn","");
            roles[i].setDisable(usedRoles.contains(role));
        }
        for(Button building : buildingsList)
            building.setDisable(false);

        for (Button ship : PuertoRicoController.buttonToShipMap.keySet())
            ship.setDisable(false);

        playersList[playerTurn].enableButtons(role);
    }

    public static void showWinners(){
        int maxVP = addVictoryPointsOfBuildings(playersList);
        List<Player> winners = new ArrayList<>();
        List<Player> nonWinners = new ArrayList<>();
        for (Player p : playersList) {
            if (p.getVPIntegerValue() == maxVP) {
                winners.add(p);
            }
            else
                nonWinners.add(p);
        }
        StringBuilder winnerText = new StringBuilder();
        if (winners.size() > 1) {
            List<Player> tieBreakerWinners = tieBreaker(winners);
            if (tieBreakerWinners.size() > 1){
                winnerText.append("Winners are: ");
                for(Player p : tieBreakerWinners)
                    winnerText.append(p.playerName).append(", with VP: ").append(p.getVPIntegerValue())
                            .append(" and doubloons+goods: ").append(p.getDoubloonsValue() + numberOfPlayerGoods(p)).append("\n");
            }

            else {
                Player p = tieBreakerWinners.getFirst();
                winnerText.append("The winner is ").append(p.getPlayerName()).append(", with VP: ").append(p.getVPIntegerValue())
                        .append(" and doubloons+goods: ").append(p.getDoubloonsValue() + numberOfPlayerGoods(p)).append("\n");
            }

        } else {
            Player winner = winners.get(0);
            winnerText.append("The winner is ").append(winner.getPlayerName()).append(" with ")
                    .append(winner.getVPIntegerValue() + " VP!");
        }
        DialogUtils.winnerAnnouncement(winnerText.toString());
        //gameRestart();
    }
    public static List<Player> tieBreaker(List<Player> winners) {
        List<Player> tieBreakerWinners = new ArrayList<>();
        int maxValue = 0;
        for (Player p : winners) {
            int playerGoods = numberOfPlayerGoods(p);
            int totalValue = playerGoods + p.getDoubloonsValue();

            if (totalValue > maxValue) {
                maxValue = totalValue;
                tieBreakerWinners.clear();
                tieBreakerWinners.add(p);
            } else if (totalValue == maxValue) {
                tieBreakerWinners.add(p);
            }
        }
        return tieBreakerWinners;
    }
}
