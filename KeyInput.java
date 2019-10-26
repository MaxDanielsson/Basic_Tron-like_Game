import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class KeyInput extends KeyAdapter implements MouseListener {

    GameBoard game;

    public KeyInput (GameBoard game){
        this.game = game;
    }
    public void keyPressed(KeyEvent e){
        game.keyPressed(e);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {}

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
       game.mousePressed(mouseEvent);
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {}

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    @Override
    public void mouseExited(MouseEvent mouseEvent) {}
}
