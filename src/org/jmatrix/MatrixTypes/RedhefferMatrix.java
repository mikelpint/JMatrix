package org.jmatrix.MatrixTypes;

import java.util.ArrayList;

import org.apfloat.Apcomplex;

final public class RedhefferMatrix extends SquareMatrix {
    public RedhefferMatrix (int dimension1) {
        super (dimension1);
        
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension1; column++) {
                if (column == 0 || (column + 1) % (row + 1) == 0) {
                    super.setEntry (Apcomplex.ONE, column);
                }
            }
        }
    }
    
    public RedhefferMatrix (RedhefferMatrix redhefferMatrix) {
        super (redhefferMatrix);
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
    public boolean isHollow () {
        return false;
    }
}
