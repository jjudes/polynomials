import java.util.Arrays;

/**
 * An instance of Monomial is a wrapper class for an array containing the degrees associated with the monomial. 
 * The instance methods return simple information about the degrees that the class was initialized with but do not change the monomial. 
 * In addition, static methods for multiplication and division for monomials are introduced. 
 * While monomials are a natural case of polynomials, this class does not extend the class Polynomial. 
 * Instead, the asPolynomial() method converts the Monomial instance into a Polynomial containing a single term with unit coefficient.
 * @author Jefrey Judes
 */
public class Monomial{
	
	final int[] alpha;
	
	/**
	 * Creates an instance of a monomial in n variables is initialized with the integer degree (zero included) of all n variables listed in some consistent order. 
	 * Note that this monomial can only be part of a term in a polynomial in n variables.
	 */
	public Monomial(int... degrees) {
		alpha = degrees;
	}
	
	/**
	 * Returns the number of variables that the monomial is in (including zero powers)
	 */
	public int n() {
		return alpha.length;
	}
	
	/**
	 * Returns an array of integers corresponding to the degrees of each variable. 
	 * This is exactly the array that the monomial was initialized with.
	 */
	public int[] alpha(){
		return alpha;
	}
	
	/**
	 * Returns the degree of the variable at the specified position
	 * @param i specifies the position, starting at 0 and ending at n
	 */
	public int getDeg(int i) {
		assert(i<alpha.length);
		return alpha[i];
	}
	
	/**
	 * Returns the sum of all degrees in the polynomial
	 */
	public int totalDeg() {
		int sum = 0;
		for (int i=0; i<alpha.length; i++) {
			sum += alpha[i];
		}
		return sum;
	}
	
	/**
	 * Compares monomials by the array of degrees contained
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Monomial && o != null){
			return Arrays.equals(alpha, ((Monomial) o).alpha());
		} else {
			return false;
		}
	}
	
	/**
	 * Creates a hashcode that is not unique to the instance, but rather to the array of degrees
	 */
	@Override
	public int hashCode() {
		return Arrays.hashCode(alpha);
	}
	
	/**
	 * Returns a string formatted as (x0)^deg(x0) (x1)^deg(x1) ...
	 */
	@Override
	public String toString() {
		
		String str = "";
		
		for (int i=0; i<alpha.length; i++) {
			str = str.concat(String.format("(x%o)^%o ", i+1, alpha[i]));
		}
		
		//Without last extra space
		return str.substring(0, str.length()-1);
	}
	
	/**
	 * Converts the monomial into a polynomial containing the instance of Monomial with unit coefficient
	 * @return Polynomial in as many variables as the monomial contains
	 */
	public Polynomial asPolynomial() {
		Polynomial p = new Polynomial(n());
		p.addTerm(1, new Monomial(alpha));
		return p;
	}
	
	
	// *************************************************************
	// Static methods to work with Monomial class
	// *************************************************************	
	
	/**
	 * Computes the product of two monomials in the same number of variables
	 */
	public static Monomial multiply(Monomial m1, Monomial m2) {
		assert(m1.n()==m2.n());
		int n = m1.n();
		
		int[] prod = new int[n];
		for (int i=0; i<n; i++) {
			prod[i] = m1.getDeg(i) + m2.getDeg(i);
		}
		
		return new Monomial(prod);
	}
	
	/**
	 * Specifies whether the first monomial argument is divisible by the second
	 */
	public static Boolean isDivisible(Monomial dividend, Monomial divisor) {
		assert(divisor.n() == dividend.n());
		int n = divisor.n();
		
		Boolean divisible = true;
		for (int i=0; i<n; i++) {
			if (dividend.getDeg(i) < divisor.getDeg(i)) {
				divisible = false;
				break;
			}
		}
		return divisible;
	}
	
	/**
	 * Returns the quotient monomial obtained by dividing the first monomial argument by the second. 
	 * This method can only be called when divisibility is guaranteed
	 */
	public static Monomial divide(Monomial dividend, Monomial divisor) {
		assert(Monomial.isDivisible(dividend, divisor));
		int n = dividend.n();
		
		int[] quotient = new int[n];
		for (int i=0; i<n; i++) {
			quotient[i] = dividend.getDeg(i) - divisor.getDeg(i);
		}
		return new Monomial(quotient);
	}
	
	/**
	 * Returns a polynomial containing a single term with the specified monomial and unit coefficient
	 * @param m specifies the Monomial to be placed in the polynomial
	 * @return Polynomial in as many variables as the monomial contains
	 */
	public static Polynomial asPolynomial(Monomial m) {
		Polynomial p = new Polynomial(m.n());
		p.addTerm(1, m);
		return p;
	}

}
