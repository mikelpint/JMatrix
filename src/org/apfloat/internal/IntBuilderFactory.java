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
 * to build an <code>ApfloatImpl</code> with the <code>int</code> data element type.
 *
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class IntBuilderFactory
    implements BuilderFactory
{
    /**
     * Default constructor.
     */

    public IntBuilderFactory()
    {
    }

    @Override
    public ApfloatBuilder getApfloatBuilder()
    {
        return IntBuilderFactory.apfloatBuilder;
    }

    @Override
    public DataStorageBuilder getDataStorageBuilder()
    {
        return IntBuilderFactory.dataStorageBuilder;
    }

    @Override
    public <T> AdditionBuilder<T> getAdditionBuilder(Class<T> elementType)
        throws IllegalArgumentException
    {
        if (!Integer.TYPE.equals(elementType))
        {
           throw new IllegalArgumentException("Unsupported element type: " + elementType);
        }
        @SuppressWarnings("unchecked")
        AdditionBuilder<T> additionBuilder = (AdditionBuilder<T>) IntBuilderFactory.additionBuilder;
        return additionBuilder;
    }

    @Override
    public ConvolutionBuilder getConvolutionBuilder()
    {
        return IntBuilderFactory.convolutionBuilder;
    }

    @Override
    public NTTBuilder getNTTBuilder()
    {
        return IntBuilderFactory.nttBuilder;
    }

    @Override
    public MatrixBuilder getMatrixBuilder()
    {
        return IntBuilderFactory.matrixBuilder;
    }

    @Override
    public <T> CarryCRTBuilder<T> getCarryCRTBuilder(Class<T> elementArrayType)
        throws IllegalArgumentException
    {
        if (!int[].class.equals(elementArrayType))
        {
           throw new IllegalArgumentException("Unsupported element array type: " + elementArrayType);
        }
        @SuppressWarnings("unchecked")
        CarryCRTBuilder<T> carryCRTBuilder = (CarryCRTBuilder<T>) IntBuilderFactory.carryCRTBuilder;
        return carryCRTBuilder;
    }

    @Override
    public ExecutionBuilder getExecutionBuilder()
    {
        return IntBuilderFactory.executionBuilder;
    }

    @Override
    public Class<?> getElementType()
    {
        return Integer.TYPE;
    }

    @Override
    public Class<?> getElementArrayType()
    {
        return int[].class;
    }

    @Override
    public int getElementSize()
    {
        return 4;
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

    private static ApfloatBuilder apfloatBuilder = new IntApfloatBuilder();
    private static DataStorageBuilder dataStorageBuilder = new IntDataStorageBuilder();
    private static AdditionBuilder<Integer> additionBuilder = new IntAdditionBuilder();
    private static ConvolutionBuilder convolutionBuilder = new IntConvolutionBuilder();
    private static NTTBuilder nttBuilder = new IntNTTBuilder();
    private static MatrixBuilder matrixBuilder = new IntMatrixBuilder();
    private static CarryCRTBuilder<int[]> carryCRTBuilder = new IntCarryCRTBuilder();
    private static ExecutionBuilder executionBuilder = new ParallelExecutionBuilder();
}
