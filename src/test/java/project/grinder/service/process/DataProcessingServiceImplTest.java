package project.grinder.service.process;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import project.grinder.model.User;
import project.grinder.model.Validation;

@SpringBootTest
public class DataProcessingServiceImplTest {

    private DataProcessingService service;

    @BeforeEach
    public void setUp() {
        service = new DataProcessingServiceImpl();
    }

    @ParameterizedTest
    @MethodSource("sortIntegerTestData")
    @DisplayName("Should sort integer correctly")
    void sortIntegerTest(List<Integer> expect, List<Integer> data) {
        assertEquals(expect, service.sortInteger(data));
    }

    static Stream<Arguments> sortIntegerTestData() {
        return Stream.of(
            Arguments.of(Arrays.asList(), Arrays.asList()),
            Arguments.of(Arrays.asList(1), Arrays.asList(1)),
            Arguments.of(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3)),
            Arguments.of(Arrays.asList(1, 2, 3), Arrays.asList(3, 1, 2)),
            Arguments.of(Arrays.asList(2, 2, 2), Arrays.asList(2, 2, 2))
        );
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, Integer.MAX_VALUE })
    @DisplayName("Should return valid object when pass positive id")
    void validIdTest(int id) {
        Validation validated = service.validateUser(new User(id, "Valid Name", "0123456789"));

        assertTrue(validated.getValid());
        assertEquals(validated.getErrors().size(), 0);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, -1, Integer.MIN_VALUE })
    @DisplayName("Should return invalid object with correct suggestion when pass negative id")
    void invalidIdTest(int id) {
        Validation validated = service.validateUser(new User(id, "Valid Name", "0123456789"));

        assertFalse(validated.getValid());
        assertEquals(validated.getErrors().size(), 1);
        assertEquals(validated.getSuggestion().getId(), Math.abs(id));
    }

    @ParameterizedTest
    @ValueSource(strings = { "Valid Name", "Va Na", "V N" })
    @DisplayName("Should return valid object when pass correct name")
    void validNameTest(String name) {
        Validation validated = service.validateUser(new User(1, name, "0123456789"));

        assertTrue(validated.getValid());
        assertEquals(validated.getErrors().size(), 0);
    }

    @ParameterizedTest
    @MethodSource("invalidNameTestData")
    @DisplayName("Should return invalid object when pass incorrect name")
    void invalidNameTest(String expect, String name) {
        Validation validated = service.validateUser(new User(1, name, "0123456789"));

        assertFalse(validated.getValid());
        assertEquals(validated.getErrors().size(), 1);
        assertEquals(validated.getSuggestion().getName(), expect);
    }

    static Stream<Arguments> invalidNameTestData() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of("0", "0"),
            Arguments.of("", ""),
            Arguments.of(" ", " "),
            Arguments.of("Invalid Name", "invalid name"),
            Arguments.of("Invalid Name", "INVALID NAME"),
            Arguments.of("Invalid Name", "iNVALID nAME"),
            Arguments.of("Valid Name", "Mr. Valid Name"),
            Arguments.of("Valid Name", "Valid Name (rob)"),
            Arguments.of("Valid Name", " Valid Name ")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = { "0123456789" })
    @DisplayName("Should return valid object when pass correct phone number")
    void validPhoneTest(String phone) {
        Validation validated = service.validateUser(new User(1, "Valid Name", phone));

        assertTrue(validated.getValid());
        assertEquals(validated.getErrors().size(), 0);
    }

    @ParameterizedTest
    @MethodSource("invalidPhoneTestData")
    @DisplayName("Should return invalid object when pass incorrect phone number")
    void invalidPhoneTest(String expect, String phone) {
        Validation validated = service.validateUser(new User(1, "Valid Name", phone));

        assertFalse(validated.getValid());
        assertEquals(validated.getErrors().size(), 1);
        assertEquals(validated.getSuggestion().getPhone(), expect);
    }

    static Stream<Arguments> invalidPhoneTestData() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of("", ""),
            Arguments.of(" ", " "),
            Arguments.of("0123456789", "01 234 56789"),
            Arguments.of("0123456789", "01-234-56789"),
            Arguments.of("0123456789", "Tel. 0123456789"),
            Arguments.of("0123456789", "0123456789 (susan)"),
            Arguments.of("0123456789", " 0123456789 ")
        );
    }
}