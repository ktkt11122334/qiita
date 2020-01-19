package com.stock.service.order.create_order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import com.stock.app_utils.CreateOrderUtil;
import com.stock.app_utils.OrderShip;
import com.stock.cnst.ColumnConst;
import com.stock.entity.Customer;
import com.stock.entity.OrderDetail;
import com.stock.entity.OrderHeader;
import com.stock.entity.ProductMasterHistoryId;
import com.stock.entity.Stock;
import com.stock.entity.form.order.CreateOrder;
import com.stock.repository.CustomerRepository;
import com.stock.repository.OrderDetailRepository;
import com.stock.repository.OrderRepository;
import com.stock.repository.ProductMasterHistorySettingsRepository;
import com.stock.repository.StockConsumptionRepository;
import com.stock.repository.StockRepository;



@Service
@Transactional
public class CreateOrderServiceImpl implements CreateOrderService {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private OrderDetailRepository orderDetailRepository;

  @Autowired
  private ProductMasterHistorySettingsRepository productHistoryRepository;

  @Autowired
  private StockRepository stockRepository;

  @Autowired
  private StockConsumptionRepository stockConsumptionRepository;

  @Autowired
  private PlatformTransactionManager transactionManager;



  @Override
  public void registOrder(CreateOrder createOrder) throws Exception {

    List<CreateOrder.OrderDetail> orderDetails = createOrder.getOrderDetails();

    // 購入した商品の税率が税率日付変更前か確認する
    List<Map<String, Object>> fromChaneDateBeforeNow = CreateOrderUtil.confirmTaxChangeDate(orderDetails, productHistoryRepository);
    if ( fromChaneDateBeforeNow.size() > 0 ) {

      StringBuilder errMsg = new StringBuilder();
      fromChaneDateBeforeNow.stream().forEach( errorContents -> {

        String errorProductName = (String)errorContents.get("product_name");
        String errorBeforeChangeDate = errorContents.get("from_change_date").toString();
        errMsg.append(errorProductName + "の税率変更日付[" + errorBeforeChangeDate + "]が現在日付より後です。" );
      });

      throw new Exception( errMsg.toString() );
    }



    // 最終更新日時
    LocalDateTime nowTimeStamp = LocalDateTime.now();

    // 顧客情報がすでに存在するか確認
    CreateOrder.Customer customerInfo = createOrder.getCustomer();
    Customer customer = CreateOrderUtil.getCustomerExistence(customerInfo, customerRepository);
    // Customerの登録
    if ( customer == null ) {
      customer = new Customer();
      customer.setFirstName( customerInfo.getCreateFirstName() );
      customer.setLastName( customerInfo.getCreateLastName() );
      customer.setTellNumber( customerInfo.getCreateTellNumber() );
      customer.setCreateTs( nowTimeStamp );
    }
    customer.setPostalCode( customerInfo.getCreatePostalCode() );
    customer.setFirstAddress( customerInfo.getCreateFirstAddress() );
    customer.setLastAddress( customerInfo.getCreateLastAddress() );
    customer.setMailAddress( customerInfo.getCreateMailAddress() );
    customer.setLastModifiedTs( nowTimeStamp );
    customerRepository.save(customer);


    // Orderの登録
    CreateOrder.Order orderInfo = createOrder.getOrderHeader();
    OrderHeader order = new OrderHeader();
    order.setOrderPrice( orderInfo.getCreateOrderPrice() );
    order.setOrderStatus( ColumnConst.ORDER_STATUS_10_BEFORE_SHIP );
    order.setCustomer(customer);
    order.setCreateTs( nowTimeStamp );
    order.setLastModifiedTs( nowTimeStamp );
    orderRepository.save( order );


    // OrderDetailsの登録
    for( int i = 0; i < orderDetails.size(); i++ ) {
      Long orderDetailId = new Long( i + 1 );

      // 注文明細
      CreateOrder.OrderDetail orderDetailInfo = orderDetails.get(i);
      OrderDetail orderDetail = new OrderDetail();

      ProductMasterHistoryId pmhId = new ProductMasterHistoryId();
      pmhId.setProductId( orderDetailInfo.getCreateProductId() );
      pmhId.setLastModifiedTs( orderDetailInfo.getCreateProductLastModifiedTs() );
      orderDetail.setProductMasterHistory(productHistoryRepository.getOne(pmhId));

      orderDetail.setOrderDetailId(orderDetailId);
      orderDetail.setOrderId( order.getOrderId() );
      orderDetail.setPurchaseNumber( orderDetailInfo.getCreatePurchaseNumber() );
      orderDetail.setCreateTs( nowTimeStamp );
      orderDetail.setLastModifiedTs( nowTimeStamp );
      orderDetailRepository.save(orderDetail);




      // 将来出荷予定在庫数の変更を行う
      Stock stock = stockRepository.findOneStockByProductId( orderDetailInfo.getCreateProductId() );
      stock.setFutureShippedStockNumber(
        Integer.valueOf( stock.getFutureShippedStockNumber().intValue() + orderDetailInfo.getCreatePurchaseNumber().intValue() )
      );
      stock.setLastModifiedTs(nowTimeStamp);
      stockRepository.save(stock);

    }


  }



  @Override
  public String shipOrder(OrderShip orderShip) {

    orderShip.setRepository(transactionManager, orderRepository, orderDetailRepository, stockRepository, stockConsumptionRepository);
    Integer shipmentCount = orderRepository.getUnshippedOrderCount();
    // スレッドタスク
    orderShip.start();

    StringBuilder sb = new StringBuilder();
    if ( shipmentCount.intValue() != 0 ) {

      sb.append("出荷指示処理を開始しました。");
      sb.append("更新対象の注文情報は");
      sb.append(shipmentCount.toString());
      sb.append("件です。");
      sb.append("処理が完了次第、管理者メールアドレスに更新完了メールを送信します。");
    }
    else {
      sb.append("出荷対象のデータは0件です。");
    }

    return sb.toString();

  }



  @Override
  public List<Map<String, Object>> list() {
    return orderRepository.getListOrderHeaderWithCustomer();
  }

}
