import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import toorla.ast.Program;
import toorla.jasmin.JasminCompiler;
import toorla.nameAnalyzer.NameAnalyzer;
import toorla.typeChecker.TypeChecker;
import toorla.visitor.ErrorReporter;

public class ToorlaCompiler {
    public void compile(CharStream textStream) {
        ToorlaLexer toorlaLexer = new ToorlaLexer(textStream);
        CommonTokenStream tokenStream = new CommonTokenStream(toorlaLexer);
        ToorlaParser toorlaParser = new ToorlaParser(tokenStream);
        Program toorlaASTCode = toorlaParser.program().mProgram;
        ErrorReporter errorReporter = new ErrorReporter();
        JasminCompiler jasminCompiler = new JasminCompiler();
        NameAnalyzer nameAnalyzer = new NameAnalyzer(toorlaASTCode);
        nameAnalyzer.analyze();
        toorlaASTCode.accept(errorReporter);
        TypeChecker typeChecker = new TypeChecker(nameAnalyzer.getClassHierarchy());
        toorlaASTCode.accept(typeChecker);
        int numOfErrors = toorlaASTCode.accept( errorReporter );
        if( numOfErrors > 0 )
            System.exit(1);

        System.out.println(toorlaASTCode.accept(jasminCompiler));
    }
}
