package org.jmatrix.MatrixTypes;

import java.util.ArrayList;

import org.apfloat.Apcomplex;

public interface SquareProperties {
    public ArrayList <Apcomplex> minors ();
    public SquareMatrix inverse ();
    public boolean isLowerTriangular ();
    public boolean isUpperTriangular ();
    public boolean isAntiDiagonal ();
    public boolean isSymmetric ();
    public boolean isAntisymmetric ();
    public boolean isCentrosymmetric ();
    public boolean isPersymmetric ();
    public boolean isBisymmetric ();
    public boolean isHermitian ();
    public boolean isAntihermitian ();
    public boolean isHankel ();
    public boolean isInvolutory ();
    public boolean isIdempotent ();
    public boolean isIdentityMatrix ();
    public boolean isHadamard ();
    public boolean isOrthogonal ();
    public boolean isUnitary ();
    public boolean isNormal ();
    public boolean isRightStochastic ();
    public boolean isLeftStochastic ();
    public boolean isDoublyStochastic ();
    public boolean isHollow ();
    public boolean isP ();
    public boolean isM ();
}