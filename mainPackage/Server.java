package mainPackage;

import java.net.*;

import java.awt.Toolkit;

import java.awt.Dimension;
import java.io.*;

/**
 * The server that serves to organize the game all clients connect to this
 * 
 * @author Aaron Chen
 * @version May 26, 2016
 * @author Period: 3
 * @author Assignment: AdventuresOfKidco
 *
 * @author Sources: None
 */
public class Server {

	public static ServerBoard serverBoard;

	public static int port = 8120;

	public static boolean startGame = false;

	public volatile static boolean p1ready;

	public volatile static boolean p2ready;
	public static boolean beginAgain;

	/**
	 * constructor of the server, initializes the serverBoard
	 * 
	 * @param args
	 *            nothing important
	 * @throws IOException
	 *             incase of errors in opening port
	 */
	public static void main(String[] args) throws IOException {

		serverBoard = new ServerBoard(Data.width, Data.height);

		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while (true) {
				Socket socketsocket = serverSocket.accept();
				socketsocket.setTcpNoDelay(true);
				serverBoard.addPlayer();
				new ServerThread(socketsocket, serverBoard.getPlayerCount() - 1).start();
				if (serverBoard.getPlayerCount() == 2) {
					serverSocket.close();
					break;
				}
			}

			while (!(Server.p1ready && Server.p2ready)) {
			}
			Server.startGame = true;
		} catch (IOException e) {
			System.out.println(
					"Exception caught when trying to listen on port " + port + " or listening for a connection");
			System.out.println(e.getMessage());
		}
	}
}