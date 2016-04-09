package pt.devhub.euler.problem.impl;

import pt.devhub.euler.problem.EulerProblemSolverUtil;
import pt.devhub.euler.problem.IEulerProblem;

/**
 * Largest product in a series Problem 8 The four adjacent digits in the
 * 1000-digit number that have the greatest product are 9 × 9 × 8 × 9 = 5832.
 * 
 * Find the thirteen adjacent digits in the 1000-digit number that have the
 * greatest product. What is the value of this product?
 */
public class EulerProblem8 implements IEulerProblem {

	private static String DIGITS;

	static {
		DIGITS = EulerProblemSolverUtil.loadPropertiesAsString(EulerProblem8.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void solveProblem() {
		long result = 0;

		for (int start = 0, end = DIGITS.length() - 1; (start + 13) < (end - 13); start++, end--) {
			long startProduct = getProduct(DIGITS.substring(start, start + 13));
			long endProduct = getProduct(DIGITS.substring(end - 13, end));
			long possibleResult = startProduct > endProduct ? startProduct : endProduct;

			if (possibleResult > result) {
				result = possibleResult;
			}
		}

		EulerProblemSolverUtil.printSolution(getClass(), result);
	}

	private long getProduct(String number) {
		long result = 1;

		for (int it = 0; it < number.length(); it++) {
			result *= Long.valueOf(String.valueOf(number.charAt(it)));
		}

		return result;
	}
}
