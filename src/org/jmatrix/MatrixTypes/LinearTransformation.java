package org.jmatrix.MatrixTypes;

import java.util.ArrayList;
import java.util.Arrays;

import org.apfloat.Apcomplex;
import org.apfloat.Apfloat;
import org.apfloat.ApcomplexMath;
import org.apfloat.ApfloatMath;
import org.apfloat.Apint;

import org.jmatrix.MatrixOps.MatrixOps;

public abstract class LinearTransformation extends SquareMatrix {
    public enum Axis {
        X,
        Y
    }
    
    public LinearTransformation () {
        super (2);
    }
    
    public LinearTransformation (ArrayList <ArrayList <Apcomplex>> entries) {
        super (2);
        
        super.setMatrix (entries);
    }
    
    public LinearTransformation (LinearTransformation linearTransformation) {
        super (linearTransformation);
    }
}
