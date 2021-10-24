# Java-Game
Eidted files: Entity, Game, TileMap

Dear Carter And Emily

BUG IS FIXED! :)
I've created a new folder in bin called "maps".
It stores the level layout as a txt file.
You'll need to download tile images a and b, or make your own.
I've also provided my luke, which is currently a square.

NOTE THAT THE TILE ARRAY FLIPS THE ORIGINAL LEVEL

For example, the following arrangement

        AAAA    
        BBBB    
    AAAA    BBBB
    BBBB    AAAA
    AAAAAAAAAAAA

Becomes this in tile array

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
  
It is reading each column and then saving it as a row.
But in the real game, it will be rendered correctly.

