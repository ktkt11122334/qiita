package com.stock.service.tax;

import java.util.List;

import com.stock.entity.Tax;



public interface TaxService {

  public List<Tax> getTaxList();

  public Tax findOneTax(Long taxId);

  public Tax registTax(Tax tax) throws Exception;

  public void deleteTax(Long tax);

}
