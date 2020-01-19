package com.stock.entity.form.stock_master;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SaveStock {

  private Long stockId;

  private Long productId;

  @NotNull( message = "実在庫数を入力してください。" )
  @Range( min = 1, max = 100000, message = "{min}から{max}までの数字を入力してください。")
  private Integer actualStockNumber;

  private Integer futureShippedStockNumber;

  @NotNull( message = "基準在庫数を入力してください。" )
  @Range( min = 1, max = 1000000, message = "{min}から{max}までの数字を入力してください。")
  private Integer standardStockNumber;

  @DateTimeFormat( iso = DateTimeFormat.ISO.DATE_TIME )
  private LocalDateTime createTs;

  @DateTimeFormat( iso = DateTimeFormat.ISO.DATE_TIME )
  private LocalDateTime lastModifiedTs;

}
