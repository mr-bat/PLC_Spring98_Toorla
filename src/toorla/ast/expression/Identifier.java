package toorla.ast.expression;

import toorla.visitor.Visitor;

public class Identifier extends Expression {
    private String name;
    private Integer index = null;

    public Identifier(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public <R> R accept(Visitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        if( name != null )
            return "(Identifier," + name + (index != null ? ("_" + index) : "") + ")";
        else return "(Identifier,Dummy)";
    }
}
