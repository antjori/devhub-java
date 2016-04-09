package pt.devhub.euler.problem;

import java.util.ArrayList;
import java.util.List;

/**
 * The Euler problem solver.
 */
public class EulerProblemSolver {

	private static List<IEulerProblem> problems = new ArrayList<>();

	static {
		try {
			for (int problemNum = 1; problemNum <= 11; problemNum++) {
				problems.add((IEulerProblem) Class.forName("pt.devhub.euler.problem.impl.EulerProblem" + problemNum)
						.newInstance());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		problems.forEach(problem -> problem.solveProblem());
	}
}
