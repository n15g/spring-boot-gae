package contrib.springframework.data.gcp.search.metadata.impl;

import contrib.springframework.data.gcp.search.metadata.Accessor;
import contrib.springframework.data.gcp.search.metadata.IndexTypeRegistry;
import contrib.springframework.data.gcp.search.IndexType;
import contrib.springframework.data.gcp.search.SearchIndex;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class FieldAccessorTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private IndexTypeRegistry indexTypeRegistry;


    @Test
    public void constructWithSpecificType() throws Exception {
        Field member = FieldAccessorTest.TestClass.class.getDeclaredField("stringField");

        Accessor accessor = new FieldAccessor(FieldAccessorTest.TestClass.class, member, IndexType.IDENTIFIER);

        assertThat(accessor.getEntityType()).isEqualTo(FieldAccessorTest.TestClass.class);
        assertThat(accessor.getMember()).isEqualTo(member);
        assertThat(accessor.getMemberType()).isEqualTo(String.class);
        assertThat(accessor.getMemberName()).isEqualTo("stringField");
        assertThat(accessor.getIndexName()).isEqualTo(new FieldNameLocator().apply(member));
        assertThat(accessor.getEncodedName()).isEqualTo(new FieldNameEncoder().apply(accessor.getMemberName()));
        assertThat(accessor.getIndexType()).isEqualTo(IndexType.IDENTIFIER);
        assertThat(accessor.getValue(new FieldAccessorTest.TestClass())).isEqualTo("stringValue");
    }

    @Test
    public void constructWithTypeLookup() throws Exception {
        Field member = FieldAccessorTest.TestClass.class.getDeclaredField("stringField");

        when(indexTypeRegistry.apply(String.class)).thenReturn(IndexType.TEXT);
        Accessor accessor = new FieldAccessor(FieldAccessorTest.TestClass.class, member, indexTypeRegistry);

        assertThat(accessor.getEntityType()).isEqualTo(FieldAccessorTest.TestClass.class);
        assertThat(accessor.getMember()).isEqualTo(member);
        assertThat(accessor.getMemberType()).isEqualTo(String.class);
        assertThat(accessor.getMemberName()).isEqualTo("stringField");
        assertThat(accessor.getIndexName()).isEqualTo(new FieldNameLocator().apply(member));
        assertThat(accessor.getEncodedName()).isEqualTo(new FieldNameEncoder().apply(accessor.getMemberName()));
        assertThat(accessor.getIndexType()).isEqualTo(IndexType.TEXT);
        assertThat(accessor.getValue(new FieldAccessorTest.TestClass())).isEqualTo("stringValue");
    }


    @Test
    public void constructWithUnannotatedField() throws Exception {
        Field member = FieldAccessorTest.TestClass.class.getDeclaredField("unannotatedField");

        when(indexTypeRegistry.apply(String.class)).thenReturn(IndexType.TEXT);
        Accessor accessor = new FieldAccessor(FieldAccessorTest.TestClass.class, member, indexTypeRegistry);

        assertThat(accessor.getEntityType()).isEqualTo(FieldAccessorTest.TestClass.class);
        assertThat(accessor.getMember()).isEqualTo(member);
        assertThat(accessor.getMemberType()).isEqualTo(String.class);
        assertThat(accessor.getMemberName()).isEqualTo("unannotatedField");
        assertThat(accessor.getIndexName()).isEqualTo(new FieldNameLocator().apply(member));
        assertThat(accessor.getEncodedName()).isEqualTo(new FieldNameEncoder().apply(accessor.getMemberName()));
        assertThat(accessor.getIndexType()).isEqualTo(IndexType.TEXT);
        assertThat(accessor.getValue(new FieldAccessorTest.TestClass())).isEqualTo("unannotatedValue");
    }

    @Test
    public void constructWithAutoType() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Cannot construct an Accessor with index type AUTO. Use an IndexTypeRegistry instead.");
        Field member = FieldAccessorTest.TestClass.class.getDeclaredField("stringField");

        new FieldAccessor(FieldAccessorTest.TestClass.class, member, IndexType.AUTO);
    }

    @Test
    public void constructWithNamedMember() throws Exception {
        Field member = FieldAccessorTest.TestClass.class.getDeclaredField("namedField");

        Accessor accessor = new FieldAccessor(FieldAccessorTest.TestClass.class, member, IndexType.IDENTIFIER);

        assertThat(accessor.getEntityType()).isEqualTo(FieldAccessorTest.TestClass.class);
        assertThat(accessor.getMember()).isEqualTo(member);
        assertThat(accessor.getMemberType()).isEqualTo(String.class);
        assertThat(accessor.getMemberName()).isEqualTo(new FieldNameLocator().apply(member));
        assertThat(accessor.getIndexName()).isEqualTo(new FieldNameEncoder().apply(accessor.getMemberName()));
        assertThat(accessor.getIndexType()).isEqualTo(IndexType.IDENTIFIER);
        assertThat(accessor.getValue(new FieldAccessorTest.TestClass())).isEqualTo("namedFieldValue");
    }

    @Test
    public void constructWithTypedMember() throws Exception {
        Field member = FieldAccessorTest.TestClass.class.getDeclaredField("typedField");

        Accessor accessor = new FieldAccessor(FieldAccessorTest.TestClass.class, member, indexTypeRegistry);

        assertThat(accessor.getEntityType()).isEqualTo(FieldAccessorTest.TestClass.class);
        assertThat(accessor.getMember()).isEqualTo(member);
        assertThat(accessor.getMemberType()).isEqualTo(String.class);
        assertThat(accessor.getMemberName()).isEqualTo(new FieldNameLocator().apply(member));
        assertThat(accessor.getIndexName()).isEqualTo(new FieldNameEncoder().apply(accessor.getMemberName()));
        assertThat(accessor.getIndexType()).isEqualTo(IndexType.GEOPOINT);
        assertThat(accessor.getValue(new FieldAccessorTest.TestClass())).isEqualTo("typedFieldValue");
    }

    private static class TestClass {
        @SearchIndex
        private String stringField = "stringValue";

        private String unannotatedField = "unannotatedValue";

        @SearchIndex(name = "myField")
        private String namedField = "namedFieldValue";

        @SearchIndex(type = IndexType.GEOPOINT)
        private String typedField = "typedFieldValue";
    }
}
