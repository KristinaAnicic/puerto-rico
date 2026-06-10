package hr.java.game.puertorico;


import hr.java.game.puertorico.chat.ChatService;
import hr.java.game.puertorico.model.*;
import hr.java.game.puertorico.util.GameUtils;
import hr.java.game.puertorico.util.DocumentationUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.lang.reflect.Field;
import java.rmi.RemoteException;
import java.util.*;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import lombok.Data;

import static hr.java.game.puertorico.model.Captain.clickedShip;
import static hr.java.game.puertorico.model.Constants.*;
import static hr.java.game.puertorico.model.Globals.*;
import static hr.java.game.puertorico.model.GameSetup.*;
import static hr.java.game.puertorico.tcp.Client.sendGameState;
import static hr.java.game.puertorico.util.GameUtils.activateButtons;
import static hr.java.game.puertorico.util.DialogUtils.*;
import static hr.java.game.puertorico.util.GameMoveUtils.saveGameMove;

@Data
public class PuertoRicoController{
    @FXML
    private Button settlerBtn, builderBtn, mayorBtn, craftsmanBtn, traderBtn, captainBtn, prospectorBtn, endTurnBtn;
    //BUILDINGS-------------------------
    @FXML
    private Button smallIndigoBtn, smallSugarBtn, smallMarketBtn, haciendaBtn, constructionBtn, smallWarehouseBtn;
    @FXML
    private Button indigoBtn, sugarBtn, hospiceBtn, officeBtn, largeMarketBtn, largeWarehouseBtn;
    @FXML
    private Button tobaccoBtn, coffeeBtn, factoryBtn, universityBtn, harborBtn, wharfBtn;
    //PLANTATIONS-------------------------
    @FXML
    private Button plantationBtn0, plantationBtn1, plantationBtn2, plantationBtn3, plantationBtn4, plantationBtn5, plantationBtn6;
    @FXML private Button quarryBtn;

    //PLAYER1-------------------------------
    @FXML
    private Button P1BuildingBtn00, P1BuildingBtn01, P1BuildingBtn02, P1BuildingBtn03, P1BuildingBtn10, P1BuildingBtn11, P1BuildingBtn12, P1BuildingBtn13, P1BuildingBtn20, P1BuildingBtn21, P1BuildingBtn22, P1BuildingBtn23;
    @FXML
    private Button P1PlantationBtn00, P1PlantationBtn01, P1PlantationBtn02, P1PlantationBtn10, P1PlantationBtn11, P1PlantationBtn12, P1PlantationBtn20, P1PlantationBtn21, P1PlantationBtn22, P1PlantationBtn30, P1PlantationBtn31, P1PlantationBtn32;
    //PLAYER2-------------------------------
    @FXML
    private Button P2BuildingBtn00, P2BuildingBtn01, P2BuildingBtn02, P2BuildingBtn03, P2BuildingBtn10, P2BuildingBtn11, P2BuildingBtn12, P2BuildingBtn13, P2BuildingBtn20, P2BuildingBtn21, P2BuildingBtn22, P2BuildingBtn23;
    @FXML
    private Button P2PlantationBtn00, P2PlantationBtn01, P2PlantationBtn02, P2PlantationBtn10, P2PlantationBtn11, P2PlantationBtn12, P2PlantationBtn20, P2PlantationBtn21, P2PlantationBtn22, P2PlantationBtn30, P2PlantationBtn31, P2PlantationBtn32;
    //PLAYER3-------------------------------
    @FXML
    private Button P3BuildingBtn00, P3BuildingBtn01, P3BuildingBtn02, P3BuildingBtn03, P3BuildingBtn10, P3BuildingBtn11, P3BuildingBtn12, P3BuildingBtn13, P3BuildingBtn20, P3BuildingBtn21, P3BuildingBtn22, P3BuildingBtn23;
    @FXML
    private Button P3PlantationBtn00, P3PlantationBtn01, P3PlantationBtn02, P3PlantationBtn10, P3PlantationBtn11, P3PlantationBtn12, P3PlantationBtn20, P3PlantationBtn21, P3PlantationBtn22, P3PlantationBtn30, P3PlantationBtn31, P3PlantationBtn32;

    //BUILDING 1
    @FXML private RadioButton P1RadioBtn11, P1RadioBtn12, P1RadioBtn13, P2RadioBtn11, P2RadioBtn12, P2RadioBtn13, P3RadioBtn11, P3RadioBtn12, P3RadioBtn13/*, P4RadioBtn11, P4RadioBtn12, P4RadioBtn13, P5RadioBtn11, P5RadioBtn12, P5RadioBtn13*/;
    //BUILDING 2
    @FXML private RadioButton P1RadioBtn21, P1RadioBtn22, P1RadioBtn23, P2RadioBtn21, P2RadioBtn22, P2RadioBtn23, P3RadioBtn21, P3RadioBtn22, P3RadioBtn23/*, P4RadioBtn21, P4RadioBtn22, P4RadioBtn23, P5RadioBtn21, P5RadioBtn22, P5RadioBtn23*/;
    //BUILDING 3
    @FXML private RadioButton P1RadioBtn31, P1RadioBtn32, P1RadioBtn33, P2RadioBtn31, P2RadioBtn32, P2RadioBtn33, P3RadioBtn31, P3RadioBtn32, P3RadioBtn33/*, P4RadioBtn31, P4RadioBtn32, P4RadioBtn33, P5RadioBtn31, P5RadioBtn32, P5RadioBtn33*/;
    //BUILDING 4
    @FXML private RadioButton P1RadioBtn41, P1RadioBtn42, P1RadioBtn43, P2RadioBtn41, P2RadioBtn42, P2RadioBtn43, P3RadioBtn41, P3RadioBtn42, P3RadioBtn43/*, P4RadioBtn41, P4RadioBtn42, P4RadioBtn43, P5RadioBtn41, P5RadioBtn42, P5RadioBtn43*/;
    //BUILDING 5
    @FXML private RadioButton P1RadioBtn51, P1RadioBtn52, P1RadioBtn53, P2RadioBtn51, P2RadioBtn52, P2RadioBtn53, P3RadioBtn51, P3RadioBtn52, P3RadioBtn53/*, P4RadioBtn51, P4RadioBtn52, P4RadioBtn53, P5RadioBtn51, P5RadioBtn52, P5RadioBtn53*/;
    //BUILDING 6
    @FXML private RadioButton P1RadioBtn61, P1RadioBtn62, P1RadioBtn63, P2RadioBtn61, P2RadioBtn62, P2RadioBtn63, P3RadioBtn61, P3RadioBtn62, P3RadioBtn63/*, P4RadioBtn61, P4RadioBtn62, P4RadioBtn63, P5RadioBtn61, P5RadioBtn62, P5RadioBtn63*/;
    //BUILDING 7
    @FXML private RadioButton P1RadioBtn71, P1RadioBtn72, P1RadioBtn73, P2RadioBtn71, P2RadioBtn72, P2RadioBtn73, P3RadioBtn71, P3RadioBtn72, P3RadioBtn73/*, P4RadioBtn71, P4RadioBtn72, P4RadioBtn73, P5RadioBtn71, P5RadioBtn72, P5RadioBtn73*/;
    //BUILDING 8
    @FXML private RadioButton P1RadioBtn81, P1RadioBtn82, P1RadioBtn83, P2RadioBtn81, P2RadioBtn82, P2RadioBtn83, P3RadioBtn81, P3RadioBtn82, P3RadioBtn83/*, P4RadioBtn81, P4RadioBtn82, P4RadioBtn83, P5RadioBtn81, P5RadioBtn82, P5RadioBtn83*/;
    //BUILDING 9
    @FXML private RadioButton P1RadioBtn91, P1RadioBtn92, P1RadioBtn93, P2RadioBtn91, P2RadioBtn92, P2RadioBtn93, P3RadioBtn91, P3RadioBtn92, P3RadioBtn93/*, P4RadioBtn91, P4RadioBtn92, P4RadioBtn93, P5RadioBtn91, P5RadioBtn92, P5RadioBtn93*/;

    //PLANTATION COLONIST
    @FXML private Button P1CornBtn, P1IndigoBtn, P1TobaccoBtn, P1SugarBtn, P1CoffeeBtn;
    @FXML private Button P2CornBtn, P2IndigoBtn, P2TobaccoBtn, P2SugarBtn, P2CoffeeBtn;
    @FXML private Button P3CornBtn, P3IndigoBtn, P3TobaccoBtn, P3SugarBtn, P3CoffeeBtn;

    @FXML private Text doubloonsBank, VPBank, colonistsBank, playerTurnText, turnInfoText, colonistShip;
    @FXML private Text settlerBonusDoubloon, builderBonusDoubloon, mayorBonusDoubloon, craftsmanBonusDoubloon, traderBonusDoubloon, captainBonusDoubloon;
    @FXML private Text P1Doubloons, P1VP, P1Colonists, P2Doubloons, P2VP, P2Colonists, P3Doubloons, P3VP, P3Colonists, P4Doubloons, P4VP, P4Colonists, P5Doubloons, P5VP, P5Colonists;
    @FXML private ImageView P1Governor,P2Governor,P3Governor,P4Governor,P5Governor;
    @FXML private TextField cornStock, indigoStock, sugarStock, coffeeStock, tobaccoStock;
    @FXML private Button tradeBtn1, tradeBtn2, tradeBtn3, tradeBtn4;
    @FXML private Button ship1Btn, ship2Btn, ship3Btn, wharfShipBtn, noShipBtn;
    @FXML private TextField ship1good0, ship1good1, ship1good2, ship1good3, ship1good4, ship1good5;
    @FXML private TextField ship2good0, ship2good1, ship2good2, ship2good3, ship2good4, ship2good5, ship2good6;
    @FXML private TextField ship3good0, ship3good1, ship3good2, ship3good3, ship3good4, ship3good5, ship3good6, ship3good7;
    @FXML private Text P1smWarehouseType, P1lgWarehouseType1, P1lgWarehouseType2, P2smWarehouseType, P2lgWarehouseType1, P2lgWarehouseType2, P3smWarehouseType, P3lgWarehouseType1, P3lgWarehouseType2/*, P4smWarehouseType, P4lgWarehouseType1, P4lgWarehouseType2, P5smWarehouseType, P5lgWarehouseType1, P5lgWarehouseType2*/;
    @FXML private Button P1smWarehouseNum, P1lgWarehouseNum1, P1lgWarehouseNum2, P2smWarehouseNum, P2lgWarehouseNum1, P2lgWarehouseNum2, P3smWarehouseNum, P3lgWarehouseNum1, P3lgWarehouseNum2/*, P4smWarehouseNum, P4lgWarehouseNum1, P4lgWarehouseNum2, P5smWarehouseNum, P5lgWarehouseNum1, P5lgWarehouseNum2*/;
    @FXML private Pane wharfPane;
    @FXML private TextField chatMessageTextField;
    @FXML private TextArea chatTextArea;
    @FXML private Button sendMessageBtn;
    @FXML private Label lastGameMoveLabel;

    public static ImageView[] governorImage;
    private static Button[][] P1buildingState, P2buildingState, P3buildingState, P4buildingState, P5buildingState;
    private static Button[][] P1plantationState, P2plantationState, P3plantationState, P4plantationState, P5plantationState;
    public static Button[] P1goods, P2goods, P3goods, P4goods, P5goods, tradeBtns;
    private static TextField[] ship1, ship2, ship3;
    private static RadioButton[][] P1BuildingColonistState, P2BuildingColonistState, P3BuildingColonistState, P4BuildingColonistState, P5BuildingColonistState;
    private static Text playerDoubloons[], playerVP[], playerColonists[];

    private Player player1, player2, player3, player4, player5;
    public static Player[] playersList;
    public static Button[] buildingsList;
    public static Button publicQuarryBtn, publicEndTurnBtn;
    public static Text publicColonistShip, publicPlayerTurnText, publicTurnInfoText;
    public static Button[] roles;
    public static Button[] plantations, warehouseBtn1, warehouseBtn2,warehouseBtn3, warehouseBtn4, warehouseBtn5;
    private Text[] warehouseText1, warehouseText2,warehouseText3, warehouseText4, warehouseText5;
    public static Map<String, Text> bank;
    public static Map<String, TextField> stockOfGoods;
    public static Text[] rolesBonusDoubloons;
    public static List<String> usedRoles = new ArrayList<>();;
    public static Map<Button, TextField[]> buttonToShipMap;
    public static List<GameMove> gameMoves = new ArrayList<>();

    private TurnManager turnManager;
    private GameState gameState;
    private static ChatService stub;
    private static GameSession gameSession;
    private static GameFileManager gameFileManager;

    @FXML
    public void initialize() {
        roles = new Button[NUM_OF_ROLES];
        roles[0] = settlerBtn;
        roles[1] = builderBtn;
        roles[2] = mayorBtn;
        roles[3] = craftsmanBtn;
        roles[4] = traderBtn;
        roles[5] = captainBtn;

        rolesBonusDoubloons = new Text[NUM_OF_ROLES];
        rolesBonusDoubloons[0] = settlerBonusDoubloon;
        rolesBonusDoubloons[1] = builderBonusDoubloon;
        rolesBonusDoubloons[2] = mayorBonusDoubloon;
        rolesBonusDoubloons[3] = craftsmanBonusDoubloon;
        rolesBonusDoubloons[4] = traderBonusDoubloon;
        rolesBonusDoubloons[5] = captainBonusDoubloon;

        buildingsList = new Button[NUM_OF_BUILDING_TYPES];
        buildingsList[0] = smallIndigoBtn;
        buildingsList[1] = smallSugarBtn;
        buildingsList[2] = smallMarketBtn;
        buildingsList[3] = haciendaBtn;
        buildingsList[4] = constructionBtn;
        buildingsList[5] = indigoBtn;
        buildingsList[6] = sugarBtn;
        buildingsList[7] = hospiceBtn;
        buildingsList[8] = officeBtn;
        buildingsList[9] = largeMarketBtn;
        buildingsList[10] = tobaccoBtn;
        buildingsList[11] = coffeeBtn;
        buildingsList[12] = factoryBtn;
        buildingsList[13] = universityBtn;
        buildingsList[14] = harborBtn;

        bank = new HashMap<> ();
        bank.put(BankMapKeys.VP.getBankString(), VPBank);
        bank.put(BankMapKeys.DOUBLOONS.getBankString(), doubloonsBank);
        bank.put(BankMapKeys.COLONISTS.getBankString(), colonistsBank);

        stockOfGoods = new HashMap<> ();
        stockOfGoods.put("Coffee", coffeeStock);
        stockOfGoods.put("Tobacco", tobaccoStock);
        stockOfGoods.put("Corn", cornStock);
        stockOfGoods.put("Sugar", sugarStock);
        stockOfGoods.put("Indigo", indigoStock);

        plantations = new Button[7];
        for (int i = 0; i < (fastGameMultiplier * numOfPlayers) + 1; i++){
            plantations[i] = getElementByID("plantationBtn" + i, Button.class);
        }

        tradeBtns = new Button[TRADING_HOUSE];
        for (int i = 0; i < 4; i++) tradeBtns[i] = getElementByID("tradeBtn" + (i+1), Button.class);

        governorImage = new ImageView[numOfPlayers];
        for (int i = 0; i < numOfPlayers; i++) governorImage[i] = getElementByID("P" + (i+1) + "Governor", ImageView.class);

        ship1 = new TextField[4];
        ship2 = new TextField[5];
        ship3 = new TextField[6];

        for (int i = 0; i<4; i++) ship1[i] = getElementByID("ship1good" + i, TextField.class);
        for (int i = 0; i<5; i++) ship2[i] = getElementByID("ship2good" + i, TextField.class);
        for (int i = 0; i<6; i++) ship3[i] = getElementByID("ship3good" + i, TextField.class);

        buttonToShipMap = new LinkedHashMap<>();
        buttonToShipMap.put(ship1Btn, ship1);
        buttonToShipMap.put(ship2Btn, ship2);
        buttonToShipMap.put(ship3Btn, ship3);

        playerDoubloons = new Text[numOfPlayers];
        playerVP = new Text[numOfPlayers];
        playerColonists = new Text[numOfPlayers];
        for (int i = 0; i < numOfPlayers; i++){
            playerVP[i] = getElementByID("P"+(i+1)+"VP", Text.class);
            playerDoubloons[i] = getElementByID("P"+(i+1)+"Doubloons", Text.class);
            playerColonists[i] = getElementByID("P"+(i+1)+"Colonists", Text.class);
        }

        P1buildingState = new Button[BUILDING_ROWS][BUILDING_COLUMNS]; P2buildingState = new Button[BUILDING_ROWS][BUILDING_COLUMNS];
        P3buildingState = new Button[BUILDING_ROWS][BUILDING_COLUMNS];

        for (int i = 0; i < BUILDING_ROWS; i++) {
            for (int j = 0; j < BUILDING_COLUMNS; j++) {
                P1buildingState[i][j] = getElementByID("P1BuildingBtn" + (i) + (j), Button.class);
                P2buildingState[i][j] = getElementByID("P2BuildingBtn" + (i) + (j), Button.class);
                P3buildingState[i][j] = getElementByID("P3BuildingBtn" + (i) + (j), Button.class);
            }
        }

        P1plantationState = new Button[PLANTATION_COLUMNS][PLANTATION_ROWS]; P2plantationState = new Button[PLANTATION_COLUMNS][PLANTATION_ROWS];
        P3plantationState = new Button[PLANTATION_COLUMNS][PLANTATION_ROWS];

        for (int i = 0; i < PLANTATION_COLUMNS; i++) {
            for (int j = 0; j < PLANTATION_ROWS; j++) {
                P1plantationState[i][j] = getElementByID("P1PlantationBtn" + (i) + (j), Button.class);
                P2plantationState[i][j] = getElementByID("P2PlantationBtn" + (i) + (j), Button.class);
                P3plantationState[i][j] = getElementByID("P3PlantationBtn" + (i) + (j), Button.class);
            }
        }
        P1BuildingColonistState = new RadioButton[NUMOFBUILDINGS][3]; P2BuildingColonistState = new RadioButton[NUMOFBUILDINGS][3];
        P3BuildingColonistState = new RadioButton[NUMOFBUILDINGS][3];

        for (int i = 0; i < NUMOFBUILDINGS; i++) {
            for (int j = 0; j < NUM_OF_RADIO_BUTTONS_PER_BUILDING; j++) {
                P1BuildingColonistState[i][j] = getElementByID("P1RadioBtn" + (i + 1) + (j + 1), RadioButton.class);
                P2BuildingColonistState[i][j] = getElementByID("P2RadioBtn" + (i + 1) + (j + 1), RadioButton.class);
                P3BuildingColonistState[i][j] = getElementByID("P3RadioBtn" + (i + 1) + (j + 1), RadioButton.class);
            }
        }

        String[] goodType = new String[]{"Corn", "Indigo", "Tobacco", "Sugar", "Coffee"};
        P1goods = new Button[NUM_OF_GOODS];
        P2goods = new Button[NUM_OF_GOODS];
        P3goods = new Button[NUM_OF_GOODS];
        for (int i = 0; i < goodType.length; i++){
            P1goods[i] = getElementByID("P1"+goodType[i]+"Btn", Button.class);
            P2goods[i] = getElementByID("P2"+goodType[i]+"Btn", Button.class);
            P3goods[i] = getElementByID("P3"+goodType[i]+"Btn", Button.class);
        }
        publicQuarryBtn = quarryBtn;
        publicEndTurnBtn = endTurnBtn;
        publicColonistShip = colonistShip;
        publicPlayerTurnText = playerTurnText;
        publicTurnInfoText = turnInfoText;

        player1 = new Player("1",P1Doubloons, P1VP, P1Colonists, P1buildingState, P1plantationState, P1BuildingColonistState, P1goods);
        player2 = new Player("2",P2Doubloons, P2VP, P2Colonists, P2buildingState, P2plantationState, P2BuildingColonistState, P2goods);
        player3 = new Player("3",P3Doubloons, P3VP, P3Colonists, P3buildingState, P3plantationState, P3BuildingColonistState, P3goods);
        playersList = new Player[]{player1, player2, player3/*, player4, player5*/};

        governor = initializeGame(numOfPlayers, plantations, playersList, bank, colonistShip, stockOfGoods/*, ship1, ship2, ship3*/);
        playerTurn = governor;
        governorImage[governor].setVisible(true);
        gameEnd = false;
        endRound = true;
        playerTurnText.setText("Player "+ (governor+1));
        for (int i = 0 ; i< numOfPlayers; i++)
            playersList[i].setValues(numOfPlayers);

        turnManager = new TurnManager(rolesBonusDoubloons, roles, plantations, playersList, bank, buttonToShipMap, tradeBtns,
                governorImage, colonistShip, playerTurnText, turnInfoText, quarryBtn, endTurnBtn);

        gameFileManager = new GameFileManager(playersList, endRound, gameEnd, governorImage, stockOfGoods,
                rolesBonusDoubloons, roles, tradeBtns, plantations, quarryBtn, endTurnBtn, buttonToShipMap,
                bank, turnInfoText, playerTurnText, colonistShip);

        activateButtons();

        gameSession = new GameSession(lastGameMoveLabel, chatTextArea);
        stub = gameSession.setupChatThread();
        gameSession.setupGameMoveThread();
    }

    private <T> T getElementByID(String id, Class<T> elementType) {
        try {
            Field field = getClass().getDeclaredField(id);
            return elementType.cast(field.get(this));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }
    public void buildingClicked(Event event){
        if (event.getSource() instanceof Button b) {
            if(role.equals("builder") && numOfBuildingsAndPlantationsAPlayerCanSelect > 0) {
                Text gameColonist = Integer.parseInt(colonistsBank.getText()) > 0 ? colonistsBank : colonistShip;
                if(playersList[playerTurn].setBuilding(b, gameColonist)) {
                    numOfBuildingsAndPlantationsAPlayerCanSelect--;
                    if (!playersList[playerTurn].availableBuildingSpace())
                        gameEnd = true;

                    sendGameState();
                    saveGameMove(b,role);
                }
            }
            else if (role.equals("builder")){
                cantSelectMoreAlert(fastGameMultiplier, "building");
            }
        }
    }
    public void plantationClicked(Event event){
        if (event.getSource() instanceof Button b && !gameEnd) {
            if(role.equals("settler") && numOfBuildingsAndPlantationsAPlayerCanSelect > 0) {
                Text gameColonist = Integer.parseInt(colonistsBank.getText()) > 0 ? colonistsBank : colonistShip;
                if (playersList[playerTurn].setPlantation(b, gameColonist)){
                    numOfBuildingsAndPlantationsAPlayerCanSelect--;
                    sendGameState();
                    saveGameMove(b,role);
                }
            }
            else if (role.equals("settler"))
                cantSelectMoreAlert(fastGameMultiplier, "plantation");
        }
    }
    public void addRemoveColonist(Event event){
        if(role.equals("mayor")) {
            if (event.getSource() instanceof RadioButton rb){
                playersList[playerTurn].addRemoveBuildColonist(rb);
                saveGameMove(rb,role);
            }
            else if (event.getSource() instanceof Button b){
                playersList[playerTurn].addRemovePlantColonist(b);
                saveGameMove(b,role);
            }
            sendGameState();
        }
    }

    public void endPlayerTurn(Event event){
        if(!role.equals("craftsman") && event.getSource() instanceof Button b) {
            if(turnManager.nextTurn()) {
                sendGameState();
                GameUtils.disableAllButtons();
                saveGameMove(b,role);
            }
        }
    }

    public void goodClicked(Event event){
        if(role.equals("trader") && event.getSource() instanceof Button b && !gameEnd) {
            if(playersList[playerTurn].sellGoodTrader(b, tradeBtns, doubloonsBank)){
                saveGameMove(b,role);
                sendGameState();
            }
        }
    }
    public void chooseShip(Event event) {
        if (role.equals("captain") && event.getSource() instanceof Button b) {
            gameEnd = clickedShip(b, playersList[playerTurn], buttonToShipMap, VPBank, playerTurnText, noShipBtn);
            saveGameMove(b,role);
            sendGameState();
        }
    }

    public void startNewGame(Event event){
        turnManager.gameRestart();
        sendGameState();
        if (PuertoRico.playerName.ordinal() != playerTurn)
            GameUtils.disableAllButtons();
    }
    public void saveGame(){
        gameFileManager.saveGame();
    }
    public void loadGame(){
        gameFileManager.loadSavedGame();
        sendGameState();
    }
    public void generateDocumentation(){
        DocumentationUtils.generateHtmlDocumentation();
    }

    public void roleClicked(Event event){
        Roles roleFunctions = new Roles(role, playersList, numOfPlayers, governor, turnInfoText, bank);
        if (event.getSource() instanceof Button b && endRound && playerTurn == governor && !gameEnd){
            roleFunctions.roleClicked(b, quarryBtn, colonistShip, buttonToShipMap, playerTurnText);
            if (b.getId().equals("craftsmanBtn")){
                turnManager.endRound();
                GameUtils.disableAllButtons();
            }
            saveGameMove(b,"");
            sendGameState();
        }
    }

    public void sendChatMessage(){
        String chatMessage = chatMessageTextField.getText();
        String playerName = PuertoRico.playerName.name();
        try {
            stub.sendChatMessage(playerName + ": " + chatMessage);
            chatMessageTextField.setText("");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
    public void replayGame(){
        GameFileManager.replayGame();
    }
}