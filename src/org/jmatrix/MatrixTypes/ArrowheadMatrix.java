package org.jmatrix.MatrixTypes;

import java.util.ArrayList;

import org.apfloat.Apcomplex;

final public class ArrowheadMatrix extends SquareMatrix {
    public ArrowheadMatrix (int dimension1) {
        super (dimension1);
        
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension1; column++) {
                if (row == 0 || column == 0 || row == column) {
                    super.setEntry (Apcomplex.ONE, row, column);
                }
            }
        }
    }
    
    public ArrowheadMatrix (ArrowheadMatrix arrowheadMatrix) {
        super (arrowheadMatrix);
    }
    
    @Override
    public void setMatrix (ArrayList <ArrayList <Apcomplex>> matrix) {
        return;
    }
    
    @Override
    public void setEntry (Apcomplex entry, int... position) {
        return;
    }
}
