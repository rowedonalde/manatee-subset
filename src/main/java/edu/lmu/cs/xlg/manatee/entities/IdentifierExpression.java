package edu.lmu.cs.xlg.manatee.entities;

import edu.lmu.cs.xlg.util.Log;
import java.util.ArrayList;
import java.util.List;

/**
 * An expression consisting of a single identifier, a referencing occurrence to a variable or a
 * function.
 */
public class IdentifierExpression extends Expression {

    private String name;
    private Entity referent;
    private List<String> propertyNames;
    private List<Expression> propertyValues;
    
    /**
     * Constructor for simple ID:
     */
    public IdentifierExpression(String name) {
        this.name = name;
    }
    
    /**
     * Constructor for object literal:
     */
    public IdentifierExpression(String name, List<String> propertyNames,
                                List<Expression> propertyValues) {
        this.name = name;
        this.propertyNames = propertyNames;
        this.propertyValues = propertyValues;
    }

    public String getName() {
        return name;
    }
    
    public List<String> getPropertyNames() {
        return this.propertyNames;
    }
    
    public List<Expression> getPropertyValues() {
        return this.propertyValues;
    }

    public Entity getReferent() {
        return referent;
    }

    /**
     * Returns whether the simple variable expression is writable, which it will be if and only
     * if its referent is a variable and not marked constant.
     */
    @Override
    public boolean isWritableLValue() {
       return referent instanceof Variable && !Variable.class.cast(referent).isConstant();
    }

    @Override
    public void analyze(Log log, SymbolTable table, Subroutine owner, boolean inLoop) {
        referent = table.lookup(name, log);
        if (referent instanceof Variable) {
            type = Variable.class.cast(referent).getType();
        } else if (referent instanceof Function) {
            type = Type.FUNCTION;
        } else {
            log.error("bad.expression");
        }
    }
}
