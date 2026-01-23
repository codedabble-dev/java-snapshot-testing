package au.com.origin.snapshots.jackson3.docs;

import au.com.origin.snapshots.jackson3.serializers.v1.DeterministicJacksonSnapshotSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public class HibernateSnapshotSerializer extends DeterministicJacksonSnapshotSerializer {

    @Override
    public void configure(final JsonMapper.Builder builder) {
        super.configure(builder);

        // Ignore Hibernate Lists to prevent infinite recursion
        builder.addMixIn(List.class, IgnoreTypeMixin.class);
        builder.addMixIn(Set.class, IgnoreTypeMixin.class);

        // Ignore Fields that Hibernate generates for us automatically
        builder.addMixIn(BaseEntity.class, IgnoreHibernateEntityFields.class);
    }

    @JsonIgnoreType
    class IgnoreTypeMixin {
    }

    abstract class IgnoreHibernateEntityFields {
        @JsonIgnore
        abstract Long getId();

        @JsonIgnore
        abstract Instant getCreatedDate();

        @JsonIgnore
        abstract Instant getLastModifiedDate();
    }
}
