package view;

import java.util.Scanner;

/**
 * This class provides a console-based user interface.
 * It displays the menus, gets user input, and displays messages.
 */
public class ConsoleView implements IConsoleView {
  private Scanner scanner;

  /**
   * Constructs a new ConsoleView and scans for user input.
   */
  public ConsoleView() {
    this.scanner = new Scanner(System.in);
  }

  /**
   * Displays the main menu options to the user.
   */
  @Override
  public void displayMenu() {
    System.out.println("1. View Stock Gain/Loss");
    System.out.println("2. View x-Day Moving Average");
    System.out.println("3. View x-Day Crossovers");
    System.out.println("4. Create Portfolio");
    System.out.println("5. View Portfolio Value");
    System.out.println("6. Add Stock");
    System.out.println("7. Remove Stock");
    System.out.println("8. View Portfolio Value Distribution");
    System.out.println("9. Determine Portfolio Composition on Date");
    System.out.println("10. Rebalance Portfolio");
    System.out.println("11. Clear Portfolio");
    System.out.println("12. View Portfolio Performance Over Time");
    System.out.println("13. Update Portfolio");
    System.out.println("14. Exit");
    System.out.println("\n\nMake sure to Update Portfolio (13) to save the changes!");
  }

  /**
   * Prompts the user to enter their choice from the menu.
   *
   * @return the user's choice as an integer
   */
  @Override
  public int getUserChoice() {
    int choice = -1;
    System.out.print("Enter your choice: ");
    while (true) {
      String input = scanner.nextLine();
      try {
        choice = Integer.parseInt(input);
        break; // exit loop if parsing is successful
      } catch (NumberFormatException e) {
        System.out.print("Invalid input. Please enter a number: ");
      }
    }
    return choice;
  }

  /**
   * Prompts the user to enter a stock symbol and ensures the input is valid.
   * The input should only contain letters and may contain spaces between words.
   *
   * @param prompt the message to prompt the user
   * @return the user's input as a string
   */
  @Override
  public String getInputSymbol(String prompt) {
    String input = null;
    while (true) {
      try {
        System.out.print(prompt);
        input = scanner.nextLine().trim(); // Trim leading and trailing spaces
        if (input.isEmpty() || !input.matches("[a-zA-Z]+( [a-zA-Z]+)*")) {
          throw new ArrayIndexOutOfBoundsException(
                  "Invalid input. Input should only contain letters. Please try again.");
        }
        break; // exit loop if input is valid
      } catch (ArrayIndexOutOfBoundsException e) {
        System.out.println(e.getMessage());
      }
    }
    return input;
  }

  /**
   * Prompts the user with a message and returns their input as a String.
   *
   * @param prompt the message to prompt the user
   * @return the user's input as a string
   */
  @Override
  public String getInput(String prompt) {
    System.out.print(prompt);
    return scanner.nextLine();
  }

  /**
   * Displays a message to the console.
   *
   * @param message the message to display
   */
  @Override
  public void displayMessage(String message) {
    System.out.println(message);
  }

  /**
   * Close the entire console view.
   */
  @Override
  public void close() {
    scanner.close();
  }
}
