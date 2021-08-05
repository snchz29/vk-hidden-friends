package providers;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.var;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.util.ResourceUtils;
import ru.snchz29.models.Person;
import ru.snchz29.models.PersonMultimap;

public class PersonResponseJsonBuilderProvider implements ArgumentsProvider {
    private static final String JSON_SCHEMA_PATH = "classpath:samples/json-samples/person-response-schema.json";

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        var personToHiddenFriends = new PersonMultimap();
        personToHiddenFriends.putAll(
            new Person(619270, "Апоссум", "Raketa",
                "https://sun4-12.userapi.com/s/v1/ig2/E2x0pMfPf1yVli5ursHCMY31uN0j6VF0zUNlb4TKJs-4Cv40kn8Se0tcsFOubA2zi_0ygVk6aCgmsP17IsPkV1nI.jpg?size=200x0&quality=96&crop=0,210,719,719&ava=1",
                Collections
                    .emptyList()),
            Collections.singletonList(
                new Person(268865915, "Эрнест", "Раскин",
                    "https://sun4-15.userapi.com/s/v1/ig2/mPONcfJkpUyenNSzvlCFE0nlAA2SjZOtqz2q9FXCPgtR_ESF2dnFiezcKUdtJJrm6do_l0JPuafjo3P9mS4vHRCd.jpg?size=200x0&quality=96&crop=0,518,1620,1620&ava=1",
                    Collections.emptyList())
            )
        );
        personToHiddenFriends.putAll(
            new Person(2503204, "Sergey", "Stepanov",
                "https://sun4-10.userapi.com/s/v1/ig2/nxhpqWcnIi-LuYLS_LZRs7V_-3MOMimJhwcvEhAyMoFmmO31MmvaaAjPEGk9BoFmuB0SLB4gnozhRJBycn3fQYRD.jpg?size=200x0&quality=96&crop=0,537,591,591&ava=1",
                Collections.emptyList()),
            Collections.singletonList(
                new Person(103602, "Evgeny", "Belov",
                    "https://sun4-17.userapi.com/s/v1/if1/LHlKJ94vqH5sRHqVfC15PpLRXg8Drv2QexCOtlngav3eoyzzmh9_hbegIwOezh8j3b5ercOl.jpg?size=200x0&quality=96&crop=150,503,318,318&ava=1",
                    Collections.emptyList())
            )
        );
        var expectedJson = FileUtils.readFileToString(
            ResourceUtils.getFile(JSON_SCHEMA_PATH),
            StandardCharsets.UTF_8
        );
        return Stream.of(
            Arguments.of(true, personToHiddenFriends, expectedJson)
        );
    }
}
