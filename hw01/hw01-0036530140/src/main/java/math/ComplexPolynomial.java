package math;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** This class represents a single polynomial formed with complex numbers. It supports the following operations: 
 * 	first derivation, multiplying with another polynomial and computation of polynomial value in at a specified
 * 	point.
 * 
 * 	@author adrian
 */
public class ComplexPolynomial {
	/** List used for storing factors of this polynomial. List starts from z0, z1, z2 ... and ends with zn. */
	private List<Complex> allFactors;
	
	/** Constructor which initalizes the polynomial with provided factors in form of complex numbers.	
	 * 
	 * 	@param desired number of Complex factors
	 */
	public ComplexPolynomial(Complex ...factors) {
		this.allFactors = new ArrayList<>();
		if(factors.length == 0) {
			allFactors.add(Complex.ZERO);
			return;
		}
		for(Complex single: factors) {
			allFactors.add(single);
		}
	}
	/** Returns order of this polynom; eg. For (7+2i)z^3+2z^2+5z+1 returns 3
	 * 
	 * 	@return short value representing order
	 */
	public short order() {
		return (short) (this.allFactors.size() - 1);
	}
	
	/** Computes a new polynomial by multiplying this polynomial with provided polynomial.
	 * 
	 * 	@param ComplexPolynomial used as multiplier
	 * 	@return ComplexPolynomial as result of multiplication 
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		int thisOrder = (int) order();
		int thatOrder = (int) p.order();
		Complex[] factors = new Complex[thisOrder + thatOrder + 1];
		for(int i = 0; i < thisOrder + thatOrder + 1; i++) {
			factors[i] = Complex.ZERO;
		}
		List<Complex> thisFactors = getAllFactors();
		List<Complex> thatFactors = p.getAllFactors();
		for(int i = 0; i < thisOrder + 1; i++) {
			for(int j = 0; j < thatOrder + 1; j++) {
				factors[i + j] = factors[i + j].add(
						thisFactors.get(i).multiply(thatFactors.get(j))
				);
			}
		}
		return new ComplexPolynomial(factors);
	}
	
	/** Computes first derivative of this polynomial; for example, for (7+2i)z^3+2z^2+5z+1 returns (21+6i)z^2+4z+5
	 * 
	 * 	@return ComplexPolyonomial as result
	 */
	public ComplexPolynomial derive() {
		Complex[] factors = new Complex[this.order()];
		for(int i = 1; i < this.order() + 1; i++) {
			factors[i - 1] = this.allFactors.get(i).multiply(new Complex(i, 0));
		}
		return new ComplexPolynomial(factors);
	}
	
	/** Computes polynomial value at given point z.
	 * 
	 * @param Complex as the given point
	 * @return Complex as a result 
	 */
	public Complex apply(Complex z) {
		Complex result = null;
		int count = 0;
		for(Complex single: this.allFactors) {
			if(count == 0) {
				result = single;
				count++;
				continue;
			}
			result = result.add(
					single.multiply(z.power(count++))
					);
		}
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("f(z) = ");
		for(int i = this.allFactors.size() - 1; i > -1; i--) {
			sb.append("(" + this.allFactors.get(i) + ")*z^" + i + " + ");
		}
		int size = sb.length() - 1;
		sb.delete(size - 6, size );
		return sb.toString();
	}
	public List<Complex> getAllFactors() {
		return allFactors;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(allFactors);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComplexPolynomial other = (ComplexPolynomial) obj;
		return Objects.equals(allFactors, other.allFactors);
	}
	
	
}
