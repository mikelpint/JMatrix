package org.jmatrix.MatrixTypes;

import java.util.ArrayList;

import org.apfloat.Apcomplex;

public class RowVector extends Matrix {
    protected Matrix originalMatrix;
    
    public RowVector (int nElements) {
        super (1, nElements);
        
        this.setOriginalMatrix (null);
    }
    
    public RowVector (int nElements, ArrayList <ArrayList <Apcomplex>> vector) {
        super (1, nElements, vector);
        
        this.setOriginalMatrix (null);
    }
    
    public RowVector (RowVector rowVector) {
        super (rowVector);
        
        this.setOriginalMatrix (rowVector.originalMatrix);
    }
    
    @Override
    public int getDimension1 () {
        return 1;
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
        this.setEntry (entry, 0, element [0]);
    }
    
    // No sé por qué no va
    @Override
    public Vector transpose () {
//        return new Vector (this.dimension2, super.transpose ().matrix);
        return null;
    }
    
    @Override
    public Vector transjugate () {
        return new Vector (this.dimension2, super.transjugate ().matrix);
    }
    
    @Override
    public Vector vectorization () {
        return this.transpose ();
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
        return this;
    }
    
    @Override
    public HalfVector halfVectorization () {
        return null;
    }
}
