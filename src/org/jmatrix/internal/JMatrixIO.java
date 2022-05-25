package org.jmatrix.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

import org.apfloat.Apcomplex;
import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

import org.jmatrix.MatrixTypes.*;

public class JMatrixIO {
    public enum Styles {
        LIST,
        ARRAY,
        POSITIONAL,
        BRACKETS,
        BRACES,
        PARENTHESES,
        PIPES,
        DOUBLEPIPES
    }
    
    public static String numberToString (Apcomplex number) {
        if (number == null) {
            return null;
        }
        
        if (number.equals (Apcomplex.ZERO)) {
            return "0";
        }
        
        Apfloat realPart = number.real ();
        Apfloat imagPart = number.imag ();
        
        String numberString = ((realPart.signum () == -1) ? "-" : "") + ((ApfloatMath.abs (realPart).equals (Apfloat.ZERO)) ? "" : ApfloatMath.abs (realPart).toString (true));
        
        if (!imagPart.equals (Apfloat.ZERO)) {
           String imagString = "";
            
           if (!ApfloatMath.abs (imagPart).equals (Apfloat.ONE)) {
               imagString = ApfloatMath.abs (imagPart).toString (true);
           }
           
           imagString += "i";
           
           if (numberString != "") {
               numberString += ((imagPart.signum () == -1) ? " - " : " + ") + imagString;
           }
           
           else {
               numberString += ((imagPart.signum () == -1) ? "-" : "") + imagString;
           }
           
           return numberString;
        }
        
        else {
            return ((realPart.signum () == -1) ? "-" : "") + ApfloatMath.abs (realPart).toString (true);
        }
    }
    
    public static String matrixToString (Matrix matrix, Styles style) {
        if (matrix == null || style == null) {
            return null;
        }
        
        String matrixString = "";
        
        if (style == Styles.LIST || style == Styles.ARRAY) {
            char leftEncapsulationChar;
            char rightEncapsulationChar;
            
            if (style == Styles.LIST) {
                leftEncapsulationChar = '[';
                rightEncapsulationChar = ']';
            }
            
            else {
                leftEncapsulationChar = '{';
                rightEncapsulationChar = '}';
            }
            
            matrixString += leftEncapsulationChar;
            
            for (int row = 0; row < matrix.getDimension1 (); row++) {
                matrixString += leftEncapsulationChar;
                
                for (int column = 0; column < matrix.getDimension2 (); column++) {
                    matrixString += numberToString (matrix.getEntry (row, column));
                    
                    if (column < matrix.getDimension2 () - 1) {
                        matrixString += ", ";
                    }
                }
                
                matrixString += rightEncapsulationChar;
                
                if (row < matrix.getDimension1 () - 1) {
                    matrixString += ", ";
                }
            }
            
            matrixString += rightEncapsulationChar;
        }
        
        else if (style == Styles.POSITIONAL) {
            int positionLength = 2 + ((Integer) matrix.getDimension1 ()).toString ().length () + ((Integer) matrix.getDimension2 ()).toString ().length ();
            int numberStringLength = 0;
            
            ArrayList <String> numberStrings = new ArrayList <String> ();
            
            for (int row = 0; row < matrix.getDimension1 (); row++) {
                for (int column = 0; column < matrix.getDimension2 (); column++) {
                    String numberString = numberToString (matrix.getEntry (row, column));
                    numberStrings.add (numberString);
                    if (numberString.length () > numberStringLength) {
                        numberStringLength = numberString.length ();
                    }
                }
            }
            
            for (int row = 0; row < matrix.getDimension1 (); matrixString += (row++ < matrix.getDimension1 () - 1) ? '\n' : "") {
                for (int column = 0; column < matrix.getDimension2 (); column++) {
                    matrixString += String.format (
                        "%s%-*s%s",
                        String.format (
                            "%-*s = ",
                            positionLength,
                            String.format (
                                "[%d][%d]",
                                row + 1,
                                column + 1
                            )
                        ),
                        (column < matrix.getDimension2 () - 1) ? numberStringLength : numberStrings.get (row * matrix.getDimension1 () + column).length (),
                        numberStrings.get (row * matrix.getDimension1 () + column).length (),
                        (column < matrix.getDimension2 () - 1) ? "  " : ""
                    ); 
                }
            }
        }
        
        else {
            ArrayList <ArrayList <String>> delimiters = new ArrayList <ArrayList <String>> (
                Arrays.asList (
                    new ArrayList <String> (
                        Arrays.asList (
                            "⎡  ",
                            "  ⎤",
                            "⎢  ",
                            "  ⎥",
                            "⎣  ",
                            "  ⎦",
                            "[  ",
                            "  ]"
                        )
                    ),
                    new ArrayList <String> (
                        Arrays.asList (
                            "⎧  ",
                            "  ⎫",
                            "⎪  ",
                            "  ⎪",
                            "⎰  ",
                            "  ⎱",
                            "⎱  ",
                            "  ⎰",
                            "⎨  ",
                            "  ⎬",
                            "⎩  ",
                            "  ⎭",
                            "{  ",
                            "  }"
                        )
                    ),
                    new ArrayList <String> (
                        Arrays.asList (
                            "⎛  ",
                            "  ⎞",
                            "⎜  ",
                            "  ⎟",
                            "⎝  ",
                            "  ⎠",
                            "(  ",
                            "  )"
                        )
                    ),
                    new ArrayList <String> (
                        Arrays.asList (
                            "⎢  ",
                            "  ⎥"
                        )
                    ),
                    new ArrayList <String> (
                        Arrays.asList (
                            "⎢⎢  ",
                            "  ⎥⎥"
                        )
                    )
                )
            );
            
            int delimiterGroup = 0;
            
            if (style == Styles.BRACES) {
                delimiterGroup = 1;
            }
            
            else if (style == Styles.PARENTHESES) {
                delimiterGroup = 2;
            }
            
            else if (style == Styles.PIPES) {
                delimiterGroup = 3;
            }
            
            else if (style == Styles.DOUBLEPIPES) {
               delimiterGroup = 4; 
            }
            
            for (int row = 0; row < matrix.getDimension1 (); row++) {
                int rowDelimiters [] = {0, 1};
                
                if (delimiterGroup <= 2) {
                    if (matrix.getDimension1 () == 1) {
                        if (delimiterGroup == 0 || delimiterGroup == 2) {
                            rowDelimiters [0] = 6;
                            rowDelimiters [1] = 7;
                        }

                        else {
                            rowDelimiters [0] = 12;
                            rowDelimiters [1] = 13;
                        }
                    }
                    
                    else {
                        if (row == 0);
                        
                        else if (row == matrix.getDimension1 () - 1) {
                            if (delimiterGroup == 1) {
                                    rowDelimiters [0] = 10;
                                    rowDelimiters [1] = 11;
                            }

                            else {
                                rowDelimiters [0] = 4;
                                rowDelimiters [1] = 5;
                            }
                        }
                        
                        else {
                            rowDelimiters [0] = 2;
                            rowDelimiters [1] = 3;

                            if (delimiterGroup == 1) {
                                if ((matrix.getDimension1 () & 1) == 1) {
                                    if (row + 1 == (int) (matrix.getDimension1 () / 2) + 1) {
                                        rowDelimiters [0] = 8;
                                        rowDelimiters [1] = 9;
                                    }
                                }

                                else {
                                    if (row + 1 == (int) (matrix.getDimension1 () / 2)) {
                                        rowDelimiters [0] = 4;
                                        rowDelimiters [1] = 5;
                                    }

                                    else if (row + 1 == ((int) (matrix.getDimension1 () / 2)) + 1) {
                                        rowDelimiters [0] = 6;
                                        rowDelimiters [1] = 7;
                                    }
                                }
                            }
                        }
                    }
                }
                
                matrixString += delimiters.get (delimiterGroup).get (rowDelimiters [0]);
                
                for (int column = 0; column < matrix.getDimension2 (); column++) {
                    matrixString += numberToString (matrix.getEntry (row, column)) + ((column < matrix.getDimension2 () - 1) ? '_' : "");
                }
                
                matrixString += delimiters.get (delimiterGroup).get (rowDelimiters [1]) + ((row < matrix.getDimension1 () - 1) ? '$' : "");
            }
            
            String newMatrixString = "";
            
            ArrayList <String> rowStrings = new ArrayList <String> ();
            
            for (StringTokenizer rowTokens = new StringTokenizer (matrixString, "$"); rowTokens.hasMoreTokens (); rowStrings.add (rowTokens.nextToken ())) {}
            
            ArrayList <ArrayList <String>> elementStrings = new ArrayList <ArrayList <String>> ();
            int largestStringLength = 0;
            
            for (int row = 0; row < matrix.getDimension1 (); row++) {
                elementStrings.add (new ArrayList <String> ());
                int currentStringLength;
                
                for (
                    StringTokenizer elementTokens = new StringTokenizer (rowStrings.get (row), "_");
                    elementTokens.hasMoreTokens ();
                    largestStringLength = (currentStringLength > largestStringLength) ? currentStringLength : largestStringLength
                ) {
                    String elementString = elementTokens.nextToken ();
                    elementStrings.get (row).add (elementString);
                    
                    currentStringLength =
                        elementString.length () -
                        (
                            (
                                matrix.getDimension2 () - elementTokens.countTokens () == 0 ||
                                matrix.getDimension2 () - elementTokens.countTokens () == matrix.getDimension2 () - 1
                            )
                            ? (
                                (style == Styles.DOUBLEPIPES)
                                    ? 2
                                    : 1
                            )
                            : 0
                        )
                    ;
                }
            }
            
            for (int row = 0; row < matrix.getDimension1 (); row++) {
                for (int column = 0; column < matrix.getDimension2 (); column++) {
                    String newElementString = "";
                    
                    if ((matrix.getDimension2 () & 1) == 1) {
                        if (column + 1 < (int) (matrix.getDimension2 () / 2) + 1) {
                            newElementString =
                                String.format ("%-" + largestStringLength + "s", elementStrings.get (row).get (column)) +
                                ((column + 1 < (int) (matrix.getDimension2 () / 2)) ? "   " : "")
                            ;
                        }
                    
                        else if (column + 1 == (int) (matrix.getDimension2 () / 2) + 1) {
                            int lengthDiff = largestStringLength - elementStrings.get (row).get (column).length ();
                            
                            for (
                                int spaces = ((lengthDiff & 1) == 1) ? (lengthDiff / 2) + 1 : (lengthDiff / 2);
                                spaces > 0;
                                spaces--
                            ) {
                                newElementString += ' ';
                            }
                            
                            newElementString += elementStrings.get (row).get (column);
                            
                            for (
                                int spaces = lengthDiff / 2;
                                spaces > 0;
                                spaces--
                            ) {
                                newElementString += ' ';
                            }
                        }
                        
                        else {
                            newElementString =
                                String.format ("%" + largestStringLength + "s", elementStrings.get (row).get (column)) +
                                ((column + 1 < matrix.getDimension2 ()) ? "   " : "")
                            ;
                        }
                    }
                
                    else {
                        if (column + 1 <= matrix.getDimension2 () / 2) {
                            newElementString =
                                String.format ("%-" + largestStringLength + "s", elementStrings.get (row).get (column)) +
                                ((column + 1 < matrix.getDimension2 ()) ? "   " : "")
                            ;
                        }
                        
                        else {
                            newElementString =
                                String.format ("%" + largestStringLength + "s", elementStrings.get (row).get (column)) +
                                ((column + 1 < matrix.getDimension2 ()) ? "   " : "")
                            ;
                        }
                    }
                    
                    newMatrixString += newElementString;
                    
                    if (column == matrix.getDimension2 () - 1) {
                        newMatrixString += "\n";
                    }
                }
            }
            
            matrixString = newMatrixString;
        }
        
        return matrixString;
    }
    
    public static Apcomplex parseNumber (String numberString) {
        if (numberString == null) {
            return null;
        }
        
        for (int i = 0; i < numberString.length (); i++) {
            if (
                (numberString.charAt (i) == '(' && i != 0) ||
                (numberString.charAt (i) == ')' && i != numberString.length () - 1)
            ) {
                return null;
            }
        }
        
        numberString = numberString.replaceFirst ("(", "").replaceFirst (")", "").replaceAll (" ", "");
        
        String [] numberPartsString = numberString.split (",");
        
        if (numberPartsString.length != 2) {
            return null;
        }
        
        Apfloat [] numberPartsApfloat = {null, null};
        
        try {
            numberPartsApfloat [0] = new Apfloat (numberPartsString [0]);
            numberPartsApfloat [1] = new Apfloat (numberPartsString [1]);
        }
        
        catch (NumberFormatException e) {
            return null;
        }
        
        return new Apcomplex (numberPartsApfloat [0], numberPartsApfloat [1]);
    }
}
