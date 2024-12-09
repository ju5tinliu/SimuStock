package model;

/**
 * The IStock interface defines the methods required for a stock object.
 * Implementations of this interface provides the necessary methods to get and set
 * stock attributes such as symbol, timestamp, open, high, low, close, and volume.
 */
public interface IStock {

  /**
   * Gets the stock symbol.
   */
  String getSymbol();

  /**
   * Sets the stock symbol.
   */
  void setSymbol(String symbol);

  /**
   * Gets the timestamp of the stock data.
   */
  String getTimestamp();

  /**
   * Sets the timestamp of the stock data.
   */
  void setTimestamp(String timestamp);

  /**
   * Gets the opening price of the stock.
   */
  double getOpen();

  /**
   * Sets the opening price of the stock.
   */
  void setOpen(double open);

  /**
   * Gets the highest price of the stock.
   */
  double getHigh();

  /**
   * Sets the highest price of the stock.
   */
  void setHigh(double high);

  /**
   * Gets the lowest price of the stock.
   */
  double getLow();

  /**
   * Sets the lowest price of the stock.
   */
  void setLow(double low);

  /**
   * Gets the closing price of the stock.
   */
  double getClose();

  /**
   * Sets the closing price of the stock.
   */
  void setClose(double close);

  /**
   * Gets the volume of the stock traded.
   */
  int getVolume();

  /**
   * Sets the volume of the stock traded.
   */
  void setVolume(int volume);
}
