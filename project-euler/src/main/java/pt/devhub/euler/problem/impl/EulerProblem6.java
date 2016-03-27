package pt.devhub.euler.problem.impl;

import pt.devhub.euler.problem.EulerProblemSolverUtil;
import pt.devhub.euler.problem.IEulerProblem;

/**
 * Sum square difference Problem 6 The sum of the squares of the first ten
 * natural numbers is,
 * 
 * 1^2 + 2^2 + ... + 10^2 = 385 The square of the sum of the first ten natural
 * numbers is,
 * 
 * (1 + 2 + ... + 10)^2 = 55^2 = 3025 Hence the difference between the sum of
 * the squares of the first ten natural numbers and the square of the sum is
 * 3025 − 385 = 2640.
 * 
 * Find the difference between the sum of the squares of the first one hundred
 * natural numbers and the square of the sum.
 */
public class EulerProblem6 implements IEulerProblem {

	@Override
	public void solveProblem() {
		int stop = 100;
		int sumOfSquares = 0;
		int sum = 0;

		for (int start = 1; start <= stop; start++) {
			sumOfSquares += start * start;
			sum += start;
		}

		EulerProblemSolverUtil.printSolution(EulerProblem6.class, (sum * sum) - sumOfSquares);
	}

}
