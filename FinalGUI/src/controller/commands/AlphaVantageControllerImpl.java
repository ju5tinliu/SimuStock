package controller.commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import model.Stock;

/**
 * This class shows how to fetch and store data from the Alpha Vantage API.
 * It stores data in a CSV file and reads from it when needed, therefore minimizing API calls.
 */
public class AlphaVantageControllerImpl implements IAlphaVantageController {

  private static final String API_KEY = "4WK802NYWE4N9C3M";
  private IAlphaVantageController alphaVantageController;

  /**
   * Executes the whole program, taking the stock symbol as input and creating a new CSV file
   * based on the symbol if needed.
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    AlphaVantageControllerImpl demo = new AlphaVantageControllerImpl();
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter the stock symbol: ");
    String symbol = scanner.nextLine();
    String csvFilePath = "data_" + symbol + ".csv";

    try {
      if (!demo.isDataUpToDate(csvFilePath)) {
        demo.fetchAndStoreData(symbol, csvFilePath);
      }
      demo.readDataFromCSV(csvFilePath);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Checks whether the data in the CSV file is up-to-date.
   *
   * @param csvFilePath the path to the CSV file where data is stored
   * @return true if the file is up-to-date, false otherwise
   */
  @Override
  public boolean isDataUpToDate(String csvFilePath) {
    try {
      BasicFileAttributes attrs = Files.readAttributes(Paths.get(csvFilePath),
              BasicFileAttributes.class);
      FileTime fileTime = attrs.lastModifiedTime();
      long currentTime = System.currentTimeMillis();
      long fileTimeMillis = fileTime.toMillis();
      long diffInMillis = currentTime - fileTimeMillis;
      long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
      return diffInDays < 1; // Check if the file is less than a day old
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * Fetches and stores stock data from the Alpha Vantage API.
   *
   * @param symbol      the stock symbol
   * @param csvFilePath the path to the CSV file where data is stored
   * @throws IOException if an I/O error occurs
   */
  @Override
  public void fetchAndStoreData(String symbol, String csvFilePath) throws IOException {
    String urlString = String.format("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=%s&apikey=%s&datatype=csv", symbol, API_KEY);
    URL url = new URL(urlString);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");

    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    BufferedWriter bw = new BufferedWriter(new FileWriter(csvFilePath));

    String line;
    while ((line = br.readLine()) != null) {
      bw.write(line);
      bw.newLine();
    }

    br.close();
    bw.close();
  }

  /**
   * Fetches stock data for a given symbol from the CSV file or API if the data is not up-to-date.
   *
   * @param symbol the stock symbol
   * @return a list of Stock objects containing the fetched data
   */
  @Override
  public List<Stock> fetchStockData(String symbol) {
    String csvFilePath = "data_" + symbol + ".csv";
    List<Stock> stockData = new ArrayList<>();
    try {
      if (!isDataUpToDate(csvFilePath)) {
        fetchAndStoreData(symbol, csvFilePath);
      }
      try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
        String line;
        while ((line = br.readLine()) != null) {
          if (line.startsWith("timestamp")) {
            continue;
          }
          String[] fields = line.split(",");
          Stock stock = new Stock();
          stock.setSymbol(symbol);
          stock.setTimestamp(fields[0]);
          stock.setOpen(Double.parseDouble(fields[1]));
          stock.setHigh(Double.parseDouble(fields[2]));
          stock.setLow(Double.parseDouble(fields[3]));
          stock.setClose(Double.parseDouble(fields[4]));
          stock.setVolume(Integer.parseInt(fields[5]));
          stockData.add(stock);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return stockData;
  }

  /**
   * Reads and prints data from the CSV file.
   *
   * @param csvFilePath the path to the CSV file
   */
  @Override
  public void readDataFromCSV(String csvFilePath) {
    try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
      String line;
      while ((line = br.readLine()) != null) {
        System.out.println(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Fetches the stock price for a given stock symbol on a specified date.
   *
   * @param stockSymbol the stock symbol to fetch the price for
   * @param date the date to fetch the stock price
   * @return the closing stock price on the specified date
   * @throws IllegalArgumentException if no stock price is available for the given date
   */
  @Override
  public double getStockPrice(String stockSymbol, String date) {
    List<Stock> stocks = alphaVantageController.fetchStockData(stockSymbol);
    for (Stock stock : stocks) {
      if (stock.getTimestamp().equals(date)) {
        return stock.getClose();
      }
    }
    throw new IllegalArgumentException("No stock price available for the given date");
  }
}
