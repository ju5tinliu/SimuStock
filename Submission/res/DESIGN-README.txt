####################################################################################################
#################################### Project Design Overview #######################################
####################################################################################################

## Introduction
This document provides an in-depth explanation of the design and architecture of our project, which
follows the Model-View-Controller (MVC) pattern. The MVC pattern is a software design pattern that
separates the application into three interconnected components: Model, View, and Controller. This
separation helps in organizing the code, making it more modular, and enhancing maintainability and
scalability.

## Project Structure
## Model Layer
The Model layer is responsible for the data and business logic of the application. It includes
interfaces, their implementations, and mock versions for testing.

## Interfaces:

- IPortfolio.java: Defines the contract for a portfolio model, including methods for accessing and
manipulating portfolio data.
- IStock.java: Defines the contract for a stock model, including methods for accessing and
manipulating stock data.


## Implementations:

- Portfolio.java: Implements the IPortfolio interface, containing data structures and methods for
managing a portfolio of stocks.
- Stock.java: Implements the IStock interface, containing data structures and methods for managing
individual stock information.


## Mocks:

- MockPortfolio.java: Mock implementation of the IPortfolio interface, used for testing portfolio
functionalities.
- MockStock.java: Mock implementation of the IStock interface, used for testing stock
functionalities.


## Specialized Logic:

- PortfolioRebalancer.java: Contains logic to adjust the composition of a portfolio based on certain
criteria or strategies, enhancing the functionality of the model layer.


## View Layer
The View layer handles the user interface and presentation logic, facilitating interaction between
the user and the application.

## Interfaces:

- IConsoleView.java: Defines the contract for the console view, specifying methods for displaying
information and capturing user input from the console.


## Implementations:

ConsoleView.java: Implements the IConsoleView interface, responsible for interacting with the user
through the console. It includes methods for displaying information to the user and capturing user
input, making it the primary user interface component.

## Controller Layer
The Controller layer manages the communication between the model and the view. It processes user
input, updates the model, and refreshes the view accordingly.

## Interfaces:

- IAlphaVantageController.java: Defines methods for fetching stock data from the AlphaVantage API.
- IPortfolioController.java: Specifies methods for managing a portfolio, such as adding, removing,
and viewing stocks.
- IStockController.java: Specifies methods for managing individual stock information and operations.


## Implementations:

- AlphaVantageControllerImpl.java: Implements the IAlphaVantageController interface, responsible for
 interacting with the AlphaVantage API to fetch stock data.
- PortfolioControllerImpl.java: Implements the IPortfolioController interface, containing logic for
managing a portfolio, such as adding/removing stocks and calculating values.
- StockControllerImpl.java: Implements the IStockController interface, containing logic for managing
stock information and operations.

## Mocks:

- MockAlphaVantageController.java: Mock implementation of the IAlphaVantageController, used for
testing API interactions without making actual calls.
- MockPortfolioController.java: Mock implementation of the IPortfolioController, used for testing
portfolio management functionalities.
- MockStockController.java: Mock implementation of the IStockController, used for testing stock
management functionalities.

## Main Controller:

- MainController.java: The main controller class that coordinates actions between different
controllers and manages the application's main logic. It acts as the central point of communication
and ensures the smooth flow of data and operations across the application.


Conclusion
This project follows a well-structured MVC design pattern, ensuring a clear separation of concerns.
The Model layer handles the data and business logic, the View layer manages user interactions and
presentation, and the Controller layer facilitates communication between the Model and View.
This design not only enhances the modularity and maintainability of the code but also makes it
easier to test and extend. By adhering to these principles, the project achieves a robust and
scalable architecture, ready to handle future enhancements and modifications.