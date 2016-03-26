package pt.devhub.euler.problem;

import java.util.ArrayList;
import java.util.List;

import pt.devhub.euler.problem.impl.EulerProblem3;

/**
 * The Euler problem solver.
 */
public class EulerProblemSolver {

	private static List<IEulerProblem> problems = new ArrayList<>();

	static {
		problems.add(new EulerProblem3());
	}

	public static void main(String[] args) {
		problems.forEach(problem -> problem.solveProbem());
	}
}
