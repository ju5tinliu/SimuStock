package controller.commands;

import java.util.List;

import model.Stock;

/**
 * This interface defines the methods required for performing various stock-related calculations.
 * Implementations of this interface provides the necessary methods to calculate gain/loss, moving
 * averages, and find crossovers for a list of stocks.
 */
public interface IStockController {

  /**
   * Calculates the gain or loss for a list of stocks between the specified start and end dates.
   *
   * @param stocks    the list of stocks
   * @param startDate the start date for the calculation
   * @param endDate   the end date for the calculation
   * @return the calculated gain or loss
   */
  double calculateGainLoss(List<Stock> stocks, String startDate, String endDate);

  /**
   * Calculates the moving average for a list of stocks on a specified date using a given period.
   *
   * @param stocks the list of stocks
   * @param date   the date for the calculation
   * @param x      the period for the moving average calculation
   * @return the calculated moving average
   */
  double calculateMovingAverage(List<Stock> stocks, String date, int x);

  /**
   * Finds the crossovers for a list of stocks between the specified start and end dates using a
   * given period.
   *
   * @param stocks    the list of stocks
   * @param startDate the start date for finding crossovers
   * @param endDate   the end date for finding crossovers
   * @param x         the period for the crossover calculation
   * @return a list of crossover points
   */
  List<String> findCrossovers(List<Stock> stocks, String startDate, String endDate, int x);
}
