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
    public void setMatrix (ArrayList <ArrayList <Apcomplex>> matrix) {
        return;
    }
    
    @Override
    public void setEntry (Apcomplex entry, int... position) {
        return;
    }
    
    @Override
    public boolean isDiagonal () {
        return true;
    }
}
