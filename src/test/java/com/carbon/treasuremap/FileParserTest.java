package com.carbon.treasuremap;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.carbon.treasuremap.exceptions.TreasureMapAddElementException;
import com.carbon.treasuremap.exceptions.TreasureMapFileParseException;
import com.carbon.treasuremap.file.FileLinesParser;

public class FileParserTest {

	@Test
	public void testErrorWhenFileIsEmpty() {
		// Expect an Exception on parsing because the file is empty
		String expectedError = FileLinesParser.FILE_IS_EMPTY;
		testErrorParsing(new ArrayList<>(), expectedError);
	}

	@Test
	public void testErrorWhenFileMissMapLine() {
		// Expect an Exception on parsing because the file does not contains map line
		String expectedError = FileLinesParser.MAP_LINE_FORMAT_ERROR;
		testErrorParsing(Arrays.asList("M - 1 - 0"), expectedError);
	}

	@Test
	public void testErrorWhenAddTwoMap() {
		// Expect an Exception on parsing because the file does not contains map line
		String expectedError = FileLinesParser.MAP_LINE_FORMAT_ERROR;
		testErrorParsing(Arrays.asList("C - 5 - 5", "C - 5 - 10"), expectedError);
	}

	@Test
	public void testErrorWhenFileMapLineDimensionIsNotANumber() {
		// Expect an Exception on parsing because a map line dimension is not a number
		String expectedError = FileLinesParser.MAP_LINE_FORMAT_ERROR;
		testErrorParsing(Arrays.asList("C - 4 - test"), expectedError);
	}

	@Test
	public void testErrorWhenFileMapLineContainsZero() {
		// Expect an exception on parsing because one of the dimension of the map is
		// equals to 0
		String expectedError = FileLinesParser.MAP_DIMENSION_ZERO;
		testErrorParsing(Arrays.asList("C - 0 - 1"), expectedError);
	}

	@Test
	public void testErrorWhenBadLineIsInFile() {
		// Expect an exception on parsing because the line test cannot be parsed
		String badLine = "test";
		String expectedError = FileLinesParser.UNEXPECTED_LINE_IN_FILE + " : " + badLine;

		List<String> input = new ArrayList<>();
		input.add("C - 5 - 2");
		input.add(badLine);

		testErrorParsing(input, expectedError);
	}

	@Test
	public void testErrorBadFormatOnMontains() {
		// Expect an exception with the bad line in the message because, it does not
		// respect the montains format
		String badLine = "M - test - 1";
		String expectedError = FileLinesParser.UNEXPECTED_LINE_IN_FILE + " : " + badLine;

		List<String> input = new ArrayList<>();
		input.add("C - 5 - 2");
		input.add("M - 1 - 1");
		input.add(badLine);

		testErrorParsing(input, expectedError);
	}

	@Test
	public void testErrorBadFormatOnTreasure() {
		// Expect an exception with the bad line in the message because, it does not
		// respect the treasure format
		String badLine = "T - 2 - 1 - -1";
		String expectedError = FileLinesParser.UNEXPECTED_LINE_IN_FILE + " : " + badLine;

		List<String> input = new ArrayList<>();
		input.add("C - 5 - 2");
		input.add("M - 0 - 1");
		input.add("T - 1 - 1 - 1");
		input.add(badLine);

		testErrorParsing(input, expectedError);
	}

	@Test
	public void testErrorBadFormatOnAdventurer() {
		// Expect an exception with the bad line in the message because, it does not
		// respect the adventurer format
		String badLine = "A - Mike - 1 - 2 - 1 - AAGDD";
		String expectedError = FileLinesParser.UNEXPECTED_LINE_IN_FILE + " : " + badLine;

		List<String> input = new ArrayList<>();
		input.add("C - 5 - 2");
		input.add("M - 0 - 1");
		input.add("T - 1 - 1 - 1");
		input.add(badLine);

		testErrorParsing(input, expectedError);
	}

	@Test
	public void testIgnoreCommentLines() {
		// Expect no exception and ignore all line starting by #
		FileLinesParser parser = new FileLinesParser();
		List<String> input = new ArrayList<>();
		input.add("# Commented line");
		input.add("C - 5 - 2");
		input.add("A - Arthur - 1 - 1 - N - GDGDGD");
		try {
			parser.parseFile(input);
		} catch (TreasureMapFileParseException | TreasureMapAddElementException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testAddElementsInDifferentOrder() {
		List<String> input = new ArrayList<>();
		input.add("A - Mike - 1 - 2 - N - A");
		input.add("T - 1 - 1 - 1");
		input.add("C - 5 - 5");
		input.add("M - 0 - 1");

		FileLinesParser parser = new FileLinesParser();
		try {
			parser.parseFile(input);
		} catch (TreasureMapFileParseException | TreasureMapAddElementException e) {
			fail(e.getMessage());
		}
	}

	private void testErrorParsing(List<String> input, String expectedError) {
		FileLinesParser parser = new FileLinesParser();
		try {
			parser.parseFile(input);
			fail();
		} catch (TreasureMapFileParseException e) {
			assertTrue(e.getMessage().contains(expectedError));
		} catch (TreasureMapAddElementException e) {
			fail(e.getMessage());
		}
	}
}
