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
    
    public AssignmentStatement(List<Expression> targets, Expression source) {
        this.targets = targets;
        this.source = source;
    }
    
    public List<Expression> getTargets() {
        return targets;
    }
    
    public Expression getSource() {
        return source;
    }
}
