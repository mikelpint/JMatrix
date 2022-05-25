package org.jmatrix.MatrixTypes;

import java.util.ArrayList;

import org.apfloat.Apcomplex;

public final class RedhefferMatrix extends SquareMatrix {
    public RedhefferMatrix (int dimension1) {
        super (dimension1);
        
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension1; column++) {
                if (column == 0 || (column + 1) % (row + 1) == 0) {
                    this.setEntry (Apcomplex.ONE, column);
                }
            }
        }
    }
    
    public RedhefferMatrix (RedhefferMatrix redhefferMatrix) {
        super (redhefferMatrix);
    }
    
    @Override
    public boolean isHollow () {
        return false;
    }
}
