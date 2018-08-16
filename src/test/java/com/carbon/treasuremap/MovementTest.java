package com.carbon.treasuremap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.carbon.treasuremap.exceptions.TreasureMapAddElementException;
import com.carbon.treasuremap.exceptions.TreasureMapFileParseException;
import com.carbon.treasuremap.file.FileLinesParser;
import com.carbon.treasuremap.map.Heading;
import com.carbon.treasuremap.map.TreasureMap;
import com.carbon.treasuremap.map.elements.Adventurer;

public class MovementTest {

	@Test
	public void testHeadingEnum() {
		assertNull(Heading.getHeading("test"));

		assertEquals(Heading.N, Heading.getHeading("N"));
		assertEquals(Heading.E, Heading.getHeading("E"));
		assertEquals(Heading.S, Heading.getHeading("S"));
		assertEquals(Heading.W, Heading.getHeading("W"));

		assertEquals("N", Heading.N.toString());
		assertEquals("E", Heading.E.toString());
		assertEquals("S", Heading.S.toString());
		assertEquals("W", Heading.W.toString());

		assertEquals(Heading.E, Heading.N.getRightHeading());
		assertEquals(Heading.S, Heading.E.getRightHeading());
		assertEquals(Heading.W, Heading.S.getRightHeading());
		assertEquals(Heading.N, Heading.W.getRightHeading());

		assertEquals(Heading.W, Heading.N.getLeftHeading());
		assertEquals(Heading.N, Heading.E.getLeftHeading());
		assertEquals(Heading.E, Heading.S.getLeftHeading());
		assertEquals(Heading.S, Heading.W.getLeftHeading());
	}

	@Test
	public void testCreateGoodMapWithoutMovement() {
		// map
		int mapWidth = 5;
		int mapHeidth = 5;

		// treasure
		int tX = 4;
		int tY = 1;
		int tValue = 5;

		// mountain
		int mX = 4;
		int mY = 0;

		// adventurer
		int aX = 2;
		int aY = 0;
		String aHeading = "S";
		String aMovements = "";

		movementTest(mapWidth, mapHeidth, tX, tY, tValue, tValue, mX, mY, aX, aY, aHeading, aMovements, aX, aY,
				aHeading, 0);
	}

	@Test
	public void testOneMove() {
		// No treasure, No mountain

		// map
		int mapWidth = 5;
		int mapHeidth = 5;

		// adventurer
		int aX = 2;
		int aY = 2;

		String aHeading = "N";
		String aMovements = "A";

		int expectedAX = 2;
		int expectedAY = aY - 1;
		int expectedAValue = 0;

		movementTest(mapWidth, mapHeidth, aX, aY, aHeading, aMovements, expectedAX, expectedAY, aHeading,
				expectedAValue);

		// Other headings
		movementTest(mapWidth, mapHeidth, aX, aY, "S", aMovements, aX, aY + 1, "S", expectedAValue);
		movementTest(mapWidth, mapHeidth, aX, aY, "E", aMovements, aX + 1, aY, "E", expectedAValue);
		movementTest(mapWidth, mapHeidth, aX, aY, "W", aMovements, aX - 1, aY, "W", expectedAValue);
	}

	@Test
	public void testOneMoveOutsideTheMap() {
		// No treasure, No mountain, test one move outside the map

		// map
		int mapWidth = 5;
		int mapHeidth = 5;

		// adventurer
		int aX = 2;
		int aY = 0;
		String aHeading = "N";
		String aMovements = "A";

		int expectedAX = 2;
		int expectedAY = 0; // It me stay on 0 position
		int expectedAValue = 0;

		movementTest(mapWidth, mapHeidth, aX, aY, aHeading, aMovements, expectedAX, expectedAY, aHeading,
				expectedAValue);

		// Other headings
		movementTest(mapWidth, mapHeidth, aX, mapHeidth - 1, "S", aMovements, expectedAX, mapHeidth - 1, "S",
				expectedAValue);
		movementTest(mapWidth, mapHeidth, mapWidth - 1, aY, "E", aMovements, mapWidth - 1, expectedAY, "E",
				expectedAValue);
		movementTest(mapWidth, mapHeidth, 0, aY, "W", aMovements, 0, expectedAY, "W", expectedAValue);
	}

	@Test
	public void testMultipleMoveInOneDirection() {
		// No treasure, No mountain

		// map
		int mapWidth = 10;
		int mapHeidth = 10;

		// adventurer
		int aX = 4;
		int aY = 4;

		String aHeading = "N";
		String aMovements = "AAA";

		int expectedAX = 4;
		int expectedAY = 1;
		int expectedAValue = 0;

		movementTest(mapWidth, mapHeidth, aX, aY, aHeading, aMovements, expectedAX, expectedAY, aHeading,
				expectedAValue);

		// Other headings
		movementTest(mapWidth, mapHeidth, aX, aY, "S", aMovements, aX, aY + 3, "S", expectedAValue);
		movementTest(mapWidth, mapHeidth, aX, aY, "E", aMovements, aX + 3, aY, "E", expectedAValue);
		movementTest(mapWidth, mapHeidth, aX, aY, "W", aMovements, aX - 3, aY, "W", expectedAValue);
	}

	@Test
	public void testOneChangeHeading() {
		// No treasure, No mountain

		// map
		int mapWidth = 10;
		int mapHeidth = 10;

		// adventurer
		int aX = 4;
		int aY = 4;

		String aHeading = "N";
		String aMovements = "D";

		int expectedAX = aX;
		int expectedAY = aY;
		int expectedAValue = 0;
		String expectedAHeading = "E";

		movementTest(mapWidth, mapHeidth, aX, aY, aHeading, aMovements, expectedAX, expectedAY, expectedAHeading,
				expectedAValue);
		movementTest(mapWidth, mapHeidth, aX, aY, "E", aMovements, expectedAX, expectedAY, "S", expectedAValue);
		movementTest(mapWidth, mapHeidth, aX, aY, "S", aMovements, expectedAX, expectedAY, "W", expectedAValue);
		movementTest(mapWidth, mapHeidth, aX, aY, "W", aMovements, expectedAX, expectedAY, "N", expectedAValue);

		// Turn to the left
		movementTest(mapWidth, mapHeidth, aX, aY, "N", "G", expectedAX, expectedAY, "W", expectedAValue);
		movementTest(mapWidth, mapHeidth, aX, aY, "E", "G", expectedAX, expectedAY, "N", expectedAValue);
		movementTest(mapWidth, mapHeidth, aX, aY, "S", "G", expectedAX, expectedAY, "E", expectedAValue);
		movementTest(mapWidth, mapHeidth, aX, aY, "W", "G", expectedAX, expectedAY, "S", expectedAValue);
	}

	@Test
	public void testMultipleChangeHeading() {
		// No treasure, No mountain

		// map
		int mapWidth = 10;
		int mapHeidth = 10;

		// adventurer
		int aX = 4;
		int aY = 4;

		String aHeading = "N";
		String aMovements = "DD";

		int expectedAX = aX;
		int expectedAY = aY;
		int expectedAValue = 0;
		String expectedAHeading = "S";

		movementTest(mapWidth, mapHeidth, aX, aY, aHeading, aMovements, expectedAX, expectedAY, expectedAHeading,
				expectedAValue);
		movementTest(mapWidth, mapHeidth, aX, aY, "E", aMovements, expectedAX, expectedAY, "W", expectedAValue);
		movementTest(mapWidth, mapHeidth, aX, aY, "S", aMovements, expectedAX, expectedAY, "N", expectedAValue);
		movementTest(mapWidth, mapHeidth, aX, aY, "W", aMovements, expectedAX, expectedAY, "E", expectedAValue);

		// Turn to the left
		movementTest(mapWidth, mapHeidth, aX, aY, "N", "GG", expectedAX, expectedAY, "S", expectedAValue);
		movementTest(mapWidth, mapHeidth, aX, aY, "E", "GG", expectedAX, expectedAY, "W", expectedAValue);
		movementTest(mapWidth, mapHeidth, aX, aY, "S", "GG", expectedAX, expectedAY, "N", expectedAValue);
		movementTest(mapWidth, mapHeidth, aX, aY, "W", "GG", expectedAX, expectedAY, "E", expectedAValue);
	}

	@Test
	public void testMountain() {
		// No treasure

		// map
		int mapWidth = 5;
		int mapHeidth = 5;

		// treasure
		int mX = 2;
		int mY = 2;

		// adventurer
		int aX = 1;
		int aY = 2;
		int expectedAX = aX;
		int expectedAY = aY;
		String aHeading = "E";
		String aMovements = "A";

		int expectedAValue = 0;

		movementTest(mapWidth, mapHeidth, -1, -1, -1, -1, mX, mY, aX, aY, aHeading, aMovements, expectedAX, expectedAY,
				aHeading, expectedAValue);
	}

	@Test
	public void testTreasure() {
		// No mountain

		// map
		int mapWidth = 5;
		int mapHeidth = 5;

		// treasure
		int tX = 4;
		int tY = 1;
		int tValue = 5;
		int expectedTValue = 4;

		// adventurer
		int aX = 4;
		int aY = 0;
		String aHeading = "S";
		String aMovements = "A";
		int expectedAX = aX;
		int expectedAY = aY + 1;

		int expectedAValue = 1;

		movementTest(mapWidth, mapHeidth, tX, tY, tValue, expectedTValue, -1, -1, aX, aY, aHeading, aMovements,
				expectedAX, expectedAY, aHeading, expectedAValue);
	}

	@Test
	public void testMultipleAdventurer() {
		// No treasure, No mountain

		// map
		int mapWidth = 10;
		int mapHeidth = 10;

		// adventurer
		int a1X = 1;
		int a1Y = 2;
		int a1ExpectedX = 1;
		int a1ExpectedY = 0;

		int a2X = 0;
		int a2Y = 1;
		int a2ExpectedX = 1;
		int a2ExpectedY = 1;

		FileLinesParser parser = new FileLinesParser();
		List<String> input = new ArrayList<>();

		// map
		input.add("C - " + mapWidth + " - " + mapHeidth);

		// adventurer
		input.add("A - Bear - " + a1X + " - " + a1Y + " - N - AA");
		input.add("A - Mike - " + a2X + " - " + a2Y + " - E - AA");

		// create treasure map and verify all datas
		TreasureMap treasureMap = null;

		try {
			treasureMap = parser.parseFile(input);
			treasureMap.moveAdventurers();
		} catch (TreasureMapFileParseException | TreasureMapAddElementException e) {
			fail(e.getMessage());
		}

		assertNotNull(treasureMap);
		assertEquals(mapWidth, treasureMap.getWidth());
		assertEquals(mapHeidth, treasureMap.getHeight());

		assertNotNull(treasureMap.getCase(a1ExpectedX, a1ExpectedY).getAdventurer() != null);
		assertNotNull(treasureMap.getCase(a2ExpectedX, a2ExpectedY).getAdventurer() != null);
	}

	private void movementTest(int mapWidth, int mapHeidth, int aX, int aY, String aHeading, String aMovements,
			int expectedEndAX, int expectedEndAY, String expectedEndAH, int expectedEndAValue) {
		movementTest(mapWidth, mapHeidth, -1, -1, -1, -1, -1, -1, aX, aY, aHeading, aMovements, expectedEndAX,
				expectedEndAY, expectedEndAH, expectedEndAValue);
	}

	private void movementTest(int mapWidth, int mapHeidth, int tX, int tY, int tValue, int expectedEndTValue, int mX,
			int mY, int aX, int aY, String aHeading, String aMovements, int expectedEndAX, int expectedEndAY,
			String expectedEndAH, int expectedEndAValue) {
		FileLinesParser parser = new FileLinesParser();
		List<String> input = new ArrayList<>();

		// map
		input.add("C - " + mapWidth + " - " + mapHeidth);

		// treasure
		if (tX != -1) {
			input.add("T - " + tX + " - " + tY + " - " + tValue);
		}

		// mountain
		if (mX != -1) {
			input.add("M - " + mX + " - " + mY);
		}

		// adventurer
		String aName = "LaraCroft";
		input.add("A - " + aName + " - " + aX + " - " + aY + " - " + aHeading + " - " + aMovements);

		// create treasure map and verify all datas
		TreasureMap treasureMap = null;

		try {
			treasureMap = parser.parseFile(input);
			treasureMap.moveAdventurers();
		} catch (TreasureMapFileParseException | TreasureMapAddElementException e) {
			fail(e.getMessage());
		}

		assertNotNull(treasureMap);
		assertEquals(mapWidth, treasureMap.getWidth());
		assertEquals(mapHeidth, treasureMap.getHeight());

		if (tX != -1) {
			assertNotNull(treasureMap.getCase(tX, tY).getTreasure());
			assertEquals(expectedEndTValue, treasureMap.getCase(tX, tY).getTreasure().getNbTreasure());
		}

		if (mX != -1) {
			assertNotNull(treasureMap.getCase(mX, mY).getMountain());
		}

		Adventurer adventurer = treasureMap.getCase(expectedEndAX, expectedEndAY).getAdventurer();
		assertNotNull(adventurer);
		assertEquals(aName, adventurer.getName());
		assertEquals(expectedEndAH, adventurer.getHeading().toString());
		assertEquals(expectedEndAValue, adventurer.getNbTreasures());
	}
}
