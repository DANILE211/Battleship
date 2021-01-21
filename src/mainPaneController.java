import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class mainPaneController {
    private BattleshipMain myGame;
    @FXML
    private Text text;
    @FXML
    private VBox MainVbox;
    @FXML
    private ComboBox<String> LevelComboBox;

    @FXML
    private Button ResetButton;

    @FXML
    private Button QuitButton;

    @FXML
    private StackPane EnemyPane;

    @FXML
    private Pane ShipPane;

    @FXML
    void quit(ActionEvent event) {
        myGame.quit();
    }
    public Pane getShipPane(){
        return ShipPane;
    }
    public Text getText(){
        return text;
    }
    @FXML
    void reset(ActionEvent event) {
        myGame.reset();
    }
    public ComboBox<String> getLevelComboBox() {
        return LevelComboBox;
    }
    public VBox getMainVbox() {
        return MainVbox;
    }
    public void setBattle(BattleshipMain game){
        myGame=game;
    }
    public StackPane getEnemyPane() {
        return EnemyPane;
    }
}
