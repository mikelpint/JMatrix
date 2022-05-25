package org.jmatrix.MatrixOps;

import java.util.ArrayList;

import org.apfloat.Apcomplex;

import org.jmatrix.MatrixTypes.*;

public class MatrixMultiplication {
    public static Matrix naiveMatrixMultiplication (Matrix matrixA, Matrix matrixB) {
        Matrix matrixC = new Matrix (matrixA.getDimension1 (), matrixB.getDimension2 ());
        
        for (int i = 0; i < matrixA.getDimension1 (); i++) {
            for (int j = 0; j < matrixB.getDimension2 (); j++) {
                Apcomplex entry = Apcomplex.ZERO;
                
                for (int k = 0; k < matrixA.getDimension2 (); k++) {
                    entry = entry.add (matrixA.getEntry (i, k).multiply (matrixB.getEntry (k, j)));
                }
                
                matrixC.setEntry (entry, i, j);
            }
        }
        
        return (matrixC.getDimension1 () == matrixC.getDimension2 ()) 
            ? new SquareMatrix (matrixC.getDimension1 (), matrixC.getMatrix ())
            : (
                (matrixC.getDimension2 () == 1)
                ? new Vector (matrixC.getDimension1 (), matrixC.getMatrix ())
                : (
                    (matrixC.getDimension1 () == 1)
                    ? new RowVector (matrixC.getDimension2 (), matrixC.getMatrix ())
                    : matrixC
                )
            )
        ;
    }
    
    public static Matrix strassenMatrixMultiplication (Matrix matrixA, Matrix matrixB) {
        int matrixADimension1 = matrixA.getDimension1 ();
        int matrixADimension2 = matrixA.getDimension2 ();
        int matrixBDimension2 = matrixB.getDimension2 ();
        
        if (matrixADimension1 < 32 && matrixADimension2 < 32 && matrixBDimension2 < 32) {
            return naiveMatrixMultiplication (matrixA, matrixB);
        }
        
        int n = nextPowerOfTwo (Math.max (matrixBDimension2, Math.max (matrixADimension1, matrixADimension2)));
        
        addPadding (matrixA, n, n);
        addPadding (matrixB, n, n);
        
        ArrayList <Matrix> abcd = MatrixOps.splitMatrix (matrixA, 4);
        ArrayList <Matrix> efgh = MatrixOps.splitMatrix (matrixB, 4);
        
        removePadding (matrixA, matrixADimension1, matrixADimension2);
        removePadding (matrixB, matrixADimension2, matrixBDimension2);
        
        Matrix p1 = strassenMatrixMultiplication (abcd.get (0), MatrixOps.matrixSubtraction (efgh.get (1), efgh.get (3)));
        Matrix p2 = strassenMatrixMultiplication (MatrixOps.matrixAddition (abcd.get (0), abcd.get (1)), efgh.get (3));
        Matrix p3 = strassenMatrixMultiplication (MatrixOps.matrixAddition (abcd.get (2), abcd.get (3)), efgh.get (0));
        Matrix p4 = strassenMatrixMultiplication (abcd.get (3), MatrixOps.matrixSubtraction (efgh.get (2), efgh.get (0)));
        Matrix p5 = strassenMatrixMultiplication (MatrixOps.matrixAddition (abcd.get (0), abcd.get (3)), MatrixOps.matrixAddition (efgh.get (0), efgh.get (3)));
        Matrix p6 = strassenMatrixMultiplication (MatrixOps.matrixSubtraction (abcd.get (1), abcd.get (3)), MatrixOps.matrixAddition (efgh.get (2), efgh.get (3)));
        Matrix p7 = strassenMatrixMultiplication (MatrixOps.matrixSubtraction (abcd.get (0), abcd.get (2)), MatrixOps.matrixAddition (efgh.get (0), efgh.get (1)));
        
        Matrix matrixCA = MatrixOps.matrixAddition (MatrixOps.matrixSubtraction (MatrixOps.matrixAddition (p5, p4), p2), p6);
        Matrix matrixCB = MatrixOps.matrixAddition (p1, p2);
        Matrix matrixCC = MatrixOps.matrixAddition (p3, p4);
        Matrix matrixCD = MatrixOps.matrixAddition (MatrixOps.matrixSubtraction (MatrixOps.matrixAddition (p1, p5), p3), p7);
        
        Matrix matrixC = new Matrix (n, n);
        MatrixOps.insertMatrix (matrixC, matrixCA, 0, 0);
        MatrixOps.insertMatrix (matrixC, matrixCB, 0, n / 2);
        MatrixOps.insertMatrix (matrixC, matrixCC, n / 2, 0);
        MatrixOps.insertMatrix (matrixC, matrixCD, n / 2, n / 2);
        removePadding (matrixC, matrixADimension1, matrixBDimension2);
        
        return matrixC;
    }
    
    public static int nextPowerOfTwo (int number) {
        return (int) Math.pow (2, (int) (Math.ceil (Math.log (number) / Math.log (2))));
    }
    
    public static void addPadding (Matrix matrix, int newDimension1, int newDimension2) {
        if (newDimension1 <= matrix.getDimension1 () && newDimension2 <= matrix.getDimension2 ()) {
            return;
        }
        
        ArrayList <ArrayList <Apcomplex>> paddedMatrixEntries = Matrix.generateEmptyMatrix (newDimension1, newDimension2);
        
        for (int row = 0; row < matrix.getDimension1 (); row++) {
            for (int column = 0; column < matrix.getDimension2 (); column++) {
                paddedMatrixEntries.get (row).set (column, matrix.getMatrix ().get (row).get (column));
            }
        }
        
        matrix.setDimension1 (newDimension1);
        matrix.setDimension2 (newDimension2);
        matrix.setMatrix (paddedMatrixEntries);
    }
    
    public static void removePadding (Matrix matrix, int newDimension1, int newDimension2) {
        if (newDimension1 >= matrix.getDimension1 () && newDimension2 >= matrix.getDimension2 ()) {
            return;
        }
        
        ArrayList <ArrayList <Apcomplex>> noPaddingMatrixEntries = Matrix.generateEmptyMatrix (newDimension1, newDimension2);
        
        for (int row = 0; row < newDimension1; row++) {
            for (int column = 0; column < newDimension2; column++) {
                noPaddingMatrixEntries.get (row).set (column, matrix.getMatrix ().get (row).get (column));
            }
        }
        
        matrix.setDimension1 (newDimension1);
        matrix.setDimension2 (newDimension2);
        matrix.setMatrix (noPaddingMatrixEntries);
    }
}