package pt.devhub.euler.problem.impl;

import pt.devhub.euler.problem.EulerProblemSolverUtil;
import pt.devhub.euler.problem.IEulerProblem;

/**
 * Largest prime factor Problem 3 The prime factors of 13195 are 5, 7, 13 and
 * 29.
 * 
 * What is the largest prime factor of the number 600851475143 ?
 */
public class EulerProblem3 implements IEulerProblem {

	//private List<Long> primes = new ArrayList<>();

	private long target = 600851475143L;

	private long result = 0;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void solveProblem() {
		long sqrtTarget = (long) Math.sqrt(target);

		for (long number = 1; number <= sqrtTarget; number = number + 2) {

			if (EulerProblemSolverUtil.isPrime(number)) {
				//primes.add(number);

				if (target % number == 0) {
					result = number;
				}
			}
		}

		//primes.forEach(prime -> System.out.println(prime));
		EulerProblemSolverUtil.printSolution(getClass(), result);
	}

}
