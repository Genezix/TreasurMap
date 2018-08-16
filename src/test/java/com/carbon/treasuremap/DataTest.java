package com.carbon.treasuremap;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.carbon.treasuremap.exceptions.TreasureMapAddElementException;
import com.carbon.treasuremap.exceptions.TreasureMapFileParseException;
import com.carbon.treasuremap.file.FileLinesParser;
import com.carbon.treasuremap.map.TreasureMap;

public class DataTest {

	@Test
	public void testErrorWhenAddMountainOutsideTheMap() {
		// Expect an exception because we add mountain outside the map
		String expectedError = TreasureMap.ERROR_ADD_ELEMENT_OUTSIDE_THE_MAP;

		List<String> input = new ArrayList<>();
		input.add("C - 5 - 2");
		input.add("M - 5 - 1");

		testErrorAddElement(input, expectedError);

		input.clear();
		input.add("C - 5 - 2");
		input.add("M - 4 - 5");

		testErrorAddElement(input, expectedError);
	}

	@Test
	public void testErrorWhenAddTreasoreOutsideTheMap() {
		// Expect an exception because we add treasure outside the map
		String expectedError = TreasureMap.ERROR_ADD_ELEMENT_OUTSIDE_THE_MAP;

		List<String> input = new ArrayList<>();
		input.add("C - 5 - 2");
		input.add("T - 5 - 1 - 2");

		testErrorAddElement(input, expectedError);

		input.clear();
		input.add("C - 5 - 2");
		input.add("T - 4 - 5 - 2");

		testErrorAddElement(input, expectedError);
	}

	@Test
	public void testErrorWhenAddAdventurerOutsideTheMap() {
		// Expect an exception because we add adventurer outside the map
		String expectedError = TreasureMap.ERROR_ADD_ELEMENT_OUTSIDE_THE_MAP;

		List<String> input = new ArrayList<>();
		input.add("C - 5 - 2");
		input.add("A - Jean - 5 - 1 - S - DG");

		testErrorAddElement(input, expectedError);

		input.clear();
		input.add("C - 5 - 2");
		input.add("A - Jean - 4 - 5 - S - DG");

		testErrorAddElement(input, expectedError);
	}

	@Test
	public void testErrorWhenAddTreasureOnMontain() {
		// Expect an exception because we add treasure at the same position of mountain
		String expectedError = TreasureMap.ERROR_ADD_ELEMENT_ON_ANOTHER_ELEMENT;

		List<String> input = new ArrayList<>();
		input.add("C - 5 - 2");
		input.add("M - 4 - 1");
		input.add("T - 4 - 1 - 2");

		testErrorAddElement(input, expectedError);
	}

	@Test
	public void testErrorWhenAddMountainOnTreasure() {
		// Expect an exception because we add mountain at the same position of treasure
		String expectedError = TreasureMap.ERROR_ADD_ELEMENT_ON_ANOTHER_ELEMENT;

		List<String> input = new ArrayList<>();
		input.add("C - 5 - 2");
		input.add("T - 4 - 1 - 2");
		input.add("M - 4 - 1");

		testErrorAddElement(input, expectedError);
	}

	@Test
	public void testErrorWhenAddAdventurerOnMountain() {
		// Expect an exception because we add mountain at the same position of treasure
		String expectedError = TreasureMap.ERROR_ADD_ADVENTURER_ON_ANOTHER_ELEMENT;

		List<String> input = new ArrayList<>();
		input.add("C - 5 - 2");
		input.add("T - 4 - 1 - 2");
		input.add("M - 4 - 0");
		input.add("A - Bear - 4 - 0 - S - ADG");

		testErrorAddElement(input, expectedError);
	}

	@Test
	public void testErrorWhenAddAdventurerOnAdventurer() {
		// Expect an exception because we add mountain at the same position of treasure
		String expectedError = TreasureMap.ERROR_ADD_ADVENTURER_ON_ANOTHER_ELEMENT;

		List<String> input = new ArrayList<>();
		input.add("C - 5 - 3");
		input.add("T - 4 - 1 - 2");
		input.add("M - 4 - 2");
		input.add("A - Bear - 4 - 0 - N - ADG");
		input.add("A - Lara - 4 - 0 - S - ADG");

		testErrorAddElement(input, expectedError);
	}

	@Test
	public void testErrorNoAdventurer() {
		// Expect an exception because we did not add adventurer
		String expectedError = TreasureMap.ERROR_NO_ADVENTURER;

		List<String> input = new ArrayList<>();
		input.add("C - 5 - 2");
		input.add("T - 4 - 1 - 2");
		input.add("M - 4 - 0");

		testErrorAddElement(input, expectedError);
	}

	private void testErrorAddElement(List<String> input, String expectedError) {
		FileLinesParser parser = new FileLinesParser();
		try {
			parser.parseFile(input).moveAdventurers();
			fail();
		} catch (TreasureMapAddElementException e) {
			assertTrue(e.getMessage().contains(expectedError));
		} catch (TreasureMapFileParseException e) {
			fail(e.getMessage());
		}
	}
}
