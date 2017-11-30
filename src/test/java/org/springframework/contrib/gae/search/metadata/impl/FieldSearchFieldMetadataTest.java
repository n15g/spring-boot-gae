package org.springframework.contrib.gae.search.metadata.impl;

import org.assertj.core.api.Assertions;
import org.springframework.contrib.gae.search.IndexType;
import org.springframework.contrib.gae.search.SearchIndex;
import org.springframework.contrib.gae.search.metadata.IndexTypeRegistry;
import org.springframework.contrib.gae.search.metadata.SearchFieldMetadata;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class FieldSearchFieldMetadataTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private IndexTypeRegistry indexTypeRegistry;


    @Test
    public void constructWithSpecificType() throws Exception {
        Field member = FieldSearchFieldMetadataTest.TestClass.class.getDeclaredField("stringField");

        SearchFieldMetadata searchFieldMetadata = new FieldSearchFieldMetadata(FieldSearchFieldMetadataTest.TestClass.class, member, IndexType.IDENTIFIER);

        assertThat(searchFieldMetadata.getEntityType()).isEqualTo(FieldSearchFieldMetadataTest.TestClass.class);
        assertThat(searchFieldMetadata.getMember()).isEqualTo(member);
        assertThat(searchFieldMetadata.getMemberType()).isEqualTo(String.class);
        assertThat(searchFieldMetadata.getMemberName()).isEqualTo("stringField");
        assertThat(searchFieldMetadata.getIndexName()).isEqualTo(new FieldNameLocator().apply(member));
        assertThat(searchFieldMetadata.getEncodedName()).isEqualTo(new FieldNameEncoder().apply(searchFieldMetadata.getMemberName()));
        Assertions.assertThat(searchFieldMetadata.getIndexType()).isEqualTo(IndexType.IDENTIFIER);
        assertThat(searchFieldMetadata.getValue(new FieldSearchFieldMetadataTest.TestClass())).isEqualTo("stringValue");
    }

    @Test
    public void constructWithTypeLookup() throws Exception {
        Field member = FieldSearchFieldMetadataTest.TestClass.class.getDeclaredField("stringField");

        when(indexTypeRegistry.apply(String.class)).thenReturn(IndexType.TEXT);
        SearchFieldMetadata searchFieldMetadata = new FieldSearchFieldMetadata(FieldSearchFieldMetadataTest.TestClass.class, member, indexTypeRegistry);

        assertThat(searchFieldMetadata.getEntityType()).isEqualTo(FieldSearchFieldMetadataTest.TestClass.class);
        assertThat(searchFieldMetadata.getMember()).isEqualTo(member);
        assertThat(searchFieldMetadata.getMemberType()).isEqualTo(String.class);
        assertThat(searchFieldMetadata.getMemberName()).isEqualTo("stringField");
        assertThat(searchFieldMetadata.getIndexName()).isEqualTo(new FieldNameLocator().apply(member));
        assertThat(searchFieldMetadata.getEncodedName()).isEqualTo(new FieldNameEncoder().apply(searchFieldMetadata.getMemberName()));
        Assertions.assertThat(searchFieldMetadata.getIndexType()).isEqualTo(IndexType.TEXT);
        assertThat(searchFieldMetadata.getValue(new FieldSearchFieldMetadataTest.TestClass())).isEqualTo("stringValue");
    }


    @Test
    public void constructWithUnannotatedField() throws Exception {
        Field member = FieldSearchFieldMetadataTest.TestClass.class.getDeclaredField("unannotatedField");

        when(indexTypeRegistry.apply(String.class)).thenReturn(IndexType.TEXT);
        SearchFieldMetadata searchFieldMetadata = new FieldSearchFieldMetadata(FieldSearchFieldMetadataTest.TestClass.class, member, indexTypeRegistry);

        assertThat(searchFieldMetadata.getEntityType()).isEqualTo(FieldSearchFieldMetadataTest.TestClass.class);
        assertThat(searchFieldMetadata.getMember()).isEqualTo(member);
        assertThat(searchFieldMetadata.getMemberType()).isEqualTo(String.class);
        assertThat(searchFieldMetadata.getMemberName()).isEqualTo("unannotatedField");
        assertThat(searchFieldMetadata.getIndexName()).isEqualTo(new FieldNameLocator().apply(member));
        assertThat(searchFieldMetadata.getEncodedName()).isEqualTo(new FieldNameEncoder().apply(searchFieldMetadata.getMemberName()));
        Assertions.assertThat(searchFieldMetadata.getIndexType()).isEqualTo(IndexType.TEXT);
        assertThat(searchFieldMetadata.getValue(new FieldSearchFieldMetadataTest.TestClass())).isEqualTo("unannotatedValue");
    }

    @Test
    public void constructWithAutoType() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Cannot construct an SearchFieldMetadata with index type AUTO. Use an IndexTypeRegistry instead.");
        Field member = FieldSearchFieldMetadataTest.TestClass.class.getDeclaredField("stringField");

        new FieldSearchFieldMetadata(FieldSearchFieldMetadataTest.TestClass.class, member, IndexType.AUTO);
    }

    @Test
    public void constructWithNamedMember() throws Exception {
        Field member = FieldSearchFieldMetadataTest.TestClass.class.getDeclaredField("namedField");

        SearchFieldMetadata searchFieldMetadata = new FieldSearchFieldMetadata(FieldSearchFieldMetadataTest.TestClass.class, member, IndexType.IDENTIFIER);

        assertThat(searchFieldMetadata.getEntityType()).isEqualTo(FieldSearchFieldMetadataTest.TestClass.class);
        assertThat(searchFieldMetadata.getMember()).isEqualTo(member);
        assertThat(searchFieldMetadata.getMemberType()).isEqualTo(String.class);
        assertThat(searchFieldMetadata.getMemberName()).isEqualTo(new FieldNameLocator().apply(member));
        assertThat(searchFieldMetadata.getIndexName()).isEqualTo(new FieldNameEncoder().apply(searchFieldMetadata.getMemberName()));
        Assertions.assertThat(searchFieldMetadata.getIndexType()).isEqualTo(IndexType.IDENTIFIER);
        assertThat(searchFieldMetadata.getValue(new FieldSearchFieldMetadataTest.TestClass())).isEqualTo("namedFieldValue");
    }

    @Test
    public void constructWithTypedMember() throws Exception {
        Field member = FieldSearchFieldMetadataTest.TestClass.class.getDeclaredField("typedField");

        SearchFieldMetadata searchFieldMetadata = new FieldSearchFieldMetadata(FieldSearchFieldMetadataTest.TestClass.class, member, indexTypeRegistry);

        assertThat(searchFieldMetadata.getEntityType()).isEqualTo(FieldSearchFieldMetadataTest.TestClass.class);
        assertThat(searchFieldMetadata.getMember()).isEqualTo(member);
        assertThat(searchFieldMetadata.getMemberType()).isEqualTo(String.class);
        assertThat(searchFieldMetadata.getMemberName()).isEqualTo(new FieldNameLocator().apply(member));
        assertThat(searchFieldMetadata.getIndexName()).isEqualTo(new FieldNameEncoder().apply(searchFieldMetadata.getMemberName()));
        Assertions.assertThat(searchFieldMetadata.getIndexType()).isEqualTo(IndexType.GEOPOINT);
        assertThat(searchFieldMetadata.getValue(new FieldSearchFieldMetadataTest.TestClass())).isEqualTo("typedFieldValue");
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
