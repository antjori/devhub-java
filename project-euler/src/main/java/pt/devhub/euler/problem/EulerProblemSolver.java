package pt.devhub.euler.problem;

import java.util.ArrayList;
import java.util.List;

import pt.devhub.euler.problem.impl.EulerProblem3;
import pt.devhub.euler.problem.impl.EulerProblem4;
import pt.devhub.euler.problem.impl.EulerProblem5;

/**
 * The Euler problem solver.
 */
public class EulerProblemSolver {

	private static List<IEulerProblem> problems = new ArrayList<>();

	static {
		problems.add(new EulerProblem3());
		problems.add(new EulerProblem4());
		problems.add(new EulerProblem5());
	}

	public static void main(String[] args) {
		problems.forEach(problem -> problem.solveProbem());
	}
}
