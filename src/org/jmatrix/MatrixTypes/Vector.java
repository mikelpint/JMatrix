package org.jmatrix.MatrixTypes;

import java.util.ArrayList;

import org.apfloat.Apcomplex;

public class Vector extends Matrix {
    Matrix originalMatrix;
    
    public Vector (int nElements) {
        super (nElements, 1);
        
        this.setOriginalMatrix (null);
    }
    
    public Vector (int nElements, ArrayList <ArrayList <Apcomplex>> vector) {
        super (nElements, 1, vector);
        
        this.setOriginalMatrix (null);
    }
    
    public Vector (Matrix matrix) {
        super (matrix.dimension1 * matrix.dimension2, 1, matrix.vectorization ().matrix);
        
        this.setOriginalMatrix (matrix);
    }

    public Vector (Vector vector) {
        super (vector);
        
        this.setOriginalMatrix (vector.getOriginalMatrix ());
    }
    
    @Override
    public int getDimension2 () {
        return 1;
    }
    
    @Override
    public void setDimension2 (int dimension2) {
        return;
    }
    
    public Matrix getOriginalMatrix () {
        return this.originalMatrix;
    }
    
    public void setOriginalMatrix (Matrix matrix) {
        this.originalMatrix = matrix;
    }
    
    @Override
    public Apcomplex getEntry (int... element) {
        return super.getEntry (0, element [0]);
    }
    
    @Override
    public void setEntry (Apcomplex entry, int... element) {
        super.setEntry (entry, 0, element [0]);
    }
    
    @Override
    public RowVector transpose () {
        return (RowVector) super.transpose ();
    }
    
    @Override
    public RowVector transjugate () {
        return (RowVector) super.transjugate ();
    }
    
    @Override
    public Vector vectorization () {
        return this;
    }
    
    @Override
    public Vector columnMajorVectorization () {
        if (this.getOriginalMatrix () == null) {
            return null;
        }
        
        return this.originalMatrix.columnMajorVectorization ();
    }
    
    @Override
    public RowVector toRowVector () {
        return this.transpose ();
    }
    
    @Override
    public HalfVector halfVectorization () {
        return null;
    }
}
