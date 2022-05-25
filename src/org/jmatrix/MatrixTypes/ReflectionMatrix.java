package org.jmatrix.MatrixTypes;

import org.apfloat.Apcomplex;
import org.apfloat.ApcomplexMath;
import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.apfloat.Apint;

public final class ReflectionMatrix extends LinearTransformation {
    public ReflectionMatrix (Axis axis) {
        super ();
        
        this.setEntry ((axis == Axis.X) ? Apcomplex.ONE : Apcomplex.ONE.negate (), 0, 0);
        this.setEntry ((axis == Axis.X) ? Apcomplex.ONE.negate () : Apcomplex.ONE, 1, 1);
    }
    
    public ReflectionMatrix (Apfloat angle, boolean degrees) {
        super ();
        
        if (degrees) {
            angle = ApfloatMath.toRadians (angle);
        }
        
        Apcomplex sine = ApcomplexMath.sin ((Apcomplex) angle.multiply (new Apint (2)));
        Apcomplex cosine = ApcomplexMath.cos ((Apcomplex) angle.multiply (new Apint (2)));
        
        this.setEntry (cosine, 0, 0);
        this.setEntry (sine, 0, 1);
        this.setEntry (sine, 1, 1);
        this.setEntry (cosine.negate (), 1, 1);
    }
    
    public ReflectionMatrix (ReflectionMatrix reflectionMatrix) {
        super (reflectionMatrix);
    }
}