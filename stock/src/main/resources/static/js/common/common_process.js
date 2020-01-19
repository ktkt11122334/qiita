var CommonProcess = {

  /**
   * サーバーレスポンスのチェックボックス状態をHTMLテキストに反映する
   */
  changeCheckboxCondition : function( chkboxElm ) {
    chkboxElm.checked = ( chkboxElm.value == "" ||  chkboxElm.value == 'false' ) ? false : true;
  },



  /**
   * 日時フォーマットの整形
   */
  dateFormat : {
      _fmt : {
               'yyyy': function(date) { return date.getFullYear() + ''; },
               'MM': function(date) { return ('0' + (date.getMonth() + 1)).slice(-2); },
               'dd': function(date) { return ('0' + date.getDate()).slice(-2); },
               'hh': function(date) { return ('0' + date.getHours()).slice(-2); },
               'mm': function(date) { return ('0' + date.getMinutes()).slice(-2); },
               'ss': function(date) { return ('0' + date.getSeconds()).slice(-2); },
               'SSS': function(date) { return date.getMilliseconds(); }
             },
             _priority : ['yyyy', 'MM', 'dd', 'hh', 'mm', 'ss', 'SSS'],
             format: function(date, format){
               return this._priority.reduce((res, fmt) => res.replace(fmt, this._fmt[fmt](date)), format)
             }
   },

   formatTimestamp : function (listElm, timestampClassName, dateTimeFormat) {
     var timestampElms = listElm.getElementsBySelector( timestampClassName );
     if ( timestampElms == null) return;

     timestampElms.forEach(function(tsElement) {
       if ( tsElement.innerHTML != "" )
         tsElement.innerHTML = this.dateFormat.format( new Date(tsElement.innerHTML), dateTimeFormat );
     }.bind(this) );
   },



   /**
    *  注文IDのゼロ埋め整形
    */
   formatOrderId : function(listElm, timestampClassName, numberOfDigit) {
     var orderIdElms = listElm.getElementsBySelector( timestampClassName );
     if ( orderIdElms == null) return;

     orderIdElms.forEach( function(orderId) {
       if ( orderId.innerHTML != "" )
         orderId.innerHTML = ('0000000000' + orderId.innerHTML).slice(-numberOfDigit);
     });
   },


   /**
    *  一覧の行データ選択を行うと、行データの特定のクラスに設定されている値からリクエストパスを取得し、サーバーリクエストを行う
    */
   selectRowData : function(rowData, targetClassName) {
     var requestPath = rowData.getElementsByClassName(targetClassName)[0].value;
     window.location.href = requestPath;
   },



   /**
    *  一覧の行データの削除ボタンによってdisableに設定する
    */
   deleteRowData : function(rowData) {
     window.location.href = rowData.value;
   },



   /**
    * カウンターの定義 表組みの表増減に使用
    */
   getCountFunction : function() {

     var counter = (function(){
       var num = -1;

       return {

         countUp : function() {
           return ++num;
         },
         countDown : function() {
           return --num;
         },
         setCount : function(initializeNumber) {
           num = initializeNumber;
         },
       };

     })();

     return counter;
   },



   /**
    * *再帰関数
    * 表組みの行削除に伴う行番号の調整を行う
    */
   resetNumberAttribute : function(processElement) {

     for ( var i = 0; processElement.length > i; i++ ) {

       var numAttribute = processElement[i].getAttribute("number");
       if ( numAttribute == null ) {
         if ( processElement[i].childElements().length != 0 )
           this.resetNumberAttribute(processElement[i].childElements());
       }
       else {
         var nameAttribute = processElement[i].getAttribute("name");
         processElement[i].setAttribute("number", numAttribute - 1);
         processElement[i].setAttribute("name", nameAttribute.replace(/\[\d+\]/, '[' + (numAttribute - 1) + ']' ) );
       }
     }

   },

   /**
    * 表組みの行削除を行う *resetNumberAttribute関数を使用する
    */
   removeRowElement : function(removeTargetRowElm) {

     while ( removeTargetRowElm.tagName != 'TR' ) {
       var removeTargetRowElm = removeTargetRowElm.parentNode;
     }

     var nextTrElement = removeTargetRowElm.next();
     while( typeof nextTrElement != 'undefined' ) {

       var childElms = nextTrElement.childElements();
       this.resetNumberAttribute(childElms);

       nextTrElement = nextTrElement.next();
     }

     removeTargetRowElm.remove();
   },



   /**
    *  数値の整形 価格・・(税率の小数桁切り上げ計算など)
    */
   formatNumber : function(numberElements) {

     var roundUpNumber = function(numberElm) {
       numberElm.innerHTML = Math.ceil(numberElm.innerHTML);
       numberElm.setAttribute("value", numberElm.innerHTML);
     };

     if ( Array.isArray(numberElements) ) {

       numberElements.forEach( function(numberElm) {
         roundUpNumber(numberElm);
       });
     }
     else {
       roundUpNumber(numberElements);
     }

   },



   /**
    * Ajaxでリクエストを送信する
    */
   doAjax : function(url, type, data, contetType, dataType, callback) {

     jQuery.ajax({
       url : url,
       type : type,
       data : data,
       contentType : contetType,
       dataType    : dataType,
       success     : function(data) {
                       callback(data);
                   },
       error       : function(XMLHttpRequest, textStatus, errorThrown) {
                       callback("リクエストエラー：管理者に報告してください。");
                   }
     });

   }


};