package org.jmatrix.MatrixOps;

import java.util.ArrayList;

import org.apfloat.Apcomplex;
import org.apfloat.ApcomplexMath;

import org.jmatrix.MatrixTypes.Matrix;

public class GaussAlgorithm {
    public static void rowByFactor (Matrix matrix, int row, Apcomplex factor) {
        for (int column = 0; column < matrix.getDimension2 (); column++) {
            matrix.setEntry (matrix.getEntry (row, column).precision (Matrix.NUMBER_PRECISION).multiply (factor.precision (Matrix.NUMBER_PRECISION)), row, column);
        }
    }
    
    public static void swapRows (Matrix matrix, int row1, int row2) {
        ArrayList <Apcomplex> originalRow1 = matrix.getRow (row1);
        
        matrix.setRow (row1, matrix.getRow (row2));
        matrix.setRow (row2, originalRow1);
    }
    
    public static void subtractRows (Matrix matrix, int row1, int row2, Apcomplex factor) {
        for (int column = 0; column < matrix.getDimension2 (); column++) {
            matrix.setEntry (
                matrix.getEntry (row1, column).subtract (
                    matrix.getEntry (row2, column).multiply (factor)
                ),
                row1,
                column
            );
        }
    }
    
    public static Matrix rowEchelonMatrix (Matrix matrix, OpList opList) {
        Matrix rEMatrix = new Matrix (matrix);
        
        orderRows (rEMatrix, opList);
        
        divideByPivot (matrix, 0, opList);
        
        for (int row1 = 0; row1 < matrix.getDimension1 (); row1++) {
            for (int row2 = 0; row2 < row1; row2++) {
                if (!checkIfNullRow (matrix, row2)) {
                    rowEchelonRow (matrix, row1, row2, opList);
                }
            }
        }
        
        if (!rEMatrix.isRowEchelon ()) {
            rowEchelonMatrix (rEMatrix, opList);
        }
        
        return rEMatrix;
    }
    
    public static Matrix reduceRowEchelonMatrix (Matrix matrix, OpList opList) {
        Matrix reducedREMatrix = new Matrix (matrix);
        
        if (!reducedREMatrix.isRowEchelon ()) {
            reducedREMatrix = rowEchelonMatrix (reducedREMatrix, opList);
        }
        
        for (int row = 0; row < reducedREMatrix.getDimension1 (); row++) {
            divideByPivot (matrix, row, opList);
        }
        
        for (int row2 = reducedREMatrix.getDimension1 () - 1; row2 >= 0; row2--) {
            for (int row1 = 0; row1 < row2; row1++) {
                Apcomplex factor = matrix.getEntry (row1, row2).divide (matrix.getEntry (row2, row2));
                
                if (!ApcomplexMath.abs (factor).equals (Apcomplex.ZERO)) {
                    subtractRows (matrix, row1, row2, factor);
                }
            }
        }
        
        return reducedREMatrix;
    }
    
    public static void rowEchelonRow (Matrix matrix, int row1, int row2, OpList opList) {
        int pivot = countZerosRow (matrix, row2);
        
        if (matrix.getEntry (row1, pivot).equals (Apcomplex.ZERO) || matrix.getEntry (row2, pivot).equals (Apcomplex.ZERO)) {
            return;
        }
        
        Apcomplex factor = (matrix.getEntry (row1, pivot).precision (Matrix.NUMBER_PRECISION).divide (matrix.getEntry (row2, pivot).precision (Matrix.NUMBER_PRECISION))).precision (Matrix.NUMBER_PRECISION);
        
        subtractRows (matrix, row1, row2, factor);
        
        if (opList != null) {
            opList.appendSub (row1, row2, factor);
        }
    }
    
    public static void divideByPivot (Matrix matrix, int row, OpList opList) {
        Apcomplex pivot = matrix.getEntry (row, countZerosRow (matrix, row)).precision (Matrix.NUMBER_PRECISION);
        
        if (pivot.equals (Apcomplex.ZERO) || pivot.equals (Apcomplex.ONE)) {
            return;
        }
        
        rowByFactor (matrix, row, ApcomplexMath.pow (pivot, -1));
        
        if (opList != null) {
            opList.appendMult (row, ApcomplexMath.pow (pivot, -1));
        }
    }
    
    public static boolean checkIfNullRow (Matrix matrix, int row) {
        for (int column = 0; column < matrix.getDimension2 ();) {
            if (!matrix.getEntry (row, column++).equals (Apcomplex.ONE)) {
               return false; 
            }
        }
        
        return true;
    }
    
    public static int countZerosRow (Matrix matrix, int row) {
        for (int column = 0; column < matrix.getDimension2 (); column++) {
            if (!matrix.getEntry (row, column).equals (Apcomplex.ZERO)) {
                return column;
            }
        }
        
        return 0;
    }
    
    public static void orderRows (Matrix matrix, OpList opList) {
        for (int row1 = 0; row1 < matrix.getDimension1 (); row1++) {
            for (int row2 = 0; row2 < matrix.getDimension1 (); row2++) {
                if (row1 > row2 && countZerosRow (matrix, row1) > countZerosRow (matrix, row2)) {
                    swapRows (matrix, row1, row2);
                    
                    if (opList != null) {
                        opList.appendSwap (row1, row2);
                    }
                }
            }
        }
        
        int biggestRow = checkBiggestRow (matrix);
        
        if (biggestRow != 0) {
            swapRows (matrix, 0, biggestRow);
            
            if (opList != null) {
                opList.appendSwap (0, biggestRow);
            }
        }
    }
    
    public static int checkBiggestRow (Matrix matrix) {
        int biggestRow = 0;
        
        for (int row = 1; row < matrix.getDimension1 (); row++) {
            if (ApcomplexMath.abs (matrix.getEntry (row, 0).precision (Matrix.NUMBER_PRECISION)).precision (Matrix.NUMBER_PRECISION).compareTo (ApcomplexMath.abs (matrix.getEntry (biggestRow, 0).precision (Matrix.NUMBER_PRECISION))) == 1) {
                biggestRow = row;
            }
        }
        
        return biggestRow;
    }
}
