import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * This class represents multivariate polynomials in Java as a HashMap which maps instances of Monomial to a rational coefficient.
 * @author Jefrey Judes
 */
public class Polynomial {
	
	final int n;
	
	// Each Monomial is mapped to its coefficient
	Map<Monomial, Double> terms = new HashMap<Monomial, Double>();
	
	/**
	 * Creates a new empty polynomial in the specified number of variables.
	 */
	public Polynomial(int var) {
		n=var;
	}
	
	/**
	 * This method adds a term to the polynomial and handles the removal of terms with zero coefficients from the HashMap. 
	 * @param monomial must be an instance of Monomial in as many variables as the polynomial is in.
	 */
	public void addTerm(double coefficient, Monomial monomial) {
		
		assert(monomial.n() == n);
		if (coefficient == 0) return;
		
		if (terms.containsKey(monomial)) {
			
			double newCoef = terms.get(monomial) + coefficient;
			
			// If the term vanishes, we will remove it from the polynomial
			if (newCoef == 0) {
				terms.remove(monomial);
			} else {
				terms.put(monomial, newCoef);
			}
			
		} else {
			terms.put(monomial, coefficient);
		}
		
	}
	
	/**
	 * Returns the number of variables the polynomial is over.
	 */
	public int n() {
		return n;
	}
	
	/**
	 * Returns the number of terms with non-zero coefficients contained in the polynomial.
	 */
	public int nTerms() {
		return terms.size();
	}
	
	/**
	 * Returns whether or not the polynomial contains no non-zero terms
	 */
	public Boolean isZero() {
		return terms.isEmpty();
	}
	
	/**
	 * 
	 */
	public int[] multideg() {
		return getLM().alpha();
	}
	
	public double getCoefficient(Monomial monomial) {
		if (terms.containsKey(monomial)){
			return terms.get(monomial);
		} else {
			return 0;
		}
	}
	
	public Set<Monomial> getMonomials() {
		return terms.keySet();
	}
	
	// Using GREVLEX monomial ordering
	public Monomial getLM() {
		
		assert(!isZero());
		
		Iterator<Monomial> iter = terms.keySet().iterator();
		Monomial LM = iter.next();
		
		// Assign iter.next() to LM if iter.next() > LM wrt. GREVLEX
		while(iter.hasNext()) {
			Monomial m = iter.next();
			
			// Checking total degree first
			if (m.totalDeg() > LM.totalDeg()) {
				LM = m;
			} 
			
			// Comparing exponents from last to first, loop continues if values equal
			else if (m.totalDeg() == LM.totalDeg()) {
				for (int i=n-1 ; i>=0 ; i--) {
					if (m.getDeg(i) < LM.getDeg(i)) {
						LM = m;
						break;
					} else if (LM.getDeg(i) < m.getDeg(i)) {
						break;
					}
				}
			}
		}
		
		return LM;
	}
	
	@Override
	public String toString() {
		String str = "";
		Iterator<Monomial> iter = getMonomials().iterator();
		
		while (iter.hasNext()) {
			Monomial m = iter.next();
			String temp = String.format("%.2f * %s", terms.get(m), m.toString());
			str = str.concat(temp);
			
			if (iter.hasNext()){
				str = str.concat("  +  ");
			}
		}
		
		return str;
	}
	
	// *************************************************************
	// Static methods to work with Polynomial class
	// *************************************************************	
	
	public static Polynomial add(Polynomial p1, Polynomial p2) {
		assert(p1.n() == p2.n());
		
		Polynomial sum = p1;
		
		for (Monomial m : p2.getMonomials()) {
			double coef = p2.getCoefficient(m);
			sum.addTerm(coef, m);
		}
		
		return sum;
	}
	
	public static Polynomial subtract(Polynomial p1, Polynomial p2) {
		assert(p1.n() == p2.n());
		
		Polynomial diff = p1;
		
		for (Monomial m : p2.getMonomials()) {
			double coef = p2.getCoefficient(m);
			diff.addTerm(-coef, m);
		}
		
		return diff;
	}

	public static Polynomial multiply(Polynomial p1, Polynomial p2) {
		assert(p1.n() == p2.n());
		int N = p1.n();
		
		Polynomial product = new Polynomial(N);
		
		for (Monomial m1 : p1.getMonomials()) {
			for (Monomial m2 : p2.getMonomials()) {
				double coef = p1.getCoefficient(m1)*p2.getCoefficient(m2);
				product.addTerm(coef, Monomial.multiply(m1, m2));
			}
		}
		
		return product;
	}
	
	// Polynomial / (Polynomial 1, Polynomial 2, ...)
	public static Polynomial[] divide(Polynomial dividend, Polynomial... divisors) {
		
		int s = divisors.length;
		
		for (int i=0 ; i<s ; i++) {
			assert(divisors[i].n() == dividend.n());
		}
		
		// Last element in output corresponds to remainder
		Polynomial[] output = new Polynomial[s+1];
		for (int i=0; i<s+1; i++) {
			output[i] = new Polynomial(dividend.n());
		}
		
		Polynomial p = dividend;
		
		while (!p.isZero()) {
			
			Monomial pLeading = p.getLM();
			Boolean divisionOccurred = false;
			
			for (int i=0; i<s; i++) {
				Monomial dLeading = divisors[i].getLM();
				if (Monomial.isDivisible(pLeading, dLeading)) {
					
					double coef = p.getCoefficient(pLeading) / divisors[i].getCoefficient(dLeading);
					Monomial qLeading = Monomial.divide(pLeading, dLeading);
					
					output[i].addTerm(coef, qLeading);
					
					Polynomial temp = new Polynomial(dividend.n());
					temp.addTerm(coef, qLeading);
					
					p = Polynomial.subtract(p, Polynomial.multiply(temp, divisors[i]));
					
					divisionOccurred = true;
					break;
				}
			}
			
			if (!divisionOccurred) {
				output[s].addTerm(p.getCoefficient(pLeading), pLeading);
				p.addTerm(-p.getCoefficient(pLeading), pLeading);
			}
			
		}
		
		return output;
	}
	
	public static Polynomial remainder(Polynomial dividend, Polynomial... divisors) {
		int s = divisors.length;
		return Polynomial.divide(dividend, divisors)[s];
	}

}
