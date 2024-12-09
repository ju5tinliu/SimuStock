## Setup Instructions


## Prerequisites
1. Ensure you have Java installed on your machine. You can download it from here.
2. Obtain your AlphaVantage API key from AlphaVantage.


## Setting Up

1. Download the JAR file: Ensure you have the JAR file of the project. Place the JAR file in a
directory of your choice.

2. Configuration Files: Make sure the following files are in the same directory as the JAR file:
- AlphaVantageControllerImpl
- IAlphaVantageController
- IPortfolioController
- IStockController
- MockAlphaVantageController
- MockPortfolioController
- MockStockController
- PortfolioControllerImpl
- StockControllerImpl
- StockTradingAppGUI
- META-INF
- IPortfolio
- IStock
- MockPortfolio
- Portfolio
- PortfolioRebalancer
- Stock
- ConsoleView
- IConsoleView
- Main

## Running the Program
1. Open Command Prompt: Navigate to the directory containing the JAR file and the configuration
files.

2. Run the JAR file: Use the following command to run the JAR file:

3. Type this in command prompt while being in the same directory as the jar file: java -jar src.jar
(name of jar file created)

4. Run the GUI: To start the graphical user interface, use the following command:
java -jar Assignment_6_1.jar


## Creating Portfolios

## Creating a Portfolio with 3 Different Stocks

1. Add Stocks:
- Follow the console prompts to add the first stock (e.g., GOOG, purchased on 2022-01-01,
quantity 10).
- Add the second stock (e.g., AAPL, purchased on 2022-02-01, quantity 5).
- Add the third stock (e.g., MSFT, purchased on 2022-03-01, quantity 8).

2. Create Portfolio:
- Name your portfolio (e.g., "Tech Giants").


## Creating a Second Portfolio with 2 Different Stocks

1. Add Stocks:
- Follow the console prompts to add the first stock (e.g., TSLA, purchased on 2022-04-01, quantity
15).
- Add the second stock (e.g., AMZN, purchased on 2022-05-01, quantity 20).

2. Create Portfolio:
- Name your portfolio.


## Querying Portfolio Value on a Specific Date

1. Select Portfolio: Choose the portfolio you want to query.
2. Enter Date: Enter the date for which you want to query the value (e.g., 2023-06-01).
View Results: The program will display the value of each stock in the portfolio and the total
portfolio value on the specified date.


## Viewing Portfolio Value Distribution

1. Select Portfolio: Choose the portfolio for which you want to view the value distribution.
2. View Distribution: The program will display a breakdown of the portfolio's value by stock,
showing the percentage each stock contributes to the total portfolio value.

## Determining Portfolio Composition on a Specific Date

1. Select Portfolio: Choose the portfolio you want to analyze.
2. Enter Date: Enter the date for which you want to view the portfolio composition
(e.g., 2023-06-01).
3. View Composition: The program will display the stocks held and their quantities on the specified
date.

## Clearing a Portfolio
1. Select Portfolio: Choose the portfolio you want to clear.
2. Clear Portfolio: Confirm the action to remove all stocks from the portfolio, effectively
resetting it.

## Saving Changes and Exiting the Application

MUST USE UPDATE PORTFOLIO TO SAVE THE FILES!!

1. Save/Update Portfolio: After making changes to your portfolio, ensure to save the modifications.
2. Confirm Save: The program will persist any modifications made, ensuring that changes are
not lost.
3. Exit Command: Use the console command to exit the application.
4. Confirm Exit: The application will terminate gracefully, ensuring all resources are properly closed.