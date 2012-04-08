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
}
