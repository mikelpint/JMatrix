package org.jmatrix.MatrixTypes;

import org.apfloat.Apint;

public class RandomSquareMatrix extends SquareMatrix {
    public RandomSquareMatrix (int dimension1, boolean complex) {
        super (dimension1);
        
        this.randomizeElements (null, complex);
    }
    
    public RandomSquareMatrix (int dimension1, Apint range, boolean complex) {
        super (dimension1);
        
        this.randomizeElements (range, complex);
    }
}
