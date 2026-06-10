module hr.java.game.puertorico {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.rmi;
    requires java.naming;
    requires java.xml;


    opens hr.java.game.puertorico to javafx.fxml;
    exports hr.java.game.puertorico;
    opens hr.java.game.puertorico.chat to java.rmi;
}