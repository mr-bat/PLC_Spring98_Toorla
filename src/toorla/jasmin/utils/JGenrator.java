package toorla.jasmin.utils;

import toorla.types.Type;
import toorla.types.arrayType.ArrayType;
import toorla.types.singleType.BoolType;
import toorla.types.singleType.IntType;
import toorla.types.singleType.StringType;
import toorla.types.singleType.UserDefinedType;

import java.text.MessageFormat;

public class JGenrator {
    public static String comment(String s) {
        return MessageFormat.format("; {0}\n", s);
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
}
