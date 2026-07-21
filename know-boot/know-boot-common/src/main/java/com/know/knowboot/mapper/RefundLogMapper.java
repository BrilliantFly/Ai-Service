package com.know.knowboot.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.RefundLog;
import com.know.knowboot.util.TimeUtils;
import com.know.knowboot.util.ToolUtils;
import org.apache.ibatis.annotations.Mapper;

/**
 * 退款日志Mapper
 */
@Mapper
public interface RefundLogMapper extends IBaseMapper<RefundLog> {

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
            RefundLog snModel = this.selectOne(
                    new QueryWrapper<RefundLog>()
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
