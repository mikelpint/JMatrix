package org.jmatrix.MatrixTypes;

import java.util.ArrayList;

import org.apfloat.Apcomplex;

import org.jmatrix.internals.JMath;

abstract public class PascalMatrix extends SquareMatrix {
    public PascalMatrix (int dimension1) {
        super (dimension1);
    }
    
    public PascalMatrix (PascalMatrix pascalMatrix) {
        super (pascalMatrix);
    }
    
    final public class LowerPascalMatrix extends PascalMatrix {
        public LowerPascalMatrix (int dimension1) {
            super (dimension1);
            
            for (int row = 0; row < this.dimension1; row++) {
                for (int column = 0; column < this.dimension1; column++) {
                    if (column <= row) {
                        super.setEntry ((Apcomplex) JMath.binomialCoefficient (row, column), row, column);
                    }
                }
            }
        }
        
        public LowerPascalMatrix (LowerPascalMatrix lowerPascalMatrix) {
            super (lowerPascalMatrix);
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
        public boolean isLowerTriangular () {
            return true;
        }
    }
    
    final public class UpperPascalMatrix extends PascalMatrix {
        public UpperPascalMatrix (int dimension1) {
            super (dimension1);
            
            for (int row = 0; row < this.dimension1; row++) {
                for (int column = 0; column < this.dimension1; column++) {
                    if (row <= column) {
                        super.setEntry ((Apcomplex) JMath.binomialCoefficient (column, row), row, column);
                    }
                }
            }
        }
        
        public UpperPascalMatrix (UpperPascalMatrix upperPascalMatrix) {
            super (upperPascalMatrix);
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
        public boolean isUpperTriangular () {
            return true;
        }
    }
    
    final public class SymmetricPascalMatrix extends PascalMatrix {
        public SymmetricPascalMatrix (int dimension1) {
            super (dimension1);
            
            for (int row = 0; row < this.dimension1; row++) {
                for (int column = 0; column < this.dimension1; column++) {
                    super.setEntry ((Apcomplex) JMath.binomialCoefficient (row + column, row), row, column);
                }
            }
        }
        
        public SymmetricPascalMatrix (SymmetricPascalMatrix symmetricPascalMatrix) {
            super (symmetricPascalMatrix);
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
        public boolean isSymmetric () {
            return true;
        }
    }
}
