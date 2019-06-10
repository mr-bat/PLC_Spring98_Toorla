package toorla.jasmin.utils;

import toorla.ast.expression.Expression;
import toorla.ast.expression.FieldCall;
import toorla.ast.expression.Identifier;
import toorla.symbolTable.symbolTableItem.SymbolTableItem;
import toorla.symbolTable.symbolTableItem.varItems.FieldSymbolTableItem;
import toorla.types.Type;
import toorla.types.arrayType.ArrayType;
import toorla.types.singleType.BoolType;
import toorla.types.singleType.IntType;
import toorla.types.singleType.StringType;
import toorla.types.singleType.UserDefinedType;

import static java.text.MessageFormat.format;

public class JGenrator {
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
        if (exp instanceof FieldCall) {
            FieldSymbolTableItem fieldItem = (FieldSymbolTableItem) symbolTableItem;
            return format("getfield {0}\n", format("{0}/{1} {2}", fieldItem.getClassDeclaration().getName(), fieldItem.getName(), genType(fieldItem.getType())));
        }
        if (exp instanceof Identifier)
            return "iload " + ((Identifier) exp).getIndex() + "\n";

        throw new RuntimeException("Invalid state");
    }

    public static String changeLoadToStore(String generated) {
        if (generated.substring(0, 3).equals("get"))
            return generated.replaceFirst("get", "put");
        if (generated.substring(1, 5).equals("load"))
            return generated.replaceFirst("load", "store");

        throw new RuntimeException("Invalid change");
    }
}
