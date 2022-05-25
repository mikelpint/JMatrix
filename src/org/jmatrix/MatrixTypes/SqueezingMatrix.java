package org.jmatrix.MatrixTypes;

import org.apfloat.Apcomplex;
import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

public final class SqueezingMatrix extends LinearTransformation {
    public SqueezingMatrix (Apfloat factor, Axis axis) {
        this ((SqueezingMatrix) (SquareMatrix) new IdentityMatrix (2));
        
        this.setEntry ((Apcomplex) ((axis == Axis.X) ? factor : ApfloatMath.pow (factor, -1)), 0, 0);
        this.setEntry ((Apcomplex) ((axis == Axis.X) ? ApfloatMath.pow (factor, -1) : factor), 1, 1);
    }
    
    public SqueezingMatrix (SqueezingMatrix squeezingMatrix) {
        super (squeezingMatrix);
    }
}