package model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import controller.commands.AlphaVantageControllerImpl;

/**
 * The class to represent the collection of stocks.
 * It allows adding/removing stocks and keeps track of the
 * quantities of each stock.
 */
public class Portfolio implements IPortfolio {
  private String name;
  private Map<String, Double> stocks; // Stock symbol and quantity
  private Map<String, TreeMap<String, Double>> transactionHistory;
  private static final List<String> VALID_STOCK_SYMBOLS = Arrays.asList("AAPL",
          "GOOG", "MSFT", "TSLA", "AMZN");

  /**
   * Constructs a new portfolio with a given name.
   *
   * @param name name of the portfolio
   */
  public Portfolio(String name) {
    this.name = name;
    this.stocks = new HashMap<>();
    this.transactionHistory = new HashMap<>();
  }

  /**
   * Returns the name of the portfolio.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the stocks in the portfolio as well as their quantities.
   *
   * @return a map of stock symbols to their quantities
   */
  @Override
  public Map<String, Double> getStocks() {
    return stocks;
  }

  private void validateStockSymbol(String symbol) throws IllegalArgumentException {
    if (!VALID_STOCK_SYMBOLS.contains(symbol)) {
      throw new IllegalArgumentException("Invalid stock symbol: " + symbol);
    }
  }

  /**
   * Adds a specified quantity of a stock to the portfolio on a given date.
   * If the stock is already in the portfolio, the quantity is increased by the specified amount.
   * This method was enhanced to include the date of the transaction for better tracking and
   * historical data purposes.
   *
   * @param symbol   the stock symbol
   * @param quantity the amount of stock to add
   * @param date     the date of the transaction
   */
  @Override
  public void addStock(String symbol, double quantity, String date) {
    validateStockSymbol(symbol);
    if (quantity < 0) {
      throw new IllegalArgumentException("Quantity cannot be negative");
    }
    stocks.put(symbol, stocks.getOrDefault(symbol, 0.0) + quantity);
    transactionHistory.computeIfAbsent(symbol, k -> new TreeMap<>()).put(date, quantity);
  }

  /**
   * Removes the specified quantity of stock from the portfolio on a given date.
   * If the quantity removed is greater than or equal to the current quantity,
   * the stock is removed from the portfolio. Otherwise, the stock's quantity
   * is just decreased by the specified amount.
   * This method was enhanced to include the date of the transaction for better tracking and
   * historical data purposes.
   *
   * @param symbol   the stock symbol
   * @param quantity the amount of stock to remove
   * @param date     the date of the transaction
   */
  @Override
  public void removeStock(String symbol, double quantity, String date) {
    validateStockSymbol(symbol);
    if (quantity < 0) {
      throw new IllegalArgumentException("Quantity cannot be negative");
    }
    if (!stocks.containsKey(symbol) || stocks.get(symbol) < quantity) {
      throw new IllegalArgumentException("Not enough stock to remove");
    }
    double currentQuantity = stocks.get(symbol);
    if (currentQuantity <= quantity) {
      stocks.remove(symbol);
      transactionHistory.get(symbol).put(date, -quantity);
    } else {
      stocks.put(symbol, currentQuantity - quantity);
      transactionHistory.get(symbol).put(date, -quantity);
    }
  }

  /**
   * Returns the composition of the portfolio on a specific date.
   *
   * @param date the date for which the composition is requested
   * @return a map of stock symbols to their quantities on the given date
   */
  @Override
  public Map<String, Double> getCompositionOnDate(String date) {
    Map<String, Double> composition = new HashMap<>();
    for (String symbol : transactionHistory.keySet()) {
      double cumulativeQuantity = 0;
      for (Map.Entry<String, Double> entry : transactionHistory.get(symbol).entrySet()) {
        if (entry.getKey().compareTo(date) <= 0) {
          cumulativeQuantity += entry.getValue();
        } else {
          break;
        }
      }
      if (cumulativeQuantity > 0) {
        composition.put(symbol, cumulativeQuantity);
      }
    }
    return composition;
  }

  /**
   * Saves the portfolio to an XML file.
   *
   * @param file the file to which the portfolio should be saved
   * @throws IOException if there is an error during the saving process
   */
  @Override
  public void saveToFile(File file) throws IOException {
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.newDocument();

      Element rootElement = doc.createElement("Portfolio");
      rootElement.setAttribute("name", name);
      doc.appendChild(rootElement);

      for (Map.Entry<String, Double> entry : stocks.entrySet()) {
        String symbol = entry.getKey();
        double quantity = entry.getValue();
        String date = transactionHistory.get(symbol).lastEntry().getKey();

        Element stockElement = doc.createElement("Stock");
        stockElement.setAttribute("symbol", symbol);
        stockElement.setAttribute("quantity", String.valueOf(quantity));
        stockElement.setAttribute("date", date);
        rootElement.appendChild(stockElement);
      }

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(file);
      transformer.transform(source, result);

    } catch (ParserConfigurationException | javax.xml.transform.TransformerException e) {
      throw new IOException("Error saving portfolio to file", e);
    }
  }

  /**
   * Loads the portfolio from an XML file.
   *
   * @param file the file from which the portfolio should be loaded
   * @throws IOException if there is an error during the loading process
   */
  @Override
  public void loadFromFile(File file) throws IOException {
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(file);
      doc.getDocumentElement().normalize();

      name = doc.getDocumentElement().getAttribute("name");

      NodeList stockNodes = doc.getElementsByTagName("Stock");
      for (int i = 0; i < stockNodes.getLength(); i++) {
        Element stockElement = (Element) stockNodes.item(i);
        String symbol = stockElement.getAttribute("symbol");
        double quantity = Double.parseDouble(stockElement.getAttribute("quantity"));
        String date = stockElement.getAttribute("date");
        stocks.put(symbol, quantity);
        transactionHistory.computeIfAbsent(symbol, k -> new TreeMap<>()).put(date, quantity);
      }

    } catch (Exception e) {
      throw new IOException("Error loading portfolio from file", e);
    }
  }

  /**
   * Clears all stocks and transaction history from the portfolio.
   */
  @Override
  public void clearPortfolio() {
    stocks.clear();
    transactionHistory.clear();
  }

  /**
   * Returns the most recent transaction date for a given stock symbol.
   *
   * @param symbol the stock symbol
   * @return the most recent transaction date
   */
  @Override
  public String getTransactionDate(String symbol) {
    validateStockSymbol(symbol);
    return transactionHistory.containsKey(symbol) ? transactionHistory.get(symbol).lastEntry()
            .getKey() : null;
  }

  /**
   * Returns the portfolio values over time between two dates.
   *
   * @param startDate               the start date in the format "yyyy-MM-dd"
   * @param endDate                 the end date in the format "yyyy-MM-dd"
   * @param alphaVantageControllerImpl the AlphaVantage controller to fetch stock data
   * @return a map of dates to portfolio values
   */
  public Map<String, Double> getPortfolioValuesOverTime(String startDate,
                                                        String endDate,
                                                        AlphaVantageControllerImpl
                                                                alphaVantageControllerImpl) {
    Map<String, Double> values = new HashMap<>();
    LocalDate start = LocalDate.parse(startDate);
    LocalDate end = LocalDate.parse(endDate);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
      String dateString = date.format(formatter);
      double value = 0.0;
      Map<String, Double> composition = getCompositionOnDate(dateString);
      for (Map.Entry<String, Double> entry : composition.entrySet()) {
        String symbol = entry.getKey();
        double quantity = entry.getValue();
        List<Stock> stockData = alphaVantageControllerImpl.fetchStockData(symbol);
        for (Stock stock : stockData) {
          if (stock.getTimestamp().equals(dateString)) {
            value += stock.getClose() * quantity;
            break;
          }
        }
      }
      values.put(dateString, value);
    }
    return values;
  }
}
