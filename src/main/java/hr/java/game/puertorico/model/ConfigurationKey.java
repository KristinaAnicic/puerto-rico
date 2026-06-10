package hr.java.game.puertorico.model;

import lombok.Getter;

@Getter
public enum ConfigurationKey {
    RMI_HOST("rmi.host"), RMI_PORT("rmi.port"), PLAYER_ONE_SERVER_PORT("server.player_one_port"),
    PLAYER_TWO_SERVER_PORT("server.player_two_port"), PLAYER_THREE_SERVER_PORT("server.player_three_port"),
    HOST("server.host"), REMOTE_OBJECT_NAME("rmi.remote_object_name");

    private String key;

    private ConfigurationKey(String key){
        this.key = key;
    }
}
