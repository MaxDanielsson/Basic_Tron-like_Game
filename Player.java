import java.awt.*;

public class Player{
    private static int numberOfPlayers = 0;
    private int x = 0;
    private int y = 0;
    public Color color = Color.black;
    private int spelareNr;
    private byte direction;
    private boolean isDead;
    private int points = 0;
    private int speed = 1;
    private boolean spedUp = false;
    private boolean bot = false;
    private Tile[][] gameArea;

    public Player(){
        this.spelareNr = numberOfPlayers+1;
        numberOfPlayers++;
    }
    public void setColor(Color newColor){
        this.color = newColor;
    } //Returns a color

    public void move(){
        switch (direction){
            case (byte)1: //Öst
                x+= speed;
                if(getSpeed()){
                    x+=speed;
                }
                break;
            case (byte)2: //Väst
                x-= speed;
                if(getSpeed()){
                    x-=speed;
                }
                break;
            case (byte)3://Norr
                y-= speed;
                if(getSpeed()){
                    y-=speed;
                }
                break;
            case (byte)4://Syd
                y+= speed;
                if(getSpeed()){
                    y+=speed;
                }
                break;
        }
        borderPassing();
    }

    public void borderPassing(){
        if(y>110 && direction==(byte)4){
            y=1;
        }
        if(y<1 && direction==(byte)3){
            y=110;
        }
        if(x>178 && direction==(byte)1){
            x=1;
        }
        if(x<1 && direction==(byte)2){
            x=178;
        }
    }

    public void moveAi(){
        switch (direction){
            case (byte)1:
                if(!gameArea[x+1][y].isOwned()){ //Vi kollar tile brevid spelaren till höger
                    x+=speed;
                    direction=(byte)1;
                }else{
                    if(!gameArea[x][y+1].isOwned() && gameArea[x][y-1].isOwned()){
                        y+=speed;
                        direction=(byte)4;
                    }else if(gameArea[x][y+1].isOwned() && !gameArea[x][y-1].isOwned()){
                        y-=speed;
                        direction=(byte)3;
                    }else{
                        double a = Math.round(Math.random());
                        if(a==0){
                            y+=speed;
                            direction=(byte)4;
                        }else if(!gameArea[x][y+1].isOwned() && !gameArea[x][y-1].isOwned()){
                            y-=speed;
                            direction=(byte)3;
                        }
                    }
                }
                break;
            case (byte)2:
                if(!gameArea[x-1][y].isOwned() && x>=2){
                    System.out.println(gameArea[x-1][y].isOwned());
                    x-=speed;
                    direction=(byte)2;
                }else{
                    if(!gameArea[x][y+1].isOwned() && gameArea[x][y-1].isOwned()){
                        y+=speed;
                        direction=(byte)4;
                    }else if(gameArea[x][y+1].isOwned() && !gameArea[x][y-1].isOwned()){
                        y-=speed;
                        direction=(byte)3;
                    }else{
                        double a = Math.round(Math.random());
                        if(a==0){
                            y+=speed;
                            direction=(byte)4;
                        }else if(!gameArea[x][y+1].isOwned() && !gameArea[x][y-1].isOwned()){
                            y-=speed;
                            direction=(byte)3;
                        }
                    }
                }
                break;
            case (byte)3:
                if(!gameArea[x][y-1].isOwned() && y>=2){
                    y-=speed;
                    direction=(byte)3;
                }else{
                    if(!gameArea[x+1][y].isOwned() && gameArea[x-1][y].isOwned()){
                        x+=speed;
                        direction=(byte)1;
                    }else if(gameArea[x+1][y].isOwned() && !gameArea[x-1][y].isOwned()){
                        x-=speed;
                        direction=(byte)2;
                    }else if(!gameArea[x+1][y].isOwned() && !gameArea[x-1][y].isOwned()){
                        double a = Math.round(Math.random());
                        if(a==0){
                            x+=speed;
                            direction=(byte)1;
                        }else{
                            x-=speed;
                            direction=(byte)2;
                        }
                    }
                }
                break;
            case (byte)4:
                if(!gameArea[x][y+1].isOwned()){
                    y+=speed;
                    direction=(byte)4;
                }else{
                    if(!gameArea[x+1][y].isOwned() && gameArea[x-1][y].isOwned()){
                        x+=speed;
                        direction=(byte)1;
                    }else if(gameArea[x+1][y].isOwned() && !gameArea[x-1][y].isOwned()){
                        x-=speed;
                        direction=(byte)2;
                    }else if(!gameArea[x+1][y].isOwned() && !gameArea[x-1][y].isOwned()){
                        double a = Math.round(Math.random());
                        if(a==0){
                            x+=speed;
                            direction=(byte)1;
                        }else{
                            x-=speed;
                            direction=(byte)2;
                        }
                    }
                }
                break;
        }
        borderPassing();
    }

    public void setBot(){
        bot = true;
    }

    public boolean isBot(){
        return bot;
    }

    public void setDirection(byte newDirection){
        if(direction == 1 || direction == 2){
            if (newDirection !=1 && newDirection != 2){
                this.direction = newDirection;
            }
        }else {
            if(newDirection != 3 && newDirection !=4){
                this.direction = newDirection;
            }
        }
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

    public boolean getSpeed(){
        return spedUp;
    }

    public void tick(Tile[][] gameArea){
        this.gameArea = gameArea;
        if(!isDead) {
            if(bot){
                moveAi();
            }
            else{
                move();
            }
        }
    }
    public void render(Graphics g){
        g.setColor(color);
        g.fillRect(x*10, y*10, 10,10);
    }
    public void kill(){
        isDead = true;
    }
    public void unKill(){isDead = false;}

    public boolean getIsDead(){
        return isDead;
    }

    public int getPoint(){
        return points;
    }
    public void zeroPoint(){points =0;}

    public int pNumber (){
        return spelareNr;
    }

    public void win(){
        points++;
    }

    public void start(){
        if(spelareNr ==1){
            x = 25;
            y = 50;
            direction = 1;
        }else{
            x = 150;
            y = 50;
            direction = 2;
        }
        unKill();
    }
}