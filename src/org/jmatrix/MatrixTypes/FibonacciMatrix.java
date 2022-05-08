package org.jmatrix.MatrixTypes;

import java.util.ArrayList;

import org.apfloat.Apcomplex;
import org.apfloat.Apint;

public class FibonacciMatrix extends Matrix {
    public FibonacciMatrix (int dimension1, int dimension2) {
        super (dimension1, dimension2);
        
        Apint n1, n2;
        n1 = new Apint (0);
        n2 = new Apint (1);
        
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                Apint nTemp = new Apint (n2.toBigInteger ());
                n2 = n1.add (n2);
                n1 = nTemp;
                
                super.setEntry ((Apcomplex) n2);
            }
        }
    }
    
    public FibonacciMatrix (FibonacciMatrix fibonacciMatrix) {
        super (fibonacciMatrix);
    }
    
    @Override
    public void setMatrix (ArrayList <ArrayList <Apcomplex>> matrix) {
        return;
    }
    
    @Override
    public void setEntry (Apcomplex entry, int... position) {
        return;
    }
}
