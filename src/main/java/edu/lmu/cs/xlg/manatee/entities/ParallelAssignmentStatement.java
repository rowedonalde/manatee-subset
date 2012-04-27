package edu.lmu.cs.xlg.manatee.entities;

import edu.lmu.cs.xlg.util.Log;
import java.util.ArrayList;
import java.util.List;

/**
 * A parallel assignment statement.
 */
public class ParallelAssignmentStatement extends Statement {
    
    private List<Expression> targets;
    private List<Expression> sources;
    
    public ParallelAssignmentStatement(List<Expression> targets, List<Expression> sources) {
        this.targets = targets;
        this.sources = sources;
    }
    
    public List<Expression> getTargets() {
        return targets;
    }
    
    public List<Expression> getSources() {
        return sources;
    }
    
    @Override
    public void analyze(Log log, SymbolTable table, Subroutine owner, boolean inLoop) {
        //First, make sure that there are an equal number of
        //Expressions in each of targets and sources:
        if (targets.size() != sources.size()) {
            log.error("unbalanced.parallel.assignment");
        }
        
        //For each Expression in targets, just build a new vanilla
        //AssignmentStatement and let that class do the work:
        for (int i = 0; i < targets.size(); i++) {
            Expression target = targets.get(i);
            Expression source = sources.get(i);
            AssignmentStatement current = new AssignmentStatement(target, source);
            current.analyze(log, table, owner, inLoop);
        }
    }
}
