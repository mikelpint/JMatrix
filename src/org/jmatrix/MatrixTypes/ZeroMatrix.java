package org.jmatrix.MatrixTypes;

import java.util.ArrayList;

import org.apfloat.Apcomplex;

public class ZeroMatrix extends Matrix {
    public ZeroMatrix (int dimension1, int dimension2) {
        super (dimension1, dimension2);
    }
    
    public ZeroMatrix (ZeroMatrix zeroMatrix) {
        super (zeroMatrix);
    }
    
    @Override
    public boolean isDiagonal () {
        return true;
    }
}
