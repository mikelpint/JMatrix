package org.jmatrix.MatrixTypes;

import java.util.ArrayList;

import org.apfloat.Apcomplex;

final public class CommutationMatrix extends SquareMatrix {
    public CommutationMatrix (int dimension1, int dimension2) {
        super (dimension1 * dimension2);
        
        for (int blockMatrixRow = 0; blockMatrixRow < dimension1; blockMatrixRow++) {
            for (int blockMatrixColumn = 0; blockMatrixColumn < dimension2; blockMatrixColumn++) {
                for (int innerBlockRow = 0; innerBlockRow < dimension2; innerBlockRow++) {
                    for (int innerBlockColumn = 0; innerBlockColumn < dimension1; innerBlockColumn++) {
                        if (blockMatrixRow == innerBlockColumn && blockMatrixColumn == innerBlockRow) {
                            super.setEntry (Apcomplex.ONE, innerBlockRow + (blockMatrixRow * dimension2), innerBlockColumn + (blockMatrixColumn * dimension2));
                        }
                    }
                }
            }
        }
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