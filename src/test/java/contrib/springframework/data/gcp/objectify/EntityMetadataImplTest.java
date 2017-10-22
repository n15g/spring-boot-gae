package contrib.springframework.data.gcp.objectify;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class EntityMetadataImplTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ObjectifyProxy objectify;

    private EntityMetadataImpl entityMetadata;

    @Before
    public void setUp() throws Exception {
        entityMetadata = spy(new EntityMetadataImpl(objectify));
    }

    @Test
    public void getIdField() throws Exception {
        when(objectify.ofy().factory().getMetadata(TestStringEntity.class).getKeyMetadata().getIdFieldName()).thenReturn("id");

        assertThat(entityMetadata.getIdField(TestStringEntity.class)).isEqualTo(TestStringEntity.class.getDeclaredField("id"));
    }

    @Test
    public void getId_willReturnCachedResult_whenCalledMultipleTimes() throws Exception {
        doReturn(TestStringEntity.class.getDeclaredField("id")).when(entityMetadata).getIdFieldFromObjectify(TestStringEntity.class);

        entityMetadata.getIdField(TestStringEntity.class);
        entityMetadata.getIdField(TestStringEntity.class);
        entityMetadata.getIdField(TestStringEntity.class);

        verify(entityMetadata, times(1)).getIdFieldFromObjectify(TestStringEntity.class);
    }

    @Test
    public void getIdType() throws Exception {
        Mockito.<Class<?>>when(objectify.ofy().factory().getMetadata(TestStringEntity.class).getKeyMetadata().getIdFieldType()).thenReturn(String.class);

        assertThat(entityMetadata.getIdType(TestStringEntity.class)).isEqualTo(String.class);
    }

    @Test
    public void getIdType_willReturnCachedResult_whenCalledMultipleTimes() throws Exception {
        doReturn(String.class).when(entityMetadata).getIdTypeFromObjectify(TestStringEntity.class);

        entityMetadata.getIdType(TestStringEntity.class);
        entityMetadata.getIdType(TestStringEntity.class);
        entityMetadata.getIdType(TestStringEntity.class);

        verify(entityMetadata, times(1)).getIdTypeFromObjectify(TestStringEntity.class);
    }
}