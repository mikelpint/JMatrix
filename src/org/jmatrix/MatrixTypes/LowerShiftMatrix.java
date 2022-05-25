package org.jmatrix.MatrixTypes;

public class LowerShiftMatrix extends ShiftMatrix {
    public LowerShiftMatrix (int dimension1) {
        super (dimension1, false);
    }
    
    public LowerShiftMatrix (LowerShiftMatrix lowerShiftMatrix) {
        super (lowerShiftMatrix);
    }
}