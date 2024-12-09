package model;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import controller.commands.AlphaVantageControllerImpl;

/**
 * This interface defines the methods required for managing a portfolio of stocks.
 * Implementations of this interface provides the necessary methods to add, remove, and retrieve
 * stocks within the portfolio.
 */
public interface IPortfolio {

  /**
   * Adds the specified quantity of the given stock symbol to the portfolio.
   * If the stock symbol already exists in the portfolio, the quantity is increased by
   * the specified amount.
   *
   * @param symbol   the stock symbol to add
   * @param quantity the quantity of the stock to add
   */
  void addStock(String symbol, double quantity, String date);

  /**
   * Removes the specified quantity of the given stock symbol from the portfolio.
   * If the resulting quantity is less than or equal to zero, the stock symbol is removed
   * from the portfolio.
   *
   * @param symbol   the stock symbol to remove
   * @param quantity the quantity of the stock to remove
   */
  void removeStock(String symbol, double quantity, String date);

  /**
   * Retrieves the current stocks in the portfolio.
   *
   * @return a map of stock symbols and their corresponding quantities in the portfolio
   */
  Map<String, Double> getStocks();

  /**
   * Saves the portfolio to an XML file.
   *
   * @param file the file to save the portfolio to
   * @throws IOException if an I/O error occurs
   */
  void saveToFile(File file) throws IOException;

  /**
   * Loads the portfolio from an XML file.
   *
   * @param file the file to load the portfolio from
   * @throws IOException if an I/O error occurs
   */
  void loadFromFile(File file) throws IOException;

  Map<String, Double> getCompositionOnDate(String date);

  void clearPortfolio();

  String getTransactionDate(String symbol);

  Map<String, Double> getPortfolioValuesOverTime(String startDate,
                                                 String endDate, AlphaVantageControllerImpl
                                                         alphaVantageControllerImpl);

}
