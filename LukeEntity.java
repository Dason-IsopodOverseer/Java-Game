public class LukeEntity extends Entity
{    
    
    public LukeEntity(final Game g, final String r, final int newX, final int newY) {
        super(r, newX, newY, true);
        this.game = g;
        this.map = g.getTileMap();
    }
    
    public void setMap (TileMap m) {
    	map = m;
    }
    
    protected boolean isTileAbove() {

        // if luke's top-left or top-right corner is in a tile
        return map.getTile(right / 96, (top - 1) / 96) != null || map.getTile(left / 96, (top - 1) / 96) != null;
    }
    
    public boolean isTileBelow() {
    	
    	// if luke's bottom-left or bottom-right corner is in a tile
    	try {
    		return map.getTile(right / 96, (bottom + 1) / 96) != null || map.getTile(left / 96, (bottom + 1) / 96) != null;
    	} catch (Exception e) {
    		return false;
    	}
    	
    }
    
    protected boolean isTileLeft() {
        
    	// if luke's top-left or bottom-left corner is in a tile
        return map.getTile((left - 1) / 96, top / 96) != null || map.getTile((left - 1) / 96, bottom / 96) != null;
    }
    
    protected boolean isTileRight() {
        
    	// if luke's top-right or bottom-right corner is in a tile
    	try {
    		return map.getTile((right + 1) / 96, top / 96) != null || map.getTile((right + 1) / 96, bottom / 96) != null;
    	} catch (Exception e) {
    		return true;
    	}
    }
    
    public void collidedWith(final Entity other) {
    	if (other instanceof KlingonEntity) {
    		game.lose();
    	}
    }
    
    protected char getTileDirectlyBelow() {
    	String s = "";
    	if (map.getTile(right / 96, (bottom + 1) / 96) != null && map.getTile(left / 96, (bottom + 1) / 96) != null) {
    		if (map.getTile(right / 96, (bottom + 1) / 96) == map.getTile(left / 96, (bottom + 1) / 96))
    		s = (String) map.tileConfig.get((bottom + 1) /96);
    		return s.charAt(right / 96);
    	}
    	return 'x';
    }
}