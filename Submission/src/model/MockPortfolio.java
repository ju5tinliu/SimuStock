package model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
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
 * This class implements the IPortfolio interface to represent
 * a mock portfolio of stocks. This class provides methods to add, remove, and retrieve stocks
 * in the portfolio.
 */
public class MockPortfolio implements IPortfolio {
  private String name;
  private Map<String, Double> stocks;
  private Map<String, TreeMap<String, Double>> transactionHistory;

  /**
   * Constructs a new MockPortfolio with a given name and an empty stock portfolio.
   *
   * @param name the name of the portfolio
   */
  public MockPortfolio(String name) {
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
   * Adds the specified quantity of the given stock symbol to the portfolio on a given date.
   * If the stock symbol already exists in the portfolio, the quantity is
   * increased by the specified amount.
   *
   * @param symbol   the stock symbol to add
   * @param quantity the quantity of the stock to add
   * @param date     the date of the transaction
   * @throws IllegalArgumentException if the quantity is negative
   */
  @Override
  public void addStock(String symbol, double quantity, String date) {
    if (quantity < 0) {
      throw new IllegalArgumentException("Quantity cannot be negative");
    }
    stocks.put(symbol, stocks.getOrDefault(symbol, 0.0) + quantity);
    transactionHistory.computeIfAbsent(symbol, k -> new TreeMap<>()).put(date, quantity);
    try {
      saveToFile(new File(name + ".xml"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Removes the specified quantity of the given stock symbol from the portfolio on a given date.
   * If the resulting quantity is less than or equal to zero, the stock symbol is
   * removed from the portfolio.
   *
   * @param symbol   the stock symbol to remove
   * @param quantity the quantity of the stock to remove
   * @param date     the date of the transaction
   * @throws IllegalArgumentException if the quantity is negative or more than exists
   */
  @Override
  public void removeStock(String symbol, double quantity, String date) {
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
    try {
      saveToFile(new File(name + ".xml"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Retrieves the current stocks in the portfolio.
   *
   * @return a map of stock symbols and their corresponding quantities in the portfolio
   */
  @Override
  public Map<String, Double> getStocks() {
    return stocks;
  }

  /**
   * Retrieves the composition of the portfolio on a specific date.
   *
   * @param date the date to get the composition for
   * @return a map of stock symbols and their corresponding quantities on the specified date
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

      NodeList nList = doc.getElementsByTagName("Stock");
      for (int temp = 0; temp < nList.getLength(); temp++) {
        Element stockElement = (Element) nList.item(temp);
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
    try {
      saveToFile(new File(name + ".xml"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Returns the most recent transaction date for a given stock symbol.
   *
   * @param symbol the stock symbol
   * @return the most recent transaction date
   */
  public String getTransactionDate(String symbol) {
    return transactionHistory.containsKey(symbol) ? transactionHistory.get(symbol)
            .lastEntry().getKey() : null;
  }

  /**
   * Returns the portfolio values over time between two dates.
   *
   * @param startDate               the start date in the format "yyyy-MM-dd"
   * @param endDate                 the end date in the format "yyyy-MM-dd"
   * @param alphaVantageControllerImpl the AlphaVantage controller to fetch stock data
   * @return a map of dates to portfolio values
   */
  @Override
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
        double stockPrice = 100.0; // Mock price
        value += stockPrice * quantity;
      }
      values.put(dateString, value);
    }
    return values;
  }
}
