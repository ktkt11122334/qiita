package com.stock.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailId implements Serializable {

  @Column( name = "order_id" )
  private Long orderId;

  @Column( name = "order_detail_id" )
  private Long orderDetailId;



  @Override
  public int hashCode() {
    final int prime = 3;
    int result = 1;
    result = prime * result + ( (orderDetailId == null) ? 0 : orderDetailId.hashCode() );
    result = prime * result + ( (orderId == null) ? 0 : orderId.hashCode() );

    return result;
  }



  @Override
  public boolean equals(Object obj) {
    if ( this == obj ) return true;
    if ( obj == null ) return false;
    if (getClass() != obj.getClass() ) return false;


    OrderDetailId other = (OrderDetailId) obj;

    if ( orderDetailId == null ) {
      if ( other.orderDetailId != null ) return false;
    }
    else if ( !orderDetailId.equals(other.orderDetailId) ) {
      return false;
    }

    if ( orderId == null ) {
      if ( other.orderId != null ) return false;
    }
    else if ( !orderId.equals(other.orderId) ) {
      return false;
    }

    return true;
  }
}
