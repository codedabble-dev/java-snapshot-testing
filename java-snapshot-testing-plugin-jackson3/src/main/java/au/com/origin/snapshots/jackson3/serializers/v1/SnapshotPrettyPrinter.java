package au.com.origin.snapshots.jackson3.serializers.v1;

import tools.jackson.core.JsonGenerator;
import tools.jackson.core.util.DefaultIndenter;
import tools.jackson.core.util.DefaultPrettyPrinter;
import tools.jackson.core.util.Separators;

class SnapshotPrettyPrinter extends DefaultPrettyPrinter {

  private static final Separators SNAPSHOT_SEPARATORS =
      Separators.createDefaultInstance().withObjectNameValueSpacing(Separators.Spacing.AFTER);

  public SnapshotPrettyPrinter() {
    this(SNAPSHOT_SEPARATORS);
  }

  private SnapshotPrettyPrinter(Separators separators) {
    super(separators);
    Indenter lfOnlyIndenter = new DefaultIndenter("  ", "\n");
    this.indentArraysWith(lfOnlyIndenter);
    this.indentObjectsWith(lfOnlyIndenter);
  }

  private SnapshotPrettyPrinter(SnapshotPrettyPrinter base) {
    super(base);
  }

  private SnapshotPrettyPrinter(SnapshotPrettyPrinter base, Separators separators) {
    super(base, separators);
  }

  @Override
  public void writeRootValueSeparator(JsonGenerator gen) {}

  @Override
  public SnapshotPrettyPrinter withSeparators(Separators separators) {
    return new SnapshotPrettyPrinter(this, separators);
  }

  @Override
  public DefaultPrettyPrinter createInstance() {
    return new SnapshotPrettyPrinter(this);
  }
}
