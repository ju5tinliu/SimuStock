package model;

import java.util.HashMap;
import java.util.Map;

import controller.commands.IAlphaVantageController;
import controller.commands.IPortfolioController;

/**
 * The PortfolioRebalancer class is responsible for rebalancing a portfolio
 * based on the target weights for each stock.
 * It interacts with the portfolio controller and AlphaVantage controller
 * to fetch stock data and perform the necessary operations to achieve the desired balance.
 */
public class PortfolioRebalancer {

  private IPortfolioController portfolioController;
  private IAlphaVantageController alphaVantageController;

  /**
   * Constructs a PortfolioRebalancer with the specified portfolio and AlphaVantage controllers.
   *
   * @param portfolioController the portfolio controller to manage portfolio operations
   * @param alphaVantageController the AlphaVantage controller to fetch stock data
   * @throws IllegalArgumentException if any of the controllers are null
   */
  public PortfolioRebalancer(IPortfolioController portfolioController,
                             IAlphaVantageController alphaVantageController) {
    if (portfolioController == null || alphaVantageController == null) {
      throw new IllegalArgumentException("Controllers cannot be null");
    }
    this.portfolioController = portfolioController;
    this.alphaVantageController = alphaVantageController;
  }

  /**
   * Rebalances the specified portfolio based on the target weights for each stock
   * as of the specified date.
   *
   * @param portfolio the portfolio to be rebalanced
   * @param targetWeights a map containing the target weights for each stock
   * @param date the date on which the rebalancing should be calculated
   */
  public void rebalancePortfolio(Portfolio portfolio, Map<String, Double> targetWeights,
                                 String date) {
    Map<String, Double> currentStocks = portfolio.getStocks();
    double totalValue = portfolioController.calculatePortfolioValue(portfolio, date);

    Map<String, Double> currentValues = new HashMap<>();
    for (String stockSymbol : currentStocks.keySet()) {
      double stockPrice = this.alphaVantageController.fetchStockData(stockSymbol).stream()
              .filter(stock -> stock.getTimestamp().equals(date))
              .findFirst()
              .map(Stock::getClose)
              .orElse(0.0);
      double stockValue = stockPrice * currentStocks.get(stockSymbol);
      currentValues.put(stockSymbol, stockValue);
    }

    Map<String, Double> targetValues = new HashMap<>();
    for (String stockSymbol : targetWeights.keySet()) {
      double targetValue = totalValue * targetWeights.get(stockSymbol);
      targetValues.put(stockSymbol, targetValue);
    }

    Map<String, Double> differences = new HashMap<>();
    for (String stockSymbol : currentValues.keySet()) {
      double difference = targetValues.get(stockSymbol) - currentValues.get(stockSymbol);
      differences.put(stockSymbol, difference);
    }

    for (String stockSymbol : differences.keySet()) {
      double difference = differences.get(stockSymbol);
      double stockPrice = this.alphaVantageController.fetchStockData(stockSymbol).stream()
              .filter(stock -> stock.getTimestamp().equals(date))
              .findFirst()
              .map(Stock::getClose)
              .orElse(0.0);
      double shares = difference / stockPrice;
      shares = Math.round(shares * 10000.0) / 10000.0;

      if (shares > 0) {
        portfolioController.addStockToPortfolio(portfolio, stockSymbol, shares, date);
      } else if (shares < 0) {
        portfolioController.removeStockFromPortfolio(portfolio, stockSymbol, -shares, date);
      }
    }
  }
}
