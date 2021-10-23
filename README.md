# Java-Game

Dear Carter And Emily

I've created a new folder in bin called "maps".
It stores the level layout as a txt file.
You'll need to download tile images a and b, or make your own.
I've also provided my luke, which is currently a square.

Currently, any level will be rendered incorrectly.
The orientation is mirrored, then rotated 90 degrees counterclockwise.

For example, the following arrangement

        AAAA    
        BBBB    
    AAAA    BBBB
    BBBB    AAAA
    AAAAAAAAAAAA

Becomes this in the actual game

      ABA
      ABA
      ABA
      ABA
    AB  A
    AB  A
    AB  A
    AB  A
      BAA
      BAA
      BAA
      BAA
  
It is reading each column and then printing it as a row.
Pls halp.

