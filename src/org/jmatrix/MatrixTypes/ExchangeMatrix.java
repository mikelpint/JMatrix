package org.jmatrix.MatrixTypes;

import java.util.ArrayList;

import org.apfloat.Apcomplex;

final public class ExchangeMatrix extends SquareMatrix {
    public ExchangeMatrix (int dimension1) {
        super (dimension1);

        for (int entry = 0; entry < dimension1; entry++) {
            super.setEntry (Apcomplex.ONE, dimension1 - entry + 1, entry);
        }
    }
    
    public ExchangeMatrix (ExchangeMatrix exchangeMatrix) {
        super (exchangeMatrix);
    }
    
    @Override
    public void setMatrix (ArrayList <ArrayList <Apcomplex>> matrix) {
        return;
    }
    
    @Override
    public void setEntry (Apcomplex entry, int... position) {
        return;
    }
    
    @Override
    public SquareMatrix transpose () {
        return this;
    }
    
    @Override
    public SquareMatrix transjugate () {
        return this;
    }
    
    @Override
    public boolean isSymmetric () {
        return true;
    }
    
    @Override
    public boolean isInvolutory () {
        return true;
    }
}
