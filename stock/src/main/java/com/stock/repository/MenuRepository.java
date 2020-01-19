package com.stock.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.stock.entity.Menu;



public interface MenuRepository extends JpaRepository<Menu, Long> {

//ORDER BY m.displayOrder
//WHERE m.disableFlg == 0
  @Query("SELECT m FROM Menu m WHERE disableFlg = 0 ORDER BY m.displayOrder")
  public List<Menu> findWhereDisableIsFalseOrderByDisplayOrder();

}
