package mainPackage;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.Queue;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * 
 * player class has hitbox, gun, direction, health, statusbar and bullets to
 * Add. provides all functionality for the player
 * 
 * @author Richard Yang
 * @version May 26, 2016
 * @author Period: 3
 * @author Assignment: AdventuresOfKidco
 *
 * @author Sources: None
 */
public class Player {

	/**
	 * public:
	 * 
	 * Player p1 -> W A S D , F
	 * 
	 * Player p2 -> ^ V < > , SPACE
	 * 
	 * Shell shell -> contains bounds of body of ship & draws it
	 * 
	 * Background bkgd -> controls movement, gives out coordinates for enviro to
	 * move
	 * 
	 * Grid grid -> controls drawing & movement of the players on the inner grid
	 * 
	 * Shield shield
	 * 
	 * Gun g2
	 * 
	 */

	public String health = "15";

	public int id;

	public Shell shell;

	public Gun gun;

	private Direction[] dir; // 0 = up, 1 = down, 2 = right, 3 = left

	public double interpolation = 1;

	public Queue<Bullet> bulletsToAdd;

	private int lastmousex;

	private int lastmousey;

	public StatusBar sbar;

	public int spawnX;

	public int spawnY;

	private int lastHealth;

	/**
	 * constructor for a player in the game
	 * 
	 * @param width
	 *            of screen
	 * @param height
	 *            of screen
	 * @param friendly
	 *            if it is friendly or not to the client
	 * @param sx
	 *            spawn point x coor
	 * @param sy
	 *            spawn point y coor
	 * @param id
	 *            of the player
	 */
	public Player(int width, int height, boolean friendly, int sx, int sy, int id) {
		spawnX = sx;
		spawnY = sy;
		shell = new Shell(sx, sy, height / 8);
		gun = new Gun(sx, sy, height / 16, 0);
		bulletsToAdd = new LinkedList<Bullet>();
		sbar = new StatusBar(friendly, sx, sy, 15, id);
		dir = new Direction[4];
		for (int i = 0; i < 4; i++) {
			dir[i] = new Direction(0);
			dir[i].accel = 0;
			dir[i].op = i % 2 != 0;
		}
	}

	/**
	 * stops the player completely
	 */
	public void breakAll() {
		for (int i = 0; i < 4; i++) {
			dir[i].on = false;
		}
	}

	/**
	 * handles all the moving
	 * 
	 * @return (change in x, change in y)
	 */
	public int[] newCoordinates() {
		double nx = 0;
		double ny = 0;
		for (int i = 0; i < 4; i++) {
			if (dir[i].on && dir[i].accel < 10)
				dir[i].accel += 0.5;

			if (!dir[i].on) {
				dir[i].accel -= 1;
				if (dir[i].accel < 5) {
					dir[i].accel = 0;
				}
			}

			if (i < 2) // up and down
			{
				int b = dir[i].op ? -1 : 1;
				ny += (dir[i].accel) * b;
			} else // right and left
			{
				int b = dir[i].op ? -1 : 1;
				nx += (dir[i].accel) * b;
			}
		}
		return new int[] { (int) nx, (int) ny };

	}

	public void draw(Graphics g) {
		sbar.draw(g, gun.frames);
		shell.draw(g);
	}

	/**
	 * reads player input and sets correct direction
	 * 
	 * @param e
	 *            the key that was pressed
	 */
	public void keyPressed(KeyEvent e) {
		if (shell.dead)
			return;
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			dir[0].on = true;
			break;
		case KeyEvent.VK_A:
			dir[3].on = true;
			break;
		case KeyEvent.VK_D:
			dir[2].on = true;
			break;
		case KeyEvent.VK_S:
			dir[1].on = true;
			break;
		default:
			break;
		}
	}

	/**
	 * stops the direction once key is released
	 * 
	 * @param e
	 *            direction to stop moving in
	 */
	public void keyReleased(KeyEvent e) {
		if (shell.dead)
			return;

		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			dir[0].on = false;
			break;
		case KeyEvent.VK_A:
			dir[3].on = false;
			break;
		case KeyEvent.VK_D:
			dir[2].on = false;
			break;
		case KeyEvent.VK_S:
			dir[1].on = false;
			break;
		default:
			break;
		}

	}

	/**
	 * spawns a bullet once mouse is clicked
	 * 
	 * @param e
	 *            the input from the player
	 */
	public void mouseClicked(MouseEvent e) {
		if (shell.dead)
			return;

		if (e.getButton() == MouseEvent.BUTTON1) {
			Bullet b = gun.addBullet();
			if (b != null) {
				bulletsToAdd.add(b);
			}
		}
	}

	/**
	 * moves the gun in the direction of the mouse
	 * 
	 * @param e
	 *            the mouse information
	 */
	public void mouseMoved(MouseEvent e) {
		if (shell.dead)
			return;
		lastmousex = e.getX();
		lastmousey = e.getY();
		updateGun();
	}

	/**
	 * this actually updates the gun's angle
	 */
	public void updateGun() {
		gun.update(shell.getBounds().x, shell.getBounds().y, lastmousex, lastmousey);
	}

	/**
	 * function to simplify taking damage
	 */
	public void takeDamage() {
		health = String.valueOf(Integer.parseInt(health) - 1);
		sbar.tHealth--;
	}

	/**
	 * respawns the player their set spawn location
	 */
	public void respawn() {
		if (Integer.parseInt(health) == 0) {
			shell.bounds.x = -100;
			shell.bounds.y = -100;
			gun.x = (int) (gun.radius / 4);
			gun.y = (int) (gun.radius / 4);
			gun.centerX = (int) (gun.radius / 2);
			gun.centerY = (int) (gun.radius / 2);
			sbar.x = -1234;
			sbar.y = -1234;
			return;
		}
		shell.bounds.x = spawnX;
		shell.bounds.y = spawnY;
		gun.x = (int) (gun.radius / 4);
		gun.y = (int) (gun.radius / 4);
		gun.centerX = (int) (gun.radius / 2);
		gun.centerY = (int) (gun.radius / 2);
		sbar.x = -50;
		sbar.y = -100;
		return;
	}

	/**
	 * updates the health if it has taken any damage and checks for lethal blow
	 * 
	 * @param health
	 *            new updated health of the player
	 */
	public void updateHealth(String health) {
		this.health = health;
		sbar.tHealth = Integer.parseInt(this.health);
		int nh = sbar.tHealth;
		if ((lastHealth == 1 && nh == 0) || (lastHealth == 6 && nh == 5) || (lastHealth == 11 && nh == 10)) {
			shell.dead = true;
		}
		lastHealth = sbar.tHealth;
	}

	/**
	 * update method of the player
	 * 
	 * @param nx
	 *            new x location
	 * @param ny
	 *            new y location
	 */
	public void update(int nx, int ny) {
		shell.bounds.y = ny;
		shell.bounds.x = nx;
		sbar.x = nx - 50;
		sbar.y = ny - 50;
		sbar.tHealth = Integer.parseInt(health);
		gun.x = nx + (int) (gun.radius / 4);
		gun.y = ny - (int) (gun.radius / 4);
		gun.centerX = (int) (nx + gun.radius / 2);
		gun.centerY = (int) (ny + gun.radius / 2);
	}

	/**
	 * puts the players out of screen for ease of checking if game is over
	 * 
	 * @param lost
	 *            if this player lost the game
	 */
	public void gameOver(boolean lost) {
		if (lost) {
			update(-200, -200);
		} else {
			update(-100, -100);
		}
	}

	/**
	 * resets the player to default locations and full health and no accel
	 */
	public void reset() {
		for (int i = 0; i < dir.length; i++) {
			dir[i].accel = 0;
			dir[i].on = false;
		}
		shell.dead = false;
		update(spawnX, spawnY);
		health = "15";
		gun.frames = gun.MAX;
	}

}