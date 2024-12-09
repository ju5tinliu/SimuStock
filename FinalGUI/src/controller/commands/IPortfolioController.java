package controller.commands;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import model.Portfolio;

/**
 * This interface defines the methods required for managing portfolios.
 * Implementations of this interface provide the necessary methods to add and remove stocks
 * from a portfolio, as well as calculate the total value of a portfolio on a specific date.
 */
public interface IPortfolioController {

  /**
   * Adds a specified quantity of a stock to the given portfolio on a specified date.
   *
   * @param portfolio the portfolio to update
   * @param symbol the stock symbol to add
   * @param quantity the quantity of the stock to add
   * @param date the date of the transaction
   */
  void addStockToPortfolio(Portfolio portfolio, String symbol, double quantity, String date);

  /**
   * Removes a specified quantity of a stock from the given portfolio on a specified date.
   *
   * @param portfolio the portfolio to update
   * @param symbol the stock symbol to remove
   * @param quantity the quantity of the stock to remove
   * @param date the date of the transaction
   */
  void removeStockFromPortfolio(Portfolio portfolio, String symbol, double quantity, String date);

  /**
   * Calculates the total value of the given portfolio on a specified date.
   *
   * @param portfolio the portfolio to calculate the value for
   * @param date the date to calculate the portfolio value
   * @return the total value of the portfolio
   */
  double calculatePortfolioValue(Portfolio portfolio, String date);

  /**
   * Retrieves the composition of the given portfolio on a specified date.
   *
   * @param portfolio the portfolio to retrieve the composition for
   * @param date the date to get the composition
   * @return a map of stock symbols to their quantities on the specified date
   */
  Map<String, Double> getPortfolioCompositionOnDate(Portfolio portfolio, String date);

  /**
   * Saves the given portfolio to an XML file.
   *
   * @param portfolio the portfolio to save
   * @param file the file to save the portfolio to
   * @throws IOException if an I/O error occurs during saving
   */
  void savePortfolioToFile(Portfolio portfolio, File file) throws IOException;

  /**
   * Loads a portfolio from an XML file.
   *
   * @param file the file to load the portfolio from
   * @return the loaded portfolio
   * @throws IOException if an I/O error occurs during loading
   */
  Portfolio loadPortfolioFromFile(File file) throws IOException;

  /**
   * Clears all stocks and transaction history from the given portfolio.
   *
   * @param portfolio the portfolio to clear
   */
  void clearPortfolio(Portfolio portfolio);

  /**
   * Saves all portfolios to a single XML file.
   *
   * @param file the file to save all portfolios to
   * @throws IOException if an I/O error occurs during saving
   */
  void saveAllPortfoliosToFile(File file) throws IOException;

  /**
   * Loads all portfolios from a single XML file.
   *
   * @param file the file to load all portfolios from
   * @throws IOException if an I/O error occurs during loading
   */
  void loadAllPortfoliosFromFile(File file) throws IOException;

  /**
   * Adds a new portfolio to the controller.
   *
   * @param portfolio the portfolio to add
   */
  void addPortfolio(Portfolio portfolio);

  /**
   * Retrieves a portfolio by its name.
   *
   * @param name the name of the portfolio to retrieve
   * @return the portfolio with the specified name, or null if not found
   */
  Portfolio getPortfolio(String name);

  /**
   * Removes a portfolio by its name.
   *
   * @param name the name of the portfolio to remove
   */
  void removePortfolio(String name);

  /**
   * Retrieves all portfolios managed by the controller.
   *
   * @return a map of portfolio names to portfolios
   */
  Map<String, Portfolio> getAllPortfolios();

  /**
   * Retrieves the value distribution of the given portfolio on a specified date.
   *
   * @param portfolio the portfolio to get the value distribution for
   * @param date the date to get the value distribution
   * @return a map of stock symbols to their values on the specified date
   */
  Map<String, Double> getPortfolioValueDistribution(Portfolio portfolio, String date);

  /**
   * Draws a bar chart representing the performance of the given portfolio over a
   * specified date range.
   * The chart is displayed as a series of asterisks in the console.
   *
   * @param portfolio the portfolio to draw the performance chart for
   * @param startDate the start date of the date range
   * @param endDate the end date of the date range
   */
  void drawPortfolioPerformanceBarChart(Portfolio portfolio, String startDate, String endDate);

  void loadAllPortfoliosFromDirectory(String portfolios) throws IOException;
}
