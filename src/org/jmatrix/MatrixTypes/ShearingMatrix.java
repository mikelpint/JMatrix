package org.jmatrix.MatrixTypes;

import org.apfloat.Apcomplex;
import org.apfloat.Apfloat;

public final class ShearingMatrix extends LinearTransformation {
    public ShearingMatrix (Apfloat factor, Axis axis) {
        this ((ShearingMatrix) (SquareMatrix) new IdentityMatrix (2));
        
        this.setEntry ((Apcomplex) factor, 0, 0);
    }
    
    public ShearingMatrix (ShearingMatrix shearingMatrix) {
        super (shearingMatrix);
    }
}