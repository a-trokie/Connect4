import java.util.ArrayList;
import java.util.List;

public class State {
    char[][] board; // 2D array to represent the game board
    char currentPlayer; // Current player ('X' or 'O')
    int rows, cols; // Dimensions of the board

    // Constructor: Initializes the game state with a specified board size and starting player
    public State(int rows, int cols, char currentPlayer) {
        this.rows = rows;
        this.cols = cols;
        this.currentPlayer = currentPlayer;
        this.board = new char[rows][cols]; // Create a blank board with given dimensions
        initializeBoard(); // Fill the board with empty spaces
    }

    // Determines if the current state is a terminal state (win or draw)
    public boolean isTerminal(int winCount) {
        return checkWin(winCount) || isFull(); // Terminal if there's a win or the board is full
    }

    // Fills the board with empty spaces (' ') to initialize it
    private void initializeBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = ' '; // Empty space indicates an unoccupied cell
            }
        }
    }

    // Creates a deep copy of the current state
    public State cloneState() {
        State clone = new State(rows, cols, currentPlayer); // Create a new state with the same dimensions and current player
        for (int i = 0; i < rows; i++) {
            System.arraycopy(this.board[i], 0, clone.board[i], 0, cols); // Copy each row of the board
        }
        return clone; // Return the cloned state
    }

    // Places the current player's token in the specified column if possible
    public boolean makeMove(int col) {
        for (int row = rows - 1; row >= 0; row--) { // Start from the bottom of the column
            if (board[row][col] == ' ') { // If the cell is empty
                board[row][col] = currentPlayer; // Place the current player's token
                return true; // Move was successful
            }
        }
        return false; // Return false if the column is full
    }

    // Checks if the board is completely full
    public boolean isFull() {
        for (int col = 0; col < cols; col++) {
            if (board[0][col] == ' ') { // If any cell in the top row is empty, the board is not full
                return false;
            }
        }
        return true; // All cells are occupied
    }

    // Checks if the current player has met the win condition
    public boolean checkWin(int winCount) {
        return checkHorizontal(winCount) || checkVertical(winCount) || checkDiagonals(winCount);
    }

    // Checks for a horizontal win for the current player
    private boolean checkHorizontal(int winCount) {
        for (int row = 0; row < rows; row++) { // Iterate over all rows
            for (int col = 0; col <= cols - winCount; col++) { // Iterate over valid starting points in the row
                boolean win = true;
                for (int k = 0; k < winCount; k++) { // Check the next `winCount` cells
                    if (board[row][col + k] != currentPlayer) { // If any cell doesn't match the current player
                        win = false; // No win
                        break;
                    }
                }
                if (win) return true; // Return true if all cells match
            }
        }
        return false; // No horizontal win found
    }

    // Checks for a vertical win for the current player
    private boolean checkVertical(int winCount) {
        for (int col = 0; col < cols; col++) { // Iterate over all columns
            for (int row = 0; row <= rows - winCount; row++) { // Iterate over valid starting points in the column
                boolean win = true;
                for (int k = 0; k < winCount; k++) { // Check the next `winCount` cells
                    if (board[row + k][col] != currentPlayer) { // If any cell doesn't match the current player
                        win = false; // No win
                        break;
                    }
                }
                if (win) return true; // Return true if all cells match
            }
        }
        return false; // No vertical win found
    }

    // Checks for diagonal wins for the current player
    private boolean checkDiagonals(int winCount) {
        // Check for diagonal wins in the `/` direction
        for (int row = 0; row <= rows - winCount; row++) { // Iterate over valid starting points for `/` diagonals
            for (int col = 0; col <= cols - winCount; col++) {
                boolean win = true;
                for (int k = 0; k < winCount; k++) { // Check the next `winCount` cells diagonally
                    if (board[row + k][col + k] != currentPlayer) { // If any cell doesn't match the current player
                        win = false; // No win
                        break;
                    }
                }
                if (win) return true; // Return true if all cells match
            }
        }

        // Check for diagonal wins in the `\` direction
        for (int row = 0; row <= rows - winCount; row++) { // Iterate over valid starting points for `\` diagonals
            for (int col = winCount - 1; col < cols; col++) {
                boolean win = true;
                for (int k = 0; k < winCount; k++) { // Check the next `winCount` cells diagonally
                    if (board[row + k][col - k] != currentPlayer) { // If any cell doesn't match the current player
                        win = false; // No win
                        break;
                    }
                }
                if (win) return true; // Return true if all cells match
            }
        }

        return false; // No diagonal win found
    }

    // Returns a list of valid moves (columns that are not full)
    public List<Integer> getValidMoves() {
        List<Integer> validMoves = new ArrayList<>(); // List to store valid column indices
        for (int col = 0; col < cols; col++) {
            if (board[0][col] == ' ') { // If the top cell in the column is empty, the column is valid
                validMoves.add(col);
            }
        }
        return validMoves; // Return the list of valid moves
    }

    // Switches the current player (X -> O or O -> X)
    public void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X'; // Toggle between X and O
    }
}
