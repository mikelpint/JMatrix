package org.jmatrix.MatrixTypes;

import org.apfloat.Apint;

public class RandomMatrix extends Matrix {
    public RandomMatrix (int dimension1, int dimension2, boolean complex) {
        super (dimension1, dimension2);
        
        this.randomizeElements (null, complex);
    }
    
    public RandomMatrix (int dimension1, int dimension2, Apint range, boolean complex) {
        super (dimension1, dimension2);
        
        this.randomizeElements (range, complex);
    }
}
