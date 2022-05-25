package org.jmatrix.MatrixTypes;

import java.util.ArrayList;

import org.apfloat.Apcomplex;
import org.apfloat.Apint;
import org.apfloat.ApintMath;

public final class LehmerMatrix extends SquareMatrix {
    public LehmerMatrix (int dimension1) {
        super (dimension1);
        
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension1; column++) {
                Apint entry = new Apint (row + 1).divide (new Apint (column + 1));
                
                this.setEntry ((Apcomplex) ((column < row) ? entry : ApintMath.pow (entry, -1)), row, column);
            }
        }
    }
    
    public LehmerMatrix (LehmerMatrix lehmerMatrix) {
        super (lehmerMatrix);
    }
    
    @Override
    public boolean isSymmetric () {
        return true;
    }
}
