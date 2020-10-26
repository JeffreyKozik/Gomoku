import java.lang.reflect.*;
import org.junit.*;
import static org.junit.Assert.*;
    //assertEquals("Testing cycleSort's first for loop for 0 (no classification yards)", expectedArray0, array0);

/*
/**
 * Test Gomoku class
 * @author Jeffrey Kozik
 */
public class GomokuTester{
  
  Gomoku gomoku = new Gomoku();
  /** Tests the numberInLine method */
  @Test
  public void testNumberInLine(){
    Gomoku.Space[][] spaces = new Gomoku.Space[3][3];
    for(int i = 0; i < 3; i++){
      for(int j = 0; j < 3; j++){
        spaces[i][j] = Gomoku.Space.EMPTY;
      }
    }
    /** LOOP Counting the number of pieces in a row */
    /** Zero */
    spaces[0][2] = Gomoku.Space.WHITE;
    assertEquals("Number of pieces in a row, Zero", 2, gomoku.numberInLine(spaces, 0, 1, Gomoku.Direction.LEFT));
    /** One */
    assertEquals("Number of pieces in a row, One", 1, gomoku.numberInLine(spaces, 0, 2, Gomoku.Direction.UP));
    /** Many */
    spaces[1][1] = Gomoku.Space.WHITE;
    spaces[2][0] = Gomoku.Space.WHITE;
    assertEquals("Number of pieces in a row, Many", 3, gomoku.numberInLine(spaces, 0, 2, Gomoku.Direction.DOWNLEFT));
    /** First */
    spaces[0][0] = Gomoku.Space.BLACK;
    assertEquals("Number of pieces in a row, First", 1, gomoku.numberInLine(spaces, 0, 0, Gomoku.Direction.DOWN));
    /** Middle */
    spaces[0][1] = Gomoku.Space.BLACK;
    assertEquals("Number of pieces in a row, Middle", 2, gomoku.numberInLine(spaces, 0, 0, Gomoku.Direction.RIGHT));
    /** Last */
    spaces[2][2] = Gomoku.Space.BLACK;
    spaces[1][1] = Gomoku.Space.BLACK;
    assertEquals("Number of pieces in a row, Last", 3, gomoku.numberInLine(spaces, 2, 2, Gomoku.Direction.UPLEFT));
  }
  
  /** Tests the isOpen method */
  @Test
  public void testIsOpen(){
    Gomoku.Space[][] spaces = new Gomoku.Space[3][3];
    for(int i = 0; i < 3; i++){
      for(int j = 0; j < 3; j++){
        spaces[i][j] = Gomoku.Space.EMPTY;
      }
    }
    /** LOOP Going until the end of a line of pieces */
    /** Zero */
    assertTrue("Going until the end of a line of pieces, Zero", gomoku.isOpen(spaces, 1, 1, Gomoku.Direction.UPRIGHT));
    /** One */
    spaces[1][1] = Gomoku.Space.WHITE;
    spaces[2][2] = Gomoku.Space.BLACK;
    assertFalse("Going until the end of a line of pieces, One", gomoku.isOpen(spaces, 1, 1, Gomoku.Direction.DOWNRIGHT));
    /** Many */
    spaces[0][2] = Gomoku.Space.BLACK;
    spaces[1][2] = Gomoku.Space.BLACK;
    assertFalse("Going until the end of a line of pieces, Many", gomoku.isOpen(spaces, 2, 2, Gomoku.Direction.UP));
    /** First */
    spaces[0][0] = Gomoku.Space.WHITE;
    assertTrue("Going until the end of a line of pieces, First", gomoku.isOpen(spaces, 0, 0, Gomoku.Direction.RIGHT));
    /** Middle */
    spaces[1][0] = Gomoku.Space.WHITE;
    assertTrue("Going until the end of a line of pieces, Middle", gomoku.isOpen(spaces, 0, 0, Gomoku.Direction.DOWN));
    /** Last */
    spaces[2][0] = Gomoku.Space.WHITE;
    spaces[2][1] = Gomoku.Space.BLACK;
    assertFalse("Going until the end of a line of pieces, Last", gomoku.isOpen(spaces, 2, 2, Gomoku.Direction.LEFT));
    /** CONDITIONAL Determining if the end of a line of pieces is empty */
    /** True */
    assertTrue("Determining if the end of a line of pieces is empty, True", gomoku.isOpen(spaces, 1, 2, Gomoku.Direction.UPLEFT));
    /** False */
    assertFalse("Determining if the end of a line of pieces is empty, False", gomoku.isOpen(spaces, 1, 2, Gomoku.Direction.DOWNLEFT));
  }
  
  /** Tests the isFourFour method */
  @Test
  public void testIsFourFour(){
    Gomoku.Space[][] spaces = new Gomoku.Space[5][5];
    for(int i = 0; i < 5; i++){
      for(int j = 0; j < 5; j++){
        spaces[i][j] = Gomoku.Space.EMPTY;
      }
    }
    /** LOOP Going through all of the directions */
    /** Zero */
    assertTrue("Going through all of the directions, Zero", gomoku.isFourFour(spaces, 0, 0));
    spaces[0][0] = Gomoku.Space.EMPTY;
    /** One */
    spaces[4][0] = Gomoku.Space.BLACK;
    spaces[3][1] = Gomoku.Space.BLACK;
    spaces[1][3] = Gomoku.Space.BLACK;
    assertTrue("Going through all of the directions, One", gomoku.isFourFour(spaces, 2, 2));
    spaces[2][2] = Gomoku.Space.EMPTY;
    /** Many */
    spaces[1][1] = Gomoku.Space.BLACK;
    spaces[3][3] = Gomoku.Space.BLACK;
    spaces[4][4] = Gomoku.Space.BLACK;
    assertFalse("Going through all of the directions, Many", gomoku.isFourFour(spaces, 2, 2));
    /** First */
    spaces[0][0] = Gomoku.Space.BLACK;
    spaces[0][1] = Gomoku.Space.BLACK;
    spaces[0][2] = Gomoku.Space.BLACK;
    spaces[2][3] = Gomoku.Space.BLACK;
    assertFalse("Going through all of the directions, First", gomoku.isFourFour(spaces, 0, 3));
    /** Middle */
    assertFalse("Going through all of the directions, Middle", gomoku.isFourFour(spaces, 0, 3));
    /** Last */
    spaces[1][4] = Gomoku.Space.BLACK;
    spaces[3][4] = Gomoku.Space.BLACK;
    assertFalse("Going through all of the directions, Last", gomoku.isFourFour(spaces, 1, 2));
    /** CONDITIONAL If a pair of directions forms a four-four */
    /** True */
    spaces[2][0] = Gomoku.Space.BLACK;
    spaces[2][1] = Gomoku.Space.BLACK;
    assertFalse("If a pair of directions forms a four-four, True", gomoku.isFourFour(spaces, 2, 2));
    /** False */
    assertTrue("If a pair of directions forms a four-four, False", gomoku.isFourFour(spaces, 4, 2));
    /** CONDITIONAL If the four-four rule has been broken */
    /** True */
    spaces[4][0] = Gomoku.Space.EMPTY;
    spaces[4][1] = Gomoku.Space.BLACK;
    assertFalse("If the four-four rule has been broken, True", gomoku.isFourFour(spaces, 4, 3));
    /** False */
    assertTrue("If the four-four rule has been broken, False", gomoku.isFourFour(spaces, 2, 1));
  }
  
  /** Tests the isThreeThree method */
  @Test
  public void testIsThreeThree(){
    Gomoku.Space[][] spaces = new Gomoku.Space[5][5];
    for(int i = 0; i < 5; i++){
      for(int j = 0; j < 5; j++){
        spaces[i][j] = Gomoku.Space.EMPTY;
      }
    }
    /** LOOP Going through all of the directions */
    /** Zero */
    assertTrue("Going through all of the directions, Zero", gomoku.isThreeThree(spaces, 1, 1));
    /** One */
    spaces[3][3] = Gomoku.Space.BLACK;
    assertTrue("Going through all of the direction, One", gomoku.isThreeThree(spaces, 2, 2));
    spaces[2][2] = Gomoku.Space.EMPTY;
    /** Many */
    spaces[3][1] = Gomoku.Space.BLACK;
    spaces[1][3] = Gomoku.Space.BLACK;
    assertFalse("Going through all of the direction, Many", gomoku.isThreeThree(spaces, 2, 2));
    /** First */
    spaces[1][3] = Gomoku.Space.EMPTY;
    spaces[1][2] = Gomoku.Space.BLACK;
    spaces[2][3] = Gomoku.Space.BLACK;
    assertFalse("Going through all of the direction, First", gomoku.isThreeThree(spaces, 1, 3));
    /** Middle */
    assertFalse("Going through all of the direction, Middle", gomoku.isThreeThree(spaces, 1, 3));
    /** Last */
    spaces[3][1] = Gomoku.Space.EMPTY;
    spaces[3][2] = Gomoku.Space.BLACK;
    assertFalse("Going through all of the direction, Last", gomoku.isThreeThree(spaces, 2, 2));
    /** CONDITIONAL If a direction is open */
    /** True */
    spaces[2][1] = Gomoku.Space.BLACK;
    assertFalse("If a direction is open, True", gomoku.isThreeThree(spaces, 3, 1));
    /** False */
    spaces[3][0] = Gomoku.Space.WHITE;
    assertTrue("If a direction is open, False", gomoku.isThreeThree(spaces, 3, 1));
    /** CONDITIONAL If a pair of directions forms a three-three */
    /** True */
    assertFalse("If a pair of directions forms a three-three, True", gomoku.isThreeThree(spaces, 2, 2));
    /** False */
    assertTrue("If a pair of directions forms a three-three, False", gomoku.isThreeThree(spaces, 4, 4));
    /** CONDITIONAL If the three-three rule has been broken */
    /** True */
    spaces[1][1] = Gomoku.Space.EMPTY;
    spaces[2][1] = Gomoku.Space.EMPTY;
    spaces[1][3] = Gomoku.Space.BLACK;
    spaces[3][1] = Gomoku.Space.BLACK;
    assertFalse("If the three-three rule has been broken, True", gomoku.isThreeThree(spaces, 2, 2));
    /** False */
    assertTrue("If the three-three rule has been broken, False", gomoku.isThreeThree(spaces, 0, 0));
  }
  
  /** Tests the isWon method */
  @Test
  public void testIsWon(){
    Gomoku.Space[][] spaces = new Gomoku.Space[5][5];
    for(int i = 0; i < 5; i++){
      for(int j = 0; j < 5; j++){
        spaces[i][j] = Gomoku.Space.EMPTY;
      }
    }
    /** LOOP Going through all of the directions */
    /** Zero */
    spaces[0][0] = Gomoku.Space.WHITE;
    assertEquals("Going through all of the directions, Zero", Gomoku.Space.EMPTY, gomoku.isWon(spaces, 0, 0, Gomoku.Space.WHITE));
    /** One */
    spaces[0][1] = Gomoku.Space.WHITE;
    spaces[0][2] = Gomoku.Space.WHITE;
    spaces[0][3] = Gomoku.Space.WHITE;
    spaces[0][4] = Gomoku.Space.WHITE;
    assertEquals("Going through all of the directions, One", Gomoku.Space.WHITE, gomoku.isWon(spaces, 0, 0, Gomoku.Space.WHITE));
    /** Many */
    spaces[0][0] = Gomoku.Space.WHITE;
    spaces[0][4] = Gomoku.Space.EMPTY;
    spaces[1][3] = Gomoku.Space.WHITE;
    spaces[2][2] = Gomoku.Space.WHITE;
    spaces[3][1] = Gomoku.Space.WHITE;
    spaces[4][0] = Gomoku.Space.WHITE;
    spaces[0][4] = Gomoku.Space.WHITE;
    assertEquals("Going through all of the directions, Many", Gomoku.Space.WHITE, gomoku.isWon(spaces, 0, 4, Gomoku.Space.WHITE));
    /** First */
    spaces[4][1] = Gomoku.Space.WHITE;
    spaces[4][2] = Gomoku.Space.EMPTY;
    spaces[4][3] = Gomoku.Space.WHITE;
    spaces[4][4] = Gomoku.Space.WHITE;
    assertEquals("Going through all of the directions, First", Gomoku.Space.WHITE, gomoku.isWon(spaces, 4, 0, Gomoku.Space.WHITE));
    /** Middle */
    spaces[0][0] = Gomoku.Space.BLACK;
    spaces[1][0] = Gomoku.Space.BLACK;
    spaces[2][0] = Gomoku.Space.BLACK;
    spaces[3][0] = Gomoku.Space.BLACK;
    spaces[4][0] = Gomoku.Space.BLACK;
    assertEquals("Going through all of the directions, Middle", Gomoku.Space.BLACK, gomoku.isWon(spaces, 0, 0, Gomoku.Space.BLACK));
    /** Last */
    spaces[1][1] = Gomoku.Space.BLACK;
    spaces[2][2] = Gomoku.Space.BLACK;
    spaces[3][3] = Gomoku.Space.BLACK;
    spaces[4][4] = Gomoku.Space.BLACK;
    assertEquals("Going through all of the directions, Last", Gomoku.Space.BLACK, gomoku.isWon(spaces, 0, 0, Gomoku.Space.BLACK));
    /** CONDITIONAL The game has been won */
    /** True */
    spaces[4][0] = Gomoku.Space.WHITE;
    spaces[2][2] = Gomoku.Space.WHITE;
    assertEquals("The game has been won, True", Gomoku.Space.WHITE, gomoku.isWon(spaces, 0, 4, Gomoku.Space.WHITE));
    /** False */
    assertEquals("The game has been won, False", Gomoku.Space.EMPTY, gomoku.isWon(spaces, 0, 0, Gomoku.Space.BLACK));
  }
}


