package org.jmatrix.MatrixTypes;

import java.util.ArrayList;

import org.apfloat.Apcomplex;
import org.jmatrix.MatrixOps.MatrixOps;

public final class EliminationMatrix extends Matrix {
    public EliminationMatrix (int dimension1) {
        super ((dimension1 * (dimension1 + 1)) / 2, dimension1 * dimension1);
        
        for (int column = 0; column < dimension1; column++) {
            for (int row = 0; row < dimension1; row++) {
                this.setMatrix (
                    MatrixOps.matrixAddition (
                        this, MatrixOps.kroneckerProduct (
                            new CanonicalVector (
                                (dimension1 * (dimension1 + 1)) / 2,
                                (column * dimension1) + row - ((column * (column + 1)) / 2)
                            ),
                            MatrixOps.kroneckerProduct (
                                new CanonicalVector (dimension1, column),
                                new CanonicalVector (dimension1, row)
                            )
                        )
                    ).matrix
                );
            }
        }
    }
    
    public EliminationMatrix (EliminationMatrix eliminationMatrix) {
        super (eliminationMatrix);
    }
}
