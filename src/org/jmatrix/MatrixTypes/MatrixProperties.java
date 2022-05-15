package org.jmatrix.MatrixTypes;

import java.util.ArrayList;

public interface MatrixProperties {
    public Matrix transpose ();
    public Matrix transjugate ();
    public Matrix negate ();
    public ArrayList <Matrix> principalSubmatrices ();
    public boolean isReal ();
    public boolean isInteger ();
    public boolean isSingular ();
    public boolean isRowEchelon ();
    public boolean isDiagonal ();
    public boolean isBidiagonal ();
    public boolean isLowerBidiagonal ();
    public boolean isUpperBidiagonal ();
    public boolean isTridiagonal ();
    public boolean isPentadiagonal ();
    public boolean isToeplitz ();
    public boolean isNonnegative ();
    public boolean isPositive ();
    public boolean isMetzler ();
    public boolean isZ ();
    public boolean isL ();
}
