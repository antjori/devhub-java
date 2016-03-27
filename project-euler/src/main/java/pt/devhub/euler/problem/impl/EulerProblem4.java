package pt.devhub.euler.problem.impl;

import pt.devhub.euler.problem.EulerProblemSolverUtil;
import pt.devhub.euler.problem.IEulerProblem;

/**
 * Largest palindrome product Problem 4 A palindromic number reads the same both
 * ways. The largest palindrome made from the product of two 2-digit numbers is
 * 9009 = 91 × 99.
 * 
 * Find the largest palindrome made from the product of two 3-digit numbers.
 */
public class EulerProblem4 implements IEulerProblem {

	@Override
	public void solveProblem() {
		int start = 100;
		int stop = 999;
		int result = 0;

		while (start <= stop) {
			int target = start;
			for (int it = start; it <= stop; it++) {
				int product = target * it;
				if (isPalindrome(product) && (product > result)) {
					result = product;
				}
			}

			start++;
		}

		EulerProblemSolverUtil.printSolution(EulerProblem4.class, result);
	}

	private boolean isPalindrome(long product) {
		String prdStr = String.valueOf(product);
		boolean result = true;

		for (int startIdx = 0, stopIdx = prdStr.length() - 1; startIdx <= stopIdx; startIdx++, stopIdx--) {
			if (prdStr.charAt(startIdx) != prdStr.charAt(stopIdx)) {
				result = false;
				break;
			}
		}

		return result;
	}
}
