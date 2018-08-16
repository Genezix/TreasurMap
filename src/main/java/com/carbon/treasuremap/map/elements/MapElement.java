package com.carbon.treasuremap.map.elements;

/**
 * This class an is abstract class representing an element on the map. <br>
 * It can be an adventurer, a treasure or a mountain.
 * 
 * @author Florian
 *
 */
public abstract class MapElement {
	protected int x;
	protected int y;

	public MapElement(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
