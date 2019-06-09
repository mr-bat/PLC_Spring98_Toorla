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
import toorla.types.Type;
import toorla.utilities.graph.Graph;
import toorla.visitor.Visitor;

import java.text.MessageFormat;

import static java.text.MessageFormat.format;

public class JasminCompiler extends Visitor<String> {
    private ExpressionTypeExtractor expressionTypeExtractor;
    private MethodDeclaration currentMethod;
    private Graph<String> classHierarchy;

    public JasminCompiler(Graph<String> classHierarchy) {
        this.expressionTypeExtractor = new ExpressionTypeExtractor(classHierarchy);
        this.classHierarchy = classHierarchy;
    }

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
        SymbolTable.pushFromQueue();
        for (Statement stmt : block.body)
            stmt.accept(this);
        SymbolTable.pop();
        return "";
    }

    @Override
    public String visit(Conditional conditional) {
        SymbolTable.pushFromQueue();
        conditional.getThenStatement().accept(this);
        SymbolTable.pop();
        SymbolTable.pushFromQueue();
        conditional.getElseStatement().accept(this);
        SymbolTable.pop();
        return "";
    }

    @Override
    public String visit(While whileStat) {
        SymbolTable.pushFromQueue();
        whileStat.body.accept(this);
        SymbolTable.pop();
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
        SymbolTable.define();
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
        StringBuilder result = new StringBuilder(format(".class public {0}\n", classDeclaration.getName().getName()));
        String parentName = classDeclaration.getParentName().getName() == null ?
                "java/lang/Object" :
                classDeclaration.getParentName().getName();
        result.append(format(".super {0}\n", classDeclaration.getParentName().getName()));

        SymbolTable.pushFromQueue();
        expressionTypeExtractor.setCurrentClass(classDeclaration);

        for (ClassMemberDeclaration cmd : classDeclaration.getClassMembers()) {
            result.append(cmd.accept(this));
        }
        SymbolTable.pop();
        result.append(JGenrator.comment(format("end of class {0}", classDeclaration.getName().getName())));
        return String.valueOf(result);
    }

    @Override
    public String visit(EntryClassDeclaration entryClassDeclaration) {
        return this.visit( (ClassDeclaration) entryClassDeclaration);
    }

    @Override
    public String visit(FieldDeclaration fieldDeclaration) {
        return "";
    }

    @Override
    public String visit(ParameterDeclaration parameterDeclaration) {
        SymbolTable.define();
        return "";
    }

    @Override
    public String visit(MethodDeclaration methodDeclaration) {
        SymbolTable.reset();
        SymbolTable.pushFromQueue();
        currentMethod = methodDeclaration;
        for(ParameterDeclaration parameter : methodDeclaration.getArgs() )
            parameter.accept( this);
        for (Statement s : methodDeclaration.getBody()) {
            s.accept(this);
        }
        SymbolTable.pop();
        return "";
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
