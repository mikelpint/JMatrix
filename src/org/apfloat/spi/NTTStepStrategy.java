/*
 * MIT License
 *
 * Copyright (c) 2002-2021 Mikko Tommila
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.apfloat.spi;

import org.apfloat.ApfloatContext;
import org.apfloat.ApfloatRuntimeException;

/**
 * Steps for the six-step or two-pass NTT.
 *
 * @since 1.7.0
 * @version 1.7.0
 * @author Mikko Tommila
 */

public interface NTTStepStrategy
{
    /**
     * Multiply each matrix element <code>(i, j)</code> by <code>w<sup>i * j</sup> / totalTransformLength</code>.
     * The matrix size is n<sub>1</sub> x n<sub>2</sub>.
     *
     * @param arrayAccess The memory array to multiply.
     * @param startRow Which row in the whole matrix the starting row in the <code>arrayAccess</code> is.
     * @param startColumn Which column in the whole matrix the starting column in the <code>arrayAccess</code> is.
     * @param rows The number of rows in the <code>arrayAccess</code> to multiply.
     * @param columns The number of columns in the matrix (= n<sub>2</sub>).
     * @param length The length of data in the matrix being transformed.
     * @param totalTransformLength The total transform length, for the scaling factor. Used only for the inverse case.
     * @param isInverse If the multiplication is done for the inverse transform or not.
     * @param modulus Index of the modulus.
     */

    public void multiplyElements(ArrayAccess arrayAccess, int startRow, int startColumn, int rows, int columns, long length, long totalTransformLength, boolean isInverse, int modulus)
        throws ApfloatRuntimeException;

    /**
     * Transform the rows of the data matrix.
     * If only one processor is available, it runs all transforms in the current
     * thread. If more than one processor are available, it dispatches the calculations
     * to multiple threads to parallelize the calculation. The number of processors is
     * determined using {@link ApfloatContext#getNumberOfProcessors()}.
     *
     * @param arrayAccess The memory array to split to rows and to transform.
     * @param length Length of one transform (one row).
     * @param count Number of rows.
     * @param isInverse <code>true</code> if an inverse transform is performed, <code>false</code> if a forward transform is performed.
     * @param permute If permutation should be done.
     * @param modulus Index of the modulus.
     */

    public void transformRows(ArrayAccess arrayAccess, int length, int count, boolean isInverse, boolean permute, int modulus)
        throws ApfloatRuntimeException;

    /**
     * Get the maximum transform length.
     *
     * @return The maximum transform length.
     */

    public long getMaxTransformLength();
}
