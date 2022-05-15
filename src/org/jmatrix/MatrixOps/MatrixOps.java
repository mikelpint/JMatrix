package org.jmatrix.MatrixOps;

import java.util.ArrayList;

import org.apfloat.Apcomplex;
import org.apfloat.ApcomplexMath;
import org.apfloat.Apfloat;
import org.apfloat.Apint;
import org.apfloat.ApintMath;

import org.jmatrix.MatrixTypes.*;
import org.jmatrix.internals.*;

public class MatrixOps {
    public static ArrayList <Matrix> splitMatrix (Matrix matrix, int nSplits) {
        if (
            matrix.getMatrix () == null ||
            (nSplits != 1 && (nSplits & 1) == 1) ||
            nSplits > (matrix.getDimension1 () * matrix.getDimension2 ()) ||
            (matrix.getDimension1 () * matrix.getDimension2 ()) % nSplits != 0
        ) {
            return null;
        }
        
        ArrayList <Matrix> splits = new ArrayList <Matrix> ();
        
        if (nSplits == 1) {
            splits.add (matrix);
            return splits;
        }
        
        int nVerticalSplits = nSplits / 2;
        int nHorizontalSplits = nSplits / nVerticalSplits;
        
        int splitDimension1 = matrix.getDimension1 () / nVerticalSplits;
        int splitDimension2 = matrix.getDimension2 () / nHorizontalSplits;
        
        for (int splitsRow = 0; splitsRow < nVerticalSplits; splitsRow++) {
            for (int splitsColumn = 0; splitsColumn < nHorizontalSplits; splitsColumn++) {
                splits.add (new Matrix (splitDimension1, splitDimension2));
                
                for (int row = 0; row < splitDimension1; row++) {
                    for (int column = 0; column < splitDimension2; column++) {
                        splits.get (splitsRow * nHorizontalSplits + splitsColumn).setEntry (matrix.getEntry (row + (splitsRow * splitDimension1), column + (splitsColumn * splitDimension2)), row, column);
                    }
                }
            }
        }
        
        return splits;
    }
    
    public static void insertMatrix (Matrix matrixA, Matrix matrixB, int insertionRow, int insertionColumn) {
        if (insertionRow + matrixB.getDimension2 () > matrixA.getDimension1 () || insertionColumn + matrixB.getDimension2 () > matrixA.getDimension2 ()) {
            return;
        }
        
        for (int row = 0; row < matrixB.getDimension1 (); row++) {
            for (int column = 0; column < matrixB.getDimension2 (); column++) {
                matrixA.setEntry (matrixB.getEntry (row, column), row + insertionRow, column + insertionColumn);
            }
        }
    }
    
    public static Matrix matrixAdditionSubtraction (Matrix matrixA, Matrix matrixB, boolean addition) {
        if (matrixA.getDimension1 () != matrixB.getDimension1 () || matrixA.getDimension2 () != matrixB.getDimension2 ()) {
            return null;
        }
        
        Matrix matrixC = new Matrix (matrixA);
        
        for (int row = 0; row < matrixA.getDimension1 (); row++) {
            for (int column = 0; column < matrixA.getDimension2 (); column++) {
                if (addition) {
                    matrixC.setEntry (matrixC.getEntry (row, column).add (matrixB.getEntry (row, column)), row, column);
                }
                
                else {
                    matrixC.setEntry (matrixC.getEntry (row, column).subtract (matrixB.getEntry (row, column)), row, column);
                }
            }
        }
        
        return matrixC;
    }
    
    public static Matrix matrixAddition (Matrix matrixA, Matrix matrixB) {
        return matrixAdditionSubtraction (matrixA, matrixB, true);
    }
    
    public static Matrix matrixSubtraction (Matrix matrixA, Matrix matrixB) {
        return matrixAdditionSubtraction (matrixA, matrixB, false);
    }
    
    public static Matrix scalarMultiplication (Matrix matrix, Apcomplex scalar) {
        Matrix newMatrix = new Matrix (matrix.getDimension1 (), matrix.getDimension2 ());
        
        for (int row = 0; row < matrix.getDimension1 (); row++) {
            for (int column = 0; column < matrix.getDimension2 (); column++) {
                newMatrix.setEntry (matrix.getEntry (row, column).multiply (scalar), row, column);
            }
        }
        
        return newMatrix;
    }
    
    public static Matrix hadamardProduct (Matrix matrixA, Matrix matrixB) {
        if (matrixA.getDimension1 () != matrixB.getDimension1 () || matrixA.getDimension2 () != matrixB.getDimension2 ()) {
            return null;
        }
        
        Matrix matrixC = new Matrix (matrixA.getDimension1 (), matrixA.getDimension2 ());
        
        for (int row = 0; row < matrixA.getDimension1 (); row++) {
            for (int column = 0; column < matrixA.getDimension2 (); column++) {
                matrixC.setEntry (matrixA.getEntry (row, column).multiply (matrixB.getEntry (row, column)), row, column);
            }
        }
        
        return matrixC;
    }
    
    public static Apcomplex innerProduct (Matrix vectorA, Matrix vectorB) {
        if (
            !(vectorA instanceof Vector && vectorB instanceof Vector && vectorA.getDimension1 () != vectorB.getDimension1 ()) ||
            !(vectorB instanceof RowVector && vectorB instanceof RowVector && vectorA.getDimension2 () != vectorB.getDimension2 ())
        ) {
            return null;
        }
        
        Apcomplex result = Apcomplex.ZERO;
        
        for (int element = 0, nElements = (vectorA instanceof Vector) ? vectorA.getDimension1 () : vectorA.getDimension2 (); element < nElements; element++) {
            result = result.add (vectorA.getEntry (element).multiply (vectorB.getEntry (element).conj ()));
        }
        
        return result;
    }
    
    public static Apcomplex frobeniusInnerProduct (Matrix matrixA, Matrix matrixB) {
        if (matrixA.getDimension1 () != matrixB.getDimension2 () || matrixA.getDimension2 () != matrixB.getDimension2 ()) {
            return null;
        }
        
        return calculateTrace ((SquareMatrix) matrixMultiplication (matrixA.transjugate (), matrixB));
    }
    
    public static Matrix matrixMultiplication (Matrix matrixA, Matrix matrixB) {
        if (matrixA.getMatrix () == null || matrixB.getMatrix () == null || matrixA.getDimension2 () != matrixB.getDimension1 ()) {
            return null;
        }
        
        if (matrixA instanceof SquareMatrix && ((SquareMatrix) matrixA).isIdentityMatrix ()) {
            matrixA = (IdentityMatrix) matrixA;
            return matrixB;
        }
        
        if (matrixB instanceof SquareMatrix && ((SquareMatrix) matrixB).isIdentityMatrix ()) {
            matrixB = (IdentityMatrix) matrixB;
            return matrixA;
        }
        
        if (matrixA.equals (matrixB) && ((SquareMatrix) matrixA).isIdempotent ()) {
            return matrixA;
        }
        
        if (matrixA.equals (matrixB) && ((SquareMatrix) matrixA).isInvolutory ()) {
            return new IdentityMatrix (matrixA.getDimension1 ());
        }
        
        return MatrixMultiplication.strassenMatrixMultiplication (matrixA, matrixB);
    }
    
    public static SquareMatrix matrixPower (SquareMatrix matrix, int power) {
        if (matrix instanceof IdentityMatrix) {
            return matrix;
        }
    
        if (matrix instanceof ExchangeMatrix) {
            if (power % 2 == 0) {
                return matrix;
            }
            
            else {
                return new IdentityMatrix (matrix.getDimension1 ());
            }
        }
        
        SquareMatrix originalMatrix = new SquareMatrix (matrix);
        
        if (power == 0) {
            return new IdentityMatrix (matrix.getDimension1 ());
        }
        
        if (power < 0) {
            originalMatrix = invertMatrix (originalMatrix);
            
            
            if (originalMatrix == null) {
                return null;
            }
            
            power = Math.abs (power);
        }
        
        SquareMatrix newMatrix = new SquareMatrix (originalMatrix);
        
        for (int remainingMultiplications = power; remainingMultiplications > 1; remainingMultiplications--) {
            newMatrix = (SquareMatrix) matrixMultiplication (newMatrix, originalMatrix);
        }
        
        return newMatrix;
    }
    
    public static Matrix kroneckerProduct (Matrix matrixA, Matrix matrixB) {
        Matrix matrixC = new Matrix (matrixA.getDimension1 () * matrixB.getDimension1 (), matrixA.getDimension2 () * matrixB.getDimension2 ());
        
        for (int row = 0; row < matrixA.getDimension1 () * matrixB.getDimension1 (); row++) {
            for (int column = 0; column < matrixA.getDimension2 () * matrixB.getDimension2 (); column++) {
                matrixC.setEntry (
                    matrixA.getEntry (
                        (int) (row / matrixB.getDimension1 ()),
                        (int) (column / matrixB.getDimension2 ())
                    ).multiply (
                        matrixB.getEntry (
                            row % matrixB.getDimension1 (),
                            column % matrixB.getDimension2 ()
                        )
                    ),
                    row,
                    column
                );
            }
        }
        
        return matrixC;
    }
    
    public static Matrix kroneckerSum (Matrix matrixA, Matrix matrixB) {
        return matrixAddition (
            kroneckerProduct (matrixA, new IdentityMatrix (matrixA.getDimension1 ())),
            kroneckerProduct (matrixB, new IdentityMatrix (matrixB.getDimension2 ()))
        );
    }
    
    public static Matrix directSum (Matrix matrixA, Matrix matrixB) {
        Matrix matrixC = new Matrix (matrixA.getDimension1 () + matrixB.getDimension1 (), matrixA.getDimension2 () + matrixB.getDimension2 ());
        
        insertMatrix (matrixC, matrixA, 0, 0);
        insertMatrix (matrixC, matrixB, matrixB.getDimension1 (), matrixB.getDimension2 ());
        
        return matrixC;
    }
    
    public static int calculateRank (Matrix matrix, OpList opList) {   
        if (matrix instanceof ZeroMatrix || matrix.equals (new ZeroMatrix (matrix.getDimension1 (), matrix.getDimension2 ()))) {
            return 0;
        }
        
        if (
            matrix instanceof ScalarMatrix ||
            matrix instanceof ExchangeMatrix ||
            matrix instanceof HilbertMatrix ||
            matrix instanceof CommutationMatrix
        ) {
            return matrix.getDimension1 ();
        }
        
        if (matrix instanceof ShiftMatrix) {
            return (matrix.getDimension1 () - 1);
        }
        
        if (matrix instanceof CanonicalVector) {
            return 1;
        }
        
        if (matrix instanceof Vector || matrix instanceof RowVector) {
            for (int element = 0, nElements = (matrix instanceof Vector) ? matrix.getDimension1 () : matrix.getDimension2 (); element < nElements; element++) {
                if (!matrix.getClass ().cast (matrix).getEntry (element).equals (Apcomplex.ZERO)) {
                    return 1;
                }
            }
            
            return 0;
        }
        
        int rank = matrix.getDimension1 ();
        
        Matrix equivalentMatrix = GaussAlgorithm.rowEchelonMatrix (matrix, opList);
        
        for (int row = 0; row < equivalentMatrix.getDimension1 (); row++) {
            if (GaussAlgorithm.checkIfNullRow (equivalentMatrix, row)) {
                rank--;
            }
        }
        
        return rank;
    }
    
    public static Apcomplex calculateTrace (SquareMatrix matrix) {
        if (matrix instanceof ShiftMatrix || (Matrix) matrix instanceof ZeroMatrix || matrix.equals (new ZeroMatrix (matrix.getDimension1 (), matrix.getDimension1 ()))) {
            return Apcomplex.ZERO;
        }
        
        if (matrix instanceof PascalMatrix.SymmetricPascalMatrix) {
            Apint trace = Apint.ZERO;
            
            for (int k  = 0; k <= matrix.getDimension1 () - 1; k++) {
                trace = trace.add (
                    ApintMath.factorial (2 * k).divide (
                        ApintMath.pow (ApintMath.factorial (k), 2)
                    )
                );
            }
            
            return (Apcomplex) trace;
        }
        
        if (matrix instanceof LehmerMatrix || matrix instanceof PascalMatrix.LowerPascalMatrix || matrix instanceof PascalMatrix.UpperPascalMatrix) {
            return new Apcomplex (new Apfloat (matrix.getDimension1 ()));
        }
        
        if (matrix instanceof ScalarMatrix) {
            if (matrix instanceof IdentityMatrix) {
                return (Apcomplex) new Apfloat (matrix.getDimension1 ());
            }
            
            return matrix.getEntry (0, 0).multiply ((Apcomplex) new Apfloat (matrix.getDimension1 ()));
        }
        
        if (matrix instanceof ExchangeMatrix) {
            return (Apcomplex) new Apint (matrix.getDimension1 () % 2);
        }
        
        Apcomplex result = Apcomplex.ZERO;
        
        for (int element = 0; element < matrix.getDimension1 (); element++) {
            result = result.add (matrix.getEntry (element, element));
        }
        
        return result;
    }
    
    public static Apcomplex calculateDeterminant (SquareMatrix matrix) {
        if (matrix instanceof ShiftMatrix) {
            return Apcomplex.ZERO;
        }
        
        if (matrix instanceof PascalMatrix) {
            return Apcomplex.ONE;
        }
        
        if (matrix instanceof ScalarMatrix) {
            if (matrix instanceof IdentityMatrix) {
                return Apcomplex.ONE;
            }
            
            return matrix.getEntry (0, 0).multiply ((Apcomplex) new Apfloat (matrix.getDimension1 ()));
        }
        
        if (matrix instanceof HilbertMatrix) {
            Apint determinantReciprocal = ApintMath.factorial (matrix.getDimension1 ());
            
            for (int i = 1; i < (2 * matrix.getDimension1 ()); i++) {
                determinantReciprocal = determinantReciprocal.multiply (JMath.binomialCoefficient (i, i / 2));
            }
            
            return ApcomplexMath.pow ((Apcomplex) determinantReciprocal, -1);
        }
        
        if (matrix instanceof ExchangeMatrix) {
            return (Apcomplex) ApintMath.pow (Apint.ONE.negate (), (matrix.getDimension1 () * (matrix.getDimension1 () - 1) / 2));
        }
        
        if (matrix instanceof RedhefferMatrix) {
            return (Apcomplex) JMath.mertens (matrix.getDimension1 ());
        }
        
        OpList opList = new OpList ();
        
        if (calculateRank (matrix, opList) < matrix.getDimension1 ()) {
            return Apcomplex.ZERO;
        }
        
        Matrix equivalentMatrix = new Matrix (matrix);
        opList.apply (equivalentMatrix);
        
        Apcomplex determinant = Apcomplex.ONE;
        
        for (int entry = 0; entry < matrix.getDimension1 (); entry++) {
            determinant = determinant.multiply (equivalentMatrix.getEntry (entry, entry));
        }
        
        int swapRowsCount = 0;
        
        for (OpList.Op op : opList.getOps ()) {
            if (op instanceof OpList.Op.MultOp) {
                determinant.multiply (((OpList.Op.MultOp) op).getFactor ());
            }
            
            else if (op instanceof OpList.Op.SwapOp) {
                swapRowsCount++;
            }
        }
        
        if ((swapRowsCount & 1) == 1) {
            determinant.negate ();
        }
        
        return determinant;
    }
    
    public static SquareMatrix invertMatrix (SquareMatrix matrix) {
        if (matrix instanceof ScalarMatrix) {
            if (matrix instanceof IdentityMatrix) {
                return matrix;
            }
            
            return new ScalarMatrix (matrix.getDimension1 (), ApcomplexMath.pow (matrix.getEntry (0, 0), -1));
        }
        
        if (matrix instanceof CommutationMatrix || matrix instanceof ExchangeMatrix) {
            return matrix;
        }
        
        if (matrix instanceof HilbertMatrix) {
            int n = matrix.getDimension1 ();
            
            SquareMatrix inverseMatrix = new SquareMatrix (n);
            
            for (int row = 0; row <= n; row++) {
                for (int column = 0; column <= n; column++) {
                    inverseMatrix.setEntry (
                        (Apcomplex) ApintMath.pow (Apint.ONE.negate (), row + column + 2).multiply (
                            new Apint (row + column + 1).multiply (
                                JMath.binomialCoefficient (n + row, n - column - 1)
                            ).multiply (
                                JMath.binomialCoefficient (n + column, n - row - 1)
                            ).multiply (
                                ApintMath.pow (JMath.binomialCoefficient (row + column, row), 2)
                            )
                        ),
                        row,
                        column
                    );
                }
            }
            
            return inverseMatrix;
        }
        
        OpList opList = new OpList ();
        
        if (calculateRank (matrix, opList) < matrix.getDimension1 ()) {
            return null;
        }
        
        SquareMatrix inverseMatrix = new SquareMatrix (matrix);
        opList.apply (inverseMatrix);
        
        GaussAlgorithm.reduceRowEchelonMatrix (inverseMatrix, opList);
        
        opList.apply (inverseMatrix);
        
        return inverseMatrix;
    }
}