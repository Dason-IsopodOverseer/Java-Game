public class KlingonEntity extends EnemyEntity
{    
    protected final int speed = 50;
	
    public KlingonEntity(final Game g, final String r, final int newX, final int newY) {
        super(g, r, newX, newY, 50);
    }
    
    public void move(long delta) {
    	super.move(delta);
    	
    	// check if this entity should turn around
		if (isTileBelow() && !isTileCompletelyBelow() && (game.luke.getY() <= y)) {
			dx = -dx;
			x += (delta * dx) / 500;
		} // if
    } // move
    
    public void collidedWith(final Entity other) {
    }
}