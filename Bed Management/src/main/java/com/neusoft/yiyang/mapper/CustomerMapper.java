package com.neusoft.yiyang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.yiyang.entity.Customer;
import org.apache.ibatis.annotations.Mapper;

/**
 * 客户 Mapper。床位调换时需要同步客户的房间号、床号、楼号。
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
}
