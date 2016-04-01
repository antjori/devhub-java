package pt.devhub.euler.problem.impl;

import pt.devhub.euler.problem.EulerProblemSolverUtil;
import pt.devhub.euler.problem.IEulerProblem;

/**
 * A Pythagorean triplet is a set of three natural numbers, a < b < c, for
 * which,
 * 
 * a^2 + b^2 = c^2 For example, 3^2 + 4^2 = 9 + 16 = 25 = 5^2.
 * 
 * There exists exactly one Pythagorean triplet for which a + b + c = 1000. Find
 * the product abc.
 */
public class EulerProblem9 implements IEulerProblem {

	@Override
	public void solveProblem() {
		boolean found = false;
		short a = 1;
		short b = 2;
		short c = 997;

		while (!found) {
			while (a < b && b < c) {

				if (isPythagoreanTriplet(a, b, c)) {
					found = true;
					break;
				}
				c--;
				b++;
			}

			if (found) {
				break;
			}

			a++;
			b = (short) (a + 1);
			c = (short) (1000 - a - b);
		}

		EulerProblemSolverUtil.printSolution(this.getClass(), (a*b*c));
	}

	private boolean isPythagoreanTriplet(short a, short b, short c) {
		return (((a * a) + (b * b)) == (c * c));
	}
}
