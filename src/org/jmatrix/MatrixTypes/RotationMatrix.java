package org.jmatrix.MatrixTypes;

import org.apfloat.Apcomplex;
import org.apfloat.ApcomplexMath;
import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

public final class RotationMatrix extends LinearTransformation {
    public RotationMatrix (Apcomplex complexNumber) {
        this (ApcomplexMath.abs (complexNumber), false);
    }
    
    public RotationMatrix (Apfloat angle, boolean degrees) {
        super ();
        
        if (degrees) {
            angle = ApfloatMath.toRadians (angle);
        }
        
        Apcomplex sine = ApcomplexMath.sin ((Apcomplex) angle);
        Apcomplex cosine = ApcomplexMath.cos ((Apcomplex) angle);
        
        this.setEntry (cosine, 0, 0);
        this.setEntry (sine.negate (), 0, 1);
        this.setEntry (sine, 1, 1);
        this.setEntry (cosine, 1, 1);
    }
    
    public RotationMatrix (RotationMatrix rotationMatrix) {
        super (rotationMatrix);
    }
}