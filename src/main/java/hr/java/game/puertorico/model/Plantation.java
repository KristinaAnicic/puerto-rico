package hr.java.game.puertorico.model;

import javafx.scene.control.Button;
import javafx.scene.text.Text;

import static hr.java.game.puertorico.model.Constants.PLANTATION_COLUMNS;
import static hr.java.game.puertorico.model.Constants.PLANTATION_ROWS;
import static hr.java.game.puertorico.model.GameSetup.numOfQuarryTiles;
import static hr.java.game.puertorico.model.GameSetup.randomPlantationPicker;
import static hr.java.game.puertorico.util.DialogUtils.confirmationDialogHacienda;

public class Plantation {
    public static boolean placePlantation(Player p, Button plantationToPlace, Text gameColonists){
        for (int i = 0; i < PLANTATION_COLUMNS; i++) {
            for (int j = 0; j < PLANTATION_ROWS; j++) {
                if (p.getPlantationState()[i][j].getText().isEmpty()) {
                    p.getPlantationState()[i][j].setText(plantationToPlace.getText());
                    String[] lines = plantationToPlace.getText().split("\n");
                    p.getPlantationState()[i][j].setId(lines[0]);

                    if (plantationToPlace.getText().startsWith("Quarry")) {
                        p.setDidPlayerTakeQuarry(true);
                        numOfQuarryTiles--;
                    }
                    plantationToPlace.setId("chosen");
                    plantationToPlace.setDisable(true);
                    hospiceBonus(gameColonists,p,i,j);
                    return true;
                }
            }
        }
        return false;
    }

    public static void hospiceBonus(Text gameColonists, Player p, int row, int column){
        int colonistBankValue = Integer.parseInt(gameColonists.getText());
        if(p.buildingKeyValueMap.get("hospice") & colonistBankValue > 0 & !p.isHaciendaBonusUsed()) {
            p.increaseColonists(gameColonists, 1);
            p.addRemovePlantColonist(p.getPlantationState()[row][column]);
        }
    }

    public static boolean haciendaBonus(Player p, Text colonistBank){
        if(p.buildingKeyValueMap.get("hacienda")) {
            if (confirmationDialogHacienda().equals("yes")) {
                String randomTile = randomPlantationPicker();
                Button haciendaPlantation = new Button();
                haciendaPlantation.setText(randomTile + "\n\n〇");
                p.setHaciendaBonusUsed(placePlantation(p, haciendaPlantation, colonistBank));
                return p.isHaciendaBonusUsed();
            }
        }
        return false;
    }
}
