package org.jmatrix.MatrixTypes;

import java.util.ArrayList;

import org.apfloat.Apcomplex;
import org.apfloat.Apint;
import org.apfloat.ApintMath;

final public class LehmerMatrix extends SquareMatrix {
    public LehmerMatrix (int dimension1) {
        super (dimension1);
        
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension1; column++) {
                Apint entry = new Apint (row + 1).divide (new Apint (column + 1));
                
                super.setEntry ((Apcomplex) ((column < row) ? entry : ApintMath.pow (entry, -1)), row, column);
            }
        }
    }
    
    public LehmerMatrix (LehmerMatrix lehmerMatrix) {
        super (lehmerMatrix);
    }
    
    @Override
    public void setMatrix (ArrayList <ArrayList <Apcomplex>> matrix) {
        return;
    }
    
    @Override
    public void setEntry (Apcomplex entry, int... position) {
        return;
    }
    
    @Override
    public boolean isSymmetric () {
        return true;
    }
}
