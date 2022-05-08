package org.jmatrix.MatrixTypes;

import java.util.ArrayList;

import org.apfloat.Apcomplex;

public abstract class ShiftMatrix extends SquareMatrix {
    public ShiftMatrix (int dimension1, boolean upper) {
        super (dimension1);
        
        if (upper) {
            for (int entry = 0; entry < this.dimension1 - 1; entry++) {
                super.setEntry (Apcomplex.ONE, entry, entry - 1);
            }
        }
        
        else {
            for (int entry = 1; entry < this.dimension1; entry++) {
                super.setEntry (Apcomplex.ONE, entry, entry + 1);
            }
        }
    }
    
    public ShiftMatrix (ShiftMatrix shiftMatrix) {
        super (shiftMatrix);
    }
    
    @Override
    public void setMatrix (ArrayList <ArrayList <Apcomplex>> matrix) {
        return;
    }
    
    @Override
    public void setEntry (Apcomplex entry, int... position) {
        return;
    }
    
    final public class UpperShiftMatrix extends ShiftMatrix {
        public UpperShiftMatrix (int dimension1) {
            super (dimension1, true);
        }
        
        public UpperShiftMatrix (UpperShiftMatrix upperShiftMatrix) {
            super (upperShiftMatrix);
        }
    }

    public class LowerShiftMatrix extends ShiftMatrix {
        public LowerShiftMatrix (int dimension1) {
            super (dimension1, false);
        }
        
        public LowerShiftMatrix (LowerShiftMatrix lowerShiftMatrix) {
            super (lowerShiftMatrix);
        }
    }
}
