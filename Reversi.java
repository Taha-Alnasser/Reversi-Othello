import java.util.ArrayList;
import java.util.Scanner;

public class Reversi {
    public class Board {
        private char[][] board;
        public Board() {
            board = new char[8][8];
            // creating the base board
            for (int i=0; i<8;i++){
                for (int j=0; j<8;j++) {
                    board[i][j] = ' ';
                }
            }
            board[3][3] = 'X';
            board[4][4] = 'X';
            board[3][4] = 'O';
            board[4][3] = 'O';
        }
        public void printBoard() {
            // Styling the game so it is user-friendly
            System.out.println();
            System.out.println("    1   2   3   4   5   6   7   8");
            for (int i=0; i<8; i++) {
                System.out.println("  + - + - + - + - + - + - + - + - +");
                System.out.println("  |   |   |   |   |   |   |   |");
                System.out.print((i+1) + " ");
                for (int j = 0;j<8;j++){
                    System.out.print("| " + board[i][j] + " ");
                }
                System.out.println("|");
                System.out.println("  |   |   |   |   |   |   |   |");
            }
            System.out.println("  + - + - + - + - + - + - + - + - +");
            System.out.println("    1   2   3   4   5   6   7   8");
            System.out.println();
        }
        // method to identify the opponent
        public char getOpponent(char player) {
            if (player == 'X'){
                return 'O';
            }
            else {
                return 'X';
            }
        }
        // method to find if a certain move is valid
        public boolean isValidMove(int xstart, int ystart, char player) {
            char opponent = getOpponent(player);
            // to align the game with the java index which starts from 0
            xstart -=1;
            ystart -=1;
            // if the position is not empty or if it is out of bounds, return false
            if (board[xstart][ystart] != ' ' || !(xstart >= 0 && xstart < 8 && ystart >= 0 && ystart < 8)) {
                return false;
            }
            int[][] directions = {
                {-1, -1},  // northwest
                {-1, 0},   // west
                {-1, 1},   // southwest
                {0, -1},   // north
                {0, 1},    // south
                {1, -1},   // northeast
                {1, 0},    // east
                {1, 1}     // southeast
            };
            // looping thru all directions to check if the move is going to bracket something on some direction
            for (int[] direction : directions) {
                int x = xstart + direction[0];
                int y = ystart + direction[1];
                // we check if in some direction we have the opponent's piece, we continue on that direction
                // and if we found our piece at the end of the direction, then this is a valid move
                if (x >= 0 && x < 8 && y >= 0 && y < 8 && board[x][y] == opponent) {
                    while (x >= 0 && x < 8 && y >= 0 && y < 8 && board[x][y] == opponent) {
                        x += direction[0];
                        y += direction[1];
                    }
                    if (x >= 0 && x < 8 && y >= 0 && y < 8 && board[x][y] == player) {
                        return true;
                    }
                }
            }
            return false;
        }
        // making a list of all the valid moves, using the isValidMove method
        public ArrayList<ArrayList<Integer>> validMoves(char player) {
            ArrayList<ArrayList<Integer>> moves = new ArrayList<>();
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    // we use +1 because of the alignation with the board since it starts with 1 instead of 0
                    if (isValidMove(x + 1, y + 1, player)) { 
                        ArrayList<Integer> move = new ArrayList<>();
                        move.add(x + 1); 
                        move.add(y + 1); 
                        moves.add(move);
                    }
                }
            }
            return moves;
        }
        public void flip(int xstart, int ystart, char player) {
            char opponent = getOpponent(player);
            // to align the game with the java index which starts from 0
            xstart -=1;
            ystart -=1;
            int[][] directions = {
                {-1, -1},  // northwest
                {-1, 0},   // west
                {-1, 1},   // southwest
                {0, -1},   // north
                {0, 1},    // south
                {1, -1},   // northeast
                {1, 0},    // east
                {1, 1}     // southeast
            };
            // this uses the same direction concept as the isValidMove method
            for (int[] direction : directions) {
                int x = xstart + direction[0];
                int y = ystart + direction[1];
                if (x >= 0 && x < 8 && y >= 0 && y < 8 && board[x][y] == opponent) {
                    while (x >= 0 && x < 8 && y >= 0 && y < 8 && board[x][y] == opponent) {
                        x += direction[0];
                        y += direction[1];
                    }
                    // we start flipping after we checked that the line is bracketed by the player's pieces
                    if (x >= 0 && x < 8 && y >= 0 && y < 8 && board[x][y] == player) {
                        while (board[x -= direction[0]][y -= direction[1]] == opponent) {
                            board[x][y] = player;
                        }
                    }
                }
            }
        }
        public void getScore(){
            int oSpots = 0;
            int xSpots = 0;
            // this method will count the occurances of the player's pieces to finally make a decision for who wins
            for (int i=0; i<8;i++){
                for (int j=0; j<8;j++){
                    if (board[i][j] == 'O'){
                        oSpots+=1;
                    }
                    else if (board[i][j]=='X'){
                        xSpots +=1;
                    }
                }
            }
            System.out.println("O got a score of: " + oSpots);
            System.out.println("X got a score of: " + xSpots);
            // checking for the winner
            if (xSpots > oSpots) {
                System.out.println("X wins!");
            }
            else if (oSpots > xSpots) {
                System.out.println("O wins!");
            }
            else {
                System.out.println("Draw!");
            }
        }
        public void ai() {
            char opponent = 'O';
            ArrayList<ArrayList<Integer>> validMoves = validMoves(opponent);
        
            if (validMoves.isEmpty()) {
                return;
            }
        
            ArrayList<Integer> selectedMove = null;
        
            // checking from the available moves if any move is on a corner, if yes, it will be selected
            for (ArrayList<Integer> move : validMoves) {
                int x = move.get(0);
                int y = move.get(1);
                if ((x == 1 && y == 1) || (x == 1 && y == 8) || (x == 8 && y == 1) || (x == 8 && y == 8)) {
                    selectedMove = move;
                    break;
                }
            }
        
            // if we couldn't find any corner, then select anything randomly from the valid moves
            if (selectedMove == null) {
                selectedMove = validMoves.get((int) (Math.random() * validMoves.size()));
            }
            // 
            int x = selectedMove.get(0);
            int y = selectedMove.get(1);
            board[x-1][y-1] = opponent;
            flip(x, y, opponent);
        }
        
        // method that will return true until the game ends
        // this will be helpful in the next method play()
        public boolean onGoing(){
            for (int i=0; i<8;i++){
                for (int j=0; j<8;j++){
                    if (board[i][j] == ' '){
                        return true;
                    }
                }
            }
            return false;
        }
        public void play() {
            Scanner myScanner = new Scanner(System.in);
            // this outside while loop will continue running until the board has no empty spots
            while (onGoing()){
                // this loop is for X's turn, it will keeping looping until they enter a valid move
                while (true){
                    System.out.println("You are the X and your valid moves are (row, column): ");
                    System.out.println(validMoves('X'));
                    System.out.print("Enter the x position: ");
                    int x = myScanner.nextInt();
                    System.out.print("Enter the y position: ");
                    int y = myScanner.nextInt();
                    if (!(isValidMove(x, y, 'X'))){
                        System.out.println("Enter a valid move!");
                    } else {
                        board[x-1][y-1] = 'X';
                        flip(x, y, 'X');
                        System.out.println("Board after X's move: ");
                        printBoard();
                        break;
                    }
                    // avoiding terminal warning
                    myScanner.close();
                }
                // after X moved, we now check if there are still spots on the board, if yes, it's the ai's turn
                if (onGoing()) {
                    ai();
                    System.out.println("AI made a move");
                    printBoard();
                }
            }
            // this is when the game ends
            getScore();
        }
    }
    public static void main(String[] args){
        Reversi game = new Reversi();
        Board board = game.new Board();
        board.printBoard();
        board.play();
    }
}