package gae.spring.data.objectify.repository;

import org.junit.Test;
import gae.spring.data.objectify.TestStringEntity;

import java.util.Arrays;

@SuppressWarnings("ConstantConditions")
public abstract class StringSearchRepositoryTests extends StringRepositoryTests {

    @Override
    protected abstract SearchRepository<TestStringEntity, String> getRepository();

    @Test
    @Override
    public void saveCollection_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().save(
                Arrays.asList(
                        new TestStringEntity("id1"),
                        null,
                        new TestStringEntity("id3")
                )
        );
    }


    @Test
    @Override
    public void saveVarargs_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().save(
                new TestStringEntity("id1"),
                null,
                new TestStringEntity("id3")
        );
    }

    @Test
    @Override
    public void saveAsyncCollection_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().saveAsync(
                Arrays.asList(
                        new TestStringEntity("id1"),
                        null,
                        new TestStringEntity("id3")
                )
        ).get();
    }

    @Test
    @Override
    public void saveAsyncVarargs_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().saveAsync(
                new TestStringEntity("id1"),
                null,
                new TestStringEntity("id3")
        ).get();
    }
}
