package contrib.springframework.data.gcp.search.metadata.impl;

import contrib.springframework.data.gcp.search.IndexType;
import contrib.springframework.data.gcp.search.SearchIndex;
import contrib.springframework.data.gcp.search.metadata.Accessor;
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

public class MethodAccessorTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private IndexTypeRegistry indexTypeRegistry;


    @Test
    public void constructWithSpecificType() throws Exception {
        Method member = TestClass.class.getMethod("stringMethod");

        Accessor accessor = new MethodAccessor(TestClass.class, member, IndexType.IDENTIFIER);

        assertThat(accessor.getEntityType()).isEqualTo(TestClass.class);
        assertThat(accessor.getMember()).isEqualTo(member);
        assertThat(accessor.getMemberType()).isEqualTo(String.class);
        assertThat(accessor.getMemberName()).isEqualTo("stringMethod");
        assertThat(accessor.getIndexName()).isEqualTo(new MethodNameCalculator().apply(member));
        assertThat(accessor.getEncodedName()).isEqualTo(new FieldNameEncoder().apply(accessor.getMemberName()));
        Assertions.assertThat(accessor.getIndexType()).isEqualTo(IndexType.IDENTIFIER);
        assertThat(accessor.getValue(new TestClass())).isEqualTo("stringValue");
    }

    @Test
    public void constructWithTypeLookup() throws Exception {
        Method member = TestClass.class.getMethod("stringMethod");

        when(indexTypeRegistry.apply(String.class)).thenReturn(IndexType.TEXT);
        Accessor accessor = new MethodAccessor(TestClass.class, member, indexTypeRegistry);

        assertThat(accessor.getEntityType()).isEqualTo(TestClass.class);
        assertThat(accessor.getMember()).isEqualTo(member);
        assertThat(accessor.getMemberType()).isEqualTo(String.class);
        assertThat(accessor.getMemberName()).isEqualTo("stringMethod");
        assertThat(accessor.getIndexName()).isEqualTo(new MethodNameCalculator().apply(member));
        assertThat(accessor.getEncodedName()).isEqualTo(new FieldNameEncoder().apply(accessor.getMemberName()));
        Assertions.assertThat(accessor.getIndexType()).isEqualTo(IndexType.TEXT);
        assertThat(accessor.getValue(new TestClass())).isEqualTo("stringValue");
    }


    @Test
    public void constructWithUnannotatedMethod() throws Exception {
        Method member = TestClass.class.getMethod("unannotatedMethod");

        when(indexTypeRegistry.apply(String.class)).thenReturn(IndexType.TEXT);
        Accessor accessor = new MethodAccessor(TestClass.class, member, indexTypeRegistry);

        assertThat(accessor.getEntityType()).isEqualTo(TestClass.class);
        assertThat(accessor.getMember()).isEqualTo(member);
        assertThat(accessor.getMemberType()).isEqualTo(String.class);
        assertThat(accessor.getMemberName()).isEqualTo("unannotatedMethod");
        assertThat(accessor.getIndexName()).isEqualTo(new MethodNameCalculator().apply(member));
        assertThat(accessor.getEncodedName()).isEqualTo(new FieldNameEncoder().apply(accessor.getMemberName()));
        Assertions.assertThat(accessor.getIndexType()).isEqualTo(IndexType.TEXT);
        assertThat(accessor.getValue(new TestClass())).isEqualTo("unannotatedValue");
    }

    @Test
    public void constructWithAutoType() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Cannot construct an Accessor with index type AUTO. Use an IndexTypeRegistry instead.");
        Method member = TestClass.class.getMethod("stringMethod");

        new MethodAccessor(TestClass.class, member, IndexType.AUTO);
    }

    @Test
    public void constructWithNamedMember() throws Exception {
        Method member = TestClass.class.getMethod("namedMethod");

        Accessor accessor = new MethodAccessor(TestClass.class, member, IndexType.IDENTIFIER);

        assertThat(accessor.getEntityType()).isEqualTo(TestClass.class);
        assertThat(accessor.getMember()).isEqualTo(member);
        assertThat(accessor.getMemberType()).isEqualTo(String.class);
        assertThat(accessor.getMemberName()).isEqualTo(new MethodNameCalculator().apply(member));
        assertThat(accessor.getIndexName()).isEqualTo(new FieldNameEncoder().apply(accessor.getMemberName()));
        Assertions.assertThat(accessor.getIndexType()).isEqualTo(IndexType.IDENTIFIER);
        assertThat(accessor.getValue(new TestClass())).isEqualTo("namedMethodValue");
    }

    @Test
    public void constructWithTypedMember() throws Exception {
        Method member = TestClass.class.getMethod("typedMethod");

        Accessor accessor = new MethodAccessor(TestClass.class, member, indexTypeRegistry);

        assertThat(accessor.getEntityType()).isEqualTo(TestClass.class);
        assertThat(accessor.getMember()).isEqualTo(member);
        assertThat(accessor.getMemberType()).isEqualTo(String.class);
        assertThat(accessor.getMemberName()).isEqualTo(new MethodNameCalculator().apply(member));
        assertThat(accessor.getIndexName()).isEqualTo(new FieldNameEncoder().apply(accessor.getMemberName()));
        Assertions.assertThat(accessor.getIndexType()).isEqualTo(IndexType.GEOPOINT);
        assertThat(accessor.getValue(new TestClass())).isEqualTo("typedMethodValue");
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