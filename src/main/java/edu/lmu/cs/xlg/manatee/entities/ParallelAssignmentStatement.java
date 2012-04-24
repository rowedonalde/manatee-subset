package edu.lmu.cs.xlg.manatee.entities;

import edu.lmu.cs.xlg.util.Log;
import java.util.ArrayList;
import java.util.List;

/**
 * A parallel assignment statement.
 */
public class ParallelAssignmentStatement extends Statement {
    
    private List<Expression> targets;
    private Expression source;
    
    public ParallelAssignmentStatement(List<Expression> targets, Expression source) {
        this.targets = targets;
        this.source = source;
    }
    
    public List<Expression> getTargets() {
        return targets;
    }
    
    public Expression getSource() {
        return source;
    }
    
    @Override
    public void analyze(Log log, SymbolTable table, Subroutine owner, boolean inLoop) {
        //For each Expression in targets, just build a new vanilla
        //AssignmentStatement and let that class do the work:
        for (int i = 0; i < targets.size(); i++) {
            Expression target = targets.get(i);
            AssignmentStatement current = new AssignmentStatement(target, source);
            current.analyze(log, table, owner, inLoop);
        }
    }
}
