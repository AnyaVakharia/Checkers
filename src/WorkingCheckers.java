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
 *          users are able to click the "Resign" button which will end the game
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
            message.setText("Red:  Make your move.");
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
                gameOver("RED resigns.  BLACK wins.");
            else
                gameOver("BLACK resigns.  RED wins.");
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
                        message.setText("RED:  Make your move.");
                    else
                        message.setText("BLACK:  Make your move.");
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

            message.setText("Click the square you want to move to.");

        }  // end doClickSquare()


        // Checker is moved to the specified spot
        void doMakeMove(CheckersMove move) {

            board.makeMove(move);

            // Check if this move was a jump
            if (move.isJump()) {
                legalMoves = board.getLegalJumpsFrom(currentPlayer, move.toRow, move.toCol);
                if (legalMoves != null) {
                    if (currentPlayer == CheckersData.RED)
                        message.setText("RED:  You must continue jumping.");
                    else
                        message.setText("BLACK:  You must continue jumping.");
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
                    gameOver("BLACK has no moves.  RED wins.");
                else if (legalMoves[0].isJump())
                    message.setText("BLACK:  Make your move.  You must jump.");
                else
                    message.setText("BLACK:  Make your move.");
            } else {
                currentPlayer = CheckersData.RED;
                legalMoves = board.getLegalMoves(currentPlayer);
                if (legalMoves == null)
                    gameOver("RED has no moves.  BLACK wins.");
                else if (legalMoves[0].isJump())
                    message.setText("RED:  Make your move.  You must jump.");
                else
                    message.setText("RED:  Make your move.");
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


        /**
         * An object of this class holds data about a game of checkers.
         * It knows what kind of piece is on each square of the checkerboard.
         * Note that RED moves "up" the board (i.e. row number decreases)
         * while BLACK moves "down" the board (i.e. row number increases).
         * Methods are provided to return lists of available legal moves.
         */
        private static class CheckersData {
      
      /*  The following constants represent the possible contents of a square
          on the board.  The constants RED and BLACK also represent players
          in the game. */

            static final int
                    EMPTY = 0,
                    RED = 1,
                    RED_KING = 2,
                    BLACK = 3,
                    BLACK_KING = 4;


            int[][] board;  // board[r][c] is the contents of row r, column c.


            /**
             * Constructor.  Create the board and set it up for a new game.
             */
            CheckersData() {
                board = new int[8][8];
                setUpGame();
            }


            /**
             * Set up the board with checkers in position for the beginning
             * of a game.  Note that checkers can only be found in squares
             * that satisfy  row % 2 == col % 2.  At the start of the game,
             * all such squares in the first three rows contain black squares
             * and all such squares in the last three rows contain red squares.
             */
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
            }  // end setUpGame()


            /**
             * Return the contents of the square in the specified row and column.
             */
            int pieceAt(int row, int col) {
                return board[row][col];
            }


            /**
             * Make the specified move.  It is assumed that move
             * is non-null and that the move it represents is legal.
             */
            void makeMove(CheckersMove move) {
                makeMove(move.fromRow, move.fromCol, move.toRow, move.toCol);
            }


            /**
             * Make the move from (fromRow,fromCol) to (toRow,toCol).  It is
             * assumed that this move is legal.  If the move is a jump, the
             * jumped piece is removed from the board.  If a piece moves to
             * the last row on the opponent's side of the board, the
             * piece becomes a king.
             */
            void makeMove(int fromRow, int fromCol, int toRow, int toCol) {
                board[toRow][toCol] = board[fromRow][fromCol];
                board[fromRow][fromCol] = EMPTY;
                if (fromRow - toRow == 2 || fromRow - toRow == -2) {
                    // The move is a jump.  Remove the jumped piece from the board.
                    int jumpRow = (fromRow + toRow) / 2;  // Row of the jumped piece.
                    int jumpCol = (fromCol + toCol) / 2;  // Column of the jumped piece.
                    board[jumpRow][jumpCol] = EMPTY;
                }
                if (toRow == 0 && board[toRow][toCol] == RED)
                    board[toRow][toCol] = RED_KING;
                if (toRow == 7 && board[toRow][toCol] == BLACK)
                    board[toRow][toCol] = BLACK_KING;
            }

            /**
             * Return an array containing all the legal CheckersMoves
             * for the specified player on the current board.  If the player
             * has no legal moves, null is returned.  The value of player
             * should be one of the constants RED or BLACK; if not, null
             * is returned.  If the returned value is non-null, it consists
             * entirely of jump moves or entirely of regular moves, since
             * if the player can jump, only jumps are legal moves.
             */
            CheckersMove[] getLegalMoves(int player) {

                if (player != RED && player != BLACK)
                    return null;

                int playerKing;  // The constant representing a King belonging to player.
                if (player == RED)
                    playerKing = RED_KING;
                else
                    playerKing = BLACK_KING;

                ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();  // Moves will be stored in this list.
         
         /*  First, check for any possible jumps.  Look at each square on the board.
          If that square contains one of the player's pieces, look at a possible
          jump in each of the four directions from that square.  If there is 
          a legal jump in that direction, put it in the moves ArrayList.
          */

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
         
         /*  If any jump moves were found, then the user must jump, so we don't 
          add any regular moves.  However, if no jumps were found, check for
          any legal regular moves.  Look at each square on the board.
          If that square contains one of the player's pieces, look at a possible
          move in each of the four directions from that square.  If there is 
          a legal move in that direction, put it in the moves ArrayList.
          */

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
         
         /* If no legal moves have been found, return null.  Otherwise, create
          an array just big enough to hold all the legal moves, copy the
          legal moves from the ArrayList into the array, and return the array. */

                if (moves.size() == 0)
                    return null;
                else {
                    CheckersMove[] moveArray = new CheckersMove[moves.size()];
                    for (int i = 0; i < moves.size(); i++)
                        moveArray[i] = moves.get(i);
                    return moveArray;
                }

            }  // end getLegalMoves


            /**
             * Return a list of the legal jumps that the specified player can
             * make starting from the specified row and column.  If no such
             * jumps are possible, null is returned.  The logic is similar
             * to the logic of the getLegalMoves() method.
             */
            CheckersMove[] getLegalJumpsFrom(int player, int row, int col) {
                if (player != RED && player != BLACK)
                    return null;
                int playerKing;  // The constant representing a King belonging to player.
                if (player == RED)
                    playerKing = RED_KING;
                else
                    playerKing = BLACK_KING;
                ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();  // The legal jumps will be stored in this list.
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
            }  // end getLegalMovesFrom()


            /**
             * This is called by the two previous methods to check whether the
             * player can legally jump from (r1,c1) to (r3,c3).  It is assumed
             * that the player has a piece at (r1,c1), that (r3,c3) is a position
             * that is 2 rows and 2 columns distant from (r1,c1) and that
             * (r2,c2) is the square between (r1,c1) and (r3,c3).
             */
            private boolean canJump(int player, int r1, int c1, int r2, int c2, int r3, int c3) {

                if (r3 < 0 || r3 >= 8 || c3 < 0 || c3 >= 8)
                    return false;  // (r3,c3) is off the board.

                if (board[r3][c3] != EMPTY)
                    return false;  // (r3,c3) already contains a piece.

                if (player == RED) {
                    if (board[r1][c1] == RED && r3 > r1)
                        return false;  // Regular red piece can only move up.
                    if (board[r2][c2] != BLACK && board[r2][c2] != BLACK_KING)
                        return false;  // There is no black piece to jump.
                    return true;  // The jump is legal.
                } else {
                    if (board[r1][c1] == BLACK && r3 < r1)
                        return false;  // Regular black piece can only move downn.
                    if (board[r2][c2] != RED && board[r2][c2] != RED_KING)
                        return false;  // There is no red piece to jump.
                    return true;  // The jump is legal.
                }

            }  // end canJump()


            /**
             * This is called by the getLegalMoves() method to determine whether
             * the player can legally move from (r1,c1) to (r2,c2).  It is
             * assumed that (r1,r2) contains one of the player's pieces and
             * that (r2,c2) is a neighboring square.
             */
            private boolean canMove(int player, int r1, int c1, int r2, int c2) {

                if (r2 < 0 || r2 >= 8 || c2 < 0 || c2 >= 8)
                    return false;  // (r2,c2) is off the board.

                if (board[r2][c2] != EMPTY)
                    return false;  // (r2,c2) already contains a piece.

                if (player == RED) {
                    if (board[r1][c1] == RED && r2 > r1)
                        return false;  // Regular red piece can only move down.
                    return true;  // The move is legal.
                } else {
                    if (board[r1][c1] == BLACK && r2 < r1)
                        return false;  // Regular black piece can only move up.
                    return true;  // The move is legal.
                }

            }  // end canMove()


        } // end class CheckersData


    } // end class Checkers
}