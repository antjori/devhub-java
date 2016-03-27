package pt.devhub.euler.problem;

/**
 * The utility class for the Euler problem solver.
 */
public final class EulerProblemSolverUtil {

	/**
	 * Prints the solution of a Euler problem.
	 * 
	 * @param eulerProblemClass
	 *            the Euler problem
	 * @param solution
	 *            the solution to print
	 */
	public static void printSolution(final Class<? extends IEulerProblem> eulerProblemClass, final Number solution) {
		System.out.println(eulerProblemClass.getSimpleName() + " solution: " + solution);
	}

	/**
	 * Validates if a number is a prime number.
	 * 
	 * @param number
	 *            the number to validate
	 * @return true if the number is a prime number; false otherwise
	 */
	public static final boolean isPrime(long number) {
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
