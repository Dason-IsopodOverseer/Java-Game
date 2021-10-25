public class KlingonEntity extends Entity {    
    private int speed = 50;
    private final int fallingSpeed = 100;
	
    public KlingonEntity(final Game g, final String r, final int newX, final int newY) {
        super(r, newX, newY, true);
        game = g;
        map = g.getTileMap();
        dx = speed;
    }
    
    public void move(long delta) {
    	dy = fallingSpeed;
    	super.move(delta);
    	
    	// check if this entity should turn around
		if (isTileBelow() && !isTileCompletelyBelow()) {
			dx = -dx;
			x += (delta * dx) / 500;
		}
    } // move
    
    private boolean isTileCompletelyBelow() {
    	
    	// if entity's bottom-left or bottom-right corner is in a tile
     	try {
     		return map.getTile(right / 40, (bottom + 1) / 40) != null && map.getTile(left / 40, (bottom + 1) / 40) != null;
     	} catch (Exception e) {
     		return false;
     	}
    } // isTileCompletelyBelow
    
    public void collidedWith(final Entity other) {
    }
}