package org.jmatrix.MatrixTypes;

import org.apfloat.Apint;

public class RandomRowVector extends RowVector {
    public RandomRowVector (int nElements, boolean complex) {
        super (nElements);
        
        this.randomizeElements (null, complex);
    }
    
    public RandomRowVector (int nElements, Apint range, boolean complex) {
        super (nElements);
        
        this.randomizeElements (range, complex);
    }
}
