package mainPackage;

import java.util.ArrayList;

/**
 * used to make parsing inputs and outputs from ServerThread and ClientThread
 * easier
 * 
 * @author Richard Yang
 * @version May 26, 2016
 * @author Period: 3
 * @author Assignment: AdventuresOfKidco
 *
 * @author Sources: None
 */
public class Data {

	public final static int width = 1200;

	public final static int height = 800;

	/**
	 * _ separator 1 moving , separates x and y Encodes one player's worth of
	 * information
	 * 
	 * @param values
	 *            the player's values
	 * @param id
	 *            the id of the player
	 * @return the encoded string of player info
	 */
	public static String encodeOne(int[] values, int id) {

		String total = "";
		total += "_";
		// player x, player y, bullet x, bullet y, bullet angle, player angle,
		total += values[0];
		total += ",";
		total += values[1];
		total += ",";
		total += values[2];
		total += ",";
		total += values[3];
		total += ",";
		total += values[4];
		total += ",";
		total += values[5];
		total += ",";
		total += values[6];
		total += ",";
		total += values[7];
		// add comma, add bullet spawn information here
		return (total);
	}

	/**
	 * helper method to help calculate stock
	 * 
	 * @param tHealth
	 *            total health of player
	 * @return stock of player
	 */
	public static int convertToStock(int tHealth) {
		if (tHealth > 10)
			return 3;
		else if (tHealth > 5)
			return 2;
		else
			return 1;
	}

	/**
	 * helper method to help calculate current health of player
	 * 
	 * @param tHealth
	 *            total health of player
	 * @return current health of player
	 */
	public static int convertToHealth(int tHealth) {
		if (tHealth > 10)
			return tHealth - 10;
		else if (tHealth > 5)
			return tHealth - 5;
		else
			return tHealth;
	}

	/**
	 * encodeAll encodes player, environment, and bullets and combines it into
	 * one string
	 * 
	 * @param playerInfo
	 *            player info to encode
	 * @param env
	 *            environment to encode
	 * @param bullets
	 *            bullets to encode
	 * @return
	 */
	public static String encodeAll(ArrayList<Player> playerInfo, ArrayList<Entity> env, ArrayList<Bullet> bullets) {
		String total = "";
		for (int i = 0; i < playerInfo.size(); i++) {
			total += "_";
			total += playerInfo.get(i).shell.bounds.x;
			total += ",";
			total += playerInfo.get(i).shell.bounds.y;
			total += ",";
			total += (int) playerInfo.get(i).gun.angle;
			total += ",";
			total += playerInfo.get(i).health;
			total += ",";
			total += playerInfo.get(i).gun.frames;
		}
		String separator = "?";
		total += separator;
		for (int i = 0; i < bullets.size(); i++) {
			total += "_";
			total += bullets.get(i).getBounds().x;
			total += ",";
			total += bullets.get(i).getBounds().y;
		}

		String moreSeparator = "@";
		total += moreSeparator;
		for (int i = 0; i < env.size(); i++) {
			total += "_";
			total += env.get(i).getBounds().x;
			total += ",";
			total += env.get(i).getBounds().y;
		}
		return (total);
	}

	/**
	 * returns an array that is formatted as follows: arr[0] = x; arr[1] = y;
	 * Decodes a the string made from encodeOne back to an array
	 * 
	 * @param input
	 *            the encoded string
	 * @return the decoded array
	 */
	public static int[] decodeOne(String input) {
		input = input.substring(1);

		int[] returner = new int[8]; // px, py, bx, by, bangle, pangle, phealth,
										// pframes

		for (int i = 0; i < 7; i++) {
			returner[i] = Integer.parseInt(input.substring(0, input.indexOf(",")));
			input = input.substring(input.indexOf(",") + 1);
		}
		returner[7] = Integer.parseInt(input);
		return returner;
	}

	/**
	 * decodeAll decodes the ecodeAll into one big arraylist
	 * 
	 * @param input
	 *            encode input
	 * @return decoded arraylist
	 */
	public static ArrayList<Integer> decodeAll(String input) {
		String firstPart = input.substring(0, input.indexOf("?"));
		ArrayList<Integer> values = new ArrayList<Integer>();
		int count = 0;
		while (count + 1 < firstPart.length()) {
			firstPart = firstPart.substring(1);

			values.add(Integer.parseInt(firstPart.substring(0, firstPart.indexOf(","))));
			firstPart = firstPart.substring(firstPart.indexOf(",") + 1);
			values.add(Integer.parseInt(firstPart.substring(0, firstPart.indexOf(","))));
			firstPart = firstPart.substring(firstPart.indexOf(",") + 1);
			values.add(Integer.parseInt(firstPart.substring(0, firstPart.indexOf(","))));
			firstPart = firstPart.substring(firstPart.indexOf(",") + 1);
			values.add(Integer.parseInt(firstPart.substring(0, firstPart.indexOf(","))));
			firstPart = firstPart.substring(firstPart.indexOf(",") + 1);

			if (firstPart.indexOf("_") == -1) {
				values.add(Integer.parseInt(firstPart.substring(0, firstPart.length())));
				break;
			}
			values.add(Integer.parseInt(firstPart.substring(0, firstPart.indexOf("_"))));
			firstPart = firstPart.substring(firstPart.indexOf("_"));
		}

		values.add(Integer.MIN_VALUE);
		String secondPart = input.substring(input.indexOf("?") + 1, input.indexOf("@"));
		count = 0;
		while (count + 1 < secondPart.length()) {
			secondPart = secondPart.substring(1);

			values.add(Integer.parseInt(secondPart.substring(0, secondPart.indexOf(","))));
			secondPart = secondPart.substring(secondPart.indexOf(",") + 1);
			if (secondPart.indexOf("_") == -1) {
				values.add(Integer.parseInt(secondPart.substring(0, secondPart.length())));
				break;
			}
			values.add(Integer.parseInt(secondPart.substring(0, secondPart.indexOf("_"))));
			secondPart = secondPart.substring(secondPart.indexOf("_"));
		}
		values.add(Integer.MAX_VALUE);
		String thirdPart = input.substring(input.indexOf("@") + 1, input.length());
		count = 0;
		while (count + 1 < thirdPart.length()) {
			thirdPart = thirdPart.substring(1);

			values.add(Integer.parseInt(thirdPart.substring(0, thirdPart.indexOf(","))));
			thirdPart = thirdPart.substring(thirdPart.indexOf(",") + 1);
			if (thirdPart.indexOf("_") == -1) {
				values.add(Integer.parseInt(thirdPart.substring(0, thirdPart.length())));
				return values;
			}
			values.add(Integer.parseInt(thirdPart.substring(0, thirdPart.indexOf("_"))));
			thirdPart = thirdPart.substring(thirdPart.indexOf("_"));
		}

		return values;

	}

}