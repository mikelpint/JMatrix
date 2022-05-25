package org.jmatrix.MatrixTypes;

import org.apfloat.Apcomplex;
import org.jmatrix.internal.JMath;

public final class SymmetricPascalMatrix extends PascalMatrix {
    public SymmetricPascalMatrix (int dimension1) {
        super (dimension1);
        
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension1; column++) {
                this.setEntry ((Apcomplex) JMath.binomialCoefficient (row + column, row), row, column);
            }
        }
    }
    
    public SymmetricPascalMatrix (SymmetricPascalMatrix symmetricPascalMatrix) {
        super (symmetricPascalMatrix);
    }
    
    @Override
    public boolean isSymmetric () {
        return true;
    }
}