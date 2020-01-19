package com.stock.entity.form.product_master;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
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
public class SaveProductMaster {

  private Long productId;

  @NotBlank( message = "商品コードを入力してください。" )
  private String productCode;

  @NotBlank( message = "jancodeを入力してください。" )
  private String jancode;

  private Boolean disableFlg  = Boolean.FALSE;

  @NotBlank( message = "商品名を入力してください。" )
  private String productName;

  @NotNull( message = "価格を入力してください。" )
  @Range( min = 1, max = 1000000, message = "{min}から{max}までの数字を入力してください。")
  private Integer price;

  @NotNull( message = "税率を選択してください。" )
  private Long taxId;

  @DateTimeFormat( iso = DateTimeFormat.ISO.DATE_TIME )
  private LocalDateTime productMasterCreateTs;

  @DateTimeFormat( iso = DateTimeFormat.ISO.DATE_TIME )
  private LocalDateTime productMasterHistoryCreateTs;

  @DateTimeFormat( iso = DateTimeFormat.ISO.DATE_TIME )
  private LocalDateTime productMasterLastModifiedTs;

  @DateTimeFormat( iso = DateTimeFormat.ISO.DATE_TIME )
  private LocalDateTime productMasterHistoryLastModifiedTs;

}
