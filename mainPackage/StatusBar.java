package mainPackage;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * acts as the GUI of the player in game, showing health and lives
 * 
 * @author Richard Yang
 * @version May 26, 2016
 * @author Period: 3
 * @author Assignment: AdventuresOfKidco
 *
 * @author Sources: None
 */
public class StatusBar {

	public int x;
	public int y;
	private int width;
	private int height;
	private Image bar;
	private Image[] healthbars;
	private Image[] ehealthbars;
	public int tHealth;
	boolean isFriendly;
	public int id;

	/**
	 * the constructor for the status bar
	 * 
	 * @param friend
	 *            determines the color of the health bar
	 * @param x
	 *            x location of player
	 * @param y
	 *            y location of player
	 * @param health
	 *            total starting health
	 * @param id
	 *            to get the name of the player
	 */
	public StatusBar(boolean friend, int x, int y, int health, int id) {
		this.id = id;
		isFriendly = friend;
		this.x = x - 200;
		this.y = y - 200;
		this.tHealth = health;
		width = 170;
		height = 30;
		ehealthbars = new Image[6];
		healthbars = new Image[6];
		try {
			bar = ImageIO.read(new File("bar1.png"));
			healthbars[0] = ImageIO.read(new File("healthbar0.png"));
			healthbars[1] = ImageIO.read(new File("healthbar1.png"));
			healthbars[2] = ImageIO.read(new File("healthbar2.png"));
			healthbars[3] = ImageIO.read(new File("healthbar3.png"));
			healthbars[4] = ImageIO.read(new File("healthbar4.png"));
			healthbars[5] = ImageIO.read(new File("healthbar5.png"));
			ehealthbars[0] = ImageIO.read(new File("ebar0.png"));
			ehealthbars[1] = ImageIO.read(new File("ebar1.png"));
			ehealthbars[2] = ImageIO.read(new File("ebar2.png"));
			ehealthbars[3] = ImageIO.read(new File("ebar3.png"));
			ehealthbars[4] = ImageIO.read(new File("ebar4.png"));
			ehealthbars[5] = ImageIO.read(new File("ebar5.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * draws the name, healthbar, and lives of the player
	 * 
	 * @param g
	 *            the graphics to draw everything
	 */
	public void draw(Graphics g, int frames) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 20));

		if (id < Client.names.size() && Client.names.get(id) != null)
			g.drawString(Client.names.get(id), x, y - 20);

		g.setColor(Color.YELLOW);
		g.fillRect(x + 45, y + 23, (int) ((width - 45) * ((0.0 + frames) / Gun.MAX)), 5);
		g.setColor(Color.WHITE);

		g.drawImage(bar, x, y, width, height, null);
		if (isFriendly) {
			g.drawImage(healthbars[Data.convertToHealth(tHealth)], x, y, width, height, null);
		} else {
			g.drawImage(ehealthbars[Data.convertToHealth(tHealth)], x, y, width, height, null);
		}

		g.drawString(String.valueOf(Data.convertToStock(tHealth)), x + 9, y + 22);

	}

}
