
public class BorgEntity extends EnemyEntity {
	private final int speed = 50;
	
    public BorgEntity(final Game g, final String r, final int newX, final int newY) {
        super(g, r, newX, newY, 50);
    }
    
    public void move(long delta) {
    	super.move(delta);
    	
    	// check if this entity should turn around
		if (isTileBelow() && !isTileCompletelyBelow()) {
			dx = -dx;
			x += (delta * dx) / 500;
		} // if
    } // move
    
    public void collidedWith(final Entity other) {
    }
}
