package io.github.finoid.snapshots.jackson.serializers.v1;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

class SnapshotPrettyPrinter extends DefaultPrettyPrinter {

    @SuppressWarnings("deprecation") // TODO (nw) rewrite
    public SnapshotPrettyPrinter() {
        super("");
        Indenter lfOnlyIndenter = new DefaultIndenter("  ", "\n");
        this.indentArraysWith(lfOnlyIndenter);
        this.indentObjectsWith(lfOnlyIndenter);

        this._objectFieldValueSeparatorWithSpaces =
            this._separators.getObjectFieldValueSeparator() + " ";
    }

    // It's a requirement
    // @see https://github.com/FasterXML/jackson-databind/issues/2203
    @Override
    public DefaultPrettyPrinter createInstance() {
        return new DefaultPrettyPrinter(this);
    }
}
