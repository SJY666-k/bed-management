package com.neusoft.yiyang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.yiyang.entity.BedDetails;
import com.neusoft.yiyang.vo.BedDetailsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 床位使用详情 Mapper。
 *
 * <p>「床位管理」列表需要 beddetails 与 customer 两表联查 + 动态条件，
 * 因此写在 {@code resources/mapper/BedDetailsMapper.xml} 里。</p>
 */
@Mapper
public interface BedDetailsMapper extends BaseMapper<BedDetails> {

    /**
     * 按「使用状态 + 客户姓名（模糊）+ 入住日期」多条件组合查询床位使用详情。
     *
     * @param isDeleted    0 正在使用、1 使用历史
     * @param customerName 客户姓名，可为 null（不加条件）
     * @param startDate    入住日期（yyyy-MM-dd），可为 null（不加条件）
     * @return 床位使用详情列表
     */
    List<BedDetailsVO> selectDetailsList(@Param("isDeleted") Integer isDeleted,
                                         @Param("customerName") String customerName,
                                         @Param("startDate") String startDate);
}
