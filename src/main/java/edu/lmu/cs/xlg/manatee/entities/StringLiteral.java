package edu.lmu.cs.xlg.manatee.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * A string literal.
 */
public class StringLiteral extends Literal {

    public StringLiteral(String lexeme) {
        super(lexeme);
    }

    @Override
    public void analyze(Log log, SymbolTable table, Subroutine owner, boolean inLoop) {
        this.type = Type.STRING;
    }
}
