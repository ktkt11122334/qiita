package com.stock.controller;

import java.util.List;

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
import com.stock.entity.Tax;
import com.stock.entity.form.product_master.SaveProductMaster;
import com.stock.entity.form.product_master.SelectProductMasterList;
import com.stock.service.product_master_settings.ProductMasterSettingsService;
import com.stock.service.tax.TaxService;



@Controller
@RequestMapping("product_master")
public class ProductMasterSettingsController {

  @Autowired
  private ProductMasterSettingsService productMasterSettingsService;

  @Autowired
  private TaxService taxService;



  // 初期表示
  @RequestMapping( value = "list", method = RequestMethod.GET ) // ModelAttributeは各要素名に初期値でnullが格納される
  public String list(@ModelAttribute SelectProductMasterList selectProductMasterList, Model model) {
    model.addAttribute( "tax", taxService.getTaxList() );
    model.addAttribute( "searchProductMasterObject", selectProductMasterList );
    model.addAttribute( "productList", productMasterSettingsService.getProductMasterList(selectProductMasterList) );

    return "product_master/list";
  }



  @RequestMapping( value = "list", method = RequestMethod.POST )
  public String search(@ModelAttribute SelectProductMasterList selectProductMasterList, Model model) {
    model.addAttribute( "tax", taxService.getTaxList() );
    model.addAttribute( "searchProductMasterObject", selectProductMasterList );
    model.addAttribute( "productList", productMasterSettingsService.getProductMasterList(selectProductMasterList) );

    return "product_master/list";
  }



  @RequestMapping( value = "save/{productId}", method = RequestMethod.GET )
  public String save(@PathVariable Long productId, Model model) {

    ProductMaster productMaster = productMasterSettingsService.getOneProductMaster(productId);
    ProductMasterHistory productMasterHistory = productMasterSettingsService.findOneProductMasterHistory( productId, productMaster.getLastModifiedTs() );
    Tax tax = taxService.findOneTax( productMasterHistory.getTax().getTaxId() );

    if ( model.getAttribute("productMaster") == null )
      model.addAttribute( "productMaster", productMaster);

    if ( model.getAttribute("productMasterHistory") == null )
    model.addAttribute( "productMasterHistory", productMasterHistory);

    if ( model.getAttribute("taxMaster") == null )
      model.addAttribute( "taxMaster", tax);


    List<Tax> taxMaster = taxService.getTaxList();
    model.addAttribute( "taxMasterList", taxMaster);

    return "product_master/save";
  }



  @RequestMapping( value = "create", method = RequestMethod.GET )
  public String creat(Model model) {

    if ( model.getAttribute("productMaster") == null )
      model.addAttribute( "productMaster", new ProductMaster() );
    if ( model.getAttribute("productMasterHistory") == null )
      model.addAttribute( "productMasterHistory", new ProductMasterHistory() );
    if ( model.getAttribute("taxMaster") == null )
      model.addAttribute( "taxMaster", new Tax() );

    List<Tax> taxMaster = taxService.getTaxList();
    model.addAttribute( "taxMasterList", taxMaster);

    return "product_master/save";
  }



  @RequestMapping( value = "save", method = RequestMethod.POST)
  public String regist(RedirectAttributes redirectAttribute, @ModelAttribute @Validated SaveProductMaster productMaster, BindingResult bindingResult, Model model) {


    if ( bindingResult.hasErrors() ) {

      // エラーメッセージ取得
      if ( bindingResult.getFieldError("productCode") != null)
        redirectAttribute.addFlashAttribute( "productCodeError", bindingResult.getFieldError("productCode").getDefaultMessage() );
      if ( bindingResult.getFieldError("jancode") != null)
        redirectAttribute.addFlashAttribute( "jancodeError", bindingResult.getFieldError("jancode").getDefaultMessage() );
      if ( bindingResult.getFieldError("productName") != null)
        redirectAttribute.addFlashAttribute( "productNameError", bindingResult.getFieldError("productName").getDefaultMessage() );
      if ( bindingResult.getFieldError("price") != null)
        redirectAttribute.addFlashAttribute( "priceError", bindingResult.getFieldError("price").getDefaultMessage() );
      if ( bindingResult.getFieldError("taxId") != null)
        redirectAttribute.addFlashAttribute( "taxIdError", bindingResult.getFieldError("taxId").getDefaultMessage() );


      // リダイレクト先に更新パラメータを保持
      ProductMaster pm = new ProductMaster();
      pm.setProductId( productMaster.getProductId() );
      pm.setProductCode( productMaster.getProductCode() );
      pm.setJancode( productMaster.getJancode() );
      pm.setDisableFlg( productMaster.getDisableFlg() );
      pm.setCreateTs( productMaster.getProductMasterCreateTs() );
      pm.setLastModifiedTs( productMaster.getProductMasterLastModifiedTs() );
      redirectAttribute.addFlashAttribute( "productMaster", pm );

      ProductMasterHistory pmh = new ProductMasterHistory();
      pmh.setProductName( productMaster.getProductName() );
      pmh.setPrice( productMaster.getPrice() );
      pmh.setCreateTs( productMaster.getProductMasterHistoryCreateTs() );
      pmh.setLastModifiedTs( productMaster.getProductMasterHistoryLastModifiedTs() );
      redirectAttribute.addFlashAttribute( "productMasterHistory", pmh );

      Tax tax;
      if ( productMaster.getTaxId() == null ) {
        tax = new Tax();
      }
      else {
        tax = taxService.findOneTax( productMaster.getTaxId() );
      }
      redirectAttribute.addFlashAttribute( "taxMaster", tax );


      return "redirect:" + ( productMaster.getProductId() != null ? "save/" + productMaster.getProductId() : "create" );
    }

    try {
      productMasterSettingsService.registProduct(productMaster);
    }
    catch(Exception e) {
      redirectAttribute.addFlashAttribute("error", e.getMessage());
    }

    return "redirect:list";
  }

}
