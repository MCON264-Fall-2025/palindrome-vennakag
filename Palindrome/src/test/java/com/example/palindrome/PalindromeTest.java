package com.example.palindrome;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class PalindromeTest {

    /*
     * Functional interface shared by both solutions.
     * Students do NOT have to implement this, they just need to know:
     * - check(String s) should return true/false if s is a palindrome
     */
    @FunctionalInterface
    interface PalindromeStrategy {
        boolean check(String s);
    }

    /**
     * Simple holder for an implementation.
     * (We are not using Java 'record' here.)
     */
    static class StrategyCase {
        private final String name;
        private final PalindromeStrategy strategy;

        StrategyCase(String name, PalindromeStrategy strategy) {
            this.name = name;
            this.strategy = strategy;
        }

        String getName() {
            return name;
        }

        PalindromeStrategy getStrategy() {
            return strategy;
        }
    }

    /**
     * Returns both implementations we want to test.
     */
    private static List<StrategyCase> getStrategies() {
        List<StrategyCase> list = new ArrayList<>();
        list.add(new StrategyCase("StackQueue", PalindromeWithStackQueue::isPalindrome));
        list.add(new StrategyCase("TwoPointers", PalindromeWithTwoPointers::isPalindrome));
        return list;
    }

    // ---------- Helper MethodSources ----------

    /**
     * Base palindrome cases with expected answers.
     */
    private static Object[][] baseCases() {
        return new Object[][]{
                { "", true },
                { "   \t\n ", true },
                { "x", true },
                { "abba", true },
                { "racecar", true },
                { "A man, a plan, a canal: Panama", true },
                { "Madam, I'm Adam.", true },
                { "hello", false },
                { "neuroscribe", false }
        };
    }

    /**
     * MethodSource for general cases:
     * Each Arguments: (input, expected, implName, strategy)
     *
     * We manually build a List<Arguments> (instead of Streams).
     */
    static List<Arguments> palindromeCasesAllStrategies() {
        List<Arguments> all = new ArrayList<>();
        Object[][] base = baseCases();
        List<StrategyCase> impls = getStrategies();

        for (int i = 0; i < base.length; i++) {
            String input = (String) base[i][0];
            boolean expected = (Boolean) base[i][1];
            for (StrategyCase sc : impls) {
                all.add(Arguments.of(input, expected, sc.getName(), sc.getStrategy()));
            }
        }
        return all;
    }

    /**
     * For null handling specifically.
     * Each Arguments: (implName, strategy)
     */
    static List<Arguments> nullStrategySource() {
        List<Arguments> all = new ArrayList<>();
        for (StrategyCase sc : getStrategies()) {
            all.add(Arguments.of(sc.getName(), sc.getStrategy()));
        }
        return all;
    }

    /**
     * Known palindromes only, similar to your old @ValueSource usage.
     * Each argument: (candidate, implName, strategy)
     */
    static List<Arguments> knownPalindromesAllStrategies() {
        String[] pals = {
                "abba",
                "racecar",
                "A man, a plan, a canal: Panama",
                "Madam"
        };

        List<Arguments> all = new ArrayList<>();
        List<StrategyCase> impls = getStrategies();

        for (String p : pals) {
            for (StrategyCase sc : impls) {
                all.add(Arguments.of(p, sc.getName(), sc.getStrategy()));
            }
        }
        return all;
    }

    /**
     * Csv-like pairs with expected result.
     * Each Arguments: (input, expected, implName, strategy)
     */
    static List<Arguments> csvLikeCasesAllStrategies() {
        Object[][] rows = new Object[][]{
                { "hello", false },
                { "Was it a car or a cat I saw", true },
                { "No lemon, no melon", true },
                { "notpal", false }
        };

        List<Arguments> all = new ArrayList<>();
        List<StrategyCase> impls = getStrategies();

        for (int i = 0; i < rows.length; i++) {
            String input = (String) rows[i][0];
            boolean expected = (Boolean) rows[i][1];
            for (StrategyCase sc : impls) {
                all.add(Arguments.of(input, expected, sc.getName(), sc.getStrategy()));
            }
        }
        return all;
    }

    /**
     * Matches your old methodSourceProvider.
     * Each Arguments: (input, expected, implName, strategy)
     */
    static List<Arguments> methodSourceCasesAllStrategies() {
        Object[][] rows = new Object[][]{
                { "x", true },
                { "xyx", true },
                { "xy", false },
                { "Never odd or even", true }
        };

        List<Arguments> all = new ArrayList<>();
        List<StrategyCase> impls = getStrategies();

        for (int i = 0; i < rows.length; i++) {
            String input = (String) rows[i][0];
            boolean expected = (Boolean) rows[i][1];
            for (StrategyCase sc : impls) {
                all.add(Arguments.of(input, expected, sc.getName(), sc.getStrategy()));
            }
        }
        return all;
    }

    // ---------- Core Tests ----------

    @ParameterizedTest(name = "[{index}] {0} - null should throw")
    @MethodSource("nullStrategySource")
    @DisplayName("null input should throw IllegalArgumentException (both impls)")
    void nullInputThrows(String implName, PalindromeStrategy strategy) {
        assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        strategy.check(null);
                    }
                },
                "Implementation " + implName + " should throw on null"
        );
    }

    @ParameterizedTest(name = "[{index}] {2} - \"{0}\" -> {1}")
    @MethodSource("palindromeCasesAllStrategies")
    @DisplayName("general palindrome / non-palindrome behavior (both impls)")
    void generalBehavior(String input, boolean expected, String implName, PalindromeStrategy strategy) {
        assertEquals(
                expected,
                strategy.check(input),
                "Implementation " + implName + " failed for input: " + input
        );
    }

    @Test
    @DisplayName("normalize keeps only alphanumeric and lowercases")
    void normalizeLeavesOnlyAlnumLowercase() {
        String in = "Ab!2 C";
        String expected = "ab2c";
        assertEquals(expected, PalindromeWithTwoPointers.normalize(in));
    }

    // ---------- Advanced / Grouped / Parameterized ----------

    @Nested
    @DisplayName("Grouped tests: parameterized + aggregated assertions")
    class GroupedTests {

        @ParameterizedTest(name = "[{index}] {1} - \"{0}\" should be palindrome")
        @MethodSource("com.example.palindrome.PalindromeTest#knownPalindromesAllStrategies")
        @DisplayName("several known palindromes")
        void valueSourcePalindromes(String candidate, String implName, PalindromeStrategy strategy) {
            assertTrue(
                    strategy.check(candidate),
                    implName + " should consider this a palindrome: " + candidate
            );
        }

        @ParameterizedTest(name = "[{index}] {2} - \"{0}\" -> {1}")
        @MethodSource("com.example.palindrome.PalindromeTest#csvLikeCasesAllStrategies")
        @DisplayName("csv-driven style cases with expected results")
        void csvSourceExamples(String input, boolean expected, String implName, PalindromeStrategy strategy) {
            assertEquals(
                    expected,
                    strategy.check(input),
                    implName + " wrong result for: " + input
            );
        }

        @Test
        @DisplayName("assertAll example checks multiple palindromes/non-palindromes at once (both impls)")
        void assertAllExample() {
            List<StrategyCase> impls = getStrategies();

            for (final StrategyCase sc : impls) {
                assertAll(
                        "multiple for " + sc.getName(),
                        new Executable() {
                            @Override
                            public void execute() {
                                assertTrue(sc.getStrategy().check("aba"), sc.getName());
                            }
                        },
                        new Executable() {
                            @Override
                            public void execute() {
                                assertTrue(sc.getStrategy().check("A man, a plan, a canal: Panama"), sc.getName());
                            }
                        },
                        new Executable() {
                            @Override
                            public void execute() {
                                assertFalse(sc.getStrategy().check("abc"), sc.getName());
                            }
                        }
                );
            }
        }
    }

    @ParameterizedTest(name = "[{index}] {2} - \"{0}\" -> {1}")
    @MethodSource("methodSourceCasesAllStrategies")
    @DisplayName("method-source parameterized tests (both impls)")
    void methodSourceTests(String input, boolean expected, String implName, PalindromeStrategy strategy) {
        assertEquals(
                expected,
                strategy.check(input),
                implName + " wrong result for: " + input
        );
    }

    @TestFactory
    @DisplayName("dynamic tests generated at runtime (both impls)")
    List<DynamicTest> dynamicTestsFromStrings() {
        String[] cases = { "rotor", "step on no pets", "java" };
        List<DynamicTest> tests = new ArrayList<>();
        List<StrategyCase> impls = getStrategies();

        for (final StrategyCase sc : impls) {
            for (final String s : cases) {
                final boolean expected = !"java".equals(s);
                tests.add(
                        DynamicTest.dynamicTest(
                                "dynamic[" + sc.getName() + "]: " + s,
                                new Executable() {
                                    @Override
                                    public void execute() {
                                        assertEquals(
                                                expected,
                                                sc.getStrategy().check(s),
                                                sc.getName() + " wrong for dynamic case: " + s
                                        );
                                    }
                                }
                        )
                );
            }
        }
        return tests;
    }

    @RepeatedTest(3)
    @DisplayName("repeated test example (runs 3 times on all impls)")
    void repeatedTestExample() {
        List<StrategyCase> impls = getStrategies();
        for (StrategyCase sc : impls) {
            assertTrue(
                    sc.getStrategy().check("aa"),
                    "should be palindrome for " + sc.getName()
            );
        }
    }

    @Test
    @DisplayName("timeout example (must complete quickly) on both impls")
    @Timeout(value = 1) // seconds
    void timeoutExample() {
        List<StrategyCase> impls = getStrategies();
        for (final StrategyCase sc : impls) {
            assertTimeoutPreemptively(
                    Duration.ofMillis(200),
                    new Executable() {
                        @Override
                        public void execute() {
                            assertTrue(sc.getStrategy().check("racecar"));
                        }
                    },
                    "implementation too slow: " + sc.getName()
            );
        }
    }

    @Test
    @DisplayName("assumption example - will be skipped if assumption fails")
    void assumptionExample() {
        assumeTrue(System.getenv("ENV") != null, "Skipping because ENV is not set");

        List<StrategyCase> impls = getStrategies();
        for (StrategyCase sc : impls) {
            assertTrue(
                    sc.getStrategy().check("level"),
                    "level should be palindrome for " + sc.getName()
            );
        }
    }

    @Test
    @Disabled("example of a disabled test")
    @DisplayName("disabled test example")
    void disabledExample() {
        fail("This test is disabled and should not run.");
    }

    // ---------- Lifecycle Hooks ----------

    @BeforeAll
    static void beforeAll() {
        System.out.println("PalindromeTest: @BeforeAll");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("PalindromeTest: @AfterAll");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("PalindromeTest: @BeforeEach");
    }

    @AfterEach
    void afterEach() {
        System.out.println("PalindromeTest: @AfterEach");
    }
}
