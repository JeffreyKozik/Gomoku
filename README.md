# Gomoku
<h2> Rules </h2>
<ul> 
  <li>Three-and-three rule: player can't make a move that allows them to have 2 sets of three in a row where both sets are open on both ends </li>
  <li>Four-and-four rule: player can't make a move that allows them to have 2 sets of four in a row (whether or not the sets are open on the ends)</li>
  <li>Overline rule: player must win with exactly 5 in a row</li>
</ul>

Default board size is 19x19 and default needed to win is 5 in a row

<h2> Game Modes </h2>
<ul> 
  <li>java Gomoku creates a 2 player gomoku game on a 19x19 board where 5 in a row wins and all 3 rules are followed as stated</li>
  <li>java Gomoku 5 10 creates a 2 player gomoku game on a 5x10 board (5 rows, 10 columns) where 5 in a row wins and all 3 rules are followed as stated</li>
  <li>java Gomoku 6 creates a 2 player gomoku game on a 19x19 board where 6 in a row wins and the three-and-three rule becomes "player can't make a move that allows them to have 2 sets of FOUR in a row where both sets are open on both ends" and the four-and-four rule becomes "player can't make a move that allows them to have 2 sets of FIVE in a row (whether or not the sets are open on the ends)" and the overline rule becomes "player must win with exactly SIX in a row"</li>
  <li>java Gomoku 6 5 10 is a combination of the last 2 versions (6 in a row wins and it's played on a 5x10 board)</li>
  <li>java Gomoku computer creates a default game (19x19, 5 in a row wins) where the user goes first and plays against the AI. The way the AI works is it assigns a point value to creating 2, 3, 4, 5 in a row sets and to blocking 2, 3, 4, and 5 in a row sets and it then chooses the move with the highest point value. I designed it to be pretty defensive, although you can change the values in the code if you'd like. It's actually pretty good! It has beaten me on multiple occasions and has beaten multiple of my friends. I eventually figured out a surefire strategy, but I was impressed with how effective the AI was without even incorporating machine learning or anything as sophisticated as that. </li>
</ul>

<h2> Notes </h2>
<ul>
  <li>This program uses JavaFX which doesn't work with the latest version of Java (JDK 15.0.1 https://jdk.java.net/15/) last time I checked. I used Java 8 (https://docs.aws.amazon.com/corretto/latest/corretto-8-ug/downloads-list.html)</li>
  <li> Uses JUnit Testing to ensure program works correctly. </li>
  <li> This game was created December 2019 as part of my Java Programming class. </li>
</ul>








