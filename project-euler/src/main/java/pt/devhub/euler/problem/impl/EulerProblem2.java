package pt.devhub.euler.problem.impl;

import java.util.ArrayList;
import java.util.List;

import pt.devhub.euler.problem.EulerProblemSolverUtil;
import pt.devhub.euler.problem.IEulerProblem;

/**
 * Even Fibonacci numbers Problem 2 Each new term in the Fibonacci sequence is
 * generated by adding the previous two terms. By starting with 1 and 2, the
 * first 10 terms will be:
 * 
 * 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, ...
 * 
 * By considering the terms in the Fibonacci sequence whose values do not exceed
 * four million, find the sum of the even-valued terms.
 */
public class EulerProblem2 implements IEulerProblem {

	@Override
	public void solveProblem() {
		int prev = 1;
		int current = 1;
		int result = 0;
		int even = 0;
		List<Integer> terms = new ArrayList<>();

		while (prev + current < 4000000) {
			result = prev + current;
			terms.add(result);
			if (result % 2 == 0) {
				even += result;
			}
			prev = current;
			current = result;
		}

//		terms.forEach(term -> System.out.println(term));
		EulerProblemSolverUtil.printSolution(this.getClass(), even);
	}

}
