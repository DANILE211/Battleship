import java.io.IOException;
import java.util.Random;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BattleshipMain extends Application {

    private boolean running = false;
    private Board enemyBoard, playerBoard;
    private ComboBox<String> choiceBox;
    private String currentLvl="Easy";
    private boolean enemyTurn = false;
    private int LastX=-1;
    private int LastY=-1;
    private Random random = new Random();
    private boolean END=false;
    private BrainAI brain;
    private FXMLLoader mainLoader;
    private AnchorPane root;
    private mainPaneController controller;
    private Pane enemyPane;
    private StackPane ShipPane;
    private Text whoWon;
    private Image metal = new Image("file:resources/metal.png");
    private Image water = new Image("file:resources/water.png");
    private Image bomb = new Image("file:resources/bomb.png");
    private void setImage(Pane pane,String what){
        Image logo;
        pane.getChildren().clear();
        //if picture not found do sth
        logo=new Image("file:resources/"+what+ ".png");
        pane.getChildren().add(new ImageView(logo));
    }
    private void setImageShip(StackPane pane,Integer what){
        System.out.println("shippane");
        System.out.println(what.toString()+".gif");
        Image logo;
        pane.getChildren().clear();
        logo=new Image("file:resources/"+what.toString()+ ".gif");
        pane.getChildren().add(new ImageView(logo));
    }
    private Parent createContent() throws IOException {

        mainLoader = new FXMLLoader(getClass().getClassLoader().getResource("mainPane.fxml"));
        root = mainLoader.load();
        controller=mainLoader.getController();
        enemyPane=controller.getShipPane();
        ShipPane=controller.getEnemyPane();
        choiceBox=controller.getLevelComboBox();
        choiceBox.setItems(FXCollections.observableArrayList("Easy", "Medium", "Hard"));
        choiceBox.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, number2) -> {
            currentLvl=choiceBox.getValue();
            setImage(enemyPane,currentLvl);
            System.out.println(currentLvl);
        });
        whoWon=controller.getText();
        choiceBox.setValue("Easy");
        enemyBoard = new Board(false, event -> {
            if (!running || END)
                return;

            Board.Cell cell = (Board.Cell) event.getSource();
            if (cell.wasShot)
                return;

            cell.shoot();
            if (enemyBoard.ships == 0) {
                whoWon.setText("YOU WON!");
                if(currentLvl.equals("Hard")) whoWon.setText("YOU CHEATED!");
                END=true;
                highlightShips(enemyBoard);
            }
            if(currentLvl.equals("Easy"))
                enemyMoveEasy();
            else if(currentLvl.equals("Medium"))
                enemyMoveMedium();
            else enemyMoveHard();
        });
        playerBoard = new Board(false, event -> {
            if (running)
                return;
            Board.Cell cell = (Board.Cell) event.getSource();
            if (playerBoard.ships>0) {
                setImageShip(ShipPane, playerBoard.ships);
                playerBoard.placeShip(new Ship(playerBoard.ships), cell.x, cell.y);
            }else{
                brain=new BrainAI(playerBoard);
                startGame();
            }
        });
        VBox vbox=controller.getMainVbox();
        vbox.getChildren().add(enemyBoard);
        vbox.getChildren().add(playerBoard);
        vbox.setSpacing(20);
        vbox.setAlignment(Pos.CENTER);
        controller.setBattle(this);
        return root;
    }

    private void enemyMoveEasy() {
        int x = random.nextInt(10);
        int y = random.nextInt(10);
        Board.Cell cell = playerBoard.getCell(x, y);
        if (cell.wasShot){
            System.out.println("I double");
            return;
        }
        cell.shoot();
        if (playerBoard.ships == 0) {
            whoWon.setText("YOU LOST!");
            END=true;
            highlightShips(enemyBoard);
        }
    }
    private void highlightShips(Board board){
        Board.Cell cell;
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                cell=board.getCell(x,y);
                if(cell.ship != null)
                    cell.setFill(new ImagePattern(metal));
                if(cell.ship != null && cell.wasShot)
                    cell.setFill(new ImagePattern(bomb));
            }
        }
    }
    private Board.Cell next(Board.Cell cell){
        int x,y;
        x= cell.x;
        y= cell.y;
        Board.Cell toReturn=null;
        for (Board.Cell neighbor : playerBoard.getNeighbors(x, y)) {
            if (playerBoard.isValidPoint(neighbor.x, neighbor.y) && !neighbor.wasShot){
                toReturn=playerBoard.getCell(neighbor.x, neighbor.y);
            }
        }
        return toReturn;
    }
    private void enemyMoveMedium() {
        System.out.println("mediumEnemy");
        int x,y;
        Board.Cell cell;
        Board.Cell toHit;
        if(LastX!=-1 && LastY!=-1){
            //System.out.println("1");
            cell=playerBoard.getCell(LastX,LastY);
            toHit=next(cell);
            if(toHit!=null){
                //System.out.println("2");
                if(toHit.shoot()) {
                   // System.out.println("3");
                    LastX=toHit.x;
                    LastY=toHit.y;
                }
                System.out.println(LastX+ " " +LastY);
            }else {
                //System.out.println("4");
                LastX=-1;
                LastY=-1;
                enemyMoveMedium();
            }
        }
        else{
            //System.out.println("5");
            do{
                x = random.nextInt(10);
                y = random.nextInt(10);
                cell = playerBoard.getCell(x, y);
            }while (cell.wasShot);
            System.out.println(x+ " " +y);
            if(cell.shoot()){
                //System.out.println(cell.x+ " "+cell.y);
                //System.out.println("6");
                LastX=cell.x;
                LastY=cell.y;
            }
        }
        if (playerBoard.ships == 0) {
            whoWon.setText("YOU LOST!");
            END=true;
            highlightShips(enemyBoard);
        }
    }
    private void enemyMoveHard() {
        brain.shoot();
        if (playerBoard.ships == 0) {
            whoWon.setText("YOU LOST!");
            //System.exit(0);
            END=true;
            highlightShips(enemyBoard);
        }
    }
    private void startGame() {
        int type = 5;
        while (type > 0) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);
            if (enemyBoard.AIplaceShip(new Ship(type,Math.random() < 0.5), x, y)) {
                type--;
            }
        }
        playerBoard.ships=5;
        running = true;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Battleship");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void quit() {
        System.exit(0);
    }
    public void reset() {
        whoWon.setText("");
        END = false;
        running = false;
        playerBoard.resetBoard();
        enemyBoard.resetBoard();
    }
}
