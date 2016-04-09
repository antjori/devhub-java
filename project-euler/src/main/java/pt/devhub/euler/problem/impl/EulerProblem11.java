package pt.devhub.euler.problem.impl;

import java.util.Map;

import pt.devhub.euler.problem.EulerProblemSolverUtil;
import pt.devhub.euler.problem.IEulerProblem;

/**
 * In the 20×20 grid below, four numbers along a diagonal line have been marked
 * in red. The product of these numbers is 26 × 63 × 78 × 14 = 1788696.
 * 
 * What is the greatest product of four adjacent numbers in the same direction
 * (up, down, left, right, or diagonally) in the 20×20 grid?
 */
public class EulerProblem11 implements IEulerProblem {

	private int[][] grid = new int[20][20];

	static {
		Map<Object, Object> digits = EulerProblemSolverUtil.loadProperties(EulerProblem11.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void solveProblem() {
		// TODO Auto-generated method stub

	}

}
