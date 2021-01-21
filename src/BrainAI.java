import java.util.Random;

public class BrainAI {
    private Board myBoard;
    private int LastX;
    private int LastY;
    private boolean randomMode;
    private boolean[] tabOfShips;
    private Random random = new Random();
    private Board.Cell toHit;
    private TURN where=null;
    private int counterOfMisses;
    private enum TURN
    {
        UP,DOWN,LEFT,RIGHT;
    }
    public BrainAI(Board board){
        this.myBoard=board;
        LastX=-1;
        LastY=-1;
        randomMode=true;
        tabOfShips=new boolean[6];
        for(int i=1;i<6;i++) tabOfShips[i]=true;
        counterOfMisses=100;

    }
    private TURN giveDirect(Board.Cell src, Board.Cell dst){
        if(src.x==dst.x){
            if(src.y<dst.y) return TURN.DOWN;
            else return TURN.UP;
        }else{
            if(src.x<dst.x) return TURN.RIGHT;
            else return TURN.LEFT;
        }
    }
    private TURN oppositeDirect(TURN direction){
        if(direction==TURN.DOWN) return TURN.UP;
        if(direction==TURN.UP) return TURN.DOWN;
        if(direction==TURN.LEFT) return TURN.RIGHT;
        if(direction==TURN.RIGHT) return TURN.LEFT;
        return null;
    }
    private void didFinishedShip(Board.Cell cell){
        if(!cell.ship.isAlive()){
            tabOfShips[cell.ship.type]=false;
            System.out.println(tabOfShips[1] + " " + tabOfShips[2] + " "+ tabOfShips[3] + " "
                    + tabOfShips[4] + " "+ tabOfShips[5] + " ");
            randomMode=true;
            counterOfMisses=100;
            where=null;
        }
    }
    private boolean goodCoordinates(int x, int y){
        return (x % 2 == 0 && y % 2 == 1) || (y % 2 == 0 && x % 2 == 1);
    }
    void shoot(){
        int x,y;
        int tmpX,tmpY;
        Board.Cell cell;
        int Left,Right,Up,Dn;
        if(randomMode){
            System.out.println("randomMode");
            while (true){
                Left=0;Right=0;Up=0;Dn=0;
                boolean HorizontTest=false;
                boolean VerticalTest=false;
                boolean NeighbourTest=true;
                boolean OddEvenTest=false;
                x = random.nextInt(10);
                y = random.nextInt(10);
                cell=myBoard.getCell(x,y);
                if (cell.wasShot) continue;
                if(cell.ship!=null && cell.ship.type==1) {
                    System.out.println("hitted one");
                    OddEvenTest = true;
                }
                if(goodCoordinates(cell.x,cell.y))
                    OddEvenTest=true;
                if(!OddEvenTest) continue;
                tmpX=x-1;
                while (tmpX>=0 && !myBoard.getCell(tmpX,y).wasShot){
                    //myBoard.getCell(tmpX,y).setFill(Color.YELLOW);
                    tmpX--;
                    Left++;
                }
                tmpX=x+1;
                while (tmpX<=9 && !myBoard.getCell(tmpX,y).wasShot){
                    //myBoard.getCell(tmpX,y).setFill(Color.LIGHTGRAY);
                    tmpX++;
                    Right++;
                }
                int HorizontSize=Right+Left+1;
                System.out.println(HorizontSize);
                for(int i=1;i<6;i++){
                    if (tabOfShips[i] && HorizontSize >= i) // this ship is alive
                    {
                        HorizontTest = true;
                        System.out.println("HorizontTest passed");
                        break;
                    }
                }
                tmpY=y-1;
                while (tmpY>=0 && !myBoard.getCell(x,tmpY).wasShot){
                    //myBoard.getCell(x,tmpY).setFill(Color.YELLOW);
                    tmpY--;
                    Up++;
                }
                tmpY=y+1;
                while (tmpY<=9 && !myBoard.getCell(x,tmpY).wasShot){
                    //myBoard.getCell(x,tmpY).setFill(Color.LIGHTGRAY);
                    tmpY++;
                    Dn++;
                }
                int VerticalSize=Up+Dn+1;
                System.out.println(VerticalSize);
                for(int i=1;i<6;i++){
                    if (tabOfShips[i] && VerticalSize >= i) // this ship is alive
                    {
                        VerticalTest = true;
                        System.out.println("VerticalTest passed");
                        break;
                    }
                }
                if (!HorizontTest && !VerticalTest) continue;
                for (Board.Cell neighbor : myBoard.getNeighbors(cell.x, cell.y)) {
                    if (myBoard.isValidPoint(neighbor.x, neighbor.y)){
                        if(neighbor.wasShot && neighbor.ship != null)
                            NeighbourTest=false;
                    }
                }
                if(!NeighbourTest) continue;
                System.out.println("NeighbourTest passed");
                if(counterOfMisses==0){
                    System.out.println("went here");
                    counterOfMisses=100;
                    do{
                        x = random.nextInt(10);
                        y = random.nextInt(10);
                        cell=myBoard.getCell(x,y);
                    }while(cell.ship == null || cell.wasShot);
                    System.out.println(cell.ship.type);
                    System.out.println(cell.x + " " + cell.y);
                }

                // after these tests I can shoot
                if(cell.shoot()){
                    LastX=cell.x;
                    LastY=cell.y;
                    didFinishedShip(cell);
                    randomMode=false;
                }else {
                    System.out.println("decreased");
                    counterOfMisses--;
                }
                break;
            }
        }
        else{
            Board.Cell begin=myBoard.getCell(LastX,LastY);
            Board.Cell toHit=null;
            if(where==null){
                System.out.println("1");
                for (Board.Cell neighbor : myBoard.getNeighbors(begin.x, begin.y)) {
                    if (myBoard.isValidPoint(neighbor.x, neighbor.y)){
                        if(!neighbor.wasShot){
                            System.out.println("chosen neighbour:" + neighbor.x + " " + neighbor.y);
                            toHit=neighbor;
                            break;
                        }
                    }
                }
                if(toHit==null){
                    randomMode=true;
                    shoot();
                }
                if(toHit!=null && toHit.shoot()){
                    System.out.println("2 hitted");
                    LastX=toHit.x;
                    LastY=toHit.y;
                    where=giveDirect(begin,toHit);
                    System.out.println("where: " + where);
                    didFinishedShip(toHit);
                }
                System.out.println("after everything: lastX: " + LastX + " LastY: " + LastY);
            }else if(where==TURN.LEFT) {
                System.out.println("wanna Left: " + "=lastX: " + LastX + " LastY: " + LastY);
                boolean goOpposite = false;
                int X;
                if (myBoard.isValidPoint(LastX - 1, LastY)) {
                    if (!myBoard.getCell(LastX - 1, LastY).wasShot) {
                        if (myBoard.getCell(LastX - 1, LastY).shoot()) {
                            LastX = LastX - 1;
                            didFinishedShip(myBoard.getCell(LastX, LastY));
                        } else goOpposite = true;
                    } else goOpposite = true;
                } else goOpposite = true;
                if (goOpposite) {
                    where = oppositeDirect(where);
                    X = LastX;
                    while (myBoard.isValidPoint(X, LastY) && myBoard.getCell(X, LastY).wasShot) {
                        X++;
                    }
                    LastX = X - 1;
                    System.out.println("gone opposite from Left: " + "=lastX: " + LastX + " LastY: " + LastY);
                }
            }else if(where==TURN.RIGHT){
                System.out.println("wanna right: " + "=lastX: " + LastX + " LastY: " + LastY);
                boolean goOpposite=false;
                int X;
                if(myBoard.isValidPoint(LastX+1,LastY)){
                    if(!myBoard.getCell(LastX+1,LastY).wasShot){
                        if(myBoard.getCell(LastX+1,LastY).shoot()){
                            LastX=LastX+1;
                            didFinishedShip(myBoard.getCell(LastX,LastY));
                        }else goOpposite=true;
                    } else goOpposite = true;
                }else goOpposite=true;
                if(goOpposite){
                    where=oppositeDirect(where);
                    X=LastX;
                    while (myBoard.isValidPoint(X,LastY) && myBoard.getCell(X,LastY).wasShot){
                        X--;
                    }
                    LastX=X+1;
                    System.out.println("gone opposite from right: " + "=lastX: " + LastX + " LastY: " + LastY);
                }

            }else if(where==TURN.UP){
                boolean goOpposite=false;
                int Y;
                if(myBoard.isValidPoint(LastX,LastY-1)){
                    if(!myBoard.getCell(LastX,LastY-1).wasShot){
                        if(myBoard.getCell(LastX,LastY-1).shoot()){
                            LastY=LastY-1;
                            didFinishedShip(myBoard.getCell(LastX,LastY));
                        }else goOpposite=true;
                    } else goOpposite = true;
                }else goOpposite=true;
                if(goOpposite){
                    where=oppositeDirect(where);
                    Y=LastY;
                    while (myBoard.isValidPoint(LastX,Y) && myBoard.getCell(LastX,Y).wasShot){
                        Y++;
                    }
                    LastY=Y-1;
                }
            }else{
                boolean goOpposite=false;
                int Y;
                if(myBoard.isValidPoint(LastX,LastY+1)){
                    if(!myBoard.getCell(LastX,LastY+1).wasShot){
                        if(myBoard.getCell(LastX,LastY+1).shoot()){
                            LastY=LastY+1;
                            didFinishedShip(myBoard.getCell(LastX,LastY));
                        }else goOpposite=true;
                    } else goOpposite = true;
                }else goOpposite=true;
                if(goOpposite){
                    where=oppositeDirect(where);
                    Y=LastY;
                    while (myBoard.isValidPoint(LastX,Y) && myBoard.getCell(LastX,Y).wasShot){
                        Y--;
                    }
                    LastY=Y+1;
                }
            }
        }
    }
}