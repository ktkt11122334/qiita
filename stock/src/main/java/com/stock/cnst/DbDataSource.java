package com.stock.cnst;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class DbDataSource {



  private static final String DB_DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
  private static final String DB_URL = "";       // DB URL
  private static final String DB_USERNAME = "";  // DB user name
  private static final String DB_PASS = "";      // DB pass

  private static final int MAX_TOTAL = 96;
  private static final int MAX_IDLE = 16;
  private static final int MIN_IDLE = 0;
  private static final int MAX_WAIT_MILLIS = 60000;

  @Bean(destroyMethod="close")
  public DataSource dataSource() {

    BasicDataSource dataSource = new BasicDataSource();

    dataSource.setDriverClassName(DB_DRIVER_CLASS_NAME);
    dataSource.setUrl(DB_URL);
    dataSource.setUsername(DB_USERNAME);
    dataSource.setPassword(DB_PASS);
    dataSource.setDefaultAutoCommit(false);
    dataSource.setMaxTotal(MAX_TOTAL);
    dataSource.setMaxIdle(MAX_IDLE);
    dataSource.setMinIdle(MIN_IDLE);
    dataSource.setMaxWaitMillis(MAX_WAIT_MILLIS);

    return dataSource;
  }

}
