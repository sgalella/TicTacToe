package main.java.tictactoe;

import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * GUI for the Tic-Tac-Toe.
 *
 * @Author: sgalella
 */
class GUI {

    private final int dimensions = 3;
    private final Field[][] mainPanel;
    private String playerToken;
    private String opponentToken;
    private int numPlayerTokens = 0;

    /**
     * Initializes GUI.
     */
    GUI() {

        JFrame frame = new JFrame("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(dimensions, dimensions));

        mainPanel = new Field[3][3];
        for (int i = 0; i < dimensions; i++) {
            for (int j = 0; j < dimensions; j++) {
                mainPanel[i][j] = new Field();
                frame.add(mainPanel[i][j]);
            }
        }

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Sets the token for each player.
     * @param playerToken
     * @param opponentToken
     */
    void setPlayersTokens(String playerToken, String opponentToken){
        this.playerToken = playerToken;
        this.opponentToken = opponentToken;
    }

    /**
     * Checks if any of the players won the game.
     * @param board
     * @param token
     * @return
     */
    public boolean checkWinner(String[][] board, String token){

        Color color;

        if (token.equals("X")){
            color = new Color(255,140, 140);
        } else {
            color = new Color(135,206, 250);
        }

        // Horizontal
        if (board[0][0].equals(token) && board[0][1].equals(token) && board[0][2].equals(token)){
            mainPanel[0][0].setBackground(color);
            mainPanel[0][1].setBackground(color);
            mainPanel[0][2].setBackground(color);
            return true;
        } else if (board[1][0].equals(token) && board[1][1].equals(token) && board[1][2].equals(token)){
            mainPanel[1][0].setBackground(color);
            mainPanel[1][1].setBackground(color);
            mainPanel[1][2].setBackground(color);
            return true;
        } else if (board[2][0].equals(token) && board[2][1].equals(token) && board[2][2].equals(token)){
            mainPanel[2][0].setBackground(color);
            mainPanel[2][1].setBackground(color);
            mainPanel[2][2].setBackground(color);
            return true;
        }
        // Vertical
        else if (board[0][0].equals(token) && board[1][0].equals(token) && board[2][0].equals(token)){
            mainPanel[0][0].setBackground(color);
            mainPanel[1][0].setBackground(color);
            mainPanel[2][0].setBackground(color);
            return true;
        } else if (board[0][1].equals(token) && board[1][1].equals(token) && board[2][1].equals(token)){
            mainPanel[0][1].setBackground(color);
            mainPanel[1][1].setBackground(color);
            mainPanel[2][1].setBackground(color);
            return true;
        } else if (board[0][2].equals(token) && board[1][2].equals(token) && board[2][2].equals(token)){
            mainPanel[0][2].setBackground(color);
            mainPanel[1][2].setBackground(color);
            mainPanel[2][2].setBackground(color);
            return true;
            // Diagonal
        } else if (board[0][0].equals(token) && board[1][1].equals(token) && board[2][2].equals(token)){
            mainPanel[0][0].setBackground(color);
            mainPanel[1][1].setBackground(color);
            mainPanel[2][2].setBackground(color);
            return true;
        } else if (board[0][2].equals(token) && board[1][1].equals(token) && board[2][0].equals(token)){
            mainPanel[0][2].setBackground(color);
            mainPanel[1][1].setBackground(color);
            mainPanel[2][0].setBackground(color);
            return true;
        }

        return false;
    }

    /**
     * Clears the board for a new game.
     */
    void reset() {
        for (int i = 0; i < dimensions; i++) {
            for (int j = 0; j < dimensions; j++) {
                mainPanel[i][j].setToken("-");
                mainPanel[i][j].setBackground(Color.white);
            }
        }
        numPlayerTokens = 0;
    }

    /**
     * Returns number of player's tokens.
     * @return
     */
    int getPlayerTokens(){
        return numPlayerTokens;
    }

    /**
     * Gets the board from the gui
     * @return
     */
    String[][] getBoard() {
        String[][] board = { { "-", "-", "-" }, { "-", "-", "-" }, { "-", "-", "-" } };
        for (int i = 0; i < dimensions; i++) {
            for (int j = 0; j < dimensions; j++) {
                board[i][j] = mainPanel[i][j].getToken();
            }
        }
        return board;
    }

    /**
     * Sets the opponent token on the board
     */
    void setTokenBoard(int x, int y) {
        for (int i = 0; i < dimensions; i++) {
            for (int j = 0; j < dimensions; j++) {
                if (i == x && j == y)
                    mainPanel[i][j].setToken(this.opponentToken);
            }
        }
    }

    /**
     * Class for drawing each cell on the board.
     */
    class Field extends JPanel {

        private String token;

        Field() {
            this.token = "-";
            Border border = BorderFactory.createLineBorder(Color.black);
            this.setBackground(Color.white);
            this.setBorder(border);
            this.setPreferredSize(new Dimension(100, 100));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (token.equals("-")) {
                        setToken(playerToken);
                        numPlayerTokens += 1;
                    }
                }

            });

        }

        /**
         * Sets the token of the player or the opponent.
         * Called at the beginning of each game.
         * @param token
         */
        void setToken(String token) {
            this.token = token;
        }

        /**
         * Gives the token on the cell.
         * @return
         */
        String getToken(){
            return this.token;
        }

        /**
         * Painting tokens.
         *
         */
        public void paintComponent(Graphics g){

            int width = getWidth()/2;
            int height = getHeight()/2;
            int radius = getWidth()/4;
            Graphics2D g2 = (Graphics2D) g;
            int s = 20;
            g2.setStroke(new BasicStroke(3));

            if (this.token.equals("O")){
                g2.setColor(Color.blue);
                g2.drawOval(width-radius, height-radius, 2*radius, 2*radius);
            } else if (this.token.equals("X")){
                g2.setColor(Color.red);
                g2.drawLine(width-s, height-s, width+s, height+s);
                g2.drawLine(width-s, height+s, width+s, height-s);

            }
            repaint();

        }

    }

}