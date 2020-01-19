package com.stock.entity.form.stock_master;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SelectProductStockList {

  private String searchProductCode;

  private String searchProductName;

}
