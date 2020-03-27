package src.sgalella.tictactoe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;


/**
 * Tic-Tac-Toe implementation using the minimax algorithm.
 * 
 * @Author: sgalella
 */
public class TicTacToe {

    private static String playerToken;
    private static String oponentToken;


    public static void printBoard(String[][] board){
        for (int i = 0; i < board[0].length; i++){
            for (int j = 0; j < board.length; j++){
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * Gets user next position from command line.
     * @param line
     * @return
     */
    public static int[] getPosition(String line){

        int[] position = new int[2];

        position[0] =  Integer.parseInt(line.trim().split(" ")[0]);
        position[1] =  Integer.parseInt(line.trim().split(" ")[1]);

        return position;

    }


    /**
     * Checks if any of the players won the game.
     * @param board
     * @param token
     * @return
     */
    public static boolean checkWinner(String[][] board, String token){
        // Horizontal
        if (board[0][0] == token && board[0][1] == token && board[0][2] == token){
            return true;
        } else if (board[1][0] == token && board[1][1] == token && board[1][2] == token){
            return true;
        } else if (board[2][0] == token && board[2][1] == token && board[2][2] == token){
            return true;
        } 
        // Vertical
        else if (board[0][0] == token && board[1][0] == token && board[2][0] == token){
            return true;
        } else if (board[0][1] == token && board[1][1] == token && board[2][1] == token){
            return true;
        } else if (board[0][2] == token && board[1][2] == token && board[2][2] == token){
            return true;
        // Diagonal
        } else if (board[0][0] == token && board[1][1] == token && board[2][2] == token){
           return true;
        } else if (board[0][2] == token && board[1][1] == token && board[2][0] == token){
            return true;
        }

        return false;
    }

    /**
     * Checks available positions to move.
     * @param board
     * @return
     */
    public static List<int[]> checkAvailablePositions(String[][] board) {

        List<int[]> availablePositions = new ArrayList<>();

        for (int i = 0; i < board[0].length; i++){
            for (int j = 0; j < board.length; j++){
                if (board[i][j] == "-"){
                    availablePositions.add(new int[]{i, j});
                }
            }
        }
        return availablePositions;
    }

    /**
     * Prints possible moves.
     * @param moves
     */
    public static void printPossibleMoves(List<int[]> moves){
        for (int[] position:moves){
            System.out.println("[" +  position[0] + " " + position[1]+ "]");
        }
    }

    /**
     * Minimax algorithm.
     * @param board
     * @param depth
     * @param isMaximizing
     * @return
     */
    public static int minimax(String[][] board, int depth, boolean isMaximizing){

        List<int[]> nextPosition;
        int eval, maxEval, minEval;

        if (depth == 0 || checkWinner(board, oponentToken) || checkWinner(board, playerToken) || checkAvailablePositions(board).size() == 0){
            if (checkWinner(board, oponentToken)){
                return 1;
            } else if (checkWinner(board, playerToken)){
                return -1;
            } else {
                return 0;
            }
        }

        if (isMaximizing){
            maxEval = -10000;
            nextPosition = checkAvailablePositions(board);
            for(int[] position:nextPosition){
                board[position[0]][position[1]] = oponentToken;
                eval = minimax(board, depth-1, false);
                if (eval > maxEval){
                    maxEval = eval;
                }
                board[position[0]][position[1]] = "-";
            }
            return maxEval;
            
        } else {
            minEval = 10000;
            nextPosition = checkAvailablePositions(board);
            for(int[] position:nextPosition){
                board[position[0]][position[1]] = playerToken;
                eval = minimax(board, depth-1, true);
                if (eval < minEval){
                    minEval = eval;
                }
                board[position[0]][position[1]] = "-";
            }
            return minEval;
        }

    }

    /**
     * Runs the game.
     * @param gui
     */

    public static void run(GUI gui){
        String winner;
        String currentPlayer;
        String[][] board = {{"-","-","-"}, {"-","-","-"}, {"-","-","-"}};
        Random rand = new Random();
        //rand.setSeed(1234); // For reproducibility        

        // Choose turns randomly
        if (rand.nextFloat() < 0.5){
            currentPlayer = "Player";
            playerToken = "O";
            oponentToken = "X";
        } else {
            currentPlayer = "Oponent";
            playerToken = "X";
            oponentToken = "O";
        }

        // Assigns the tokens to each player in the gui
        gui.setPlayersTokens(playerToken, oponentToken);
        int numPlayerTokens = gui.getPlayerTokens();

        // Game loop
        while (true){
            // Player move
            if (currentPlayer.equals("Player")){
                while (gui.getPlayerTokens() == numPlayerTokens){
                    // Put a token
                    try {
                        Thread.sleep(500);
                    } catch(InterruptedException e){
                    }
                }
                numPlayerTokens++;
                board = gui.getBoard();
                if (checkWinner(board, playerToken)){
                    winner = playerToken;
                    break;
                }
                currentPlayer = "Oponent";
            } 
            // Oponent move
            else if (currentPlayer.equals("Oponent")) {
                List<int[]> possibleMoves = checkAvailablePositions(board);
                int[] bestMove = new int[2];
                int maxEval = -10000;
                for (int[] moves:possibleMoves){
                    board[moves[0]][moves[1]] = oponentToken;
                    int eval = minimax(board, 5, false);
                    if (eval > maxEval){
                        maxEval = eval;
                        bestMove = moves;
                    }
                    board[moves[0]][moves[1]] = "-";
                }
                board[bestMove[0]][bestMove[1]] = oponentToken;
                gui.setTokenBoard(board, bestMove[0], bestMove[1]);
                if (checkWinner(board, oponentToken)){
                    winner = oponentToken;
                    break;
                }
                currentPlayer = "Player";
            } 
            // If no one won and no available positions
            if (checkAvailablePositions(board).size() == 0){
                winner = "none";
                break;
            }
        }

        // Sets the dialog in the window
        String dialog;
        if (winner.equals(playerToken)){
            dialog = "You win! Play again?";
        } else if (winner.equals(oponentToken)){
            dialog = "You lose! Play again?";
        } else {
            dialog = "You tie! Play again?";
        }

        // Shows the dialog window, initializes another game or closes the application
        int output = JOptionPane.showConfirmDialog(null, dialog);
        if (output == 0){
            gui.reset();
            run(gui);
        } else if (output == 1){
            System.exit(0);
        }
    }
    
    /**
     * Main.
     * @param args
     */
    public static void main(String[] args) {
        GUI gui = new GUI();
        run(gui);
    }
     
}