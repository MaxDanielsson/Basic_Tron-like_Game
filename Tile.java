
public class Tile {

    private boolean owned =false;
    private int xPos;
    private int yPos;

    public Tile(int xPos, int yPos){
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public boolean isOwned(){
        return owned;
    }

    public void setTileOwner(){
        owned = true;
    }
    public void clear(){
        owned = false;
    }
}

