package toorla.ast.declaration.localVarDecs;

import toorla.ast.declaration.TypedVariableDeclaration;
import toorla.ast.expression.Identifier;
import toorla.types.Type;
import toorla.visitor.IVisitor;

public class ParameterDeclaration extends TypedVariableDeclaration {
    private int index;

    public ParameterDeclaration(Identifier name, Type type) {
        this.identifier = name;
        this.type = type;
    }

    public int getIndex() {
        return identifier.getIndex();
    }

    public void setIndex(int index) {
        this.identifier.setIndex(index);
    }

    @Override
    public <R> R accept(IVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "Parameter";
    }
}