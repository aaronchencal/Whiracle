package mainPackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * gun class for the player, spawns the bullets and draws the "gun"
 * 
 * @author Richard Yang
 * @version May 26, 2016
 * @author Period: 3
 * @author Assignment: AdventuresOfKidco
 *
 * @author Sources: None
 */
public class Gun {
	public boolean firing;

	private static final int RELOAD_TIME = 20;
	private int reloadFrames = 0;

	public static final int MAX = 400;

	public int frames = MAX;

	public double angle;
	public boolean hasClicked;
	private Image gun;

	int centerX;

	int centerY;

	int x;

	int y;
	public double radius;

	/**
	 * the constructor for the gun
	 * 
	 * @param centerX
	 *            x coor of the player
	 * @param centerY
	 *            y coor of the player
	 * @param rad
	 *            the radius of the gun
	 * @param angle
	 *            the angle of the gun
	 */
	public Gun(int centerX, int centerY, double rad, double angle) {

		try {
			gun = ImageIO.read(new File("head.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		x = centerX;
		y = centerY;
		this.centerX = (int) (centerX + rad);
		this.centerY = (int) (centerY - rad);
		this.radius = rad;
		this.angle = angle;
	}

	public void updateFrames() {
		hasClicked = false;
		reloadFrames = 0;
		frames -= 100;
	}

	/**
	 * spawns a bullet
	 * 
	 * @return the bullet made
	 */
	public Bullet addBullet() {
		if (frames < 100)
			return null;
		if (reloadFrames < RELOAD_TIME)
			return null;
		hasClicked = true;
		double dumangle = 360 - angle;
		if (dumangle < 0) {
			dumangle += 360d;
		}

		return new Bullet((int) (centerX + radius * 2 * Math.cos(Math.toRadians(dumangle))),
				(int) (centerY + radius * 2 * Math.sin(Math.toRadians(dumangle)) * -1), angle);
	}

	/**
	 * draws the gun
	 * 
	 * @param g
	 *            the graphics the spawns the gun
	 */
	public void draw(Graphics2D g) {
		g.drawImage(gun, x, y, (int) (radius * 2), (int) (radius * 2), null);
	}

	/**
	 * updates the gun
	 * 
	 * @param x
	 *            the new x
	 * @param y
	 *            the new y
	 * @param mousex
	 *            the mouse x
	 * @param mousey
	 *            the mouse y
	 */
	public void update(int x, int y, int mousex, int mousey) {
		double deltaY = (mousey - y);
		double deltaX = (mousex - x);
		double result = Math.toDegrees(Math.atan2(deltaY, deltaX));
		angle = (result < 0) ? (360d + result) : result;

		this.x = x + (int) (radius / 4);
		this.y = y - (int) (radius / 4);
		centerX = (int) (x + radius / 2);
		centerY = (int) (y + radius / 2);
		if (reloadFrames != RELOAD_TIME)
			reloadFrames++;
		if (frames != MAX)
			frames++;
	}

}
