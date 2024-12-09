package controller.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Stock;

/**
 * This class implements the IAlphaVantageController interface to provide mock implementations for
 * interacting with the Alpha Vantage API.
 */
public class MockAlphaVantageController implements IAlphaVantageController {

  private IAlphaVantageController alphaVantageController;

  /**
   * Simulates fetching and storing data for a given stock symbol to a specified file path.
   *
   * @param symbol   the stock symbol to fetch data for
   * @param filePath the file path to store the fetched data
   * @throws IOException if an I/O error occurs or if the symbol or filePath is invalid
   */
  @Override
  public void fetchAndStoreData(String symbol, String filePath) throws IOException {
    if (symbol == null || symbol.isEmpty()) {
      throw new IllegalArgumentException("Stock symbol cannot be empty");
    }
    if (filePath == null || filePath.isEmpty()) {
      throw new IOException("File path cannot be empty");
    }
    if ("INVALID".equals(symbol)) {
      throw new IOException("Network failure simulated for symbol: " + symbol);
    }
    System.out.println("Mock fetchAndStoreData called with symbol: " + symbol +
            " and filePath: " + filePath);
  }

  /**
   * Simulates fetching stock data for a given stock symbol.
   *
   * @param symbol the stock symbol to fetch data for
   * @return a list of Stock objects containing mock data
   * @throws IllegalArgumentException if the symbol is invalid
   */
  @Override
  public List<Stock> fetchStockData(String symbol) {
    if (symbol == null || symbol.isEmpty()) {
      throw new IllegalArgumentException("Stock symbol cannot be empty");
    }
    // Return mock data
    List<Stock> stockData = new ArrayList<>();
    if ("INVALID_CSV".equals(symbol)) {
      return stockData; // Simulate empty data due to invalid CSV
    }
    Stock stock = new Stock();
    stock.setSymbol(symbol);
    stock.setTimestamp("2024-01-01");
    stock.setOpen(100.0);
    stock.setHigh(110.0);
    stock.setLow(90.0);
    stock.setClose(105.0);
    stock.setVolume(1000);
    stockData.add(stock);
    return stockData;
  }

  /**
   * Simulates checking if the data at the specified file path is up-to-date.
   *
   * @param filePath the file path to check for data currency
   * @return true, since this is a mock implementation
   */
  @Override
  public boolean isDataUpToDate(String filePath) {
    return !"non_existent_file.csv".equals(filePath);
  }

  /**
   * Simulates reading data from a CSV file at the specified file path.
   *
   * @param filePath the file path to read data from
   */
  @Override
  public void readDataFromCSV(String filePath) {
    System.out.println("Mock readDataFromCSV called with filePath: " + filePath);
  }

  /**
   * Simulates fetching the closing stock price for a given stock symbol on a specific date.
   *
   * @param stockSymbol the stock symbol to fetch the price for
   * @param date the date for which to fetch the stock price
   * @return the closing stock price on the specified date
   * @throws IllegalArgumentException if no stock price is available for the given date
   */
  @Override
  public double getStockPrice(String stockSymbol, String date) {
    List<Stock> stocks = alphaVantageController.fetchStockData(stockSymbol);
    for (Stock stock : stocks) {
      if (stock.getTimestamp().equals(date)) {
        return stock.getClose();
      }
    }
    throw new IllegalArgumentException("No stock price available for the given date");
  }
}
