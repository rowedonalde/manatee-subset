package edu.lmu.cs.xlg.manatee.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * A Manatee error throwing statement object
 */

public class FailStatement extends Statement {
    
    private Expression expression;

    public FailStatement(Expression expression) {
        this.expression = expression;
    }
    
    public Expression getExpression() {
        return expression;
    }
    
    @Override
    public void analyze(Log log, SymbolTable table, Subroutine owner, boolean inLoop) {
        this.expression.analyze(log, table, owner, inLoop);
    }
}
