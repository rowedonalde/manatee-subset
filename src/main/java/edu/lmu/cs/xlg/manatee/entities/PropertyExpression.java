package edu.lmu.cs.xlg.manatee.entities;

import edu.lmu.cs.xlg.util.Log;

public class PropertyExpression extends Expression {
    private Expression object;
    private Type objectType;
    private String propertyName;
    private Variable property;
    private SymbolTable propertyTable;
    //inherits type field from Expression
    
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
    
    public Variable getProperty() {
        return this.property;
    }
    
    public Type getObjectType() {
        return this.objectType;
    }
    
    @Override
    boolean isWritableLValue() {
        return this.object.isWritableLValue();
    }
    
    @Override
    public void analyze(Log log, SymbolTable table, Subroutine owner, boolean inLoop) {
        //Analyze the parent object:
        this.object.analyze(log, table, owner, inLoop);
        
        //If the parent object is not actually an object type,
        //throw an error and quit:
        this.objectType = this.object.getType();
        if (objectType == null || !objectType.isObjectType()) {
            log.error("not.object.type");
            return;
        }
        
        //Analyze the property:
        this.propertyTable = this.objectType.getPropertyTable();
        this.property = (Variable)this.propertyTable.lookup(this.propertyName, log);
        this.property.analyze(log, this.propertyTable, owner, inLoop);
        
        //Get the property's type:
        this.type = this.property.getType();
    }
}
