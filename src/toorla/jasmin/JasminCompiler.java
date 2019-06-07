package toorla.jasmin;

import toorla.ast.Program;
import toorla.ast.declaration.classDecs.ClassDeclaration;
import toorla.ast.declaration.classDecs.EntryClassDeclaration;
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
import toorla.visitor.Visitor;

public class JasminCompiler extends Visitor<String> {
    @Override
    public String visit(Plus plusExpr) {
        return super.visit(plusExpr);
    }

    @Override
    public String visit(Minus minusExpr) {
        return super.visit(minusExpr);
    }

    @Override
    public String visit(Times timesExpr) {
        return super.visit(timesExpr);
    }

    @Override
    public String visit(Division divExpr) {
        return super.visit(divExpr);
    }

    @Override
    public String visit(Modulo moduloExpr) {
        return super.visit(moduloExpr);
    }

    @Override
    public String visit(Equals equalsExpr) {
        return super.visit(equalsExpr);
    }

    @Override
    public String visit(GreaterThan gtExpr) {
        return super.visit(gtExpr);
    }

    @Override
    public String visit(LessThan lessThanExpr) {
        return super.visit(lessThanExpr);
    }

    @Override
    public String visit(And andExpr) {
        return super.visit(andExpr);
    }

    @Override
    public String visit(Or orExpr) {
        return super.visit(orExpr);
    }

    @Override
    public String visit(Neg negExpr) {
        return super.visit(negExpr);
    }

    @Override
    public String visit(Not notExpr) {
        return super.visit(notExpr);
    }

    @Override
    public String visit(MethodCall methodCall) {
        return super.visit(methodCall);
    }

    @Override
    public String visit(Identifier identifier) {
        return super.visit(identifier);
    }

    @Override
    public String visit(Self self) {
        return super.visit(self);
    }

    @Override
    public String visit(IntValue intValue) {
        return super.visit(intValue);
    }

    @Override
    public String visit(NewArray newArray) {
        return super.visit(newArray);
    }

    @Override
    public String visit(BoolValue booleanValue) {
        return super.visit(booleanValue);
    }

    @Override
    public String visit(StringValue stringValue) {
        return super.visit(stringValue);
    }

    @Override
    public String visit(NewClassInstance newClassInstance) {
        return super.visit(newClassInstance);
    }

    @Override
    public String visit(FieldCall fieldCall) {
        return super.visit(fieldCall);
    }

    @Override
    public String visit(ArrayCall arrayCall) {
        return super.visit(arrayCall);
    }

    @Override
    public String visit(NotEquals notEquals) {
        return super.visit(notEquals);
    }

    @Override
    public String visit(PrintLine printStat) {
        return super.visit(printStat);
    }

    @Override
    public String visit(Assign assignStat) {
        return super.visit(assignStat);
    }

    @Override
    public String visit(Block block) {
        return super.visit(block);
    }

    @Override
    public String visit(Conditional conditional) {
        return super.visit(conditional);
    }

    @Override
    public String visit(While whileStat) {
        return super.visit(whileStat);
    }

    @Override
    public String visit(Return returnStat) {
        return super.visit(returnStat);
    }

    @Override
    public String visit(Break breakStat) {
        return super.visit(breakStat);
    }

    @Override
    public String visit(Continue continueStat) {
        return super.visit(continueStat);
    }

    @Override
    public String visit(Skip skip) {
        return super.visit(skip);
    }

    @Override
    public String visit(LocalVarDef localVarDef) {
        return super.visit(localVarDef);
    }

    @Override
    public String visit(IncStatement incStatement) {
        return super.visit(incStatement);
    }

    @Override
    public String visit(DecStatement decStatement) {
        return super.visit(decStatement);
    }

    @Override
    public String visit(ClassDeclaration classDeclaration) {
        return super.visit(classDeclaration);
    }

    @Override
    public String visit(EntryClassDeclaration entryClassDeclaration) {
        return super.visit(entryClassDeclaration);
    }

    @Override
    public String visit(FieldDeclaration fieldDeclaration) {
        return super.visit(fieldDeclaration);
    }

    @Override
    public String visit(ParameterDeclaration parameterDeclaration) {
        return super.visit(parameterDeclaration);
    }

    @Override
    public String visit(MethodDeclaration methodDeclaration) {
        return super.visit(methodDeclaration);
    }

    @Override
    public String visit(LocalVarsDefinitions localVarsDefinitions) {
        return super.visit(localVarsDefinitions);
    }

    @Override
    public String visit(Program program) {
        return super.visit(program);
    }
}
