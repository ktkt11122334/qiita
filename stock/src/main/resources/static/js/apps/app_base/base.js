Class$Base = Class.create({



  initialize : function($super) {
    this.setOnload();
  },



  setOnload: function() {

    window.onload = function() {

      this.onload();
    }.bind(this);
  },


  /**
   *
   * htmlテキストロード後に実行される関数
   * 継承される子クラスでオーバーライドして使用
   */
  onload : function() {

  },

});