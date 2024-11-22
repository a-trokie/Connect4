# Connect Four with Multiple AI Opponents

## Overview
This project is a **Connect Four game** implemented in Java with multiple game modes and AI opponents. Players can choose between different board sizes and opponents, including:
1. A human opponent (two-player mode).
2. An AI agent that plays randomly.
3. An AI agent that uses the **Minimax algorithm**.
4. An AI agent that uses **Minimax with Alpha-Beta Pruning** for improved performance.

## Features
- **Dynamic Board Sizes**: Choose between 3x3, 3x5, and 6x7 grids.
- **Multiple Opponents**: Play against another player or AI with varying strategies.
- **Efficient AI**: Leverages Alpha-Beta Pruning to make smarter moves with fewer computations.

## How It Works
1. **State Representation**:
   - The game board is represented as a 2D array.
   - Each player's move updates the board state.

2. **AI Algorithms**:
   - **Random AI**: Selects a valid move at random.
   - **Minimax AI**: Explores all possible moves to make the best decision.
   - **Minimax with Alpha-Beta Pruning**: Optimized version of Minimax, reducing the number of states evaluated.

3. **Game Rules**:
   - Players take turns dropping their pieces into a column.
   - The game ends when a player achieves the required number of consecutive pieces (horizontally, vertically, or diagonally) or the board is full.

## How to Run the Project
1. **Compile the Code**:
   - Ensure you have Java installed on your system.
   - Navigate to the directory containing the code and compile the files:
     ``` make ```

2. **Run the Game**:
   - Execute the main program:
     ``` make run ```
    
3. **Clean the Directory**:
   - Clean The Directory:
     ``` make clean ```


3. **Follow the Prompts**:
   - Select the board size and opponent type from the menu.
   - If playing against an AI, observe its decision-making process (e.g., number of states visited, elapsed time).

## Example Gameplay
```
Choose your game:

Tiny 3x3x3 Connect-Three
Wider 3x5x3 Connect-Three
Standard 6x7x4 Connect-Four

Choose your opponent:
Human opponent (two-player mode).
An agent that plays randomly
An agent that uses MINIMAX
An agent that uses MINIMAX with alpha-beta pruning

```

## Future Enhancements
- Implement additional AI strategies (e.g., Monte Carlo Tree Search).
- Add a graphical user interface (GUI) for improved user experience.