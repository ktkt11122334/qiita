package com.stock.common_process.converter;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ConverterUtil {


  /**
   * LocalDateTime型に値を整形する
   */
  public static LocalDateTime toLocalDateTime(String date, String format) throws ParseException {

    // ミリ秒の整形を行う
    String[] millis = date.split("\\.");
    while( millis[1].length() != 3) {
      millis[1] = millis[1] + "0";
    }
    date = millis[0] + "." + millis[1];


    DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
    return LocalDateTime.parse(date, dtf);
  }

}
