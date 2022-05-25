package org.jmatrix.internal;

import java.util.ArrayList;

import org.apfloat.Apcomplex;
import org.apfloat.Apint;
import org.apfloat.ApintMath;

import org.jmatrix.MatrixTypes.Matrix;

public class JMath {
    public static Apint binomialCoefficient (long n, long k) {
        if (n < 0 || k < 0 || n < k) {
            return null;
        }
        
        return ApintMath.factorial (n).divide (ApintMath.factorial (k).multiply (ApintMath.factorial (n - k)));
    }
    
    public static Apint moebius (long n) {
        Apint result = Apint.ZERO;
        
        if (n % 2 == 0) {
            result = result.add (Apint.ONE);
            
            if ((n / 2) % 2 == 0) {
                return Apint.ZERO;
            }
        }
        
        for (int i = 3; i <= Math.sqrt (n); i += 2) {
            if (n % i == 0) {
                result = result.add (Apint.ONE);
                
                if ((n / i) % i == 0) {
                    return Apint.ZERO;
                }
            }
        }
        
        if (result.mod (new Apint (2)).equals (Apint.ZERO)) {
            result = Apint.ONE.negate ();
        }
        
        else {
            result = Apint.ONE;
        }
        
        return result;
    }
    
    public static Apint mertens (long n) {
        Apint result = Apint.ZERO;
        
        for (int i = 1; i <= n; i++) {
            result = result.add (moebius (i));
        }
        
        return result;
    }
    
    public static Apint LucasSequenceU (int n, Apint p, Apint q) {
        if (n < 0) {
            return null;
        }
        
        else if (n == 0) {
            return Apint.ZERO;
        }
        
        else if (n == 1) {
            return Apint.ONE;
        }
        
        return p.multiply (LucasSequenceU (n - 1, p, q).subtract (q.multiply (LucasSequenceU (n - 2, p, q))));
    }
    
    public static Apint LucasSequenceV (int n, Apint p, Apint q) {
        if (n < 0) {
            return null;
        }
        
        else if (n == 0) {
            return new Apint (2);
        }
        
        else if (n == 1) {
            return p;
        }
        
        return p.multiply (LucasSequenceV (n - 1, p, q).subtract (q.multiply (LucasSequenceV (n - 2, p, q))));
    }
    
    public static ArrayList <ArrayList <Apcomplex>> LucasSequenceMatrix (int dimension1, int dimension2, Apint p, Apint q, boolean u) {
        ArrayList <ArrayList <Apcomplex>> matrix = Matrix.generateEmptyMatrix (dimension1, dimension2);
        
        
        if (u) {
            for (int row = 0; row < dimension1; row++) {
                for (int column = 0; column < dimension2; column++) {
                    matrix.get (row).set (column, LucasSequenceU (row * dimension2 + column, p, q));
                }
            }
            
            return matrix;
        }
        
        for (int row = 0; row < dimension1; row++) {
            for (int column = 0; column < dimension2; column++) {
                matrix.get (row).set (column, LucasSequenceV (row * dimension2 + column, p, q));
            }
        }
        
        return matrix;
    }
    
    public static ArrayList <ArrayList <Apcomplex>> Fibonacci (int dimension1, int dimension2) {
        return LucasSequenceMatrix (dimension1, dimension2, Apint.ONE, Apint.ONE.negate (), true);
    }
    
    public static ArrayList <ArrayList <Apcomplex>> Lucas (int dimension1, int dimension2) {
        return LucasSequenceMatrix (dimension1, dimension2, Apint.ONE, Apint.ONE.negate (), false);
    }
    
    public static ArrayList <ArrayList <Apcomplex>> Pell (int dimension1, int dimension2) {
        return LucasSequenceMatrix (dimension1, dimension2, new Apint (2), Apint.ONE.negate (), true);
    }
    
    public static ArrayList <ArrayList <Apcomplex>> PellLucas (int dimension1, int dimension2) {
        return LucasSequenceMatrix (dimension1, dimension2, new Apint (2), Apint.ONE.negate (), false);
    }
    
    public static ArrayList <ArrayList <Apcomplex>> Jacobsthal (int dimension1, int dimension2) {
        return LucasSequenceMatrix (dimension1, dimension2, Apint.ONE, new Apint (-2), true);
    }
    
    public static ArrayList <ArrayList <Apcomplex>> JacobsthalLucas (int dimension1, int dimension2) {
        return LucasSequenceMatrix (dimension1, dimension2, Apint.ONE, new Apint (-2), false);
    }
    
    public static ArrayList <ArrayList <Apcomplex>> Mersenne (int dimension1, int dimension2) {
        return LucasSequenceMatrix (dimension1, dimension2, new Apint (3), new Apint (2), true);
    }
    
    public static ArrayList <ArrayList <Apcomplex>> twoNPlusOne (int dimension1, int dimension2) {
        return LucasSequenceMatrix (dimension1, dimension2, new Apint (3), new Apint (2), false);
    }
    
    public static ArrayList <ArrayList <Apcomplex>> sqrtSquareTriangNums (int dimension1, int dimension2) {
        return LucasSequenceMatrix (dimension1, dimension2, new Apint (6), Apint.ONE, true);
    }
}
