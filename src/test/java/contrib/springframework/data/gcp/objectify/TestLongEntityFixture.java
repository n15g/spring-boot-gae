package contrib.springframework.data.gcp.objectify;

import java.util.function.Supplier;

public class TestLongEntityFixture implements Supplier<TestLongEntity> {

    @Override
    public TestLongEntity get() {
        return new TestLongEntity(null);
    }

    public TestLongEntity[] get(int count) {
        TestLongEntity[] result = new TestLongEntity[count];

        for (int i = 0; i < count; i++) {
            int num = i + 1;
            result[i] = new TestLongEntity(null)
                    .setName("entity" + num);
        }

        return result;
    }
}
