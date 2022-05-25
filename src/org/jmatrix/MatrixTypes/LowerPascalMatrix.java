package org.jmatrix.MatrixTypes;

import org.apfloat.Apcomplex;
import org.jmatrix.internal.JMath;

public final class LowerPascalMatrix extends PascalMatrix {
    public LowerPascalMatrix (int dimension1) {
        super (dimension1);
        
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension1; column++) {
                if (column <= row) {
                    this.setEntry ((Apcomplex) JMath.binomialCoefficient (row, column), row, column);
                }
            }
        }
    }
    
    public LowerPascalMatrix (LowerPascalMatrix lowerPascalMatrix) {
        super (lowerPascalMatrix);
    }
    
    @Override
    public boolean isLowerTriangular () {
        return true;
    }
}