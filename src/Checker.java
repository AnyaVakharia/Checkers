import java.awt.*;

public class Checker {
    int xPos;
    int yPos;
    Color color;
    int player;

    //Default Constructor
    public void Checker() {
        xPos = 0;
        yPos = 0;
        color = Color.red;
        player = 1;
    }

    //Constructor Method
    public void Checker(Color color, int xPos, int yPos, int player) {
        this.color = color;
        this.xPos = xPos;
        this.yPos = yPos;
        this.player = player;
    }

    //Getter Methods
    public Color getColor() {
        return color;
    }
    public int getxPos() {
        return xPos;
    }
    public int getyPos() {
        return yPos;
    }
    public int getPlayer() {
        return player;
    }

    //Setter Methods
    public void setColor(Color color) {
        this.color = color;
    }
    public void setxPos(int xPos) {
        this.xPos = xPos;
    }
    public void setyPos(int yPos) {
        this.yPos = yPos;
    }
    public void setPlayer(int player) {
        this.player = player;
    }


    public void moveChecker(int checkerNum) {

    }
}
