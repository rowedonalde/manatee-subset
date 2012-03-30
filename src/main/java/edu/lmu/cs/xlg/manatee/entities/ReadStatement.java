package edu.lmu.cs.xlg.manatee.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * A statement that reads into a single variable.
 */
public class ReadStatement extends Statement {

    private Expression expression;

    public ReadStatement(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public void analyze(Log log, SymbolTable table, Subroutine owner, boolean inLoop) {
        expression.analyze(log, table, owner, inLoop);
        if (!expression.isWritableLValue()) {
            log.error("non.writable.in.read.statement");
        }
    }
}
