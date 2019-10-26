import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Collections;

public class GameBoard extends Canvas implements Runnable{
    public static final int WIDTH = 900; //Konstanter är static final
    public static final int HEIGHT = WIDTH / 16 *10; //höjd och bredd på game är konstant
    private static final int SCALE = 2;
    private final String TITLE = "Tron";
    private boolean running = false;
    private Thread thread;
    private ArrayList <Player> inPlay =new ArrayList<>();
    private Player p = new Player();
    private Player p2 = new Player();
    private Player pAi = new Player();
    private Menu menu;
    private boolean clear = true;
    private Tile[][] gameArea = new Tile[WIDTH][HEIGHT];
    private int deadCount = 0;

    public enum STATE{
        MENU,
        GAME,
        HIGHSCORE
    }

    public static STATE state = STATE.MENU;

    public static void main (String[]args){
        System.setProperty("sun.java2d.opengl", "True");
        GameBoard game = new GameBoard();

        game.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE)); //Hanterar fönstret som vi spelar inuti.
        game.setMaximumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));   //Maximum/ minimum och preferd är samma => Fönstrets storlek kan inte ändras
        game.setMinimumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));

        JFrame frame = new JFrame(game.TITLE); //JFrame som vi kan spela i
        frame.add(game); //addar canvas till frame
        frame.pack();                          //Pack sätter delarna relativt till varandras + fönstrets storlek Som att använda setSize och setBounds
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        game.start();
    }
    public void run(){ //Hjärtat aka GameLoop
        pAi.setBot();
        for(int i=0;i<gameArea.length; i++){
            for(int j=0;j<gameArea[i].length;j++){
                gameArea[i][j] = new Tile(i,j);
            }
        }

        menu = new Menu();
        addKeyListener(new KeyInput(this));
        addMouseListener(new KeyInput(this));

        long lastTime = System.nanoTime();
        final double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        int updates = 0;
        long timer = System.currentTimeMillis();

        while (running){
            long now = System.nanoTime();
            delta += (now-lastTime) / ns;
            lastTime = now;

            if(delta >= 3){
                tick(); // Tick kallar på spelarnas tick
                updates++;
                delta-=1.8;
                render();
            }
            if (System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                System.out.println(updates + "Ticks");
                updates = 0;
            }
        }
        stop();
    }
    private void tick (){
        HighScore high = new HighScore();
        if(state == STATE.GAME) {
            for(Player x : inPlay) {
                Tile tile;
                if(!x.getIsDead()){
                    try {
                        tile = gameArea[x.getX()][x.getY()];
                        if(deadCount == inPlay.size()-1){
                            for(int i=0;i<gameArea.length; i++){
                                for(int j=0;j<gameArea[i].length;j++){
                                    Tile t = gameArea[i][j];
                                    t.clear();
                                }
                            }
                            x.win();
                            clear = true;
                            deadCount = 0;
                            menu.hideWinner();
                            if(x.getPoint() ==3){
                                menu.showWinner(x);
                                String name = JOptionPane.showInputDialog(this, "Namn på spelare" +x.pNumber()+"?");
                                high.save(name);
                                GameBoard.state = STATE.MENU;
                                x.zeroPoint();
                            }else {
                                for (Player y : inPlay){
                                    y.start();
                                }
                            }
                        }
                        else if (tile.isOwned()) {
                            x.kill();
                            deadCount++;
                        }else {
                            tile.setTileOwner();
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                    x.tick(gameArea);
                }
            }
        }
    }

    private void render() throws NullPointerException{
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null){
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        Graphics2D g2 = (Graphics2D) g;
        ///////////////////////////
        if(state == STATE.GAME) {
            if(clear){
                g2.clearRect(0, 0,GameBoard.WIDTH*2, GameBoard.HEIGHT*2);
                clear = false;
            }
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, 10, HEIGHT * SCALE);
            g2.fillRect((WIDTH * SCALE) - 10, 0, 10, HEIGHT * SCALE);
            g2.fillRect(0, 0, WIDTH * SCALE, 10);
            g2.fillRect(0, HEIGHT * SCALE - 10, WIDTH * SCALE, 10);
            for (Player x : inPlay) {
                x.render(g2);
            }
            ///////////////////////////
        }else if (state == STATE.MENU){
            if(clear){
                g2.clearRect(0, 0,GameBoard.WIDTH*2, GameBoard.HEIGHT*2);
                clear = false;
            }
            menu.render(g);
            clear = true;
        }else if (state == STATE.HIGHSCORE){
            int count = 0;
            HighScore dispHigh = new HighScore();
            dispHigh.sort();
            g2.clearRect(0, 0,GameBoard.WIDTH*2, GameBoard.HEIGHT*2);
            Font fnt = new Font("comic sans", Font.BOLD, 50);
            g2.setFont(fnt);
            g2.drawString("Highscore", GameBoard.WIDTH-150,150);
            g2.drawString("Namn:                   Score:", GameBoard.WIDTH-375, 250);
            Font fnt1 = new Font("comic sans", Font.BOLD, 30);
            g2.setFont(fnt1);
            Collections.reverse(dispHigh.scoreList);
            for(int i = 0; i<dispHigh.scoreList.size();i++){
                int mellanrum = i*100;
                g2.drawString(dispHigh.scoreList.get(i).getName(),GameBoard.WIDTH-375, 350+mellanrum);
                g2.drawString(dispHigh.scoreList.get(i).getScore()+ " Vinster",GameBoard.WIDTH+150, 350+mellanrum);
                count++;
                if(count == 5){
                    i = dispHigh.scoreList.size();
                }
            }
            Rectangle backToMenu = new Rectangle(GameBoard.WIDTH -700, 225, 100, 50);
            g2.draw(backToMenu);
            g2.drawString("Back",GameBoard.WIDTH-690,260);
        }
        g2.dispose();
        bs.show();
    }

    public void mousePressed(MouseEvent mouseEvent) {
        int mx = mouseEvent.getX();
        int my = mouseEvent.getY();

        if(GameBoard.state == STATE.MENU) {
            if (mx >= GameBoard.WIDTH - 475 && mx <= GameBoard.WIDTH - 275) {
                if (my <= 325 && my >= 225){
                    inPlay.clear();
                    inPlay.add(p);
                    inPlay.add(p2);
                    p.setColor(menu.getColor1());
                    p2.setColor(menu.getColor2());
                    for(Player q: inPlay){
                        q.start();
                    }
                    GameBoard.state = STATE.GAME; //PvP
                } else if (my <= 525 && my >= 425) {
                    menu.clickedChange1(); //Color1
                    p.setColor(menu.getColor1());
                }
                else if(my <= 725 && my >= 625){
                    System.out.println("Du klickade på Highscore");
                    GameBoard.state = STATE.HIGHSCORE;
                }
            }
            if (mx >= GameBoard.WIDTH + 100 && mx <= GameBoard.WIDTH + 300) {
                if (my <= 325 && my >= 225) {
                    inPlay.clear();
                    inPlay.add(p);
                    inPlay.add(pAi);
                    p.setColor(menu.getColor1());
                    pAi.setColor(menu.getColor2());
                    p.start();
                    pAi.start();
                    GameBoard.state = GameBoard.state.GAME;//PvE
                } else if (my <= 525 && my >= 425) { //Color2
                    menu.clickedChange2();
                    p2.setColor(menu.getColor2());
                } else if (my <= 725 && my >= 625) {
                    int a = JOptionPane.showConfirmDialog(this, "Är du säker på att du vill avsluta?");
                    if(a==0){
                        System.exit(0);
                    }else{

                    }
                }
            }
        }
        if(GameBoard.state == STATE.HIGHSCORE){
            if(mx>=200 && mx <=300){
                if(my>=225 && my <= 275){
                    state = STATE.MENU;
                }
            }
        }
    }

    public void keyPressed (KeyEvent e){
        int key = e.getKeyCode();
        switch (key){
            case KeyEvent.VK_RIGHT:
                p2.setDirection((byte) 1);
                break;
            case KeyEvent.VK_LEFT:
                p2.setDirection((byte) 2);
                break;
            case KeyEvent.VK_UP:
                p2.setDirection((byte) 3);
                break;
            case KeyEvent.VK_DOWN:
                p2.setDirection((byte) 4);
                break;
            case KeyEvent.VK_D: //Höger
                p.setDirection((byte) 1);
                break;
            case KeyEvent.VK_A: //Vänster
                p.setDirection((byte) 2);
                break;
            case KeyEvent.VK_W://Upp
                p.setDirection((byte) 3);
                break;
            case KeyEvent.VK_S:
                p.setDirection((byte) 4);
                break;
        }
    }

    /** Start och stop tillhör hanteringen av threads.*/
    private synchronized void start (){
        if(!running){
            running = true;
            thread = new Thread(this);
            thread.start();
        }
    }
    private synchronized void stop(){
        if(running){
            running = false;
            try {
                thread.join(); //Har Interrupted Exception (Osannolikt)
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
            System.exit(1);
        }
    }
}