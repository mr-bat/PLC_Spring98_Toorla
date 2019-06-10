package toorla.jasmin;

import toorla.ast.Program;
import toorla.ast.declaration.classDecs.ClassDeclaration;
import toorla.ast.declaration.classDecs.EntryClassDeclaration;
import toorla.ast.declaration.classDecs.classMembersDecs.ClassMemberDeclaration;
import toorla.ast.declaration.classDecs.classMembersDecs.FieldDeclaration;
import toorla.ast.declaration.classDecs.classMembersDecs.MethodDeclaration;
import toorla.ast.declaration.localVarDecs.ParameterDeclaration;
import toorla.ast.expression.*;
import toorla.ast.expression.binaryExpression.*;
import toorla.ast.expression.unaryExpression.Neg;
import toorla.ast.expression.unaryExpression.Not;
import toorla.ast.expression.value.BoolValue;
import toorla.ast.expression.value.IntValue;
import toorla.ast.expression.value.StringValue;
import toorla.ast.statement.*;
import toorla.ast.statement.localVarStats.LocalVarDef;
import toorla.ast.statement.localVarStats.LocalVarsDefinitions;
import toorla.ast.statement.returnStatement.Return;
import toorla.jasmin.utils.JGenrator;
import toorla.symbolTable.SymbolTable;
import toorla.typeChecker.ExpressionTypeExtractor;
import toorla.types.arrayType.ArrayType;
import toorla.types.singleType.UserDefinedType;
import toorla.utilities.graph.Graph;
import toorla.visitor.Visitor;

import static java.text.MessageFormat.format;

public class JasminCompiler extends Visitor<String> {
    private static final int STACK_SIZE = 64;
    private static final int LOCALS_SIZE = 64;
    private ExpressionTypeExtractor expressionTypeExtractor;
    private ClassDeclaration currentClass = null;
    private MethodDeclaration currentMethod = null;
    private int whileCounter = 0, ifCounter = 0;
    private Graph<String> classHierarchy;

    private String generateBeginLabel(int scopeCounter) {
        return currentMethod.getName().getName() + "_" + currentClass.getName().getName() + "_" + scopeCounter + "_begin";
    }
    private String generateElseLabel(int scopeCounter) {
        return currentMethod.getName().getName() + "_" + currentClass.getName().getName() + "_" + scopeCounter + "_else";
    }
    private String generateEndLabel(int scopeCounter) {
        return currentMethod.getName().getName() + "_" + currentClass.getName().getName() + "_" + scopeCounter + "_end";
    }

    public JasminCompiler(Graph<String> classHierarchy) {
        this.expressionTypeExtractor = new ExpressionTypeExtractor(classHierarchy);
        this.classHierarchy = classHierarchy;
    }

    @Override
    public String visit(Plus plusExpr) {
        return plusExpr.getLhs().accept(this) + plusExpr.getRhs().accept(this) + "iadd\n";
    }

    @Override
    public String visit(Minus minusExpr) {
        return minusExpr.getLhs().accept(this) + minusExpr.getRhs().accept(this) + "isub\n";
    }

    @Override
    public String visit(Times timesExpr) {
        return timesExpr.getLhs().accept(this) + timesExpr.getRhs().accept(this) + "imul\n";
    }

    @Override
    public String visit(Division divExpr) {
        return divExpr.getLhs().accept(this) + divExpr.getRhs().accept(this) + "idiv\n";
    }

    @Override
    public String visit(Modulo moduloExpr) {
        return moduloExpr.getLhs().accept(this) + moduloExpr.getRhs().accept(this) + "irem\n";
    }

    @Override
    public String visit(Equals equalsExpr) {
        return "";
    }

    @Override
    public String visit(GreaterThan gtExpr) {
        return "";
    }

    @Override
    public String visit(LessThan lessThanExpr) {
        return "";
    }

    @Override
    public String visit(And andExpr) {
        return andExpr.getLhs().accept(this) + andExpr.getRhs().accept(this) + "iand\n";
    }

    @Override
    public String visit(Or orExpr) {
        return orExpr.getLhs().accept(this) + orExpr.getRhs().accept(this) + "ior\n";

    }

    @Override
    public String visit(Neg negExpr) {
        return "";
    }

    @Override
    public String visit(Not notExpr) {
        return "";
    }

    @Override
    public String visit(MethodCall methodCall) {
        return "";
    }

    @Override
    public String visit(Identifier identifier) {
        return JGenrator.loadLVal(SymbolTable.top().get("var_" + identifier.getName()), identifier);
    }

    @Override
    public String visit(Self self) {
        return "";
    }

    @Override
    public String visit(IntValue intValue) {
        return "ldc " + intValue.getConstant();
    }

    @Override
    public String visit(NewArray newArray) {
        return "";
    }

    @Override
    public String visit(BoolValue booleanValue) {
        return "ldc " + (booleanValue.isConstant() ? "1" : "0");
    }

    @Override
    public String visit(StringValue stringValue) {
        return "ldc " + stringValue.getConstant();
    }

    @Override
    public String visit(NewClassInstance newClassInstance) {
        return "";
    }

    @Override
    public String visit(FieldCall fieldCall) {
        return JGenrator.loadLVal(SymbolTable.top().get("var_" + fieldCall.getField().getName()), fieldCall);
    }

    @Override
    public String visit(ArrayCall arrayCall) {
        return "";
    }

    @Override
    public String visit(NotEquals notEquals) {
        return "";
    }

    @Override
    public String visit(PrintLine printStat) {
        return "";
    }

    @Override
    public String visit(Assign assignStat) {
        String result = assignStat.getRvalue().accept(this) + '\n';
        result += JGenrator.changeLoadToStore(assignStat.getLvalue().accept(this));
        return result;
    }

    @Override
    public String visit(Block block) {
        StringBuilder result = new StringBuilder();
        SymbolTable.pushFromQueue();
        for (Statement stmt : block.body)
            result.append(stmt.accept(this));
        SymbolTable.pop();
        return result.toString();
    }

    @Override
    public String visit(Conditional conditional) {
        int scopeId = ifCounter++;

        String result = conditional.getCondition().accept(this);
        SymbolTable.pushFromQueue();
        result += conditional.getThenStatement().accept(this);
        SymbolTable.pop();
        result += "goto " + generateEndLabel(scopeId) + "\n";

        result += generateElseLabel(scopeId) + ":\n";
        SymbolTable.pushFromQueue();
        result += conditional.getElseStatement().accept(this);
        SymbolTable.pop();
        result += generateEndLabel(scopeId) + ":\n";

        return result;
    }

    @Override
    public String visit(While whileStat) {
        int scopeId = whileCounter++;

        String result = generateBeginLabel(scopeId) + ":\n";
        result += whileStat.expr.accept(this);
        SymbolTable.pushFromQueue();
        result += whileStat.body.accept(this);
        SymbolTable.pop();
        result += "goto " + generateBeginLabel(scopeId) + "\n";
        result += generateElseLabel(scopeId) + ":\n";

        return result;
    }

    @Override
    public String visit(Return returnStat) {
        if (returnStat.getReturnedExpr().isLvalue()) {
            return returnStat.getReturnedExpr().accept(this) + "\nareturn\n";
        }
        return returnStat.getReturnedExpr().accept(this) + "\nireturn\n";
    }

    @Override
    public String visit(Break breakStat) {
        return "goto " + generateElseLabel(whileCounter - 1) + "\n";
    }

    @Override
    public String visit(Continue continueStat) {
        return "goto " + generateBeginLabel(whileCounter - 1) + "\n";
    }

    @Override
    public String visit(Skip skip) {
        return "";
    }

    @Override
    public String visit(LocalVarDef localVarDef) {
        SymbolTable.define();
        return "";
    }

    @Override
    public String visit(LocalVarsDefinitions localVarsDefinitions) {
        StringBuilder result = new StringBuilder();
        for (LocalVarDef lvd : localVarsDefinitions.getVarDefinitions()) {
            result.append(lvd.accept(this));
        }
        return result.toString();
    }

    @Override
    public String visit(IncStatement incStatement) {
        String result = incStatement.getOperand().accept(this);
        result += "ldc 1\n";
        result += "iadd\n";
        result += JGenrator.changeLoadToStore(incStatement.getOperand().accept(this));
//        result += decStatement.getOperand().accept(this); //TODO: ++ returns?
        return result;
    }

    @Override
    public String visit(DecStatement decStatement) {
        String result = decStatement.getOperand().accept(this);
        result += "ldc 1\n";
        result += "isub\n";
        result += JGenrator.changeLoadToStore(decStatement.getOperand().accept(this));
//        result += decStatement.getOperand().accept(this); //TODO: -- returns?
        return result;
    }

    @Override
    public String visit(ClassDeclaration classDeclaration) {
        this.currentClass = classDeclaration;
        StringBuilder result = new StringBuilder(format(".class public {0}\n", classDeclaration.getName().getName()));
        String parentName = classDeclaration.getParentName().getName() == null ?
                "java/lang/Object" :
                classDeclaration.getParentName().getName();
        result.append(format(".super {0}\n", parentName));

        SymbolTable.pushFromQueue();
        expressionTypeExtractor.setCurrentClass(classDeclaration);

        for (ClassMemberDeclaration cmd : classDeclaration.getClassMembers())
            if (cmd instanceof FieldDeclaration)
                result.append(cmd.accept(this));
        for (ClassMemberDeclaration cmd : classDeclaration.getClassMembers())
            if (cmd instanceof MethodDeclaration)
                result.append(cmd.accept(this));

        SymbolTable.pop();
        result.append(JGenrator.comment(format("end of class {0}", classDeclaration.getName().getName())));
        return String.valueOf(result);
    }

    @Override
    public String visit(EntryClassDeclaration entryClassDeclaration) {
        return this.visit( (ClassDeclaration) entryClassDeclaration);
    }

    @Override
    public String visit(FieldDeclaration fD) {
        return format(".field {2} {0} {1}\n", fD.getIdentifier().getName(), JGenrator.genType(fD.getType()), fD.getAccessModifier());
    }

    @Override
    public String visit(ParameterDeclaration parameterDeclaration) {
        SymbolTable.define();
        return JGenrator.genType(parameterDeclaration.getType());
    }

    @Override
    public String visit(MethodDeclaration mD) {
        StringBuilder result = new StringBuilder(format(".method {1} {0}(", mD.getName().getName(), mD.getAccessModifier()));
        SymbolTable.reset();
        SymbolTable.pushFromQueue();
        currentMethod = mD;
        for(ParameterDeclaration parameter : mD.getArgs() )
            result.append(parameter.accept(this));
        result.append(format("){0}\n", JGenrator.genType(mD.getReturnType())));
        result.append(format(".limit stack {0}\n", STACK_SIZE));
        result.append(format(".limit locals {0}\n", LOCALS_SIZE));

        for (Statement s : mD.getBody()) {
            result.append(s.accept(this));
        }
        SymbolTable.pop();
        result.append(".end\n");
        return result.toString();
    }

    @Override
    public String visit(Program program) {
        StringBuilder result = new StringBuilder();
        currentMethod = null;
        SymbolTable.pushFromQueue();
        for( ClassDeclaration classDeclaration : program.getClasses() )
            result.append(classDeclaration.accept(this));
        SymbolTable.pop();
        SymbolTable.resetQueueCounter();

        return result.toString();
    }
}
