package org.jmatrix.MatrixTypes;

import java.util.ArrayList;
import java.util.HashSet;

import org.apfloat.Apcomplex;
import org.apfloat.ApcomplexMath;
import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.apfloat.Apint;
import org.jmatrix.MatrixOps.MatrixOps;

public class Matrix implements Vectorizable, MatrixProperties {
    final public static int maxEntries = 10000;
    
    final static long maxRandomDigits = 50000;
    final static Apint maxRange = new Apint (maxRandomDigits, 2);
    
    protected int dimension1;
    protected int dimension2;
    protected ArrayList <ArrayList <Apcomplex>> matrix = new ArrayList <ArrayList <Apcomplex>> ();
    
    public Matrix (int dimension1, int dimension2) {
        super ();

        this.setDimension1 (dimension1);
        this.setDimension2 (dimension2);
        this.setMatrix (generateEmptyMatrix (dimension1, dimension2));
    }

    public Matrix (int dimension1, int dimension2, ArrayList <ArrayList <Apcomplex>> matrix) {
        super ();

        this.setDimension1 (dimension2);
        this.setDimension2 (dimension2);
        this.setMatrix (matrix);
    }
    
    public Matrix (Matrix matrix) {
        super ();
        
        this.dimension1 = matrix.getDimension1 ();
        this.dimension2 = matrix.getDimension2 ();
        this.matrix = matrix.getMatrix ();
    }

    public int getDimension1 () {
        return dimension1;
    }

    public void setDimension1 (int dimension1) {
        if (dimension1 > 0 && dimension1 <= maxEntries) {
            this.dimension1 = dimension1;
        }
        
        else {
            this.dimension1 = -1;
        }
    }

    public int getDimension2 () {
        return dimension2;
    }

    public void setDimension2 (int dimension2) {
        if (dimension2 > 0 && dimension2 <= maxEntries) {
            this.dimension2 = dimension2;
        }
        
        else {
            this.dimension2 = -1;
        }
    }

    public ArrayList <ArrayList <Apcomplex>> getMatrix () {
        return matrix;
    }
    
    public void setMatrix (ArrayList <ArrayList <Apcomplex>> matrix) {
        if (this.dimension1 == -1 || this.dimension2 == -1 || matrix.size () != this.dimension1) {
            return;
        }
        
        for (int row = 0; row < matrix.size (); row++) {
            if (matrix.get (row).size () != this.dimension2) {
                return;
            }
        }
        
        this.matrix = matrix;
    }
    
    public ArrayList <Apcomplex> getRow (int row) {
        if (row < 0 || row >= this.dimension1) {
            return null;
        }
        
        return this.matrix.get (row);
    }
    
    public void setRow (int rowIndex, ArrayList <Apcomplex> row) {
        if (row == null || row.size () != this.dimension2) {
            return;
        }
        
        this.matrix.set (rowIndex, row);
    }
    
    public Apcomplex getEntry (int... position) {
        if (position.length != 2 || position [0] < 0 || position [0] >= this.dimension1 || position [1]  < 0 || position [1] >= this.dimension1) {
            return null;
        }
        
        return this.matrix.get (position [0]).get (position [1]);
    }
    
    public void setEntry (Apcomplex entry, int... position) {
        if (position.length != 2 || position [0] < 0 || position [0] >= this.dimension1 || position [1]  < 0 || position [1] >= this.dimension1 || entry == null) {
            return;
        }
        
        this.matrix.get (position [0]).set (position [1], entry);
    }
    
    public boolean equals (Matrix matrix) {
        if (this.dimension1 != matrix.dimension1 || this.dimension2 != matrix.dimension2) {
            return false;
        }
        
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                if (!this.matrix.get (row).get (column).equals (matrix.matrix.get (row).get (column))) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    public static ArrayList <ArrayList <Apcomplex>> generateEmptyMatrix (int dimension1, int dimension2) {
        ArrayList <ArrayList <Apcomplex>> matrixEntries = new ArrayList <ArrayList <Apcomplex>> ();
        
        for (int row = 0; row < dimension1; row++) {
            matrixEntries.add (new ArrayList <Apcomplex> ());
            
            for (int column = 0; column < dimension2; column++) {
                matrixEntries.get (row).add (Apcomplex.ZERO);
            }
        }
        
        return matrixEntries;
    }
    
    public void randomizeElements (Apint range, boolean complex) {
        Apcomplex entry = new Apcomplex (Apfloat.ZERO, Apfloat.ZERO);
        
        if (range == null) {
            range = maxRange;
        }
        
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                entry.add (new Apcomplex ((Apint) ApfloatMath.random (maxRandomDigits).mod (range.multiply (new Apint (2)).add (Apfloat.ONE)).subtract (range)));
                
                if (complex) {
                    entry.add (new Apcomplex ((Apint) ApfloatMath.random (maxRandomDigits).mod (range.multiply (new Apint (2)).add (Apfloat.ONE)).subtract (range)).multiply (Apcomplex.I));
                }  
            }
        }
    }
    
    @Override
    public Matrix transpose () {
        if (this.matrix == null) {
            return null;
        }

        Matrix matrixTranspose = new Matrix (this);
    
        for (int row = 0; row < this.dimension2; row++) {
            for (int column = 0; column < this.dimension1; column++) {
                matrixTranspose.setEntry (this.getEntry (column, row), row, column);
            }
        }
        
        return matrixTranspose;
    }
    
    @Override
    public Matrix transjugate () {
        if (this.matrix == null) {
            return null;
        }
        
        Matrix matrixTransjugate = this.transpose ();
        
        if (!this.isReal ()) {
            for (int row = 0; row < this.dimension2; row++) {
                for (int column = 0; column < this.dimension1; column++) {
                    matrixTransjugate.setEntry (matrixTransjugate.getEntry (row, column).conj (), row, column);
                }
            }
        }
        
        return matrixTransjugate;
    }
    
    @Override
    public Matrix negate () {
        if (this.matrix == null) {
            return null;
        }
        
        Matrix matrixNegate = new Matrix (this.dimension1, this.dimension2);
        
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                matrixNegate.setEntry (this.getEntry (row, column).negate (), row, column);
            }
        }
        
        return matrixNegate;
    }
    
    @Override
    public ArrayList <Matrix> submatrices () {
        ArrayList <Matrix> submatrices = new ArrayList <Matrix> ();
        
        HashSet <Integer> rowsToBeDeleted = new HashSet <Integer> ();
        HashSet <Integer> columnsToBeDeleted = new HashSet <Integer> ();

        for (int delRow = 0; delRow < this.dimension1; delRow++) {
            rowsToBeDeleted.add (delRow);
            
            for (int delColumn = 0; delColumn < this.dimension2; delColumn++) {
                columnsToBeDeleted.add (delColumn);
                
                ArrayList <ArrayList <Apcomplex>> submatrixEntries = new ArrayList <ArrayList <Apcomplex>> ();
                
                for (int row = 0; row < this.dimension1; row++) {
                    if (!rowsToBeDeleted.contains (row)) {
                        submatrixEntries.add (this.getRow (row));
                    }
                    
                    for (int column = 0; column < this.dimension2; column++) {
                        for (Integer noColumn : columnsToBeDeleted) {
                            submatrixEntries.remove ((int) noColumn);
                        }
                    }
                }
                
                submatrices.add (new Matrix (this.dimension1 - submatrixEntries.size (), this.dimension2 - submatrixEntries.size (), submatrixEntries));
                
                columnsToBeDeleted.clear ();
            }
        }
        
        return submatrices;
    }

    @Override
    public Vector vectorization () {
        if (this.matrix == null) {
            return null;
        }

        Vector vector = new Vector (this.dimension1 * this.dimension2);
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                vector.setEntry (this.getEntry (row, column), row * this.dimension2 + column);
            }
        }
        
        return vector;
    }
    
    @Override
    public Vector columnMajorVectorization () {
        if (this.matrix == null) {
            return null;
        }
        
        return (Vector) MatrixOps.matrixMultiplication (new CommutationMatrix (this.dimension1, this.dimension2), this.vectorization ());
    }
    
    @Override
    public RowVector toRowVector () {
        if (this.matrix == null) {
            return null;
        }

        ArrayList <ArrayList <Apcomplex>> rowVectorEntries = generateEmptyMatrix (1, this.dimension1 * this.dimension2);
        
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                rowVectorEntries.get (0).set (row * this.dimension2 + column, this.matrix.get (row).get (column));
            }
        }
        
        return new RowVector (this.dimension1 * this.dimension2, rowVectorEntries);
    }
    
    @Override
    public HalfVector halfVectorization () {
        return null;
    }
    
    @Override
    public boolean isReal () {
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                if (!this.getEntry (row, column).equals (Apfloat.ZERO)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isInteger () {
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                if (!this.getEntry (row, column).isInteger ()) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isSingular () {
        return !(MatrixOps.calculateRank (this, null) < this.dimension1);
    }
    
    @Override
    public boolean isRowEchelon () {
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                if (column < row && !this.getEntry (row, column).equals (Apcomplex.ZERO)) {
                   return false; 
                }
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isDiagonal () {
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                if (row != column && !this.getEntry (row, column).equals (Apcomplex.ZERO)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isBidiagonal () {
        return (this.isLowerBidiagonal () || this.isUpperBidiagonal ());
    }
    
    @Override
    public boolean isLowerBidiagonal () {
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                if (
                    (
                        this.getEntry (row, column).equals (Apcomplex.ZERO) && (row == column || row == column + 1)
                    ) ||
                    (
                        !this.getEntry (row, column).equals (Apcomplex.ZERO) && row != column + 1
                    )
                ) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isUpperBidiagonal () {
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                if (
                    (
                        this.getEntry (row, column).equals (Apcomplex.ZERO) && (row == column || row == column - 1)
                    ) ||
                    (
                        !this.getEntry (row, column).equals (Apcomplex.ZERO) && row != column - 1
                    )
                ) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isTridiagonal () {
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                if (
                    (
                        (
                            row == column - 1 ||
                            row == column ||
                            row == column + 1
                        ) &&
                        this.getEntry (row, column).equals (Apcomplex.ZERO)
                    ) ||
                    (
                        (
                            row != column - 1 ||
                            row != column ||
                            row != column + 1
                        ) &&
                        this.getEntry (row, column).equals (Apcomplex.ZERO)
                    )
                ) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isPentadiagonal () {
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                if (
                    (
                        (
                            row == column - 2 ||
                            row == column - 1 ||
                            row == column ||
                            row == column + 1 ||
                            row == column + 2
                        ) &&
                        this.getEntry (row, column).equals (Apcomplex.ZERO)
                    ) ||
                    (
                        (
                            row != column - 2 ||
                            row != column - 1 ||
                            row != column ||
                            row != column + 1 ||
                            row != column + 2
                        ) &&
                        this.getEntry (row, column).equals (Apcomplex.ZERO)
                    )
                ) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isToeplitz () {
        for (int row = 0; row < this.dimension1 - 1; row++) {
            for (int column = 0; column < this.dimension2 - 1; column++) {
                if (!this.getEntry ().equals (this.getEntry (row + 1, column + 1))) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isNonnegative () {
        if (!this.isReal ()) {
            return false;
        }
        
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                if (this.getEntry (row, column).real ().signum () == -1) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isPositive () {
        if (!this.isReal ()) {
            return false;
        }
        
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                if (this.getEntry (row, column).real ().signum () <= 0) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isMetzler () {
        if (!this.isReal ()) {
            return false;
        }
        
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                if (row != column && ApcomplexMath.abs (this.getEntry (row, column)).compareTo (Apfloat.ZERO) == -1) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isZ () {
        return this.negate ().isMetzler ();
    }
    
    @Override
    public boolean isL () {
        if (!this.isReal ()) {
            return false;
        }
        
        if (!this.isZ ()) {
            return false;
        }
        
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                if (row == column && ApcomplexMath.abs (this.getEntry (row, column)).compareTo (Apcomplex.ZERO) != 1) {
                    return false;
                }
            }
        }
        
        return true;
    }
}