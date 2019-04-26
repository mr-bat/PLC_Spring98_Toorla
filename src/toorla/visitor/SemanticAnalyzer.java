package toorla.visitor;

import toorla.ast.Program;
import toorla.ast.Tree;
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
import toorla.symbolTable.exceptions.ItemAlreadyExistsException;
import toorla.symbolTable.symbolTableItem.IdentifierSymbolTableItem;
import toorla.symbolTable.symbolTableItem.varItems.LocalVariableSymbolTableItem;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class SemanticAnalyzer implements Visitor<Tree> {
    private Map<String, SymbolTable> methods = new HashMap<>(), fields = new HashMap<>();
    private SymbolTable classList;
    private SymbolTable currentMethods, currentFields;
    private Integer VarIndex;
    private boolean showError;

    private void initializeClassList() {
        classList = new SymbolTable();
        try {
            classList.put(new IdentifierSymbolTableItem("Any"));
        } catch (ItemAlreadyExistsException ignored) { }
    }
    public SemanticAnalyzer() {
        initializeClassList();
    }

    @Override
    public Tree visit(PrintLine printLine) {
        printLine.getArg().accept(this);
        return printLine;
    }

    @Override
    public Tree visit(Assign assign) {
        assign.getLvalue().accept(this);
        assign.getRvalue().accept(this);
        return assign;
    }

    @Override
    public Tree visit(Block block) { // TODO
        SymbolTable.push(new SymbolTable());
        for (Statement s : block.body)
            s.accept(this);
        SymbolTable.pop();
        return block;
    }

    @Override
    public Tree visit(Conditional conditional) {
        conditional.getCondition().accept(this);
        conditional.getThenStatement().accept(this);
        conditional.getElseStatement().accept(this);
        return conditional;
    }

    @Override
    public Tree visit(While whileStat) {
        whileStat.expr.accept(this);
        whileStat.body.accept(this);
        return whileStat;
    }

    @Override
    public Tree visit(Return returnStat) {
        returnStat.getReturnedExpr().accept(this);
        return returnStat;
    }

    @Override
    public Tree visit(Plus plusExpr) {
        plusExpr.getLhs().accept(this);
        plusExpr.getRhs().accept(this);
        return plusExpr;
    }

    @Override
    public Tree visit(Minus minusExpr) {
        minusExpr.getLhs().accept(this);
        minusExpr.getRhs().accept(this);
        return minusExpr;
    }

    @Override
    public Tree visit(Times timesExpr) {
        timesExpr.getLhs().accept(this);
        timesExpr.getRhs().accept(this);
        return timesExpr;
    }

    @Override
    public Tree visit(Division divisionExpr) {
        divisionExpr.getLhs().accept(this);
        divisionExpr.getRhs().accept(this);
        return divisionExpr;
    }

    @Override
    public Tree visit(Modulo moduloExpr) {
        moduloExpr.getLhs().accept(this);
        moduloExpr.getRhs().accept(this);
        return moduloExpr;
    }

    @Override
    public Tree visit(Equals equalsExpr) {
        equalsExpr.getLhs().accept(this);
        equalsExpr.getRhs().accept(this);
        return equalsExpr;
    }

    @Override
    public Tree visit(GreaterThan gtExpr) {
        gtExpr.getLhs().accept(this);
        gtExpr.getRhs().accept(this);
        return gtExpr;
    }

    @Override
    public Tree visit(LessThan ltExpr) {
        ltExpr.getLhs().accept(this);
        ltExpr.getRhs().accept(this);
        return ltExpr;
    }

    @Override
    public Tree visit(And andExpr) {
        andExpr.getLhs().accept(this);
        andExpr.getRhs().accept(this);
        return andExpr;
    }

    @Override
    public Tree visit(Or orExpr) {
        orExpr.getLhs().accept(this);
        orExpr.getRhs().accept(this);
        return orExpr;
    }

    @Override
    public Tree visit(Neg negExpr) {
        negExpr.getExpr().accept(this);
        return negExpr;
    }

    @Override
    public Tree visit(Not notExpr) {
        notExpr.getExpr().accept(this);
        return notExpr;
    }

    @Override
    public Tree visit(MethodCall methodCall) {
        methodCall.getInstance().accept(this);
        methodCall.getMethodName().accept(this);
        for (Expression arg : methodCall.getArgs()) {
            arg.accept(this);
        }
        return methodCall;
    }

    @Override
    public Tree visit(Identifier identifier) {
        return identifier;
    }

    @Override
    public Tree visit(Self self) {
        return self;
    }

    @Override
    public Tree visit(Break breakStat) {
        return breakStat;
    }

    @Override
    public Tree visit(Continue continueStat) {
        return continueStat;
    }

    @Override
    public Tree visit(Skip skip) {
        return skip;
    }

    @Override
    public Tree visit(IntValue intValue) {
        return intValue;
    }

    @Override
    public Tree visit(NewArray newArray) {
        newArray.getLength().accept(this);
        return newArray;
    }

    @Override
    public Tree visit(BoolValue booleanValue) {
        return booleanValue;
    }

    @Override
    public Tree visit(StringValue stringValue) {
        return stringValue;
    }

    @Override
    public Tree visit(NewClassInstance newClassInstance) {
        newClassInstance.getClassName().accept(this);
        return newClassInstance;
    }

    @Override
    public Tree visit(FieldCall fieldCall) {
        fieldCall.getInstance().accept(this);
        fieldCall.getField().accept(this);
        return fieldCall;
    }

    @Override
    public Tree visit(ArrayCall arrayCall) {
        arrayCall.getInstance().accept(this);
        arrayCall.getIndex().accept(this);
        return arrayCall;
    }

    @Override
    public Tree visit(NotEquals notEquals) {
        notEquals.getLhs().accept(this);
        notEquals.getRhs().accept(this);
        return notEquals;
    }

    @Override
    public Tree visit(LocalVarDef localVarDef) {
        Identifier identifier = localVarDef.getLocalVarName();
        try {
            SymbolTable.top.put(new LocalVariableSymbolTableItem(identifier.getName(), VarIndex));
            identifier.setIndex(VarIndex);
            ++VarIndex;
        } catch (ItemAlreadyExistsException e) {
            if (showError)
                System.out.println(MessageFormat.format(
                    "Error:Line:{0}:Redefinition of Local Variable {1} in current scope",
                    identifier.line, identifier.getName()
                ));
        }
        localVarDef.getLocalVarName().accept(this);
        localVarDef.getInitialValue().accept(this);
        return localVarDef;
    }

    @Override
    public Tree visit(IncStatement incStatement) {
        incStatement.getOperand().accept(this);
        return incStatement;
    }

    @Override
    public Tree visit(DecStatement decStatement) {
        decStatement.getOperand().accept(this);
        return decStatement;
    }


    private void initializeNewClass(Identifier identifier) {
        currentMethods = new SymbolTable();
        currentFields = new SymbolTable();
        try {
            classList.put(new IdentifierSymbolTableItem(identifier.getName()));
        } catch (ItemAlreadyExistsException e) {
            if (showError)
                System.out.println(MessageFormat.format(
                        "Error:Line:{0}:Redefinition of Class {1}",
                        identifier.line, identifier.getName()
                ));
        }
    }

    private void handleClassInheritance(String parName) {
        SymbolTable parMethodsList = methods.get(parName);
        SymbolTable parFieldsList = fields.get(parName);
        currentMethods = new SymbolTable(parMethodsList);
        currentFields = new SymbolTable(parFieldsList);
    }

    @Override
    public Tree visit(ClassDeclaration classDeclaration) {
        Identifier identifier = classDeclaration.getName();
        initializeNewClass(identifier);
        identifier.accept(this);

        if (classDeclaration.getParentName().getName() != null) {
            String parName = classDeclaration.getParentName().getName();
            handleClassInheritance(parName);
            classDeclaration.getParentName().accept(this);
        }

        for (ClassMemberDeclaration md : classDeclaration.getClassMembers())
            md.accept(this);

        finalizeClass(identifier);
        return classDeclaration;
    }

    private void finalizeClass(Identifier identifier) {
        methods.put(identifier.getName(), currentMethods);
        currentMethods = null;
        fields.put(identifier.getName(), currentFields);
        currentFields = null;
    }

    @Override
    public Tree visit(EntryClassDeclaration entryClassDeclaration) {
        return visit((ClassDeclaration) entryClassDeclaration);
    }

    @Override
    public Tree visit(FieldDeclaration fieldDeclaration) { // TODO: SHOULD ADD FIELD INDEX?
        Identifier identifier = fieldDeclaration.getIdentifier();
        if (identifier.getName().equals("length")) {
            if (showError)
                System.out.println(MessageFormat.format(
                        "Error:Line:{0}:Definition of length as field of a class",
                        identifier.line
                ));
        } else {
            try {
                currentFields.uniquePut(new IdentifierSymbolTableItem(identifier.getName()));
            } catch (ItemAlreadyExistsException e) {
                if (showError)
                    System.out.println(MessageFormat.format(
                            "Error:Line:{0}:Redefinition of Field {1}",
                            identifier.line, identifier.getName()
                    ));
            }
        }

        fieldDeclaration.getIdentifier().accept(this);
        return fieldDeclaration;
    }

    @Override
    public Tree visit(ParameterDeclaration parameterDeclaration) {
        Identifier identifier = parameterDeclaration.getIdentifier();
        try {
            SymbolTable.top.put(new LocalVariableSymbolTableItem(identifier.getName(), VarIndex));
            identifier.setIndex(VarIndex);
            ++VarIndex;
        } catch (ItemAlreadyExistsException e) {
            if (showError)
                System.out.println(MessageFormat.format(
                    "Error:Line:{0}:Redefinition of Local Variable {1} in current scope",
                    identifier.line, identifier.getName()
                ));
        }

        identifier.accept(this);
        return parameterDeclaration;
    }

    @Override
    public Tree visit(MethodDeclaration methodDeclaration) {
        SymbolTable.push(new SymbolTable());
        VarIndex = 1;

        Identifier identifier = methodDeclaration.getName();
        try {
            currentFields.uniquePut(new IdentifierSymbolTableItem(identifier.getName()));
        } catch (ItemAlreadyExistsException e) {
            if (showError)
                System.out.println(MessageFormat.format(
                        "Error:Line:{0}:Redefinition of Method {1}",
                        identifier.line, identifier.getName()
                ));
        }
        methodDeclaration.getName().accept(this);
        for (ParameterDeclaration pd : methodDeclaration.getArgs()) {
            pd.accept(this);
        }
        for (Statement stmt : methodDeclaration.getBody())
            stmt.accept(this);
        SymbolTable.pop();
        return methodDeclaration;
    }

    @Override
    public Tree visit(LocalVarsDefinitions localVarsDefinitions) {
        for (LocalVarDef lvd : localVarsDefinitions.getVarDefinitions())
            lvd.accept(this);
        return localVarsDefinitions;
    }

    @Override
    public Tree visit(Program program) {
        showError = false;
        for (ClassDeclaration ignored : program.getClasses())
            for (ClassDeclaration cd : program.getClasses())
                cd.accept(this);

        initializeClassList();
        showError = true;
        for (ClassDeclaration cd : program.getClasses())
            cd.accept(this);
        return program;
    }
}