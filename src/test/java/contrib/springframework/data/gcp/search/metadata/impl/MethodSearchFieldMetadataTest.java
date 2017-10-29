package contrib.springframework.data.gcp.search.metadata.impl;

import contrib.springframework.data.gcp.search.IndexType;
import contrib.springframework.data.gcp.search.SearchIndex;
import contrib.springframework.data.gcp.search.metadata.SearchFieldMetadata;
import contrib.springframework.data.gcp.search.metadata.IndexTypeRegistry;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class MethodSearchFieldMetadataTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private IndexTypeRegistry indexTypeRegistry;


    @Test
    public void constructWithSpecificType() throws Exception {
        Method member = TestClass.class.getMethod("stringMethod");

        SearchFieldMetadata searchFieldMetadata = new MethodSearchFieldMetadata(TestClass.class, member, IndexType.IDENTIFIER);

        assertThat(searchFieldMetadata.getEntityType()).isEqualTo(TestClass.class);
        assertThat(searchFieldMetadata.getMember()).isEqualTo(member);
        assertThat(searchFieldMetadata.getMemberType()).isEqualTo(String.class);
        assertThat(searchFieldMetadata.getMemberName()).isEqualTo("stringMethod");
        assertThat(searchFieldMetadata.getIndexName()).isEqualTo(new MethodNameCalculator().apply(member));
        assertThat(searchFieldMetadata.getEncodedName()).isEqualTo(new FieldNameEncoder().apply(searchFieldMetadata.getMemberName()));
        Assertions.assertThat(searchFieldMetadata.getIndexType()).isEqualTo(IndexType.IDENTIFIER);
        assertThat(searchFieldMetadata.getValue(new TestClass())).isEqualTo("stringValue");
    }

    @Test
    public void constructWithTypeLookup() throws Exception {
        Method member = TestClass.class.getMethod("stringMethod");

        when(indexTypeRegistry.apply(String.class)).thenReturn(IndexType.TEXT);
        SearchFieldMetadata searchFieldMetadata = new MethodSearchFieldMetadata(TestClass.class, member, indexTypeRegistry);

        assertThat(searchFieldMetadata.getEntityType()).isEqualTo(TestClass.class);
        assertThat(searchFieldMetadata.getMember()).isEqualTo(member);
        assertThat(searchFieldMetadata.getMemberType()).isEqualTo(String.class);
        assertThat(searchFieldMetadata.getMemberName()).isEqualTo("stringMethod");
        assertThat(searchFieldMetadata.getIndexName()).isEqualTo(new MethodNameCalculator().apply(member));
        assertThat(searchFieldMetadata.getEncodedName()).isEqualTo(new FieldNameEncoder().apply(searchFieldMetadata.getMemberName()));
        Assertions.assertThat(searchFieldMetadata.getIndexType()).isEqualTo(IndexType.TEXT);
        assertThat(searchFieldMetadata.getValue(new TestClass())).isEqualTo("stringValue");
    }


    @Test
    public void constructWithUnannotatedMethod() throws Exception {
        Method member = TestClass.class.getMethod("unannotatedMethod");

        when(indexTypeRegistry.apply(String.class)).thenReturn(IndexType.TEXT);
        SearchFieldMetadata searchFieldMetadata = new MethodSearchFieldMetadata(TestClass.class, member, indexTypeRegistry);

        assertThat(searchFieldMetadata.getEntityType()).isEqualTo(TestClass.class);
        assertThat(searchFieldMetadata.getMember()).isEqualTo(member);
        assertThat(searchFieldMetadata.getMemberType()).isEqualTo(String.class);
        assertThat(searchFieldMetadata.getMemberName()).isEqualTo("unannotatedMethod");
        assertThat(searchFieldMetadata.getIndexName()).isEqualTo(new MethodNameCalculator().apply(member));
        assertThat(searchFieldMetadata.getEncodedName()).isEqualTo(new FieldNameEncoder().apply(searchFieldMetadata.getMemberName()));
        Assertions.assertThat(searchFieldMetadata.getIndexType()).isEqualTo(IndexType.TEXT);
        assertThat(searchFieldMetadata.getValue(new TestClass())).isEqualTo("unannotatedValue");
    }

    @Test
    public void constructWithAutoType() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Cannot construct an SearchFieldMetadata with index type AUTO. Use an IndexTypeRegistry instead.");
        Method member = TestClass.class.getMethod("stringMethod");

        new MethodSearchFieldMetadata(TestClass.class, member, IndexType.AUTO);
    }

    @Test
    public void constructWithNamedMember() throws Exception {
        Method member = TestClass.class.getMethod("namedMethod");

        SearchFieldMetadata searchFieldMetadata = new MethodSearchFieldMetadata(TestClass.class, member, IndexType.IDENTIFIER);

        assertThat(searchFieldMetadata.getEntityType()).isEqualTo(TestClass.class);
        assertThat(searchFieldMetadata.getMember()).isEqualTo(member);
        assertThat(searchFieldMetadata.getMemberType()).isEqualTo(String.class);
        assertThat(searchFieldMetadata.getMemberName()).isEqualTo(new MethodNameCalculator().apply(member));
        assertThat(searchFieldMetadata.getIndexName()).isEqualTo(new FieldNameEncoder().apply(searchFieldMetadata.getMemberName()));
        Assertions.assertThat(searchFieldMetadata.getIndexType()).isEqualTo(IndexType.IDENTIFIER);
        assertThat(searchFieldMetadata.getValue(new TestClass())).isEqualTo("namedMethodValue");
    }

    @Test
    public void constructWithTypedMember() throws Exception {
        Method member = TestClass.class.getMethod("typedMethod");

        SearchFieldMetadata searchFieldMetadata = new MethodSearchFieldMetadata(TestClass.class, member, indexTypeRegistry);

        assertThat(searchFieldMetadata.getEntityType()).isEqualTo(TestClass.class);
        assertThat(searchFieldMetadata.getMember()).isEqualTo(member);
        assertThat(searchFieldMetadata.getMemberType()).isEqualTo(String.class);
        assertThat(searchFieldMetadata.getMemberName()).isEqualTo(new MethodNameCalculator().apply(member));
        assertThat(searchFieldMetadata.getIndexName()).isEqualTo(new FieldNameEncoder().apply(searchFieldMetadata.getMemberName()));
        Assertions.assertThat(searchFieldMetadata.getIndexType()).isEqualTo(IndexType.GEOPOINT);
        assertThat(searchFieldMetadata.getValue(new TestClass())).isEqualTo("typedMethodValue");
    }

    private static class TestClass {
        @SearchIndex
        public String stringMethod() {
            return "stringValue";
        }

        public String unannotatedMethod() {
            return "unannotatedValue";
        }

        @SearchIndex(name = "myMethod")
        public String namedMethod() {
            return "namedMethodValue";
        }

        @SearchIndex(type = IndexType.GEOPOINT)
        public String typedMethod() {
            return "typedMethodValue";
        }
    }
}