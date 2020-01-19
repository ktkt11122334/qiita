package com.stock.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class ProductMasterHistoryId implements Serializable {

  @Column( name = "product_id" )
  private Long productId;

  @Column( name = "last_modified_ts" )
  private LocalDateTime lastModifiedTs;



  @Override
  public int hashCode() {
    final int prime = 3;
    int result = 1;
    result = prime * result + ( (lastModifiedTs == null) ? 0 : lastModifiedTs.hashCode() );
    result = prime * result + ( (productId == null) ? 0 : productId.hashCode() );

    return result;
  }



  @Override
  public boolean equals(Object obj) {
    if ( this == obj ) return true;
    if ( obj == null ) return false;
    if (getClass() != obj.getClass() ) return false;


    ProductMasterHistoryId other = (ProductMasterHistoryId) obj;

    if ( lastModifiedTs == null ) {
      if ( other.lastModifiedTs != null ) return false;
    }
    else if ( !lastModifiedTs.equals(other.lastModifiedTs) ) {
      return false;
    }

    if ( productId == null ) {
      if ( other.productId != null ) return false;
    }
    else if ( !productId.equals(other.productId) ) {
      return false;
    }

    return true;
  }

}
