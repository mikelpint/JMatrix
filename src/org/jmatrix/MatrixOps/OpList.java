package org.jmatrix.MatrixOps;

import java.util.ArrayList;

import org.apfloat.Apcomplex;

import org.jmatrix.MatrixTypes.Matrix;

public class OpList {
    static protected abstract class Op {
        abstract void apply (Matrix matrix);
        
        protected static class MultOp extends Op {
            protected int row;
            protected Apcomplex factor;
            
            protected MultOp (int row, Apcomplex factor) {
                super ();
                
                this.setRow (row);
                this.setFactor (factor);
            }
            
            protected MultOp (MultOp op) {
                super ();
                
                this.setRow (op.row);
                this.setFactor (op.factor);
            }
            
            protected int getRow () {
                return this.row;
            }
            
            protected void setRow (int row) {
                if (row < 0 && row > Matrix.MAX_ENTRIES) {
                    return;
                }
                
                this.row = row;
            }
            
            protected Apcomplex getFactor () {
                return this.factor;
            }
            
            protected void setFactor (Apcomplex factor) {
                if (factor == null) {
                    return;
                }
                
                this.factor = factor;
            }
            
            protected void apply (Matrix matrix) {
                GaussAlgorithm.rowByFactor (matrix, this.row, this.factor);
            }
        }
        
        
        protected static class SwapOp extends Op {
            protected int row1;
            protected int row2;
            
            protected SwapOp (int row1, int row2) {
                super ();
                
                this.setRow1 (row1);
                this.setRow2 (row2);
            }
            
            protected SwapOp (SwapOp op) {
                super ();
                
                this.setRow1 (op.row1);
                this.setRow2 (op.row2);
            }
            
            protected int getRow1 () {
                return this.row1;
            }
            
            protected void setRow1 (int row) {
                if (row < 0 || row > Matrix.MAX_ENTRIES) {
                    return;
                }
                
                this.row1 = row;
            }
            
            protected int getRow2 () {
                return this.row2;
            }
            
            protected void setRow2 (int row) {
                if (row < 0 || row > Matrix.MAX_ENTRIES) {
                    return;
                }
                
                this.row2 = row;
            }
            
            protected void apply (Matrix matrix) {
                GaussAlgorithm.swapRows (matrix, this.row1, this.row2);
            }
        }
        
        protected static class SubOp extends Op {
            protected int row1;
            protected int row2;
            protected Apcomplex factor;
            
            protected SubOp (int row1, int row2, Apcomplex factor) {
                super ();
                
                this.setRow1 (row1);
                this.setRow2 (row2);
                this.setFactor (factor);
            }
            
            protected SubOp (SubOp op) {
                super ();
                
                this.setRow1 (op.row1);
                this.setRow2 (op.row2);
                this.setFactor (op.factor);
            }
            
            protected int getRow1 () {
                return this.row1;
            }
            
            protected void setRow1 (int row) {
                if (row < 0 || row > Matrix.MAX_ENTRIES) {
                    return;
                }
                
                this.row1 = row;
            }
            
            protected int getRow2 () {
                return this.row2;
            }
            
            protected void setRow2 (int row) {
                if (row < 0 || row > Matrix.MAX_ENTRIES) {
                    return;
                }
                
                this.row2 = row;
            }
            
            protected Apcomplex getFactor () {
                return this.factor;
            }
            
            protected void setFactor (Apcomplex factor) {
                if (factor == null) {
                    return;
                }
                
                this.factor = factor;
            }
            
            protected void apply (Matrix matrix) {
                GaussAlgorithm.subtractRows (matrix, this.row1, this.row2, this.factor);
            }
        }
    }

    protected ArrayList <Op> ops = new ArrayList <Op> ();
    
    public OpList () {
        super ();
    }
    
    public OpList (OpList opList) {
        super ();
        
        this.setOps (opList.ops);
    }
    
    public ArrayList <Op> getOps () {
        return this.ops;
    }
    
    public void setOps (ArrayList <Op> ops) {
        if (ops == null) {
            return;
        }
        
        this.ops = ops;
    }
    
    public void appendMult (int row, Apcomplex factor) {
        this.ops.add (new Op.MultOp (row, factor));
    }
    
    public void appendSwap (int row1, int row2) {
        this.ops.add (new Op.SwapOp (row1, row2));
    }
    
    public void appendSub (int row1, int row2, Apcomplex factor) {
        this.ops.add (new Op.SubOp (row1, row2, factor));
    }
    
    public Matrix apply (Matrix matrix) {
        if (this.ops == null) {
            return matrix;
        }
        
        Matrix newMatrix = new Matrix (matrix);
        
        for (Op op : this.ops) {
            op.apply (newMatrix);
        }
        
        return newMatrix;
    }
}
