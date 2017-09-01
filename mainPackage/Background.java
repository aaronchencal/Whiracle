package mainPackage;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Draws the Background
 *
 * @author Richard Yang
 * @version Apr 3, 2016
 * @author Period: 3
 * @author Assignment: AdventuresOfKidco
 *
 * @author Sources: None
 */
public class Background {
	private Image[] backgrounds;

	private int width;

	private int height;

	private int frame = 0;

	private int drawFrame = 0;

	/**
	 * Stores width and height and initializes the background images
	 * 
	 * @param width
	 *            width of the background
	 * @param height
	 *            height of the background
	 */
	public Background(int width, int height) {
		backgrounds = new Image[8];
		this.width = width;
		this.height = height;
		try {
			for (int i = 0; i < backgrounds.length; i++) {
				backgrounds[i] = ImageIO.read(new File("frame" + (i + 1) + ".gif"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Draws the background
	 * 
	 * @param g
	 *            graphics used to draw
	 */
	public void draw(Graphics g) {
		frame++;
		if (frame == 100) {
			frame = 0;
		}

		if (frame % 10 == 0) {
			drawFrame++;
		}
		if (drawFrame == 8) {
			drawFrame = 0;
		}

		g.drawImage(backgrounds[drawFrame], 0, 0, width, height, null);
	}

}