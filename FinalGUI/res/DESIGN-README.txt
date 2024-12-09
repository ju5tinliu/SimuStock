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
 - Justification: This interface ensures that any implementation of a portfolio model adheres to a
   consistent set of methods for managing and retrieving portfolio data. This promotes modularity
   and allows for easy swapping of different implementations without affecting the rest of the
   application.

- IStock.java: Defines the contract for a stock model, including methods for accessing and
manipulating stock data.
 - Justification: By defining a clear contract for stock models, this interface ensures consistency
   and interoperability among different stock model implementations. It facilitates the development
   of flexible and interchangeable components that adhere to the same standards for stock data
   management.


## Implementations:

- Portfolio.java: Implements the IPortfolio interface, containing data structures and methods for
managing a portfolio of stocks.
 - Justification: This class provides the concrete implementation of the portfolio model, handling
 the actual data management and business logic for stock portfolios. It ensures that all required
 methods defined by the IPortfolio interface are properly implemented and operational.

- Stock.java: Implements the IStock interface, containing data structures and methods for managing
individual stock information.
 - Justification: This class serves as the concrete implementation of the stock model, managing
  individual stock details and operations. It ensures compliance with the IStock interface,
  providing a reliable and standardized way to handle stock information within the application.


## Mocks:

- MockPortfolio.java: Mock implementation of the IPortfolio interface, used for testing portfolio
functionalities.
 - Justification: This mock class allows for isolated testing of portfolio-related functionalities
   without relying on actual data or complex business logic. It facilitates unit testing by
   providing controlled and predictable behavior, making it easier to identify and fix issues.

- MockStock.java: Mock implementation of the IStock interface, used for testing stock
functionalities.
 - Justification: Similar to MockPortfolio, this mock class enables the testing of stock-related
 functionalities in isolation. It provides a simplified and controlled environment for testing,
 helping to ensure that stock management features work correctly without external dependencies.


## Specialized Logic:

- PortfolioRebalancer.java: Contains logic to adjust the composition of a portfolio based on certain
criteria or strategies, enhancing the functionality of the model layer.
 - Justification: This specialized class adds advanced functionality to the model layer by providing
  methods to rebalance portfolios according to specified criteria or strategies. It enhances the
  application's ability to manage portfolio compositions dynamically, improving overall flexibility
  and responsiveness to market conditions.


## View Layer
The View layer handles the user interface and presentation logic, facilitating interaction between
the user and the application.

## Interfaces:

- IConsoleView.java: Defines the contract for the console view, specifying methods for displaying
information and capturing user input from the console.
 - Justification: This interface ensures a consistent approach to interacting with users through the
   console. By defining standard methods for displaying information and capturing input, it enables
   the development of interchangeable console views that can be easily modified or replaced.


## Implementations:

- ConsoleView.java: Implements the IConsoleView interface, responsible for interacting with the user
through the console. It includes methods for displaying information to the user and capturing user
input, making it the primary user interface component.
 - Justification: This class provides the concrete implementation of the console-based user
 interface, ensuring that all required methods are effectively implemented. It serves as the
 primary means of interaction for users who prefer or require a text-based interface, supporting
 the application's usability and accessibility.

- StockTradingAppGUI.java: Provides a graphical user interface for the application, enhancing user
experience by offering visual interaction elements for managing portfolios and stocks.
 - Justification: This class enhances the user experience by providing a graphical user interface
 (GUI) for the application. The GUI makes the application more accessible and user-friendly,
 catering to users who prefer visual interactions over text-based interfaces. It ensures that the
 application can appeal to a broader audience while maintaining the integrity of the MVC pattern.


## Controller Layer
The Controller layer manages the communication between the model and the view. It processes user
input, updates the model, and refreshes the view accordingly.

## Interfaces:

- IAlphaVantageController.java: Defines methods for fetching stock data from the AlphaVantage API.
 - Justification: This interface standardizes the methods for interacting with the AlphaVantage API,
  ensuring consistent and reliable access to stock data. It promotes modularity and flexibility by
  allowing different implementations to be used without affecting other parts of the application.

- IPortfolioController.java: Specifies methods for managing a portfolio, such as adding, removing,
and viewing stocks.
 - Justification: By defining a clear set of methods for portfolio management, this interface
 ensures consistency and interoperability among different portfolio controller implementations.
 It supports modular development and allows for easy updates or replacements of the controller
 component.

- IStockController.java: Specifies methods for managing individual stock information and operations.
 - Justification: This interface ensures that all stock management operations adhere to a consistent
  set of methods, promoting modularity and flexibility. It enables different implementations to be
  easily swapped or updated without affecting the overall application.


## Implementations:

- AlphaVantageControllerImpl.java: Implements the IAlphaVantageController interface, responsible for
 interacting with the AlphaVantage API to fetch stock data.
  - Justification: This class provides the concrete implementation for fetching and managing stock
  data from the AlphaVantage API. It ensures that all required methods are effectively implemented,
  supporting the application's need for real-time stock information.

- PortfolioControllerImpl.java: Implements the IPortfolioController interface, containing logic for
managing a portfolio, such as adding/removing stocks and calculating values.
 - Justification: This class serves as the concrete implementation for portfolio management,
 ensuring that all methods defined by the IPortfolioController interface are properly implemented.
 It handles the business logic for adding, removing, and managing stocks within portfolios,
 supporting the application's core functionality.

- StockControllerImpl.java: Implements the IStockController interface, containing logic for managing
stock information and operations.
Justification: This class provides the concrete implementation for stock management, ensuring
compliance with the IStockController interface. It handles the detailed operations related to
individual stocks, supporting the application's ability to manage stock data and perform necessary
calculations.

- StockTradingAppGUI.java: Implements the graphical user interface for the stock trading
application, enabling users to interact with the application through a visual interface.
 - Justification: Integrating the `StockTradingAppGUI.java` with the existing controllers ensures
   that both the text-based and graphical interfaces can utilize the same business logic and data
   management functionalities. This integration maintains the integrity of the MVC pattern,
   providing flexibility for users to choose their preferred interaction method. Additionally,
   the ability to run the `MainController` with a `-text` argument ensures backward compatibility
   and supports users who prefer or require a console-based interface.


## Mocks:

- MockAlphaVantageController.java: Mock implementation of the IAlphaVantageController, used for
testing API interactions without making actual calls.
 - Justification: This mock class allows for testing API interactions in isolation, providing a
 controlled environment for testing without relying on actual API calls. It helps ensure that the
 application can handle various scenarios and errors related to API interactions.

- MockPortfolioController.java: Mock implementation of the IPortfolioController, used for testing
portfolio management functionalities.
 - Justification: This mock class facilitates the testing of portfolio management functionalities
 without relying on the actual implementation. It provides a simplified and controlled environment
 for testing, making it easier to identify and fix issues.

- MockStockController.java: Mock implementation of the IStockController, used for testing stock
management functionalities.
 - Justification: Similar to MockPortfolioController, this mock class enables the isolated testing
 of stock management functionalities. It ensures that the application can be thoroughly tested
 without dependencies on actual data or complex logic.

## Main Controller:

- MainController.java: The main controller class that coordinates actions between different
controllers and manages the application's main logic. It acts as the central point of communication
and ensures the smooth flow of data and operations across the application.
 - Justification: This class serves as the central hub for managing the application's main logic and
  coordinating actions between different controllers. It ensures the smooth flow of data and
  operations, supporting the overall functionality and responsiveness of the application. The
  ability to run the MainController with a -text argument provides flexibility for users to
  choose between text-based and graphical interfaces, ensuring backward compatibility and user
  preference.


Conclusion
This project follows a well-structured MVC design pattern, ensuring a clear separation of concerns.
The Model layer handles the data and business logic, the View layer manages user interactions and
presentation, and the Controller layer facilitates communication between the Model and View.
This design not only enhances the modularity and maintainability of the code but also makes it
easier to test and extend. By adhering to these principles, the project achieves a robust and
scalable architecture, ready to handle future enhancements and modifications.