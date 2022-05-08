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

import org.apfloat.spi.AdditionBuilder;
import org.apfloat.spi.BuilderFactory;
import org.apfloat.spi.ApfloatBuilder;
import org.apfloat.spi.DataStorageBuilder;
import org.apfloat.spi.ConvolutionBuilder;
import org.apfloat.spi.NTTBuilder;
import org.apfloat.spi.MatrixBuilder;
import org.apfloat.spi.CarryCRTBuilder;
import org.apfloat.spi.ExecutionBuilder;

/**
 * Factory class for getting instances of the various builder classes needed
 * to build an <code>ApfloatImpl</code> with the <code>long</code> data element type.
 *
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class LongBuilderFactory
    implements BuilderFactory
{
    /**
     * Default constructor.
     */

    public LongBuilderFactory()
    {
    }

    @Override
    public ApfloatBuilder getApfloatBuilder()
    {
        return LongBuilderFactory.apfloatBuilder;
    }

    @Override
    public DataStorageBuilder getDataStorageBuilder()
    {
        return LongBuilderFactory.dataStorageBuilder;
    }

    @Override
    public <T> AdditionBuilder<T> getAdditionBuilder(Class<T> elementType)
        throws IllegalArgumentException
    {
        if (!Long.TYPE.equals(elementType))
        {
           throw new IllegalArgumentException("Unsupported element type: " + elementType);
        }
        @SuppressWarnings("unchecked")
        AdditionBuilder<T> additionBuilder = (AdditionBuilder<T>) LongBuilderFactory.additionBuilder;
        return additionBuilder;
    }

    @Override
    public ConvolutionBuilder getConvolutionBuilder()
    {
        return LongBuilderFactory.convolutionBuilder;
    }

    @Override
    public NTTBuilder getNTTBuilder()
    {
        return LongBuilderFactory.nttBuilder;
    }

    @Override
    public MatrixBuilder getMatrixBuilder()
    {
        return LongBuilderFactory.matrixBuilder;
    }

    @Override
    public <T> CarryCRTBuilder<T> getCarryCRTBuilder(Class<T> elementArrayType)
        throws IllegalArgumentException
    {
        if (!long[].class.equals(elementArrayType))
        {
           throw new IllegalArgumentException("Unsupported element array type: " + elementArrayType);
        }
        @SuppressWarnings("unchecked")
        CarryCRTBuilder<T> carryCRTBuilder = (CarryCRTBuilder<T>) LongBuilderFactory.carryCRTBuilder;
        return carryCRTBuilder;
    }

    @Override
    public ExecutionBuilder getExecutionBuilder()
    {
        return LongBuilderFactory.executionBuilder;
    }

    @Override
    public Class<?> getElementType()
    {
        return Long.TYPE;
    }

    @Override
    public Class<?> getElementArrayType()
    {
        return long[].class;
    }

    @Override
    public int getElementSize()
    {
        return 8;
    }

    @Override
    public void shutdown()
        throws ApfloatRuntimeException
    {
        DiskDataStorage.cleanUp();
    }

    @Override
    public void gc()
        throws ApfloatRuntimeException
    {
        System.gc();
        System.gc();
        System.runFinalization();
        DiskDataStorage.gc();
    }

    private static ApfloatBuilder apfloatBuilder = new LongApfloatBuilder();
    private static DataStorageBuilder dataStorageBuilder = new LongDataStorageBuilder();
    private static AdditionBuilder<Long> additionBuilder = new LongAdditionBuilder();
    private static ConvolutionBuilder convolutionBuilder = new LongConvolutionBuilder();
    private static NTTBuilder nttBuilder = new LongNTTBuilder();
    private static MatrixBuilder matrixBuilder = new LongMatrixBuilder();
    private static CarryCRTBuilder<long[]> carryCRTBuilder = new LongCarryCRTBuilder();
    private static ExecutionBuilder executionBuilder = new ParallelExecutionBuilder();
}
