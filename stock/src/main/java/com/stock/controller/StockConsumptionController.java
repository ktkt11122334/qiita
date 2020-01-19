package com.stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.stock.service.stock_consumption.StockConsumptionServiceImpl;



@Controller
@RequestMapping( value = "stock_consumption")
public class StockConsumptionController {


  @Autowired
  StockConsumptionServiceImpl stockConsumptionService;



  @RequestMapping( value = "list", method = RequestMethod.GET )
  public String list(Model model) {

    model.addAttribute("stockConsumptions", stockConsumptionService.list() );

    return "stock_consumption/list";
  }

}
