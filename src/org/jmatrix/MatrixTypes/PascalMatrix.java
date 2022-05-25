package org.jmatrix.MatrixTypes;

import java.util.ArrayList;

import org.apfloat.Apcomplex;
import org.jmatrix.internal.JMath;

public abstract class PascalMatrix extends SquareMatrix {
    public PascalMatrix (int dimension1) {
        super (dimension1);
    }
    
    public PascalMatrix (PascalMatrix pascalMatrix) {
        super (pascalMatrix);
    }
    
    @Override
    public boolean isLowerTriangular () {
        return false;
    }
    
    @Override
    public boolean isUpperTriangular () {
        return false;
    }
    
    @Override
    public boolean isSymmetric () {
        return false;
    }
}
