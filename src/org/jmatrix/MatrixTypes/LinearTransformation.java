package org.jmatrix.MatrixTypes;

import java.util.ArrayList;
import java.util.Arrays;

import org.apfloat.Apcomplex;
import org.apfloat.Apfloat;
import org.apfloat.ApcomplexMath;
import org.apfloat.ApfloatMath;
import org.apfloat.Apint;

import org.jmatrix.MatrixOps.MatrixOps;

abstract public class LinearTransformation extends SquareMatrix {
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
    
    @Override
    public void setMatrix (ArrayList <ArrayList <Apcomplex>> matrix) {
        return;
    }
    
    final public class RotationMatrix extends LinearTransformation {
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
            
            super.setEntry (cosine, 0, 0);
            super.setEntry (sine.negate (), 0, 1);
            super.setEntry (sine, 1, 1);
            super.setEntry (cosine, 1, 1);
        }
        
        public RotationMatrix (RotationMatrix rotationMatrix) {
            super (rotationMatrix);
        }
        
        @Override
        public void setEntry (Apcomplex entry, int... position) {
            return;
        }
    }
    
    final public class ReflectionMatrix extends LinearTransformation {
        public ReflectionMatrix (Axis axis) {
            super ();
            
            super.setEntry ((axis == Axis.X) ? Apcomplex.ONE : Apcomplex.ONE.negate (), 0, 0);
            super.setEntry ((axis == Axis.X) ? Apcomplex.ONE.negate () : Apcomplex.ONE, 1, 1);
        }
        
        public ReflectionMatrix (Apfloat angle, boolean degrees) {
            super ();
            
            if (degrees) {
                angle = ApfloatMath.toRadians (angle);
            }
            
            Apcomplex sine = ApcomplexMath.sin ((Apcomplex) angle.multiply (new Apint (2)));
            Apcomplex cosine = ApcomplexMath.cos ((Apcomplex) angle.multiply (new Apint (2)));
            
            super.setEntry (cosine, 0, 0);
            super.setEntry (sine, 0, 1);
            super.setEntry (sine, 1, 1);
            super.setEntry (cosine.negate (), 1, 1);
        }
        
        public ReflectionMatrix (ReflectionMatrix reflectionMatrix) {
            super (reflectionMatrix);
        }
        
        @Override
        public void setEntry (Apcomplex entry, int... position) {
            return;
        }
    }
    
    final public class ScalingMatrix extends LinearTransformation {
        public ScalingMatrix (Apfloat factor, boolean shrink) {
            this ((ScalingMatrix) (SquareMatrix) new ScalarMatrix (2, (Apcomplex) ((shrink) ? ApfloatMath.pow (factor, -1) : factor)));
        }
        
        public ScalingMatrix (Apfloat factor, boolean shrink, Axis axis) {
            this ((ScalingMatrix) (SquareMatrix) new IdentityMatrix (2));
            
            super.setEntry ((Apcomplex) ((shrink) ? ApfloatMath.pow (factor, -1) : factor), (axis == Axis.X) ? (new int [] {0, 1}) : (new int [] {1, 0}));
        }
        
        public ScalingMatrix (ScalingMatrix scalingMatrix) {
            super (scalingMatrix);
        }
        
        @Override
        public void setEntry (Apcomplex entry, int... position) {
            return;
        }
    }
    
    final public class SqueezingMatrix extends LinearTransformation {
        public SqueezingMatrix (Apfloat factor, Axis axis) {
            this ((SqueezingMatrix) (SquareMatrix) new IdentityMatrix (2));
            
            super.setEntry ((Apcomplex) ((axis == Axis.X) ? factor : ApfloatMath.pow (factor, -1)), 0, 0);
            super.setEntry ((Apcomplex) ((axis == Axis.X) ? ApfloatMath.pow (factor, -1) : factor), 1, 1);
        }
        
        public SqueezingMatrix (SqueezingMatrix squeezingMatrix) {
            super (squeezingMatrix);
        }
        
        @Override
        public void setEntry (Apcomplex entry, int... position) {
            return;
        }
    }
    
    final public class ShearingMatrix extends LinearTransformation {
        public ShearingMatrix (Apfloat factor, Axis axis) {
            this ((ShearingMatrix) (SquareMatrix) new IdentityMatrix (2));
            
            super.setEntry ((Apcomplex) factor, 0, 0);
        }
        
        public ShearingMatrix (ShearingMatrix shearingMatrix) {
            super (shearingMatrix);
        }
        
        @Override
        public void setEntry (Apcomplex entry, int... position) {
            return;
        }
    }
    
    final public class ProjectionMatrix extends LinearTransformation {
        public ProjectionMatrix (Apfloat x, Apfloat y) {
            this (
                new Vector (
                    2,
                    new ArrayList <ArrayList <Apcomplex>> (
                        Arrays.asList (
                            new ArrayList <Apcomplex> (
                                Arrays.asList (
                                    (Apcomplex) x,
                                    (Apcomplex) y
                                )
                            )
                        )
                    )
                )
            );
        }
        
        public ProjectionMatrix (Vector vector) {
            this (
                (ProjectionMatrix) (SquareMatrix) MatrixOps.scalarMultiplication (
                    MatrixOps.matrixMultiplication (
                        (Matrix) vector,
                        (Matrix) vector.transpose ()
                    ),
                    ApcomplexMath.pow (vector.modulus (), -2)
                )
            );
        }
        
        public ProjectionMatrix (Axis axis) {
            this ((ProjectionMatrix) new SquareMatrix (2));
            
            super.setEntry (Apcomplex.ONE, (axis == Axis.X) ? (new int [] {0, 0}) : (new int [] {1, 1}));
        }
        
        public ProjectionMatrix (ProjectionMatrix projectionMatrix) {
            super (projectionMatrix);
        }
        
        @Override
        public void setEntry (Apcomplex entry, int... position) {
            return;
        }
        
        @Override
        public boolean isIdempotent () {
            return true;
        }
        
        @Override
        public boolean isSymmetric () {
            return true;
        }
    }
}
