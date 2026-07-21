//package com.know.knowboot.mybatis.config;
//
//import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
//import org.apache.ibatis.reflection.MetaObject;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//
///**
// * 自动天聪创建时间、更新时间
// */
//@Component
//public class MybatisAutoFill implements MetaObjectHandler {
//
//    @Override
//    public void insertFill(MetaObject metaObject) {
//        metaObject.setValue("createTime", new Date());
//        metaObject.setValue("delFlag", 0);
//    }
//
//    @Override
//    public void updateFill(MetaObject metaObject) {
//        this.setFieldValByName("updateTime",new Date(), metaObject);
//    }
//
//}
