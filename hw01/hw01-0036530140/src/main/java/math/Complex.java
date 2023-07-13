package math;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/** This class represents a single complex number. It supports several operations. It can also produce new complex numbers,
 * 	based on a chosen operation.
 * 
 * 	@author adrian
 */
public class Complex {
	/** Stores the real part of complex number. */
	private double real;
	/** Stores the imaginary part of complex number. */
	private double imag;
	
	/** Some common complex numbers already initialized. */
	public static final Complex ZERO = new Complex(0,0);
	public static final Complex ONE = new Complex(1,0);
	public static final Complex ONE_NEG = new Complex(-1,0);
	public static final Complex IM = new Complex(0,1);
	public static final Complex IM_NEG = new Complex(0,-1);
	
	/**	Default constructor, creates complex number with value both the value of the real and imaginary
	 * 	parts are zero.
	 */
	public Complex() {
		this(0, 0);
	}
	
	/**	Constructor initializes the values of the real and imaginary part of a complex number with
	 * 	the given values rounded to the predefined decimal.
	 * 	
	 * 	@param double value of the real part of complex number
	 * 	@param double value of the imaginary part of complex number
	 */
	public Complex(double re, double im) {
		this.real = re ;
		this.imag = im ;
	}
	
	/** Returns module of complex number.
	 * 
	 * @return double value of module
	 */
	public double module() {
		return Math.sqrt(Math.pow(real, 2) + Math.pow(imag, 2));
	}
	
	/** Returns resulting complex number of multiplying this complex number with provided complex number.
	 * 
	 * @param Complex number used as multiplier
	 * @return Complex number as result
	 */
	public Complex multiply(Complex c) {
		double newReal = this.real*c.real - this.imag*c.imag;
		double newImag = this.real*c.imag + this.imag*c.real;
		return new Complex(newReal, newImag);
	}
	
	/** Returns resulting complex number of dividing this complex number with provided complex number.
	 * 
	 * @param Complex number used as divisor
	 * @return Complex number as result
	 */
	public Complex divide(Complex c) {
		double newReal = (this.real*c.real + this.imag*c.imag) / (Math.pow(c.real, 2) + Math.pow(c.imag, 2)) ;
		double newImag = (this.imag*c.real - this.real*c.imag) / (Math.pow(c.real, 2) + Math.pow(c.imag, 2)) ;
		return new Complex(newReal, newImag);
	}
	
	/** Returns resulting complex number of adding this complex number to the provided complex number.
	 * 
	 * @param Complex number used for addition
	 * @return Complex number as result
	 */
	public Complex add(Complex c) {
		double newReal = this.real + c.real ;
		double newImag = this.imag + c.imag ;
		return new Complex(newReal, newImag);
	}
	
	/** Returns resulting complex number of subtracting this complex number with provided complex number.
	 * 
	 * @param Complex number used as subtrahend
	 * @return Complex number as result
	 */
	public Complex sub(Complex c) {
		double newReal = this.real - c.real;
		double newImag = this.imag - c.imag ;
		return new Complex(newReal, newImag);
	}
	
	/** Returns complex number negated in comparison to this one.
	 * 
	 * @return Complex number negated
	 */
	public Complex negate() {
		return new Complex(-this.real, -this.imag);
	}
	
	/** Returns this^n, where n is non-negative integer.
	 * 
	 * @param integer value as exponent
	 * @throws IllegalArgumentException if exponent is not a non-negative integer
	 * @return Complex number as result
	 */
	public Complex power(int n) {
		if(n < 0) 
			throw new IllegalArgumentException("Exponent should be non-negative when powering complex numbers!");
		if(n == 0) return Complex.ONE;
		double r = module();
		double theta = Math.atan2(this.imag, this.real);
		double newReal = Math.pow(r, n) * Math.cos(n * theta) ;
		double newImag = Math.pow(r, n) * Math.sin(n * theta) ;
		return new Complex(newReal, newImag);
	}
	
	/** Returns n-th root of this, where n must be a positive integer.
	 * 
	 * @param integer as n-th root 
	 * @throws IllegalArgumentException if provided root is not a positive integer 
	 * @return List<Complex> of Complex instances
	 */
	public List<Complex> root(int n) {
		if(n <= 0) 
			throw new IllegalArgumentException("The number of roots must be a positive integer!");
		List<Complex> roots = new ArrayList<>();
		double sqrR = Math.pow(module(), 1.0 / n);
		double theta = Math.atan(this.imag / this.real);
		double newReal;
		double newImag;
		for(int i = 0; i < n; i++) {
			newReal =  sqrR * Math.cos( (theta + 2 * Math.PI * i) / n ) ;
			newImag = sqrR * Math.sin( (theta + 2 * Math.PI * i) / n ) ;
			roots.add(new Complex(newReal, newImag));
		}
		return roots;
	}
	
	@Override
	public String toString() {
		if(this.imag >= 0)
			return String.format("%.1f + i%.1f", this.real, this.imag);
		else
			return String.format("%.1f - i%.1f", this.real, -this.imag);		
	}
	public double getReal() {
		return real;
	}
	public double getImag() {
		return imag;
	}
	@Override
	public int hashCode() {
		return Objects.hash(imag, real);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Complex other = (Complex) obj;
		return Math.abs(other.getReal() - real) < 0.0000001 && Math.abs(other.getImag() - imag) < 0.0000001;
	}
	
}
