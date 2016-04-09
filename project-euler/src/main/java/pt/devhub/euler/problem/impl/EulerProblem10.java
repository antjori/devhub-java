package pt.devhub.euler.problem.impl;

import pt.devhub.euler.problem.EulerProblemSolverUtil;
import pt.devhub.euler.problem.IEulerProblem;

/**
 * Summation of primes Problem 10 The sum of the primes below 10 is 2 + 3 + 5 +
 * 7 = 17.
 * 
 * Find the sum of all the primes below two million.
 */
public class EulerProblem10 implements IEulerProblem {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void solveProblem() {
		long result = 0;

		for (long number = 2; number < 2000000; number++) {
			if (EulerProblemSolverUtil.isPrime(number)) {
				result += number;
			}
		}

		EulerProblemSolverUtil.printSolution(getClass(), result);
	}

}
