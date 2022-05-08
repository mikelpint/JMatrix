package org.jmatrix.MatrixTypes;

import java.util.ArrayList;

import org.apfloat.Apfloat;
import org.apfloat.Apcomplex;
import org.apfloat.ApcomplexMath;
import org.jmatrix.MatrixOps.MatrixOps;

public class SquareMatrix extends Matrix implements SquareProperties {
    public SquareMatrix (int dimension1) {
        super (dimension1, dimension1);
    }
    
    public SquareMatrix (int dimension1, ArrayList <ArrayList <Apcomplex>> matrix) {
        super (dimension1, dimension1, matrix);
    }
    
    public SquareMatrix (SquareMatrix matrix) {
        super (matrix);
    }
    
    @Override
    public SquareMatrix transpose () {
        return (SquareMatrix) super.transpose ();
    }
    
    @Override
    public SquareMatrix transjugate () {
        return (SquareMatrix) super.transjugate ();
    }
    
    @Override
    public SquareMatrix negate () {
        return (SquareMatrix) super.negate ();
    }
    
    @Override
    public ArrayList <Apcomplex> minors () {
        ArrayList <Apcomplex> minors = new ArrayList <Apcomplex> ();
        
        ArrayList <Matrix> submatrices = this.submatrices ();
        
        for (int submatrix = 0; submatrix < submatrices.size (); submatrix++) {
            if (submatrices.get (submatrix).dimension1 != this.dimension1 - 1 && submatrices.get (submatrix).dimension2 != this.dimension1) {
                break;
            }
            
            minors.add (MatrixOps.calculateDeterminant ((SquareMatrix) submatrices.get (submatrix)));
        }
        
        return minors;
    }
    
    @Override
    public SquareMatrix inverse () {
        return MatrixOps.invertMatrix (this);
    }
    
    @Override
    public boolean isSymmetric () {
        return this.equals (this.transpose ());
    }
    
    @Override
    public boolean isAntisymmetric () {
        return this.equals (this.transpose ().negate ());
    }
    
    @Override
    public boolean isCentrosymmetric () {
        ExchangeMatrix exchangeMatrix = new ExchangeMatrix (this.dimension1);

        return MatrixOps.matrixMultiplication (this, exchangeMatrix).equals (MatrixOps.matrixMultiplication (exchangeMatrix, this));
    }
    
    @Override
    public boolean isPersymmetric () {
        ExchangeMatrix exchangeMatrix = new ExchangeMatrix (this.dimension1);
        
        return MatrixOps.matrixMultiplication (this, exchangeMatrix).equals (MatrixOps.matrixMultiplication (exchangeMatrix, this.transpose ()));
    }
    
    @Override
    public boolean isBisymmetric () {
        return (this.isSymmetric () && this.isCentrosymmetric ());
    }
    
    @Override
    public boolean isHermitian () {
        return this.equals (this.transjugate ());
    }
    
    @Override
    public boolean isAntihermitian () {
        return this.equals (this.transjugate ().negate ());
    }
    
    @Override
    public boolean isHankel () {
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension1; column++) {
                for (int k = 0; k <= column - row; k++) {
                    if (row <= column && !this.getEntry (row, column).equals (this.getEntry (row + k, column - k))) {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isInvolutory () {
        return (new IdentityMatrix (this.dimension1)).equals (MatrixOps.matrixPower (this, 2));
    }
    
    @Override
    public boolean isIdempotent () {
        return this.equals (MatrixOps.matrixPower (this, 2));
    }
    
    @Override
    public boolean isIdentityMatrix () {
        return (this instanceof IdentityMatrix || this.equals (new IdentityMatrix (this.dimension1)));
    }
    
    @Override
    public boolean isHadamard () {
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension1; column++) {
                if (!ApcomplexMath.abs (this.getEntry (row, column)).equals (Apfloat.ONE)) {
                    return false;
                }
            }
        }
        
        return MatrixOps.matrixMultiplication ((Matrix) this, (Matrix) this.transjugate ()).equals (MatrixOps.scalarMultiplication ((Matrix) new IdentityMatrix (this.dimension1), new Apcomplex (new Apfloat (this.dimension1))));
    }
    
    @Override
    public boolean isOrthogonal () {
        return this.transpose ().equals (this.inverse ());
    }
    
    @Override
    public boolean isUnitary () {
        return this.transjugate ().equals (this.inverse ());
    }
    
    @Override
    public boolean isNormal () {
        if (this.isHermitian () || this.isAntihermitian () || this.isUnitary ()) {
            return true;
        }
        
        Matrix matrixTransjugate = (Matrix) this.transjugate ();
        
        return (MatrixOps.matrixMultiplication (matrixTransjugate, (Matrix) this)).equals (MatrixOps.matrixMultiplication ((Matrix) this, matrixTransjugate));
    }
    
    @Override
    public boolean isRightStochastic () {
        if (!this.isNonnegative ()) {
            return false;
        }
        
        for (int row = 0; row < this.dimension1; row++) {
            Apcomplex rowSum = Apcomplex.ZERO;
            
            for (int column = 0; column < this.dimension1; column++) {
                rowSum = rowSum.add (this.getEntry (row, column));
            }
            
            if (!rowSum.equals (Apcomplex.ONE)) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isLeftStochastic () {
        if (!this.isNonnegative ()) {
            return false;
        }
        
        SquareMatrix matrixTranspose = this.transpose ();
        
        for (int column = 0; column < this.dimension1; column++) {
            Apcomplex columnSum = Apcomplex.ZERO;
            
            for (int row = 0; row < this.dimension1; row++) {
                columnSum = columnSum.add (matrixTranspose.getEntry (column, row));
            }
            
            if (!columnSum.equals (Apcomplex.ONE)) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isDoublyStochastic () {
        return (this.isRightStochastic () && this.isLeftStochastic ());
    }
    
    @Override
    public boolean isHollow () {
        for (int entry = 0; entry < this.dimension1; entry++) {
            if (!this.getEntry (entry, entry).equals (Apcomplex.ZERO)) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isM () {
        if (!this.isZ ()) {
            return false;
        }
        
        return this.inverse ().isNonnegative ();
    }
    
    @Override
    public boolean isP () {
        for (Apcomplex minor : this.minors ()) {
            if (ApcomplexMath.abs (minor).compareTo (Apcomplex.ZERO) != 1) {
                return false;
            }
        }
        
        return true;
    }
}