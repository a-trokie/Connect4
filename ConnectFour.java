import java.util.Random;
import java.util.Scanner;

public class ConnectFour {
    private State state; // Represents the current game state (board and player turn)
    private int winCount; // Number of consecutive pieces needed to win
    private int opponentType; // Opponent type (1 = Human, 2 = Random AI, 3 = Minimax AI, 4 = Alpha-Beta Pruning AI)

    // Constructor: Initializes the game with the specified board size, win condition, and opponent type
    public ConnectFour(int rows, int cols, int winCount, int opponentType) {
        this.winCount = winCount; // Set the win condition
        this.opponentType = opponentType; // Set the opponent type
        this.state = new State(rows, cols, 'X'); // Initialize the game state with the first player as 'X'
    }

    // Main game loop
    public void playGame() {
        Scanner scanner = new Scanner(System.in); // For user input
        Random random = new Random(); // Used for Random AI's move selection
        boolean gameRunning = true; // Flag to keep the game loop running

        while (gameRunning) {
            printBoard(); // Print the current state of the board

            // Decide the type of move based on the current player or opponent type
            if (state.currentPlayer == 'X' || opponentType == 1) {
                // Human player's turn or two-player mode
                System.out.println("Player " + state.currentPlayer + ", enter column (1-" + state.cols + "): ");
                int col = -1; // Initialize to an invalid column
                boolean validInput = false;

                // Loop until the player provides a valid column number
                while (!validInput) {
                    try {
                        String input = scanner.nextLine(); // Read the input as a string
                        col = Integer.parseInt(input) - 1; // Convert to 0-based index
                        // Validate the input
                        if (col < 0 || col >= state.cols) {
                            System.out.println("Invalid column. Try again.");
                        } else if (!state.getValidMoves().contains(col)) {
                            System.out.println("Column is full. Try another.");
                        } else {
                            validInput = true; // Input is valid
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                    }
                }

                state.makeMove(col); // Apply the player's move
            } else if (opponentType == 2) {
                // Random AI's turn
                System.out.println("AI is making a move...");
                int col;
                do {
                    col = random.nextInt(state.cols); // Randomly select a column
                } while (!state.getValidMoves().contains(col)); // Ensure the column is not full
                state.makeMove(col); // Apply the move
            } else if (opponentType == 3) {
                // Minimax AI's turn
                System.out.println("Minimax AI is thinking...");
                int bestMove = getBestMove(state, 5); // Use Minimax to find the best move with a depth limit of 5
                state.makeMove(bestMove); // Apply the best move
            } else if (opponentType == 4) {
                // Alpha-Beta Pruning AI's turn
                System.out.println("Alpha-Beta Pruning AI is thinking...");
                int bestMove = getBestMoveAlphaBeta(state, 5); // Use Alpha-Beta pruning to find the best move
                state.makeMove(bestMove); // Apply the best move
            }

            // Check for win or draw
            if (state.checkWin(winCount)) {
                // If the current player has won
                printBoard(); // Show the final board state
                System.out.println("Player " + state.currentPlayer + " wins!");
                gameRunning = false; // End the game loop
            } else if (state.isFull()) {
                // If the board is full and it's a draw
                printBoard(); // Show the final board state
                System.out.println("It's a draw!");
                gameRunning = false; // End the game loop
            } else {
                state.switchPlayer(); // Switch the turn to the other player
            }
        }

        scanner.close(); // Close the scanner to prevent resource leaks
    }

    // Prints the current state of the board
    private void printBoard() {
        // Print column numbers
        System.out.print("   "); // Indentation for row numbers
        for (int j = 0; j < state.cols; j++) {
            System.out.print(" " + (j + 1) + "  "); // Print column numbers (1-based indexing)
        }
        System.out.println();
    
        // Print rows with row numbers
        for (int i = 0; i < state.rows; i++) {
            System.out.print((i + 1) + " "); // Print the row number (1-based indexing)
            for (int j = 0; j < state.cols; j++) {
                System.out.print("| " + state.board[i][j] + " "); // Print the cell content with column dividers
            }
            System.out.println("|"); // Close the row with a border
        }
    
        // Print bottom line for formatting
        System.out.print("   "); // Align with column numbers
        for (int j = 0; j < state.cols; j++) {
            System.out.print("----"); // Print the bottom border
        }
        System.out.println("-");
    }

    // Evaluates the game state for AI algorithms
    private int evaluateState(State state) {
        if (state.checkWin(winCount)) {
            // Return high/low scores depending on who wins
            return state.currentPlayer == 'X' ? -100 : 100; // X losing is bad for AI, O winning is good for AI
        }
        return 0; // Neutral score if no one has won
    }

    private int visitedStates; // Tracks the number of states evaluated during AI computations

    // Minimax algorithm: Explores all possible moves to find the optimal one
    private int minimax(State state, int depth, boolean isMaximizing) {
        visitedStates++; // Increment state counter
        if (state.isTerminal(winCount) || depth == 0) {
            return evaluateState(state); // Evaluate the state if terminal or depth limit reached
        }

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int move : state.getValidMoves()) {
                State childState = state.cloneState();
                childState.makeMove(move);
                childState.switchPlayer();
                int eval = minimax(childState, depth - 1, false); // Evaluate as minimizing player
                maxEval = Math.max(maxEval, eval);
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int move : state.getValidMoves()) {
                State childState = state.cloneState();
                childState.makeMove(move);
                childState.switchPlayer();
                int eval = minimax(childState, depth - 1, true); // Evaluate as maximizing player
                minEval = Math.min(minEval, eval);
            }
            return minEval;
        }
    }

    // Finds the best move using Minimax
    private int getBestMove(State state, int depth) {
        visitedStates = 0; // Reset visited states counter
        long startTime = System.nanoTime(); // Start the timer

        int bestMove = -1; // Store the best move
        int bestValue = Integer.MIN_VALUE;

        for (int move : state.getValidMoves()) {
            State childState = state.cloneState();
            childState.makeMove(move);
            childState.switchPlayer();
            int moveValue = minimax(childState, depth - 1, false); // Evaluate using Minimax
            if (moveValue > bestValue) { // Update the best move
                bestValue = moveValue;
                bestMove = move;
            }
        }

        long endTime = System.nanoTime(); // End the timer
        double elapsedTime = (endTime - startTime) / 1_000_000_000.0; // Convert to seconds

        // Print debug information
        System.out.println("I’m thinking...");
        System.out.printf(" visited %d states%n", visitedStates);
        System.out.printf(" best move: @%d, value: %.6f%n", bestMove + 1, bestValue / 1000.0);
        System.out.printf(" Elapsed time: %.3f secs%n", elapsedTime);
        System.out.println(" @" + (bestMove + 1));

        return bestMove;
    }

    // Alpha-Beta Pruning algorithm: Optimized Minimax with pruning
    private int alphaBeta(State state, int depth, int alpha, int beta, boolean isMaximizing) {
        visitedStates++; // Increment state counter
        if (state.isTerminal(winCount) || depth == 0) {
            return evaluateState(state);
        }

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int move : state.getValidMoves()) {
                State childState = state.cloneState();
                childState.makeMove(move);
                childState.switchPlayer();
                int eval = alphaBeta(childState, depth - 1, alpha, beta, false); // Evaluate as minimizing player
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval); // Update alpha
                if (beta <= alpha) {
                    break; // Prune remaining branches
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int move : state.getValidMoves()) {
                State childState = state.cloneState();
                childState.makeMove(move);
                childState.switchPlayer();
                int eval = alphaBeta(childState, depth - 1, alpha, beta, true); // Evaluate as maximizing player
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval); // Update beta
                if (beta <= alpha) {
                    break; // Prune remaining branches
                }
            }
            return minEval;
        }
    }

    // Finds the best move using Alpha-Beta Pruning
    private int getBestMoveAlphaBeta(State state, int depth) {
        visitedStates = 0; // Reset visited states counter
        long startTime = System.nanoTime(); // Start the timer

        int bestMove = -1; // Store the best move
        int bestValue = Integer.MIN_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        for (int move : state.getValidMoves()) {
            State childState = state.cloneState();
            childState.makeMove(move);
            childState.switchPlayer();
            int moveValue = alphaBeta(childState, depth - 1, alpha, beta, false); // Evaluate using Alpha-Beta pruning
            if (moveValue > bestValue) { // Update the best move
                bestValue = moveValue;
                bestMove = move;
            }
            alpha = Math.max(alpha, moveValue); // Update alpha
        }

        long endTime = System.nanoTime(); // End the timer
        double elapsedTime = (endTime - startTime) / 1_000_000_000.0; // Convert to seconds

        // Print debug information
        System.out.println("Alpha-Beta Pruning AI is thinking...");
        System.out.printf(" visited %d states%n", visitedStates);
        System.out.printf(" best move: @%d, value: %.6f%n", bestMove + 1, bestValue / 1000.0);
        System.out.printf(" Elapsed time: %.3f secs%n", elapsedTime);
        System.out.println(" @" + (bestMove + 1));

        return bestMove;
    }

    // Main method: Entry point of the program
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Game type selection
        System.out.println("Choose your game:");
        System.out.println("1. Tiny 3x3x3 Connect-Three");
        System.out.println("2. Wider 3x5x3 Connect-Three");
        System.out.println("3. Standard 6x7x4 Connect-Four");

        System.out.print("Choice: ");
        int choice = scanner.nextInt();
        int rows, cols, winCount;

        // Set board dimensions and win condition based on user's choice
        switch (choice) {
            case 1:
                rows = 3;
                cols = 3;
                winCount = 3;
                break;
            case 2:
                rows = 3;
                cols = 5;
                winCount = 3;
                break;
            case 3:
                rows = 6;
                cols = 7;
                winCount = 4;
                break;
            default:
                System.out.println("Invalid choice. Defaulting to Tiny 3x3x3.");
                rows = 3;
                cols = 3;
                winCount = 3;
                break;
        }

        // Opponent type selection
        System.out.println("Choose your opponent:");
        System.out.println("1. Human opponent (two-player mode)");
        System.out.println("2. An agent that plays randomly");
        System.out.println("3. An agent that uses MINIMAX");
        System.out.println("4. An agent that uses MINIMAX with alpha-beta pruning");

        System.out.print("Choice: ");
        int opponentType = scanner.nextInt();
        if (opponentType < 1 || opponentType > 4) {
            System.out.println("Invalid choice. Defaulting to Normal (2 user inputs).");
            opponentType = 1;
        }

        // Start the game
        ConnectFour game = new ConnectFour(rows, cols, winCount, opponentType);
        game.playGame(); // Begin the game loop
        scanner.close(); // Close the scanner
    }
}
