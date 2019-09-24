import java.util.ArrayList;
import java.util.List;

public class Solver {

	public static void main(String[] args) {
		
		int[] solutions = new int[81];
		List<Polynomial> generators = new ArrayList<Polynomial>();
		
		final int[] sumProduct = {-2, -1, 1, 2, 3, 4, 5, 6, 7};
		final Monomial constant = new Monomial(new int[81]);
		
		
		// Encode the sumProduct values for each Sudoku box
		for (int s=0; s<81; s++) {
			
			// Set p = 1
			Polynomial p = new Polynomial(81);
			p.addTerm(1, constant);
			
			// Multiply p by each (x_i - sumProduct[i])
			for (int i=0; i<9; i++) {
				
				Polynomial q = new Polynomial(81);
				
				int[] alpha = new int[81];
				alpha[s] = 1;
				q.addTerm(1, new Monomial(alpha));
				
				q.addTerm(-sumProduct[i], constant);
				
				p = Polynomial.multiply(p, q);
				
			}
			
			// Add p to the list of generators
			generators.add(p);
		}
		
		//Encode row rules with sum-product polynomials
		for (int row=0; row<9; row++) {
			
			Polynomial rowSum = new Polynomial(81);
			Polynomial rowProd = new Polynomial(81);
			rowProd.addTerm(1, constant);
			
			for (int i=0; i<9; i++) {
				
				int[] alpha = new int[81];
				alpha[9*row + i] = 1;
				Monomial x = new Monomial(alpha);
				
				Polynomial p = new Polynomial(81);
				p.addTerm(1, x);
				
				rowSum = Polynomial.add(rowSum, p);
				rowProd = Polynomial.multiply(rowProd, p);
				
			}
			
			rowSum.addTerm(-25, constant);
			generators.add(rowSum);
			rowProd.addTerm(-10080, constant);
			generators.add(rowProd);
			
		}
		
		//Encode column rules with sum-product polynomials
		for (int col=0; col<9; col++) {
			
			Polynomial colSum = new Polynomial(81);
			Polynomial colProd = new Polynomial(81);
			colProd.addTerm(1, constant);
			
			for (int i=0; i<9; i++) {
				
				int[] alpha = new int[81];
				alpha[9*i + col] = 1;
				Monomial x = new Monomial(alpha);
				
				Polynomial p = new Polynomial(81);
				p.addTerm(1, x);
				
				colSum = Polynomial.add(colSum, p);
				colProd = Polynomial.multiply(colProd, p);
				
			}
			
			colSum.addTerm(-25, constant);
			generators.add(colSum);
			colProd.addTerm(-10080, constant);
			generators.add(colProd);
			
		}
		
		//Encode box rules with sum-product polynomials
		for (int box=0; box<9; box++) {
			
			Polynomial boxSum = new Polynomial(81);
			Polynomial boxProd = new Polynomial(81);
			boxProd.addTerm(1, constant);
			
			for (int i=0; i<3; i++) {
				for(int j=0; j<3; j++) {
					int[] alpha = new int[81];
					int r = box%3;
					int q = (box-r)/3;
					alpha[9*(3*q+i) + 3*r+j] = 1;
					Monomial x = new Monomial(alpha);
					
					Polynomial p = new Polynomial(81);
					p.addTerm(1, x);
					
					boxSum = Polynomial.add(boxSum, p);
					boxProd = Polynomial.multiply(boxProd, p);
				}
			}
			
			boxSum.addTerm(-25, constant);
			generators.add(boxSum);
			boxProd.addTerm(-10080, constant);
			generators.add(boxProd);
			
		}
				
		//Read in Sudoku puzzle
		String puzzle = "800000000003600000070090200050006000000045700000100030001000068008500010090000400";
		assert(puzzle.length()==81);
		
		for(int i=0; i<81; i++) {
			
			int h = Character.getNumericValue(puzzle.charAt(i));
			
			if (h!=0) {
				solutions[i] = h;
				
				int[] alpha = new int[81];
				alpha[i] = 1;
				Monomial x = new Monomial(alpha);
				
				Polynomial p = new Polynomial(81);
				p.addTerm(1, x);
				p.addTerm(-sumProduct[h-1], constant);
				
				generators.add(p);
			}
			
		}
		
		Ideal sudoku = new Ideal(generators.toArray(new Polynomial[generators.size()]));
		
		//Check if (x-a) is in the ideal
		
		for (int i=0; i<81; i++) {
			
			int[] alpha = new int[81];
			alpha[i] = 1;
			Monomial x = new Monomial(alpha);
			
			for (int j=0; j<9; j++) {
				
				Polynomial v = new Polynomial(81);
				v.addTerm(1, x);
				v.addTerm(-sumProduct[j], constant);
				
				if (sudoku.isMember(v)) {
					solutions[i] = j+1;
					break;
				}
			}		
		}
				
		for (int i=0; i<9; i++) {
			for (int j=0; j<9; j++) {
				System.out.print(solutions[9*i+j] + " ");
			}
			System.out.println();
		}
		
	}

}
