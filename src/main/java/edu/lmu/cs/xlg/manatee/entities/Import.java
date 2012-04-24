package edu.lmu.cs.xlg.manatee.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * A Manatee imported module object
 */

public class Import extends Statement {
    private String ModuleName;
    
    public Import(String mod) {
        this.ModuleName = mod;
    }
    
    public String getModuleName() {
        return this.ModuleName;
    }
    
    @Override
    public void analyze(Log log, SymbolTable table, Subroutine owner, boolean inLoop) {
        //Make sure the file exists? TODO
    }
}
