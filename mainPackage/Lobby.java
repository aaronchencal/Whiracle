package mainPackage;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.*;

/**
 * this acts to organize the players before the actual game starts
 * 
 * @author Richard Yang
 * @version Apr 3, 2016
 * @author Period: 3
 * @author Assignment: AdventuresOfKidco
 *
 * @author Sources: None
 */
public class Lobby extends JPanel {

	private static int height;

	private static int width;

	public ArrayList<Player> players;

	private NameGetter ng;

	private JButton connectToServer;

	/**
	 * Constructor of the Lobby, makes name
	 * 
	 * @param w
	 * @param h
	 */
	public Lobby(int w, int h) {
		width = w;
		height = h;
		players = new ArrayList<Player>();

		ng = new NameGetter(500, 500);
		this.add(ng);
		ng.setVisible(false);
		connectToServer = new JButton("Connect to Server");
		connectToServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean tryServer = Client.init();
				if (!tryServer) {
					connectToServer.setText("Something failed, press to try again");
				} else {
					ng.setVisible(true);
					connectToServer.setVisible(false);
				}
			}
		});

		this.add(connectToServer);
		connectToServer.setVisible(true);

		this.setFocusable(true);
		this.setVisible(true);
	}

}