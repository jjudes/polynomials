import java.util.List;
import java.util.Arrays;

public class Ideal {
	
	final Polynomial[] basis;
	Polynomial[] groebnerBasis;
	
	public Ideal(Polynomial...polynomials) {
		assert(polynomials.length != 0);
		basis = polynomials;
		//groebnerBasis = Ideal.grobnerBasis(polynomials);
	}
	
	public Boolean isMember(Polynomial p) {
		return Polynomial.remainder(p, groebnerBasis).isZero();
	}
	
//	public static Polynomial[] grobnerBasis(Polynomial[] generators) {
//		
//		List<Polynomial> groebner = Arrays.asList(generators);
//		List<Polynomial> g;
//		
//		do {
//		
//		
//		} while(g != groebner);
//		
//		return groebner.toArray(new Polynomial[groebner.size()]);
//		
//	}

	public static Polynomial S(Polynomial p1, Polynomial p2) {
		assert(p1.n() == p2.n());
		int n = p1.n();
		
		Monomial a = p1.getLM();
		Monomial b = p2.getLM();
		
		int[] degrees = new int[n];
		
		for (int i=0; i < n; i++) {
			if (a.getDeg(i) >= b.getDeg(i)){
				degrees[i] = a.getDeg(i);
			} else {
				degrees[i] = b.getDeg(i);
			}
		}
		
		Monomial LCM = new Monomial(degrees);
		
		Polynomial tempA = new Polynomial(n);
		tempA.addTerm(1/p1.getCoefficient(a), Monomial.divide(LCM, a));
		tempA = Polynomial.multiply(tempA, p1);
		
		Polynomial tempB = new Polynomial(n);
		tempB.addTerm(1/p2.getCoefficient(b), Monomial.divide(LCM, b));
		tempB = Polynomial.multiply(tempB, p2);
		
		return Polynomial.subtract(tempA, tempB);
		
	}
	
}