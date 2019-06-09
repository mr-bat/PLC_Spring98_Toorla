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
import toorla.symbolTable.SymbolTable;
import toorla.typeChecker.ExpressionTypeExtractor;
import toorla.utilities.graph.Graph;
import toorla.visitor.Visitor;

public class JasminCompiler extends Visitor<String> {
    private ExpressionTypeExtractor expressionTypeExtractor;
    private MethodDeclaration currentMethod;
    private int activeWhileStatCount;
    private int numOfEntryClasses, variableIndex;
    private Graph<String> classHierarchy;

    @Override
    public String visit(Plus plusExpr) {
        return "";
    }

    @Override
    public String visit(Minus minusExpr) {
        return "";
    }

    @Override
    public String visit(Times timesExpr) {
        return "";
    }

    @Override
    public String visit(Division divExpr) {
        return "";
    }

    @Override
    public String visit(Modulo moduloExpr) {
        return "";
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
        return "";
    }

    @Override
    public String visit(Or orExpr) {
        return "";
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
        return "";
    }

    @Override
    public String visit(Self self) {
        return "";
    }

    @Override
    public String visit(IntValue intValue) {
        return "";
    }

    @Override
    public String visit(NewArray newArray) {
        return "";
    }

    @Override
    public String visit(BoolValue booleanValue) {
        return "";
    }

    @Override
    public String visit(StringValue stringValue) {
        return "";
    }

    @Override
    public String visit(NewClassInstance newClassInstance) {
        return "";
    }

    @Override
    public String visit(FieldCall fieldCall) {
        return "";
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
        return "";
    }

    @Override
    public String visit(Block block) {
        for (Statement stmt : block.body)
            stmt.accept(this);
        return "";
    }

    @Override
    public String visit(Conditional conditional) {
        conditional.getThenStatement().accept(this);
        conditional.getElseStatement().accept(this);
        return "";
    }

    @Override
    public String visit(While whileStat) {
        whileStat.body.accept(this);
        return "";
    }

    @Override
    public String visit(Return returnStat) {
        return "";
    }

    @Override
    public String visit(Break breakStat) {
        return "";
    }

    @Override
    public String visit(Continue continueStat) {
        return "";
    }

    @Override
    public String visit(Skip skip) {
        return "";
    }

    @Override
    public String visit(LocalVarDef localVarDef) {
        System.out.println(localVarDef.getLocalVarName().getName() + " : " + localVarDef.getIndex());
        return "";
    }

    @Override
    public String visit(LocalVarsDefinitions localVarsDefinitions) {
        for (LocalVarDef lvd : localVarsDefinitions.getVarDefinitions()) {
            lvd.accept(this);
        }
        return "";
    }

    @Override
    public String visit(IncStatement incStatement) {
        return "";
    }

    @Override
    public String visit(DecStatement decStatement) {
        return "";
    }

    @Override
    public String visit(ClassDeclaration classDeclaration) {
        for (ClassMemberDeclaration cmd : classDeclaration.getClassMembers()) {
            cmd.accept(this);
        }
        return "";
    }

    @Override
    public String visit(EntryClassDeclaration entryClassDeclaration) {
        this.visit( (ClassDeclaration) entryClassDeclaration);
        return "";
    }

    @Override
    public String visit(FieldDeclaration fieldDeclaration) {
        return "";
    }

    @Override
    public String visit(ParameterDeclaration parameterDeclaration) {
        System.out.println(parameterDeclaration.getIdentifier().getName() + " : " + parameterDeclaration.getIndex());
        return "";
    }

    @Override
    public String visit(MethodDeclaration methodDeclaration) {
        currentMethod = methodDeclaration;
        for(ParameterDeclaration parameter : methodDeclaration.getArgs() )
            parameter.accept( this);
        for (Statement s : methodDeclaration.getBody()) {
            s.accept(this);
        }
        return "";
    }

    @Override
    public String visit(Program program) {
        currentMethod = null;
        activeWhileStatCount = 0;
        SymbolTable.pushFromQueue();
        for( ClassDeclaration classDeclaration : program.getClasses() )
            classDeclaration.accept(this);
        SymbolTable.pop();
        SymbolTable.resetQueueCounter();

        return "";
    }
}
