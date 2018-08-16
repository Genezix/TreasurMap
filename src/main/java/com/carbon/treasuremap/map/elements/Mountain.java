package com.carbon.treasuremap.map.elements;

/**
 * This class represent a mountain. <br>
 * Adventurer cannot go on the same case of a mountain.
 * 
 * @author Florian
 *
 */
public class Mountain extends MapElement {
	public Mountain(int x, int y) {
		super(x, y);
	}

	@Override
	public String toString() {
		return "M - " + this.x + " - " + this.y;
	}
}
