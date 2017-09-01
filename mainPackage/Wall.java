package mainPackage;

import java.awt.*;
import java.io.*;
import java.util.*;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Serves to act as a barrier for players in the game has a hitbox and a picture
 * 
 * @author Richard Yang
 * @version May 26, 2016
 * @author Period: 3
 * @author Assignment: AdventuresOfKidco
 *
 * @author Sources: None
 */
public class Wall extends Entity {
	private Image wall;

	private int width;

	private int height;

	private double x;

	private double y;

	private int offset;

	private Rectangle bounds;

	/**
	 * constructor for the wall
	 * 
	 * @param width
	 *            the width of wall
	 * @param height
	 *            the height of wall
	 * @param x
	 *            the left location of wall
	 * @param y
	 *            the top location of wall
	 */
	public Wall(int width, int height, int x, int y) {
		offset = 5;
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		bounds = new Rectangle(x, y, width, height);
		try {
			wall = ImageIO.read(new File("wall.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * incase the wall needs to move
	 * 
	 * @param x
	 *            delta x of change
	 * @param y
	 *            delta y of change
	 */
	public void update(double x, double y) {
		this.x += x;
		this.y -= y;
		bounds.x = (int) this.x;
		bounds.y = (int) this.y;
	}

	/**
	 * draws the wall
	 * 
	 * @param g
	 *            graphics to draw it
	 */
	public void draw(Graphics g) {
		g.setClip(bounds);
		g.drawImage(wall, (int) x + offset / 2, (int) y + offset / 2, width - offset, height - offset, null);
		g.setClip(null);
	}

	/**
	 * returns the hitbox of the wall
	 * 
	 * @return bounds of wall
	 */
	public Rectangle getBounds() {
		return bounds;
	}

	/**
	 * returns the hitbox of the new wall location
	 * 
	 * @param x
	 *            the delta x
	 * @param y
	 *            the delta y
	 * @return bounds of wall
	 */
	public Rectangle tryBounds(int x, int y) {
		return new Rectangle(bounds.x, bounds.y, width, height);
	}

}