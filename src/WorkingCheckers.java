import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;


/** Description: This application allows for users to play a two player
 *          checkers game. The game begins with checkers in designated spots
 *          and from there the users are able to move their checkers one by one.
 *          They can choose which checker they want to move where, as long
 *          as it is a possible and legal move. Players can jump each other's
 *          checkers, which will ultimately lead them to winning. While playing,
 *          users are able to click the "End Game" button which will end the game
 *          and tell the players who was winning thus far. A "New Game" button
 *          is also available for user's to restart the game.
 *
 * @ author Tori Brunette, Dane Stewart, and Anya Vakharia
 */
public class WorkingCheckers extends JPanel {
    public static void main(String[] args) {
        JFrame window = new JFrame("Checkers");
        WorkingCheckers content = new WorkingCheckers();
        window.setContentPane(content);
        window.pack();
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation((screensize.width - window.getWidth()) / 2,
                (screensize.height - window.getHeight()) / 2);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setVisible(true);
    }

    // Assign all public and private variables
    private JButton newGameButton;  // Starts a new game
    private JButton resignButton;   // End a game

    private JLabel message;  // Displays the messages

    public WorkingCheckers() {

        setLayout(null);
        setPreferredSize(new Dimension(350, 250));

        setBackground(new Color(0, 150, 0));  // Background color

        // Board class
        Board board = new Board();

        add(board);
        add(newGameButton);
        add(resignButton);
        add(message);

        // Set the position and size of the board by calling the setBounds method
        board.setBounds(20, 20, 164, 164);
        newGameButton.setBounds(210, 60, 120, 30);
        resignButton.setBounds(210, 120, 120, 30);
        message.setBounds(0, 200, 350, 30);

    } // end constructor


    // --------------------  Nested Classes -------------------------------

    // This class allows for checkers to actually move during the game
    private static class CheckersMove {
        int fromRow, fromCol;  // Position
        int toRow, toCol;

        CheckersMove(int r1, int c1, int r2, int c2) {
            // Constructor!
            fromRow = r1;
            fromCol = c1;
            toRow = r2;
            toCol = c2;
        }

        // Checks if the move is a jump, meaning one checker would go away
        boolean isJump() {
            return (fromRow - toRow == 2 || fromRow - toRow == -2);
        }
    }  // end class CheckersMove.


    // This class designs the actual checkers board with the checkers on it
    private class Board extends JPanel implements ActionListener, MouseListener {


        CheckersData board;  // Data for the board and moves made on the board

        boolean gameInProgress; // Checks if game is still running

        int currentPlayer;      // Checks which player it is

        int selectedRow, selectedCol;

        CheckersMove[] legalMoves; // Array that checks the moves


        // Board method that holds the buttons printed on the board
        Board() {
            setBackground(Color.BLACK);
            addMouseListener(this);
            resignButton = new JButton("End Game");
            resignButton.addActionListener(this);
            newGameButton = new JButton("New Game");
            newGameButton.addActionListener(this);
            message = new JLabel("", JLabel.CENTER);
            message.setFont(new Font("Serif", Font.BOLD, 14));
            message.setForeground(Color.green);
            board = new CheckersData();
            doNewGame();
        }


        // Records user's clicks
        public void actionPerformed(ActionEvent evt) {
            Object src = evt.getSource();
            if (src == newGameButton)
                doNewGame();
            else if (src == resignButton)
                doResign();
        }


        // New game is created in this method
        void doNewGame() {
            if (gameInProgress == true) {
                message.setText("Finish the current game first!");
                return;
            }
            board.setUpGame();   // Sets up pieces
            currentPlayer = CheckersData.RED;   // Red checker moves first
            legalMoves = board.getLegalMoves(CheckersData.RED);  // Record Red's moves
            selectedRow = -1;
            message.setText("Red's Turn");
            gameInProgress = true;
            newGameButton.setEnabled(false);
            resignButton.setEnabled(true);
            repaint();
        }


        // This method refers to when the player wants to end the game
        void doResign() {
            if (gameInProgress == false) {
                message.setText("There is no game in progress!");
                return;
            }
            if (currentPlayer == CheckersData.RED)
                gameOver("Black Wins!");
            else
                gameOver("Red Wins!");
        }


        // The game ends when this method is used -- No moves are possible
        void gameOver(String str) {
            message.setText(str);
            newGameButton.setEnabled(true);
            resignButton.setEnabled(false);
            gameInProgress = false;
        }


        // Method that allows users to click legal positions and guides them in the process
        void doClickSquare(int row, int col) {

            // If the move is legal, the checker moves

            for (int i = 0; i < legalMoves.length; i++)
                if (legalMoves[i].fromRow == row && legalMoves[i].fromCol == col) {
                    selectedRow = row;
                    selectedCol = col;
                    if (currentPlayer == CheckersData.RED)
                        message.setText("Red's Turn");
                    else
                        message.setText("Black's Turn");
                    repaint();
                    return;
                }


            if (selectedRow < 0) {
                message.setText("Click the piece you want to move.");
                return;
            }

            // Actually move the checker if possible

            for (int i = 0; i < legalMoves.length; i++)
                if (legalMoves[i].fromRow == selectedRow && legalMoves[i].fromCol == selectedCol
                        && legalMoves[i].toRow == row && legalMoves[i].toCol == col) {
                    doMakeMove(legalMoves[i]);
                    return;
                }

            message.setText("Click the space you want to move to.");

        }  // end doClickSquare()


        // Checker is moved to the specified spot
        void doMakeMove(CheckersMove move) {

            board.makeMove(move);

            // Check if this move was a jump
            if (move.isJump()) {
                legalMoves = board.getLegalJumpsFrom(currentPlayer, move.toRow, move.toCol);
                if (legalMoves != null) {
                    if (currentPlayer == CheckersData.RED)
                        message.setText("Red, You must continue jumping.");
                    else
                        message.setText("Black, You must continue jumping.");
                    selectedRow = move.toRow;
                    selectedCol = move.toCol;
                    repaint();
                    return;
                }
            }

            if (currentPlayer == CheckersData.RED) {
                currentPlayer = CheckersData.BLACK;
                legalMoves = board.getLegalMoves(currentPlayer);
                if (legalMoves == null)
                    gameOver("Red Wins!");
                else if (legalMoves[0].isJump())
                    message.setText("Black's turn, You must jump.");
                else
                    message.setText("Black's Turn");
            } else {
                currentPlayer = CheckersData.RED;
                legalMoves = board.getLegalMoves(currentPlayer);
                if (legalMoves == null)
                    gameOver("Black Wins!");
                else if (legalMoves[0].isJump())
                    message.setText("Red's Turn, You must jump.");
                else
                    message.setText("Red's Turn");
            }

            // -1 if the player has not selected a move
            selectedRow = -1;

            if (legalMoves != null) {
                boolean sameStartSquare = true;
                for (int i = 1; i < legalMoves.length; i++)
                    if (legalMoves[i].fromRow != legalMoves[0].fromRow
                            || legalMoves[i].fromCol != legalMoves[0].fromCol) {
                        sameStartSquare = false;
                        break;
                    }
                if (sameStartSquare) {
                    selectedRow = legalMoves[0].fromRow;
                    selectedCol = legalMoves[0].fromCol;
                }
            }
            // Board is recreated
            repaint();

        }  // end doMakeMove();


       // The actual board is created as well as the checkers that sit on it
        public void paintComponent(Graphics g) {

            g.setColor(Color.black);
            g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
            g.drawRect(1, 1, getSize().width - 3, getSize().height - 3);


            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (row % 2 == col % 2)
                        g.setColor(Color.LIGHT_GRAY);
                    else
                        g.setColor(Color.GRAY);
                    g.fillRect(2 + col * 20, 2 + row * 20, 20, 20);
                    switch (board.pieceAt(row, col)) {
                        case CheckersData.RED:
                            g.setColor(Color.RED);
                            g.fillOval(4 + col * 20, 4 + row * 20, 15, 15);
                            break;
                        case CheckersData.BLACK:
                            g.setColor(Color.BLACK);
                            g.fillOval(4 + col * 20, 4 + row * 20, 15, 15);
                            break;
                        case CheckersData.RED_KING:
                            g.setColor(Color.RED);
                            g.fillOval(4 + col * 20, 4 + row * 20, 15, 15);
                            g.setColor(Color.WHITE);
                            g.drawString("K", 7 + col * 20, 16 + row * 20);
                            break;
                        case CheckersData.BLACK_KING:
                            g.setColor(Color.BLACK);
                            g.fillOval(4 + col * 20, 4 + row * 20, 15, 15);
                            g.setColor(Color.WHITE);
                            g.drawString("K", 7 + col * 20, 16 + row * 20);
                            break;
                    }
                }
            }
         

            if (gameInProgress) {
                g.setColor(Color.cyan);
                for (int i = 0; i < legalMoves.length; i++) {
                    g.drawRect(2 + legalMoves[i].fromCol * 20, 2 + legalMoves[i].fromRow * 20, 19, 19);
                    g.drawRect(3 + legalMoves[i].fromCol * 20, 3 + legalMoves[i].fromRow * 20, 17, 17);
                }

                if (selectedRow >= 0) {
                    g.setColor(Color.white);
                    g.drawRect(2 + selectedCol * 20, 2 + selectedRow * 20, 19, 19);
                    g.drawRect(3 + selectedCol * 20, 3 + selectedRow * 20, 17, 17);
                    g.setColor(Color.green);
                    for (int i = 0; i < legalMoves.length; i++) {
                        if (legalMoves[i].fromCol == selectedCol && legalMoves[i].fromRow == selectedRow) {
                            g.drawRect(2 + legalMoves[i].toCol * 20, 2 + legalMoves[i].toRow * 20, 19, 19);
                            g.drawRect(3 + legalMoves[i].toCol * 20, 3 + legalMoves[i].toRow * 20, 17, 17);
                        }
                    }
                }
            }

        }

        // Responds to the user clicking a specific square that they want their checkers to move
        public void mousePressed(MouseEvent evt) {
            if (gameInProgress == false)
                message.setText("Click \"New Game\" to start a new game.");
            else {
                int col = (evt.getX() - 2) / 20;
                int row = (evt.getY() - 2) / 20;
                if (col >= 0 && col < 8 && row >= 0 && row < 8)
                    doClickSquare(row, col);
            }
        }

        public void mouseReleased(MouseEvent evt) {
        }

        public void mouseClicked(MouseEvent evt) {
        }

        public void mouseEntered(MouseEvent evt) {
        }

        public void mouseExited(MouseEvent evt) {
        }

        private static class CheckersData {

       //RED pieces move upwards while BLACK pieces move downwards on the board.
            static final int
              EMPTY = 0,
              RED = 1,
              RED_KING = 2,
              BLACK = 3,
              BLACK_KING = 4;


            int[][] board;  // board[r][c] represents the row (r), and column (c).


           // new constructor
            CheckersData() {
                board = new int[8][8];
                setUpGame();
            }

            //checkers can only occur in squares such that row % 2 == col % 2.
            void setUpGame() {
                for (int row = 0; row < 8; row++) {
                    for (int col = 0; col < 8; col++) {
                        if (row % 2 == col % 2) {
                            if (row < 3)
                                board[row][col] = BLACK;
                            else if (row > 4)
                                board[row][col] = RED;
                            else
                                board[row][col] = EMPTY;
                        } else {
                            board[row][col] = EMPTY;
                        }
                    }
                }
            }

            int pieceAt(int row, int col) {
                return board[row][col];
            }


            //assume that move is legal.

            void makeMove(CheckersMove move) {
                makeMove(move.fromRow, move.fromCol, move.toRow, move.toCol);
            }


            void makeMove(int fromRow, int fromCol, int toRow, int toCol) {
                board[toRow][toCol] = board[fromRow][fromCol];
                board[fromRow][fromCol] = EMPTY;
                if (fromRow - toRow == 2 || fromRow - toRow == -2) {
                    // Removing the jumped piece.
                    int jumpRow = (fromRow + toRow) / 2;  // Row of jumped piece.
                    int jumpCol = (fromCol + toCol) / 2;  // Column of jumped piece.
                    board[jumpRow][jumpCol] = EMPTY;
                }
                if (toRow == 0 && board[toRow][toCol] == RED)
                    board[toRow][toCol] = RED_KING;
                if (toRow == 7 && board[toRow][toCol] == BLACK)
                    board[toRow][toCol] = BLACK_KING;
            }

            CheckersMove[] getLegalMoves(int player) {

                if (player != RED && player != BLACK)
                    return null;

                int playerKing;
                if (player == RED)
                    playerKing = RED_KING;
                else
                    playerKing = BLACK_KING;

                ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();  // Moves will be stored in this list.

                for (int row = 0; row < 8; row++) {
                    for (int col = 0; col < 8; col++) {
                        if (board[row][col] == player || board[row][col] == playerKing) {
                            if (canJump(player, row, col, row + 1, col + 1, row + 2, col + 2))
                                moves.add(new CheckersMove(row, col, row + 2, col + 2));
                            if (canJump(player, row, col, row - 1, col + 1, row - 2, col + 2))
                                moves.add(new CheckersMove(row, col, row - 2, col + 2));
                            if (canJump(player, row, col, row + 1, col - 1, row + 2, col - 2))
                                moves.add(new CheckersMove(row, col, row + 2, col - 2));
                            if (canJump(player, row, col, row - 1, col - 1, row - 2, col - 2))
                                moves.add(new CheckersMove(row, col, row - 2, col - 2));
                        }
                    }
                }
         

                if (moves.size() == 0) {
                    for (int row = 0; row < 8; row++) {
                        for (int col = 0; col < 8; col++) {
                            if (board[row][col] == player || board[row][col] == playerKing) {
                                if (canMove(player, row, col, row + 1, col + 1))
                                    moves.add(new CheckersMove(row, col, row + 1, col + 1));
                                if (canMove(player, row, col, row - 1, col + 1))
                                    moves.add(new CheckersMove(row, col, row - 1, col + 1));
                                if (canMove(player, row, col, row + 1, col - 1))
                                    moves.add(new CheckersMove(row, col, row + 1, col - 1));
                                if (canMove(player, row, col, row - 1, col - 1))
                                    moves.add(new CheckersMove(row, col, row - 1, col - 1));
                            }
                        }
                    }
                }

                if (moves.size() == 0)
                    return null;
                else {
                    CheckersMove[] moveArray = new CheckersMove[moves.size()];
                    for (int i = 0; i < moves.size(); i++)
                        moveArray[i] = moves.get(i);
                    return moveArray;
                }

            }

            // Specify when a user can jump legally
            CheckersMove[] getLegalJumpsFrom(int player, int row, int col) {
                if (player != RED && player != BLACK)
                    return null;
                int playerKing;
                if (player == RED)
                    playerKing = RED_KING;
                else
                    playerKing = BLACK_KING;
                ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();  //legal jumps will be stored in list.
                if (board[row][col] == player || board[row][col] == playerKing) {
                    if (canJump(player, row, col, row + 1, col + 1, row + 2, col + 2))
                        moves.add(new CheckersMove(row, col, row + 2, col + 2));
                    if (canJump(player, row, col, row - 1, col + 1, row - 2, col + 2))
                        moves.add(new CheckersMove(row, col, row - 2, col + 2));
                    if (canJump(player, row, col, row + 1, col - 1, row + 2, col - 2))
                        moves.add(new CheckersMove(row, col, row + 2, col - 2));
                    if (canJump(player, row, col, row - 1, col - 1, row - 2, col - 2))
                        moves.add(new CheckersMove(row, col, row - 2, col - 2));
                }
                if (moves.size() == 0)
                    return null;
                else {
                    CheckersMove[] moveArray = new CheckersMove[moves.size()];
                    for (int i = 0; i < moves.size(); i++)
                        moveArray[i] = moves.get(i);
                    return moveArray;
                }
            }

            // Specifies where users can jump
            private boolean canJump(int player, int r1, int c1, int r2, int c2, int r3, int c3) {

                if (r3 < 0 || r3 >= 8 || c3 < 0 || c3 >= 8)
                    return false;

                if (board[r3][c3] != EMPTY)
                    return false;  // (r3,c3) already has a piece.

                if (player == RED) {
                    if (board[r1][c1] == RED && r3 > r1)
                        return false;  // Red pieces can only move upwards.
                    if (board[r2][c2] != BLACK && board[r2][c2] != BLACK_KING)
                        return false;  // no black pieces to jump.
                    return true;  // jump is legal.
                } else {
                    if (board[r1][c1] == BLACK && r3 < r1)
                        return false;  // Black piece can only move downwards.
                    if (board[r2][c2] != RED && board[r2][c2] != RED_KING)
                        return false;  // no red pieces to jump.
                    return true;  // jump is legal.
                }

            }

            // Specify when users can move their checkers
            private boolean canMove(int player, int r1, int c1, int r2, int c2) {

                if (r2 < 0 || r2 >= 8 || c2 < 0 || c2 >= 8)
                    return false;

                if (board[r2][c2] != EMPTY)
                    return false;  // (r2,c2) already contains a piece.

                if (player == RED) {
                    if (board[r1][c1] == RED && r2 > r1)
                        return false;  // Red pieces can only move downwards.
                    return true;  //legal move.
                } else {
                    if (board[r1][c1] == BLACK && r2 < r1)
                        return false;  // Black pieces can only move upwards.
                    return true;  // legal move.
                }
            }
        }
    }
}