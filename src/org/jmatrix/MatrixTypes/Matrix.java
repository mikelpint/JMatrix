package org.jmatrix.MatrixTypes;

import java.util.ArrayList;
import java.util.HashSet;
import java.io.Serializable;

import org.apfloat.Apcomplex;
import org.apfloat.ApcomplexMath;
import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.apfloat.Apint;
import org.apfloat.ApintMath;
import org.jmatrix.MatrixOps.MatrixOps;
import org.jmatrix.internal.JMatrixIO;
import org.jmatrix.internal.Settings;
import org.jmatrix.internal.JMatrixIO.Styles;

public class Matrix implements Vectorizable, MatrixProperties, Serializable {
    private static final long serialVersionUID = 1L;
    
    public static final int MAX_ENTRIES = 10000;
    
    public static final long NUMBER_PRECISION = 1000;
    
    public static final long MAX_RANDOM_DIGITS = 50000;
    public static final Apint MAX_RANGE = new Apint (MAX_RANDOM_DIGITS, 2);
    
    protected int dimension1;
    protected int dimension2;
    protected ArrayList <ArrayList <Apcomplex>> matrix;
    
    public Matrix (int dimension1, int dimension2) {
        super ();

        this.setDimension1 (dimension1);
        this.setDimension2 (dimension2);
        this.setMatrix (generateEmptyMatrix (dimension1, dimension2));
    }

    public Matrix (int dimension1, int dimension2, ArrayList <ArrayList <Apcomplex>> matrix) {
        super ();

        this.setDimension1 (dimension2);
        this.setDimension2 (dimension2);
        this.setMatrix (matrix);
    }
    
    public Matrix (Matrix matrix) {
        super ();
        
        this.dimension1 = matrix.dimension1;
        this.dimension2 = matrix.dimension2;
        this.setMatrix (matrix.matrix);
    }

    public int getDimension1 () {
        return dimension1;
    }

    public void setDimension1 (int dimension1) {
        if (dimension1 > 0 && dimension1 <= MAX_ENTRIES) {
            this.dimension1 = dimension1;
        }
        
        else {
            this.dimension1 = -1;
        }
    }

    public int getDimension2 () {
        return dimension2;
    }

    public void setDimension2 (int dimension2) {
        if (dimension2 > 0 && dimension2 <= MAX_ENTRIES) {
            this.dimension2 = dimension2;
        }
        
        else {
            this.dimension2 = -1;
        }
    }

    public ArrayList <ArrayList <Apcomplex>> getMatrix () {
        return matrix;
    }
    
    public void setMatrix (ArrayList <ArrayList <Apcomplex>> matrix) {
        if (this.dimension1 == -1 || this.dimension2 == -1 || matrix.size () != this.dimension1) {
            return;
        }
        
        for (int row = 0; row < matrix.size (); row++) {
            if (matrix.get (row).size () != this.dimension2) {
                return;
            }
        }
        
        this.matrix = new ArrayList <ArrayList <Apcomplex>> ();
        
        for (int row = 0; row < matrix.size (); row++) {
            this.matrix.add (new ArrayList <Apcomplex> ());
            
            for (int column = 0; column < matrix.get (0).size (); column++) {
                this.matrix.get (row).add (matrix.get (row).get (column));
            }
        }
    }
    
    public ArrayList <Apcomplex> getRow (int row) {
        if (row < 0 || row >= this.dimension1) {
            return null;
        }
        
        return this.matrix.get (row);
    }
    
    public void setRow (int rowIndex, ArrayList <Apcomplex> row) {
        if (row == null || row.size () != this.dimension2) {
            return;
        }
        
        for (int column = 0; column < row.size (); column++) {
            this.matrix.get (rowIndex).set (column, row.get (column));
        }
    }
    
    public Apcomplex getEntry (int... position) {
        if (position [0] < 0 || position [0] >= this.dimension1 || position [1] < 0 || position [1] >= this.dimension2) {
            return null;
        }
        
        return this.matrix.get (position [0]).get (position [1]);
    }
    
    public void setEntry (Apcomplex entry, int... position) {
        if (entry == null || position [0] < 0 || position [0] >= this.dimension1 || position [1]  < 0 || position [1] >= this.dimension2) {
            return;
        }
        
        this.matrix.get (position [0]).set (position [1], entry);
    }
    
    public boolean equals (Matrix matrix) {
        if (this.dimension1 != matrix.dimension1 || this.dimension2 != matrix.dimension2) {
            return false;
        }
        
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                if (!(this.getEntry (row, column).equals (matrix.getEntry (row, column)))) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public String toString () {
        Styles defaultStyle = null;
        
        if (Settings.settings.get ("Style") == "Array") {
            defaultStyle = Styles.ARRAY;
        }
        
        else if (Settings.settings.get ("Style") == "List") {
            defaultStyle = Styles.LIST;
        }
        
        else if (Settings.settings.get ("Style") == "Positional") {
            defaultStyle = Styles.POSITIONAL;
        }
        
        else if (Settings.settings.get ("Style") == "Brackets") {
            defaultStyle = Styles.BRACKETS;
        }
        
        else if (Settings.settings.get ("Style") == "Braces") {
            defaultStyle = Styles.BRACES;
        }
        
        else if (Settings.settings.get ("Style") == "Parentheses") {
            defaultStyle = Styles.PARENTHESES;
        }
        
        else if (Settings.settings.get ("Style") == "Pipes") {
            defaultStyle = Styles.PIPES;
        }
        
        else if (Settings.settings.get ("Style") == "Double pipes") {
            defaultStyle = Styles.DOUBLEPIPES;
        }
        
        return JMatrixIO.matrixToString (this, defaultStyle);
    }
    
    public static ArrayList <ArrayList <Apcomplex>> generateEmptyMatrix (int dimension1, int dimension2) {
        ArrayList <ArrayList <Apcomplex>> matrixEntries = new ArrayList <ArrayList <Apcomplex>> ();
        
        for (int row = 0; row < dimension1; row++) {
            matrixEntries.add (new ArrayList <Apcomplex> ());
            
            for (int column = 0; column < dimension2; column++) {
                matrixEntries.get (row).add (Apcomplex.ZERO);
            }
        }
        
        return matrixEntries;
    }
    
    public void randomizeElements (Apint range, boolean complex) {
        Apcomplex entry = Apcomplex.ZERO;
        
        if (range == null) {
            range = MAX_RANGE.toRadix (10);
        }
        
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                entry = entry.add (
                    new Apcomplex (
                        ApfloatMath.random (MAX_RANDOM_DIGITS).multiply (range.add (Apint.ONE)).truncate ()
                    )
                );
                
                if (ApintMath.random (MAX_RANDOM_DIGITS).mod (new Apint (2)).equals (Apint.ONE)) {
                    entry = entry.negate ();
                }
                
                if (complex) {
                    entry = entry.add (
                        new Apcomplex (
                            Apfloat.ZERO,
                            ApfloatMath.random (MAX_RANDOM_DIGITS).multiply (range.add (Apint.ONE)).truncate ()
                        )
                    );
                    
                    if (ApintMath.random (MAX_RANDOM_DIGITS).mod (new Apint (2)).equals (Apint.ONE)) {
                        entry = entry.conj ();
                    }
                }
                
                this.setEntry (entry, row, column);
                entry = Apcomplex.ZERO;
            }
        }
    }
    
    @Override
    public Matrix transpose () {
        if (this.matrix == null) {
            return null;
        }

        Matrix matrixTranspose = new Matrix (this);
    
        for (int row = 0; row < this.dimension2; row++) {
            for (int column = 0; column < this.dimension1; column++) {
                matrixTranspose.setEntry (this.getEntry (column, row), row, column);
            }
        }
        
        return matrixTranspose;
    }
    
    @Override
    public Matrix transjugate () {
        if (this.matrix == null) {
            return null;
        }
        
        Matrix matrixTransjugate = this.transpose ();
        
        if (!this.isReal ()) {
            for (int row = 0; row < this.dimension2; row++) {
                for (int column = 0; column < this.dimension1; column++) {
                    matrixTransjugate.setEntry (matrixTransjugate.getEntry (row, column).conj (), row, column);
                }
            }
        }
        
        return matrixTransjugate;
    }
    
    @Override
    public Matrix negate () {
        if (this.matrix == null) {
            return null;
        }
        
        Matrix matrixNegate = new Matrix (this.dimension1, this.dimension2);
        
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                matrixNegate.setEntry (this.getEntry (row, column).negate (), row, column);
            }
        }
        
        return matrixNegate;
    }
    
    // No sé qué es lo que pasa exactamente con esta función que no va :(
    @Override
    public ArrayList <Matrix> principalSubmatrices () {
//        if (this.matrix == null || this.dimension1 <= 1 || this.dimension2 <= 1) {
//            return null;
//        }
//        
//        ArrayList <Matrix> submatrices = new ArrayList <Matrix> ();
//        
//        HashSet <int []> deletionSets = new HashSet <int []> ();
//        
//        for (int row = 0; row < this.dimension1; row++) {
//            for (int column = 0; column < this.dimension2; column++) {
//                deletionSets.add (new int [] {row, column});
//            }
//        }
//        
//        for (int [] deletionSet : deletionSets) {
//            ArrayList <ArrayList <Apcomplex>> submatrixEntries = new ArrayList <ArrayList <Apcomplex>> ();
//            
//            for (int row = 0; row < this.dimension1; row++) {
//                if (row != deletionSet [0]) {
//                    submatrixEntries.add (this.getRow (row));
//                }
//            }
//            
//            for (int row = 0; row < submatrixEntries.size (); row++) {
//                submatrixEntries.get (row).remove (deletionSet [1]);
//            }
//            
//            submatrices.add (new Matrix (this.dimension1 - 1, this.dimension2 - 1, submatrixEntries));
//        }
//        
//        return submatrices;
        return null;
    }
    
    public Apcomplex modulus () {
        if (!(this instanceof Vector) && !(this instanceof RowVector)) {
            return null;
        }
        
        Apcomplex modulus = Apcomplex.ZERO;
        
        for (int element = 0; element < Math.max (this.dimension1, this.dimension2); element++) {
            modulus = modulus.add (ApcomplexMath.pow (this.getEntry (element), 2));
        }
        
        return ApcomplexMath.sqrt (modulus);
    }

    @Override
    public Vector vectorization () {
        if (this.matrix == null) {
            return null;
        }

        Vector vector = new Vector (this.dimension1 * this.dimension2);
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                vector.setEntry (this.getEntry (row, column), row * this.dimension2 + column);
            }
        }
        
        return vector;
    }
    
    // No sé por qué pero no va
    @Override
    public Vector columnMajorVectorization () {
//        if (this.matrix == null) {
//            return null;
//        }
//        
//        ArrayList <ArrayList <Apcomplex>> vectorEntries = MatrixOps.matrixMultiplication (
//            new CommutationMatrix (this.dimension1, this.dimension2),
//            this.vectorization ()
//        ).matrix;
//        
//        return new Vector (this.dimension1 * this.dimension2, vectorEntries);
        return null;
    }
    
    @Override
    public RowVector toRowVector () {
        if (this.matrix == null) {
            return null;
        }

        return this.vectorization ().toRowVector ();
    }
    
    @Override
    public HalfVector halfVectorization () {
        return null;
    }
    
    @Override
    public boolean isReal () {
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                if (!this.getEntry (row, column).equals (Apfloat.ZERO)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isInteger () {
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                if (!this.getEntry (row, column).isInteger ()) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isSingular () {
        return !(MatrixOps.calculateRank (this, null) < this.dimension1);
    }
    
    @Override
    public boolean isRowEchelon () {
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                if (column < row && !this.getEntry (row, column).equals (Apcomplex.ZERO)) {
                   return false; 
                }
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isDiagonal () {
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                if (row != column && !this.getEntry (row, column).equals (Apcomplex.ZERO)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isBidiagonal () {
        return (this.isLowerBidiagonal () || this.isUpperBidiagonal ());
    }
    
    @Override
    public boolean isLowerBidiagonal () {
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                if (
                    (
                        this.getEntry (row, column).equals (Apcomplex.ZERO) && (row == column || row == column + 1)
                    ) ||
                    (
                        !this.getEntry (row, column).equals (Apcomplex.ZERO) && row != column + 1
                    )
                ) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isUpperBidiagonal () {
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                if (
                    (
                        this.getEntry (row, column).equals (Apcomplex.ZERO) && (row == column || row == column - 1)
                    ) ||
                    (
                        !this.getEntry (row, column).equals (Apcomplex.ZERO) && row != column - 1
                    )
                ) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isTridiagonal () {
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                if (
                    (
                        (
                            row == column - 1 ||
                            row == column ||
                            row == column + 1
                        ) &&
                        this.getEntry (row, column).equals (Apcomplex.ZERO)
                    ) ||
                    (
                        (
                            row != column - 1 ||
                            row != column ||
                            row != column + 1
                        ) &&
                        this.getEntry (row, column).equals (Apcomplex.ZERO)
                    )
                ) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isPentadiagonal () {
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                if (
                    (
                        (
                            row == column - 2 ||
                            row == column - 1 ||
                            row == column ||
                            row == column + 1 ||
                            row == column + 2
                        ) &&
                        this.getEntry (row, column).equals (Apcomplex.ZERO)
                    ) ||
                    (
                        (
                            row != column - 2 ||
                            row != column - 1 ||
                            row != column ||
                            row != column + 1 ||
                            row != column + 2
                        ) &&
                        this.getEntry (row, column).equals (Apcomplex.ZERO)
                    )
                ) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isToeplitz () {
        for (int row = 0; row < this.dimension1 - 1; row++) {
            for (int column = 0; column < this.dimension2 - 1; column++) {
                if (!this.getEntry (row, column).equals (this.getEntry (row + 1, column + 1))) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isNonnegative () {
        if (!this.isReal ()) {
            return false;
        }
        
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                if (this.getEntry (row, column).real ().signum () == -1) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isPositive () {
        if (!this.isReal ()) {
            return false;
        }
        
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                if (this.getEntry (row, column).real ().signum () <= 0) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isMetzler () {
        if (!this.isReal ()) {
            return false;
        }
        
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                if (row != column && ApcomplexMath.abs (this.getEntry (row, column)).compareTo (Apfloat.ZERO) == -1) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isZ () {
        return this.negate ().isMetzler ();
    }
    
    @Override
    public boolean isL () {
        if (!this.isReal ()) {
            return false;
        }
        
        if (!this.isZ ()) {
            return false;
        }
        
        for (int row = 0; row < this.dimension1; row++) {
            for (int column = 0; column < this.dimension2; column++) {
                if (row == column && ApcomplexMath.abs (this.getEntry (row, column)).compareTo (Apcomplex.ZERO) != 1) {
                    return false;
                }
            }
        }
        
        return true;
    }
}