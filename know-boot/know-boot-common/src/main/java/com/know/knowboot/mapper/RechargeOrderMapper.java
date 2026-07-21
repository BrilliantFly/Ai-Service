package com.know.knowboot.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.RechargeOrder;
import com.know.knowboot.util.TimeUtils;
import com.know.knowboot.util.ToolUtils;

/**
 * 充值订单Mapper
 */
public interface RechargeOrderMapper extends IBaseMapper<RechargeOrder> {

    /**
     * 生成唯一单号
     *
     * @author fzr
     * @param field 字段名
     * @return String
     */
    default String randMakeOrderSn(String field) {
        String date = TimeUtils.timestampToDate(System.currentTimeMillis()/1000, "yyyyMMddHHmmss");
        String sn;
        while (true) {
            sn = date + ToolUtils.randomInt(12);
            RechargeOrder snModel = this.selectOne(
                    new QueryWrapper<RechargeOrder>()
                            .select("id")
                            .eq(field, sn)
                            .last("limit 1"));
            if (snModel == null) {
                break;
            }
        }
        return sn;
    }

}
