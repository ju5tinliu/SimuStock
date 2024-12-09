package controller.commands;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import model.Stock;


/**
 * The class which provides methods to perform calculations on stock data,
 * such as calculating the gain/loss, moving averages, and identifying crossovers.
 */
public class StockControllerImpl implements IStockController {
  private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

  /**
   * Calculates the percentage gain or loss of a stock over a specific time.
   *
   *
   * @param stocks    the list of Stock objects containing stock data
   * @param startDate the start date of the period
   * @param endDate   the end date of the period
   * @return the percentage gain or loss over the specified period or else returns 0.0
   */
  @Override
  public double calculateGainLoss(List<Stock> stocks, String startDate, String endDate) {
    try {
      Date start = dateFormat.parse(startDate);
      Date end = dateFormat.parse(endDate);
      double startPrice = 0.0;
      double endPrice = 0.0;

      for (Stock stock : stocks) {
        Date date = dateFormat.parse(stock.getTimestamp());
        if (date.equals(start)) {
          startPrice = stock.getClose();
        }
        if (date.equals(end)) {
          endPrice = stock.getClose();
        }
      }

      if (startPrice == 0.0 || endPrice == 0.0) {
        return 0.0; // If any price is not found
      }

      return Math.round(((endPrice - startPrice) / startPrice) * 100 * 100.0) / 100.0;

    } catch (ParseException e) {
      System.out.println("Date is Invalid try again");
      return 0.0;
    }
  }

  /**
   * Calculates the moving average of a stock over a specified time.
   *
   * @param stocks the list of Stock objects containing the stock data
   * @param date   the target date
   * @param x      the number of days which to calculate the moving average
   * @return returns the number of days in moving average.
   */
  @Override
  public double calculateMovingAverage(List<Stock> stocks, String date, int x) {
    try {
      Date targetDate = dateFormat.parse(date);
      List<Stock> filteredStocks = stocks.stream()
              .filter(stock -> {
                try {
                  return dateFormat.parse(stock.getTimestamp()).compareTo(targetDate) <= 0;
                } catch (ParseException e) {
                  e.printStackTrace();
                  return false;
                }
              })
              .sorted((s1, s2) -> {
                try {
                  return dateFormat.parse(s2.getTimestamp()).compareTo(
                          dateFormat.parse(s1.getTimestamp()));
                } catch (ParseException e) {
                  e.printStackTrace();
                  return 0;
                }
              })
              .limit(x)
              .collect(Collectors.toList());

      if (filteredStocks.size() < x) {
        return 0.0; // Not enough data
      }

      double sum = 0.0;
      for (Stock stock : filteredStocks) {
        sum += stock.getClose();
      }

      return Math.round(sum / x * 100.0) / 100.0;

    } catch (ParseException e) {
      System.out.println("Date is Invalid try again");
      return 0.0;
    }
  }

  /**
   * Finds the dates where the stock price crosses above its moving average
   * at a specific time.
   *
   * @param stocks    the list fo Stock objects containing stock data
   * @param startDate the start date of a time
   * @param endDate   the end date of a time
   * @param x         the numbers of days for moving average calculation
   * @return returns a list of dates where crossovers occur
   */
  @Override
  public List<String> findCrossovers(List<Stock> stocks, String startDate, String endDate, int x) {
    List<String> crossovers = new ArrayList<>();
    try {
      Date start = dateFormat.parse(startDate);
      Date end = dateFormat.parse(endDate);

      for (Stock stock : stocks) {
        Date date = dateFormat.parse(stock.getTimestamp());
        if (!date.before(start) && !date.after(end)) {
          double movingAverage = calculateMovingAverage(stocks, stock.getTimestamp(), x);
          if (stock.getClose() > movingAverage) {
            crossovers.add(stock.getTimestamp());
          }
        }
      }
    } catch (ParseException e) {
      System.out.println("Date is Invalid try again");
    }
    return crossovers;
  }
}
