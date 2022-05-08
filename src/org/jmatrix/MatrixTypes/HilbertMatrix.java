package org.jmatrix.MatrixTypes;

import java.util.ArrayList;

import org.apfloat.Apcomplex;
import org.apfloat.ApcomplexMath;
import org.apfloat.Apfloat;

public class HilbertMatrix extends SquareMatrix {
    public HilbertMatrix (int dimension1) {
        super (dimension1);
        
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension1; column++) {
                super.setEntry (ApcomplexMath.pow ((Apcomplex) new Apfloat (row + column + 1), -1), row, column);
            }
        }
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
    
    @Override
    public boolean isNonnegative () {
        return true;
    }
    
    @Override
    public boolean isHankel () {
        return true;
    }
}
