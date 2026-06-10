package hr.java.game.puertorico.model;

import lombok.Getter;

@Getter
public enum BankMapKeys {
    VP("VP"), DOUBLOONS("doubloons"), COLONISTS("colonists");
    private String bankString;

    private BankMapKeys(String bankString){
        this.bankString = bankString;
    }
}
