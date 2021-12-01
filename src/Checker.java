import java.awt.*;

public class Checker {
    int xPos;
    int yPos;
    int player;

    //Default Constructor
    public void Checker() {
        xPos = 0;
        yPos = 0;
        player = 1;
    }

    //Constructor Method
    public void Checker(int xPos, int yPos, int player) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.player = player;
    }

    //Getter Methods
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
    public void setxPos(int xPos) {this.xPos = xPos;}
    public void setyPos(int yPos) {
        this.yPos = yPos;
    }
    public void setPlayer(int player) {
        this.player = player;
    }


    public void moveChecker(int checkerNum) {

    }
}
