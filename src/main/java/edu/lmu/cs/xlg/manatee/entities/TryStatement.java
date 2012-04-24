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
        this.recoverBlock = recoverBlock;
    }
    
    public Block getTryBlock() {
        return this.tryBlock;
    }
    
    public Block getRecoverBlock() {
        return this.recoverBlock;
    }
    
    @Override
    public void analyze(Log log, SymbolTable table, Subroutine owner, boolean inLoop) {
        this.tryBlock.analyze(log, table, owner, inLoop);
        this.recoverBlock.analyze(log, table, owner, inLoop);
    }
}
