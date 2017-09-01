package mainPackage;

import java.awt.*;
import java.io.*;

import javax.imageio.ImageIO;

/**
 * 
 * Something that fires out of the player's gun
 *
 * @author Richard Yang
 * @version May 26, 2016
 * @author Period: 3
 * @author Assignment: AdventuresOfKidco
 *
 * @author Sources: None
 */
public class Bullet extends Entity {
	private Image bullet;

	private static final int width = 17;

	private static final int height = 17;

	private double x;

	private double y;

	private double cornerX;

	private double cornerY;

	private int speed = 20;

	private double dx = 0;

	private double dy = 0;

	private Rectangle bounds;

	public double angle;

	/**
	 * Sets the angle, x, y, dx, dy, bounds, and images of the bullet
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param angle
	 *            the angle
	 */
	public Bullet(int x, int y, double angle) {
		this.angle = angle;
		this.x = x;
		this.y = y;
		cornerX = x - width / 2;
		cornerY = y - width / 2;
		dx = Math.cos(Math.toRadians(angle)) * speed;
		dy = Math.sin(Math.toRadians(angle)) * speed;
		bounds = new Rectangle(x, y, width, height);

		try {
			bullet = ImageIO.read(new File("body.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Draws the bullet
	 * 
	 * @param g
	 *            Graphics object
	 */
	public void draw(Graphics g) {
		g.drawImage(bullet, (int) cornerX, (int) cornerY, width, height, null);
	}

	/**
	 * Sets the x and y
	 * 
	 * @param x
	 *            the x to set
	 * @param y
	 *            the y to set
	 */
	public void verify(int x, int y) {
		this.x = x;
		this.y = y;
		cornerX = this.x - width / 2;
		cornerY = this.y - height / 2;
		bounds.x = (int) this.x;
		bounds.y = (int) this.y;

	}

	/**
	 * Moves the bullet by dx and dy
	 */
	public void update() {
		this.x += dx;
		this.y += dy;
		cornerX = this.x - width / 2;
		cornerY = this.y - height / 2;
		bounds.x = (int) this.x;
		bounds.y = (int) this.y;
	}

	@Override

	/**
	 * "tries" a new bounds by moving it
	 * 
	 * @param x
	 *            the x coordinate to move
	 * @param y
	 *            the y coordinate to move
	 */
	public Rectangle tryBounds(int x, int y) {
		return new Rectangle(bounds.x + x, bounds.y - y, width, height);
	}

	/**
	 * returns the bounds
	 */
	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	/**
	 * toString method
	 * 
	 * @return String representation
	 */
	@Override
	public String toString() {
		return dx + " " + dy;
	}

}
