import java.util.*;
import javafx.application.Application.*;
import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.event.*;
import javafx.scene.paint.*;
import javafx.geometry.Insets;

/**
 * The game of Gomoku
 * @author Jeffrey Kozik
 */
public class Gomoku extends Application {
  
  /**
   * Represents the condition of a space in the board - empty if no piece is on it, white and black if occupied by those respective pieces
   * Also represents which player's turn it is
   */
  enum Space{
    EMPTY, WHITE, BLACK;
  }
  
  /**
   * Represents direction. Used to determine how many pieces are in a row.
   */
  enum Direction{
    /** Each of the directions */
    LEFT(-1, 0, 0), RIGHT(1, 0, 0), UP(0, 1, 1), DOWN(0, -1, 1), UPRIGHT(1, 1, 2), UPLEFT(-1, 1, 3), DOWNLEFT(-1, -1, 2), DOWNRIGHT(1, -1, 3);   
    /** How many units in the x axis the direction is moving (horizontally) */
    private int xDirection;
    /** How many units in the y axis the direction is moving (vertically) */
    private int yDirection;
    /** Within an array storing direction pairs, where in the array each direction fits. 
      * Two directions going in opposite directions are stored together because combined they are how many pieces are in a row.
      */
    private int pair;
    /**
     * Creates each Direction with xDirection, yDirection, and its pair value
     * @param xDirection The number of units horizontally a direction moves
     * @param yDirection The number of units vertically a direction moves
     * @param pair The location in an array the direction is stored as well as its pair
     */
    private Direction(int xDirection, int yDirection, int pair){
      this.xDirection = xDirection;
      this.yDirection = yDirection;
      this.pair = pair;
    }
  }
  /** If computer is storing the string computer, the user will play a game versus the computer */
  private String computer = "";
  /** Stores whether or not its the computers turn */
  private boolean computersTurn = false;
  /** The importance given to a computer's move that causes 1 in a row on offense */
  private int oneOPoints = 1;
  /** The importance given to a computer's move that causes 2 in a row on offense */
  private int twoOPoints = 2;
  /** The importance given to a computer's move that causes 3 in a row on offense */
  private int threeOPoints = 4;
  /** The importance given to a computer's move that causes 4 in a row on offense */
  private int fourOPoints = 8;
  /** The importance given to a computer's move that causes 5 in a row on offense */
  private int fiveOPoints = 10000000;
  /** The importance given to a computer's move that blocks 1 in a row on defense */
  private int oneDPoints = 2;
  /** The importance given to a computer's move that blocks 2 in a row on defense */
  private int twoDPoints = 3;
  /** The importance given to a computer's move that blocks 3 in a row on defense */
  private int threeDPoints = 10;
  /** The importance given to a computer's move that blocks 4 in a row on defense */
  private int fourDPoints = 50;
  /** The importance given to a computers move that blocks 5 in a row on defense */
  private int fiveDPoints = 2000;
  /** Determines if moves are allowed or not - utilized at the end of the game because no moves can be made once the game is over */
  private boolean movesAllowed = true;
  /** The height and width of the game squares */
  private int spaceSize = 40;
  /** The number of rows in the board */
  private int rows = 19;
  /** The number of columns in the board */
  private int columns = 19;
  /** The number in a row needed to win */
  private int winInARow = 5;
  /** The board - a 2d array of buttons */
  private Button[][] board;;
  /** An array to store which state the board space is in - unoccupied, white, or black. */
  private Space[][] spaces;
  /** The inset value of the buttons on the board, determines spacing */
  private final int insetValueBoard = 1;
  /** The inset value of the pieces on the board, determines spacing */
  private final int insetValuePieces = 3;
  /** The corner radii value of the buttons on the board when a piece is showing */
  private final double cornerRadiiValue = 50;
  /** The background fill for the board */
  private BackgroundFill boardBackgroundFill = new BackgroundFill((Color.GREEN), CornerRadii.EMPTY, new Insets(insetValueBoard, insetValueBoard, insetValueBoard, insetValueBoard));
  /** The background fill of a white piece */
  private BackgroundFill whitePieceBackgroundFill = new BackgroundFill((Color.WHITE), new CornerRadii(cornerRadiiValue), new Insets(insetValuePieces, insetValuePieces, insetValuePieces, insetValuePieces));
  /** The background fill of a black piece */
  private BackgroundFill blackPieceBackgroundFill = new BackgroundFill((Color.BLACK), new CornerRadii(cornerRadiiValue), new Insets(insetValuePieces, insetValuePieces, insetValuePieces, insetValuePieces));
  /** The current player whose turn it is, true is white, false is black */
  private Space currentPlayer = Space.BLACK;
  
  /**
   * Creates a gomoku game with a board, the ability to add pieces, and rules
   */
  public void start(Stage primaryStage){
    /** Determines whether the iterator has been run through or not - this ensures if the user inputs more than 3 arguments only the first 3 are taken into consideration */
    boolean ranThrough = false;
    /** 
     * Counts the number of arguments that have been passed in.
     * The program only needs this metric in the case that there are only two arguments passed in because in that case
     * The user wants those two arguments to be the rows and columns
     * The do NOT want them to be the winInARow and the rows
     * This adjusts for that nuance
     */
    int numArgs = 0;
    /** A list storing the arguments passed in by the user */
    List<String> list = getParameters().getRaw();
    /** A list iterator to go through the list storing the arguments passed in by the user */
    ListIterator<String> listIterator = list.listIterator();
    /**
     * While there another element to loop through and the loop hasn't been run through already
     * The number of pieces in a row needed to win is changed to the user's preference
     * The number of rows and columns on the board is changed to the user's preference
     * Each iteration these changes happen - there really only will be one iteration because the second run through 
     * Arguments passed the first three would be evaluated which are irrelevant
     */
    while(listIterator.hasNext() && !ranThrough){
      /**
       * Essentially each of these metrics for the game is changed and once all of the metrics the user wants to be changed have been changed
       * A run time exception occurs which is caught 
       * The catch doesn't do anything unless there were only 2 arguments inputted in which case
       * The metrics are adjusted to what the user actually wants them to be
       */
      try{
        computer = listIterator.next();
        winInARow = Integer.parseInt(computer);
        numArgs++;
        rows = Integer.parseInt(listIterator.next());
        numArgs++;
        columns = Integer.parseInt(listIterator.next());
        numArgs++;
      }
      catch (java.lang.RuntimeException exception1){
        /** If there are only two arguments inputted, the user wants those two inputs to change the number of rows and columns, so that is adjusted for */
        if(numArgs == 2){
          columns = rows;
          /** If the number of columns is less than or equal to 0 (an unrealistic number) a message is printed to the user and the number of columns is changed to 19 */
          if(columns <= 0){
            System.out.println("Sorry, but the number of columns you wanted isn't possible! So, we made 19 columns, recompile and run this program again if this isn't what you wanted.");
            columns = 19;
          }
          rows = winInARow;
          /** If the number of rows is less than or equal to 0 (an unrealistic number) a message is printed to the user and the number of rows is changed to 19 */
          if(rows <= 0){
            System.out.println("Sorry, but the number of rows you wanted isn't possible! So, we made 19 rows, recompile and run this program again if this isn't what you wanted.");
            rows = 19;
          }
          winInARow = 5;
        }
      }
      ranThrough = true;
    }
    /** If the number of rows is less than or equal to 0 (an unrealistic number) a message is printed to the user and the number of rows is changed to 19 */
    if(rows <= 0){
      System.out.println("Sorry, but the number of rows you wanted isn't possible! So, we made 19 rows, recompile and run this program again if this isn't what you wanted.");
      rows = 19;
    }
    /** If the number of columns is less than or equal to 0 (an unrealistic number) a message is printed to the user and the number of columns is changed to 19 */
    if(columns <= 0){
      System.out.println("Sorry, but the number of columns you wanted isn't possible! So, we made 19 columns, recompile and run this program again if this isn't what you wanted.");
      columns = 19;
    }
    board = new Button[rows][columns];
    spaces = new Space[rows][columns];
    /** Creates a new grid pane - the board */
    GridPane grid = new GridPane();
    /** 
     * Loops through the rows of the board to make each button and display it properly as well as to make all of the spaces empty
     * Each iteration it goes through all of the columns in the row
     */
    for(int i = 0; i < rows; i++){
      /**
       * Loops through all of the columns in a specific row and creates the buttons in that column, displays it properly, and makes all of the spaces start as empty
       * Each iteration it goes through a single column and performs this behavior on the column
       */
      for(int j = 0; j < columns; j++){
        spaces[i][j] = Space.EMPTY;
        board[i][j] = new Button();
        board[i][j].setMinWidth(spaceSize);
        board[i][j].setMinHeight(spaceSize);
        board[i][j].setBackground(new Background(boardBackgroundFill));
        board[i][j].setOnAction(new moveMade(i, j));
        grid.add(board[i][j], j, i);
      }
    }
    /** Creates a new scene containing the grid */
    Scene scene = new Scene(grid);
    /** Creates a new stage with the scene containing the grid */
    primaryStage.setScene(scene);
    /** Makes the primary stage visible */
    primaryStage.show();
  }
  
  /**
   * Determines how many pieces are in a row in a certain direction
   * @param spaces The array of Space to use, in other words the logic behind the board
   * @param row The row at which to determine how many are "in a row"
   * @param column The column at which to determine how many are "in a row"
   * @param direction The direction to determine in
   * @return The number of pieces of the same color in a row
   */
  public int numberInLine(Space[][] spaces, int row, int column, Direction direction){
    /** The current color of the player whose turn it is */
    Space currentColor = spaces[row][column];
    /** The number of pieces in a row */
    int inARow = 0;
    /**
     * Loops through until all of the same color pieces have been counted - stops either when hitting the edge of the board or a piece of a different color or an empty space
     * Each iteration it increments the number of pieces in a row and changes the row and column to be the next space to check in that direction
     */
    for(; row > -1 && column > -1 && row < spaces.length && column < spaces[0].length && spaces[row][column] == currentColor; row-=direction.yDirection, column+=direction.xDirection, inARow++){}
    return inARow;
  }
  
  /**
   * Determines whether at the end of a line of the same color of pieces if there is an empty space or not
   * @param spaces The array of Space to use, in other words the logic behind the board
   * @param row The row at which to start the determination of if the line ends in an empty space
   * @param column The column at which to start the determination of if the line ends in an empty space
   * @param direction The direction to determine in
   * @return Whether or not the line ends in an empty space
   */
  public boolean isOpen(Space[][] spaces, int row, int column, Direction direction){
    /** The current color of the player whose turn it is */
    Space currentColor = spaces[row][column];
    /**
     * Loops through until all of the same color pieces have been counted - stops either when hitting the edge of the board or a piece of a different color or an empty space
     * Each iteration it increments the number of pieces in a row and changes the row and column to be the next space to check in that direction
     * True is returned if the reason the loop stops is because it has hit an empty space
     */
    for(; row > -1 && column > -1 && row < spaces.length && column < spaces[0].length && (spaces[row][column] == currentColor || spaces[row][column] == Space.EMPTY); row-=direction.yDirection, column+=direction.xDirection){
      /**
       * If the space is empty true is returned because that means the line of in a row pieces is open
       * Otherwise the loop continues
       */
      if(spaces[row][column] == Space.EMPTY){
        return true;
      }
    }
    return false;
  }
  
  /**
   * Determines whether a move would cause a group of two or more four in a rows
   * Or generically one less than the winning amount in a row - if 6 in a row are needed to win this determines is if a group of two or more five in a rows is caused
   * @param spaces The array of Space to use, in other words the logic behind the board
   * @param row The row at which to start the determination of if the line is 4 in a row
   * @param column The column at which to start the determination of if the line is 4 in a row
   * @return Whether or not the move would cause two or more groups of four in a row
   */
  public boolean isFourFour(Space[][] spaces, int row, int column){
    /** The space in the logic behind the gameboard is changed to the color of the player whose turn it is - this will be changed back to empty if the move is illegal */
    spaces[row][column] = currentPlayer;
    /** An array to store how many are in a row, not just in one direction but in the two opposite directions.
      * The first dimension is how many are in a row, the second dimension is how many of the direction pair have been counted
      */
    int[][] fours = new int[4][2];
    /** The number of groups of four in a row this would cause */
    int foursInARow = 0;
    /**
     * Loops through all of the directions in the enum and determines how many are in a row with the numberInLine method
     * This is incremented to the location in the array each direction corresponds to along with its opposite
     * Also, each time a direction in a direction pair is incremented the second dimension is incremented as well so that the program knows when both parts of the pair have been counted 
     * Each iteration the array is updated based on the amount in a row and how many of a pair of directions have been counted
     * If a move would cause one group of four in a row, the number of fours in a row is incremented
     */
    for(Direction d: Direction.values()){
      fours[d.pair][0] += numberInLine(spaces, row, column, d);
      fours[d.pair][1]++;
      /**
       * If the number in a row in a direction pair is equal to the amount needed to win (4 is the default) and both parts of pair have been counted
       * The number of fours in a row is incremented
       * Otherwise the loop continues
       * The reason we want to know if it is equal to the number needed to win in a row is because the original location we start from
       * That space is double counted
       */
      if(fours[d.pair][0] == winInARow && fours[d.pair][1] == 2){
        foursInARow++;
      }
    }
    /**
     * If there are more than one groups of four in a row created, then false is returned because we don't want this move to be allowed
     * Also, the space on the board is turned back to empty because the piece isn't allowed there
     * And a message is printed to the user informing them why their move wasn't allowed
     */
    if(foursInARow > 1){
      spaces[row][column] = Space.EMPTY;
      /** If it is the computers turn no messages should be displayed when they are deciding on a move so messages are only printed if it's the users turn */
      if(computersTurn == false){
        System.out.println("Please make another move, your current move violates the four four rule of Gomoku.");
      }
      return false;
    }
    /** If it is the computers turn the space they are considering returns to empty because they may or may not choose that space */
    if(computersTurn == true){
      spaces[row][column] = Space.EMPTY;
    }
    /** Otherwise true is returned because no rule has been violated */
    return true;
  }
  
  /**
   * Determines whether a move would cause a group of two or more threes in a rows such that both groups of three in a row are open on all four ends
   * Or generically one less than the winning amount in a row - if 6 in a row are needed to win this determines  if a group of two or more four in a rows is caused (that are open on both ends)
   * @param spaces The array of Space to use, in other words the logic behind the board
   * @param row The row at which to start the determination of if the line is 3 in a row and open on both ends
   * @param column The column at which to start the determination of if the line is 3 in a row and open on both ends
   * @return Whether or not the move would cause two or more groups of three in a row that are open on both ends
   */
  public boolean isThreeThree(Space[][] spaces, int row, int column){
    /** The space in the logic behind the gameboard is changed to the color of the player whose turn it is - this will be changed back to empty if the move is illegal */
    spaces[row][column] = currentPlayer;
    /** An array to store how many are in a row, not just in one direction but in the two opposite directions.
      * The first dimension is how many are in a row, the second dimension is how many of the direction pair have been counted
      */
    int[][] threes = new int[4][2];
    /** The number of groups of three in a row this would cause */
    int threesInARow = 0;
    /**
     * Loops through all of the directions in the enum and determines how many are in a row with the numberInLine method
     * This is incremented to the location in the array each direction corresponds to along with its opposite
     * Also, each time a direction in a direction pair is incremented the second dimension is incremented as well so that the program knows when both parts of the pair have been counted 
     * Each iteration the array is updated based on the amount in a row and how many of a pair of directions have been counted
     * If a move would cause one group of three in a row, the number of threes in a row is incremented
     */
    for(Direction d: Direction.values()){
      /** 
       * If in direction d, the space after those in a row is an empty space
       * The element in the array corresponding to this direction and its corresponding opposite pair and the number in a row is incremented by how many are in a row
       * The element in the array corresponding to this direction and its corresponding opposite pair and the number of directions counted is incremented by one
       * Otherwise the program continues because this direction doesn't create a potential scenario of threes in a row open on both ends
       */
      if(isOpen(spaces, row, column, d)){
        threes[d.pair][0] += numberInLine(spaces, row, column, d);
        threes[d.pair][1]++;
      }
      /**
       * If the number in a row in a direction pair is equal to the amount needed to win minus 1 (3 is the default) and both parts of pair have been counted
       * The number of threes in a row is incremented
       * Otherwise the loop continues
       * The reason we want to know if it is equal to the number needed to win in a row - 1 (not - 2) is because the original location we start from
       * That space is double counted
       */
      if(threes[d.pair][0] == winInARow - 1 && threes[d.pair][1] == 2){
        threesInARow++;
      }
    }
    /**
     * If there are more than one groups of three in a row created, then false is returned because we don't want this move to be allowed
     * Also, the space on the board is turned back to empty because the piece isn't allowed there
     * And a message is printed to the user informing them why their move wasn't allowed
     */
    if(threesInARow > 1){
      spaces[row][column] = Space.EMPTY;
      /** If it is the computers turn no messages should be displayed when they are deciding on a move so messages are only printed if it's the users turn */
      if(computersTurn == false){
        System.out.println("Please make another move, your current move violates the three three rule of Gomoku.");
      }
      return false;
    }
    /** If it is the computers turn the space they are considering returns to empty because they may or may not choose that space */
    if(computersTurn == true){
      spaces[row][column] = Space.EMPTY;
    }
    /** Otherwise true is returned because no rule has been violated */
    return true;
  }
  
  /**
   * Determines if a player has won the game
   * @param spaces The array of Space to use, in other words the logic behind the board
   * @param row The row at which to start the determination of if a player has won
   * @param column The column at which to start the determination of if a player has won
   * @param space The player whose turn it is and has potentially won
   * @return The player who has won
   */
  public Space isWon(Space[][] spaces, int row, int column, Space space){
    /** An array to store how many are in a row, not just in one direction but in the two opposite directions.
      * The first dimension is how many are in a row, the second dimension is how many of the direction pair have been counted
      */
    int[][] won = new int[4][2];
    /**
     * Loops through all of the directions in the enum and determines how many are in a row with the numberInLine method
     * This is incremented to the location in the array each direction corresponds to along with its opposite
     * Also, each time a direction in a direction pair is incremented the second dimension is incremented as well so that the program knows when both parts of the pair have been counted 
     * Each iteration the array is updated based on the amount in a row and how many of a pair of directions have been counted
     * If the number in a row is exactly equal to the number needed to win in a row (winInARow + 1 because the piece started from is double counted), the player who has won is returned
     */
    for(Direction d: Direction.values()){
      won[d.pair][0] += numberInLine(spaces, row, column, d);
      won[d.pair][1]++;
      /**
       * If the number in a row is exactly equal to the number needed to win in a row (winInARow + 1 because the piece started from is double counted), 
       * Moves are no longer allowed because the game is over
       * And the player who has won is returned
       * Otherwise the loop continues
       */
      if(won[d.pair][0] == winInARow + 1 && won[d.pair][1] == 2){
        movesAllowed = false;
        return space;
      }
    }
    /* Otherwise empty is returned because no player has won the game */
    return Space.EMPTY;
  }
  
  /** Nested class to determine what happens after a move has been made */
  public class moveMade implements EventHandler<ActionEvent> {
    /** The row a move was made in */
    private int rowLocation;
    /** The column a move was made in */
    private int columnLocation;
    
    /**
     * Creates an instance of a move being made with both the row and column locations of the move
     * @param rowLocation The row the move was made in
     * @param columnLocation The column the move was made in
     */
    public moveMade(int rowLocation, int columnLocation){
      this.rowLocation = rowLocation;
      this.columnLocation = columnLocation;
    }
    
    /**
     * Determines what happens when a button is pressed
     * @param e The Action Event happening
     */
    public void handle(ActionEvent e){
      /** Extracting the button from what event has happened */
      Button b = (Button)e.getSource();
      /** If the move is allowed, meaning the game isn't over, then the subsequent if statement can be carried forward and both the board and logic behind the board can be changed */
      if(movesAllowed){
        /**
         * If the space being clicked is empty, the move is legal (it doesn't violate the four four or three three rules)
         * A series of conditionals are carried out from there to determine what to do
         * Determines if the game has been won
         * Sets the space to have the correct piece on them 
         */
        if(spaces[rowLocation][columnLocation] == Space.EMPTY && isFourFour(spaces, rowLocation, columnLocation) && isThreeThree(spaces, rowLocation, columnLocation)){
          /** If the game has been won, the winner is announced */
          if(isWon(spaces, rowLocation, columnLocation, currentPlayer) != Space.EMPTY){
            System.out.println(isWon(spaces, rowLocation, columnLocation, currentPlayer) + " WON!");
          }
          /** If computer is enabled then instead of usual two player gameplay preceding, the computer is the second player */
          if(computer.equals("computer")){
            b.setBackground(new Background(boardBackgroundFill, blackPieceBackgroundFill));
            /** If the game has been won, the winner is announced */
            computersTurn = true;
            /** An array storing the computers current best move - the first element represents the row and the second element represents the column */
            int[] bestMove = new int[2];
            /** A point value assigned to each potential move the computer could make */
            int bestMovePoints = 0;
            /**
             * Loops through all of the rows in the logic behind the board
             * Each incrementation, the columns in each row are looped through as well
             * The purpose of this is to assign point values to every possible move the computer can make and then make the best move or the move with the highest point value
             */
            for(int i = 0; i < spaces.length; i++){
              /**
               * Loops through all of the columns in each row 
               * Each incrementation an overarching if statement to ensure the potential move is even legal is made
               * Then a series of for statements determining the quality of a move are gone through
               */
              for(int j = 0; j < spaces[0].length; j++){
                /** If a space is empty and moving there wouldn't violate the four four or three three rules, the move is evaluated, otherwise it is skipped */
                if(spaces[i][j] == Space.EMPTY && isFourFour(spaces, i, j) && isThreeThree(spaces, i, j)){
                  /** The point value of the current move */
                  int tempPoints = 0;
                  /** How defensively powerful the current move is */
                  int defensePoints = 0;
                  /** How offensively powerful the current move is */
                  int offensePoints = 0;
                  spaces[i][j] = Space.WHITE;
                  /** 
                   * An array storing all four directions off of a piece, the first dimension represents how many pieces are in a row, and the second dimension represents how many of 
                   * the direction pair has been checked 
                   */
                  int[][] directional = new int[4][2];
                  /**
                   * Every direction is looped through and evaluated
                   * Each iteration an offensive point value is assigned to a move based upon how many pieces in a row that move would form
                   */
                  for(Direction d: Direction.values()){
                    directional[d.pair][0] += numberInLine(spaces, i, j, d);
                    directional[d.pair][1]++;
                    /**
                     * If both directions of the direction pair have been counted, the effectiveness of that move in terms of offense in that direction is evaluated
                     */
                    if(directional[d.pair][1] == 2){
                      /** If the move creates 1 piece in a row, the according number of points is incremented to offensive points in general */
                      if(directional[d.pair][0] == 2){
                        offensePoints += oneOPoints;
                      }
                      /** If the move creates 2 pieces in a row, the according number of points is incremented to offensive points in general */
                      if(directional[d.pair][0] == 3){
                        offensePoints += twoOPoints;
                      }
                      /** If the move creates 3 pieces in a row, the according number of points is incremented to offensive points in general */
                      if(directional[d.pair][0] == 4){
                        offensePoints += threeOPoints;
                      }
                      /** If the move creates 4 pieces in a row, the according number of points is incremented to offensive points in general */
                      if(directional[d.pair][0] == 5){
                        offensePoints += fourOPoints;
                      }
                      /** If the move creates 5 pieces in a row, the according number of points is incremented to offensive points in general */
                      if(directional[d.pair][0] == 6){
                        offensePoints += fiveOPoints;
                      }
                      directional[d.pair][0] = 0;
                      directional[d.pair][1] = 0;
                    }
                  }
                  spaces[i][j] = Space.BLACK;
                  /**
                   * Every direction is looped through and evaluated
                   * Each iteration a defensive point value is assigned to a move based upon how many pieces in a row that move would form
                   */
                  for(Direction d: Direction.values()){
                    directional[d.pair][0] += numberInLine(spaces, i, j, d);
                    directional[d.pair][1]++;
                    /**
                     * If both directions of the direction pair have been counted, the effectiveness of that move in terms of offense in that direction is evaluated
                     */
                    if(directional[d.pair][1] == 2){
                      /** If a move blocks a 1 in a row, the according number of points is incremented to defensive points in general */
                      if(directional[d.pair][0] == 3){
                        offensePoints += twoDPoints;
                      }
                      /** If a move blocks a 2 in a row, the according number of points is incremented to defensive points in general */
                      if(directional[d.pair][0] == 4){
                        offensePoints += threeDPoints;
                      }
                      /** If a move blocks a 3 in a row, the according number of points is incremented to defensive points in general */
                      if(directional[d.pair][0] == 5){
                        offensePoints += fourDPoints;
                      }
                      /** If a move blocks a 4 in a row, the according number of points is incremented to defensive points in general */
                      if(directional[d.pair][0] == 6){
                        offensePoints += fiveDPoints;
                      }
                      directional[d.pair][0] = 0;
                      directional[d.pair][1] = 0;
                    }
                  }
                  spaces[i][j] = Space.EMPTY;
                  tempPoints += (offensePoints + defensePoints);
                  /** If this moves points is better than or the same as the currently best thought move, this becomes the new best move */
                  if(tempPoints >= bestMovePoints){
                    bestMove[0] = i;
                    bestMove[1] = j;
                    bestMovePoints = tempPoints;
                  }
                }
              }
            }
            spaces[bestMove[0]][bestMove[1]] = Space.WHITE;
            /** If the computer won it rubs it in your face */
            if(isWon(spaces, bestMove[0], bestMove[1], Space.WHITE) != Space.EMPTY){
              System.out.println("COMPUTER DOMINATION IS NEARING. YOU LOST.");
            }
            board[bestMove[0]][bestMove[1]].setBackground(new Background(boardBackgroundFill, whitePieceBackgroundFill));
            currentPlayer = Space.BLACK;
            computersTurn = false;
          }
          else { 
            /** If it's white's turn, the space they clicked places a white piece down and now it's black's turn*/
            if(currentPlayer == Space.WHITE){
              b.setBackground(new Background(boardBackgroundFill, whitePieceBackgroundFill));
              currentPlayer = Space.BLACK;
            }
            /** If it's black's turn, the space they clicked places a black piece down and now it's white's turn */
            else{
              b.setBackground(new Background(boardBackgroundFill, blackPieceBackgroundFill));
              currentPlayer = Space.WHITE;
            }
          }
        }
      }
    }
  }
  
  /**
   * Processes the arguments the user inputs
   * @param args The arguments inputted
   */
  public static void main(String[] args){
    Application.launch(args);
  }
  }