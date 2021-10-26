import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;

public class Game extends Canvas {
	private BufferStrategy strategy;   // take advantage of accelerated graphics
	
	private boolean leftPressed = false;  // true if left arrow key currently pressed
    private boolean rightPressed = false; // true if right arrow key currently pressed
    protected boolean upPressed = false; // true if up arrow key currently pressed
    private boolean attackPressed = false; // true if attack key (spacebar) currently pressed
    
    public final int GAMEWIDTH = 960; // width in px of game window
    public final int GAMEHEIGHT = 480; // height in px of game window
    private int levelHeight = 1920; // height of the first "level" of the game
    private int newLevelHeight = 0; // XXX can probably remove this
    
    private double amountScrolled = 0; // constantly increases to make the platforms rise
    private double scrollSpeed = -50; // speed that amountScrolled increases at
    private boolean superScroll = false; // when luke is in the bottom 2/10 of the screen, scroll faster
    private boolean scrolling = true;
    
    private int lvl = 1;
    private int attackInterval = 500;
    private int attackDuration = 300;
    
    public ArrayList<Entity> entities = new ArrayList(); // list of entities
    public ArrayList<Entity> deadEnemies = new ArrayList();
    private double moveSpeed = 400;
    public Entity luke;
    public Entity enemy;
    
    private boolean inFreefall = true; // true when luke is falling and moving down
    private int jumpTarget;
    private boolean jumping = false; // true when luke is "falling", but moving up
    //private boolean facingRight = true;
    public int health = 3;
    public int deathToll = 0;
    private long lastAttack = 0;
    
    private TileMap map = new TileMap("level1.txt", this);
    
    /*
	 * Construct our game and set it running.
	 */
	public Game() {
		// create a frame to contain game
		JFrame container = new JFrame("Title of page goes here");

		// get hold the content of the frame
		JPanel panel = (JPanel) container.getContentPane();

		// set up the resolution of the game
		panel.setPreferredSize(new Dimension(GAMEWIDTH,GAMEHEIGHT));
		panel.setLayout(null);

		// set up canvas size (this) and add to frame
		setBounds(0,0,GAMEWIDTH,GAMEHEIGHT);
		panel.add(this);

		// Tell AWT not to bother repainting canvas since that will
        // be done using graphics acceleration
		setIgnoreRepaint(true);

		// make the window visible
		container.pack();
		container.setResizable(false);
		container.setVisible(true);


        // if user closes window, shutdown game and jre
		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			} // windowClosing
		});

		// add key listener to this canvas
		addKeyListener(new KeyInputHandler());

		// request focus so key events are handled by this canvas
		requestFocus();

		// create buffer strategy to take advantage of accelerated graphics
		createBufferStrategy(2);
		strategy = getBufferStrategy();

		luke = new LukeEntity(this, "luke", 0, 0);
		entities.add(luke);
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).setMap();
		}
		

		// start the game
		gameLoop();
    } // constructor

	public TileMap getTileMap() {
		return this.map;
	}
	
	public void gameLoop() {
		
		long lastLoopTime = System.currentTimeMillis();

        // keep loop running until game ends
        while (true) {
        	
			// calc. time since last update, will be used to calculate
            // entities movement
            long delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();
            
            // get graphics context for the accelerated surface and make it black
            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
            g.setColor(new Color(0,0,0));
            g.fillRect(0,0,GAMEWIDTH,GAMEHEIGHT);
            
		
            // remove dead entities
            entities.removeAll(deadEnemies);

            // move entities
            for (int i = 0; i < entities.size(); i++) {
                Entity entity = (Entity) entities.get(i);
                entity.move(delta);
            } // for
           
            // check superScroll
            if ((luke.getY() + amountScrolled > (GAMEHEIGHT * 0.6) && scrollSpeed != 0)) {
            	// if luke is in the bottom 4/10 of the screen
            	superScroll = true;

            } else if (luke.getY() + amountScrolled < (GAMEHEIGHT * 0.2)) {
            	// if luke is in the top 2/10 of the screen
            	superScroll = false;
            } // else if
            
            // scroll screen
            if (scrolling) {
	            if (superScroll) {
	            	amountScrolled += (-1000 * delta) / 1000;
	            } else {
	            	amountScrolled += (scrollSpeed * delta) / 1000;
	            } // else
	            
	           
	            if ((levelHeight - GAMEHEIGHT) + amountScrolled < 5) {
	        		scrolling = false;
	        		
	        	}
            }
        	
            if ((luke.getY() + amountScrolled) < 0 && luke.isTileBelow()) {
            	lose();
            } // if
        	
            Sprite tile = null;
            int topY = (int) amountScrolled / -96;
            int bottomY = (GAMEHEIGHT / 96) + topY;
    		for (int i = 0; i < map.getWidth(); i++) { 
    			for (int j = topY; j <= bottomY; j++) {
    				tile = map.getTile(i, j);
    				if (tile != null) {
    					tile.draw(g, (i * 96), (int)(j * 96 + amountScrolled));
    				}
    			}
    		}
    		
    		
    		// update entity animations to account for movement
            for (int i = 0; i < entities.size(); i++) {
                Entity entity = (Entity) entities.get(i);
                entity.updateAnimations();
            } // for

            // draw entities
            for (int i = 0; i < entities.size(); i++) {
            	Entity entity = (Entity) entities.get(i);
            	entity.draw(g, amountScrolled);
            }
            
            for (int i = 0; i < entities.size(); i++) {
    	        if (entities.get(i).collidesWith(luke) && entities.get(i) != luke) {
    	        	if (luke.attacking) {
    	        		deadEnemies.add(entities.get(i));
    	        	}
    	        	else {
    	        		luke.collidedWith(entities.get(i));
    	        	}
    	        }
    	    }
            
            // check if luke should be moving vertically
           
            if (jumping) {
            	// check if luke has reached the peak of his jump
            	if (luke.getY() < jumpTarget || (luke.getY() + amountScrolled < 0)) {
            		jumping = false;
            		inFreefall = true; 
            	} // if
            	
            } else { 
	        	inFreefall = true;
            } // else
           
            // clear graphics and flip buffer
            g.dispose();
            strategy.show();
            
            // revert luke to default movement
            if (inFreefall) {
            	luke.setVerticalMovement(600);
            }
            luke.setHorizontalMovement(0);
            	
        	// Handle input
 			if (rightPressed && !leftPressed) {
 				luke.setHorizontalMovement(moveSpeed);
 				//facingRight = true;
 			} else if (leftPressed) {
 				luke.setHorizontalMovement(-moveSpeed);
 				//facingRight = false;
 			} if (upPressed && luke.isTileBelow()) {
 				luke.setVerticalMovement(-600);
 				jumpTarget = luke.getY() - 200;
 				jumping = true;
 				inFreefall = false;
 			} // if
 			
 			if (attackPressed && (System.currentTimeMillis() - lastAttack > attackInterval)) {
 				lastAttack = System.currentTimeMillis();
 				luke.attacking = true;
 			}
 			
 			if (System.currentTimeMillis() - lastAttack > attackDuration) {
 				luke.attacking = false; 
 			}
 			
 			// see if luke is dead
 			if (health == 0) {
 				lose();
 			}
 			
 			// if luke is at the bottom region of the level
 			if (luke.y > (levelHeight - 200)) {
	 			if (((LukeEntity) luke).getTileDirectlyBelow() == 'N') {
	 				lvl++;
	 				goToNextLevel(lastLoopTime);
	 				((LukeEntity) luke).setMap(map);
	 				scrolling = true;
	 			}
 			}
        
         } // while
	} // gameLoop
	
	private void goToNextLevel(long lastLoopTime) {
			
            // get graphics context for the accelerated surface and make it black
            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
            g.setColor(new Color(0,0,0));
            g.fillRect(0,0,GAMEWIDTH,GAMEHEIGHT);
            
            loadLvlMap();
            
            amountScrolled = 0;
            
            luke.x = 0;
            luke.y = 0;
            
            //attacking = false;
            
            //clear graphics and flip buffer
            g.dispose();
            strategy.show();
            
            return;
		//} // while
	} // goToNextLevel
	
	public void stopJumping() {
		jumping = false;
	}
	
	public boolean getJumping() {
		return jumping;
	}
	
	public static boolean enemyOnPlatform(Entity e) {
		return false;
	} // enemyOnPlatform
	
	public static void lose() {
		System.out.println("You lose");
		System.exit(0);
	} // lose
	
	private void loadLvlMap() {
		if (lvl == 1) {
			map = new TileMap("level1.txt", this);
			levelHeight = map.getHeight() * 96;
			for (int i = 0; i < entities.size(); i++) {
				entities.get(i).setMap();
			}
		}
		else if (lvl == 2) {
			map = new TileMap("level2.txt", this);
			levelHeight = map.getHeight() * 96;
			for (int i = 0; i < entities.size(); i++) {
				entities.get(i).setMap();
			}
		}
		else if (lvl == 3) {
			
		}
	}
	
	private void printNewLvlScreen() {
		
	}
	
	private class KeyInputHandler extends KeyAdapter {
        
       /* The following methods are required
        * for any class that extends the abstract
        * class KeyAdapter.  They handle keyPressed,
        * keyReleased and keyTyped events.
        */
        public void keyPressed(KeyEvent e) {
	         
	         // respond to move left, right or fire
	         if (e.getKeyCode() == KeyEvent.VK_LEFT) {
	        	 leftPressed = true;
	         } // if
	
	         if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
	        	 rightPressed = true;
	         } // if
	       
	         if (e.getKeyCode() == KeyEvent.VK_UP) {
	       	  	upPressed = true;
	         } // if
	         
	         if (e.getKeyCode() == KeyEvent.VK_SPACE) {
	        	 attackPressed = true;
	         }
        } // keyPressed

        public void keyReleased(KeyEvent e) {
	         
	         // respond to move left, right or fire
	         if (e.getKeyCode() == KeyEvent.VK_LEFT) {
	        	 leftPressed = false;
	         } // if
	
	         if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
	        	 rightPressed = false;
	         } // if
	
	         if (e.getKeyCode() == KeyEvent.VK_UP) {
	       	  	upPressed = false;
	         } // if
	         
	         if(e.getKeyCode() == KeyEvent.VK_SPACE) {
	        	 attackPressed = false;
	         }
		} // keyReleased

	    public void keyTyped(KeyEvent e) {
	          // if escape is pressed, end game
	          if (e.getKeyChar() == 27) {
	            System.exit(0);
	          } // if escape pressed
	    } // keyTyped

	} // class KeyInputHandler
	
	public static void main(String[] args) {
		new Game();
	} // main
} // class Game