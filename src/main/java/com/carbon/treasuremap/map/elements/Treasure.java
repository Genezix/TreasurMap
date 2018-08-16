package com.carbon.treasuremap.map.elements;

/**
 * This class represent a treasure.
 * 
 * @author Florian
 *
 */
public class Treasure extends MapElement {

	private int nbTreasure;

	public Treasure(int x, int y, int value) {
		super(x, y);
		this.nbTreasure = value;
	}

	public int getNbTreasure() {
		return this.nbTreasure;
	}

	public void removeOneTreasure() {
		this.nbTreasure--;
	}

	@Override
	public String toString() {
		return "T - " + this.x + " - " + this.y + " - " + nbTreasure;
	}
}
