package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import providers.PersonResponseJsonBuilderProvider;
import ru.snchz29.models.PersonMultimap;
import ru.snchz29.utils.PersonResponseBuilder;

public class PersonResponseJsonBuilderTest {

    @ParameterizedTest
    @ArgumentsSource(PersonResponseJsonBuilderProvider.class)
    public void outputJsonSchemaValidityTest(boolean isLoggedIn, PersonMultimap personToHiddenFriends,
                                             String correctJson) throws JsonProcessingException {
        String actualJson = PersonResponseBuilder
            .json()
            .addField("loggedIn", String.valueOf(isLoggedIn))
            .addField("result", personToHiddenFriends.asJson())
            .toString();
        System.out.println(actualJson);
        System.out.println(correctJson);
        ObjectMapper mapper = new ObjectMapper();
        assertEquals(mapper.readTree(correctJson), mapper.readTree(actualJson));
    }
}
