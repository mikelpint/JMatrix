package org.jmatrix.MatrixTypes;


import org.apfloat.Apcomplex;

import org.jmatrix.MatrixOps.*;

public final class DuplicationMatrix extends Matrix {
    public DuplicationMatrix (int dimension1) {
        super ((dimension1 * (dimension1 + 1)) / 2, dimension1 * dimension1);
        
        for (int column = 0; column < dimension1; column++) {
            for (int row = 0; row < dimension1; row++) {
                Matrix matrixT = new Matrix (dimension1, dimension1);
                matrixT.setEntry (Apcomplex.ONE, row, column);
                matrixT.setEntry (Apcomplex.ONE, column, row);

                this.setMatrix (
                    MatrixOps.matrixAddition (
                        this, 
                        MatrixOps.matrixMultiplication (
                            new CanonicalVector (
                                (dimension1 * (dimension1 + 1)) / 2,
                                (column * dimension1) + row - ((column * (column + 1)) / 2)
                            ),
                            matrixT.columnMajorVectorization ().transpose ()
                        )
                    ).matrix
                );
            }
        }
        
        this.setDimension1 (dimension1 * dimension1);
        this.setDimension2 ((dimension1 * (dimension1 + 1)) / 2);
        this.setMatrix (this.transpose ().matrix);
    }
    
    public DuplicationMatrix (DuplicationMatrix duplicationMatrix) {
        super (duplicationMatrix);
    }
}