package mainPackage;

import java.awt.*;
import java.io.*;
import java.util.*;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Acts as the generic hitbox of any player
 * 
 * @author Richard Yang
 * @version May 26, 2016
 * @author Period: 3
 * @author Assignment: AdventuresOfKidco
 *
 * @author Sources: None
 */
public class Shell {
	public boolean dead = false;

	private Image tank;

	private Image skull;

	public int centerX;

	public int centerY;

	public int width;

	public Rectangle bounds;

	private double alpha = 1.0;

	/**
	 * constructor of the hitbox
	 * 
	 * @param x
	 *            the x coor of hitbox
	 * @param y
	 *            the y coor of hitbox
	 * @param width
	 *            the width of the hitbox ( and height, it is a circle)
	 */
	public Shell(int x, int y, int width) {
		centerX = x;
		centerY = y;
		this.width = width;
		width = (int) (width / Math.sqrt(2));
		bounds = new Rectangle(x - (width / 2), y - (width / 2), width, width);
		try {
			tank = ImageIO.read(new File("tank.jpg"));
			skull = ImageIO.read(new File("death.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * draw function of the shell
	 * 
	 * @param g
	 *            the graphics to draw the shell
	 */
	public void draw(Graphics g) {
		if (dead) {

			if (alpha <= 0) {
				alpha = 1.0;
				dead = false;
			}
			Graphics2D g2d = (Graphics2D) g;
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setClip(bounds);
			g2d.drawImage(skull, bounds.x, bounds.y, bounds.width, bounds.width, null);
			g2d.setClip(null);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

			alpha -= 0.03f;
			return;
		}

		g.setClip(bounds);
		g.drawImage(tank, bounds.x, bounds.y, bounds.width, bounds.width, null);
		g.setClip(null);

	}

	/**
	 * getter for the bounds
	 * 
	 * @return bounds of the shell
	 */
	public Rectangle getBounds() {
		return bounds;
	}

	/**
	 * trys a new bound of the player, used for checking for collisions
	 * 
	 * @param x
	 *            delta x
	 * @param y
	 *            delta y
	 * @return the new bounds of the bounds
	 */
	public Rectangle tryBounds(int x, int y) {
		return new Rectangle(bounds.x + x, bounds.y - y, bounds.width, bounds.width);
	}

}