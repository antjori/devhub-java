package pt.devhub.euler.problem.impl;

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
	public void solveProbem() {
		long sqrtTarget = (long) Math.sqrt(target);

		for (long number = 1; number <= sqrtTarget; number = number + 2) {

			if (isPrime(number)) {
				//primes.add(number);

				if (target % number == 0) {
					result = number;
				}
			}
		}

		//primes.forEach(prime -> System.out.println(prime));
		System.out.println(result);
	}

	private boolean isPrime(long number) {
		if (number < 1) {
			return false;
		}

		if (number <= 3) {
			return true;
		}

		if ((number % 2 == 0) || (number % 3 == 0)) {
			return false;
		}

		int it = 5;

		while (it * it <= number) {
			if ((number % it == 0) || ((number % (it + 2) == 0))) {
				return false;
			}

			it += 6;
		}

		return true;
	}

}
