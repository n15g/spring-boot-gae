package gae.spring.data.objectify.repository;

import org.junit.Test;
import gae.spring.data.objectify.TestLongEntity;

import java.util.Arrays;

@SuppressWarnings("ConstantConditions")
public abstract class LongSearchRepositoryTests extends LongRepositoryTests {

    @Override
    protected abstract SearchRepository<TestLongEntity, Long> getRepository();

    @Test
    @Override
    public void saveCollection_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().save(
                Arrays.asList(
                        new TestLongEntity(1L),
                        null,
                        new TestLongEntity(3L)
                )
        );
    }

    @Test
    @Override
    public void saveVarargs_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().save(
                new TestLongEntity(1L),
                null,
                new TestLongEntity(3L)
        );
    }

    @Test
    @Override
    public void saveAsyncCollection_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().saveAsync(
                Arrays.asList(
                        new TestLongEntity(1L),
                        null,
                        new TestLongEntity(3L)
                )
        ).get();
    }


    @Test
    @Override
    public void saveAsyncVarargs_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().saveAsync(
                new TestLongEntity(1L),
                null,
                new TestLongEntity(3L)
        ).get();
    }
}
