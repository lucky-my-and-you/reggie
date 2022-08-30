package com.zhang.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.reggie.entity.AddressBook;
import com.zhang.reggie.service.AddressBookService;
import com.zhang.reggie.mapper.AddressBookMapper;
import org.springframework.stereotype.Service;

/**
* @author 13269
* @description 针对表【address_book(地址管理)】的数据库操作Service实现
* @createDate 2022-08-27 14:33:00
*/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
    implements AddressBookService{

}




