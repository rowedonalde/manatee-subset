package edu.lmu.cs.xlg.manatee.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * A Manatee UntilLoop object
 */

public class UntilLoop extends Statement {
    private Expression condition;
    private Block body;
    
    public UntilLoop(Expression condition, Block body) {
        this.condition = condition;
        this.body = body;
    }
    
    public Expression getCondition() {
        return condition;
    }

    public Block getBody() {
        return body;
    }
    
    @Override
    public void analyze(Log log, SymbolTable table, Subroutine owner, boolean inLoop) {
        condition.analyze(log, table, owner, inLoop);
        condition.assertBoolean("until loop", log);
        body.analyze(log, table, owner, true);
    }
}
