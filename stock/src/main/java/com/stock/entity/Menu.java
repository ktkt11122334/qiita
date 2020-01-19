package com.stock.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;



@Entity
@Getter
@Setter
public class Menu implements Serializable {

  @Id
  @GeneratedValue( strategy = GenerationType.IDENTITY )
  @Column( name = "menu_id" )
  private Long menuId;

  @Column( name = "display_name" )
  private String displayName;

  @Column( name = "path" )
  private String path;

  @Column( name = "display_order")
  private Integer displayOrder;

  @Column( name = "disable_flg" )
  private Byte disableFlg;

}
