package controller;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import controller.commands.IAlphaVantageController;
import controller.commands.MockAlphaVantageController;
import model.Stock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class provides unit tests for the MockAlphaVantageController class.
 * It tests various methods related to fetching and storing stock data, and checking data currency.
 */
public class AlphaVantagePortfolioControllerTest {

  private IAlphaVantageController mockController;

  @Before
  public void setUp() {
    mockController = new MockAlphaVantageController();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFetchAndStoreDataWithInvalidSymbol() throws IOException {
    String symbol = "";
    String filePath = "mock_data_invalid.csv";
    mockController.fetchAndStoreData(symbol, filePath);
  }

  @Test(expected = IOException.class)
  public void testFetchAndStoreDataWithInvalidPath() throws IOException {
    String symbol = "AAPL";
    String filePath = "";
    mockController.fetchAndStoreData(symbol, filePath);
  }

  @Test(expected = IOException.class)
  public void testFetchAndStoreDataNetworkFailure() throws IOException {
    String symbol = "INVALID";
    String filePath = "mock_data_" + symbol + ".csv";
    // Simulate network failure by using an invalid symbol or URL
    mockController.fetchAndStoreData(symbol, filePath);
  }

  @Test
  public void testFetchStockData() {
    String symbol = "AAPL";
    List<Stock> stockData = mockController.fetchStockData(symbol);
    assertEquals(1, stockData.size());
    Stock stock = stockData.get(0);
    assertEquals("AAPL", stock.getSymbol());
    assertEquals("2024-01-01", stock.getTimestamp());
    assertEquals(100.0, stock.getOpen(), 0.001);
    assertEquals(110.0, stock.getHigh(), 0.001);
    assertEquals(90.0, stock.getLow(), 0.001);
    assertEquals(105.0, stock.getClose(), 0.001);
    assertEquals(1000, stock.getVolume());

    // Additional test with a different symbol
    symbol = "GOOGL";
    stockData = mockController.fetchStockData(symbol);
    assertEquals(1, stockData.size());
    stock = stockData.get(0);
    assertEquals("GOOGL", stock.getSymbol());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFetchStockDataWithInvalidSymbol() {
    String symbol = "";
    mockController.fetchStockData(symbol);
  }

  @Test
  public void testFetchStockDataWithInvalidCSV() {
    String symbol = "INVALID_CSV";
    List<Stock> stockData = mockController.fetchStockData(symbol);
    // Expecting no data due to invalid CSV format
    assertTrue(stockData.isEmpty());
  }

  @Test
  public void testIsDataUpToDate() {
    String filePath = "mock_data_AAPL.csv";
    assertTrue(mockController.isDataUpToDate(filePath));

    // Additional test with a non-existent file
    filePath = "non_existent_file.csv";
    assertFalse(mockController.isDataUpToDate(filePath));
  }

}
