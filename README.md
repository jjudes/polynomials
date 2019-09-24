The above is a simple representation in Java of multivariate polynomials from R^n to R. The classes provided are Monomial and Polynomial (full documentation provided in Javadoc). Monomials are represented as a list of degrees corresponding to each variable (e.g. Monomial(3, 4, 1) represents a monomial (x^3)(y^4)z) and polynomials are represented via a Hashmap that maps Monomials to a Double value representing it's corresponding coefficient.

Methods are provided to retrieve information about each class of object (e.g. multidegree, length, string representation, etc.) as well as to do simple operations (addition, multiplication, long division, etc.)

Also included is the (work in progress) Ideal class. Ideal represents a mathematical ideal in the mathematical ring of polynomials. The class is initialized with Polynomials by which it is defined, and when initialized the class will compute a Groebner basis for the ideal.
