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
                this.setEntry (ApcomplexMath.pow ((Apcomplex) new Apfloat (row + column + 1, 1000), -1), row, column);
            }
        }
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
