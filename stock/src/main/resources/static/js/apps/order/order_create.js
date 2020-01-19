Class$OrderCreate = Class.create(Class$Base, {


  /**
   * Constoructor
   */
  initialize : function($super) {
    $super();
  },




  // カウンタ関数の初期化
  counter : CommonProcess.getCountFunction(),

  // 商品選択一覧から商品Idに紐づく商品単価を保持するマップ
  convertProductIdToPrice : new Map(),


  /**
   * @Override
   * htmlテキストロード後に実行される関数
   */
  onload : function() {

    // 購入商品カウンタのリセットを行う
    this.counter.setCount( $('purchaseProductSettings').childElements().length - 1 );

    // 商品一覧の価格の小数切り上げ(税率計算)
    var selectProductTotalPrices = $('selectProductTableBody').getElementsBySelector('.js-select-table--total-price');
    if ( selectProductTotalPrices != null)
      CommonProcess.formatNumber(selectProductTotalPrices);

    // 商品一覧に表示されている商品Idと商品価格のマッピング
    var selectListProductsId = $('selectProductTableBody').getElementsBySelector('.js-select-table--product-id');
    var selectListProductsPrice= $('selectProductTableBody').getElementsBySelector('.js-select-table--total-price');
    for ( var i = 0; i < selectListProductsId.length; i++ )
      this.convertProductIdToPrice.set( selectListProductsId[i].getAttribute('value'), selectListProductsPrice[i].getAttribute("value") );
  },



  /**
   * 購入商品の行の削除時の動作
   * * click function
   */
  removeTargetProduct : function(removeTargetElm) {

    // 削除対象行の要素を削除
    CommonProcess.removeRowElement(removeTargetElm);
    // 削除に伴う行カウンターのカウントダウン
    this.counter.countDown();
    // 購入商品欄削除に伴う金額合計の再計算
    this.recalculateTotalPrice();
  },



  /**
   * 購入商品の合計金額を算出/表示するメソッド 各購入商品のvalue値も同時に変更する
   * *click function
   */
  recalculateTotalPrice : function() {

    var purchaseProductId = $('purchaseProductSettings').getElementsBySelector('.js-input__purchase-product-id');
    var purchaseProductNumber = $('purchaseProductSettings').getElementsBySelector('.js-input__purchase-product-number');
    var purchaseProductEachTotalPrice = $('purchaseProductSettings').getElementsBySelector('.js-input__purchase-product-price');

    var purchaseProductRowData = purchaseProductNumber.length;
    if ( purchaseProductRowData == 0 ) return;



    var totalPrice = 0;
    for ( var i = 0; i < purchaseProductRowData; i++ ) {

      // 商品毎の合計金額取得
      var purchaseNum = parseInt( purchaseProductNumber[i].value );
      var productPrice = parseInt( this.convertProductIdToPrice.get( purchaseProductId[i].value ) );
      var eachTotalPrice = purchaseNum * productPrice;

      // 要素毎のvalueの更新
      purchaseProductNumber[i].setAttribute( 'value', purchaseNum );             // 購入商品個数のvalue更新
      purchaseProductEachTotalPrice[i].setAttribute( 'value', eachTotalPrice );  // 購入各商品価格のvalue更新

      // 購入合計の計算
      totalPrice += eachTotalPrice;
    }

    $('createOrderPrice').setAttribute("value", totalPrice);
  },



  /**
   * 商品購入一覧を動的html追加するメソッド
   * *click function
   */
  setPurchaseProduct : function(rowData) {

    var purchaseProductElm = $('purchaseProductSettings');
    purchaseProductElm.innerHTML = purchaseProductElm.innerHTML + this.getProductInfoTemplate(rowData, this.counter.countUp() );
  },

  getProductInfoTemplate : function(rowData, cnt) {

    // テンプレートにrowDataから取得した各値を挿入すればいける
    var selectedProductName = rowData.getElementsByClassName('js-select-table--product-name')[0].getAttribute('value');
    var selectedProductCode = rowData.getElementsByClassName('js-select-table--product-code')[0].getAttribute('value');
    var purchaseNumber = 0;
    var selectedTotalPrice = 0;
    // var selectedTotalPrice = rowData.getElementsByClassName('js-select-table--total-price')[0].getAttribute('value');
    var selectedProductId = rowData.getElementsByClassName('js-select-table--product-id')[0].getAttribute('value');
    var selectedProductLastModifiedTs = rowData.getElementsByClassName('js-select-table--product-last-modified-ts')[0].getAttribute('value');

    var template =
    '<tr>' +
      '<td><input class="common__input-not-focus" type=\"text\" name=\"orderDetails[' + cnt + '].createProductName\" value=\"' + selectedProductName + '\" number=\"' + cnt + '\" readonly/></td>' +
      '<td><input class="common__input-not-focus" type=\"text\" name=\"orderDetails[' + cnt + '].createProductCode\" value=\"' + selectedProductCode + '\" number=\"' + cnt + '\" readonly/></td>' +
      '<td><input type=\"text\" class="js-input__purchase-product-number" name=\"orderDetails[' + cnt + '].createPurchaseNumber\" value=\"' + purchaseNumber + '\" number=\"' + cnt + '\" onChange=\"createOrderInstance.recalculateTotalPrice();\"/>' +
      '<td><input class="common__input-not-focus js-input__purchase-product-price" type=\"text\" name=\"orderDetails[' + cnt + '].createTotalPrice\" value=\"' + selectedTotalPrice + '\" number=\"' + cnt + '\" readonly/></td>' +
      '<td style=\"display: none;\"><input type=\"text\" class=\"common__input-not-focus js-input__purchase-product-id\" name=\"orderDetails[' + cnt + '].createProductId\" value=\"' + selectedProductId + '\" number=\"' + cnt + '\"></td>' +
      '<td style=\"display: none;\"><input type=\"text\" class=\"common__input-not-focus\" name=\"orderDetails[' + cnt + '].createProductLastModifiedTs\" value=\"' + selectedProductLastModifiedTs + '\" number=\"' + cnt + '\"></td>' +
      '<td><button type=\"button\" onclick="createOrderInstance.removeTargetProduct(this)">削除</button></td>'
    '</tr>';

    return template;
  },



  /**
   * 検索条件に紐づく商品一覧取得より商品選択一覧を再描画する
   */
  searchProductList : function() {

    var url = '/rest/search_product';
    var type = 'POST';
    var data = JSON.stringify({
      'searchProductCode' : $('searchProductCode').value,
      'searchProductName' : $('searchProductName').value
    });
    var contetType = 'application/json';
    var dataType = 'json';


    CommonProcess.doAjax(url, type, data, contetType, dataType, function(result) {

      var selectProductListTable = $('selectProductTableBody');
      selectProductListTable.innerHTML = ''; // 商品選択一覧を初期化

      result.forEach( function(productInfo) {
        selectProductListTable.innerHTML += this.setSelectProductListTemplate(productInfo);
      }.bind(this));
    }.bind(this));

  },

  setSelectProductListTemplate : function(productInfo) {

    var totalPrice = Math.ceil( productInfo.price + ( productInfo.price * productInfo.tax_rate * 0.01 ) );

    var template =
      '<tr class=\"create-contents__form-select-product__table__tbody__tr list-contents__table-body__tr\" onclick=\"createOrderInstance.setPurchaseProduct(this);\">' +
        '<td class=\"js-select-table--product-name width180\" value=\"' + productInfo.product_name + '\">' + productInfo.product_name + '</td>' +
        '<td class=\"js-select-table--product-code width180\" value=\"' + productInfo.product_code + '\">' + productInfo.product_code + '</td>' +
        '<td class=\"js-select-table--total-price width100\" value=\"' + totalPrice + '\">' + totalPrice + '</td>' +
        '<td class=\"js-select-table--price width100\" value=\"' + productInfo.price + '\">' + productInfo.price + '</td>' +
        '<td class=\"js-select-table--tax-rate width50\" value=\"' + productInfo.tax_rate + '\">' + productInfo.tax_rate + '</td>' +
        '<td style=\"display: none;\" class=\"js-select-table--product-id\" value=\"' + productInfo.product_id + '\">' + productInfo.product_id + '</td>' +
        '<td style=\"display: none;\" class=\"js-select-table--product-last-modified-ts\" value=\"' + productInfo.last_modified_ts + '\">' + productInfo.last_modified_ts + '</td>' +
      '</tr>';

    return template;
  },

});