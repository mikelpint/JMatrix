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

import java.math.BigInteger;

import org.apfloat.ApfloatRuntimeException;
import org.apfloat.spi.CarryCRTStepStrategy;
import org.apfloat.spi.DataStorage;
import static org.apfloat.internal.FloatModConstants.*;

/**
 * Class for performing the final steps of a three-modulus
 * Number Theoretic Transform based convolution. Works for the
 * <code>float</code> type.<p>
 *
 * All access to this class must be externally synchronized.
 *
 * @since 1.7.0
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class FloatCarryCRTStepStrategy
    extends FloatCRTMath
    implements CarryCRTStepStrategy<float[]>
{
    /**
     * Creates a carry-CRT steps object using the specified radix.
     *
     * @param radix The radix that will be used.
     */

    public FloatCarryCRTStepStrategy(int radix)
    {
        super(radix);
    }

    @Override
    public float[] crt(DataStorage resultMod0, DataStorage resultMod1, DataStorage resultMod2, DataStorage dataStorage, long size, long resultSize, long offset, long length)
        throws ApfloatRuntimeException
    {
        long skipSize = (offset == 0 ? size - resultSize + 1: 0);   // For the first block, ignore the first 1-3 elements
        long lastSize = (offset + length == size ? 1: 0);           // For the last block, add 1 element
        long nonLastSize = 1 - lastSize;                            // For the other than last blocks, move 1 element
        long subResultSize = length - skipSize + lastSize;

        long subStart = size - offset,
             subEnd = subStart - length,
             subResultStart = size - offset - length + nonLastSize + subResultSize,
             subResultEnd = subResultStart - subResultSize;

        DataStorage.Iterator src0 = resultMod0.iterator(DataStorage.READ, subStart, subEnd),
                             src1 = resultMod1.iterator(DataStorage.READ, subStart, subEnd),
                             src2 = resultMod2.iterator(DataStorage.READ, subStart, subEnd);
        try (DataStorage.Iterator dst = dataStorage.iterator(DataStorage.WRITE, subResultStart, subResultEnd))
        {
            float[] carryResult = new float[3],
                      sum = new float[3],
                      tmp = new float[3];

            // Preliminary carry-CRT calculation (happens in parallel in multiple blocks)
            for (long i = 0; i < length; i++)
            {
                float y0 = MATH_MOD_0.modMultiply(T0, src0.getFloat()),
                        y1 = MATH_MOD_1.modMultiply(T1, src1.getFloat()),
                        y2 = MATH_MOD_2.modMultiply(T2, src2.getFloat());

                multiply(M12, y0, sum);
                multiply(M02, y1, tmp);

                if (add(tmp, sum) != 0 ||
                    compare(sum, M012) >= 0)
                {
                    subtract(M012, sum);
                }

                multiply(M01, y2, tmp);

                if (add(tmp, sum) != 0 ||
                    compare(sum, M012) >= 0)
                {
                    subtract(M012, sum);
                }

                add(sum, carryResult);

                float result = divide(carryResult);

                // In the first block, ignore the first element (it's zero in full precision calculations)
                // and possibly one or two more in limited precision calculations
                if (i >= skipSize)
                {
                    dst.setFloat(result);
                    dst.next();
                }

                src0.next();
                src1.next();
                src2.next();
            }

            // Calculate the last words (in base math)
            float result0 = divide(carryResult);
            float result1 = carryResult[2];

            assert (carryResult[0] == 0);
            assert (carryResult[1] == 0);

            // Last block has one extra element (corresponding to the one skipped in the first block)
            if (subResultSize == length - skipSize + 1)
            {
                dst.setFloat(result0);

                result0 = result1;
                assert (result1 == 0);
            }

            float[] results = { result1, result0 };

            return results;
        }
    }

    @Override
    public float[] carry(DataStorage dataStorage, long size, long resultSize, long offset, long length, float[] results, float[] previousResults)
        throws ApfloatRuntimeException
    {
        long skipSize = (offset == 0 ? size - resultSize + 1: 0);   // For the first block, ignore the first 1-3 elements
        long lastSize = (offset + length == size ? 1: 0);           // For the last block, add 1 element
        long nonLastSize = 1 - lastSize;                            // For the other than last blocks, move 1 element
        long subResultSize = length - skipSize + lastSize;

        long subResultStart = size - offset - length + nonLastSize + subResultSize,
             subResultEnd = subResultStart - subResultSize;

        // Get iterators for the previous block carries, and dst, padded with this block's carries
        // Note that size could be 1 but carries size is 2
        DataStorage.Iterator src = arrayIterator(previousResults);
        try (DataStorage.Iterator dst = compositeIterator(dataStorage.iterator(DataStorage.READ_WRITE, subResultStart, subResultEnd), subResultSize, arrayIterator(results)))
        {
            // Propagate base addition through dst, and this block's carries
            float carry = baseAdd(dst, src, 0, dst, previousResults.length);
            carry = baseCarry(dst, carry, subResultSize);

            assert (carry == 0);
        }                                                           // Iterator likely was not iterated to end

        return results;
    }

    private float baseCarry(DataStorage.Iterator srcDst, float carry, long size)
        throws ApfloatRuntimeException
    {
        for (long i = 0; i < size && carry > 0; i++)
        {
            carry = baseAdd(srcDst, null, carry, srcDst, 1);
        }

        return carry;
    }

    // Wrap an array in a simple reverse-order iterator, padded with zeros
    private static DataStorage.Iterator arrayIterator(float[] data)
    {
        return new DataStorage.Iterator()
        {
            @Override
            public boolean hasNext()
            {
                return true;
            }

            @Override
            public void next()
            {
                this.position--;
            }

            @Override
            public float getFloat()
            {
                assert (this.position >= 0);
                return data[this.position];
            }

            @Override
            public void setFloat(float value)
            {
                assert (this.position >= 0);
                data[this.position] = value;
            }

            private static final long serialVersionUID = 1L;

            private int position = data.length - 1;
        };
    }

    // Composite iterator, made by concatenating two iterators
    private static DataStorage.Iterator compositeIterator(DataStorage.Iterator iterator1, long size, DataStorage.Iterator iterator2)
    {
        return new DataStorage.Iterator()
        {
            @Override
            public boolean hasNext()
            {
                return (this.position < size ? iterator1.hasNext() : iterator2.hasNext());
            }

            @Override
            public void next()
                throws ApfloatRuntimeException
            {
                (this.position < size ? iterator1 : iterator2).next();
                this.position++;
            }

            @Override
            public float getFloat()
                throws ApfloatRuntimeException
            {
                return (this.position < size ? iterator1 : iterator2).getFloat();
            }

            @Override
            public void setFloat(float value)
                throws ApfloatRuntimeException
            {
                (this.position < size ? iterator1 : iterator2).setFloat(value);
            }

            @Override
            public void close()
                throws ApfloatRuntimeException
            {
                (this.position < size ? iterator1 : iterator2).close();
            }

            private static final long serialVersionUID = 1L;

            private long position;
        };
    }

    private static final long serialVersionUID = 3192182234524626533L;

    private static final FloatModMath MATH_MOD_0,
                                        MATH_MOD_1,
                                        MATH_MOD_2;
    private static final float T0,
                                 T1,
                                 T2;
    private static final float[] M01,
                                   M02,
                                   M12,
                                   M012;

    static
    {
        MATH_MOD_0 = new FloatModMath();
        MATH_MOD_1 = new FloatModMath();
        MATH_MOD_2 = new FloatModMath();

        MATH_MOD_0.setModulus(MODULUS[0]);
        MATH_MOD_1.setModulus(MODULUS[1]);
        MATH_MOD_2.setModulus(MODULUS[2]);

        // Probably sub-optimal, but it's a one-time operation

        BigInteger base = BigInteger.valueOf(Math.abs((long) MAX_POWER_OF_TWO_BASE)),   // In int case the base is 0x80000000
                   m0 = BigInteger.valueOf((long) MODULUS[0]),
                   m1 = BigInteger.valueOf((long) MODULUS[1]),
                   m2 = BigInteger.valueOf((long) MODULUS[2]),
                   m01 = m0.multiply(m1),
                   m02 = m0.multiply(m2),
                   m12 = m1.multiply(m2);

        T0 = m12.modInverse(m0).floatValue();
        T1 = m02.modInverse(m1).floatValue();
        T2 = m01.modInverse(m2).floatValue();

        M01 = new float[2];
        M02 = new float[2];
        M12 = new float[2];
        M012 = new float[3];

        BigInteger[] qr = m01.divideAndRemainder(base);
        M01[0] = qr[0].floatValue();
        M01[1] = qr[1].floatValue();

        qr = m02.divideAndRemainder(base);
        M02[0] = qr[0].floatValue();
        M02[1] = qr[1].floatValue();

        qr = m12.divideAndRemainder(base);
        M12[0] = qr[0].floatValue();
        M12[1] = qr[1].floatValue();

        qr = m0.multiply(m12).divideAndRemainder(base);
        M012[2] = qr[1].floatValue();
        qr = qr[0].divideAndRemainder(base);
        M012[0] = qr[0].floatValue();
        M012[1] = qr[1].floatValue();
    }
}
