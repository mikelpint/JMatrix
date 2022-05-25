package org.jmatrix.MatrixTypes;


public final class UpperShiftMatrix extends ShiftMatrix {
    public UpperShiftMatrix (int dimension1) {
        super (dimension1, true);
    }
    
    public UpperShiftMatrix (UpperShiftMatrix upperShiftMatrix) {
        super (upperShiftMatrix);
    }
}