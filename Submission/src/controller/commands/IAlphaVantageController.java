package controller.commands;

import java.io.IOException;
import java.util.List;

import model.Stock;

/**
 * Provides the necessary methods to fetch and store stock data, check if the data is up-to-date,
 * and read data from a CSV file.
 */
public interface IAlphaVantageController {

  /**
   * Fetches stock data for a given stock symbol and stores it at the specified file path.
   *
   * @param symbol   the stock symbol to fetch data for
   * @param filePath the file path to store the fetched data
   * @throws IOException if an I/O error occurs
   */
  void fetchAndStoreData(String symbol, String filePath) throws IOException;

  /**
   * Fetches stock data for a given stock symbol.
   *
   * @param symbol the stock symbol to fetch data for
   * @return a list of {@code Stock} objects containing the fetched data
   */
  List<Stock> fetchStockData(String symbol);

  /**
   * Checks if the stock data stored at a given file path is up to date.
   *
   * @param filePath the file path to check
   * @return true if the data is up-to-date, false otherwise
   */
  boolean isDataUpToDate(String filePath);

  /**
   * Reads stock data from a CSV file at a specified file path.
   *
   * @param filePath the file path to read data from
   */
  void readDataFromCSV(String filePath);

  /**
   * Fetches the stock price for a given stock symbol on a specified date.
   *
   * @param stockSymbol the stock symbol to fetch the price for
   * @param date the date to fetch the stock price
   * @return the closing stock price on the specified date
   */
  double getStockPrice(String stockSymbol, String date);
}
