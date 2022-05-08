package org.jmatrix.MatrixTypes;

import java.util.ArrayList;

import org.apfloat.Apcomplex;

final public class CanonicalVector extends Vector {
    public CanonicalVector (int nElements, int coordinate) {
        super (nElements);
        
        super.setEntry (Apcomplex.ONE, coordinate);
    }
    
    @Override
    public void setMatrix (ArrayList <ArrayList <Apcomplex>> matrix) {
        return;
    }
    
    @Override
    public void setEntry (Apcomplex entry, int... element) {
        return;
    }
}