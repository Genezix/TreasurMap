package com.carbon.treasuremap.map;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carbon.treasuremap.exceptions.TreasureMapAddElementException;
import com.carbon.treasuremap.map.elements.Adventurer;
import com.carbon.treasuremap.map.elements.Mountain;
import com.carbon.treasuremap.map.elements.Treasure;

/**
 * This class contains all cases of the treasure map.
 * 
 * Adventurer will be place and moved one these cases.
 * 
 * @author Florian
 *
 */
public class TreasureMap {

	private final static Logger logger = LoggerFactory.getLogger(TreasureMap.class);

	public final static String ERROR_NO_ADVENTURER = "No adventurer specified in file";
	public final static String ERROR_ADD_ELEMENT_OUTSIDE_THE_MAP = "Try to add element outside the map";
	public final static String ERROR_ADD_ELEMENT_ON_ANOTHER_ELEMENT = "Try to add element on another element";
	public final static String ERROR_ADD_ADVENTURER_ON_ANOTHER_ELEMENT = "Try to add adventurer at the same position of another adventurer or a mountain";

	private final Case[][] cases;

	private final int mapWidth;
	private final int mapHeight;

	private List<Adventurer> adventurers = null;
	private List<Mountain> mountains = null;
	private List<Treasure> treasures = null;

	public TreasureMap(int width, int height) {
		this.mapWidth = width;
		this.mapHeight = height;

		cases = new Case[this.mapWidth][this.mapHeight];

		for (int x = 0; x < this.mapWidth; x++) {
			for (int y = 0; y < this.mapHeight; y++) {
				cases[x][y] = new Case();
			}
		}
	}

	/**
	 * Create {@link Mountain} and add it in the map.
	 * 
	 * @param x : horizontal position
	 * @param y : vertical position
	 * @throws TreasureMapAddElementException If the mountain is added outside the
	 *                                        map or if another element is already
	 *                                        added on this position, it throws a
	 *                                        TreasureMapAddElementException
	 */
	public void addMountainInMap(int x, int y) throws TreasureMapAddElementException {
		if (x < 0 || x >= mapWidth || y < 0 || y >= mapHeight) {
			throw new TreasureMapAddElementException(ERROR_ADD_ELEMENT_OUTSIDE_THE_MAP);
		}

		// if the case contains a mountain or a treasure, throws exception
		Case case1 = cases[x][y];
		if (case1.getTreasure() != null || case1.getMountain() != null || case1.getAdventurer() != null) {
			throw new TreasureMapAddElementException(ERROR_ADD_ELEMENT_ON_ANOTHER_ELEMENT);
		}

		if (mountains == null) {
			mountains = new LinkedList<>();
		}

		Mountain mountain = new Mountain(x, y);
		case1.setMountain(mountain);
		mountains.add(mountain);
	}

	/**
	 * Create {@link Treasure} and add it in the map.
	 * 
	 * @param x     : horizontal position
	 * @param y     : vertical position
	 * @param value : nbTrasure on this case
	 * @throws TreasureMapAddElementException If the treasure is added outside the
	 *                                        map or if another element is already
	 *                                        added on this position, it throws a
	 *                                        TreasureMapAddElementException
	 */
	public void addTreasureInMap(int x, int y, int value) throws TreasureMapAddElementException {
		if (x < 0 || x >= mapWidth || y < 0 || y >= mapHeight) {
			throw new TreasureMapAddElementException(ERROR_ADD_ELEMENT_OUTSIDE_THE_MAP);
		}

		// if the case contains a mountain or a treasure, throws exception
		Case case1 = cases[x][y];
		if (case1.getTreasure() != null || case1.getMountain() != null || case1.getAdventurer() != null) {
			throw new TreasureMapAddElementException(ERROR_ADD_ELEMENT_ON_ANOTHER_ELEMENT);
		}

		if (treasures == null) {
			treasures = new LinkedList<>();
		}

		Treasure treasure = new Treasure(x, y, value);
		case1.setTreasure(treasure);
		treasures.add(treasure);
	}

	/**
	 * Create {@link Adventurer} and add it in the map.
	 * 
	 * @param name    : adventurer name
	 * @param x       : adventurer horizontal position
	 * @param y       : adventurer vertical position
	 * @param heading : adventurer heading (NSEW)
	 * @param roadMap : adventurer movements
	 * @throws TreasureMapAddElementException If the adventurer is added outside the
	 *                                        map or if a mountain is already added,
	 *                                        it throws a
	 *                                        TreasureMapAddElementException
	 */
	public void addAdventurerInMap(String name, int x, int y, String heading, String roadMap)
			throws TreasureMapAddElementException {
		if (x < 0 || x >= mapWidth || y < 0 || y >= mapHeight) {
			throw new TreasureMapAddElementException(ERROR_ADD_ELEMENT_OUTSIDE_THE_MAP);
		}

		Case case1 = cases[x][y];
		// if the case contains a mountain or an adventurer, throws exception
		if (case1.getMountain() != null || case1.getAdventurer() != null) {
			throw new TreasureMapAddElementException(ERROR_ADD_ADVENTURER_ON_ANOTHER_ELEMENT);
		}

		if (adventurers == null) {
			adventurers = new LinkedList<>();
		}

		Adventurer adventurer = new Adventurer(name, x, y, Heading.getHeading(heading), roadMap);
		case1.setAdventurer(adventurer);
		adventurers.add(adventurer);
	}

	/**
	 * Execute all adventurer movements.
	 * 
	 * @throws TreasureMapAddElementException if no adventurer added throws
	 *                                        TreasureMapAddElementException
	 */
	public void moveAdventurers() throws TreasureMapAddElementException {
		printMapStatus("Start map status");

		if (adventurers == null) {
			throw new TreasureMapAddElementException(ERROR_NO_ADVENTURER);
		}

		// execute move round by round
		// it means that on the round one, each adventurer will perform one move, etc...
		boolean keepGoing = true;
		int round = 0;
		while (keepGoing) {
			keepGoing = false;
			for (Adventurer adventurer : adventurers) {
				if (adventurer.getRoadMap().length() > round) {
					moveAdventurer(adventurer, adventurer.getRoadMap().charAt(round));
					if (adventurer.getRoadMap().length() > round + 1) {
						keepGoing = true;
					}
				}
			}
			round++;
		}

		printMapStatus("End map status");
	}

	private void moveAdventurer(Adventurer adventurer, char move) {
		// Get current position of the adventurer
		int x = adventurer.getX();
		int y = adventurer.getY();
		Heading heading = adventurer.getHeading();

		if (move == 'A') {
			// move on front is if possible
			int newX = adventurer.getXIfMove(mapWidth - 1);
			int newY = adventurer.getYIfMove(mapHeight - 1);

			if (cases[newX][newY].getAdventurer() == null && cases[newX][newY].getMountain() == null) {
				move(adventurer, x, y, newX, newY);
				x = newX;
				y = newY;
			}
		} else if (move == 'D') {
			// turn to the right
			heading = heading.getRightHeading();
		} else if (move == 'G') {
			// turn to the left
			heading = heading.getLeftHeading();
		}

		// Set new position of the adventurer
		adventurer.setX(x);
		adventurer.setY(y);
		adventurer.setHeading(heading);
	}

	private void move(Adventurer adventurer, int x, int y, int newX, int newY) {
		cases[x][y].setAdventurer(null);
		cases[newX][newY].setAdventurer(adventurer);

		// If the new case contains a treasure and is contains more than 0 treasure add
		// one to the adventurer and remove one of the treasure.
		Treasure treasure = cases[newX][newY].getTreasure();
		if (treasure != null && treasure.getNbTreasure() > 0) {
			treasure.removeOneTreasure();
			adventurer.addTreasure();
		}
	}

	public int getWidth() {
		return mapWidth;
	}

	public int getHeight() {
		return mapHeight;
	}

	/**
	 * Return the case at position (x,y)
	 * 
	 * @param x : horizontal position
	 * @param y : vertical position
	 * @return {@link Case}
	 */
	public Case getCase(int x, int y) {
		return cases[x][y];
	}

	/**
	 * Get result to write in file.
	 * 
	 * @return current state of the treasurMap.
	 */
	public String buildMapStatusResult() {
		String s = System.lineSeparator();
		StringBuilder builder = new StringBuilder();
		builder.append("C - " + mapWidth + " - " + mapHeight);
		mountains.forEach(m -> builder.append(s + m));
		treasures.stream().filter(t -> cases[t.getX()][t.getY()].getAdventurer() == null)
				.forEach(t -> builder.append(s + t));
		adventurers.forEach(a -> builder.append(s + a));
		return builder.toString();
	}

	/**
	 * Print treasure map status, for debug.
	 * 
	 * @param message to add before log treasure map.
	 */
	public void printMapStatus(String message) {
		if (logger.isInfoEnabled()) {
			StringBuilder builder = new StringBuilder();

			for (int y = 0; y < mapHeight; y++) {
				for (int x = 0; x < mapWidth; x++) {
					builder.append(cases[x][y] + "|");
				}
				builder.append(System.lineSeparator());
			}

			logger.info(message + System.lineSeparator() + builder.toString());
		}
	}
}
