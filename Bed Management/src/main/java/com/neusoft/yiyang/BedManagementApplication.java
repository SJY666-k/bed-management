package com.neusoft.yiyang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 东软颐养中心管理系统 —— 床位管理模块启动类。
 *
 * <p>本模块只负责「床位示意图」与「床位管理」两件事，其余模块（用户管理、客户管理、
 * 入住登记等）由其它同学负责，最终通过统一的 {@code /api} 前缀整合。</p>
 *
 * @author 床位管理模块
 */
@SpringBootApplication
@MapperScan("com.neusoft.yiyang.mapper")
public class BedManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(BedManagementApplication.class, args);
    }
}
