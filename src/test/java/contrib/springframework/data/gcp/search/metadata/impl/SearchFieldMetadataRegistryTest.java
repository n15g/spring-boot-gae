package contrib.springframework.data.gcp.search.metadata.impl;

import contrib.springframework.data.gcp.search.SearchId;
import contrib.springframework.data.gcp.search.SearchIndex;
import contrib.springframework.data.gcp.search.metadata.IndexTypeRegistry;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchFieldMetadataRegistryTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private IndexTypeRegistry indexTypeRegistry;

    private SearchFieldMetadataRegistry registry;

    @Before
    public void setUp() throws Exception {
        registry = new SearchFieldMetadataRegistryImpl(indexTypeRegistry);
    }

    @Test
    public void register() throws Exception {
        registry.register(TestEntity.class);

        assertThat(registry.get(TestEntity.class, "indexedField").getMemberName()).isEqualTo("indexedField");
    }

    @Test
    public void get_willRegisterAutomatically_whenClassIsUnregistered() throws Exception {
        assertThat(registry.get(TestEntity.class, "indexedMethod").getMemberName()).isEqualTo("indexedMethod");
    }

    @Test
    public void get_willThrowException_whenFieldIsNotIndexed() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'unindexedField' is not an indexed member on entity class: " + TestEntity.class);
        registry.get(TestEntity.class, "unindexedField");
    }

    @Test
    public void get_willThrowException_whenMethodIsNotIndexed() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'unindexedMethod' is not an indexed member on entity class: " + TestEntity.class);
        registry.get(TestEntity.class, "unindexedMethod");
    }

    @Test
    public void get_willThrowException_whenMemberDoesNotExist() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'nonexistentMember' is not an indexed member on entity class: " + TestEntity.class);
        registry.get(TestEntity.class, "nonexistentMember");
    }

    @Test
    public void get_willFailGracefully_whenNotAnEntity() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'some-field' is not an indexed member on entity class: " + String.class);
        registry.get(String.class, "some-field");
    }

    @Test
    public void getByEncodedName() {
        registry.register(TestEntity.class);
        assertThat(registry.getByEncodedName(TestEntity.class, "indexedField").getMemberName())
                .isEqualTo("indexedField");
    }

    @Test
    public void getByEncodedName_willWork_whenTargetIsMethod() {
        registry.register(TestEntity.class);
        assertThat(registry.getByEncodedName(TestEntity.class, "indexedMethod").getMemberName())
                .isEqualTo("indexedMethod");
    }

    @Test
    public void getByEncodedName_willRegisterAutomatically_whenClassIsUnregistered() {
        assertThat(registry.getByEncodedName(TestEntity.class, "indexedField").getMemberName())
                .isEqualTo("indexedField");
    }

    @Test
    public void getByEncodedName_willWork_whenFieldNameRequiresEncoding() {
        assertThat(registry.getByEncodedName(TestEntity.class, "fieldNameRequiringEncoding").getMemberName())
                .isEqualTo("_fieldNameRequiringEncoding");
    }

    @Test
    public void getByEncodedName_willWork_whenExplicitFieldNameRequiresEncoding() {
        assertThat(registry.getByEncodedName(TestEntity.class, "explicitly_named_field_requiring_encoding").getMemberName())
                .isEqualTo("explicitlyNamedFieldRequiringEncoding");
    }

    @Test
    public void getByEncodedName_willWork_whenMethodNameRequiresEncoding() {
        assertThat(registry.getByEncodedName(TestEntity.class, "methodNameRequiringEncoding").getMemberName())
                .isEqualTo("_methodNameRequiringEncoding");
    }

    @Test
    public void getByEncodedName_willWork_whenExplicitMethodNameRequiresEncoding() {
        assertThat(registry.getByEncodedName(TestEntity.class, "explicitly_named_method_requiring_encoding").getMemberName())
                .isEqualTo("explicitlyNamedMethodRequiringEncoding");
    }

    @Test
    public void getByEncodedName_willThrowException_whenFieldIsNotIndexed() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Encoded name 'unindexedField' is not an indexed member on entity class: " + TestEntity.class);
        registry.getByEncodedName(TestEntity.class, "unindexedField");
    }

    @Test
    public void getByEncodedName_willThrowException_whenMethodIsNotIndexed() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Encoded name 'unindexedMethod' is not an indexed member on entity class: " + TestEntity.class);
        registry.getByEncodedName(TestEntity.class, "unindexedMethod");
    }

    @Test
    public void getByEncodedName_willThrowException_whenMemberDoesNotExist() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Encoded name 'some_unknown_encoded_member_name' is not an indexed member on entity class: " + TestEntity.class);
        registry.getByEncodedName(TestEntity.class, "some_unknown_encoded_member_name");
    }

    @Test
    public void getByEncodedName_willFailGracefully_whenNotAnEntity() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Encoded name 'someMethod' is not an indexed member on entity class: " + String.class);
        registry.getByEncodedName(String.class, "someMethod");
    }

    @Test
    public void getAll() throws Exception {
        assertThat(registry.get(TestEntity.class))
                .containsKeys("id", "indexedField", "indexedMethod")
                .doesNotContainKeys("unindexedField", "unindexedMethod");
    }

    @Test
    public void getByEncodedName_willReturnNothing_whenNotAnEntity() {
        assertThat(registry.get(String.class)).isEmpty();
    }

    @Test
    public void getIdField() throws Exception {
        registry.register(TestEntity.class);
        assertThat(registry.getIdField(TestEntity.class).getMemberName()).isEqualTo("id");
    }

    @Test
    public void getIdField_willRegisterAutomatically_whenClassIsUnregistered() throws Exception {
        assertThat(registry.getIdField(TestEntity.class).getMemberName()).isEqualTo("id");
    }

    @Test
    public void getIdField_willThrowException_whenThereIsNoId() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("No @SearchId on entity class: " + TestNonEntity.class);
        assertThat(registry.getIdField(TestNonEntity.class));
    }

    @Test
    public void getIdField_willWork_whenIdIsAMethod() throws Exception {
        assertThat(registry.getIdField(TestIdMethodEntity.class).getMemberName()).isEqualTo("id");
    }

    @Test
    public void getIdField_willFailGracefully_whenNotAnEntity() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("No @SearchId on entity class: " + String.class);
        registry.getIdField(String.class);
    }

    @SuppressWarnings("unused")
    private class TestEntity {
        @SearchId
        private String id;

        @SearchIndex
        private String indexedField;
        private String unindexedField;

        @SearchIndex
        private String _fieldNameRequiringEncoding;
        @SearchIndex("explicitly-named-field-requiring-encoding")
        private String explicitlyNamedFieldRequiringEncoding;

        @SearchIndex
        public String indexedMethod() {
            return "";
        }

        public String unindexedMethod() {
            return "";
        }

        @SearchIndex
        public String _methodNameRequiringEncoding() {
            return "";
        }

        @SearchIndex("explicitly-named-method-requiring-encoding")
        public String explicitlyNamedMethodRequiringEncoding() {
            return "";
        }
    }

    private class TestIdMethodEntity {
        @SearchId
        public String id() {
            return "";
        }
    }

    private class TestNonEntity {
    }
}