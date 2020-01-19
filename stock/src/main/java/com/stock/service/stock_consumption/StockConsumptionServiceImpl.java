package com.stock.service.stock_consumption;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stock.repository.StockConsumptionRepository;



@Service
public class StockConsumptionServiceImpl implements StockConsumptionService {

  @Autowired
  private StockConsumptionRepository stockConsumptionRepository;



  @Override
  public List<Map<String, Object>> list() {
    return stockConsumptionRepository.findAllWithProductInfo();
  }

}
