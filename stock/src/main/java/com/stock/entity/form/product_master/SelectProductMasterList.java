package com.stock.entity.form.product_master;

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
public class SelectProductMasterList {

  private String searchProductCode;

  private String searchJancode;

  private String searchProductName;

  private Byte searchTaxRate;

  private Boolean searchDisableFlg = Boolean.FALSE;

  private Boolean searchTaxDisableFlg;

}
