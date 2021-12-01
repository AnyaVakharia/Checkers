import java.awt.*;

public class WorkingCheckers {
    public static void main(String[] args) {

        // Set the canvas size and scale
        StdDraw.setCanvasSize(500, 500);
        StdDraw.setScale(-0.01, 8.01);

        int [][] board = new int[8][8];
        drawBoard(board);
        int player = 1;
        Checker[] clist = new Checker[24];
        for (int i = 0; i < clist.length; i++) {
            clist[i] = new Checker();
        }

        //Initializing Player 1's checkers
        for (int i = 0; i < 4; i++) {
            clist[i].setColor(Color.red);
            clist[i].setxPos(2 * i);
            clist[i].setyPos(0);
            clist[i].setPlayer(1);
        }
        for (int i = 4; i < 8; i++) {
            clist[i].setColor(Color.red);
            clist[i].setxPos((2 * i) - 7);
            clist[i].setyPos(0);
            clist[i].setPlayer(1);
        }
        for (int i = 8; i < 12; i++) {
            clist[i].setColor(Color.red);
            clist[i].setxPos((2 * i) - 16);
            clist[i].setyPos(0);
            clist[i].setPlayer(1);
        }

        //Initializing Player2's checkers
        for (int i = 12; i < 16; i++) {
            clist[i].setColor(Color.black);
            clist[i].setxPos((2 * i) - 24);
            clist[i].setyPos(5);
            clist[i].setPlayer(2);
        }
        for (int i = 16; i < 20; i++) {
            clist[i].setColor(Color.black);
            clist[i].setxPos((2 * i) - 35);
            clist[i].setyPos(6);
            clist[i].setPlayer(2);
        }
        for (int i = 20; i < 24; i++) {
            clist[i].setColor(Color.black);
            clist[i].setxPos((2 * i) - 48);
            clist[i].setyPos(7);
            clist[i].setPlayer(2);
        }
    }
    public static void drawBoard(int[][] board) {
        StdDraw.setPenColor(StdDraw.BLACK);
        for (int j = 0; j < 65; j++) {
            StdDraw.line(0, j, 8, j);
            StdDraw.line(j, 0, j, 8);
            StdDraw.filledSquare(j+0.5, j +0.5, 0.5);
            StdDraw.filledSquare(j+0.5, j +2.5, 0.5);
            StdDraw.filledSquare(j+0.5, j +4.5, 0.5);
            StdDraw.filledSquare(j+0.5, j +6.5, 0.5);
            StdDraw.filledSquare(j+0.5, j -1.5, 0.5);
            StdDraw.filledSquare(j+0.5, j -3.5, 0.5);
            StdDraw.filledSquare(j+0.5, j -5.5, 0.5);
        }

    }
}
