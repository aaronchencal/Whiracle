package mainPackage;

import java.awt.FlowLayout;
import java.awt.event.*;

import javax.swing.*;

/**
 * this holds the FRAME of the game...think of it as the window of the game,
 * doesn't actually control what's inside the game
 *
 * @author Richard Yang
 * @version Apr 3, 2016
 * @author Period: 3
 * @author Assignment: AdventuresOfKidco
 *
 * @author Sources: None
 */
public class MainFrame extends JFrame {
	public Lobby lobby;

	public GameBoard gameBoard;

	/**
	 * Constructor of the mainFrame, makes the lobby and gameBoard to a
	 * uniformed width and height
	 */
	public MainFrame() {

		setSize(Data.width, Data.height);
		setLocationRelativeTo(null);

		setResizable(false);
		setVisible(true);
		lobby = new Lobby(getWidth(), getHeight());
		gameBoard = new GameBoard(getWidth(), getHeight());
		startLobby();
		setVisible(true);
	}

	/**
	 * starts the lobby and puts it in focus
	 */
	public void startLobby() {
		this.getContentPane().add(lobby);
		lobby.requestFocus();
	}

	/**
	 * ends the lobby and starts the game
	 */
	public void endLobbyStartGame() {
		this.getContentPane().remove(lobby);
		this.getContentPane().add(gameBoard);
		this.revalidate();
		gameBoard.repaint();
		gameBoard.requestFocusInWindow();
	}

}