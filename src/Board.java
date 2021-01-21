import java.util.ArrayList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Board extends Parent {
    private VBox rows = new VBox();
    private Image metal = new Image("file:resources/metal.png");
    private Image water = new Image("file:resources/water.png");
    private Image bomb = new Image("file:resources/bomb.png");
    private Image splash = new Image("file:resources/splash.png");
    private boolean enemy = false;
    public int ships = 5;
    private EventHandler<? super MouseEvent> MainHandler;
    public Board(boolean enemy, EventHandler<? super MouseEvent> handler) {
        this.enemy = enemy;
        this.MainHandler=handler;
        for (int y = 0; y < 10; y++) {
            HBox row = new HBox();
            for (int x = 0; x < 10; x++) {
                Cell c = new Cell(x, y, this);
                c.setOnMouseClicked(handler);
                row.getChildren().add(c);
            }

            rows.getChildren().add(row);
        }
        getChildren().add(rows);
    }
    private void reset(){
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                Cell c = getCell(x,y);
                if(c.getFill()==Color.GREENYELLOW){
                    c.setFill(new ImagePattern(water));
                    c.setStroke(Color.LIGHTBLUE);
                }
                c.setOnMouseClicked(MainHandler);
            }
        }
    }
    public void placeShip(Ship ship, int x, int y) {
        Cell cell;
        boolean start=true;
        reset();
        if(!isValidPoint(x,y)){
            start=false;
        }
        for (Cell neighbor : getNeighbors(x, y)) {
            if (neighbor.ship != null) {
                start = false;
                break;
            }
        }
        if(start){
            cell = getCell(x, y);
            cell.setFill(Color.GREENYELLOW);
            cell.setStroke(Color.GREEN);
            MarkAvailableCells(x,y,ship);

        }
    }
    private void MarkAvailableCells(int x, int y,Ship ship){
        Cell cell;
        int d = ship.type;
        boolean test=true;
        if(isValidPoint(x+d-1,y)){
            System.out.println("isValid1");
            cell = getCell(x+d-1, y);
            if (cell.ship != null){
                System.out.println("False1");
                test=false;
            }
            for(int i=1;i<d;i++) {
                for (Cell neighbor : getNeighbors(x + i, y)) {
                    if (!isValidPoint(x+i, y)){
                        System.out.println("False1");
                        test=false;
                    }

                    if (neighbor.ship != null){
                        System.out.println("False1");
                        test=false;
                    }
                }
            }
            if(test){
                System.out.println("Test Passed1");
                cell.setFill(Color.GREENYELLOW);
                cell.setStroke(Color.GREEN);
                cell.setOnMouseClicked(event -> {
                    System.out.println("I am in");
                    for (int i = x; i < x + d; i++) {
                        Cell temp = getCell(i, y);
                        temp.ship = ship;
                        if (!enemy) {
                            temp.setFill(new ImagePattern(metal));
                            temp.setStroke(Color.LIGHTBLUE);
                        }
                    }
                    ships--;
                    reset();
                });
            }
        }
        test=true;
        if(isValidPoint(x,y-d+1)){
            System.out.println("isValid3");
            cell = getCell(x, y-d+1);
            if (cell.ship != null){
                System.out.println("False3");
                test=false;
            }
            for(int i=1;i<d;i++) {
                for (Cell neighbor : getNeighbors(x, y-i)) {
                    if (!isValidPoint(x, y-i)){
                        System.out.println("False3");
                        test=false;
                    }

                    if (neighbor.ship != null){
                        System.out.println("False3");
                        test=false;
                    }
                }
            }
            if(test){
                System.out.println("Test Passed3");
                cell.setFill(Color.GREENYELLOW);
                cell.setStroke(Color.GREEN);
                cell.setOnMouseClicked(event -> {
                    System.out.println("I am in");
                    for (int i = y; i > y - d; i--) {
                        Cell temp = getCell(x, i);
                        temp.ship = ship;
                        if (!enemy) {
                            temp.setFill(new ImagePattern(metal));
                            temp.setStroke(Color.LIGHTBLUE);
                        }
                    }
                    ships--;
                    reset();
                });
            }
        }
        test=true;
        if(isValidPoint(x-d+1,y)){
            System.out.println("isValid2");
            cell = getCell(x-d+1, y);
            if (cell.ship != null){
                System.out.println("False2");
                test=false;
            }
            for(int i=1;i<d;i++) {
                for (Cell neighbor : getNeighbors(x - i, y)) {
                    if (!isValidPoint(x-i, y)){
                        System.out.println("False2");
                        test=false;
                    }

                    if (neighbor.ship != null){
                        System.out.println("False2");
                        test=false;
                    }
                }
            }
            if(test){
                System.out.println("Test Passed2");
                cell.setFill(Color.GREENYELLOW);
                cell.setStroke(Color.GREEN);
                cell.setOnMouseClicked(event -> {
                    System.out.println("I am in");
                    for (int i = x; i > x - d; i--) {
                        Cell temp = getCell(i, y);
                        temp.ship = ship;
                        if (!enemy) {
                            temp.setFill(new ImagePattern(metal));
                            temp.setStroke(Color.LIGHTBLUE);
                        }
                    }
                    ships--;
                    reset();
                });
            }
        }
        test=true;
        if(isValidPoint(x,y+d-1)){
            System.out.println("isValid4");
            cell = getCell(x, y+d-1);
            if (cell.ship != null){
                System.out.println("False4");
                test=false;
            }
            for(int i=1;i<d;i++) {
                for (Cell neighbor : getNeighbors(x, y+i)) {
                    if (!isValidPoint(x, y+i)){
                        System.out.println("False4");
                        test=false;
                    }

                    if (neighbor.ship != null){
                        System.out.println("False4");
                        test=false;
                    }
                }
            }
            if(test){
                System.out.println("Test Passed4");
                cell.setFill(Color.GREENYELLOW);
                cell.setStroke(Color.GREEN);
                cell.setOnMouseClicked(event -> {
                    System.out.println("I am in");
                    for (int i = y; i < y + d; i++) {
                        Cell temp = getCell(x, i);
                        temp.ship = ship;
                        if (!enemy) {
                            temp.setFill(new ImagePattern(metal));
                            temp.setStroke(Color.LIGHTBLUE);
                        }
                    }
                    ships--;
                    reset();
                });
            }
        }
    }
    public Cell getCell(int x, int y) {
        return (Cell)((HBox)rows.getChildren().get(y)).getChildren().get(x);
    }
    public Cell[] getNeighbors(int x, int y) {
        Point2D[] points = new Point2D[] {
                new Point2D(x - 1, y),
                new Point2D(x + 1, y),
                new Point2D(x, y - 1),
                new Point2D(x, y + 1)
        };

        List<Cell> neighbors = new ArrayList<Cell>();

        for (Point2D p : points) {
            if (isValidPoint(p)) {
                neighbors.add(getCell((int)p.getX(), (int)p.getY()));
            }
        }
        return neighbors.toArray(new Cell[0]);
    }
    public boolean isValidPoint(Point2D point) {
        return isValidPoint(point.getX(), point.getY());
    }
    public boolean isValidPoint(double x, double y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }
    private boolean AIcanPlaceShip(Ship ship, int x, int y) {
        int length = ship.type;

        if (ship.vertical) {
            for (int i = y; i < y + length; i++) {
                if (!isValidPoint(x, i))
                    return false;

                Cell cell = getCell(x, i);
                if (cell.ship != null)
                    return false;

                for (Cell neighbor : getNeighbors(x, i)) {
                    if (!isValidPoint(x, i))
                        return false;

                    if (neighbor.ship != null)
                        return false;
                }
            }
        }
        else {
            for (int i = x; i < x + length; i++) {
                if (!isValidPoint(i, y))
                    return false;

                Cell cell = getCell(i, y);
                if (cell.ship != null)
                    return false;

                for (Cell neighbor : getNeighbors(i, y)) {
                    if (!isValidPoint(i, y))
                        return false;

                    if (neighbor.ship != null)
                        return false;
                }
            }
        }

        return true;
    }
    public boolean AIplaceShip(Ship ship, int x, int y) {
        if (AIcanPlaceShip(ship, x, y)) {
            int length = ship.type;

            if (ship.vertical) {
                for (int i = y; i < y + length; i++) {
                    Cell cell = getCell(x, i);
                    cell.ship = ship;
                    if (!enemy) {
                        cell.setFill(new ImagePattern(metal));
                        cell.setStroke(Color.LIGHTBLUE);
                    }
                }
            }
            else {
                for (int i = x; i < x + length; i++) {
                    Cell cell = getCell(i, y);
                    cell.ship = ship;
                    if (!enemy) {
                        cell.setFill(new ImagePattern(metal));
                        cell.setStroke(Color.LIGHTBLUE);
                    }
                }
            }

            return true;
        }

        return false;
    }

    public void resetBoard() {
        ships=5;
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                Cell c = getCell(x,y);
                c.wasShot=false;
                c.ship=null;
                c.setFill(new ImagePattern(water));
                c.setStroke(Color.LIGHTBLUE);
                c.setOnMouseClicked(MainHandler);
            }
        }
    }

    public class Cell extends Rectangle {
        public int x, y;
        public Ship ship = null;
        public boolean wasShot = false;
        private Board board;
        public Cell(int x, int y, Board board) {
            super(30, 30);
            this.x = x;
            this.y = y;
            this.board = board;
            setFill(new ImagePattern(water));
            //setFill(Color.LIGHTBLUE);
            setStroke(Color.LIGHTBLUE);
        }
        public boolean shoot() {
            wasShot = true;
            setFill(new ImagePattern(splash));
            if (ship != null) {
                ship.hit();
                setFill(new ImagePattern(bomb));
                if (!ship.isAlive()) {
                    board.ships--;
                }
                return true;
            }

            return false;
        }
    }
}