package pt.devhub.euler.problem.impl;

import pt.devhub.euler.problem.EulerProblemSolverUtil;
import pt.devhub.euler.problem.IEulerProblem;

/**
 * 10001st prime Problem 7 By listing the first six prime numbers: 2, 3, 5, 7,
 * 11, and 13, we can see that the 6th prime is 13.
 * 
 * What is the 10 001st prime number?
 */
public class EulerProblem7 implements IEulerProblem {

	@Override
	public void solveProblem() {
		int nPrimes = 1;
		int number = 1;

		while (nPrimes < 10001) {
			number += 2;
			if (EulerProblemSolverUtil.isPrime(number)) {
				nPrimes++;
			}
		}

		EulerProblemSolverUtil.printSolution(getClass(), number);
	}
}
