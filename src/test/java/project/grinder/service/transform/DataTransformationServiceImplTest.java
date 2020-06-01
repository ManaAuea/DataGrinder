package project.grinder.service.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static java.util.Collections.singletonMap;
import static java.util.Arrays.asList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import project.grinder.model.Order;
import project.grinder.model.Record;
import project.grinder.model.Summary;
import project.grinder.model.User;

@SpringBootTest
public class DataTransformationServiceImplTest {

    private DataTransformationService service;

    @BeforeEach
    public void setUp() {
        service = new DataTransformationServiceImpl();
    }

    @ParameterizedTest
    @MethodSource("flattenJsonTestData")
    @DisplayName("Should flatten json correctly")
    void flattenJsonTest(Map<String, Object> expect, Map<String, Object> json) {
        assertEquals(expect, service.flattenJson(json));
    }

    static Stream<Arguments> flattenJsonTestData() {
        return Stream.of(
            // Single member json with different type cases
            Arguments.of(singletonMap("null", null), singletonMap("null", null)),
            Arguments.of(singletonMap("boolean", true), singletonMap("boolean", true)),
            Arguments.of(singletonMap("int", 1), singletonMap("int", 1)),
            Arguments.of(singletonMap("double", 1.2), singletonMap("double", 1.2)),
            Arguments.of(singletonMap("string", "string"), singletonMap("string", "string")),

            // Json with multiple level depth
            Arguments.of(
                createMap(new String[][] { { "A_B", "C" }, { "A_D", "E" } }),
                singletonMap("A", createMap(new String[][] { { "B", "C" }, { "D", "E" } }))
            ),
            Arguments.of(singletonMap("A_B_C", "D"), singletonMap("A", singletonMap("B", singletonMap("C", "D")))),

            // Json with list and multiple level depth
            Arguments.of(
                createMap(new String[][] { { "A_0", "B" }, { "A_1", "C" } }),
                singletonMap("A", asList("B", "C"))
            ),
            Arguments.of(
                createMap(new String[][] { { "A_0_B", "C" }, { "A_1_D", "E" } }),
                singletonMap("A", asList(singletonMap("B", "C"), singletonMap("D", "E")))
            ),
            Arguments.of(
                createMap(new String[][] { { "A_B_0_C", "D" }, { "A_B_1_E", "F" }, { "A_B_1_G", "H" } }),
                singletonMap(
                    "A",
                    singletonMap(
                        "B",
                        Arrays.asList(
                            singletonMap("C", "D"),
                            createMap(new String[][] { { "E", "F" }, { "G", "H" } })
                        )
                    )
                )
            )
        );
    }

    @ParameterizedTest
    @MethodSource("summarizeUserTestData")
    @DisplayName("Should merge user data from multiple records to unique user")
    void summarizeUserTest(List<User> expect, Map<String, List<Record>> records) {
        Map<Integer, Summary> summary = service.summarizeRecord(records);

        expect.forEach(user -> {
            Summary sum = summary.get(user.getId());
            assertNotNull(sum);
            assertEquals(user.getId(), sum.getInfo().getId());
            assertEquals(user.getName(), sum.getInfo().getName());
            assertEquals(user.getPhone(), sum.getInfo().getPhone());
        });
    }

    static Stream<Arguments> summarizeUserTestData() {
        User userA = new User(1, "userA", "0123456789");
        User userB = new User(2, "userB", "9876543210");
        Record recordA = new Record(1, "userA", "0123456789");
        Record recordB = new Record(2, "userB", "9876543210");
        return Stream.of(
            Arguments.of(asList(userA), singletonMap("12-01-2020", asList(recordA))),
            Arguments.of(asList(userA, userB), singletonMap("12-01-2020", asList(recordA, recordB, recordA))),
            Arguments.of(
                asList(userA, userB),
                Stream.of(
                    createTestRecordMapEntry("12-01-2020",asList(recordA, recordB, recordA)),
                    createTestRecordMapEntry("12-02-2020",asList(recordB, recordA, recordB))
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
            )
        );
    }

    @ParameterizedTest
    @MethodSource("summarizeOrderTestData")
    @DisplayName("Should map order data from multiple records for each user")
    void summarizeOrderTest(Map<Map<Integer, String>, Order> expect, Map<String, List<Record>> records) {
        Map<Integer, Summary> summary = service.summarizeRecord(records);

        expect.forEach((identity, expectOrder) -> {
            int id = identity.keySet().stream().findFirst().get();
            Summary sum = summary.get(id);
            assertNotNull(sum);

            LocalDate date = LocalDate.parse(identity.get(id), DateTimeFormatter.ofPattern("MM-dd-yyyy"));
            List<Order> sumOrders = sum.getOrder().get(date);
            assertNotNull(sumOrders);

            Order order = sumOrders.stream().filter(o -> {
                return o.getItem().equalsIgnoreCase(expectOrder.getItem()) 
                        && o.getAmount() == expectOrder.getAmount()
                        && o.getPrice() == expectOrder.getPrice();
                }).findFirst().orElse(null);
            assertNotNull(order);
        });
    }

    static Stream<Arguments> summarizeOrderTestData() {
        int USER_A_ID = 1;
        int USER_B_ID = 2;
        Order orderA1 = new Order("itemA1", 2, 100.50);
        Order orderA2 = new Order("itemA2", 4, 250.36);
        Order orderB1 = new Order("itemB1", 3, 200.30);
        Order orderB2 = new Order("itemB2", 6, 950.85);
        Record recordA1 = new Record("itemA1", 2, 100.50, USER_A_ID, "userA", "0123456789");
        Record recordA2 = new Record("itemA2", 4, 250.36, USER_A_ID, "userA", "0123456789");
        Record recordB1 = new Record("itemB1", 3, 200.30, USER_B_ID, "userB", "9876543210");
        Record recordB2 = new Record("itemB2", 6, 950.85, USER_B_ID, "userB", "9876543210");

        return Stream.of(
            Arguments.of(
                singletonMap(singletonMap(USER_A_ID, "12-01-2020"), orderA1),
                singletonMap("12-01-2020", asList(recordA1))
            ),
            Arguments.of(
                Stream.of(
                    createExpectedOrderMapEntry(singletonMap(USER_A_ID, "12-02-2020"), orderA2),
                    createExpectedOrderMapEntry(singletonMap(USER_B_ID, "12-02-2020"), orderB2)
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)),
                Stream.of(
                    createTestRecordMapEntry("12-01-2020", asList()),
                    createTestRecordMapEntry("12-02-2020", asList(recordA2, recordB2))
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
            ),
            Arguments.of(
                Stream.of(
                    createExpectedOrderMapEntry(singletonMap(USER_A_ID, "12-01-2020"), orderA1),
                    createExpectedOrderMapEntry(singletonMap(USER_A_ID, "12-02-2020"), orderA2),
                    createExpectedOrderMapEntry(singletonMap(USER_B_ID, "12-01-2020"), orderB1),
                    createExpectedOrderMapEntry(singletonMap(USER_B_ID, "12-02-2020"), orderB2)
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)),
                Stream.of(
                    createTestRecordMapEntry("12-01-2020", asList(recordA1, recordB1)),
                    createTestRecordMapEntry("12-02-2020",asList(recordA2, recordB2))
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
            )
        );
    }

    @ParameterizedTest
    @MethodSource("summarizeTotalAmountTestData")
    @DisplayName("Should summarize total amount from multiple records for each user")
    void summarizeTotalAmountTest(Map<Integer, Double> expect, Map<String, List<Record>> records) {
        Map<Integer, Summary> summary = service.summarizeRecord(records);

        expect.forEach((id, total) -> {
            Summary sum = summary.get(id);
            assertNotNull(sum);
            assertEquals(total, sum.getTotalAmount());
        });
    }

    static Stream<Arguments> summarizeTotalAmountTestData() {
        int USER_A_ID = 1;
        int USER_B_ID = 2;
        Record recordA1 = new Record("itemA1", 2, 100.50, USER_A_ID, "userA", "0123456789");
        Record recordA2 = new Record("itemA2", 4, 250.36, USER_A_ID, "userA", "0123456789");
        Record recordB1 = new Record("itemB1", 3, 200.21, USER_B_ID, "userB", "9876543210");
        Record recordB2 = new Record("itemB2", 6, 950.15, USER_B_ID, "userB", "9876543210");
        return Stream.of(
            Arguments.of(singletonMap(USER_A_ID, 201.0), singletonMap("12-01-2020", asList(recordA1))),
            Arguments.of(singletonMap(USER_A_ID, 1202.44), singletonMap("12-01-2020", asList(recordA1, recordA2))),
            Arguments.of(
                createMap(new Object[][] { { USER_A_ID, 201.0 }, { USER_B_ID, 600.63 } }),
                singletonMap("12-01-2020", asList(recordA1, recordB1))
            ),
            Arguments.of(
                createMap(new Object[][] { { USER_A_ID, 1202.44 }, { USER_B_ID, 6301.53 } }),
                Stream.of(
                    createTestRecordMapEntry("12-01-2020", asList(recordA1, recordB1)),
                    createTestRecordMapEntry("12-02-2020",asList(recordA2, recordB2))
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
            )
        );
    }

    private static Map<Object, Object> createMap(Object[][] members) {
        return Stream.of(members).collect(Collectors.toMap(d -> d[0], d -> d[1]));
    }

    private static SimpleImmutableEntry<Map<Integer, String>, Order> createExpectedOrderMapEntry(Map<Integer, String> identity, Order order) {
        return new AbstractMap.SimpleImmutableEntry<Map<Integer, String>, Order>(identity, order);
    }

    private static SimpleImmutableEntry<String, List<Record>> createTestRecordMapEntry(String date, List<Record> records) {
        return new AbstractMap.SimpleImmutableEntry<String, List<Record>>(date, records);
    }
}