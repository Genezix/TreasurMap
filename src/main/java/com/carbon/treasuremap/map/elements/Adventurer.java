package com.carbon.treasuremap.map.elements;

import com.carbon.treasuremap.map.Heading;

/**
 * This class represent an adventurer.
 * 
 * @author Florian
 *
 */
public class Adventurer extends MapElement {
	private Heading heading;
	private final String roadMap;
	private final String name;
	private int nbTreasures = 0;

	public Adventurer(String name, int x, int y, Heading heading, String roadMap) {
		super(x, y);
		this.heading = heading;
		this.name = name;
		this.roadMap = roadMap;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Heading getHeading() {
		return heading;
	}

	public void setHeading(Heading heading) {
		this.heading = heading;
	}

	public String getRoadMap() {
		return roadMap;
	}

	public String getName() {
		return name;
	}

	public int getNbTreasures() {
		return nbTreasures;
	}

	public void addTreasure() {
		this.nbTreasures++;
	}

	/**
	 * Estimated horizontal position after movements.
	 * 
	 * @param max : end of the map
	 * @return estimated position : x, x + 1 or x - 1
	 */
	public int getXIfMove(int max) {
		switch (heading) {
		case E:
			return x + 1 > max ? x : x + 1;
		case W:
			return x - 1 < 0 ? x : x - 1;
		default:
			return x;
		}
	}

	/**
	 * Estimated vertical position after movements.
	 * 
	 * @param max : end of the map
	 * @return estimated position : y, y + 1 or y - 1
	 */
	public int getYIfMove(int max) {
		switch (heading) {
		case N:
			return y - 1 < 0 ? y : y - 1;
		case S:
			return y + 1 > max ? y : y + 1;
		default:
			return y;
		}
	}

	@Override
	public String toString() {
		return "A - " + name + " - " + this.x + " - " + this.y + " - " + heading + " - " + nbTreasures;
	}
}