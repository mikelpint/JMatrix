package org.jmatrix.MatrixTypes;

import org.apfloat.Apint;

public class RandomVector extends Vector {
    public RandomVector (int nElements, boolean complex) {
        super (nElements);
        
        this.randomizeElements (null, complex);
    }
    
    public RandomVector (int nElements, Apint range, boolean complex) {
        super (nElements);
        
        this.randomizeElements (range, complex);
    }
}
