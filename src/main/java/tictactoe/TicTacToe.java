package main.java.tictactoe;

import java.util.*;
import javax.swing.JOptionPane;


/**
 * Tic-Tac-Toe implementation using the minimax algorithm.
 *
 * @Author: sgalella
 */
public class TicTacToe {

    private static String playerToken;
    private static String opponentToken;

    public static void printBoard(String[][] board) {
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * Gets user next position from command line.
     *
     * @param line
     * @return
     */
    public static int[] getPosition(String line) {

        int[] position = new int[2];

        position[0] = Integer.parseInt(line.trim().split(" ")[0]);
        position[1] = Integer.parseInt(line.trim().split(" ")[1]);

        return position;

    }

    /**
     * Checks if any of the players won the game.
     *
     * @param board
     * @param token
     * @return
     */
    public static boolean checkWinner(String[][] board, String token) {
        // Horizontal
        if (board[0][0].equals(token) && board[0][1].equals(token) && board[0][2].equals(token)) {
            return true;
        } else if (board[1][0].equals(token) && board[1][1].equals(token) && board[1][2].equals(token)) {
            return true;
        } else if (board[2][0].equals(token) && board[2][1].equals(token) && board[2][2].equals(token)) {
            return true;
        }
        // Vertical
        else if (board[0][0].equals(token) && board[1][0].equals(token) && board[2][0].equals(token)) {
            return true;
        } else if (board[0][1].equals(token) && board[1][1].equals(token) && board[2][1].equals(token)) {
            return true;
        } else if (board[0][2].equals(token) && board[1][2].equals(token) && board[2][2].equals(token)) {
            return true;
            // Diagonal
        } else if (board[0][0].equals(token) && board[1][1].equals(token) && board[2][2].equals(token)) {
            return true;
        } else return board[0][2].equals(token) && board[1][1].equals(token) && board[2][0].equals(token);

    }

    /**
     * Checks available positions to move.
     *
     * @param board
     * @return
     */
    public static List<int[]> checkAvailablePositions(String[][] board) {

        List<int[]> availablePositions = new ArrayList<>();

        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j].equals("-")) {
                    availablePositions.add(new int[]{i, j});
                }
            }
        }
        return availablePositions;
    }

    /**
     * Prints possible moves.
     *
     * @param moves
     */
    public static void printPossibleMoves(List<int[]> moves) {
        for (int[] position : moves) {
            System.out.println("[" + position[0] + " " + position[1] + "]");
        }
    }

    /**
     * Minimax algorithm.
     *
     * @param board
     * @param depth
     * @param isMaximizing
     * @return
     */
    public static int minimax(String[][] board, int depth, boolean isMaximizing) {

        List<int[]> nextPosition;
        int eval, maxEval, minEval;

        if (depth == 0 || checkWinner(board, opponentToken) || checkWinner(board, playerToken) || checkAvailablePositions(board).size() == 0) {
            if (checkWinner(board, opponentToken)) {
                return 1;
            } else if (checkWinner(board, playerToken)) {
                return -1;
            } else {
                return 0;
            }
        }

        if (isMaximizing) {
            maxEval = -10000;
            nextPosition = checkAvailablePositions(board);
            for (int[] position : nextPosition) {
                board[position[0]][position[1]] = opponentToken;
                eval = minimax(board, depth - 1, false);
                if (eval > maxEval) {
                    maxEval = eval;
                }
                board[position[0]][position[1]] = "-";
            }
            return maxEval;

        } else {
            minEval = 10000;
            nextPosition = checkAvailablePositions(board);
            for (int[] position : nextPosition) {
                board[position[0]][position[1]] = playerToken;
                eval = minimax(board, depth - 1, true);
                if (eval < minEval) {
                    minEval = eval;
                }
                board[position[0]][position[1]] = "-";
            }
            return minEval;
        }

    }

    /**
     * Runs the game.
     *
     * @param gui
     */
    public static void run(GUI gui) {
        String winner;
        String currentPlayer;
        Map<Integer[],Integer> mapPositionToValue;
        List<Integer[]> bestMoves;
        String[][] board = {{"-", "-", "-"}, {"-", "-", "-"}, {"-", "-", "-"}};
        Random rand = new Random();
        //rand.setSeed(1234); // For reproducibility

        // Choose turns randomly
        if (rand.nextFloat() < 0.5) {
            currentPlayer = "Player";
            playerToken = "O";
            opponentToken = "X";
        } else {
            currentPlayer = "opponent";
            playerToken = "X";
            opponentToken = "O";
        }

        // Assigns the tokens to each player in the gui
        gui.setPlayersTokens(playerToken, opponentToken);
        int numPlayerTokens = gui.getPlayerTokens();

        // Game loop
        while (true) {
            // Player move
            if (currentPlayer.equals("Player")) {
                while (gui.getPlayerTokens() == numPlayerTokens) {
                    // Put a token
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                }
                numPlayerTokens++;
                board = gui.getBoard();
                if (gui.checkWinner(board, playerToken)) {
                    winner = playerToken;
                    break;
                }
                currentPlayer = "opponent";
            }
            // opponent move
            else {
                List<int[]> possibleMoves = checkAvailablePositions(board);
                Integer[] nextMove = new Integer[2];
                mapPositionToValue = new HashMap<>();
                bestMoves = new ArrayList<>();
                System.out.println(gui.getDepth());
                for (int[] moves : possibleMoves) {
                    board[moves[0]][moves[1]] = opponentToken;
                    int eval = minimax(board, gui.getDepth(), false);
                    mapPositionToValue.put(new Integer[]{moves[0], moves[1]}, eval);
                    board[moves[0]][moves[1]] = "-";
                }
                // Find the best value of the different possible moves
                int maxValue = Collections.max(mapPositionToValue.values());
                // Find the best possible moves
                for(Map.Entry<Integer[],Integer> entry:mapPositionToValue.entrySet()){
                    if (entry.getValue() == maxValue){
                        bestMoves.add(entry.getKey());
                    }
                }
                // Retrieve next move
                nextMove = bestMoves.get(new Random().nextInt(bestMoves.size()));
                board[nextMove[0]][nextMove[1]] = opponentToken;
                gui.setTokenBoard(nextMove[0], nextMove[1]);
                if (gui.checkWinner(board, opponentToken)) {
                    winner = opponentToken;
                    break;
                }
                currentPlayer = "Player";
            }
            // If no one won and no available positions
            if (checkAvailablePositions(board).size() == 0) {
                winner = "none";
                break;
            }
        }

        // Sets the dialog in the window
        String dialog;
        if (winner.equals(playerToken)) {
            dialog = "You win! Play again?";
        } else if (winner.equals(opponentToken)) {
            dialog = "You lose! Play again?";
        } else {
            dialog = "You tie! Play again?";
        }

        // Shows the dialog window, initializes another game or closes the application
        int output = JOptionPane.showConfirmDialog(null, dialog);
        if (output == 0) {
            gui.reset();
            run(gui);
        } else if (output == 1) {
            System.exit(0);
        }
    }

    /**
     * Main.
     *
     * @param args
     */
    public static void main(String[] args) {
        GUI gui = new GUI();
        run(gui);
    }

}
