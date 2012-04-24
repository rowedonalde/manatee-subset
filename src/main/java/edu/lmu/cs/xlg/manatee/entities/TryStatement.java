package edu.lmu.cs.xlg.manatee.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * A Manatee try statement object for handling errors
 */

public class TryStatement extends Statement {
    
    private Block tryBlock;
    private Block recoverBlock;
    
    public TryStatement(Block tryBlock, Block recoverBlock) {
        this.tryBlock = tryBlock;
        this.recoverBlock = recoverblock;
    }
    
    public Block getTryBlock() {
        return this.tryBlock;
    }
    
    public Block getRecoverBlock() {
        return this.recoverBlock;
    }
    
    
}
