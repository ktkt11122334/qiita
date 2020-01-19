package com.stock.service.menu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stock.entity.Menu;
import com.stock.repository.MenuRepository;



@Service
public class MenuServiceImpl implements MenuService {

  @Autowired
  private MenuRepository menuRepository;



  @Override
  public List<Menu> getMenuList() {
    return menuRepository.findWhereDisableIsFalseOrderByDisplayOrder();
  }

}
