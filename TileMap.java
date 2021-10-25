import java.io.*;
import java.util.ArrayList; // import the ArrayList class

public class TileMap {

	private Sprite[][] tiles;
	public ArrayList tileConfig = new ArrayList(); // stores the configuration of different tiles after being read by mapReader
	private Sprite a;
	private Sprite b;
	private int height = 0; // stores the height of the map
	private int width = 0; // stores the width of the map
	
	public TileMap(String tileFile) {
		
		// read the txt tileFile and populate the empty tileConfig arraylist
		// also gets width and height
		mapReader(tileFile);
		
		tiles = new Sprite[width][height];
		
		// set sprites a and b to corresponding tile images
		a = (SpriteStore.get()).getSprite("sprites/a.png");
		b = (SpriteStore.get()).getSprite("sprites/b.png");
		fillMap();
	}
	
	public int getWidth() {
		return tiles[0].length;
	}
	
	public int getHeight() {
		return tiles.length;
	}
	
	// get width - tiles.length
	// get height - tiles[0].length
	public Sprite getTile(int x, int y) {
			return tiles[x][y];
	}
	
	public void setTile(int x, int y, Sprite tile) {
		tiles[x][y] = tile;
	}
	
	// fills sprite array with appropriate tiles (based on string [] input)
	private Sprite[][] fillMap() {
		
	    // begin to parse!
	    for (int y = 0; y < width; y++) {
	        for (int x = 0; x < height; x++) {
	        	String line = (String) tileConfig.get(x);
	            char ch = line.charAt(y);
	            
	            // check if the char represents tile A, B, C, etc.
				if (ch == 'A') {
					tiles[y][x] = a;
				} else if (ch == 'B') {
					tiles[y][x] = b;
				} else {
					tiles[y][x] = null;
				}
	        }
	    }
		return tiles;
	}
	
	// reads the tileFile ad stores it in tileConfig
	private void mapReader(String tileFile) {
		
        // try to retrieve file contents
        try {
        	
        	// input
            String folderName = "/maps/";
            String resource = tileFile;

			// this is the path within the jar file
			InputStream input = TileMap.class.getResourceAsStream(folderName + resource);
			if (input == null) {
				
				// this is how we load file within editor (eg eclipse)
				input = TileMap.class.getClassLoader().getResourceAsStream(resource);
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(input));
			in.mark(Short.MAX_VALUE);  // see api

		    while (true) {
		        String line = in.readLine();
		        
		        // no more lines to read
		        if (line == null) {
		            in.close();
		            break;
		        }
		        
		        // add every line except for comments
		        if (!line.startsWith("#")) {
		            tileConfig.add(line);
		            
		            // set the width
		            width = Math.max(width, line.length());
		        }
		    }
		    
		    // set the height
		    height = tileConfig.size();
        } catch (Exception e) {
        	System.out.println("File Input Error");
        } // end of try catch
    }
}

