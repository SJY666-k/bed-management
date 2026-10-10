package com.neusoft.yiyang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.yiyang.entity.Bed;
import com.neusoft.yiyang.vo.BedStatisticsVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 床位 Mapper。
 *
 * <p>继承 {@link BaseMapper} 后即拥有单表增删改查能力；
 * 床位统计需要一次聚合查询，所以写在 {@code resources/mapper/BedMapper.xml} 里。</p>
 */
@Mapper
public interface BedMapper extends BaseMapper<Bed> {

    /**
     * 统计总床位 / 空闲 / 有人 / 外出 数量。
     *
     * @return 一次性返回四个统计值
     */
    BedStatisticsVO selectStatistics();
}
