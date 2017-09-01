package mainPackage;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

/**
 * this acts as the first screen where the player enters their name
 *
 * @author Aaron Chen
 * @version Apr 3, 2016
 * @author Period: 3
 * @author Assignment: AdventuresOfKidco
 *
 * @author Sources: NONE
 */
public class NameGetter extends JPanel implements ActionListener {

	private static int height;

	private static int width;

	private JTextField name;

	private JLabel label;

	/**
	 * the constructor of NameGetter
	 * 
	 * @param w
	 *            the width of the screen
	 * @param h
	 *            the height of the screen
	 */
	public NameGetter(int w, int h) {
		width = w;
		height = h;
		name = new JTextField(20);
		label = new JLabel("What's your name? (hit enter when finished)");

		name.addActionListener(this);
		name.setActionCommand("name");

		this.add(label);
		this.add(name);

		name.setVisible(true);
		label.setVisible(true);
		this.setFocusable(true);
		this.setVisible(true);
	}

	/**
	 * checks if there is an update in the textbox or if button is pressed
	 * 
	 * @param e
	 *            the action that was performed
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		new Thread(new Runnable() {
			public void run() {
				try {
					SwingUtilities.invokeAndWait(new Runnable() {
						public void run() {
							String text = name.getText();
							Client.myName = text;
							name.setText("Waiting to start...");
							name.setEditable(false);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
				Client.waitForEveryone();
			}
		}).start();

	}

}