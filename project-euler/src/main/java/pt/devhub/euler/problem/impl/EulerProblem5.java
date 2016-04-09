package pt.devhub.euler.problem.impl;

import pt.devhub.euler.problem.EulerProblemSolverUtil;
import pt.devhub.euler.problem.IEulerProblem;

/**
 * Smallest multiple Problem 5 2520 is the smallest number that can be divided
 * by each of the numbers from 1 to 10 without any remainder.
 * 
 * What is the smallest positive number that is evenly divisible by all of the
 * numbers from 1 to 20?
 */
public class EulerProblem5 implements IEulerProblem {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void solveProblem() {
		boolean found = false;
		long number = 0;

		while (!found) {
			found = true;
			number+=2;

			for (int it = 1; it <= 20; it++) {
				if (number % it != 0) {
					found = false;
					break;
				}
			}
		}

		EulerProblemSolverUtil.printSolution(getClass(), number);
	}

}
