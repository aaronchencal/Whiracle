package mainPackage;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.util.ArrayList;

/**
 * The server-side version of the gamestate
 * 
 * @author Aaron Chen
 * @version May 26, 2016
 * @author Period: 3
 * @author Assignment: AdventuresOfKidco
 *
 * @author Sources: None
 */
public class ServerBoard {

	public static ArrayList<Player> players;

	private static int height;

	private static int width;

	public static ArrayList<Entity> environment;

	public static ArrayList<Bullet> bullets;

	public static String[] names;

	public static int[] stocks;

	public volatile static int connections;

	/**
	 * Accepts information -> width of screen, height of screen makes bullets,
	 * enviro, and players array
	 * 
	 * @param w
	 *            -> width of the screen
	 * @param h
	 *            -> height of the screen
	 */
	public ServerBoard(int w, int h) {
		width = w;
		height = h;
		players = new ArrayList<Player>();

		environment = new ArrayList<Entity>();
		environment.add(new Wall(100, h / 2, w / 4 - 50, h / 4));
		environment.add(new Wall(100, h / 2, w * 3 / 4 - 50, h / 4));

		bullets = new ArrayList<Bullet>();
	}

	/**
	 * gives the player count
	 * 
	 * @return player count
	 */
	public int getPlayerCount() {
		return players.size();
	}

	/**
	 * adds a player to the game
	 */
	public void addPlayer() {
		int sx = 0;
		int sy = 0;
		switch (players.size() + 1) {
		case 1:
			sx = 110;
			sy = 110;
			break;
		case 2:
			sx = width - 110;
			sy = height - 110;
			break;
		case 3:
			sx = width - 110;
			sy = 110;
			break;
		case 4:
			sx = 110;
			sy = height - 110;
			break;
		default:
			break;
		}
		players.add(new Player(500, 500, true, sx, sy, players.size()));
	}

	/**
	 * Game loop: 1) update events 2) draw everything 3) clear screen -> repeat
	 * 
	 * @param code
	 *            the code that is from client
	 * @param id
	 *            the id of the client
	 * @return the new game state
	 */
	public String update(String code, int id) {

		int[] updVls = Data.decodeOne(code);
		if (updVls[4] != -1) {
			bullets.add(new Bullet(updVls[2], updVls[3], updVls[4]));
		}

		players.get(id).gun.angle = updVls[5];
		players.get(id).health = String.valueOf(updVls[6]);
		players.get(id).gun.frames = updVls[7];
		updVls = handleCollisions(updVls, id);

		for (int i = 0; i < bullets.size(); i++) {
			Bullet b = bullets.get(i);
			if (b.getBounds().x < 0 || b.getBounds().x > width || b.getBounds().y < 0 || b.getBounds().y > height) {
				try {
					bullets.remove(i);
					i--;
				} catch (Exception e) {

				}
			}
			b.update();

		}

		players.get(id).shell.bounds.x += updVls[0];
		players.get(id).shell.bounds.y -= updVls[1];

		return Data.encodeAll(players, environment, bullets);
	}

	/**
	 * 
	 * @param coors
	 *            the coors of the player
	 * @param id
	 *            the id of the player
	 * @return the new coors of the player
	 */
	public int[] handleCollisions(int[] coors, int id) {

		for (int i = 0; i < environment.size(); i++) {
			Entity item = environment.get(i);

			if (players.get(id).shell.tryBounds(coors[0], 0).intersects((item.getBounds()))) {
				if (item instanceof Wall) {
					coors[0] = 0;
				}
			}
			if (players.get(id).shell.tryBounds(0, coors[1]).intersects((item.getBounds()))) {
				if (item instanceof Wall) {
					coors[1] = 0;
				}
			}
		}
		for (int i = 0; i < bullets.size(); i++) {
			Bullet item = bullets.get(i);
			for (int b = 0; b < environment.size(); b++) {
				if (i == -1)
					i = 0;
				if (environment.get(b).getBounds().intersects(item.getBounds())) {
					try {
						bullets.remove(i);
						i--;
					} catch (IndexOutOfBoundsException e) {

					}
				}
			}
			if (players.get(id).shell.tryBounds(coors[0], coors[1]).intersects((item.getBounds()))) {
				try {
					bullets.remove(i);
					i--;
				} catch (IndexOutOfBoundsException e) {

				}
				players.get(id).takeDamage();

				if (Integer.parseInt(players.get(id).health) % 5 == 0) {
					players.get(id).respawn();
				}
				for (int b = 0; b < players.size(); b++) {
					if (Integer.parseInt(players.get(id).health) == 0) {
						players.get(b).gameOver(b == id);
					}
				}
				continue;
			}

		}
		return coors;
	}

	/**
	 * Called in order to reset everything after a game has finished
	 */
	public void reset() {
		bullets.clear();
		for (int b = 0; b < players.size(); b++) {
			players.get(b).reset();
		}
	}

}