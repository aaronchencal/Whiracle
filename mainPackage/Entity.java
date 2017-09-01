package mainPackage;

import java.awt.*;
import java.io.*;
import java.util.*;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * The entity class is the generic class for all objects that is not a player
 * 
 * @author Richard Yang
 * @version May 26, 2016
 * @author Period: 3
 * @author Assignment: AdventuresOfKidco
 *
 * @author Sources: None
 */
public class Entity {
	private Image img;

	private Rectangle bounds;

	/**
	 * draw function of entity
	 * 
	 * @param g
	 *            the graphics used to draw
	 */
	public void draw(Graphics g) {
		if (bounds instanceof Rectangle) {
			Rectangle temp = (Rectangle) bounds;
			g.drawImage(img, temp.x, temp.y, temp.width, temp.height, null);
		}
	}

	/**
	 * returns the new bounds of the updated x and y values
	 * 
	 * @param x
	 *            delta x
	 * @param y
	 *            delta y
	 * @return new bounds
	 */
	public Rectangle tryBounds(int x, int y) {
		if (bounds instanceof Rectangle) {
			Rectangle temp = (Rectangle) bounds;
			return new Rectangle(temp.x - x, temp.y + y, temp.width, temp.height);
		}
		return null;
	}

	/**
	 * actually changes the bounds of the entity
	 * 
	 * @param x
	 *            delta x
	 * @param y
	 *            delta y
	 */
	public void update(double x, double y) {
		if (bounds instanceof Rectangle) {
			bounds = (Rectangle) bounds;
		}
	}

	/**
	 * returns current bounds
	 * 
	 * @return bounds
	 */
	public Rectangle getBounds() {
		return bounds;
	}
}
