package controller;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import controller.commands.IStockController;
import controller.commands.MockStockController;
import model.Stock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This class provides unit tests for the MockStockController class.
 * It tests various methods related to stock calculations such as gain/loss,
 * moving average, and crossovers.
 */
public class StockPortfolioControllerTest {

  private IStockController stockController;
  private List<Stock> mockStocks;

  @Before
  public void setUp() {
    stockController = new MockStockController();
    mockStocks = new ArrayList<>();
    mockStocks.add(createStock("AAPL", "2024-01-01", 100.0, 110.0,
            90.0, 105.0, 1000));
    mockStocks.add(createStock("AAPL", "2024-01-02", 105.0, 115.0,
            95.0, 110.0, 1100));
    mockStocks.add(createStock("AAPL", "2024-01-03", 110.0, 120.0,
            100.0, 115.0, 1200));
    mockStocks.add(createStock("AAPL", "2024-01-04", 115.0, 125.0,
            105.0, 120.0, 1300));
  }

  private Stock createStock(String symbol, String timestamp, double open, double high,
                            double low, double close, int volume) {
    Stock stock = new Stock();
    stock.setSymbol(symbol);
    stock.setTimestamp(timestamp);
    stock.setOpen(open);
    stock.setHigh(high);
    stock.setLow(low);
    stock.setClose(close);
    stock.setVolume(volume);
    return stock;
  }

  @Test
  public void testCalculateGainLoss() {
    double gainLoss = stockController.calculateGainLoss(mockStocks, "2024-01-01",
            "2024-01-04");
    assertEquals(14.29, gainLoss, 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateGainLossWithInvalidDates() {
    stockController.calculateGainLoss(mockStocks, "2024-01-01", "invalid-date");
  }

  @Test
  public void testCalculateMovingAverage() {
    double movingAverage = stockController.calculateMovingAverage(mockStocks, "2024-01-04",
            3);
    assertEquals(115.0, movingAverage, 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateMovingAverageWithInvalidDate() {
    stockController.calculateMovingAverage(mockStocks, "invalid-date", 3);
  }

  @Test
  public void testFindCrossovers() {
    List<String> crossovers = stockController.findCrossovers(mockStocks, "2024-01-01",
            "2024-01-04", 2);
    assertEquals(2, crossovers.size());
    assertTrue(crossovers.contains("2024-01-03"));
    assertTrue(crossovers.contains("2024-01-04"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindCrossoversWithInvalidDates() {
    stockController.findCrossovers(mockStocks, "2024-01-01", "invalid-date",
            2);
  }

  @Test
  public void testCalculateGainLossInvalidDates() {
    double gainLoss = stockController.calculateGainLoss(mockStocks, "2024-01-01",
            "2024-01-05");
    assertEquals(0.0, gainLoss, 0.01);
  }

  @Test
  public void testCalculateMovingAverageNotEnoughData() {
    double movingAverage = stockController.calculateMovingAverage(mockStocks, "2024-01-04",
            5);
    assertEquals(0.0, movingAverage, 0.01);
  }

  @Test
  public void testFindCrossoversNoCrossovers() {
    List<String> crossovers = stockController.findCrossovers(mockStocks, "2024-01-01",
            "2024-01-02", 2);
    assertTrue(crossovers.isEmpty());
  }

  @Test
  public void testEmptyStocks() {
    List<Stock> emptyStocks = new ArrayList<>();
    assertEquals(0.0, stockController.calculateGainLoss(emptyStocks,
            "2024-01-01", "2024-01-04"), 0.01);
    assertEquals(0.0, stockController.calculateMovingAverage(emptyStocks,
            "2024-01-04", 3), 0.01);
    assertTrue(stockController.findCrossovers(emptyStocks, "2024-01-01",
            "2024-01-04", 2).isEmpty());
  }

  @Test
  public void testSingleDayRangeForGainLoss() {
    double gainLoss = stockController.calculateGainLoss(mockStocks, "2024-01-01",
            "2024-01-01");
    assertEquals(0.0, gainLoss, 0.01);
  }

  @Test
  public void testNonExistingDates() {
    assertEquals(0.0, stockController.calculateGainLoss(mockStocks,
            "2024-01-05", "2024-01-06"), 0.01);
    assertEquals(0.0, stockController.calculateMovingAverage(mockStocks,
            "2024-01-05", 3), 0.01);
    assertTrue(stockController.findCrossovers(mockStocks, "2024-01-05",
            "2024-01-06", 2).isEmpty());
  }

  @Test
  public void testInvalidDateFormats() {
    assertEquals(0.0, stockController.calculateGainLoss(mockStocks,
            "invalid-date", "2024-01-04"), 0.01);
    assertEquals(0.0, stockController.calculateMovingAverage(mockStocks,
            "invalid-date", 3), 0.01);
    assertTrue(stockController.findCrossovers(mockStocks, "invalid-date",
            "2024-01-04", 2).isEmpty());
  }
}
