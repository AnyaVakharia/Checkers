public class WorkingCheckers {
    public static void main(String[] args) {

        // Set the canvas size and scale
        StdDraw.setCanvasSize(500, 500);
        StdDraw.setScale(0.01, 8.01);

        int [][] board = new int[8][8];
        drawBoard(board);

        //test

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
