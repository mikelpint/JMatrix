package org.jmatrix.MatrixTypes;

import org.apfloat.Apcomplex;
import org.jmatrix.internal.JMath;

public final class UpperPascalMatrix extends PascalMatrix {
    public UpperPascalMatrix (int dimension1) {
        super (dimension1);
        
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension1; column++) {
                if (row <= column) {
                    this.setEntry ((Apcomplex) JMath.binomialCoefficient (column, row), row, column);
                }
            }
        }
    }
    
    public UpperPascalMatrix (UpperPascalMatrix upperPascalMatrix) {
        super (upperPascalMatrix);
    }
    
    @Override
    public boolean isUpperTriangular () {
        return true;
    }
}