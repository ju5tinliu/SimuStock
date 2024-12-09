package controller;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import controller.commands.MockPortfolioController;
import model.Portfolio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * This class contains unit tests for the PortfolioController implementation.
 * It uses a mock implementation of the PortfolioController to verify the correct behavior
 * of portfolio management operations such as adding and removing stocks, calculating values,
 * and persisting portfolio data.
 */

public class PortfolioControllerTest {
  private MockPortfolioController mockPortfolioController;
  private Portfolio testPortfolio;

  @Before
  public void setUp() {
    mockPortfolioController = new MockPortfolioController() {
      @Override
      public void loadAllPortfoliosFromDirectory(String portfolios) throws IOException {
        // This is empty in order to run the MockPortfolioController properly.
      }
    };
    testPortfolio = new Portfolio("test");
  }

  @Test
  public void testAddStockToPortfolio() {
    String stockSymbol = "AAPL";
    double quantity = 10.5;
    String date = "2024-06-01";
    mockPortfolioController.addStockToPortfolio(testPortfolio, stockSymbol, quantity, date);

    assertEquals(10.5, testPortfolio.getStocks().get(stockSymbol), 0.001);
    assertEquals(date, testPortfolio.getTransactionDate(stockSymbol));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddStockToPortfolioWithNegativeQuantity() {
    mockPortfolioController.addStockToPortfolio(testPortfolio, "AAPL", -10.0,
            "2024-06-01");
  }

  @Test
  public void testRemoveStockFromPortfolio() {
    String stockSymbol = "AAPL";
    double quantity = 10.5;
    String addDate = "2024-06-01";
    String removeDate = "2024-06-02";
    mockPortfolioController.addStockToPortfolio(testPortfolio, stockSymbol, quantity, addDate);
    mockPortfolioController.removeStockFromPortfolio(testPortfolio, stockSymbol,
            5.0, removeDate);

    assertEquals(5.5, testPortfolio.getStocks().get(stockSymbol), 0.001);
    assertEquals(removeDate, testPortfolio.getTransactionDate(stockSymbol));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveStockFromPortfolioWithNegativeQuantity() {
    mockPortfolioController.removeStockFromPortfolio(testPortfolio, "AAPL", -10.0,
            "2024-06-02");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveStockFromPortfolioWithMoreThanExists() {
    mockPortfolioController.addStockToPortfolio(testPortfolio, "AAPL", 10.0,
            "2024-06-01");
    mockPortfolioController.removeStockFromPortfolio(testPortfolio, "AAPL", 20.0,
            "2024-06-02");
  }

  @Test
  public void testCalculatePortfolioValue() {
    String stockSymbol = "AAPL";
    double quantity = 10.0;
    String date = "2024-06-01";
    mockPortfolioController.addStockToPortfolio(testPortfolio, stockSymbol, quantity, date);

    double expectedValue = 1000.0; // 100.0 (mock price) * 10 (quantity)
    double actualValue = mockPortfolioController.calculatePortfolioValue(testPortfolio,
            "2024-06-07");

    assertEquals(expectedValue, actualValue, 0.001);

    mockPortfolioController.clearPortfolio(testPortfolio);

    mockPortfolioController.addStockToPortfolio(testPortfolio, "AAPL", 10.0,
            "2024-01-01");
    mockPortfolioController.addStockToPortfolio(testPortfolio, "GOOG", 5.0,
            "2024-01-01");
    mockPortfolioController.addStockToPortfolio(testPortfolio, "MSFT", 20.0,
            "2024-02-01");

    double expectedValue2 = 3500.0; // 100.0 (mock price) * (10+5+20) shares
    double actualValue2 = mockPortfolioController.calculatePortfolioValue(testPortfolio,
            "2024-03-01");

    assertEquals(expectedValue2, actualValue2, 0.001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculatePortfolioValueWithInvalidDate() {
    mockPortfolioController.addStockToPortfolio(testPortfolio, "AAPL", 10.0,
            "2024-06-01");
    mockPortfolioController.calculatePortfolioValue(testPortfolio, "invalid-date");
  }

  @Test
  public void testGetPortfolioValueDistribution() {
    mockPortfolioController.addStockToPortfolio(testPortfolio, "AAPL", 10.0,
            "2024-01-01");
    mockPortfolioController.addStockToPortfolio(testPortfolio, "GOOG", 5.0,
            "2024-01-01");
    mockPortfolioController.addStockToPortfolio(testPortfolio, "MSFT", 20.0,
            "2024-02-01");

    Map<String, Double> distribution = mockPortfolioController
            .getPortfolioValueDistribution(testPortfolio, "2024-03-01");

    assertEquals(1000.0, distribution.get("AAPL"), 0.001);
    assertEquals(500.0, distribution.get("GOOG"), 0.001);
    assertEquals(2000.0, distribution.get("MSFT"), 0.001);
    double totalValue = distribution.values().stream().mapToDouble(Double::doubleValue).sum();
    assertEquals(3500.0, totalValue, 0.001);
  }

  @Test
  public void testPersistPortfolio() throws IOException {
    mockPortfolioController.addStockToPortfolio(testPortfolio, "AAPL", 10.0,
            "2024-06-01");
    mockPortfolioController.addStockToPortfolio(testPortfolio, "GOOG", 15.0,
            "2024-06-01");

    File file = new File("TestPortfolio.xml");
    mockPortfolioController.savePortfolioToFile(testPortfolio, file);

    Portfolio loadedPortfolio = mockPortfolioController.loadPortfolioFromFile(file);

    Map<String, Double> loadedStocks = loadedPortfolio.getStocks();
    assertEquals(10.0, loadedStocks.get("AAPL"), 0.001);
    assertEquals(15.0, loadedStocks.get("GOOG"), 0.001);
  }

  @Test
  public void testClearPortfolio() {
    mockPortfolioController.addStockToPortfolio(testPortfolio, "AAPL", 10.0,
            "2024-06-01");
    mockPortfolioController.addStockToPortfolio(testPortfolio, "GOOG", 15.0,
            "2024-06-01");

    mockPortfolioController.clearPortfolio(testPortfolio);

    assertTrue(testPortfolio.getStocks().isEmpty());
    assertTrue(testPortfolio.getTransactionDate("AAPL") == null);
    assertTrue(testPortfolio.getTransactionDate("GOOG") == null);
  }

  @Test
  public void testGetCompositionOnSpecificDate() {
    Portfolio portfolio = new Portfolio("TestPortfolio");
    portfolio.addStock("AAPL", 10.0, "2024-01-01");
    portfolio.addStock("GOOG", 5.0, "2024-01-01");
    portfolio.addStock("MSFT", 20.0, "2024-02-01");

    Map<String, Double> composition = portfolio.getCompositionOnDate("2024-02-01");
    assertEquals(10.0, composition.get("AAPL"), 0.001);
    assertEquals(5.0, composition.get("GOOG"), 0.001);
    assertEquals(20.0, composition.get("MSFT"), 0.001);
  }

  @Test
  public void testCalculatePortfolioValueOnSpecificDate() {
    Portfolio portfolio = new Portfolio("TestPortfolio");
    mockPortfolioController.addStockToPortfolio(portfolio, "AAPL", 10.0,
            "2024-01-01");
    mockPortfolioController.addStockToPortfolio(portfolio, "GOOG", 5.0,
            "2024-01-01");
    mockPortfolioController.addStockToPortfolio(portfolio, "MSFT", 20.0,
            "2024-02-01");

    double expectedValue = 3500.0; // Mock values assume each stock price is 100.0
    double actualValue = mockPortfolioController.calculatePortfolioValue(portfolio,
            "2024-03-01");
    assertEquals(expectedValue, actualValue, 0.001);
  }

  @Test
  public void testParsePersistedData() throws IOException {
    Portfolio portfolio = new Portfolio("TestPortfolio");
    String xmlData = "<Portfolio name=\"TestPortfolio\">" +
            "<Stock symbol=\"AAPL\" quantity=\"10.0\" date=\"2024-01-01\"/>" +
            "<Stock symbol=\"GOOG\" quantity=\"5.0\" date=\"2024-01-01\"/>" +
            "<Stock symbol=\"MSFT\" quantity=\"20.0\" date=\"2024-02-01\"/>" +
            "</Portfolio>";
    File file = new File("TestPortfolio.xml");
    try (PrintWriter out = new PrintWriter(file)) {
      out.println(xmlData);
    }
    portfolio.loadFromFile(file);

    Map<String, Double> stocks = portfolio.getStocks();
    assertEquals(10.0, stocks.get("AAPL"), 0.001);
    assertEquals(5.0, stocks.get("GOOG"), 0.001);
    assertEquals(20.0, stocks.get("MSFT"), 0.001);
  }

  @Test
  public void testCalculateCostBasis() {
    Portfolio portfolio = new Portfolio("TestPortfolio");
    mockPortfolioController.addStockToPortfolio(portfolio, "AAPL", 10.0,
            "2024-01-01");
    mockPortfolioController.addStockToPortfolio(portfolio, "GOOG", 5.0,
            "2024-01-01");
    mockPortfolioController.addStockToPortfolio(portfolio, "MSFT", 20.0,
            "2024-02-01");

    double expectedCostBasis = 3500.0; // Mock values assume each stock price is 100.0
    double actualCostBasis = mockPortfolioController.calculatePortfolioValue(portfolio,
            "2024-03-01");
    assertEquals(expectedCostBasis, actualCostBasis, 0.001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddStockWithInvalidDate() {
    Portfolio portfolio = new Portfolio("TestPortfolio");
    mockPortfolioController.addStockToPortfolio(portfolio, "AAPL", 10.0,
            "invalid-date");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveStockWithInvalidDate() {
    Portfolio portfolio = new Portfolio("TestPortfolio");
    mockPortfolioController.addStockToPortfolio(portfolio, "AAPL", 10.0,
            "2024-01-01");
    mockPortfolioController.removeStockFromPortfolio(portfolio, "AAPL", 5.0,
            "invalid-date");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveStockExceedingQuantity() {
    Portfolio portfolio = new Portfolio("TestPortfolio");
    mockPortfolioController.addStockToPortfolio(portfolio, "AAPL", 10.0,
            "2024-01-01");
    mockPortfolioController.removeStockFromPortfolio(portfolio, "AAPL", 20.0,
            "2024-01-02");
  }

}
