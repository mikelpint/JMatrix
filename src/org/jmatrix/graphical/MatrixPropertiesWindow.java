package org.jmatrix.graphical;

import org.apfloat.Apcomplex;
import org.jmatrix.MatrixOps.MatrixOps;
import org.jmatrix.MatrixTypes.*;
import org.jmatrix.internal.JMatrixIO;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class MatrixPropertiesWindow extends Window {
    protected JTextPane textPane;
    protected String matrixPropertiesString;
    
    public MatrixPropertiesWindow (Matrix matrix) {
        this (matrix, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    
    public MatrixPropertiesWindow (Matrix matrix, int width, int height) {
        this (matrix, width, height, DEFAULT_TITLE);
    }
    
    public MatrixPropertiesWindow (Matrix matrix, int width, int height, String title) {
        this (matrix, width, height, title, DEFAULT_ICON);
    }
    
    public MatrixPropertiesWindow (Matrix matrix, int width, int height, String title, ImageIcon icon) {
        this (matrix, width, height, title, icon, DEFAULT_FONT);
    }
    
    public MatrixPropertiesWindow (Matrix matrix, int width, int height, String title, ImageIcon icon, Font font) {
        this (matrix, width, height, title, icon, font, DEFAULT_FOREGROUND, DEFAULT_BACKGROUND);
    }
    
    public MatrixPropertiesWindow (Matrix matrix, int width, int height, String title, ImageIcon icon, Font font, Color foreground, Color background) {
        super (width, height, title, icon, font, foreground, background);
        
        Container container = this.getContentPane ();
        container.setLayout (new BorderLayout (0, 0));
        
        this.matrixPropertiesString = "";
        
        this.matrixPropertiesString += matrix.toString () + "\n";
        
        this.matrixPropertiesString += "\nMatrix type: ";
        
        
        if (matrix instanceof RowVector) {
            this.matrixPropertiesString += "row vector";
        }
        
        else if (matrix instanceof Vector) {
            if (matrix instanceof CanonicalVector) {
                this.matrixPropertiesString += "canonical vector";
            }
            
            else if (matrix instanceof HalfVector) {
                this.matrixPropertiesString += "half-vector";
            }
            
            else {
                this.matrixPropertiesString += "vector";
            }
        }
        
        else if (matrix instanceof SquareMatrix) {
            if (matrix instanceof PascalMatrix) {
                if (matrix instanceof LowerPascalMatrix) {
                    this.matrixPropertiesString += "lower Pascal matrix";
                }
                
                else if (matrix instanceof UpperPascalMatrix) {
                    this.matrixPropertiesString += "upper Pascal matrix";
                }
                
                else {
                    this.matrixPropertiesString += "symmetric Pascal matrix";
                }
            }
            
            else if (matrix instanceof RedhefferMatrix) {
                this.matrixPropertiesString += "Redheffer matrix";
            }
            
            else if (matrix instanceof CommutationMatrix) {
                this.matrixPropertiesString += "commutation matrix";
            }
            
            else if (matrix instanceof ScalarMatrix) {
                if (matrix instanceof IdentityMatrix || ((SquareMatrix) matrix).isIdentityMatrix ()) {
                    this.matrixPropertiesString += "identity matrix";
                }
                
                else {
                    this.matrixPropertiesString += "scalar matrix";
                }
            }
            
            else if (matrix instanceof ShiftMatrix) {
                if (matrix instanceof LowerShiftMatrix) {
                    this.matrixPropertiesString += "lower shift matrix";
                }
                
                else {
                    this.matrixPropertiesString += "upper shift matrix";
                }
            }
            
            else if (matrix instanceof PauliMatrix) {
                if (matrix instanceof PauliMatrixX) {
                    this.matrixPropertiesString += "Pauli matrix X";
                }
                
                else if (matrix instanceof PauliMatrixY) {
                    this.matrixPropertiesString += "Pauli matrix Y";
                }
                
                else {
                    this.matrixPropertiesString += "Pauli matrix Z";
                }
            }
            
            else if (matrix instanceof ExchangeMatrix) {
                this.matrixPropertiesString += "exchange matrix";
            }
            
            else if (matrix instanceof HilbertMatrix) {
                this.matrixPropertiesString += "Hilbert matrix";
            }
            
            else if (matrix instanceof LehmerMatrix) {
                this.matrixPropertiesString += "Lehmer matrix";
            }
            
            else {
                this.matrixPropertiesString += "square matrix";
            }
        }
        
        else {
            if (matrix instanceof ZeroMatrix) {
                if (matrix.getDimension1 () == matrix.getDimension2 ()) {
                    this.matrixPropertiesString += "square zero matrix";
                }
                
                else {
                    this.matrixPropertiesString += "zero matrix";
                }
            }
            
            else if (matrix instanceof DuplicationMatrix) {
                this.matrixPropertiesString += "duplication matrix";
            }
            
            else if (matrix instanceof EliminationMatrix) {
                this.matrixPropertiesString += "eliminationMatrix";
            }
            
            this.matrixPropertiesString += "matrix";
        }
        
        if (matrix instanceof Vector || matrix instanceof RowVector) {
            this.matrixPropertiesString += "\nNumber of elements: " + ((matrix instanceof Vector) ? matrix.getDimension1 () : matrix.getDimension2 ());
        }
        
        else {
            this.matrixPropertiesString += "\nNumber of rows: " + matrix.getDimension1 () + "\nNumber of columns: " + matrix.getDimension2 ();
        }
        
        this.matrixPropertiesString += "\nRank" + MatrixOps.calculateRank (matrix, null);
        
        Vector vectorization = matrix.vectorization ();
        if (vectorization != null) {
            this.matrixPropertiesString += "\nVectorization:\n" + vectorization.toString ();
        }
        
        Vector columnMajorVectorization = matrix.columnMajorVectorization ();
        if (columnMajorVectorization != null) {
            this.matrixPropertiesString += "\nColumn-major vectorization:\n" + columnMajorVectorization.toString ();
        }
        
        RowVector rowVectorization = matrix.toRowVector ();
        if (rowVectorization != null) {
            this.matrixPropertiesString += "\nVectorization (row vector form):\n" + rowVectorization.toString ();
        }
        
        HalfVector halfVectorization = matrix.halfVectorization ();
        if (halfVectorization != null) {
            this.matrixPropertiesString += "\nHalf-vectorization:\n" + halfVectorization.toString ();
        }
        
        this.matrixPropertiesString += "\nTranspose:\n" + matrix.transpose ().toString ();
        
        this.matrixPropertiesString += "\nTransjugate:\n" + matrix.transjugate ().toString ();
        
        this.matrixPropertiesString += "\nNegate:\n" + matrix.negate ().toString ();
        
        ArrayList <Matrix> principalSubmatrices = matrix.principalSubmatrices ();
        this.matrixPropertiesString += "\nPrincipal submatrices:";
        if (principalSubmatrices == null) {
            this.matrixPropertiesString += " none";
        }
        
        else {
            for (int i = 0; i < principalSubmatrices.size (); i++) {
                this.matrixPropertiesString += "\n" + principalSubmatrices.get (i).toString ();
            }
        }
        
        this.matrixPropertiesString += "\n" + ((matrix.isReal ()) ? "Real matrix" : "Complex matrix");
        
        if (matrix.isInteger ()) {
            this.matrixPropertiesString += "\nInteger matrix";
        }
        
        if (matrix.isSingular ()) {
            this.matrixPropertiesString += "\nSingular matrix";
        }
        
        if ((matrix instanceof SquareMatrix) && matrix.isRowEchelon ()) {
            this.matrixPropertiesString += "\nRow echelon";
        }
        
        if (matrix.isDiagonal ()) {
            this.matrixPropertiesString += "\nDiagonal";
        }
        
        if (matrix.isLowerBidiagonal ()) {
            this.matrixPropertiesString += "\nLower bidiagonal";
        }
        
        if (matrix.isUpperBidiagonal ()) {
            this.matrixPropertiesString += "\nUpper bidiagonal";
        }
        
        if (matrix.isTridiagonal ()) {
            this.matrixPropertiesString += "\nTridiagonal";
        }
        
        if (matrix.isPentadiagonal ()) {
            this.matrixPropertiesString += "\nPentadiagonal";
        }
        
        if (matrix.isToeplitz ()) {
            this.matrixPropertiesString += "\nToeplitz";
        }
        
        if (matrix.isNonnegative ()) {
            if (matrix.isPositive ()) {
                this.matrixPropertiesString += "\nPositive";
            }
            
            else {
                this.matrixPropertiesString += "\nNonnegative";
            }
            
        }
        
        if (matrix.isMetzler ()) {
            this.matrixPropertiesString += "\nMetzler";
        }
        
        if (matrix.isZ ()) {
            this.matrixPropertiesString += "\nZ-matrix";
        }
        
        if (matrix.isL ()) {
            this.matrixPropertiesString += "\nL-matrix";
        }
        
        if (matrix instanceof SquareMatrix) {
            this.matrixPropertiesString += "\nDeterminant: " + JMatrixIO.numberToString (MatrixOps.calculateDeterminant (((SquareMatrix) matrix)));
            
            ArrayList <Apcomplex> minors = ((SquareMatrix) matrix).minors ();
            if (minors != null) {
                this.matrixPropertiesString += "\nMinors: ";
                for (int i = 0; i < minors.size (); i++) {
                    this.matrixPropertiesString += JMatrixIO.numberToString (minors.get (i));
                    
                    if (i < minors.size () - 1) {
                        this.matrixPropertiesString += ", ";
                    }
                }
            }
            
            SquareMatrix inverse = ((SquareMatrix) matrix).inverse ();
            if (inverse != null) {
                this.matrixPropertiesString += "\nInverse:\n" + inverse.toString ();
            }
            
            if (((SquareMatrix) matrix).isLowerTriangular ()) {
                this.matrixPropertiesString += "\nLower triangular";
            }
            
            if (((SquareMatrix) matrix).isUpperTriangular ()) {
                this.matrixPropertiesString += "\nUpper triangular";
            }
            
            if (((SquareMatrix) matrix).isAntiDiagonal ()) {
                this.matrixPropertiesString += "\nAntidiagonal";
            }
            
            if (((SquareMatrix) matrix).isSymmetric ()) {
                this.matrixPropertiesString += "\nSymmetric";
            }
            
            if (((SquareMatrix) matrix).isAntisymmetric ()) {
                this.matrixPropertiesString += "\nAntisymmetric";
            }
            
            if (((SquareMatrix) matrix).isCentrosymmetric ()) {
                this.matrixPropertiesString += "\nCentrosymmetric";
            }
            
            if (((SquareMatrix) matrix).isPersymmetric ()) {
                this.matrixPropertiesString += "\nPersymmetric";
            }
            
            if (((SquareMatrix) matrix).isBisymmetric ()) {
                this.matrixPropertiesString += "\nBisymmetric";
            }
            
            if (((SquareMatrix) matrix).isHermitian ()) {
                this.matrixPropertiesString += "\nHermitian";
            }
            
            if (((SquareMatrix) matrix).isAntihermitian ()) {
                this.matrixPropertiesString += "\nAntihermitian";
            }
            
            if (((SquareMatrix) matrix).isHankel ()) {
                this.matrixPropertiesString += "\nHankel";
            }
            
            if (((SquareMatrix) matrix).isInvolutory ()) {
                this.matrixPropertiesString += "\nInvolutory";
            }
            
            if (((SquareMatrix) matrix).isIdempotent ()) {
                this.matrixPropertiesString += "\nIdempotent";
            }
            
            if (((SquareMatrix) matrix).isHadamard ()) {
                this.matrixPropertiesString += "\nHadamard";
            }
            
            if (((SquareMatrix) matrix).isOrthogonal ()) {
                this.matrixPropertiesString += "\nOrthogonal";
            }
            
            if (((SquareMatrix) matrix).isUnitary ()) {
                this.matrixPropertiesString += "\nUnitary";
            }
            
            if (((SquareMatrix) matrix).isNormal ()) {
                this.matrixPropertiesString += "\nNormal";
            }
            
            if (((SquareMatrix) matrix).isDoublyStochastic ()) {
                this.matrixPropertiesString += "\nDoubly stochastic";
            }
            
            else {
                if (((SquareMatrix) matrix).isRightStochastic ()) {
                    this.matrixPropertiesString += "\nRight stochastic";
                }
                
                else if (((SquareMatrix) matrix).isLeftStochastic ()) {
                    this.matrixPropertiesString += "\nLeft stochastic";
                }
            }
            
            if (((SquareMatrix) matrix).isHollow ()) {
                this.matrixPropertiesString += "\nHollow";
            }
            
            if (((SquareMatrix) matrix).isP ()) {
                this.matrixPropertiesString += "\nP-matrix";
            }
            
            if (((SquareMatrix) matrix).isM ()) {
                this.matrixPropertiesString += "\nM-matrix";
            }
        }
        
        this.textPane = new JTextPane ();
        this.textPane.setEditable (false);
        
        textPane.setText (matrixPropertiesString);
        
        JScrollPane scrollPane = new JScrollPane (this.textPane);
        scrollPane.setVerticalScrollBarPolicy (JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize (new Dimension (250, 145));
        scrollPane.setMinimumSize (new Dimension (10, 10));
        
        container.add (scrollPane);
        
        this.setLocationRelativeTo (null);
        this.setVisible (true);
    }
}
