 /*

import java.awt.Graphics;
 import java.awt.Image;

public class AnimatedEntity extends Entity {
	private Image[] stand;
	private Image[] moveLeft;
	private Image[] moveRight;
	private int direction; // direction sprite is facing
	private int frame; // game frames
	private int index; // index of frame sprite is on
	public Image image;  // the image to be drawn for this sprite
	
	public AnimatedEntity(String r, int newX, int newY) {
		super(r, newX, newY, true);
	}
	
	public void updateAnimation() {
		frame++;
		if (frame % 5 == 0) {
			selectDirection();
			selectImages();
			advanceToNextImage();
		}
	}
	
	public void selectDirection() {
	}
	
	public void selectImages() {
		if (direction == 0) {
			
		}
	}
	
	public void advanceToNextImage() {
		index++;
		if (index == moveLeft.length) {
			index = 0;
		}
		image = moveLeft[index];
	}
}
*/
