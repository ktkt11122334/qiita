package com.stock.service.stock;

import java.util.List;
import java.util.Map;

import com.stock.entity.Stock;
import com.stock.entity.form.stock_master.SaveStock;
import com.stock.entity.form.stock_master.SelectProductStockList;



public interface StockService {

  public List<Stock> getStockList();

  public Stock findOneStockByProductId(Long product_id);

  public List<Map<String, Object>> findProductStockList(SelectProductStockList selectProductStockList);

  public List<Map<String, Object>> registStock(SaveStock saveStock) throws Exception;

}
