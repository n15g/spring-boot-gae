package contrib.springframework.data.gcp.objectify;

import java.util.function.Supplier;

public class TestStringEntityFixture implements Supplier<TestStringEntity> {

    @Override
    public TestStringEntity get() {
        return new TestStringEntity("id");
    }

    public TestStringEntity[] get(int count) {
        TestStringEntity[] result = new TestStringEntity[count];

        for (int i = 0; i < count; i++) {
            int num = i + 1;
            result[i] = new TestStringEntity("id" + num)
                    .setName("entity" + num);
        }

        return result;
    }
}
