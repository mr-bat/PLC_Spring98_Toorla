package toorla.jasmin.utils;

import toorla.ast.expression.Expression;
import toorla.ast.expression.FieldCall;
import toorla.ast.expression.Identifier;
import toorla.ast.expression.binaryExpression.BinaryExpression;
import toorla.jasmin.JasminCompiler;
import toorla.symbolTable.symbolTableItem.SymbolTableItem;
import toorla.symbolTable.symbolTableItem.varItems.FieldSymbolTableItem;
import toorla.symbolTable.symbolTableItem.varItems.LocalVariableSymbolTableItem;
import toorla.types.Type;
import toorla.types.arrayType.ArrayType;
import toorla.types.singleType.BoolType;
import toorla.types.singleType.IntType;
import toorla.types.singleType.StringType;
import toorla.types.singleType.UserDefinedType;

import static java.text.MessageFormat.format;

public class JGenrator {
    private static int nextLabel = 0;

    public static String comment(String s) {
        return format("; {0}\n", s);
    }

    public static String genType(Type type) {
        if(type instanceof BoolType)
            return "Z";
        if(type instanceof IntType)
            return "I";
        if(type instanceof StringType)
            return "Ljava/lang/String;";
        if(type instanceof ArrayType)
            return "[" + genType(((ArrayType) type).getSingleType());
        if(type instanceof UserDefinedType)
            return "L" + ((UserDefinedType) type).getClassDeclaration().getName().getName() + ";";

        throw new RuntimeException("this type is inappropriate");
    }

    public static String loadLVal(SymbolTableItem symbolTableItem, Expression exp) {
        if (!exp.isLvalue())
            throw new RuntimeException("loadLVal needs l-value");
        if (symbolTableItem instanceof FieldSymbolTableItem) {
            FieldSymbolTableItem fieldItem = (FieldSymbolTableItem) symbolTableItem;
            return format("getfield {0}/{1} {2}\n", fieldItem.getClassDeclaration().getName().getName(), fieldItem.getName(), genType(fieldItem.getType()));
        }
        if (symbolTableItem instanceof LocalVariableSymbolTableItem)
            return "iload " + ((LocalVariableSymbolTableItem) symbolTableItem).getIndex() + "\n";

        throw new RuntimeException("Invalid state");
    }

    public static String changeLoadToStore(String generated) {
        if (generated.substring(0, 3).equals("get"))
            return generated.replaceFirst("get", "put");
        if (generated.substring(1, 5).equals("load"))
            return generated.replaceFirst("load", "store");

        throw new RuntimeException("Invalid change");
    }

    public static String genCondition(String type) {
        String result = "if_icmp" + type + " condition_" + nextLabel + "\n";
        result += "pop\niconst_1\n";
        result += nextLabel + ": \n";

        return result;
    }
}
