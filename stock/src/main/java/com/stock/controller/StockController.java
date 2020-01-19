package com.stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stock.entity.ProductMaster;
import com.stock.entity.ProductMasterHistory;
import com.stock.entity.Stock;
import com.stock.entity.form.stock_master.SaveStock;
import com.stock.entity.form.stock_master.SelectProductStockList;
import com.stock.service.product_master_settings.ProductMasterSettingsService;
import com.stock.service.stock.StockService;



@Controller
@RequestMapping("stock_master")
public class StockController {

  @Autowired
  private StockService stockService;

  @Autowired
  private ProductMasterSettingsService productMasterSettingsService;



  @RequestMapping( value = "list", method = RequestMethod.GET )
  public String getProductStockList(@ModelAttribute SelectProductStockList selectProductStockList , Model model ) {
    model.addAttribute( "productStockList", stockService.findProductStockList(selectProductStockList) );
    model.addAttribute( "searchProductStockObject", selectProductStockList );

    return "stock_master/list";
  }



  @RequestMapping( value = "list", method = RequestMethod.POST )
  public String search(@ModelAttribute SelectProductStockList selectProductStockList , Model model ) {
    model.addAttribute( "productStockList", stockService.findProductStockList(selectProductStockList) );
    model.addAttribute( "searchProductStockObject", selectProductStockList );

    return "stock_master/list";
  }



  @RequestMapping( value = "save/{productId}", method = RequestMethod.GET )
  public String save(@PathVariable Long productId, Model model) {

    ProductMaster productMaster = productMasterSettingsService.getOneProductMaster(productId);
    ProductMasterHistory productMasterHistory = productMasterSettingsService.findOneProductMasterHistory( productId, productMaster.getLastModifiedTs() );
    Stock stock = stockService.findOneStockByProductId(productId);

    model.addAttribute( "productMaster", productMaster);
    model.addAttribute( "productMasterHistory", productMasterHistory);
    // エラーからのリダイレクト時は、stockData値が保持されるのでmodelに追加しない
    if ( model.getAttribute( "stockData" ) == null ) {
      if ( stock == null ) // 未登録の場合
        stock = new Stock();
      model.addAttribute( "stockData", stock );
    }

    return "stock_master/save";
  }



  @RequestMapping( value = "save", method = RequestMethod.POST )
  public String regist(RedirectAttributes redirectAttribute, @ModelAttribute @Validated SaveStock saveStock, BindingResult bindingResult, Model model) {

    if ( bindingResult.hasErrors() ) {

      // エラーメッセージ取得
      if ( bindingResult.getFieldError("actualStockNumber") != null)
        redirectAttribute.addFlashAttribute( "actualStockNumberError", bindingResult.getFieldError("actualStockNumber").getDefaultMessage() );
      if ( bindingResult.getFieldError("standardStockNumber") != null)
        redirectAttribute.addFlashAttribute( "standardStockNumberError", bindingResult.getFieldError("standardStockNumber").getDefaultMessage() );

      redirectAttribute.addFlashAttribute( "stockData", saveStock );
      return "redirect:" + ( saveStock.getStockId() != null ? "save/" + saveStock.getStockId() : "save/" + saveStock.getProductId() );
    }

    try {
      stockService.registStock(saveStock);
    }
    catch(Exception e) {
      redirectAttribute.addFlashAttribute("error", e.getMessage());
    }

    return "redirect:list";
  }

}
