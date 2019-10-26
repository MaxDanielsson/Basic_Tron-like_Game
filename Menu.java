import java.awt.*;

public class Menu {

    private int count1 = 0;
    private int count2 = 0;
    private static Color[] difColors = {Color.black, Color.blue, Color.green, Color.gray, Color.pink, Color.orange};
    private boolean showWin = false;
    private Player theWinner;

    private Rectangle play2Btn = new Rectangle(GameBoard.WIDTH+100, 225, 200, 100); //X, Y, BREDD, HÖJD
    private Rectangle color2Btn = new Rectangle(GameBoard.WIDTH+100, 425, 200, 100);
    private Rectangle quitBtn = new Rectangle(GameBoard.WIDTH+100, 625, 200, 100);
    private Rectangle play1Btn = new Rectangle(GameBoard.WIDTH -475, 225, 200, 100);
    private Rectangle color1Btn = new Rectangle(GameBoard.WIDTH -475, 425, 200, 100);
    private Rectangle scoreBtn = new Rectangle(GameBoard.WIDTH -475, 625, 200, 100);
    private Rectangle colorChange1 = new Rectangle(GameBoard.WIDTH -250, 465, 25, 25);
    private Rectangle colorChange2 = new Rectangle(GameBoard.WIDTH+50, 465, 25, 25);
    private Rectangle winnerIs = new Rectangle(GameBoard.WIDTH - 115, 425, 50, 50);
    public void render (Graphics g){

        Graphics2D g2d = (Graphics2D) g;

        Font fnt = new Font("comic sans", Font.BOLD, 50);
        g.setFont(fnt);
        g.setColor(Color.black);
        g.drawString("TRON", GameBoard.WIDTH-75, 150);

        Font fnt1 = new Font("comic sans", Font.ITALIC, 25);
        g.setFont(fnt1);
        g.drawString("PvP", play1Btn.x+65,play1Btn.y+55);
        g.drawString("ColorP1", color1Btn.x+55,color1Btn.y+55);
        g.drawString("Quit", quitBtn.x+65,quitBtn.y+55);
        g.drawString("PvE", play2Btn.x+65,play2Btn.y+55);
        g.drawString("ColorP2", color2Btn.x+55,color2Btn.y+55);
        g.drawString("HighScore", scoreBtn.x+40,scoreBtn.y+55);

        if(showWin){
            g2d.draw(winnerIs);
            g.drawString("Winner", winnerIs.x - 15,winnerIs.y - 20);
            if(theWinner.isBot()){
                g.drawString("Ai",winnerIs.x +15, winnerIs.y + 35);
            }else {
                g.drawString(String.valueOf(theWinner.pNumber()),winnerIs.x +20, winnerIs.y + 35);
            }
        }

        g2d.draw(play1Btn);
        g2d.draw(play2Btn);
        g2d.draw(color1Btn);
        g2d.draw(color2Btn);
        g2d.draw(quitBtn);
        g2d.draw(scoreBtn);

        g2d.setColor(getColor1());
        g2d.fill(colorChange1);

        g2d.setColor(getColor2());
        g2d.fill(colorChange2);
    }
    public Color getColor1(){
        return difColors[count1];
    }
    public void clickedChange1(){
        count1++;
        if(count1 >= difColors.length){
            count1 = 0;
        }
    }
    public void showWinner(Player x){
        theWinner = x;
        showWin = true;
    }
    public void hideWinner(){
        showWin = false;
    }
    public Color getColor2(){
        return difColors[count2];
    }
    public void clickedChange2(){
        count2++;
        if(count2 >= difColors.length){
            count2 = 0;
        }
    }
}