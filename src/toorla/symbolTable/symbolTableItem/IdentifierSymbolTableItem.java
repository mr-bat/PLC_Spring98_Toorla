package toorla.symbolTable.symbolTableItem;

public class IdentifierSymbolTableItem extends SymbolTableItem {
    public IdentifierSymbolTableItem(String name) {
        this.name = name;
    }

    @Override
    public String getKey() {
        return name;
    }
}
