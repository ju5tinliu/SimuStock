package model;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class provides unit tests for the MockPortfolio class.
 * It tests various methods related to managing stocks in a portfolio.
 */
public class PortfolioTest {

  @Test
  public void testMockPortfolio() {
    MockPortfolio portfolio = new MockPortfolio("TestPortfolio");

    // Test adding stocks
    portfolio.addStock("AAPL", 10.5, "2024-06-01");
    portfolio.addStock("GOOG", 15.75, "2024-06-01");

    Map<String, Double> stocks = portfolio.getStocks();
    assertEquals(10.5, stocks.get("AAPL"), 0.001);
    assertEquals(15.75, stocks.get("GOOG"), 0.001);

    // Test removing stocks
    portfolio.removeStock("AAPL", 5.0, "2024-06-02");
    assertEquals(5.5, stocks.get("AAPL"), 0.001);

    portfolio.removeStock("AAPL", 5.5, "2024-06-03");
    assertFalse(stocks.containsKey("AAPL"));
  }

  @Test
  public void testGetCompositionOnDate() {
    MockPortfolio portfolio = new MockPortfolio("TestPortfolio");

    // Initial transactions
    portfolio.addStock("AAPL", 10.0, "2024-01-01");
    portfolio.addStock("GOOG", 15.0, "2024-01-01");
    portfolio.addStock("MSFT", 5.0, "2024-02-01");

    // Transactions on March 1
    portfolio.removeStock("AAPL", 5.0, "2024-03-01");
    portfolio.addStock("GOOG", 10.0, "2024-03-01");

    // Verify composition on different dates
    Map<String, Double> composition = portfolio.getCompositionOnDate("2024-01-15");
    assertEquals(10.0, composition.get("AAPL"), 0.001);
    assertEquals(15.0, composition.get("GOOG"), 0.001);
    assertEquals(0.0, composition.getOrDefault("MSFT", 0.0), 0.001);

    composition = portfolio.getCompositionOnDate("2024-02-15");
    assertEquals(10.0, composition.get("AAPL"), 0.001);
    assertEquals(15.0, composition.get("GOOG"), 0.001);
    assertEquals(5.0, composition.get("MSFT"), 0.001);

    composition = portfolio.getCompositionOnDate("2024-03-15");
    assertEquals(5.0, composition.get("AAPL"), 0.001);
    assertEquals(25.0, composition.get("GOOG"), 0.001);
    assertEquals(5.0, composition.get("MSFT"), 0.001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddStockWithNegativeQuantity() {
    MockPortfolio portfolio = new MockPortfolio("TestPortfolio");
    portfolio.addStock("AAPL", -10.0, "2024-06-01");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveStockWithNegativeQuantity() {
    MockPortfolio portfolio = new MockPortfolio("TestPortfolio");
    portfolio.removeStock("AAPL", -10.0, "2024-06-01");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveStockWithMoreThanExists() {
    MockPortfolio portfolio = new MockPortfolio("TestPortfolio");
    portfolio.addStock("AAPL", 10.0, "2024-06-01");
    portfolio.removeStock("AAPL", 20.0, "2024-06-02");
  }

  @Test
  public void testGetStocksWhenEmpty() {
    MockPortfolio portfolio = new MockPortfolio("TestPortfolio");
    Map<String, Double> stocks = portfolio.getStocks();
    assertTrue(stocks.isEmpty());
  }

  @Test
  public void testRemoveStockNotPresent() {
    MockPortfolio portfolio = new MockPortfolio("TestPortfolio");
    portfolio.addStock("AAPL", 10.0, "2024-06-01");
    portfolio.removeStock("GOOG", 5.0, "2024-06-02"); // Removing stock not present
    assertEquals(10.0, portfolio.getStocks().get("AAPL"), 0.001);
    assertFalse(portfolio.getStocks().containsKey("GOOG"));
  }

  @Test
  public void testAddStockWithZeroQuantity() {
    MockPortfolio portfolio = new MockPortfolio("TestPortfolio");
    portfolio.addStock("AAPL", 0.0, "2024-06-01"); // Adding stock with zero quantity
    assertTrue(portfolio.getStocks().isEmpty());
  }

  @Test
  public void testSaveAndLoadPortfolio() throws IOException {
    MockPortfolio portfolio = new MockPortfolio("TestPortfolio");
    portfolio.addStock("AAPL", 10.0, "2024-06-01");
    portfolio.addStock("GOOG", 15.0, "2024-06-01");

    File file = new File("TestPortfolio.xml");
    portfolio.saveToFile(file);

    MockPortfolio loadedPortfolio = new MockPortfolio("TestPortfolio");
    loadedPortfolio.loadFromFile(file);

    Map<String, Double> loadedStocks = loadedPortfolio.getStocks();
    assertEquals(10.0, loadedStocks.get("AAPL"), 0.001);
    assertEquals(15.0, loadedStocks.get("GOOG"), 0.001);
  }

  @Test
  public void testClearPortfolio() {
    MockPortfolio portfolio = new MockPortfolio("TestPortfolio");
    portfolio.addStock("AAPL", 10.0, "2024-06-01");
    portfolio.addStock("GOOG", 15.0, "2024-06-01");

    portfolio.clearPortfolio();

    assertTrue(portfolio.getStocks().isEmpty());
    assertTrue(portfolio.getTransactionDate("AAPL") == null);
    assertTrue(portfolio.getTransactionDate("GOOG") == null);
  }

}
