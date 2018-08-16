package com.carbon.treasuremap.file;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.carbon.treasuremap.exceptions.TreasureMapAddElementException;
import com.carbon.treasuremap.exceptions.TreasureMapFileParseException;
import com.carbon.treasuremap.map.TreasureMap;

/**
 * Parse all lines from input file.
 * 
 * <pre>
 * Verify lines and generate exception when the format is not respected.
 * C - x - y
 * M - x - y
 * T - x - y - value
 * A - name - x - y - heading - movements
 * 
 * <u>Postulates</u> : 
 *	- If an adventurer is added on treasure, the adventurer does not get treasure
 *	- A mountain cannot be added on treasure or another mountain
 *	- A treasure cannot be added on mountain or another treasure 
 *	- A adventurer cannot be added on mountain or another adventurer 
 *	- If a treasure case has no more treasure the case stay a treasure case with value 0
 *	- On result if an adventurer is on treasure case, don't add this treasure in file (see in example exercise) 
 *	- Map dimensions cannot equals to 0
 * </pre>
 * 
 * @author Florian
 *
 */
public class FileLinesParser {
	public final static String FILE_IS_EMPTY = "File is empty";
	public final static String UNEXPECTED_LINE_IN_FILE = "Can't this parse line in file";

	// Map line parameters
	public final static String MAP_LINE_FORMAT_ERROR = "Bad format or missing map line in file (C - x - y)";
	public final static String MAP_DIMENSION_ZERO = "Map line contains dimension with value 0";
	private final static String regexMap = "^C - ([0-9]{1,}) - ([0-9]{1,})$";

	// Mountain line parameters
	private final static String regexMountain = "^M - ([0-9]{1,}) - ([0-9]{1,})$";

	// Treasure line parameters
	private final static String regexTreasure = "^T - ([0-9]{1,}) - ([0-9]{1,}) - ([0-9]{1,})$";

	// Adventurer line parameters
	private final static String regexAdventurer = "^A - ([a-zA-Z]{1,}) - ([0-9]{1,}) - ([0-9]{1,}) - ([NSEW]) - ([AGD]{0,})$";

	private TreasureMap treasureMap = null;

	public TreasureMap parseFile(List<String> lines)
			throws TreasureMapFileParseException, TreasureMapAddElementException {
		lines.removeIf(f -> f.startsWith("#"));

		// Test if file is empty
		if (lines.size() == 0) {
			throw new TreasureMapFileParseException(FILE_IS_EMPTY);
		}

		Pattern compileMap = Pattern.compile(regexMap);
		List<String> collectMap = lines.stream().filter(l -> compileMap.matcher(l).find()).collect(Collectors.toList());
		if (collectMap.size() != 1) {
			throw new TreasureMapFileParseException(
					MAP_LINE_FORMAT_ERROR + " : bad number of map line expected 1 but find " + collectMap.size());
		}

		// Test and create map
		treasureMap = parseMapLine(collectMap.get(0));
		lines.removeIf(f -> compileMap.matcher(f).find());

		// Add all elements
		for (String line : lines) {
			Matcher mountainMatcher = Pattern.compile(regexMountain).matcher(line);
			Matcher treasureMatcher = Pattern.compile(regexTreasure).matcher(line);
			Matcher adventurerMatcher = Pattern.compile(regexAdventurer).matcher(line);

			if (mountainMatcher.find()) {
				treasureMap.addMountainInMap(Integer.parseInt(mountainMatcher.group(1)),
						Integer.parseInt(mountainMatcher.group(2)));
			} else if (treasureMatcher.find()) {
				treasureMap.addTreasureInMap(Integer.parseInt(treasureMatcher.group(1)),
						Integer.parseInt(treasureMatcher.group(2)), Integer.parseInt(treasureMatcher.group(3)));
			} else if (adventurerMatcher.find()) {
				treasureMap.addAdventurerInMap(adventurerMatcher.group(1), Integer.parseInt(adventurerMatcher.group(2)),
						Integer.parseInt(adventurerMatcher.group(3)), adventurerMatcher.group(4),
						adventurerMatcher.group(5));
			} else {
				throw new TreasureMapFileParseException(UNEXPECTED_LINE_IN_FILE + " : " + line);
			}
		}

		return treasureMap;
	}

	private TreasureMap parseMapLine(String mapLine) throws TreasureMapFileParseException {
		// Search for Map line
		Pattern compile = Pattern.compile(regexMap);
		Matcher matcher = compile.matcher(mapLine);
		matcher.find();

		// Verify dimension (must be > 0)
		int width = Integer.parseInt(matcher.group(1));
		int height = Integer.parseInt(matcher.group(2));

		if (width <= 0 || height <= 0) {
			throw new TreasureMapFileParseException(MAP_DIMENSION_ZERO + " : " + mapLine);
		}

		return new TreasureMap(width, height);
	}
}
