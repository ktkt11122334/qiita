package com.stock.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Entity
@Getter
@Setter
@ToString
public class ProductMaster implements Serializable {

  private static final long serialVersionUID = 1L;



  @Id
  @GeneratedValue( strategy = GenerationType.IDENTITY )
  @Column( name = "product_id" )
  private Long productId;

  @Column( name = "product_code" )
  private String productCode;

  @Column( name = "jancode" )
  private String jancode;

  @Column( name = "disable_flg" )
  private Boolean disableFlg;

  @Column( name = "create_ts" )
  private LocalDateTime createTs;

  @Column( name = "last_modified_ts" )
  private LocalDateTime lastModifiedTs;

}
