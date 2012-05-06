package edu.lmu.cs.xlg.manatee.generators;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import edu.lmu.cs.xlg.manatee.entities.ArrayConstructor;
import edu.lmu.cs.xlg.manatee.entities.ArrayType;
import edu.lmu.cs.xlg.manatee.entities.AssignmentStatement;
import edu.lmu.cs.xlg.manatee.entities.BinaryExpression;
import edu.lmu.cs.xlg.manatee.entities.Block;
import edu.lmu.cs.xlg.manatee.entities.BooleanLiteral;
import edu.lmu.cs.xlg.manatee.entities.CallStatement;
import edu.lmu.cs.xlg.manatee.entities.CharacterLiteral;
import edu.lmu.cs.xlg.manatee.entities.CollectionLoop;
import edu.lmu.cs.xlg.manatee.entities.ConditionalStatement;
import edu.lmu.cs.xlg.manatee.entities.Declaration;
import edu.lmu.cs.xlg.manatee.entities.DoNothingStatement;
import edu.lmu.cs.xlg.manatee.entities.ExitStatement;
import edu.lmu.cs.xlg.manatee.entities.Expression;
import edu.lmu.cs.xlg.manatee.entities.FailStatement;
import edu.lmu.cs.xlg.manatee.entities.Function;
import edu.lmu.cs.xlg.manatee.entities.FunctionCall;
import edu.lmu.cs.xlg.manatee.entities.IdentifierExpression;
import edu.lmu.cs.xlg.manatee.entities.IncrementStatement;
import edu.lmu.cs.xlg.manatee.entities.Literal;
import edu.lmu.cs.xlg.manatee.entities.ModifiedStatement;
import edu.lmu.cs.xlg.manatee.entities.NullLiteral;
import edu.lmu.cs.xlg.manatee.entities.NumberLiteral;
import edu.lmu.cs.xlg.manatee.entities.ObjectLiteral;
import edu.lmu.cs.xlg.manatee.entities.ParallelAssignmentStatement;
import edu.lmu.cs.xlg.manatee.entities.PlainLoop;
import edu.lmu.cs.xlg.manatee.entities.Procedure;
import edu.lmu.cs.xlg.manatee.entities.PropertyExpression;
import edu.lmu.cs.xlg.manatee.entities.RangeLoop;
import edu.lmu.cs.xlg.manatee.entities.ReadStatement;
import edu.lmu.cs.xlg.manatee.entities.ReturnStatement;
import edu.lmu.cs.xlg.manatee.entities.Script;
import edu.lmu.cs.xlg.manatee.entities.Statement;
import edu.lmu.cs.xlg.manatee.entities.StringLiteral;
import edu.lmu.cs.xlg.manatee.entities.Subroutine;
import edu.lmu.cs.xlg.manatee.entities.SubscriptExpression;
import edu.lmu.cs.xlg.manatee.entities.TimesLoop;
import edu.lmu.cs.xlg.manatee.entities.TryStatement;
import edu.lmu.cs.xlg.manatee.entities.Type;
import edu.lmu.cs.xlg.manatee.entities.UnaryExpression;
import edu.lmu.cs.xlg.manatee.entities.UntilLoop;
import edu.lmu.cs.xlg.manatee.entities.Variable;
import edu.lmu.cs.xlg.manatee.entities.WhileLoop;
import edu.lmu.cs.xlg.manatee.entities.WholeNumberLiteral;
import edu.lmu.cs.xlg.manatee.entities.WriteStatement;
import edu.lmu.cs.xlg.manatee.entities.ConditionalStatement.Arm;

/**
 * A generator that translates an Manatee program into JavaScript.
 */
public class ManateeToJavaScriptGenerator extends Generator {

    @Override
    public void generate(Script script, PrintWriter writer) {
        this.writer = writer;

        emit("(function () {");
        generateBlock(script);
        emit("}());");
    }

    /**
     * Emits JavaScript code for the given Manatee block.
     */
    private void generateBlock(Block block) {
        indentLevel++;
        for (Statement s: block.getStatements()) {
            generateStatement(s);
        }
        indentLevel--;
    }

    /**
     * Emits JavaScript code for the given Manatee statement.
     */
    private void generateStatement(Statement s) {
        if (s instanceof Type && Type.class.cast(s).isObjectType()) {
            Type sType = (Type)s;
            emit("var " + id(sType) + " = " + "function(options) {");
            indentLevel++;
            for (Variable p: sType.getProperties()) {
                //Just using the original names for properties since
                //they're limited to their own little namespace:
                emit("this." + p.getName() + " = options." + p.getName() + ";");
            }
            indentLevel--;
            emit("};");
    
        } else if (s instanceof Declaration) {
            generateDeclaration(Declaration.class.cast(s));
            
        //} else if (s instanceof Function) {
        //    generateDeclaration(Declaration.class.cast(s));

        } else if (s instanceof DoNothingStatement) {
            emit(";");

        } else if (s instanceof AssignmentStatement) {
            AssignmentStatement a = AssignmentStatement.class.cast(s);
            String target = generateExpression(a.getTarget());
            String source = generateExpression(a.getSource());
            emit(String.format("%s = %s;", target, source));

        } else if (s instanceof ReadStatement) {
            // TODO
            emit("// READ STATEMENTS NOT YET HANDLED");
            
        } else if (s instanceof FailStatement) {
            //TODO
            emit("// FAIL STATEMENTS NOT YET HANDLED");

        } else if (s instanceof WriteStatement) {
            Expression e = WriteStatement.class.cast(s).getExpression();
            emit("console.log(" + generateExpression(e) + ");");

        } else if (s instanceof ExitStatement) {
            emit("break;");

        } else if (s instanceof ReturnStatement) {
            Expression e = ReturnStatement.class.cast(s).getExpression();
            emit("return" + (e == null ? "" : " " + generateExpression(e)) + ";");

        } else if (s instanceof CallStatement) {
            CallStatement call = CallStatement.class.cast(s);
            String procedure = id(call.getProcedure());
            List<String> arguments = new ArrayList<String>();
            Expression waitTime = call.getWaitTime();
            for (Expression argument: call.getArgs()) {
                arguments.add(generateExpression(argument));
            }
            if (waitTime != null) {
                emit("setTimeout(function(){"
                     + String.format("%s(%s)", procedure, StringUtils.join(arguments, ", "))
                     + "}, 1000 * " + generateExpression(waitTime) + ");");
            } else {
                emit(String.format("%s(%s)", procedure, StringUtils.join(arguments, ", "))
                     + ";");
            }

        } else if (s instanceof ModifiedStatement) {
            ModifiedStatement m = ModifiedStatement.class.cast(s);
            String key;
            switch (m.getModifier().getType()) {
            //case IF: key = "if"; break;
            //case WHILE: key = "while"; break;
            case IF: key = "if ("; break;
            case WHILE: key = "while ("; break;
            case UNLESS: key = "if (!"; break;
            case UNTIL: key = "while (!"; break;
            default: throw new RuntimeException("Internal error: unknown modifier");
            }
            String condition = generateExpression(m.getModifier().getCondition());
            emit(String.format("%s %s) {", key, condition));
            indentLevel++;
            generateStatement(m.getStatement());
            indentLevel--;
            emit("}");

        } else if (s instanceof ConditionalStatement) {
            generateConditionalStatement(ConditionalStatement.class.cast(s));

        } else if (s instanceof PlainLoop) {
            emit("while (true) {");
            generateBlock(PlainLoop.class.cast(s).getBody());
            emit("}");

        } else if (s instanceof TimesLoop) {
            TimesLoop loop = TimesLoop.class.cast(s);
            Variable counter = new Variable("", Type.WHOLE_NUMBER);
            String count = generateExpression(loop.getCount());
            emit(String.format("for (var %s = %s; %s > 0; %s--) {",
                    id(counter), count, id(counter), id(counter)));
            generateBlock(loop.getBody());
            emit("}");

        } else if (s instanceof CollectionLoop) {
            CollectionLoop loop = CollectionLoop.class.cast(s);
            String index = id(loop.getIterator());
            String collection = generateExpression(loop.getCollection());
            emit(String.format("%s%s.forEach(function (%s) {",
                    collection,
                    loop.getCollection().getType() == Type.STRING ? ".split('')" : "",
                    index));
            generateBlock(loop.getBody());
            emit("});");

        } else if (s instanceof RangeLoop) {
            RangeLoop loop = RangeLoop.class.cast(s);
            String index = id(loop.getIterator());
            String low = generateExpression(loop.getLow());
            String high = generateExpression(loop.getHigh());
            String step = loop.getStep() == null ? "1" : generateExpression(loop.getStep());
            emit(String.format("for (var %s = %s; %s <= %s; %s += %s) {",
                    index, low, index, high, index, step));
            generateBlock(loop.getBody());
            emit("}");

        } else if (s instanceof WhileLoop) {
            WhileLoop loop = WhileLoop.class.cast(s);
            emit("while (" + generateExpression(loop.getCondition()) + ") {");
            generateBlock(loop.getBody());
            emit("}");
        }
    }

    /**
     * Generates JavaScript code for conditional statement s.
     */
    private void generateConditionalStatement(ConditionalStatement s) {

        boolean firstArm = true;
        for (Arm arm: s.getArms()) {
            String lead = firstArm ? "if" : "} else if";
            emit(lead + " (" + generateExpression(arm.getCondition()) + ") {");
            generateBlock(arm.getBlock());
            firstArm = false;
        }
        if (s.getElsePart() != null) {
            emit("} else {");
            generateBlock(s.getElsePart());
        }
        emit("}");
    }

    /**
     * Returns a JavaScript expression for the given Manatee expression.
     */
    private String generateExpression(Expression e) {
        if (e instanceof Literal) {
            return generateLiteral(Literal.class.cast(e));
        
        } else if (e instanceof ObjectLiteral) {
            //return generateObjectLiteral(ObjectLiteral.class.cast(e));
            ObjectLiteral eObjectLiteral = (ObjectLiteral)e;
            String output = "";
            Type type = eObjectLiteral.getType();
            List<String> propertyNames = eObjectLiteral.getPropertyNames();
            List<Expression> propertyValues = eObjectLiteral.getPropertyValues();
            output += "new " + id(type) + "({";
            //Cycle through the properties and generate options:
            for (int i = 0; i < propertyNames.size(); i++) {
                if (i > 0) {
                    output += ", ";
                }
                output += propertyNames.get(i) + ": ";
                output += generateExpression(propertyValues.get(i));
            }
            output += "})";
            return output;

        } else if (e instanceof IdentifierExpression) {
            return id(IdentifierExpression.class.cast(e).getReferent());

        } else if (e instanceof UnaryExpression) {
            return generateUnaryExpression(UnaryExpression.class.cast(e));

        } else if (e instanceof BinaryExpression) {
            return generateBinaryExpression(BinaryExpression.class.cast(e));

        } else if (e instanceof ArrayConstructor) {
            List<String> values = new ArrayList<String>();
            for (Expression element: ArrayConstructor.class.cast(e).getExpressions()) {
                values.add(generateExpression(element));
            }
            return String.format("[%s]", StringUtils.join(values, ", "));

        } else if (e instanceof SubscriptExpression) {
            SubscriptExpression s = SubscriptExpression.class.cast(e);
            String base = generateExpression(s.getBase());
            String index = generateExpression(s.getIndex());
            return String.format("%s[%s]", base, index);

        } else if (e instanceof FunctionCall) {
            FunctionCall call = FunctionCall.class.cast(e);
            String f = generateExpression(call.getFunction());
            List<String> arguments = new ArrayList<String>();
            for (Expression a: call.getArgs()) {
                arguments.add(generateExpression(a));
            }
            return String.format("%s(%s)", f, StringUtils.join(arguments, ", "));

        } else {
            throw new RuntimeException("Internal Operator: statement");
        }
    }

    /**
     * Emits JavaScript code for the given Manatee declaration.
     */
    private void generateDeclaration(Declaration d) {

        if (d instanceof Variable) {
            Variable v = Variable.class.cast(d);
            if (v.getInitializer() == null) {
                emit("var " + id(d) + ";");
            } else {
                emit("var " + id(d) + " = " + generateExpression(v.getInitializer()) + ";");
            }
        /*
        } else if (d instanceof Function) {
            Function f = Function.class.cast(d);
            List<String> parameters = new ArrayList<String>();
            for (Variable v: f.getParameters()) {
                parameters.add(id(v));
            }
            emit(String.format("function %s(%s) {", id(f), StringUtils.join(parameters, ", ")));
            generateBlock(f.getBody());
            emit("}");
        */
        } else {
            Subroutine s = Subroutine.class.cast(d);
            List<String> parameters = new ArrayList<String>();
            for (Variable v: s.getParameters()) {
                parameters.add(id(v));
            }
            emit(String.format("function %s(%s) {", id(s), StringUtils.join(parameters, ", ")));
            generateBlock(s.getBody());
            emit("}");
        }
    }

    private String generateLiteral(Literal e) {

        if (BooleanLiteral.FALSE.equals(e)) {
            return "false";

        } else if (BooleanLiteral.TRUE.equals(e)) {
            return "true";

        } else if (e instanceof CharacterLiteral) {
            // Note: Unicode escapes are not part of the subset language.
            return e.getLexeme();

        } else if (e instanceof StringLiteral) {
            // Note: Unicode escapes are not part of the subset language.
            return e.getLexeme();

        } else if (e instanceof NumberLiteral) {
            return NumberLiteral.class.cast(e).getValue() + "";

        } else if (e instanceof WholeNumberLiteral) {
            return WholeNumberLiteral.class.cast(e).getValue() + "";

        } else if (e instanceof NullLiteral) {
            return "null";

        } else {
            throw new RuntimeException("Internal Error: unknown literal type");
        }
    }

    /**
     * Returns JavaScript source for the given Manatee unary expression.
     */
    private String generateUnaryExpression(UnaryExpression e) {

        String operand = generateExpression(e.getOperand());
        if ("-".equals(e.getOp())) {
            return "(-(" + operand + "))";
        } else if ("not".equals(e.getOp())) {
            return "(!(" + operand + "))";
        } else if ("length".equals(e.getOp())) {
            return "((" + operand + ").length)";
        } else {
            throw new RuntimeException("Internal Error: unknown unary operator");
        }
    }

    /**
     * Returns JavaScript source for the given Manatee unary expression.
     */
    private String generateBinaryExpression(BinaryExpression e) {

        String op = e.getOp();
        String left = generateExpression(e.getLeft());
        String right = generateExpression(e.getRight());

        if (op.equals("+")) {
            if (e.getLeft().isArrayOrString() || e.getRight().isArray()) {
                if (e.getLeft().isArray()) {
                    if (e.getRight().isArray()) {
                        return left + ".concat(" + right + ")" ;
                    } else {
                        return left + ".push(" + right + ")" ;
                    }
                } else if (e.getRight().isArray()) {
                    return right + ".unshift(" + left + ")" ;
                } else {
                    return left + ".concat(" + right + ")" ;
                }
            }
        } else if (op.equals("*")) {
            if (e.getLeft().getType() == Type.STRING) {
                Variable counter = new Variable("", Type.WHOLE_NUMBER);
                Variable result = new Variable("", Type.STRING);
                String value = generateExpression(e.getLeft());
                String count = generateExpression(e.getRight());
                emit(String.format("for (var %s = \"\", %s = %s; %s > 0; %s--) {",
                        id(result), id(counter), count, id(counter), id(counter)));
                indentLevel++;
                emit(String.format("%s = %s.concat(%s);", id(result), id(result), value));
                indentLevel--;
                emit("}");
                return id(result);
            }
        } else if (op.equals("and")) {
            op = "&&";
        } else if (op.equals("or")) {
            op = "||";
        } else if (op.equals("=") || op.equals("is")) {
            op = "===";
        } else if (op.equals("≠") || op.equals("is not")) {
            op = "!==";
        } else if (op.equals("in")) {
            return String.format("(%s.indexOf(%s) >= 0)", right, left);
        } else if (op.equals("bit or")) {
            op = "|";
        } else if (op.equals("bit and")) {
            op = "&";
        } else if (op.equals("bit xor")) {
            op = "^";
        } else if (op.matches("-|/|<<|>>|<|<=|>|>=")) {
            // Nothing here, just checking the operator is valid
        } else {
            throw new RuntimeException("Internal Error: unknown binary operator");
        }
        return String.format("(%s %s %s)", left, op, right);
    }
}
