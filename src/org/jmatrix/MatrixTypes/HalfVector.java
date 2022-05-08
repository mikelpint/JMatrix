package org.jmatrix.MatrixTypes;

import java.util.ArrayList;

import org.apfloat.Apcomplex;
import org.jmatrix.MatrixOps.MatrixOps;

final public class HalfVector extends Vector {
    public HalfVector (SquareMatrix matrix) {
        super ((matrix.dimension1 * matrix.dimension1 + 1) / 2);
        
        for (int row = 0, vechElement = 0; row < matrix.dimension1; row++) {
            for (int column = 0; column <= row; column++, vechElement++) {
                super.setEntry (matrix.matrix.get (row).get (column), vechElement);
            }
        }
        
        this.setOriginalMatrix (matrix);
    }
    
    @Override
    public void setMatrix (ArrayList <ArrayList <Apcomplex>> matrix) {
        return;
    }
    
    @Override
    public void setEntry (Apcomplex entry, int... position) {
        return;
    }
    
    public HalfVector (HalfVector halfVector) {
        super (halfVector);
    }
    
    @Override
    public Vector vectorization () {
        return (Vector) MatrixOps.matrixMultiplication (new DuplicationMatrix (this.getOriginalMatrix ().dimension1), (Matrix) this);
    }
    
    @Override
    public Vector columnMajorVectorization () {
        return this.vectorization ();
    }
    
    @Override
    public HalfVector halfVectorization () {
        return this;
    }
}