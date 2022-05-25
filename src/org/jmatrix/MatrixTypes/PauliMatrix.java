package org.jmatrix.MatrixTypes;

import java.util.Arrays;
import java.util.ArrayList;

import org.apfloat.Apcomplex;

public abstract class PauliMatrix extends SquareMatrix {
    public enum Types {
        X,
        Y,
        Z
    }
    
    final ArrayList <ArrayList <ArrayList <Apcomplex>>> pauliMatrices = new ArrayList <ArrayList <ArrayList <Apcomplex>>> (
        Arrays.asList (
            new ArrayList <ArrayList <Apcomplex>> (
                Arrays.asList (
                    new ArrayList <Apcomplex> (
                        Arrays.asList (
                            Apcomplex.ZERO,
                            Apcomplex.ONE
                        )
                    ),
                    new ArrayList <Apcomplex> (
                        Arrays.asList (
                            Apcomplex.ONE,
                            Apcomplex.ZERO
                        )
                    )
                )
            ),
            new ArrayList <ArrayList <Apcomplex>> (
                Arrays.asList (
                    new ArrayList <Apcomplex> (
                        Arrays.asList (
                            Apcomplex.ZERO,
                            Apcomplex.I.conj ()
                        )
                    ),
                    new ArrayList <Apcomplex> (
                        Arrays.asList (
                            Apcomplex.I,
                            Apcomplex.ZERO
                        )
                    )
                )
            ),
            new ArrayList <ArrayList <Apcomplex>> (
                Arrays.asList (
                    new ArrayList <Apcomplex> (
                        Arrays.asList (
                            Apcomplex.ONE,
                            Apcomplex.ZERO
                        )
                    ),
                    new ArrayList <Apcomplex> (
                        Arrays.asList (
                            Apcomplex.ZERO,
                            Apcomplex.ONE.negate ()
                        )
                    )
                )
            )
        )
    );
    
    public PauliMatrix (Types type) {
        super (2);
        
        if (type == Types.X) {
            super.setMatrix (pauliMatrices.get (0));
        }
        
        else if (type == Types.Y) {
            super.setMatrix (pauliMatrices.get (1));
        }
        
        else {
            super.setMatrix (pauliMatrices.get (2));
        }
    }
    
    public PauliMatrix (PauliMatrix pauliMatrix) {
        super (pauliMatrix);
    }
    
    @Override
    public boolean isHermitian () {
        return true;
    }
    
    @Override
    public boolean isInvolutory () {
        return true;
    }
    
    @Override
    public boolean isUnitary () {
        return true;
    }
}
