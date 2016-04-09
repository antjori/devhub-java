package pt.devhub.euler.problem;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

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

	/**
	 * Loads a properties file.
	 * 
	 * @param clazz
	 *            the current executing class
	 * @return a Map with the contents of the loaded properties file
	 */
	public static final Map<Object, Object> loadProperties(Class<? extends IEulerProblem> clazz) {
		InputStream inputStream = clazz.getClassLoader()
				.getResourceAsStream(clazz.getSimpleName().toLowerCase() + ".properties");
		Properties properties = new Properties();

		try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new TreeMap<>(properties);
	}

	/**
	 * Loads a properties file and concatenates its contents into a string.
	 * 
	 * @param clazz
	 *            the current executing class
	 * @return a String representative of the contents of the loaded properties
	 *         file
	 */
	public static final String loadPropertiesAsString(Class<? extends IEulerProblem> clazz) {
		Map<Object, Object> digitsMap = loadProperties(clazz);
		StringBuilder builder = new StringBuilder();

		digitsMap.keySet().forEach(key -> builder.append(digitsMap.get((String) key)));

		return builder.toString();
	}
}
