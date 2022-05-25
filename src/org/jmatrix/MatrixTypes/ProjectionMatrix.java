package org.jmatrix.MatrixTypes;

import java.util.ArrayList;
import java.util.Arrays;

import org.apfloat.Apcomplex;
import org.apfloat.ApcomplexMath;
import org.apfloat.Apfloat;
import org.jmatrix.MatrixOps.MatrixOps;

public final class ProjectionMatrix extends LinearTransformation {
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
        
        this.setEntry (Apcomplex.ONE, (axis == Axis.X) ? (new int [] {0, 0}) : (new int [] {1, 1}));
    }
    
    public ProjectionMatrix (ProjectionMatrix projectionMatrix) {
        super (projectionMatrix);
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