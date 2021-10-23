/*
public class KlingonEntity extends Entity {
	private Game game; // the game in which the ship exists
*/
	  /* construct the player character
	   * input: game - the game in which the ship is being created
	   *        r - a string with the name of the image associated to
	   *              the sprite for the ship
	   *        x, y - initial location of luke
	   */
/*
	  public KlingonEntity(Game g, String r, int newX, int newY) {
	    super(r, newX, newY);  // calls the constructor in Entity
	    game = g;
	    dx = 30;

	  } */ // constructor

	  /* move
	   * input: delta - time elapsed since last move (ms)
	   * purpose: move the klingon 
	   */
/*
	  public void move (long delta){
	    if (game.enemyOnPlatform(this)) {
	    	dy = 0;
	    } else {
	    	dy = 160;
	    } // else
	    
	    super.move(delta);  // calls the move method in Entity
	    
	    if (!game.enemyOnPlatform(this)) {
	    	dx = -dx; // turn around
	    	super.move(delta); // go back before being drawn
	    } // if
	  } // move
*/
	  /* collidedWith
	   * input: other - the entity with which the ship has collided
	   * purpose: notification that the player's ship has collided
	   *          with something
	   */
/*
	   public void collidedWith(Entity other) {
	    
	   } // collidedWith    
} // class KlingonEntity
*/