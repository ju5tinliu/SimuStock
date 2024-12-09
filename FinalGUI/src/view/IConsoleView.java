package view;

/**
 * Provides methods to display menus, get user input, display messages, and close the console view.
 */
public interface IConsoleView {

  /**
   * Displays the menu to the user.
   */
  void displayMenu();

  /**
   * Gets the user's choice from the menu.
   *
   * @return the user's choice as an integer
   */
  int getUserChoice();

  /**
   * Prompts the user to input a stock symbol and returns the entered symbol.
   *
   * @param prompt the prompt message to display
   * @return the entered stock symbol
   */
  String getInputSymbol(String prompt);

  /**
   * Prompts the user to input a value and returns the entered input.
   *
   * @param prompt the prompt message to display
   * @return the entered input
   */
  String getInput(String prompt);

  /**
   * Displays a message to the user.
   *
   * @param message the message to display
   */
  void displayMessage(String message);

  /**
   * Closes the console view.
   */
  void close();
}
