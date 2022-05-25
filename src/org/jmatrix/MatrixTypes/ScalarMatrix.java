package org.jmatrix.MatrixTypes;

import java.util.ArrayList;

import org.apfloat.Apcomplex;

public class ScalarMatrix extends SquareMatrix {
    public ScalarMatrix (int dimension1, Apcomplex scalar) {
        super (dimension1);

        for (int entry = 0; entry < dimension1; entry++) {
            this.setEntry (scalar, entry, entry);
        }
    }
    
    public ScalarMatrix (ScalarMatrix scalarMatrix) {
        super (scalarMatrix);
    }
    
    @Override
    public SquareMatrix transpose () {
        return this;
    }
    
    @Override
    public SquareMatrix transjugate () {
        return this;
    }
    
    @Override
    public boolean isReal () {
        return true;
    }
    
    @Override
    public boolean isInteger () {
        return true;
    }
    
    @Override
    public boolean isSymmetric () {
        return true;
    }
    
    @Override
    public boolean isDiagonal () {
        return true;
    }
    
    @Override
    public boolean isSingular () {
        return false;
    }
    
    @Override
    public boolean isHollow () {
        return false;
    }
}
