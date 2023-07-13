package math;

import java.util.ArrayList;
import java.util.List;

/** This class represents a single polynomial formed with complex numbers. It supports the following operations: 
 * 	first derivation, multiplying with another polynomial and computation of polynomial value in at a specified
 * 	point.
 * 
 * 	@author adrian
 */
public class ComplexRootedPolynomial {
	/** Stores the value of z0, the constant. */
	private Complex constant;
	/** List used for storing factors of this rooted polynomial. List starts from z0, z1, z2 ... and ends with zn. */
	private List<Complex> allRoots;
	
	/**	Constructor which initializes this rooted polynomial. At least one complex number must be provided.
	 * 
	 * 	@param Complex instances, one or more of them
	 */
	public ComplexRootedPolynomial(Complex constant, Complex ... roots) {
		this.allRoots = new ArrayList<>();
		this.constant = constant;
		for(Complex one: roots) {
			allRoots.add(one);
		}
	}
	
	/** Computes polynomial value at given point z.
	 * 
	 * 	@param Complex as the given point z
	 * 	@return Complex as result
	 */
	public Complex apply(Complex z) {
		Complex result = this.constant;
		for(Complex one: allRoots) {
			result = result.multiply(z.sub(one));
		}
		return result;
	}
	
	/** Converts this representation to ComplexPolynomial type. 
	 * 
	 * 	@return ComplexPolynomial 
	 */
	public ComplexPolynomial toComplexPolynom() {
		Complex[] elems = new Complex[this.allRoots.size() + 1];
		for(int i = 0; i < elems.length; i++) {
			elems[i] = Complex.ZERO;
		}
		elems[0] = this.constant;
		int count = 1;
		for(Complex root: this.allRoots) {
			for(int i = count; i > 0; i--) {
				if(count == i) {
					elems[i] = elems[i - 1].multiply(root.negate());
				} else {
					elems[i] = elems[i - 1].multiply(root.negate()).add(elems[i]);
				}
			}
			count++;
		}
		Complex[] reverse = new Complex[this.allRoots.size() + 1];
		for(Complex elem: elems) {
			reverse[--count] = elem;
		}
		return new ComplexPolynomial(reverse);
	}
	
	/** Finds index of closest root for given complex number z that is within treshold; if there is no such
	 *  root, returns -1. First root has index 0, second index 1, etc.
	 * 	@param Complex as the given z
	 * 	@param double as treshold
	 * 	@return integer value as index
	 */
	public int indexOfClosestRootFor(Complex z, double treshold) {
		double current;
		for(int i = 0, count = this.allRoots.size(); i < count; i++) {
			current = z.sub(this.allRoots.get(i)).module();
			if(current <= treshold) 
				return i;
		}
		return -1;
	}
		
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("f(z) = ");
		if(this.allRoots.isEmpty())
			sb.append(this.constant);
		else 
			sb.append("(" + this.constant +")");
		for(Complex one: allRoots) {
			sb.append(" * (z - (" + one + "))");
		}
		return sb.toString();
	}
}
