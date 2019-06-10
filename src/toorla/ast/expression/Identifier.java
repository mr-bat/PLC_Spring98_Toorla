package toorla.ast.expression;

import toorla.visitor.IVisitor;

public class Identifier extends Expression {
    private String name;
    private int index;

    public Identifier(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public <R> R accept(IVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        if (name != null)
            return "(Identifier," + name + ")";
        else
            return "(Identifier,Dummy)";
    }

    @Override
    public boolean isLvalue()
    {
        return true;
    }
}
