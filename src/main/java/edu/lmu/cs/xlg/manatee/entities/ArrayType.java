package edu.lmu.cs.xlg.manatee.entities;

import java.util.ArrayList;

/**
 * A type which is an array. Array types objects are never constructed during syntax analysis.
 * Instead, each construct that references an array type stores only a typename of the form
 * "t list" or "t list list", for example. It is only during semantic analysis that array type
 * objects are created.
 */
public class ArrayType extends Type {

    private Type baseType;
    //private int depth = 0;
    private Integer depth;
    private Type fundamentalType;

    /**
     * Constructs an array type for arrays with the given base type.
     */
    public ArrayType(Type baseType) {
        super(baseType.getName() + " list");
        this.baseType = baseType;
    }

    /**
     * Returns the type of the elements of this array type.
     */
    public Type getBaseType() {
        return baseType;
    }
    
    /**
     * Returns how many dimensions deep this array is.
     * For example, a number list is 1, a number list list is 2, etc.
     */
    public int getDepth() {
        if (this.depth == null) {
            this.drill();
        }
        
        return this.depth.intValue();
    }
    
    /**
     * Returns the "ultimate" base type.
     * If this is a number list, return number; if this is a number list list,
     * return number; etc.
     */
    public Type getFundamentalType() {
        if (this.fundamentalType == null) {
            this.drill();
        }
        
        return this.fundamentalType;
    }
    
    //Goes through the dimensions of this ArrayType and establishes the depth
    //and fundamentalType
    private void drill() {
        boolean hasMoreLevels = true;
        int depth = 0;
        ArrayType currentLevel = this;
        Type nextLevel;
        
        while (hasMoreLevels) {
            depth++;
            nextLevel = currentLevel.getBaseType();
            if (nextLevel instanceof ArrayType) {
                currentLevel = (ArrayType)nextLevel;
            } else {
                hasMoreLevels = false;
                this.fundamentalType = nextLevel;
            }
        }
        
        this.depth = new Integer(depth);
    }

}
