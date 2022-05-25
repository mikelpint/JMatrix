package org.jmatrix.MatrixTypes;

public interface Vectorizable {
    public Vector vectorization ();
    public Vector columnMajorVectorization ();
    public RowVector toRowVector ();
    public HalfVector halfVectorization ();
}