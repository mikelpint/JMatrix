package org.jmatrix.MatrixTypes;

import org.apfloat.Apcomplex;

final public class IdentityMatrix extends ScalarMatrix {
    public IdentityMatrix (int dimension1) {
        super (dimension1, Apcomplex.ONE);
    }
    
    public IdentityMatrix (IdentityMatrix identityMatrix) {
        super (identityMatrix);
    }
    
    @Override
    public boolean isInvolutory () {
        return true;
    }
    
    @Override
    public boolean isIdempotent () {
        return true;
    }
}
