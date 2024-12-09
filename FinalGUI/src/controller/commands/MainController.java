package controller.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import controller.commands.AlphaVantageControllerImpl;
import controller.commands.IPortfolioController;
import controller.commands.IStockController;
import controller.commands.PortfolioControllerImpl;
import controller.commands.StockControllerImpl;
import model.Portfolio;
import model.PortfolioRebalancer;
import model.Stock;
import view.ConsoleView;
import view.IConsoleView;

/**
 * This class provides the main part of the application where it manages
 * the user interactions through the console view and delegates tasks to the
 * appropriate controllers.
 */
public class MainController {

  private static final String PORTFOLIOS_DIRECTORY = "portfolios";

  /**
   * The main method that initializes the controllers, views, and starts the
   * console-based user interaction loop.
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    ConsoleView view = new ConsoleView();
    StockControllerImpl stockControllerImpl = new StockControllerImpl();
    PortfolioControllerImpl portfolioControllerImpl = new PortfolioControllerImpl();
    AlphaVantageControllerImpl alphaVantageControllerImpl = new AlphaVantageControllerImpl();
    List<Stock> stockData = new ArrayList<>();
    List<Portfolio> portfolios = new ArrayList<>();
    String symbol;
    String startDate;
    String endDate;
    String date;
    String portfolioName;
    double quantity;
    int x;

    IPortfolioController portfolioController = new PortfolioControllerImpl();
    IStockController stockController = new StockControllerImpl();
    IConsoleView consoleView = new ConsoleView();

    Scanner scanner = new Scanner(System.in);
    boolean exit = false;

    // Ensure the portfolios directory exists
    Path portfoliosDir = Paths.get(PORTFOLIOS_DIRECTORY);
    if (!Files.exists(portfoliosDir)) {
      try {
        Files.createDirectory(portfoliosDir);
      } catch (IOException e) {
        view.displayMessage("Error creating portfolios directory: " + e.getMessage());
        return; // Exit the application if the directory cannot be created
      }
    }

    try {
      portfolioControllerImpl.loadAllPortfoliosFromDirectory(PORTFOLIOS_DIRECTORY);
      portfolios.addAll(portfolioControllerImpl.getAllPortfolios().values());
    } catch (IOException e) {
      view.displayMessage("Error loading portfolios: " + e.getMessage());
    }

    boolean running = true;
    while (running) {
      view.displayMenu();
      int choice = view.getUserChoice();

      switch (choice) {
        case 1:
          // Handle view stock gain/loss
          symbol = view.getInputSymbol("Enter stock symbol: ");
          stockData = alphaVantageControllerImpl.fetchStockData(symbol);
          startDate = view.getInput("Enter start date (YYYY-MM-DD): ");
          endDate = view.getInput("Enter end date (YYYY-MM-DD): ");
          double gainLoss = stockControllerImpl.calculateGainLoss(stockData, startDate, endDate);
          view.displayMessage("Gain/Loss: " + gainLoss);
          break;
        case 2:
          // Handle view x-day moving average
          symbol = view.getInputSymbol("Enter stock symbol: ");
          stockData = alphaVantageControllerImpl.fetchStockData(symbol);
          date = view.getInput("Enter date (YYYY-MM-DD): ");
          x = Integer.parseInt(view.getInput("Enter value of x: "));
          double movingAverage = stockControllerImpl.calculateMovingAverage(stockData, date, x);
          view.displayMessage("x-Day Moving Average: " + movingAverage);
          break;
        case 3:
          // Handle view x-day crossovers
          symbol = view.getInputSymbol("Enter stock symbol: ");
          stockData = alphaVantageControllerImpl.fetchStockData(symbol);
          startDate = view.getInput("Enter start date (YYYY-MM-DD): ");
          endDate = view.getInput("Enter end date (YYYY-MM-DD): ");
          x = Integer.parseInt(view.getInput("Enter value of x: "));
          List<String> crossovers = stockControllerImpl.findCrossovers(stockData,
                  startDate, endDate, x);
          view.displayMessage("x-Day Crossovers: " + crossovers);
          break;
        case 4:
          // Handle create portfolio
          portfolioName = view.getInput("Enter portfolio name: ");
          Portfolio portfolio = new Portfolio(portfolioName);
          portfolios.add(portfolio);
          try {
            portfolioControllerImpl.savePortfolioToFile(portfolio,
                    portfoliosDir.resolve(portfolioName + ".xml").toFile());
            view.displayMessage("Portfolio created and saved: " + portfolioName);
          } catch (IOException e) {
            view.displayMessage("Error saving portfolio: " + e.getMessage());
          }
          break;
        case 5:
          // Handle view portfolio value
          view.displayMessage("Existing Portfolios: " + getPortfolioNames(portfolios));
          portfolioName = view.getInput("Enter portfolio name: ");
          date = view.getInput("Enter date (YYYY-MM-DD): ");
          Portfolio selectedPortfolio = portfolioControllerImpl.getPortfolio(portfolioName);
          if (selectedPortfolio != null) {
            double portfolioValue = portfolioControllerImpl
                    .calculatePortfolioValue(selectedPortfolio, date);
            portfolioValue = Math.round(portfolioValue * 100.0) / 100.0;
            view.displayMessage("Portfolio Value: $" + portfolioValue);
          } else {
            view.displayMessage("Portfolio not found.");
          }
          break;
        case 6:
          // Handle add stock to portfolio
          view.displayMessage("Existing Portfolios: " + getPortfolioNames(portfolios));
          portfolioName = view.getInput("Enter portfolio name: ");
          selectedPortfolio = portfolioControllerImpl.getPortfolio(portfolioName);
          if (selectedPortfolio != null) {
            symbol = view.getInput("Enter stock symbol: ");
            quantity = Double.parseDouble(view.getInput("Enter quantity: "));
            date = view.getInput("Enter date (YYYY-MM-DD): ");
            portfolioControllerImpl.addStockToPortfolio(selectedPortfolio, symbol, quantity, date);
            view.displayMessage("Stock added to portfolio.");
          } else {
            view.displayMessage("Portfolio not found.");
          }
          break;
        case 7:
          // Handle remove stock from portfolio
          view.displayMessage("Existing Portfolios: " + getPortfolioNames(portfolios));
          portfolioName = view.getInput("Enter portfolio name: ");
          selectedPortfolio = portfolioControllerImpl.getPortfolio(portfolioName);
          if (selectedPortfolio != null) {
            symbol = view.getInput("Enter stock symbol: ");
            quantity = Double.parseDouble(view.getInput("Enter quantity: "));
            date = view.getInput("Enter date (YYYY-MM-DD): ");
            portfolioControllerImpl
                    .removeStockFromPortfolio(selectedPortfolio, symbol, quantity, date);
            view.displayMessage("Stock removed from portfolio.");
          } else {
            view.displayMessage("Portfolio not found.");
          }
          break;
        case 8:
          // Handle view portfolio value distribution
          view.displayMessage("Existing Portfolios: " + getPortfolioNames(portfolios));
          portfolioName = view.getInput("Enter portfolio name: ");
          date = view.getInput("Enter date (YYYY-MM-DD): ");
          selectedPortfolio = portfolioControllerImpl.getPortfolio(portfolioName);
          if (selectedPortfolio != null) {
            Map<String, Double> valueDistribution = portfolioControllerImpl
                    .getPortfolioValueDistribution(selectedPortfolio, date);
            Map<String, Double> roundedValueDistribution = new HashMap<>();
            for (Map.Entry<String, Double> entry : valueDistribution.entrySet()) {
              roundedValueDistribution.put(entry.getKey(), Math.round(entry
                      .getValue() * 100.0) / 100.0);
            }
            view.displayMessage("Portfolio Value Distribution: " + roundedValueDistribution);
          } else {
            view.displayMessage("Portfolio not found.");
          }
          break;
        case 9:
          // Handle view portfolio composition on a specific date
          view.displayMessage("Existing Portfolios: " + getPortfolioNames(portfolios));
          portfolioName = view.getInput("Enter portfolio name: ");
          date = view.getInput("Enter date (YYYY-MM-DD): ");
          selectedPortfolio = portfolioControllerImpl.getPortfolio(portfolioName);
          if (selectedPortfolio != null) {
            Map<String, Double> composition = portfolioControllerImpl
                    .getPortfolioCompositionOnDate(selectedPortfolio, date);
            Map<String, Double> roundedComposition = new HashMap<>();
            for (Map.Entry<String, Double> entry : composition.entrySet()) {
              roundedComposition.put(entry.getKey(), Math.round(entry.getValue() * 100.0) / 100.0);
            }
            view.displayMessage("Portfolio Composition on " + date + ": " + roundedComposition);
          } else {
            view.displayMessage("Portfolio not found.");
          }
          break;
        case 10:
          // Handle rebalance portfolio
          view.displayMessage("Existing Portfolios: " + getPortfolioNames(portfolios));
          handleRebalancePortfolio(portfolioControllerImpl, consoleView,
                  alphaVantageControllerImpl);
          break;
        case 11:
          // Handle clear portfolio
          view.displayMessage("Existing Portfolios: " + getPortfolioNames(portfolios));
          portfolioName = view.getInput("Enter portfolio name: ");
          selectedPortfolio = portfolioControllerImpl.getPortfolio(portfolioName);
          if (selectedPortfolio != null) {
            portfolioControllerImpl.clearPortfolio(selectedPortfolio);
            view.displayMessage("Portfolio cleared.");
          } else {
            view.displayMessage("Portfolio not found.");
          }
          break;
        case 12:
          // Handle view portfolio performance over time
          view.displayMessage("Existing Portfolios: " + getPortfolioNames(portfolios));
          portfolioName = view.getInput("Enter portfolio name: ");
          startDate = view.getInput("Enter start date (YYYY-MM-DD): ");
          endDate = view.getInput("Enter end date (YYYY-MM-DD): ");
          selectedPortfolio = portfolioControllerImpl.getPortfolio(portfolioName);
          if (selectedPortfolio != null) {
            portfolioControllerImpl
                    .drawPortfolioPerformanceBarChart(selectedPortfolio, startDate, endDate);
          } else {
            view.displayMessage("Portfolio not found.");
          }
          break;
        case 13:
          // Handle reload portfolios
          try {
            portfolioControllerImpl.loadAllPortfoliosFromDirectory(PORTFOLIOS_DIRECTORY);
            portfolios.clear();
            portfolios.addAll(portfolioControllerImpl.getAllPortfolios().values());
            view.displayMessage("Portfolios reloaded.");
          } catch (IOException e) {
            view.displayMessage("Error reloading portfolios: " + e.getMessage());
          }
          break;
        case 14:
          // Exit
          try {
            portfolioControllerImpl.saveAllPortfoliosToDirectory(PORTFOLIOS_DIRECTORY);
            view.displayMessage("All portfolios saved.");
          } catch (IOException e) {
            view.displayMessage("Error saving portfolios: " + e.getMessage());
          }
          running = false;
          view.displayMessage("Exiting...");
          break;
        default:
          view.displayMessage("Invalid choice. Please try again.");
      }
    }
    view.close();
  }

  /**
   * Retrieves the names of all portfolios in the provided list.
   *
   * @param portfolios the list of portfolios
   * @return a comma-separated string of portfolio names
   */
  private static String getPortfolioNames(List<Portfolio> portfolios) {
    if (portfolios.isEmpty()) {
      return "None";
    }
    StringBuilder sb = new StringBuilder();
    for (Portfolio portfolio : portfolios) {
      sb.append(portfolio.getName()).append(", ");
    }
    return sb.substring(0, sb.length() - 2);
  }

  /**
   * Handles the rebalancing of a portfolio by interacting with the user to get the
   * necessary inputs and delegating the rebalancing task to the PortfolioRebalancer.
   *
   * @param portfolioController the portfolio controller to manage the portfolio
   * @param consoleView the console view to interact with the user
   * @param alphaVantageController the AlphaVantage controller to fetch stock data
   */
  private static void handleRebalancePortfolio(PortfolioControllerImpl
                                                       portfolioController, IConsoleView
          consoleView, AlphaVantageControllerImpl alphaVantageController) {
    try {
      consoleView.displayMessage("Enter the name of the portfolio to rebalance:");
      String portfolioName = consoleView.getInput("Portfolio Name: ");
      Path portfolioPath = Paths.get(PORTFOLIOS_DIRECTORY, portfolioName + ".xml");

      if (!Files.exists(portfolioPath)) {
        consoleView.displayMessage("Portfolio file not found.");
        return;
      }

      Portfolio portfolio = portfolioController.loadPortfolioFromFile(portfolioPath.toFile());

      consoleView.displayMessage("Enter the date for rebalancing (YYYY-MM-DD):");
      String date = consoleView.getInput("Date: ");

      consoleView.displayMessage("Enter target weights for each stock in the portfolio:");
      Map<String, Double> targetWeights = new HashMap<>();
      double totalWeight = 0.0;

      for (String stockSymbol : portfolio.getStocks().keySet()) {
        String weightInput = consoleView.getInput(stockSymbol + " weight (in %): ");
        double weight;
        try {
          weight = Double.parseDouble(weightInput) / 100.0;
          if (weight < 0) {
            consoleView.displayMessage("Weight cannot be negative. Please try again.");
            return;
          }
          targetWeights.put(stockSymbol, weight);
          totalWeight += weight;
        } catch (NumberFormatException e) {
          consoleView.displayMessage("Invalid weight input. Please try again.");
          return;
        }
      }

      if (Math.abs(totalWeight - 1.0) > 0.01) {
        consoleView.displayMessage("Total weights must sum to 100%.");
        return;
      }

      PortfolioRebalancer rebalancer = new PortfolioRebalancer(portfolioController,
              alphaVantageController);
      rebalancer.rebalancePortfolio(portfolio, targetWeights, date);
      consoleView.displayMessage("Portfolio rebalanced successfully.");

    } catch (Exception e) {
      consoleView.displayMessage("An error occurred during rebalancing: " + e.getMessage());
    }
  }
}
