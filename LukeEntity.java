public class LukeEntity extends Entity
{    
    
    public LukeEntity(final Game g, final String r, final int newX, final int newY) {
        super(r, newX, newY, true);
        this.game = g;
        this.map = g.getTileMap();
    }
    
    protected boolean isTileAbove() {

        // if luke's top-left or top-right corner is in a tile
        return map.getTile(right / 32, (top - 1) / 32) != null || map.getTile(left / 32, (top - 1) / 32) != null;
    }
    
    public boolean isTileBelow() {
    	
    	// if luke's bottom-left or bottom-right corner is in a tile
    	try {
    		return map.getTile(right / 32, (bottom + 1) / 32) != null || map.getTile(left / 32, (bottom + 1) / 32) != null;
    	} catch (Exception e) {
    		return false;
    	}
    	
    }
    
    protected boolean isTileLeft() {
        
    	// if luke's top-left or bottom-left corner is in a tile
        return map.getTile((left - 1) / 32, top / 32) != null || map.getTile((left - 1) / 32, bottom / 32) != null;
    }
    
    protected boolean isTileRight() {
        
    	// if luke's top-right or bottom-right corner is in a tile
    	try {
    		return map.getTile((right + 1) / 32, top / 32) != null || map.getTile((right + 1) / 32, bottom / 32) != null;
    	} catch (Exception e) {
    		return true;
    	}
    }
    
    public void collidedWith(final Entity other) {
    }
}
