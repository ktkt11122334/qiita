package com.stock.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stock.app_utils.CreateOrderUtil;
import com.stock.entity.form.order.CreateOrder;
import com.stock.entity.form.product_master.SelectProductMasterList;
import com.stock.service.order.create_order.CreateOrderService;
import com.stock.service.product_master_settings.ProductMasterSettingsService;



@Controller
@RequestMapping("order")
public class OrderController {

  @Autowired
  private ProductMasterSettingsService productMasterSettingsService;

  @Autowired
  private CreateOrderService createOrderService;



  @RequestMapping( value = "create", method = RequestMethod.GET)
  public String create(@ModelAttribute CreateOrder createOrder, Model model) {

    // 注文情報作成に使用するモデルを定義
    if ( model.getAttribute("orderHeader") == null )
      model.addAttribute( "orderHeader", createOrder.getOrderHeader() );

    if ( model.getAttribute("orderDetails") == null )
      model.addAttribute( "orderDetails", createOrder.getOrderDetails() );

    if ( model.getAttribute("customer") == null )
      model.addAttribute( "customer", createOrder.getCustomer() );

    // 商品一覧を取得
    List<Map<String, Object>> productList = CreateOrderUtil.getSelectProductList( new SelectProductMasterList(), productMasterSettingsService );
    CreateOrderUtil.setLocalDateTimeFormat(productList, "last_modified_ts", "yyyy-MM-dd HH:mm:ss.SSS");

    model.addAttribute( "productList", productList);

    return "order/order_create";
  }



  @RequestMapping( value = "save", method = RequestMethod.POST)
  public String save(RedirectAttributes redirectAttribute,  @ModelAttribute @Validated CreateOrder createOrder, BindingResult bindingResult, Model model) {


    // バリデーション
    if ( bindingResult.hasErrors() ) {

      // Order
      if ( bindingResult.getFieldError("order.createOrderPrice") != null)
        redirectAttribute.addFlashAttribute( "createOrderPriceError", bindingResult.getFieldError("order.createOrderPrice").getDefaultMessage() );


      // OrderDetails
      List<CreateOrder.OrderDetail> orderDetails = createOrder.getOrderDetails();
      for ( int i = 0; i < orderDetails.size(); i++ ) {

        String errorRowPurchaseNumber = "orderDetails[" + i + "].createPurchaseNumber";
        if ( bindingResult.getFieldError(errorRowPurchaseNumber) != null ) {
          orderDetails.get(i).setCreatePurchaseNumberErr(Boolean.TRUE);
          redirectAttribute.addFlashAttribute( "createPurchaseNumberError", bindingResult.getFieldError(errorRowPurchaseNumber).getDefaultMessage() );
        }

//      改行方法の調査はいずれ調査
//      purchaseProductErrMsg.append( i + "行目:" + bindingResult.getFieldError(errorRowFirstName).getDefaultMessage() + "<br/>" );
      }

      // Customer
      if ( bindingResult.getFieldError("customer.createFirstName") != null)
        redirectAttribute.addFlashAttribute( "createFirstNameError", bindingResult.getFieldError("customer.createFirstName").getDefaultMessage() );

      if ( bindingResult.getFieldError("customer.createLastName") != null)
        redirectAttribute.addFlashAttribute( "createLastNameError", bindingResult.getFieldError("customer.createLastName").getDefaultMessage() );

      if ( bindingResult.getFieldError("customer.createTellNumber") != null)
        redirectAttribute.addFlashAttribute( "createTellNumberError", bindingResult.getFieldError("customer.createTellNumber").getDefaultMessage() );

      if ( bindingResult.getFieldError("customer.createPostalCode") != null)
        redirectAttribute.addFlashAttribute( "createPostalCodeError", bindingResult.getFieldError("customer.createPostalCode").getDefaultMessage() );

      if ( bindingResult.getFieldError("customer.createFirstAddress") != null)
        redirectAttribute.addFlashAttribute( "createFirstAddressError", bindingResult.getFieldError("customer.createFirstAddress").getDefaultMessage() );

      if ( bindingResult.getFieldError("customer.createLastAddress") != null)
        redirectAttribute.addFlashAttribute( "createLastAddressError", bindingResult.getFieldError("customer.createLastAddress").getDefaultMessage() );

      if ( bindingResult.getFieldError("customer.createMailAddress") != null)
        redirectAttribute.addFlashAttribute( "createMailAddressError", bindingResult.getFieldError("customer.createMailAddress").getDefaultMessage() );


      CreateOrderUtil.setRedirectAttributeToCreate(createOrder, redirectAttribute);

      return "redirect:create";
    }

    try {
      createOrderService.registOrder(createOrder);
    }
    catch ( Exception e ) {
      CreateOrderUtil.setRedirectAttributeToCreate(createOrder, redirectAttribute);
      redirectAttribute.addFlashAttribute( "registrationErrMsg", e.getMessage() );
      return "redirect:create";
    }

    return "redirect:/menu";
  }



  @RequestMapping( value = "list", method = RequestMethod.GET )
  public String list(Model model) {
    List<Map<String, Object>> list = createOrderService.list();
    model.addAttribute("orderList", list);
    return "order/list";
  }


}
