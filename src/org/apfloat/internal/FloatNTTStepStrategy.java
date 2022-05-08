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
package org.apfloat.internal;

import org.apfloat.ApfloatRuntimeException;
import org.apfloat.spi.ArrayAccess;
import org.apfloat.spi.NTTStepStrategy;
import static org.apfloat.internal.FloatModConstants.*;

/**
 * Common methods to calculate Fast Number Theoretic Transforms
 * in parallel using multiple threads.<p>
 *
 * All access to this class must be externally synchronized.
 *
 * @since 1.7.0
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class FloatNTTStepStrategy
    extends FloatTableFNT
    implements NTTStepStrategy, Parallelizable
{
    // Runnable for calculating the row transforms in parallel
    private class TableFNTRunnable
        implements Runnable
    {
        public TableFNTRunnable(int length, boolean isInverse, ArrayAccess arrayAccess, float[] wTable, int[] permutationTable)
        {
            this.length = length;               // Transform length
            this.isInverse = isInverse;
            this.arrayAccess = arrayAccess;
            this.wTable = wTable;
            this.permutationTable = permutationTable;
        }

        @Override
        public void run()
        {
            int maxI = this.arrayAccess.getLength();

            for (int i = 0; i < maxI; i += this.length)
            {
                ArrayAccess arrayAccess = this.arrayAccess.subsequence(i, this.length);

                if (this.isInverse)
                {
                    inverseTableFNT(arrayAccess, this.wTable, this.permutationTable);
                }
                else
                {
                    tableFNT(arrayAccess, this.wTable, this.permutationTable);
                }
            }
        }

        private int length;
        private boolean isInverse;
        private ArrayAccess arrayAccess;
        private float[] wTable;
        private int[] permutationTable;
    }

    // Runnable for multiplying elements in the matrix
    private class MultiplyRunnable
        implements Runnable
    {
        public MultiplyRunnable(ArrayAccess arrayAccess, int startRow, int startColumn, int rows, int columns, float w, float scaleFactor)
        {
            this.arrayAccess = arrayAccess;
            this.startRow = startRow;
            this.startColumn = startColumn;
            this.rows = rows;
            this.columns = columns;
            this.w = w;
            this.scaleFactor = scaleFactor;
        }

        @Override
        public void run()
        {
            float[] data = this.arrayAccess.getFloatData();
            int position = this.arrayAccess.getOffset();
            float rowFactor = modPow(this.w, (float) this.startRow);
            float columnFactor = modPow(this.w, (float) this.startColumn);
            float rowStartFactor = modMultiply(this.scaleFactor, modPow(rowFactor, (float) this.startColumn));

            for (int i = 0; i < this.rows; i++)
            {
                float factor = rowStartFactor;

                for (int j = 0; j < this.columns; j++, position++)
                {
                    data[position] = modMultiply(data[position], factor);
                    factor = modMultiply(factor, rowFactor);
                }

                rowFactor = modMultiply(rowFactor, this.w);
                rowStartFactor = modMultiply(rowStartFactor, columnFactor);
            }
         }

        private ArrayAccess arrayAccess;
        private int startRow;
        private int startColumn;
        private int rows;
        private int columns;
        private float w;
        private float scaleFactor;
    }

    /**
     * Default constructor.
     */

    public FloatNTTStepStrategy()
    {
    }

    @Override
    public void multiplyElements(ArrayAccess arrayAccess, int startRow, int startColumn, int rows, int columns, long length, long totalTransformLength, boolean isInverse, int modulus)
        throws ApfloatRuntimeException
    {
        ParallelRunnable parallelRunnable = createMultiplyElementsParallelRunnable(arrayAccess, startRow, startColumn, rows, columns, length, totalTransformLength, isInverse, modulus);

        ParallelRunner.runParallel(parallelRunnable);
    }

    @Override
    public void transformRows(ArrayAccess arrayAccess, int length, int count, boolean isInverse, boolean permute, int modulus)
        throws ApfloatRuntimeException
    {
        ParallelRunnable parallelRunnable = createTransformRowsParallelRunnable(arrayAccess, length, count, isInverse, permute, modulus);

        ParallelRunner.runParallel(parallelRunnable);
    }

    @Override
    public long getMaxTransformLength()
    {
        return MAX_TRANSFORM_LENGTH;
    }

    /**
     * Create a ParallelRunnable object for multiplying the elements of the matrix.
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
     *
     * @return An object suitable for multiplying the elements of the matrix in parallel.
     */

    protected ParallelRunnable createMultiplyElementsParallelRunnable(ArrayAccess arrayAccess, int startRow, int startColumn, int rows, int columns, long length, long totalTransformLength, boolean isInverse, int modulus)
        throws ApfloatRuntimeException
    {
        setModulus(MODULUS[modulus]);
        float w = (isInverse ?
                     getInverseNthRoot(PRIMITIVE_ROOT[modulus], length) :
                     getForwardNthRoot(PRIMITIVE_ROOT[modulus], length)),
                scaleFactor = (isInverse ?
                               modDivide((float) 1, (float) totalTransformLength) :
                               (float) 1);

        ParallelRunnable parallelRunnable = new ParallelRunnable(rows)
        {
            @Override
            public Runnable getRunnable(int strideStartRow, int strideRows)
            {
                ArrayAccess subArrayAccess = arrayAccess.subsequence(strideStartRow * columns, strideRows * columns);
                return new MultiplyRunnable(subArrayAccess, startRow + strideStartRow, startColumn, strideRows, columns, w, scaleFactor);
            }
        };

        return parallelRunnable;
    }

    /**
     * Create a ParallelRunnable object for transforming the rows of the matrix.
     *
     * @param arrayAccess The memory array to split to rows and to transform.
     * @param length Length of one transform (one row).
     * @param count Number of rows.
     * @param isInverse <code>true</code> if an inverse transform is performed, <code>false</code> if a forward transform is performed.
     * @param permute If permutation should be done.
     * @param modulus Index of the modulus.
     *
     * @return An object suitable for transforming the rows of the matrix in parallel.
     */

    protected ParallelRunnable createTransformRowsParallelRunnable(ArrayAccess arrayAccess, int length, int count, boolean isInverse, boolean permute, int modulus)
        throws ApfloatRuntimeException
    {
        setModulus(MODULUS[modulus]);
        float[] wTable = (isInverse ?
                            FloatWTables.getInverseWTable(modulus, length) :
                            FloatWTables.getWTable(modulus, length));
        int[] permutationTable = (permute ? Scramble.createScrambleTable(length) : null);

        ParallelRunnable parallelRunnable = new ParallelRunnable(count)
        {
            @Override
            public Runnable getRunnable(int startIndex, int strideCount)
            {
                ArrayAccess subArrayAccess = arrayAccess.subsequence(startIndex * length, strideCount * length);
                return new TableFNTRunnable(length, isInverse, subArrayAccess, wTable, permutationTable);
            }
        };

        return parallelRunnable;
    }
}
