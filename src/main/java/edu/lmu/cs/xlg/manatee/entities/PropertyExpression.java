package edu.lmu.cs.xlg.manatee.entities;

import edu.lmu.cs.xlg.util.Log;

public class PropertyExpression extends Expression {
    private Expression object;
    private String propertyName;
    
    public PropertyExpression(Expression object, String propertyName) {
        this.object = object;
        this.propertyName = propertyName;
    }
    
    public Expression getObject() {
        return this.object;
    }
    
    public String getPropertyName() {
        return this.propertyName;
    }
}
