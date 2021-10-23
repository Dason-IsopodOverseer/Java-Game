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
    public final int GAMEWIDTH = 640; // width in px of game window
    public final int GAMEHEIGHT = 640; // height in px of game window
    
    public ArrayList entities = new ArrayList(); // list of entities
    private double moveSpeed = 75;

    public Entity luke;
    public Entity enemy;
    private boolean inFreefall = true; // true when luke is falling and moving down
    private int jumpTarget;
    private boolean jumping = false; // true when luke is "falling", but moving up
    private double amountScrolled = 0; // constantly increases to make the platforms rise
    private double scrollSpeed = -0.01; // speed that amountScrolled increases at
    private boolean superScroll = false; // when luke is in the bottom 2/10 of the screen, scroll faster
    private final int levelHeight = 1000; // height of the first "level" of the game
    private boolean facingRight = true;
        		    
    private TileMap map = new TileMap("level1.txt");
    
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

		luke = new LukeEntity(this, "sprites/luke.png", 0, 32);
		entities.add(luke);
		//enemy = new KlingonEntity(this, "sprites/luke.jpg",50,0);
		//entities.add(enemy);

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
            
		
            //XXX SCALE GRAPHICS HEREEEEEEEEEEE

            // move entities
            for (int i = 0; i < entities.size(); i++) {
                Entity entity = (Entity) entities.get(i);
                entity.move(delta);
            } // for

            // check if level is over
            if (luke.getY() > levelHeight) {
            	goToNextLevel(lastLoopTime);
            } // if
            
            // check superScroll
            if (luke.getY() + amountScrolled > GAMEHEIGHT * 0.8) {
            	// if luke is in the bottom 2/10 of the screen
            	superScroll = true;

            } else if (luke.getY() + amountScrolled < GAMEHEIGHT * 0.2) {
            	// if luke is in the top 2/10 of the screen
            	superScroll = false;
            } // else if
            
            // scroll screen
            if (superScroll) {
            	amountScrolled += -2 * delta;
            } else {
            	amountScrolled += scrollSpeed * delta;
            } // else
        	scrollSpeed += delta / 10000;
        	if ((luke.getY() + amountScrolled) < 0 && ((LukeEntity) luke).isTileBelow()) {
            	lose();
            } // if
            
            // draw entities and tilemap XXX MAKE THIS A METHOD??
        	
            Sprite tile = null;
    		for (int i = 0; i < map.getHeight(); i++) {
    			for (int j = 0; j < map.getWidth(); j++) {
    				tile = map.getTile(i, j);
    				if (tile != null) {
    					tile.draw(g, (i * 32), (int)(j * 32 + amountScrolled));
    				}
    			}
    		}

            for (int i = 0; i < entities.size(); i++) {
            	Entity entity = (Entity) entities.get(i);
            	entity.draw(g, amountScrolled);
            }

            // collisions
            /*
            if (luke.collidesWith(enemy)) {
            	luke.collidedWith(enemy);
            }
            */
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
            	luke.setVerticalMovement(100);
            }//if (jumping) {
            	
            //} else {
            luke.setHorizontalMovement(0);
            	
            	// Handle input
     			if (rightPressed && !leftPressed) {
     				luke.setHorizontalMovement(moveSpeed);
     				//facingRight = true;
     			} else if (leftPressed) {
     				luke.setHorizontalMovement(-moveSpeed);
     				//facingRight = false;
     			} if (upPressed && ((LukeEntity) luke).isTileBelow()) {
     				luke.setVerticalMovement(-100);
     				jumpTarget = luke.getY() - 70;
     				jumping = true;
     				inFreefall = false;
     			} // if
            //} // else
         } // while
	} // gameLoop
	
	private void goToNextLevel(long lastLoopTime) {
		luke.setVerticalMovement(2000);
		
		while(luke.getY() < levelHeight + 2000) {
			// calc. time since last update, will be used to calculate
            // entities movement
            long delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();
            
            // keep luke still on screen
            luke.move(delta);
            
            // get graphics context for the accelerated surface and make it black
            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
            g.setColor(new Color(0,0,0));
            g.fillRect(0,0,GAMEWIDTH,GAMEHEIGHT);
            
            amountScrolled += -2 * delta;
            
            // draw luke and platforms
            luke.draw(g, amountScrolled);
            
            // clear graphics and flip buffer
            g.dispose();
            strategy.show();
		} // while
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
