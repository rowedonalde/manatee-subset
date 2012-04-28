package edu.lmu.cs.xlg.manatee.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * An increment statement. The converse "decrement" statement is also
 * covered in this class.
 */
public class IncrementStatement extends Statement {
    //This switch is true when this IncrementStatement is actually a decrement:
    private boolean isDecrementStatement = false;
    
    //This Expression is the value that gets incremented/decremented:
    private Expression target;
    
    //The Expression representing the value by which target should be
    //incremented/decremented:
    private Expression increment;
    
    /**
     * Constructor that sets the target, increment expression, and
     * the flag for whether it's a decrement statement.
     */
    public IncrementStatement(Expression target, Expression increment,
                              boolean isDecrementStatement) {
        this.target = target;
        this.increment = increment;
        this.isDecrementStatement = isDecrementStatement;
    }
    
    /**
     * Constructor that sets the target and increment expression.
     * Automatically remains as an increment statement.
     */
    public IncrementStatement(Expression target, Expression increment) {
        this.target = target;
        this.increment = increment;
    }
    
    /**
     * Returns the target expression of this increment statement
     */
    public Expression getTarget() {
        return this.target;
    }
    
    /**
     * Returns the expression by which the target should be incremented
     */
    public Expression getIncrement() {
        return this.increment;
    }
    
    /**
     * Returns true if this is actually a decrement statement.
     * Returns false otherwise.
     */
    public boolean isDecrementStatement() {
        return this.isDecrementStatement;
    }
    
    @Override
    public void analyze(Log log, SymbolTable table, Subroutine owner, boolean inLoop) {
        this.target.analyze(log, table, owner, inLoop);
        this.increment.analyze(log, table, owner, inLoop);
        //Check to make sure that args are arithmetic and that
        //target is assignable:
        this.target.assertArithmetic("increment", log);
        this.increment.assertArithmetic("increment", log);
        
        if (!this.target.isWritableLValue()) {
            log.error("non.writable.in.assignment.statement");
        }
    }
}
