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

import com.stock.entity.Tax;
import com.stock.service.tax.TaxService;



@Controller
@RequestMapping("tax_master")
public class TaxController {

  @Autowired
  private TaxService taxService;

//  @InitBinder
//  public void initBinder(WebDataBinder binder) {
//    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//    dateFormat.setLenient(false);
//    binder.registerCustomEditor( java.sql.Date.class, new CustomDateEditor(dateFormat, true) );
//
//  }


  @RequestMapping("list")
  public String list(Model model) {
    model.addAttribute("taxList", taxService.getTaxList());

    return "tax_master/list";
  }



  /**
   * 設計変更により未使用なメソッド
   */
  @Deprecated
  @RequestMapping( value = "save/{taxId}", method = RequestMethod.GET )
  public String save(@PathVariable Long taxId, Model model) {
    if ( model.getAttribute("tax") == null )
      model.addAttribute("tax", taxService.findOneTax(taxId));

    return "tax_master/save";
  }



  @RequestMapping( value = "create", method = RequestMethod.GET )
  public String creat(Model model) {
    if ( model.getAttribute("tax") == null )
      model.addAttribute("tax", new Tax());

    return "tax_master/save";
  }



  @RequestMapping( value = "save", method = RequestMethod.POST)
  public String regist(RedirectAttributes redirectAttribute, @ModelAttribute @Validated Tax tax, BindingResult bindingResult, Model model) {

    if ( bindingResult.hasErrors() ) {
//      *メモ書き
//      for( ObjectError error : bindingResult.getAllErrors() ) {
//                                              *** フィールド名が取得できない・・・
//        redirectAttribute.addFlashAttribute(error.getObjectName() + "Error", error.getDefaultMessage());
//      }

      // エラーメッセージ取得
      if ( bindingResult.getFieldError("taxRate") != null)
        redirectAttribute.addFlashAttribute( "taxRateError", bindingResult.getFieldError("taxRate").getDefaultMessage() );
      if ( bindingResult.getFieldError("fromChangeDate") != null)
        redirectAttribute.addFlashAttribute( "fromChangeDateError", bindingResult.getFieldError("fromChangeDate").getDefaultMessage() );

      redirectAttribute.addFlashAttribute( "tax", tax );
      return "redirect:" + ( tax.getTaxId() != null ? "save/" + tax.getTaxId() : "create" );
    }

    try {
      taxService.registTax(tax);
    }
    catch(Exception e) {
      redirectAttribute.addFlashAttribute("error", e.getMessage());
    }

    return "redirect:list";
  }



  /**
   * 指定のtaxをdisableに設定する(画面操作からのable設定不可)
   */
  @RequestMapping( value = "delete/{taxId}", method = RequestMethod.GET )
  public String delete(@PathVariable Long taxId, Model model) {
    taxService.deleteTax(taxId);

    return "redirect:/tax_master/list";
  }


}
