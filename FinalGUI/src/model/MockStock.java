package model;

/**
 * This class implements the IStock interface to represent
 * a mock stock with attributes such as symbol, timestamp, open, high, low, close, and volume.
 * This class provides methods to get and set these attributes.
 */
public class MockStock implements IStock {
  private String symbol;
  private String timestamp;
  private double open;
  private double high;
  private double low;
  private double close;
  private int volume;

  /**
   * Constructs a new MockStock with the specified attributes.
   *
   * @param symbol    the stock symbol
   * @param timestamp the timestamp of the stock data
   * @param open      the opening price of the stock
   * @param high      the highest price of the stock
   * @param low       the lowest price of the stock
   * @param close     the closing price of the stock
   * @param volume    the volume of the stock traded
   */
  public MockStock(String symbol, String timestamp, double open,
                   double high, double low, double close, int volume) {
    this.symbol = symbol;
    this.timestamp = timestamp;
    this.open = open;
    this.high = high;
    this.low = low;
    this.close = close;
    this.volume = volume;
  }

  /**
   * Gets the stock symbol.
   */
  @Override
  public String getSymbol() {
    return symbol;
  }

  /**
   * Sets the symbol.
   */
  @Override
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  /**
   * Gets the timestamp of the stock data.
   */
  @Override
  public String getTimestamp() {
    return timestamp;
  }

  /**
   * Sets the timestamp of the stock data.
   */
  @Override
  public void setTimestamp(String timestamp) {
    if (!isValidDate(timestamp)) {
      throw new IllegalArgumentException("Invalid date format");
    }
    this.timestamp = timestamp;
  }

  /**
   * Checks if the date inputted is valid.
   */
  private boolean isValidDate(String date) {
    // Regular expression to match the format yyyy-MM-dd
    String regex = "^(\\d{4})-(\\d{2})-(\\d{2})$";
    return date.matches(regex);
  }

  /**
   * Gets the opening price of the stock.
   */
  @Override
  public double getOpen() {
    return open;
  }

  /**
   * Sets the opening price of the stock.
   */
  @Override
  public void setOpen(double open) {
    if (open < 0) {
      throw new IllegalArgumentException("Open price cannot be negative");
    }
    this.open = open;
  }

  /**
   * Gets the highest price of the stock.
   */
  @Override
  public double getHigh() {
    return high;
  }

  /**
   * Sets the highest price of the stock.
   */
  @Override
  public void setHigh(double high) {
    if (high < 0) {
      throw new IllegalArgumentException("High price cannot be negative");
    }
    this.high = high;
  }

  /**
   * Gets the lowest price of the stock.
   */
  @Override
  public double getLow() {
    return low;
  }

  /**
   * Sets the lowest price of the stock.
   */
  @Override
  public void setLow(double low) {
    if (low < 0) {
      throw new IllegalArgumentException("Low price cannot be negative");
    }
    this.low = low;
  }

  /**
   * Gets the closing price of the stock.
   */
  @Override
  public double getClose() {
    return close;
  }


  /**
   * Sets the closing price of the stock.
   */
  @Override
  public void setClose(double close) {
    if (close < 0) {
      throw new IllegalArgumentException("Close price cannot be negative");
    }
    this.close = close;
  }

  /**
   * Gets the trading volume of the stock.
   */
  @Override
  public int getVolume() {
    return volume;
  }

  /**
   * Sets the trading volume of the stock.
   */
  @Override
  public void setVolume(int volume) {
    if (volume < 0) {
      throw new IllegalArgumentException("Volume cannot be negative");
    }
    this.volume = volume;
  }


}
