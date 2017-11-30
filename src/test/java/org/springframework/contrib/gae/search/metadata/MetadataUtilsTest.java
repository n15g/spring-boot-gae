package org.springframework.contrib.gae.search.metadata;

import org.springframework.contrib.gae.search.metadata.impl.MetadataUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class MetadataUtilsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private SoftAssertions softly = new SoftAssertions();

    @Test
    @SuppressWarnings("InstantiatingObjectToGetClassObject")
    public void getRawType() throws Exception {
        softly.assertThat(MetadataUtils.getRawType(String.class)).isEqualTo(String.class);
        softly.assertThat(MetadataUtils.getRawType(List.class)).isEqualTo(List.class);
        softly.assertThat(MetadataUtils.getRawType(Map.Entry.class)).isEqualTo(Map.Entry.class);

        softly.assertThat(MetadataUtils.getRawType(new ArrayList<String>().getClass())).isEqualTo(ArrayList.class);
        softly.assertThat(MetadataUtils.getRawType(new ArrayList<ArrayList<Integer>>().getClass())).isEqualTo(ArrayList.class);

        softly.assertAll();
    }

    @Test
    @SuppressWarnings("InstantiatingObjectToGetClassObject")
    public void isCollectionType() throws Exception {
        softly.assertThat(MetadataUtils.isCollectionType(String[].class)).isTrue();
        softly.assertThat(MetadataUtils.isCollectionType(Integer[].class)).isTrue();
        softly.assertThat(MetadataUtils.isCollectionType(int[].class)).isTrue();
        softly.assertThat(MetadataUtils.isCollectionType(Object[].class)).isTrue();
        softly.assertThat(MetadataUtils.isCollectionType(new String[]{}.getClass())).isTrue();


        softly.assertThat(MetadataUtils.isCollectionType(List.class)).isTrue();
        softly.assertThat(MetadataUtils.isCollectionType(Collection.class)).isTrue();
        softly.assertThat(MetadataUtils.isCollectionType(Set.class)).isTrue();
        softly.assertThat(MetadataUtils.isCollectionType(new HashSet<String>().getClass())).isTrue();
        softly.assertThat(MetadataUtils.isCollectionType(new ArrayList<String>().getClass())).isTrue();
        softly.assertThat(MetadataUtils.isCollectionType(new ArrayList<ArrayList<String>>().getClass())).isTrue();

        softly.assertThat(MetadataUtils.isCollectionType(String.class)).isFalse();
        softly.assertThat(MetadataUtils.isCollectionType(Integer.class)).isFalse();
        softly.assertThat(MetadataUtils.isCollectionType(MetadataUtils.class)).isFalse();
        softly.assertThat(MetadataUtils.isCollectionType("".getClass())).isFalse();

        softly.assertAll();
    }

    @Test
    @SuppressWarnings({"InstantiatingObjectToGetClassObject", "MismatchedQueryAndUpdateOfCollection"})
    public void getCollectionType() throws Exception {
        softly.assertThat(MetadataUtils.getCollectionType(String[].class)).isEqualTo(String.class);
        softly.assertThat(MetadataUtils.getCollectionType(Integer[].class)).isEqualTo(Integer.class);
        softly.assertThat(MetadataUtils.getCollectionType(int[].class)).isEqualTo(int.class);
        softly.assertThat(MetadataUtils.getCollectionType(Object[].class)).isEqualTo(Object.class);
        softly.assertThat(MetadataUtils.getCollectionType(new String[]{}.getClass())).isEqualTo(String.class);

        softly.assertAll();
    }

    @Test
    public void getCollectionTypeStringList() throws Exception {
        Field field = TestClass.class.getDeclaredField("stringList");
        assertThat(MetadataUtils.getCollectionType(field.getGenericType())).isEqualTo(String.class);
    }

    @Test
    public void getCollectionTypeIntegerSet() throws Exception {
        Field field = TestClass.class.getDeclaredField("integerSet");
        assertThat(MetadataUtils.getCollectionType(field.getGenericType())).isEqualTo(Integer.class);
    }

    @Test
    public void getCollectionTypeListSet() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Unsupported type: java.util.List<java.util.Set<java.lang.String>>");
        Field field = TestClass.class.getDeclaredField("listSet");
        assertThat(MetadataUtils.getCollectionType(field.getGenericType())).isEqualTo(Set.class);
    }

    @Test
    public void getCollectionTypeStringArray() throws Exception {
        Field field = TestClass.class.getDeclaredField("stringArray");
        assertThat(MetadataUtils.getCollectionType(field.getGenericType())).isEqualTo(String.class);
    }

    @Test
    public void getCollectionType_willThrownException_whenNonParameterized() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Cannot infer index type for non-parameterized type: interface java.util.List");
        assertThat(MetadataUtils.getCollectionType(List.class)).isEqualTo(String.class);
    }

    @Test
    public void getCollectionType_willThrownException_whenNonCollectionType() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Unsupported type: class java.lang.String");
        assertThat(MetadataUtils.getCollectionType(String.class)).isEqualTo(String.class);
    }

    private class TestClass {
        List<String> stringList;
        Set<Integer> integerSet;
        List<Set<String>> listSet;

        String[] stringArray;
    }
}