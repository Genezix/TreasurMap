package com.carbon.treasuremap.map;

import com.carbon.treasuremap.map.elements.Adventurer;
import com.carbon.treasuremap.map.elements.Mountain;
import com.carbon.treasuremap.map.elements.Treasure;

/**
 * This class represent one case in the map.<br>
 * It can be empty.<br>
 * It can contains a (treasure and/or adventurer) or mountain.
 * 
 * @author Florian
 *
 */
public class Case {
	private Treasure treasure = null;
	private Mountain mountain = null;
	private Adventurer adventurer = null;

	public Case() {
		super();
	}

	public Treasure getTreasure() {
		return treasure;
	}

	public void setTreasure(Treasure treasure) {
		this.treasure = treasure;
	}

	public Mountain getMountain() {
		return mountain;
	}

	public void setMountain(Mountain mountain) {
		this.mountain = mountain;
	}

	public void setAdventurer(Adventurer adventurer) {
		this.adventurer = adventurer;
	}

	public Adventurer getAdventurer() {
		return this.adventurer;
	}

	@Override
	public String toString() {
		if (this.adventurer != null) {
			return "(" + this.adventurer.getName().charAt(0) + ")";
		}

		if (this.treasure != null) {
			int value = this.treasure.getNbTreasure();
			return String.format(value < 10 ? "%2d " : "%3d", this.treasure.getNbTreasure()).toString();
		}

		if (mountain != null) {
			return " M ";
		}

		return " . ";
	}
}
