package com.stock.service.product_master_settings;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.stock.entity.ProductMaster;
import com.stock.entity.ProductMasterHistory;
import com.stock.entity.form.product_master.SaveProductMaster;
import com.stock.entity.form.product_master.SelectProductMasterList;



public interface ProductMasterSettingsService {

  public ProductMaster getOneProductMaster(Long productId);

  public List<Map<String, Object>> getProductMasterList(SelectProductMasterList selectProductMasterList);

  public ProductMasterHistory findOneProductMasterHistory(Long productId, LocalDateTime lastModifiedTs);

  public List<Map<String, Object>> registProduct(SaveProductMaster productMaster) throws Exception;

}
