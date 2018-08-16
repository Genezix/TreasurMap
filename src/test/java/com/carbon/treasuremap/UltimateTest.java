package com.carbon.treasuremap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.carbon.treasuremap.exceptions.TreasureMapAddElementException;
import com.carbon.treasuremap.exceptions.TreasureMapFileParseException;
import com.carbon.treasuremap.file.FileLinesParser;
import com.carbon.treasuremap.map.TreasureMap;
import com.carbon.treasuremap.map.elements.Adventurer;

public class UltimateTest {
	@Test
	public void testExerciceExample() {

		// adventurer
		int a1ExpectedX = 0;
		int a1ExpectedY = 3;
		String a1ExpectedHeading = "S";
		int a1ExpectedTreasure = 3;

		FileLinesParser parser = new FileLinesParser();
		List<String> input = new ArrayList<>();

		// map
		input.add("C - 3 - 4");

		// mountains
		input.add("M - 1 - 0");
		input.add("M - 2 - 1");

		// treasures
		input.add("T - 0 - 3 - 2");
		input.add("T - 1 - 3 - 3");

		// adventurer
		input.add("A - Lara - 1 - 1 - S - AADADAGGA");

		// create treasure map and verify all datas
		TreasureMap treasureMap = null;

		try {
			treasureMap = parser.parseFile(input);
			treasureMap.moveAdventurers();
		} catch (TreasureMapFileParseException | TreasureMapAddElementException e) {
			fail(e.getMessage());
		}

		assertNotNull(treasureMap);

		Adventurer adventurer = treasureMap.getCase(a1ExpectedX, a1ExpectedY).getAdventurer();
		assertNotNull(adventurer);

		assertEquals(a1ExpectedHeading, adventurer.getHeading().toString());
		assertEquals(a1ExpectedTreasure, adventurer.getNbTreasures());

		assertNotNull(treasureMap.getCase(0, 3).getTreasure());
		assertEquals(0, treasureMap.getCase(0, 3).getTreasure().getNbTreasure());
		assertNotNull(treasureMap.getCase(1, 3).getTreasure());
		assertEquals(2, treasureMap.getCase(1, 3).getTreasure().getNbTreasure());

		String s = System.lineSeparator();
		StringBuilder builder = new StringBuilder();
		builder.append("C - 3 - 4" + s);
		builder.append("M - 1 - 0" + s);
		builder.append("M - 2 - 1" + s);
		builder.append("T - 1 - 3 - 2" + s);
		builder.append("A - Lara - 0 - 3 - S - 3");
		assertEquals(builder.toString(), treasureMap.buildMapStatusResult());
	}

	/**
	 * Just for fun ^^.
	 */
	@Test
	public void testBigMap() {
		List<String> input = new ArrayList<>();
		input.add("C - 10 - 15");
		input.add("M - 5 - 1");
		input.add("M - 8 - 2");
		input.add("M - 2 - 2");
		input.add("M - 6 - 4");
		input.add("M - 2 - 5");
		input.add("M - 1 - 10");
		input.add("M - 8 - 11");
		input.add("M - 4 - 12");
		input.add("T - 1 - 1 - 200");
		input.add("T - 4 - 3 - 5");
		input.add("T - 8 - 3 - 10");
		input.add("T - 7 - 6 - 6");
		input.add("T - 3 - 8 - 4");
		input.add("T - 4 - 8 - 3");
		input.add("T - 7 - 8 - 5");
		input.add("T - 6 - 10 - 8");
		input.add("T - 2 - 12 - 1");
		input.add("T - 3 - 12 - 1");
		input.add("T - 5 - 12 - 1");
		input.add("T - 6 - 12 - 2");
		input.add("A - Emiya - 2 - 1 - W - ADADDAAGAADAGAAAAAAAAA");
		String emiyaExpectedEndResult = "A - Emiya - 9 - 3 - E - 4";
		input.add("A - Dora - 8 - 13 - W - ADAAGAADAAADAGAAADDAAAAAAAAAA");
		String doraExpectedEndResult = "A - Dora - 7 - 14 - S - 5";
		input.add("A - Kirito - 8 - 12 - N - GAAAADAGAAGADAAADAAAAAAAAAAADAADAAA");
		String kiritoExpectedEndResult = "A - Kirito - 2 - 1 - S - 5";
		input.add("A - Pikachu - 4 - 9 - N - A");
		String pikachuExpectedEndResult = "A - Pikachu - 4 - 8 - N - 1";

		FileLinesParser parser = new FileLinesParser();
		TreasureMap treasureMap = null;
		try {
			treasureMap = parser.parseFile(input);
			treasureMap.moveAdventurers();
		} catch (TreasureMapFileParseException | TreasureMapAddElementException e) {
			fail(e.getMessage());
		}

		String s = System.lineSeparator();
		StringBuilder builder = new StringBuilder();
		builder.append("C - 10 - 15" + s);
		builder.append("M - 5 - 1" + s);
		builder.append("M - 8 - 2" + s);
		builder.append("M - 2 - 2" + s);
		builder.append("M - 6 - 4" + s);
		builder.append("M - 2 - 5" + s);
		builder.append("M - 1 - 10" + s);
		builder.append("M - 8 - 11" + s);
		builder.append("M - 4 - 12" + s);
		builder.append("T - 1 - 1 - 197" + s); // -3
		builder.append("T - 4 - 3 - 4" + s); // -1
		builder.append("T - 8 - 3 - 9" + s); // -1
		builder.append("T - 7 - 6 - 5" + s); // -1
		builder.append("T - 3 - 8 - 4" + s);
//		builder.append("T - 4 - 8 - 3" + s); An adventurer is on this case
		builder.append("T - 7 - 8 - 3" + s); // -2
		builder.append("T - 6 - 10 - 7" + s); // -1
		builder.append("T - 2 - 12 - 0" + s); // -1
		builder.append("T - 3 - 12 - 0" + s); // -1
		builder.append("T - 5 - 12 - 0" + s); // -1
		builder.append("T - 6 - 12 - 0" + s); // -2
		builder.append(emiyaExpectedEndResult + s);
		builder.append(doraExpectedEndResult + s);
		builder.append(kiritoExpectedEndResult + s);
		builder.append(pikachuExpectedEndResult);
		assertEquals(builder.toString(), treasureMap.buildMapStatusResult());
	}
}
