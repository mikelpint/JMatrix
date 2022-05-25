package org.jmatrix.graphical;

import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JButton;

import java.awt.BorderLayout;
import java.awt.event.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.apfloat.Apcomplex;
import org.apfloat.Apint;

import org.jmatrix.MatrixTypes.*;
import org.jmatrix.internal.JMath;
import org.jmatrix.internal.JMatrixIO;
import org.jmatrix.internal.JMatrixIO.Styles;

public class MatrixSelectorFactory {
    private static MatrixSelectorFactory matrixSelectorFactory;
    
    private MatrixSelectorFactory () {}
    
    public MatrixSelectorFactory getMatrixSelectorFactory () {
        if (MatrixSelectorFactory.matrixSelectorFactory == null) {
            MatrixSelectorFactory.matrixSelectorFactory = new MatrixSelectorFactory ();
        }
        
        return MatrixSelectorFactory.matrixSelectorFactory;
    }
    
    public static Matrix generateMatrix (String matrixType) {
        if (matrixType.equals ("Commutation matrix")) {
            String dimension1 = JOptionPane.showInputDialog ("Enter the number of rows of the original matrix:");
            
            if (dimension1 == null) {
                return null;
            }
            
            String dimension2 = JOptionPane.showInputDialog ("Enter the number of columns of the original matrix:");
            
            if (dimension2 == null) {
                return null;
            }
            
            return new CommutationMatrix (Integer.parseInt (dimension1), Integer.parseInt (dimension1));
        }
        
        else if (matrixType.equals ("Duplication matrix")) {
            String dimension1 = JOptionPane.showInputDialog ("Enter the number of rows of the original matrix:");
            
            if (dimension1 == null) {
                return null;
            }
            
            return new DuplicationMatrix (Integer.parseInt (dimension1));
        }
        
        if (matrixType.equals ("Elimination matrix")) {
            String dimension1 = JOptionPane.showInputDialog ("Enter the number of rows of the original matrix:");
            
            if (dimension1 == null) {
                return null;
            }
            
            return new EliminationMatrix (Integer.parseInt (dimension1));
        }
        
        else if (matrixType.equals ("Exchange matrix")) {
            String dimension1 = JOptionPane.showInputDialog ("Enter the number of rows:");
            
            if (dimension1 == null) {
                return null;
            }
            
            return new ExchangeMatrix (Integer.parseInt (dimension1));
        }
        
        else if (matrixType.equals ("Hilbert matrix")) {
            String dimension1 = JOptionPane.showInputDialog ("Enter the number of rows:");
            
            if (dimension1 == null) {
                return null;
            }
            
            return new HilbertMatrix (Integer.parseInt (dimension1));
        }
        
        else if (matrixType.equals ("Identity matrix")) {
            String dimension1 = JOptionPane.showInputDialog ("Enter the number of rows:");
            
            if (dimension1 == null) {
                return null;
            }
            
           return new IdentityMatrix (Integer.parseInt (dimension1));
        }
        
        else if (matrixType.equals ("Lehmer matrix")) {
            String dimension1 = JOptionPane.showInputDialog ("Enter the number of rows:");
            
            if (dimension1 == null) {
                return null;
            }
            
            return new LehmerMatrix (Integer.parseInt (dimension1));
        }
        
        else if (matrixType.equals ("Lucas sequence-based matrix")) {
            String dimension1 = JOptionPane.showInputDialog ("Enter the number of rows:");
            
            if (dimension1 == null) {
                return null;
            }
            
            String dimension2 = JOptionPane.showInputDialog ("Enter the number of columns");
            
            if (dimension2 == null) {
                return null;
            }
            
            ArrayList <ArrayList <Apcomplex>> matrixEntries = new ArrayList <ArrayList <Apcomplex>> ();
            
            String sequence = (String) JOptionPane.showInputDialog (
                null,
                "Which type of Lucas sequence do you want to base the matrix on?",
                null,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String [] {
                    "Fibonacci numbers",
                    "Lucas numbers",
                    "Pell numbers",
                    "Pell-Lucas numbers",
                    "Jacobsthal numbers",
                    "Jacobsthal-Lucas numbers",
                    "Mersenne numbers",
                    "Numbers of the form 2^n + 1",
                    "Square roots of the square triangular numbers"
                },
                "Fibonacci numbers"
            );
            
            if (sequence.equals ("Fibonacci numbers")) {
                matrixEntries = JMath.Fibonacci (Integer.parseInt (dimension1), Integer.parseInt (dimension2));
            }
            
            else if (sequence.equals ("Lucas numbers")) {
                matrixEntries = JMath.Lucas (Integer.parseInt (dimension1), Integer.parseInt (dimension2));
            }
            
            else if (sequence.equals ("Pell numbers")) {
                matrixEntries = JMath.Pell (Integer.parseInt (dimension1), Integer.parseInt (dimension2));
            }
            
            else if (sequence.equals ("Pell-Lucas numbers")) {
                matrixEntries = JMath.PellLucas (Integer.parseInt (dimension1), Integer.parseInt (dimension2));
            }
            
            else if (sequence.equals ("Jacobsthal numbers")) {
                matrixEntries = JMath.Jacobsthal (Integer.parseInt (dimension1), Integer.parseInt (dimension2));
            }
            
            else if (sequence.equals ("Jacobsthal-Lucas numbers")) {
                matrixEntries = JMath.JacobsthalLucas (Integer.parseInt (dimension1), Integer.parseInt (dimension2));
            }
            
            else if (sequence.equals ("Mersenne numbers")) {
                matrixEntries = JMath.Mersenne (Integer.parseInt (dimension1), Integer.parseInt (dimension2));
            }
            
            else if (sequence.equals ("Numbers of the form 2ⁿ + 1")) {
                matrixEntries = JMath.twoNPlusOne (Integer.parseInt (dimension1), Integer.parseInt (dimension2));
            }
            
            else {
                matrixEntries = JMath.sqrtSquareTriangNums (Integer.parseInt (dimension1), Integer.parseInt (dimension2));
            }
            
            if (Integer.parseInt (dimension1) == Integer.parseInt (dimension2)) {
                return new SquareMatrix (Integer.parseInt (dimension1), matrixEntries);
            }
            
            else {
                return new Matrix (Integer.parseInt (dimension1), Integer.parseInt (dimension2), matrixEntries);
            }
        }
        
        else if (matrixType.equals ("Pascal matrix")) {
            String dimension1 = JOptionPane.showInputDialog ("Enter the number of rows:");
            
            if (dimension1 == null) {
                return null;
            }
            
            String type = (String) JOptionPane.showInputDialog (
                null,
                "Which type of Pascal matrix do you want to generate?",
                null,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String [] {
                    "Lower",
                    "Upper",
                    "Symmetric"
                },
                "Lower"
            );
            
            if (type.equals ("Lower")) {
                return new LowerPascalMatrix (Integer.parseInt (dimension1));
            }
            
            else if (type.equals ("Upper")) {
                return new UpperPascalMatrix (Integer.parseInt (dimension1));
            }
            
            else {
                return new SymmetricPascalMatrix (Integer.parseInt (dimension1));
            }
        }
        
        else if (matrixType.equals ("Pauli matrix")) {
            String type = (String) JOptionPane.showInputDialog (
                null,
                "Which type of Pauli matrix do you want to generate?",
                null,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String [] {
                    "X",
                    "Y",
                    "Z"
                },
                "X"
            );
            
            if (type.equals ("X")) {
                return new PauliMatrixX ();
            }
            
            else if (type.equals ("Y")) {
                return new PauliMatrixY ();
            }
            
            else {
                return new PauliMatrixZ ();
            }
        }
        
        else if (matrixType.equals ("Random matrix")) {
            String dimension1 = JOptionPane.showInputDialog ("Enter the number of rows:");
            
            if (dimension1 == null) {
                return null;
            }
            
            String dimension2 = JOptionPane.showInputDialog ("Enter the number of columns:");
            
            if (dimension2 == null) {
                return null;
            }
            
            String range = JOptionPane.showInputDialog ("Enter the range of the numbers (0 for none):");
            
            if (range == null) {
                return null;
            }
            
            boolean complex = 
                (
                    JOptionPane.showOptionDialog (
                        null,
                        "Do you want the matrix to be real or complex?",
                        null,
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String [] {"Real", "Complex"},
                        "Real"
                    ) == JOptionPane.YES_OPTION
                )
                ? true
                : false
            ;
            
            if (Integer.parseInt (dimension1) == Integer.parseInt (dimension2)) {
                SquareMatrix matrix = new SquareMatrix (Integer.parseInt (dimension1));
                matrix.randomizeElements (
                        (new Apint (range).equals (Apint.ZERO)) ? null : new Apint (range), complex
                );
                
                return matrix;
            }
            
            else {
                Matrix matrix = new Matrix (Integer.parseInt (dimension1), Integer.parseInt (dimension2));
                matrix.randomizeElements (
                        (new Apint (range).equals (Apint.ZERO)) ? null : new Apint (range), complex
                );
                
                return matrix;
            }
        }
        
        else if (matrixType.equals ("Redheffer matrix")) {
            String dimension1 = JOptionPane.showInputDialog ("Enter the number of rows:");
            
            if (dimension1 == null) {
                return null;
            }
            
            return new RedhefferMatrix (Integer.parseInt (dimension1));
        }
        
        else if (matrixType.equals ("Scalar matrix")) {
            String dimension1 = JOptionPane.showInputDialog ("Enter the number of rows:");
            
            if (dimension1 == null) {
                return null;
            }
            
            Apcomplex scalar = JMatrixIO.parseNumber (
                JOptionPane.showInputDialog (
                    "Enter the scalar you wish to base the matrix on (in the form \"(a, b)\"):"
                )
            );
            
            if (scalar == null) {
                return null;
            }
            
            return new ScalarMatrix (Integer.parseInt (dimension1), scalar);
        }
        
        else if (matrixType.equals ("Shift matrix")) {
            String dimension1 = JOptionPane.showInputDialog ("Enter the number of rows:");
            
            if (dimension1 == null) {
                return null;
            }
            
            if (JOptionPane.showOptionDialog (
                    null,
                    "Do you want the matrix to be an upper shift matrix or a lower shift matrix?",
                    null,
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String [] {"Upper", "Lower"},
                    "Upper"
                ) == JOptionPane.YES_OPTION
            ) {
                return new UpperShiftMatrix (Integer.parseInt (dimension1));
            }
            
            return new LowerShiftMatrix (Integer.parseInt (dimension1));
        }
        
        else if (matrixType.equals ("Zero matrix")) {
            String dimension1 = JOptionPane.showInputDialog ("Enter the number of rows:");
            
            if (dimension1 == null) {
                return null;
            }
            
            String dimension2 = JOptionPane.showInputDialog ("Enter the number of columns:");
            
            if (dimension2 == null) {
                return null;
            }
            
            return new ZeroMatrix (Integer.parseInt (dimension1), Integer.parseInt (dimension2));
        }
        
        return null;
    }
}
