####################################################################################################
############################# Stock Portfolio Management System ####################################
####################################################################################################

## Overview
This project is a Stock Portfolio Management System that allows users to manage their stock
portfolios. The system provides functionalities to add, remove, and view stocks in the portfolio.
It integrates with the AlphaVantage API to fetch real-time stock data and displays the portfolio
performance.

----------------------------------------------------------------------------------------------------

## Features

- **View Stock Gain/Loss**: Calculate and view the gain or loss for each stock in the portfolio
based on the purchase price and current market price.

- **View x-Day Moving Average**: Calculate and display the moving average for a stock over a
specified number of days to help in analyzing the stock's performance.

- **View x-Day Crossovers**: Identify and display crossover points where the short-term moving
average crosses the long-term moving average, which can be used as buy or sell signals.

- **Create Portfolios**: Create multiple portfolios to manage different sets of stocks for various
investment strategies or goals.

- **View Portfolio**: View the list of stocks in the portfolio along with their current market value.

- **Fetch Real-Time Data**: Fetch real-time stock data using the AlphaVantage API.

- **Add Stock**: Add a stock to the portfolio with details like symbol, purchase date, and quantity.

- **Remove Stock**: Remove a stock from the portfolio.

- **Rebalance Portfolio**: Adjust the stock positions in the portfolio to match specified target
weights on a given date.

- **View Portfolio Value Distribution**: Display a breakdown of the portfolio's value by stock,
showing the percentage each stock contributes to the total portfolio value.

- **Determine Portfolio Composition on Date**: View the composition of the portfolio as it was on
a specific date, including the stocks held and their quantities.

- **Clear Portfolio**: Remove all stocks from the portfolio, effectively resetting it.

- **Save changes**: Persist any modifications made to the portfolio, ensuring that changes are not
lost.

- **Exit**: Terminate the application gracefully, ensuring all resources are properly closed.

----------------------------------------------------------------------------------------------------

## Files

1. **AlphaVantageControllerImpl.java**: Contains the implementation for fetching stock data using
AlphaVantage API, calculating moving averages, and finding crossovers.

2. **IAlphaVantageController.java**: Defines the interface for AlphaVantage controller with methods
for fetching stock data, calculating moving averages, and finding crossover dates.

3. **IPortfolioController.java**: Defines the interface for managing portfolios, including adding,
removing, updating stock positions, and saving/loading portfolios.

4. **IStockController.java**: Defines the interface for managing individual stock data,
retrieving prices, and calculating metrics.

5. **MockAlphaVantageController.java**: Provides a mock implementation of the AlphaVantage
controller for testing purposes with stubbed methods.

6. **MockPortfolioController.java**: Provides a mock implementation of the Portfolio controller
for testing purposes with stubbed methods.

7. **MockStockController.java**: Provides a mock implementation of the Stock controller for
testing purposes with stubbed methods.

8. **PortfolioControllerImpl.java**: Contains the implementation for managing portfolios,
including adding, removing, updating stock positions, and rebalancing portfolios.

9. **StockControllerImpl.java**: Contains the implementation for managing individual stock data,
retrieving prices, and calculating metrics.

10. **Main.java**: The main entry point of the application, handling user interactions and
coordinating between controllers and views.

11. **IPortfolio.java**: Defines the interface for managing portfolio details, adding, removing,
updating stock positions, and calculating portfolio value and performance metrics.

12. **IStock.java**: Defines the interface for managing individual stock details, retrieving
prices, and validating data.

13. **MockPortfolio.java**: Provides a mock implementation of the Portfolio interface for testing
purposes with stubbed methods.

14. **MockStock.java**: Provides a mock implementation of the Stock interface for testing purposes
with stubbed methods.

15. **Portfolio.java**: Contains the implementation for managing portfolio details, adding,
removing, updating stock positions, and calculating portfolio value and performance metrics.

16. **PortfolioRebalancer.java**: Contains the logic for rebalancing the portfolio to match target
allocations.

17. **Stock.java**: Contains the implementation for managing individual stock details, retrieving,
and validating stock prices and other attributes.

18. **ConsoleView.java**: Handles the console-based user interface, displaying menus, getting user
input, and displaying messages.

19. **IConsoleView.java**: Defines the interface for displaying menus, getting user input,
displaying messages, and closing the console view.

----------------------------------------------------------------------------------------------------

## Installation

1. Clone the repository to your local machine.
2. Make sure you have Java installed.
3. Open the project in your preferred IDE.
4. Add the necessary libraries and dependencies.
5. Run `Main.java` to start the application.
6. Use `PortfolioControllerImpl.java` and `StockControllerImpl.java` to manage your portfolio.

----------------------------------------------------------------------------------------------------

## Usage
1. **Add a Stock**: Use the console interface to add a new stock to your portfolio by entering the
stock symbol, purchase date, and quantity.

2. **Remove a Stock**: Remove a stock from your portfolio using the stock symbol.

3. **View Portfolio**: View the current portfolio, which displays all the stocks along with their
current market value.

4. **Fetch Real-Time Data**: Automatically fetch and update the stock prices using the AlphaVantage
API.

5. **Rebalance Portfolio**: Adjust your portfolio to match specified target allocations.

6. **View Portfolio Value Distribution**: Display a breakdown of the portfolio's value by stock,
showing the percentage each stock contributes to the total portfolio value.

7. **Determine Portfolio Composition on Date**: View the composition of the portfolio as it
was on a specific date, including the stocks held and their quantities.

8. **Clear Portfolio**: Remove all stocks from the portfolio, effectively resetting it.

9. **Save changes**: Persist any modifications made to the portfolio, ensuring that changes are not
lost.

10. **Exit**: Terminate the application gracefully, ensuring all resources are properly closed.

----------------------------------------------------------------------------------------------------

- drawPortfolioPerformanceBarChart
This method is responsible for generating and displaying a horizontal bar chart that visualizes the
performance of a portfolio over a specified date range.

- Fetch Portfolio Values: It calls the getPortfolioValuesOverTime method to retrieve a map of \
date-to-portfolio-value pairs for the specified date range.

- Handle Empty Data: If no data is available for the given date range, it prints a message and exits.
Determine Scale Factor: It calculates the maximum portfolio value in the date range and determines 
a scale factor to fit the values within 50 asterisks.

- Sort and Filter Dates: It sorts the dates in ascending order and filters out weekends.
Print Chart Title: It prints the title of the chart, including the portfolio name and the date range.
Print Bar Chart: For each date, it calculates the number of asterisks based on the portfolio value 
and the scale factor, then prints the date followed by the corresponding number of asterisks.

- Print Scale: It prints the scale indicating the value represented by each asterisk.

- getPortfolioValuesOverTime
This method calculates the portfolio value for each day within a specified date range. Here is a 
step-by-step explanation of how it works:

- Initialize Variables: It initializes a map to store the portfolio values for each date and parses 
the start and end dates.

- Iterate Over Dates: It iterates over each date in the range, incrementing by one day at a time.
Get Portfolio Composition: For each date, it calls the getCompositionOnDate method to retrieve the 
portfolio's composition (stock symbol to quantity map) as of that date.

- Fetch Stock Data: For each stock in the composition, it fetches the historical stock data using alphaVantageControllerImpl.fetchStockData.

- Calculate Portfolio Value: It calculates the portfolio value for that date by multiplying the stock's
closing price by the quantity held and summing these values for all stocks in the portfolio.

- Store Value: It stores the calculated value in the map with the date as the key.

- Return Values: After processing all dates in the range, it returns the map of 
date-to-portfolio-value pairs.

- Combined Functionality
When the drawPortfolioPerformanceBarChart method is called, it first uses getPortfolioValuesOverTime 
to obtain the portfolio values for each date in the specified range. It then generates a horizontal 
bar chart by printing lines of asterisks proportional to the portfolio values, excluding weekends, 
and finally prints the scale used for the chart. This provides a visual representation of the 
portfolio's performance over time, making it easier to analyze trends and fluctuations.
