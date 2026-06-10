package hr.java.game.puertorico.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GameMove implements Serializable {
    private String role;
    private String buttonType;
    private Integer positionX;
    private Integer positionY;
    private LocalDateTime localDateTime;
    private String buttonChangedText;
    private Integer buttonChangedPositionX;
    private Integer buttonChangedPositionY;
    private Integer playerTurn;
    private Boolean selected;
}
