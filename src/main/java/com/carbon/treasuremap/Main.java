package com.carbon.treasuremap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carbon.treasuremap.exceptions.TreasureMapAddElementException;
import com.carbon.treasuremap.exceptions.TreasureMapFileParseException;
import com.carbon.treasuremap.file.FileLinesParser;
import com.carbon.treasuremap.map.TreasureMap;

public class Main {
	private final static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		// We are waiting input file and output file
		if (args.length != 2) {
			logger.error("Missing arguments, expected arguments -> [input file] [output file]");
			System.exit(0);
		}

		String inputFile = args[0];
		String outputFile = args[1];

		// Read input file in arg[0]
		List<String> lines = readFile(inputFile);

		// Parse file
		TreasureMap treasureMap = parseFile(lines);

		// Adventurers movements
		String result = executeAdventurersMovements(treasureMap);

		// write result in file
		writeReasultInOutputFile(outputFile, result);
	}

	/**
	 * Read input file.
	 * 
	 * @param filePath inputFile
	 * @return all lines readed
	 */
	private static List<String> readFile(String inputFile) {
		Path inputFilePath = Paths.get(inputFile);
		List<String> readAllLines = null;
		try {
			readAllLines = Files.readAllLines(inputFilePath);
			if (logger.isInfoEnabled()) {
				StringBuilder builder = new StringBuilder(System.lineSeparator());
				readAllLines.forEach(l -> builder.append(l + System.lineSeparator()));
				logger.info("Read file data" + builder.toString());
			}
		} catch (IOException e) {
			logger.error("Error when try to read inputFilePath : " + inputFilePath.toString(), e);
			System.exit(0);
		}
		return readAllLines;
	}

	/**
	 * <pre>
	 * 1 Parse all lines. 
	 * 2 Create map. 
	 * 3 Add mountains, treasures and adventurers.
	 * 4 Execute movements.
	 * 5 Get result.
	 * </pre>
	 * 
	 * @param lines from inputFile
	 * @return treasure map after adventurers movements.
	 */
	private static TreasureMap parseFile(List<String> lines) {
		FileLinesParser parser = new FileLinesParser();
		TreasureMap treasureMap = null;

		try {
			treasureMap = parser.parseFile(lines);
		} catch (TreasureMapFileParseException e) {
			logger.error("Error on parsing files, see exception message", e);
			System.exit(0);
		} catch (TreasureMapAddElementException e) {
			logger.error("Error in datas, see exception message", e);
			System.exit(0);
		}

		return treasureMap;
	}

	private static String executeAdventurersMovements(TreasureMap treasureMap) {
		try {
			treasureMap.moveAdventurers();
		} catch (TreasureMapAddElementException e) {
			logger.error("Error in datas, see exception message", e);
		}

		return treasureMap.buildMapStatusResult();
	}

	/**
	 * Write result in output file.
	 * 
	 * @param outputFile
	 * @param result
	 */
	private static void writeReasultInOutputFile(String outputFile, String result) {
		if (logger.isInfoEnabled()) {
			logger.info("Result write in file" + System.lineSeparator() + result);
		}

		try {
			Path path = Paths.get(outputFile);

			if (Files.exists(path)) {
				Files.delete(path);
			}

			Files.createFile(path);
			Files.write(path, result.getBytes());
		} catch (IOException e) {
			logger.error("Error when try to write result in file " + outputFile, e);
		}
	}
}
