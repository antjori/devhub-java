package pt.devhub.euler.problem.impl;

import java.util.Map;

import pt.devhub.euler.problem.EulerProblemSolverUtil;
import pt.devhub.euler.problem.IEulerProblem;

/**
 * In the 20×20 grid below, four numbers along a diagonal line have been marked
 * in red. The product of these numbers is 26 × 63 × 78 × 14 = 1788696.
 * 
 * What is the greatest product of four adjacent numbers in the same direction
 * (up, down, left, right, or diagonally) in the 20×20 grid?
 */
public class EulerProblem11 implements IEulerProblem {

	private static short[][] grid = new short[20][20];

	static {
		Map<Object, Object> digits = EulerProblemSolverUtil.loadProperties(EulerProblem11.class);

		int line = 0;

		for (Object key : digits.keySet()) {
			String[] digitsInLine = ((String) digits.get(key)).split("\\s+");

			int column = 0;

			for (String digit : digitsInLine) {
				grid[line][column] = Short.parseShort(digit);
				column++;
			}
			line++;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void solveProblem() {
		int result = 0;

		for (short line = 0; line < grid.length; line++) {
			for (short col = 0; col < grid[line].length; col++) {
				// searches right
				int rightResult = (col + 3 < grid[line].length) ? calculateRight(line, col) : -1;

				// searches down
				int downResult = (line + 3 < grid.length) ? calculateDown(line, col) : -1;

				// searches diagonally
				int diagonalResult = ((col + 3 < grid[line].length) && (line + 3 < grid.length))
						? calculateDiagonal(line, col) : -1;

				int backDiagonalResult = ((col - 3 > 0) && (line + 3 < grid.length)) ? calculateBackDiagonal(line, col)
						: -1;

				int possibleResult = 0;

				if ((rightResult > downResult) && (rightResult > diagonalResult)
						&& (rightResult > backDiagonalResult)) {
					possibleResult = rightResult;
				} else if ((downResult > diagonalResult) && (downResult > backDiagonalResult)) {
					possibleResult = downResult;
				} else if (diagonalResult > backDiagonalResult) {
					possibleResult = diagonalResult;
				} else {
					possibleResult = backDiagonalResult;
				}

				if (possibleResult > result) {
					result = possibleResult;
				}
			}
		}

		EulerProblemSolverUtil.printSolution(getClass(), result);
	}

	private int calculateRight(final short line, final short col) {
		return getValue(line, col) * getValue(line, (short) (col + 1)) * getValue(line, (short) (col + 2))
				* getValue(line, (short) (col + 3));
	}

	private int calculateDown(final short line, final short col) {
		return getValue(line, col) * getValue((short) (line + 1), col) * getValue((short) (line + 2), col)
				* getValue((short) (line + 3), col);
	}

	private int calculateDiagonal(final short line, final short col) {
		return getValue(line, col) * getValue((short) (line + 1), (short) (col + 1))
				* getValue((short) (line + 2), (short) (col + 2)) * getValue((short) (line + 3), (short) (col + 3));
	}

	private int calculateBackDiagonal(final short line, final short col) {
		return getValue(line, col) * getValue((short) (line + 1), (short) (col - 1))
				* getValue((short) (line + 2), (short) (col - 2)) * getValue((short) (line + 3), (short) (col - 3));
	}

	private short getValue(final short line, final short col) {
		return grid[line][col];
	}
}
