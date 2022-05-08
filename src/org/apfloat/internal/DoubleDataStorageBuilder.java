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
import org.apfloat.spi.DataStorage;

/**
 * Default data storage creation strategy for the <code>double</code> data type.
 *
 * @see DoubleMemoryDataStorage
 * @see DoubleDiskDataStorage
 *
 * @version 1.8.2
 * @author Mikko Tommila
 */

public class DoubleDataStorageBuilder
    extends AbstractDataStorageBuilder
{
    /**
     * Default constructor.
     */

    public DoubleDataStorageBuilder()
    {
    }

    @Override
    protected long getMaxCachedSize()
    {
        return (long) 8 * Integer.MAX_VALUE;
    }

    @Override
    protected DataStorage createCachedDataStorage()
        throws ApfloatRuntimeException
    {
        return new DoubleMemoryDataStorage();
    }

    @Override
    protected DataStorage createNonCachedDataStorage()
        throws ApfloatRuntimeException
    {
        return new DoubleDiskDataStorage();
    }

    @Override
    protected boolean isCached(DataStorage dataStorage)
        throws ApfloatRuntimeException
    {
        return (dataStorage instanceof DoubleMemoryDataStorage);
    }
}
