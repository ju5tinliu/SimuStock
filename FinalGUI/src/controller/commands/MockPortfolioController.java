package controller.commands;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import model.Portfolio;

/**
 * This class implements the IPortfolioController interface to provide mock implementations
 * for managing portfolios.
 */
public abstract class MockPortfolioController implements IPortfolioController {
  private Map<String, Portfolio> portfolios = new HashMap<>();

  /**
   * Adds a specified quantity of a stock to the given portfolio on a specified date.
   * Updates the portfolio and saves it to a file.
   *
   * @param portfolio the portfolio to update
   * @param symbol the stock symbol to add
   * @param quantity the quantity of the stock to add
   * @param date the date of the transaction
   */
  @Override
  public void addStockToPortfolio(Portfolio portfolio, String symbol,
                                  double quantity, String date) {
    portfolio.addStock(symbol, quantity, date);
    portfolios.put(portfolio.getName(), portfolio);
    try {
      savePortfolioToFile(portfolio, new File(portfolio.getName() + ".xml"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Removes a specified quantity of a stock from the given portfolio on a specified date.
   * Updates the portfolio and saves it to a file.
   *
   * @param portfolio the portfolio to update
   * @param symbol the stock symbol to remove
   * @param quantity the quantity of the stock to remove
   * @param date the date of the transaction
   */
  @Override
  public void removeStockFromPortfolio(Portfolio portfolio, String symbol,
                                       double quantity, String date) {
    portfolio.removeStock(symbol, quantity, date);
    portfolios.put(portfolio.getName(), portfolio);
    try {
      savePortfolioToFile(portfolio, new File(portfolio.getName() + ".xml"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Calculates the total value of the given portfolio on a specified date.
   * Uses a mock stock price for calculations.
   *
   * @param portfolio the portfolio to calculate the value for
   * @param date the date to calculate the portfolio value
   * @return the total value of the portfolio
   */
  @Override
  public double calculatePortfolioValue(Portfolio portfolio, String date) {
    double totalValue = 0.0;
    for (Map.Entry<String, Double> entry : portfolio.getStocks().entrySet()) {
      String stockSymbol = entry.getKey();
      double quantity = entry.getValue();
      double stockPrice = getMockStockPrice(stockSymbol, date);
      totalValue += stockPrice * quantity;
    }
    return totalValue;
  }

  /**
   * Returns a mock stock price for a given stock symbol on a specified date.
   *
   * @param stockSymbol the stock symbol
   * @param date the date
   * @return the mock stock price
   */
  private double getMockStockPrice(String stockSymbol, String date) {
    return 100.0;
  }

  /**
   * Retrieves the composition of the given portfolio on a specified date.
   *
   * @param portfolio the portfolio to retrieve the composition for
   * @param date the date to get the composition
   * @return a map of stock symbols to their quantities on the specified date
   */
  @Override
  public Map<String, Double> getPortfolioCompositionOnDate(Portfolio portfolio, String date) {
    return portfolio.getCompositionOnDate(date);
  }

  /**
   * Saves the given portfolio to an XML file.
   *
   * @param portfolio the portfolio to save
   * @param file the file to save the portfolio to
   * @throws IOException if an I/O error occurs during saving
   */
  @Override
  public void savePortfolioToFile(Portfolio portfolio, File file) throws IOException {
    portfolio.saveToFile(file);
  }

  /**
   * Loads a portfolio from an XML file.
   *
   * @param file the file to load the portfolio from
   * @return the loaded portfolio
   * @throws IOException if an I/O error occurs during loading
   */
  @Override
  public Portfolio loadPortfolioFromFile(File file) throws IOException {
    Portfolio portfolio = new Portfolio("Loaded Portfolio");
    portfolio.loadFromFile(file);
    portfolios.put(portfolio.getName(), portfolio);
    return portfolio;
  }

  /**
   * Clears all stocks and transaction history from the given portfolio.
   * Updates the portfolio and saves it to a file.
   *
   * @param portfolio the portfolio to clear
   */
  @Override
  public void clearPortfolio(Portfolio portfolio) {
    portfolio.clearPortfolio();
    portfolios.put(portfolio.getName(), portfolio);
    try {
      savePortfolioToFile(portfolio, new File(portfolio.getName() + ".xml"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Saves all portfolios to a single XML file.
   *
   * @param file the file to save all portfolios to
   * @throws IOException if an I/O error occurs during saving
   */
  @Override
  public void saveAllPortfoliosToFile(File file) throws IOException {
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.newDocument();

      Element rootElement = doc.createElement("Portfolios");
      doc.appendChild(rootElement);

      for (Portfolio portfolio : portfolios.values()) {
        Element portfolioElement = doc.createElement("Portfolio");
        portfolioElement.setAttribute("name", portfolio.getName());
        rootElement.appendChild(portfolioElement);

        for (Map.Entry<String, Double> entry : portfolio.getStocks().entrySet()) {
          String symbol = entry.getKey();
          double quantity = entry.getValue();
          String date = portfolio.getTransactionDate(symbol);

          Element stockElement = doc.createElement("Stock");
          stockElement.setAttribute("symbol", symbol);
          stockElement.setAttribute("quantity", String.valueOf(quantity));
          stockElement.setAttribute("date", date);
          portfolioElement.appendChild(stockElement);
        }
      }

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(file);
      transformer.transform(source, result);

    } catch (ParserConfigurationException | javax.xml.transform.TransformerException e) {
      throw new IOException("Error saving portfolios to file", e);
    }
  }

  /**
   * Loads all portfolios from a single XML file.
   *
   * @param file the file to load all portfolios from
   * @throws IOException if an I/O error occurs during loading
   */
  @Override
  public void loadAllPortfoliosFromFile(File file) throws IOException {
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(file);
      doc.getDocumentElement().normalize();

      NodeList portfolioNodes = doc.getElementsByTagName("Portfolio");
      for (int i = 0; i < portfolioNodes.getLength(); i++) {
        Element portfolioElement = (Element) portfolioNodes.item(i);
        String portfolioName = portfolioElement.getAttribute("name");
        Portfolio portfolio = new Portfolio(portfolioName);

        NodeList stockNodes = portfolioElement.getElementsByTagName("Stock");
        for (int j = 0; j < stockNodes.getLength(); j++) {
          Element stockElement = (Element) stockNodes.item(j);
          String symbol = stockElement.getAttribute("symbol");
          double quantity = Double.parseDouble(stockElement.getAttribute("quantity"));
          String date = stockElement.getAttribute("date");
          portfolio.addStock(symbol, quantity, date);
        }

        portfolios.put(portfolioName, portfolio);
      }

    } catch (Exception e) {
      throw new IOException("Error loading portfolios from file", e);
    }
  }

  /**
   * Adds a new portfolio to the controller.
   *
   * @param portfolio the portfolio to add
   */
  @Override
  public void addPortfolio(Portfolio portfolio) {
    portfolios.put(portfolio.getName(), portfolio);
  }

  /**
   * Retrieves a portfolio by its name.
   *
   * @param name the name of the portfolio to retrieve
   * @return the portfolio with the specified name, or null if not found
   */
  @Override
  public Portfolio getPortfolio(String name) {
    return portfolios.get(name);
  }

  /**
   * Removes a portfolio by its name.
   *
   * @param name the name of the portfolio to remove
   */
  @Override
  public void removePortfolio(String name) {
    portfolios.remove(name);
  }

  /**
   * Retrieves all portfolios managed by the controller.
   *
   * @return a map of portfolio names to portfolios
   */
  @Override
  public Map<String, Portfolio> getAllPortfolios() {
    return portfolios;
  }

  /**
   * Retrieves the value distribution of the given portfolio on a specified date.
   *
   * @param portfolio the portfolio to get the value distribution for
   * @param date the date to get the value distribution
   * @return a map of stock symbols to their values on the specified date
   */
  @Override
  public Map<String, Double> getPortfolioValueDistribution(Portfolio portfolio, String date) {
    Map<String, Double> distribution = new HashMap<>();
    Map<String, Double> composition = portfolio.getCompositionOnDate(date);
    for (Map.Entry<String, Double> entry : composition.entrySet()) {
      String symbol = entry.getKey();
      double quantity = entry.getValue();
      double stockPrice = getMockStockPrice(symbol, date);
      double value = stockPrice * quantity;
      distribution.put(symbol, value);
    }
    return distribution;
  }

  /**
   * Draws a bar chart representing the performance of the given portfolio
   * over a specified date range.
   * The chart is displayed as a series of asterisks in the console.
   *
   * @param portfolio the portfolio to draw the performance chart for
   * @param startDate the start date of the date range
   * @param endDate the end date of the date range
   */
  @Override
  public void drawPortfolioPerformanceBarChart(Portfolio portfolio,
                                               String startDate, String endDate) {
    Map<String, Double> values = portfolio.getPortfolioValuesOverTime(
            startDate, endDate, null);
    if (values.isEmpty()) {
      System.out.println("No data available for the specified range.");
      return;
    }

    double maxValue = values.values().stream().mapToDouble(v -> v).max().orElse(1.0);
    int scaleFactor = (int) Math.ceil(maxValue / 50); // Scale to fit within 50 asterisks

    System.out.println("Performance of portfolio " + portfolio.getName() + " from "
            + startDate + " to " + endDate);
    for (Map.Entry<String, Double> entry : values.entrySet()) {
      String date = entry.getKey();
      double value = entry.getValue();
      int asterisks = (int) (value / scaleFactor);
      System.out.println(date + ": " + "*".repeat(asterisks));
    }
    System.out.println("Scale: * = " + scaleFactor + " units");
  }
}
