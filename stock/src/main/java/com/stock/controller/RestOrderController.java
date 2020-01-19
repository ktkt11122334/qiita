package com.stock.controller;

import java.lang.Thread.State;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.stock.app_utils.CreateOrderUtil;
import com.stock.app_utils.OrderShip;
import com.stock.entity.form.product_master.SelectProductMasterList;
import com.stock.service.order.create_order.CreateOrderService;
import com.stock.service.product_master_settings.ProductMasterSettingsService;



@RestController
@RequestMapping("rest")
public class RestOrderController {

  @Autowired
  private ProductMasterSettingsService productMasterSettingsService;

  @Autowired
  private CreateOrderService createOrderService;

  private OrderShip orderShip = new OrderShip();


  // curl http://localhost:8080/rest/search_product -v -X POST -H "Content-Type: application/json" -d "{\"searchProductCode\" : \"test\", \"searchProductName\" : \"test\"}";
  @RequestMapping( value = "search_product", method = RequestMethod.POST )
  public List<Map<String, Object>> getSearchProduct(@RequestBody SelectProductMasterList selectProductMasterList) {

    List<Map<String, Object>> productList = CreateOrderUtil.getSelectProductList( selectProductMasterList, productMasterSettingsService );
    CreateOrderUtil.setLocalDateTimeFormat(productList, "last_modified_ts", "yyyy-MM-dd HH:mm:ss.SSS");

    return productList;
  }



  @RequestMapping( value = "order_ship", method = RequestMethod.GET )
  public String getSearchProduct() {


    // 2つ以上のスレッドがThread.start()を同時実行する処理を防ぐ排他制御を行う
    synchronized( this.orderShip ) {

      if ( this.orderShip.getState().equals(State.TERMINATED)) {
          this.orderShip = new OrderShip();
      }

      if ( this.orderShip != null && !this.orderShip.isAlive() ) {
        String shippingMsg = createOrderService.shipOrder(this.orderShip);
        return shippingMsg;
      }
    }

    return "出荷指示処理は他者によって現在起動中です。";
  }


}
