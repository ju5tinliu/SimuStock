import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import controller.commands.AlphaVantageControllerImpl;
import controller.commands.IAlphaVantageController;
import controller.commands.IPortfolioController;
import controller.commands.IStockController;
import controller.commands.MainController;
import controller.commands.PortfolioControllerImpl;
import controller.commands.StockControllerImpl;
import model.Portfolio;
import model.PortfolioRebalancer;
import model.Stock;

/**
 * The StockTradingAppGUI class represents the graphical user interface for the Stock Trading
 * Application. It provides various functionalities for managing and analyzing stock portfolios.
 */

public class StockTradingAppGUI extends JFrame {

  private IStockController stockController;
  private IPortfolioController portfolioController;
  private IAlphaVantageController alphaVantageController;
  private List<Portfolio> portfolios;

  /**
   * Constructs a StockTradingAppGUI object.
   *
   * @param stockController        the stock controller
   * @param portfolioController    the portfolio controller
   * @param alphaVantageController the AlphaVantage controller
   * @param portfolios             the list of portfolios
   */
  public StockTradingAppGUI(IStockController stockController,
                            IPortfolioController portfolioController,
                            IAlphaVantageController alphaVantageController,
                            List<Portfolio> portfolios) {
    this.stockController = stockController;
    this.portfolioController = portfolioController;
    this.alphaVantageController = alphaVantageController;
    this.portfolios = portfolios;

    setTitle("Stock Trading Application");
    setSize(800, 700);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    // Main panel to hold the components
    JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

    // Title label
    JLabel titleLabel = new JLabel("Stock Simulation", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
    mainPanel.add(titleLabel, BorderLayout.NORTH);

    // Panel to hold buttons on the left
    JPanel buttonPanel = new JPanel(new GridLayout(14, 1, 10, 10));

    // Create buttons for each option
    String[] options = {
      "View Stock Gain/Loss",
      "View x-Day Moving Average",
      "View x-Day Crossovers",
      "Create Portfolio",
      "View Portfolio Value",
      "Add Stock",
      "Remove Stock",
      "View Portfolio Value Distribution",
      "Determine Portfolio Composition on Date", "Rebalance Portfolio",
      "Clear Portfolio",
      "Update Portfolio (Click this button first before making changes)",
      "Exit"
    };

    for (String option : options) {
      JButton button = new JButton(option);
      button.addActionListener(new MenuButtonListener());
      buttonPanel.add(button);
    }

    // Add the button panel to the main panel
    mainPanel.add(buttonPanel, BorderLayout.WEST);

    // Panel for the image and password fields
    JPanel rightPanel = new JPanel();
    rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

    JPanel imagePanel = new JPanel();
    imagePanel.setBorder(BorderFactory.createTitledBorder(""));
    imagePanel.setLayout(new BorderLayout());
    mainPanel.add(imagePanel);

    ImageIcon originalIcon = new ImageIcon("src/stocks.jpg");
    Image originalImage = originalIcon.getImage();
    Image resizedImage = originalImage.getScaledInstance(400, 400,
            Image.SCALE_SMOOTH); // Adjust size as needed
    ImageIcon resizedIcon = new ImageIcon(resizedImage);

    JLabel imageLabel = new JLabel(resizedIcon);
    imageLabel.setHorizontalAlignment(JLabel.CENTER);
    imageLabel.setVerticalAlignment(JLabel.CENTER);
    imagePanel.add(imageLabel, BorderLayout.CENTER);

    // Add the image panel to the right panel
    rightPanel.add(imagePanel);

    // Combo box for portfolios
    JPanel pPanel = new JPanel();
    pPanel.setBorder(BorderFactory.createTitledBorder("Portfolios"));
    pPanel.setLayout(new BoxLayout(pPanel, BoxLayout.X_AXIS));

    // Load portfolio names from the portfolios directory
    File portfoliosDir = new File("portfolios");
    String[] portfolioNames = portfoliosDir.list((dir, name) -> name.endsWith(".xml"));
    if (portfolioNames == null) {
      portfolioNames = new String[0];
    }
    for (int i = 0; i < portfolioNames.length; i++) {
      portfolioNames[i] = portfolioNames[i].replace(".xml", "");
    }

    // Create combo box with portfolio names
    JComboBox<String> portfolioComboBox = new JComboBox<>(portfolioNames);
    pPanel.add(portfolioComboBox);

    JButton pButton = new JButton("Enter Portfolio");
    JLabel pDisplay = new JLabel("Portfolios will appear here");
    pDisplay.setBorder(BorderFactory.createLineBorder(Color.BLACK));

    // Update action listener to display selected portfolio
    pButton.addActionListener(e -> {
      String selectedPortfolio = (String) portfolioComboBox.getSelectedItem();
      pDisplay.setText(selectedPortfolio);
    });

    pPanel.add(pButton);
    pPanel.add(pDisplay);


    rightPanel.add(pPanel);

    // Add the right panel to the main panel
    mainPanel.add(rightPanel, BorderLayout.CENTER);

    // Add the main panel to the frame
    add(mainPanel);
  }


  private class MenuButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      JButton source = (JButton) e.getSource();
      String actionCommand = source.getText();

      switch (actionCommand) {
        case "View Stock Gain/Loss":
          viewStockGainLoss();
          break;
        case "View x-Day Moving Average":
          viewMovingAverage();
          break;
        case "View x-Day Crossovers":
          viewCrossovers();
          break;
        case "Create Portfolio":
          createPortfolio();
          break;
        case "View Portfolio Value":
          viewPortfolioValue();
          break;
        case "Add Stock":
          addStock();
          break;
        case "Remove Stock":
          removeStock();
          break;
        case "View Portfolio Value Distribution":
          viewPortfolioValueDistribution();
          break;
        case "Determine Portfolio Composition on Date":
          viewPortfolioComposition();
          break;
        case "Rebalance Portfolio":
          rebalancePortfolio();
          break;
        case "Clear Portfolio":
          clearPortfolio();
          break;
        case "Update Portfolio":
          updatePortfolio();
          break;
        case "Exit":
          JOptionPane.showMessageDialog(null, "Exiting...");
          System.exit(0);
          break;
        default:
          JOptionPane.showMessageDialog(null, "Invalid Option");
          break;
      }
    }

    private void viewStockGainLoss() {
      String symbol = JOptionPane.showInputDialog("Enter stock symbol: ");
      List<Stock> stockData = alphaVantageController.fetchStockData(symbol);
      String startDate = JOptionPane.showInputDialog("Enter start date (YYYY-MM-DD): ");
      String endDate = JOptionPane.showInputDialog("Enter end date (YYYY-MM-DD): ");
      double gainLoss = stockController.calculateGainLoss(stockData, startDate, endDate);
      JOptionPane.showMessageDialog(null, "Gain/Loss: " + gainLoss);
    }

    private void viewMovingAverage() {
      String symbol = JOptionPane.showInputDialog("Enter stock symbol: ");
      List<Stock> stockData = alphaVantageController.fetchStockData(symbol);
      String date = JOptionPane.showInputDialog("Enter date (YYYY-MM-DD): ");
      int x = Integer.parseInt(JOptionPane.showInputDialog("Enter value of x: "));
      double movingAverage = stockController.calculateMovingAverage(stockData, date, x);
      JOptionPane.showMessageDialog(null,
              "x-Day Moving Average: " + movingAverage);
    }

    private void viewCrossovers() {
      String symbol = JOptionPane.showInputDialog("Enter stock symbol: ");
      List<Stock> stockData = alphaVantageController.fetchStockData(symbol);
      String startDate = JOptionPane.showInputDialog("Enter start date (YYYY-MM-DD): ");
      String endDate = JOptionPane.showInputDialog("Enter end date (YYYY-MM-DD): ");
      int x = Integer.parseInt(JOptionPane.showInputDialog("Enter value of x: "));
      List<String> crossovers = stockController.findCrossovers(stockData, startDate, endDate, x);
      JOptionPane.showMessageDialog(null,
              "x-Day Crossovers: " + crossovers);
    }

    private void createPortfolio() {
      String portfolioName = JOptionPane.showInputDialog("Enter portfolio name: ");
      Portfolio portfolio = new Portfolio(portfolioName);
      try {
        portfolioController.savePortfolioToFile(portfolio,
                Paths.get("portfolios", portfolioName + ".xml").toFile());
        JOptionPane.showMessageDialog(null,
                "Portfolio created and saved: " + portfolioName);
      } catch (IOException e) {
        JOptionPane.showMessageDialog(null,
                "Error saving portfolio: " + e.getMessage());
      }
    }

    private void viewPortfolioValue() {
      String portfolioName = JOptionPane.showInputDialog("Enter portfolio name: ");
      String date = JOptionPane.showInputDialog("Enter date (YYYY-MM-DD): ");
      Portfolio selectedPortfolio = portfolioController.getPortfolio(portfolioName);
      if (selectedPortfolio != null) {
        double portfolioValue =
                portfolioController.calculatePortfolioValue(selectedPortfolio, date);
        portfolioValue = Math.round(portfolioValue * 100.0) / 100.0;
        JOptionPane.showMessageDialog(null,
                "Portfolio Value: $" + portfolioValue);
      } else {
        JOptionPane.showMessageDialog(null, "Portfolio not found.");
      }
    }

    private void addStock() {
      String portfolioName = JOptionPane.showInputDialog("Enter portfolio name: ");
      Portfolio selectedPortfolio = portfolioController.getPortfolio(portfolioName);
      if (selectedPortfolio != null) {
        String symbol = JOptionPane.showInputDialog("Enter stock symbol: ");
        double quantity = Double.parseDouble(JOptionPane.showInputDialog("Enter quantity: "));
        String date = JOptionPane.showInputDialog("Enter date (YYYY-MM-DD): ");
        portfolioController.addStockToPortfolio(selectedPortfolio, symbol, quantity, date);
        JOptionPane.showMessageDialog(null, "Stock added to portfolio.");
      } else {
        JOptionPane.showMessageDialog(null, "Portfolio not found.");
      }
    }

    private void removeStock() {
      String portfolioName = JOptionPane.showInputDialog("Enter portfolio name: ");
      Portfolio selectedPortfolio = portfolioController.getPortfolio(portfolioName);
      if (selectedPortfolio != null) {
        String symbol = JOptionPane.showInputDialog("Enter stock symbol: ");
        double quantity = Double.parseDouble(JOptionPane.showInputDialog("Enter quantity: "));
        String date = JOptionPane.showInputDialog("Enter date (YYYY-MM-DD): ");
        portfolioController.removeStockFromPortfolio(selectedPortfolio, symbol, quantity, date);
        JOptionPane.showMessageDialog(null,
                "Stock removed from portfolio.");
      } else {
        JOptionPane.showMessageDialog(null, "Portfolio not found.");
      }
    }

    private void viewPortfolioValueDistribution() {
      String portfolioName = JOptionPane.showInputDialog("Enter portfolio name: ");
      String date = JOptionPane.showInputDialog("Enter date (YYYY-MM-DD): ");
      Portfolio selectedPortfolio = portfolioController.getPortfolio(portfolioName);
      if (selectedPortfolio != null) {
        Map<String, Double> valueDistribution =
                portfolioController.getPortfolioValueDistribution(selectedPortfolio, date);
        Map<String, Double> roundedValueDistribution = new HashMap<>();
        for (Map.Entry<String, Double> entry : valueDistribution.entrySet()) {
          roundedValueDistribution.put(entry.getKey(),
                  Math.round(entry.getValue() * 100.0) / 100.0);
        }
        JOptionPane.showMessageDialog(null,
                "Portfolio Value Distribution: " + roundedValueDistribution);
      } else {
        JOptionPane.showMessageDialog(null, "Portfolio not found.");
      }
    }

    private void viewPortfolioComposition() {
      String portfolioName = JOptionPane.showInputDialog("Enter portfolio name: ");
      String date = JOptionPane.showInputDialog("Enter date (YYYY-MM-DD): ");
      Portfolio selectedPortfolio = portfolioController.getPortfolio(portfolioName);
      if (selectedPortfolio != null) {
        Map<String, Double> composition =
                portfolioController.getPortfolioCompositionOnDate(selectedPortfolio, date);
        Map<String, Double> roundedComposition = new HashMap<>();
        for (Map.Entry<String, Double> entry : composition.entrySet()) {
          roundedComposition.put(entry.getKey(), Math.round(entry.getValue() * 100.0) / 100.0);
        }
        JOptionPane.showMessageDialog(null,
                "Portfolio Composition on " + date + ": " + roundedComposition);
      } else {
        JOptionPane.showMessageDialog(null, "Portfolio not found.");
      }
    }

    private void rebalancePortfolio() {
      String portfolioName = JOptionPane.showInputDialog("Enter portfolio name: ");
      String date = JOptionPane.showInputDialog("Enter date (YYYY-MM-DD): ");
      Portfolio selectedPortfolio = portfolioController.getPortfolio(portfolioName);
      if (selectedPortfolio != null) {
        JOptionPane.showMessageDialog(null,
                "Enter target weights for each stock in the portfolio:");
        Map<String, Double> targetWeights = new HashMap<>();
        double totalWeight = 0.0;

        for (String stockSymbol : selectedPortfolio.getStocks().keySet()) {
          String weightInput = JOptionPane.showInputDialog(stockSymbol + " weight (in %): ");
          double weight;
          try {
            weight = Double.parseDouble(weightInput) / 100.0;
            if (weight < 0) {
              JOptionPane.showMessageDialog(null,
                      "Weight cannot be negative. Please try again.");
              return;
            }
            targetWeights.put(stockSymbol, weight);
            totalWeight += weight;
          } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Invalid weight input. Please try again.");
            return;
          }
        }

        if (Math.abs(totalWeight - 1.0) > 0.01) {
          JOptionPane.showMessageDialog(null,
                  "Total weights must sum to 100%.");
          return;
        }

        PortfolioRebalancer rebalancer = new PortfolioRebalancer(portfolioController,
                alphaVantageController);
        rebalancer.rebalancePortfolio(selectedPortfolio, targetWeights, date);
        JOptionPane.showMessageDialog(null,
                "Portfolio rebalanced successfully.");
      } else {
        JOptionPane.showMessageDialog(null,
                "Portfolio not found.");
      }
    }

    private void clearPortfolio() {
      String portfolioName = JOptionPane.showInputDialog("Enter portfolio name: ");
      Portfolio selectedPortfolio = portfolioController.getPortfolio(portfolioName);
      if (selectedPortfolio != null) {
        portfolioController.clearPortfolio(selectedPortfolio);
        JOptionPane.showMessageDialog(null, "Portfolio cleared.");
      } else {
        JOptionPane.showMessageDialog(null, "Portfolio not found.");
      }
    }

    private void updatePortfolio() {
      try {
        portfolioController.loadAllPortfoliosFromDirectory("portfolios");
        portfolios.clear();
        portfolios.addAll(portfolioController.getAllPortfolios().values());
        JOptionPane.showMessageDialog(null, "Portfolios updated.");
      } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error updating portfolios: "
                + e.getMessage());
      }
    }
  }

  /**
   * The main method to run the Stock Trading Application. If the user runs the program with
   * -text as one of the arguments, it runs the text-based interface app. If not the program
   * runs the GUI automatically.
   *
   * @param args the command line arguments
   * @throws Exception if an error occurs
   */
  public static void main(String[] args) throws Exception {
    IStockController stockController = new StockControllerImpl();
    IPortfolioController portfolioController = new PortfolioControllerImpl();
    IAlphaVantageController alphaVantageController = new AlphaVantageControllerImpl();
    List<Portfolio> portfolios = new ArrayList<>();


    if (args.length > 0 && args[0].equalsIgnoreCase("-text")) {
      // Run MainController for text-based interface
      MainController.main(new String[]{});
    } else {
      // Runs the GUI
      SwingUtilities.invokeLater(() -> {
        StockTradingAppGUI frame = new StockTradingAppGUI(stockController,
                portfolioController, alphaVantageController, portfolios);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
      });
    }
  }
}
