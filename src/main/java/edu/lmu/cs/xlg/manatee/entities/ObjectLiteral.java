package edu.lmu.cs.xlg.manatee.entities;

import edu.lmu.cs.xlg.util.Log;
import java.util.ArrayList;
import java.util.List;

/**
 * An expression that returns an instance of a given object type with
 * specified values for properties
 */
public class ObjectLiteral extends Expression {
    private List<String> propertyNames;
    private List<Expression> propertyValues;
    private String typeName;
    private Type type;
    private SymbolTable propertyTable;
    
    public ObjectLiteral(String typeName, List<String> propertyNames,
                         List<Expression> propertyValues) {
        this.typeName = typeName;
        this.propertyNames = propertyNames;
        this.propertyValues = propertyValues;
    }
    
    public List<String> getPropertyNames() {
        return this.propertyNames;
    }
    
    public List<Expression> getPropertyValues() {
        return this.propertyValues;
    }
    
    public String getTypeName() {
        return this.typeName;
    }
    
    @Override
    public void analyze(Log log, SymbolTable table, Subroutine owner, boolean inLoop) {
        //Get the object type from the table:
        this.type = table.lookupType(this.typeName, log);
        
        //If there is no type by this name, quit:
        if (this.type == Type.ARBITRARY) {
            return;
        }
        
        //Get the internal property table for this type:
        this.propertyTable = this.type.getPropertyTable();
        
        //Make sure all the listed properties are actually in this type:
        for (String i : this.propertyNames) {
            Variable current = (Variable)this.propertyTable.lookup(i, log);
            
            //If this property doesn't exist, quit:
            if (current == Variable.ARBITRARY) {
                return;
            }
            
            //TODO
            //Make sure the current property can take the rightside expression:
            //Type rightType = 
        }
    }
}
