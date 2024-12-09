package model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * This class provides unit tests for the MockStock class.
 * It tests various methods related to stock attributes such as symbol, timestamp,
 * open, high, low, close, and volume.
 */
public class StockTest {
  @Test
  public void testMockStock() {
    // Create a MockStock object with initial values
    MockStock stock = new MockStock("AAPL", "2024-06-07", 150.00,
            155.00, 149.00, 154.00, 1000000);

    // Test getters
    assertEquals("AAPL", stock.getSymbol());
    assertEquals("2024-06-07", stock.getTimestamp());
    assertEquals(150.00, stock.getOpen(), 0.01);
    assertEquals(155.00, stock.getHigh(), 0.01);
    assertEquals(149.00, stock.getLow(), 0.01);
    assertEquals(154.00, stock.getClose(), 0.01);
    assertEquals(1000000, stock.getVolume());

    // Test setters
    stock.setSymbol("GOOG");
    assertEquals("GOOG", stock.getSymbol());

    stock.setTimestamp("2024-06-08");
    assertEquals("2024-06-08", stock.getTimestamp());

    stock.setOpen(160.00);
    assertEquals(160.00, stock.getOpen(), 0.01);

    stock.setHigh(165.00);
    assertEquals(165.00, stock.getHigh(), 0.01);

    stock.setLow(159.00);
    assertEquals(159.00, stock.getLow(), 0.01);

    stock.setClose(164.00);
    assertEquals(164.00, stock.getClose(), 0.01);

    stock.setVolume(2000000);
    assertEquals(2000000, stock.getVolume());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetInvalidOpenPrice() {
    MockStock stock = new MockStock("AAPL", "2024-06-07", 150.00,
            155.00, 149.00, 154.00, 1000000);
    stock.setOpen(-150.00);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetInvalidHighPrice() {
    MockStock stock = new MockStock("AAPL", "2024-06-07", 150.00,
            155.00, 149.00, 154.00, 1000000);
    stock.setHigh(-155.00);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetInvalidLowPrice() {
    MockStock stock = new MockStock("AAPL", "2024-06-07", 150.00,
            155.00, 149.00, 154.00, 1000000);
    stock.setLow(-149.00);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetInvalidClosePrice() {
    MockStock stock = new MockStock("AAPL", "2024-06-07", 150.00,
            155.00, 149.00, 154.00, 1000000);
    stock.setClose(-154.00);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetInvalidVolume() {
    MockStock stock = new MockStock("AAPL", "2024-06-07", 150.00,
            155.00, 149.00, 154.00, 1000000);
    stock.setVolume(-1000000);
  }

  @Test
  public void testSetZeroVolume() {
    MockStock stock = new MockStock("AAPL", "2024-06-07", 150.00,
            155.00, 149.00, 154.00, 1000000);
    stock.setVolume(0);
    assertEquals(0, stock.getVolume());
  }

  @Test
  public void testSetZeroPrices() {
    MockStock stock = new MockStock("AAPL", "2024-06-07", 150.00,
            155.00, 149.00, 154.00, 1000000);
    stock.setOpen(0);
    stock.setHigh(0);
    stock.setLow(0);
    stock.setClose(0);

    assertEquals(0, stock.getOpen(), 0.01);
    assertEquals(0, stock.getHigh(), 0.01);
    assertEquals(0, stock.getLow(), 0.01);
    assertEquals(0, stock.getClose(), 0.01);
  }

  @Test
  public void testInvalidDateFormats() {
    MockStock stock = new MockStock("AAPL", "2024-06-07", 150.00,
            155.00, 149.00, 154.00, 1000000);
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      stock.setTimestamp("invalid-date");
    });
    assertEquals("Invalid date format", thrown.getMessage());
  }
}
