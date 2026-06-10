package hr.java.game.puertorico.util;

import hr.java.game.puertorico.model.GameMove;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class XmlUtils {
    private static final String GAME_MOVES_XML_FILE_NAME = "xml/gameMoves.xml";
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    public static void saveGameMovesToXml(List<GameMove> gameMoves) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("GameMoves");
            doc.appendChild(rootElement);

            for (GameMove gameMove : gameMoves) {
                Element gameMoveElement = doc.createElement("GameMove");
                //rootElement.appendChild(gameMoveElement);

                Element roleElement = doc.createElement("Role");
                roleElement.setTextContent(gameMove.getRole());
                gameMoveElement.appendChild(roleElement);

                Element playerTurnElement = doc.createElement("PlayerTurn");
                playerTurnElement.setTextContent(String.valueOf(gameMove.getPlayerTurn()));
                gameMoveElement.appendChild(playerTurnElement);

                Element localDateTimeElement = doc.createElement("LocalDateTime");
                localDateTimeElement.setTextContent(gameMove.getLocalDateTime().format(dateTimeFormatter));
                gameMoveElement.appendChild(localDateTimeElement);

                if (gameMove.getRole().equals("settler") || gameMove.getRole().equals("builder") || gameMove.getRole().equals("trader")) {
                    Element buttonTextElement = doc.createElement("Text");
                    buttonTextElement.setTextContent(gameMove.getButtonChangedText());
                    gameMoveElement.appendChild(buttonTextElement);

                    if (gameMove.getButtonChangedPositionX() != null) {
                        Element buttonPositionX = doc.createElement("PositionX");
                        buttonPositionX.setTextContent(String.valueOf(gameMove.getButtonChangedPositionX()));
                        gameMoveElement.appendChild(buttonPositionX);
                    }

                    if (gameMove.getButtonChangedPositionY() != null) {
                        Element buttonPositionY = doc.createElement("PositionY");
                        buttonPositionY.setTextContent(String.valueOf(gameMove.getButtonChangedPositionY()));
                        gameMoveElement.appendChild(buttonPositionY);
                    }
                } else if (gameMove.getRole().equals("mayor")) {
                    Element buttonTextElement = doc.createElement("Text");
                    buttonTextElement.setTextContent(gameMove.getButtonChangedText());
                    gameMoveElement.appendChild(buttonTextElement);

                    Element buttonPositionX = doc.createElement("PositionX");
                    buttonPositionX.setTextContent(String.valueOf(gameMove.getPositionX()));
                    gameMoveElement.appendChild(buttonPositionX);

                    Element buttonPositionY = doc.createElement("PositionY");
                    buttonPositionY.setTextContent(String.valueOf(gameMove.getPositionY()));
                    gameMoveElement.appendChild(buttonPositionY);

                    Element buttonSelected = doc.createElement("Selected");
                    buttonSelected.setTextContent(String.valueOf(gameMove.getSelected()));
                    gameMoveElement.appendChild(buttonSelected);
                }
                rootElement.appendChild(gameMoveElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileWriter(new File(GAME_MOVES_XML_FILE_NAME)));
            transformer.transform(source, result);

        } catch (ParserConfigurationException | IOException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<GameMove> readGameMovesFromXml() {
        List<GameMove> gameMoves = new ArrayList<>();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = db.parse(GAME_MOVES_XML_FILE_NAME);
            Element root = dom.getDocumentElement();
            NodeList gameMoveNodes = root.getElementsByTagName("GameMove");

            for (int i = 0; i < gameMoveNodes.getLength(); i++) {
                Node gameMoveNode = gameMoveNodes.item(i);
                if (gameMoveNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) gameMoveNode;

                    String role = el.getElementsByTagName("Role").item(0).getTextContent();
                    Integer playerTurn = Integer.parseInt(el.getElementsByTagName("PlayerTurn").item(0).getTextContent());
                    LocalDateTime localDateTime = LocalDateTime.parse(el.getElementsByTagName("LocalDateTime").item(0).getTextContent(), dateTimeFormatter);

                    Integer positionX = null;
                    Integer positionY = null;
                    String buttonText = null;
                    Boolean selected = null;

                    Node positionXNode = el.getElementsByTagName("PositionX").item(0);
                    if (positionXNode != null && !"null".equals(positionXNode.getTextContent())) {
                        positionX = Integer.parseInt(positionXNode.getTextContent());
                    }

                    Node positionYNode = el.getElementsByTagName("PositionY").item(0);
                    if (positionYNode != null && !"null".equals(positionYNode.getTextContent())) {
                        positionY = Integer.parseInt(positionYNode.getTextContent());
                    }

                    Node textNode = el.getElementsByTagName("Text").item(0);
                    if (textNode != null) {
                        buttonText = textNode.getTextContent();
                    }

                    if ("mayor".equals(role)) {
                        Node selectedNode = el.getElementsByTagName("Selected").item(0);
                        if (selectedNode != null && !"null".equals(selectedNode.getTextContent())) {
                            selected = Boolean.parseBoolean(selectedNode.getTextContent());
                        }
                    }

                    GameMove gameMove = new GameMove();
                    gameMove.setRole(role);
                    gameMove.setPlayerTurn(playerTurn);
                    gameMove.setLocalDateTime(localDateTime);
                    gameMove.setButtonChangedText(buttonText);
                    gameMove.setPositionX(positionX);
                    gameMove.setPositionY(positionY);
                    gameMove.setSelected(selected);

                    if(positionY != null && positionX != null)
                        gameMoves.add(gameMove);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }

        return gameMoves;
    }
}

