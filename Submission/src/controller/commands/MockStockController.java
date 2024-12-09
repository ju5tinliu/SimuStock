package controller.commands;

import java.util.List;

import model.Stock;

/**
 * This class extends StockControllerImpl to provide mock implementations
 * of stock-related calculations.
 */
public class MockStockController extends StockControllerImpl {

  /**
   * Calculates the gain or loss for the given list of stocks between the specified
   * start and end dates.
   * This method overrides the implementation in StockControllerImpl to provide a mock
   * implementation.
   *
   * @param stocks the list of stocks to calculate the gain/loss for
   * @param startDate the start date for the calculation
   * @param endDate the end date for the calculation
   * @return the calculated gain or loss
   */
  @Override
  public double calculateGainLoss(List<Stock> stocks, String startDate, String endDate) {
    return super.calculateGainLoss(stocks, startDate, endDate);
  }

  /**
   * Calculates the moving average for the given list of stocks on the specified date over a
   * specified period.
   * This method overrides the implementation in StockControllerImpl to provide a mock
   * implementation.
   *
   * @param stocks the list of stocks to calculate the moving average for
   * @param date the date for the calculation
   * @param x the period over which to calculate the moving average
   * @return the calculated moving average
   * @throws IllegalArgumentException if the date format is invalid
   */
  @Override
  public double calculateMovingAverage(List<Stock> stocks, String date, int x) {
    if (!isValidDate(date)) {
      throw new IllegalArgumentException("Invalid date format");
    }
    return super.calculateMovingAverage(stocks, date, x);
  }

  /**
   * Finds crossovers for the given list of stocks between the specified start and end dates over
   * a specified period.
   * This method overrides the implementation in StockControllerImpl to provide a mock
   * implementation.
   *
   * @param stocks the list of stocks to find crossovers for
   * @param startDate the start date for the search
   * @param endDate the end date for the search
   * @param x the period over which to find crossovers
   * @return a list of dates where crossovers were found
   * @throws IllegalArgumentException if the date format is invalid
   */
  @Override
  public List<String> findCrossovers(List<Stock> stocks, String startDate, String endDate, int x) {
    if (!isValidDate(startDate) || !isValidDate(endDate)) {
      throw new IllegalArgumentException("Invalid date format");
    }
    return super.findCrossovers(stocks, startDate, endDate, x);
  }

  /**
   * Validates the format of the given date.
   *
   * @param date the date to validate
   * @return true if the date format is valid, false otherwise
   */
  private boolean isValidDate(String date) {
    String regex = "^(\\d{4})-(\\d{2})-(\\d{2})$";
    return date.matches(regex);
  }
}
