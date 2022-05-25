package org.jmatrix.MatrixTypes;

import org.apfloat.Apcomplex;
import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

public final class ScalingMatrix extends LinearTransformation {
    public ScalingMatrix (Apfloat factor, boolean shrink) {
        this ((ScalingMatrix) (SquareMatrix) new ScalarMatrix (2, (Apcomplex) ((shrink) ? ApfloatMath.pow (factor, -1) : factor)));
    }
    
    public ScalingMatrix (Apfloat factor, boolean shrink, Axis axis) {
        this ((ScalingMatrix) (SquareMatrix) new IdentityMatrix (2));
        
        this.setEntry ((Apcomplex) ((shrink) ? ApfloatMath.pow (factor, -1) : factor), (axis == Axis.X) ? (new int [] {0, 1}) : (new int [] {1, 0}));
    }
    
    public ScalingMatrix (ScalingMatrix scalingMatrix) {
        super (scalingMatrix);
    }
}