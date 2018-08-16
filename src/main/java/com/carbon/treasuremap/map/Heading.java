package com.carbon.treasuremap.map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum Heading {
	N(0), E(90), S(180), W(270);

	private static final Logger logger = LoggerFactory.getLogger(Heading.class);
	private final int angle;

	private Heading(int angle) {
		this.angle = angle;
	}

	public Heading getRightHeading() {
		return getHeadingByAngle((angle + 90) % 360);

	}

	public Heading getLeftHeading() {
		return getHeadingByAngle((angle - 90 + 360) % 360);
	}

	private Heading getHeadingByAngle(int angle) {
		if (N.angle == angle) {
			return N;
		}

		if (E.angle == angle) {
			return E;
		}

		if (S.angle == angle) {
			return S;
		}

		if (W.angle == angle) {
			return W;
		}

		// Impossible...
		logger.error("Bad heading " + angle);
		return null;
	}

	public static Heading getHeading(String headingString) {
		switch (headingString) {
		case "N":
			return N;
		case "S":
			return S;
		case "E":
			return E;
		case "W":
			return W;
		default:
			// It is tested with regex before
			logger.error("Bad heading " + headingString);
			return null;
		}
	}

	@Override
	public String toString() {
		return this.name().toUpperCase();
	}
}
