package org.jmatrix.MatrixTypes;

import java.util.ArrayList;

import org.apfloat.Apcomplex;

public final class CanonicalVector extends Vector {
    public CanonicalVector (int nElements, int coordinate) {
        super (nElements);
        
        this.setEntry (Apcomplex.ONE, coordinate);
    }
}