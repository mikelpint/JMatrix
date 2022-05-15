package org.jmatrix.MatrixTypes;

import java.util.ArrayList;

import org.apfloat.Apcomplex;
import org.apfloat.Apfloat;

final public class ComplexNumberMatrix extends SquareMatrix {
    public ComplexNumberMatrix (Apcomplex number) {
        super (2);
        
        super.setEntry ((Apcomplex) number.real (), 0, 0);
        super.setEntry ((Apcomplex) number.imag ().negate (), 0, 1);
        super.setEntry ((Apcomplex) number.imag (), 1, 0);
        super.setEntry ((Apcomplex) number.real (), 1, 1);
    }
    
    public ComplexNumberMatrix (ComplexNumberMatrix complexNumberMatrix) {
        super (complexNumberMatrix);
    }
    
    @Override
    public void setMatrix (ArrayList <ArrayList <Apcomplex>> matrix) {
        return;
    }
    
    @Override
    public void setEntry (Apcomplex entry, int... position) {
        return;
    }
    
    public Apcomplex toComplex () {
        return new Apcomplex ((Apfloat) this.getEntry (0, 0), (Apfloat) this.getEntry (1, 0));
    }
    
    public Apcomplex toComplexConjugate () {
        return new Apcomplex ((Apfloat) this.getEntry (0, 0), (Apfloat) this.getEntry (0, 1));
    }
}
