package org.jmatrix.MatrixTypes;

import java.util.ArrayList;

import org.apfloat.Apcomplex;

public abstract class ShiftMatrix extends SquareMatrix {
    public ShiftMatrix (int dimension1, boolean upper) {
        super (dimension1);
        
        if (upper) {
            for (int entry = 0; entry < this.dimension1 - 1; entry++) {
                this.setEntry (Apcomplex.ONE, entry, entry - 1);
            }
        }
        
        else {
            for (int entry = 1; entry < this.dimension1; entry++) {
                this.setEntry (Apcomplex.ONE, entry, entry + 1);
            }
        }
    }
    
    public ShiftMatrix (ShiftMatrix shiftMatrix) {
        super (shiftMatrix);
    }
}
