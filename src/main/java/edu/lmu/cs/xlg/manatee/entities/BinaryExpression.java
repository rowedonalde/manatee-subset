package edu.lmu.cs.xlg.manatee.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * An expression made up of an operator and two operands.
 */
public class BinaryExpression extends Expression {

    private String op;
    private Expression left;
    private Expression right;

    /**
     * Creates a binary expression for the given operator and operands.
     */
    public BinaryExpression(Expression left, String op, Expression right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

    /**
     * Returns the left operand.
     */
    public Expression getLeft() {
        return left;
    }

    /**
     * Returns the operator as a string.
     */
    public String getOp() {
        return op;
    }

    /**
     * Returns the right operand.
     */
    public Expression getRight() {
        return right;
    }

    @Override
    public void analyze(Log log, SymbolTable table, Subroutine owner, boolean inLoop) {
        left.analyze(log, table, owner, inLoop);
        right.analyze(log, table, owner, inLoop);

        // string * int
        if (op.equals("*") && left.getType() == Type.STRING) {
            right.assertInteger("*", log);
            type = Type.STRING;

        // num op num (for arithmetic op)
        } else if (op.matches("[-/]")) {
            left.assertArithmetic(op, log);
            right.assertArithmetic(op, log);
            type = (left.type == Type.NUMBER || right.type == Type.NUMBER)
                ? Type.NUMBER : Type.WHOLE_NUMBER;
                
        //Addition/array pushing
        } else if (op.matches("\\+")) {
            //If both are arrays:
            if (left.isArray() && right.isArray()) {
                ArrayType leftArrayType = (ArrayType)left.type;
                ArrayType rightArrayType = (ArrayType)right.type;
                //If arrays are exactly the same type--same depth and base:
                if (left.type == right.type) {
                    type = left.type;
                    
                } else if (left.type.canBeAssignedTo(right.type)) {
                    type = right.type;
                
                } else if (right.type.canBeAssignedTo(left.type)) {
                    type = left.type;
                    
                //Arrays are one dimension apart, with the left being deeper:
                } else if (leftArrayType.getDepth() - 1 == rightArrayType.getDepth()) {
                    if (leftArrayType.getFundamentalType() == Type.NUMBER
                        && rightArrayType.getFundamentalType().isArithmetic()) {
                        type = left.type;
                    } else if (leftArrayType.getFundamentalType() == Type.WHOLE_NUMBER
                               && rightArrayType.getFundamentalType() == Type.WHOLE_NUMBER) {
                        type = left.type;
                    //} else if (leftArrayType.getBaseType().canBeAssignedTo(right.type)) {
                    //    //type = left.type;
                    //    type = right.type;
                    } else if (right.type.canBeAssignedTo(leftArrayType.getBaseType())) {
                        type = left.type;
                    } else {
                        log.error("bad.array.expression");
                    }
                    
                //Arrays are one dimension apart and right is deeper:
                } else if (rightArrayType.getDepth() - 1 == leftArrayType.getDepth()) {
                    if (rightArrayType.getFundamentalType() == Type.NUMBER
                        && leftArrayType.getFundamentalType().isArithmetic()) {
                        type = right.type;
                    } else if (rightArrayType.getFundamentalType() == Type.WHOLE_NUMBER
                               && leftArrayType.getFundamentalType() == Type.WHOLE_NUMBER) {
                        type = right.type;
                    //} else if (rightArrayType.getBaseType().canBeAssignedTo(left.type)) {
                    //    //type = right.type;
                    //    type = left.type;
                    } else if (left.type.canBeAssignedTo(rightArrayType.getBaseType())) {
                        type = right.type;
                    
                    } else {
                        log.error("bad.array.expression");
                    }
                    
                } else {
                    log.error("bad.array.expression");
                }
            //If left is an array:  
            } else if (left.isArray() && !right.isArray()) {
                ArrayType leftArrayType = (ArrayType)left.type;
                if (leftArrayType.getBaseType() == Type.NUMBER && right.type.isArithmetic()) {
                    type = left.type;
                } else if (leftArrayType.getBaseType() == Type.WHOLE_NUMBER
                           && right.type == Type.WHOLE_NUMBER) {
                    type = leftArrayType;
                } else if (right.type.canBeAssignedTo(leftArrayType.getBaseType())) {
                    //type = right.type;
                    type = left.type;
                } else {
                    log.error("bad.array.expression");
                }
            } else if (!left.isArray() && right.isArray()) {
                ArrayType rightArrayType = (ArrayType)right.type;
                if (rightArrayType.getBaseType() == Type.NUMBER && left.type.isArithmetic()) {
                    type = right.type;
                } else if (rightArrayType.getBaseType() == Type.WHOLE_NUMBER
                           && left.type == Type.WHOLE_NUMBER) {
                    type = right.type;
                } else if (left.type.canBeAssignedTo(rightArrayType.getBaseType())) {
                    //type = left.type;
                    type = right.type;
                } else {
                    log.error("bad.array.expression");
                }
            
            //String concatenation:    
            } else if (left.type == Type.STRING && right.type == Type.STRING) {
                type = Type.STRING;
            } else if (left.type == Type.STRING && right.type == Type.CHARACTER) {
                type = Type.STRING;
            } else if (left.type == Type.CHARACTER && right.type == Type.STRING) {
                type = Type.STRING;
                
            } else {
                left.assertArithmetic(op, log);
                right.assertArithmetic(op, log);
                type = (left.type == Type.NUMBER || right.type == Type.NUMBER)
                    ? Type.NUMBER : Type.WHOLE_NUMBER;
            }
        
        //Multiplication/string multiplying:
        } else if (op.matches("\\*")) {
            if (left.type == Type.STRING && right.type == Type.WHOLE_NUMBER) {
                type = left.type;
            } else {
                left.assertArithmetic(op, log);
                right.assertArithmetic(op, log);
                type = (left.type == Type.NUMBER || right.type == Type.NUMBER)
                    ? Type.NUMBER : Type.WHOLE_NUMBER;
            }
            
        // int op int returning int (for shifts and mod)
        } else if (op.matches("<<|>>|bit or|bit xor|bit and|modulo")) {
            left.assertInteger(op, log);
            right.assertInteger(op, log);
            type = Type.WHOLE_NUMBER;

        // char/num/str op char/num/str (for greater/less inequalities)
        } else if (op.matches("<|<=|>|>=")) {
            if (left.type == Type.CHARACTER) {
                right.assertChar(op, log);
            } else if (left.type == Type.STRING) {
                right.assertString(op, log);
            } else if (left.type.isArithmetic()){
                left.assertArithmetic(op, log);
                right.assertArithmetic(op, log);
            }
            type = Type.TRUTH_VALUE;

        // equals or not equals on primitives
        } else if (op.matches("=|≠")) {
            if (!(left.type.isPrimitive() &&
                    (left.isCompatibleWith(right.type) || right.isCompatibleWith(left.type)))) {
                log.error("eq.type.error", op, left.type, right.type);
            }
            type = Type.TRUTH_VALUE;

        // bool and bool
        // bool or bool
        } else if (op.matches("and|or")) {
            left.assertBoolean(op, log);
            right.assertBoolean(op, log);
            type = Type.TRUTH_VALUE;

        // char in string
        // t in t list
        } else if (op.matches("in")) {
            right.assertArrayOrString("in", log);
            if (right.getType() == Type.STRING) {
                left.assertChar("in", log);
            } else {
                assert(left.getType().canBeAssignedTo(
                    ArrayType.class.cast(right.getType()).getBaseType()));
            }
            type = Type.TRUTH_VALUE;

        // ref is ref
        // ref is not ref
        } else if (op.matches("is") || op.matches("is not")) {
            if (!left.getType().isReference() && !right.getType().isReference()) {
                log.error("non.reference", op);
            }
            if (left.getType() != right.getType()) {
                log.error("type.mismatch", op);
            }
            type = Type.TRUTH_VALUE;

        } else {
            throw new RuntimeException("Internal error in binary expression analysis");
        }
    }
}
