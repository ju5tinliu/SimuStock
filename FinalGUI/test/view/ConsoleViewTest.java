package view;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

/**
 * This class provides unit tests for the ConsoleView class.
 * It tests various methods related to displaying menus, getting user
 * input, and displaying messages.
 */
public class ConsoleViewTest {
  private ConsoleView consoleView;
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;

  @Before
  public void setUp() {
    System.setOut(new PrintStream(outContent));
  }

  @After
  public void tearDown() {
    System.setOut(originalOut);
  }

  @Test
  public void testDisplayMenu() {
    // Capture the system output
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outContent));

    // Create an instance of ConsoleView and display the menu
    ConsoleView consoleView = new ConsoleView();
    consoleView.displayMenu();

    // Reset the system output to the original stream
    System.setOut(originalOut);

    // Define the expected output
    String expectedOutput = "1. View Stock Gain/Loss\n" +
            "2. View x-Day Moving Average\n" +
            "3. View x-Day Crossovers\n" +
            "4. Create Portfolio\n" +
            "5. View Portfolio Value\n" +
            "6. Add Stock\n" +
            "7. Remove Stock\n" +
            "8. View Portfolio Value Distribution\n" +
            "9. Determine Portfolio Composition on Date\n" +
            "10. Rebalance Portfolio\n" +
            "11. Clear Portfolio\n" +
            "12. View Portfolio Performance Over Time\n" +
            "13. Update Portfolio\n" +
            "14. Exit\n";

    // Assert that the captured output matches the expected output
    assertEquals(expectedOutput, outContent.toString().replace("\r", ""));
  }

  @Test
  public void testGetUserChoice() {
    String input = "3\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    consoleView = new ConsoleView();
    int choice = consoleView.getUserChoice();
    assertEquals(3, choice);
  }

  @Test
  public void testGetUserChoiceWithInvalidInput() {
    String input = "abc\n2\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    consoleView = new ConsoleView();
    int choice = consoleView.getUserChoice();
    assertEquals(2, choice);
  }

  @Test
  public void testGetInputSymbol() {
    String input = "AAPL\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    consoleView = new ConsoleView();
    String symbol = consoleView.getInputSymbol("Enter stock symbol: ");
    assertEquals("AAPL", symbol);
  }

  @Test
  public void testGetInputSymbolWithInvalidInput() {
    String input = "123\nAAPL\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    consoleView = new ConsoleView();
    String symbol = consoleView.getInputSymbol("Enter stock symbol: ");
    assertEquals("AAPL", symbol);
  }

  @Test
  public void testGetInput() {
    String input = "Sample input\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    consoleView = new ConsoleView();
    String result = consoleView.getInput("Enter something: ");
    assertEquals("Sample input", result);
  }

  @Test
  public void testGetInputWithEmptyInput() {
    String input = "\nSample input\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    consoleView = new ConsoleView();
    String result = consoleView.getInput("Enter something: ");
    assertEquals("", result);
  }

  @Test
  public void testDisplayMessage() {
    consoleView = new ConsoleView();
    consoleView.displayMessage("Hello, World!");

    assertEquals("Hello, World!\n", outContent.toString().replace("\r", ""));
  }

  @Test
  public void testDisplayEmptyMessage() {
    consoleView = new ConsoleView();
    consoleView.displayMessage("");

    assertEquals("\n", outContent.toString().replace("\r", ""));
  }
}

