package mainPackage;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.*;
import java.util.*;
import java.util.Timer;
import java.lang.*;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.geom.*;

/**
 * This is the foundation of the game- First panel that controls everything
 * inside the window. This class has the focus, listens for key inputs and
 * manages them What the player sees in game is run here
 * 
 * @author Aaron Chen
 * @version Apr 3, 2016
 * @author Period: 3
 * @author Assignment: AdventuresOfKidco
 *
 * @author Sources: None
 */
public class GameBoard extends JPanel implements KeyListener, MouseListener, MouseMotionListener {

	/**
	 * private:
	 * 
	 * int height -> height of the screen
	 * 
	 * int width -> width of the screen
	 * 
	 * Tank tank -> Object that represents everything not part of the
	 * environment, i.e.what you can control
	 * 
	 * ArrayList<Entity> environment -> all non-player controlled objects
	 */

	private static int height;

	private static int width;

	public int id;

	public static ArrayList<Player> players;

	private static ArrayList<Entity> environment;

	private static ArrayList<Bullet> bullets;

	public Background bkgd;

	public volatile boolean gameOver;

	public boolean lost;

	public boolean beginAgain;

	private boolean canClick;
	private float alpha = 0.0f;
	private Image winImage;
	private Image loseImage;

	/**
	 * Accepts information -> width of screen, height of screen
	 * 
	 * Constructs on its own -> Tank, ArrayList<Entity>, keyListener
	 * 
	 * @param w
	 *            -> width of the screen
	 * @param h
	 *            -> height of the screen
	 */
	public GameBoard(int w, int h) {
		width = w;
		height = h;
		bkgd = new Background(width, height);
		players = new ArrayList<Player>();
		environment = new ArrayList<Entity>();
		environment.add(new Wall(100, h / 2, w / 4 - 50, h / 4));
		environment.add(new Wall(100, h / 2, w * 3 / 4 - 50, h / 4));

		bullets = new ArrayList<Bullet>();

		/**
		 * As of right now the server donesn't check collisions as this wall is
		 * created only in ClientBoard
		 * 
		 * There can be a list of text files that outline where the walls /
		 * spawn points / whatever else are on a map
		 */

		try {
			winImage = ImageIO.read(new File("winnerImage.png"));
			loseImage = ImageIO.read(new File("loserImage.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.addKeyListener(this);
		this.setFocusable(true);
		this.setVisible(true);
		this.setOpaque(true);
	}

	/**
	 * inits ID that was given from server
	 * 
	 * @param playerID
	 *            id from server
	 */
	public void initID(int playerID) {
		id = playerID;
	}

	/**
	 * inits the amount of players as given from server
	 * 
	 * @param playerAmount
	 *            the amount of players from server
	 */
	public void initPlayers(int playerAmount) {

		for (int i = 0; i < playerAmount; i++) {
			int sx = 0;
			int sy = 0;
			switch (i) {
			case 0:
				sx = 110;
				sy = 110;
				break;
			case 1:
				sx = width - 110;
				sy = height - 110;
				break;
			case 2:
				sx = width - 110;
				sy = 110;
				break;
			case 3:
				sx = 110;
				sy = height - 110;
				break;
			default:
				break;
			}
			if (i == id) {
				players.add(new Player(500, 500, true, sx, sy, i));
			} else {
				players.add(new Player(500, 500, false, sx, sy, i));
			}

		}
	}

	/**
	 * updates from server
	 * 
	 * @param the
	 *            code that the server gives to the client
	 */
	public String update(String code) {
		if (gameOver) {
			return "no";
		}
		ArrayList<Integer> updVls = Data.decodeAll(code);
		int count = 0;

		int player = 0;

		if (players.get(id).shell.bounds.x < -50 && players.get(id).shell.bounds.x > -150) {
			gameOver = true;
			lost = true;
		} else if (players.get(id).shell.bounds.x < -150) {
			gameOver = true;
			lost = false;
		}

		while (!updVls.get(count).equals(Integer.MIN_VALUE)) {
			players.get(player).update(updVls.get(count++), updVls.get(count++));
			players.get(player).gun.angle = updVls.get(count++);
			players.get(player).updateHealth(String.valueOf(updVls.get(count++)));
			players.get(player++).gun.frames = updVls.get(count++);
		}
		count++;

		players.get(id).updateGun();
		if (players.get(id).gun.hasClicked)
			players.get(id).gun.updateFrames();

		int bullet = 0;
		int counter = count;
		int track = 0;
		while (!updVls.get(count).equals(Integer.MAX_VALUE)) {
			track++;
			count++;
		}
		track /= 2;
		while (bullets.size() < track) {
			bullets.add(new Bullet(0, 0, 0));
		}
		count = counter;
		while (bullets.size() > bullet && count + 1 < updVls.size() && !updVls.get(count).equals(Integer.MAX_VALUE)) {
			bullets.get(bullet++).verify(updVls.get(count++), updVls.get(count++));
		}
		while (bullets.size() > bullet) {
			bullets.remove(bullets.size() - 1);
		}

		int[] returner = new int[8];

		int[] newCoors = players.get(id).newCoordinates();

		returner[0] = newCoors[0];
		returner[1] = newCoors[1];

		if (players.get(id).shell.getBounds().x + returner[0] < 0)
			returner[0] = 0;
		if (players.get(id).shell.getBounds().y - returner[1] < 0)
			returner[1] = 0;
		if (players.get(id).shell.getBounds().x + 3 * players.get(id).shell.width / 4 + returner[0] > width)
			returner[0] = 0;
		if (players.get(id).shell.getBounds().y + 3 * players.get(id).shell.width / 4 - returner[1] > height)
			returner[1] = 0;

		if (players.get(id).bulletsToAdd.size() == 0) {
			returner[4] = -1;
			returner[5] = (int) players.get(id).gun.angle;
			returner[6] = Integer.parseInt(players.get(id).health);
			returner[7] = players.get(id).gun.frames;
			return Data.encodeOne(returner, id);
		}

		Bullet top = players.get(id).bulletsToAdd.remove();
		bullets.add(top);
		returner[2] = top.getBounds().x;
		returner[3] = top.getBounds().y;
		returner[4] = (int) top.angle;
		returner[5] = (int) players.get(id).gun.angle;
		returner[6] = Integer.parseInt(players.get(id).health);
		returner[7] = players.get(id).gun.frames;
		return Data.encodeOne(returner, id);
	}

	/**
	 * paints the gameBoard, ranging from background, players, bullets, walls,
	 * etc
	 * 
	 * @param g
	 *            the graphics that draws
	 */
	@Override
	public void paint(Graphics g) {
		if (gameOver) {
			if (alpha >= 1) {
				alpha = 1.0f;
				canClick = true;
			}

			Graphics2D g2d = (Graphics2D) g;
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			g2d.drawImage(!lost ? loseImage : winImage, 0, 0, width, height, null);

			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			alpha += 0.01;
			return;
		}
		g.clearRect(0, 0, width, height);
		bkgd.draw(g);

		for (Player p : players) {
			if (!p.shell.dead) {
				Graphics2D g2d = (Graphics2D) g;
				AffineTransform old = g2d.getTransform();
				Rectangle rec = p.shell.getBounds();
				g2d.rotate(Math.toRadians(p.gun.angle), rec.getX() + (rec.getWidth() / 2),
						rec.getY() + (rec.getHeight() / 2));
				p.gun.draw(g2d);
				g2d.setTransform(old);
			}
			p.draw(g);
		}

		for (Bullet b : bullets) {
			b.draw(g);
		}

		for (int i = 0; i < environment.size(); i++) {
			Entity item = environment.get(i);
			item.draw(g);
		}

	}

	/*
	 * KeyListener events:
	 * 
	 * handled by the Tank class, which further parcels out responsibility to
	 * listen to events
	 */

	/**
	 * checks for key input and calls player keyPressed accordingly
	 * 
	 * @param e
	 *            the KeyEvent of the key pressed
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		players.get(id).keyPressed(e);
	}

	/**
	 * checks for key release and calls player keyReleased accordingly
	 * 
	 * @param e
	 *            the KeyEvent of the keyPressed
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		players.get(id).keyReleased(e);
	}

	/**
	 * checks for mouse Pressed and calls player mouseClicked accordingly also
	 * restarts the game once it is gameOver
	 * 
	 * @param e
	 *            the MouseEvent of the keyPressed
	 */
	@Override
	public void mousePressed(MouseEvent arg0) {

		if (gameOver) {
			if (!canClick)
				return;
			beginAgain = true;
		}
		players.get(id).mouseClicked(arg0);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * checks if mouse moved and calls player.mouseMoved accordingly
	 * 
	 * @param e
	 *            the Mouse event
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		players.get(id).mouseMoved(e);
	}

	/**
	 * resets GameBoard back to starting locations
	 */
	public void reset() {
		gameOver = false;
		beginAgain = false;
		canClick = false;
		alpha = 0.0f;
		bullets.clear();
		for (Player p : players) {
			p.reset();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}