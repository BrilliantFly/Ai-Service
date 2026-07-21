package com.know.knowboot.classinfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.groups.Default;
import java.lang.reflect.Field;
import java.util.Date;

public class ClassInfo {

    public static void main(String[] args) throws IllegalAccessException {

        // 获取Calss对象的方式主要有三种
        Class<?> c1 = null;
        Class<?> c2 = null;
        Class<?> c3 = null;
        try{
            //第一种：最常用的形式
            c1 = Class.forName("com.know.knowboot.system.entity.SysUser");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        //第二种：通过 Object 类中方法实例
        c2 = new SysUser().getClass();
        //第三种：通过类.class实例化
        c3 = SysUser.class;
        //得到类的名称
        System.out.println("取全限定的类名(包括包名):" + c1.getName() + "---获取类名(不包括包名):" + c1.getSimpleName()
                + "---判断Class对象表示的是否是一个接口:" + c1.isInterface()
                + "---返回Class对象数组，对应Class对象所引用的类所实现的所有接口:" + c1.getInterfaces()
                + "---返回Class对象，表示Class对象所引用的类所继承的直接基类:" + c1.getSuperclass()
                + "---获得某个类的所有的公共（public）的字段，包括继承自父类的所有公共字段:" + c1.getFields()
                + "---获得某个类的自己声明的字段，即包括public、private和proteced，默认但是不包括父类声明的任何字段:" + c1.getDeclaredFields());
        System.out.println("取全限定的类名(包括包名):" + c2.getName() + "---获取类名(不包括包名):" + c2.getSimpleName());
        System.out.println("取全限定的类名(包括包名):" + c3.getName() + "---获取类名(不包括包名):" + c3.getSimpleName());

        SysUser sysUser = new SysUser();
        sysUser.setRealname("测试");
        Class clazz = sysUser.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equals("realname")){
                System.out.println(field.isAccessible());//这里的结果是false
                // 设置可以访问private变量的变量值
                field.setAccessible(true);
                System.out.println(field.getName()+":"+field.get(sysUser));
            }
        }

    }

}

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
class SysUser {

    /**
     * 登录账号
     */
    @NotBlank(message = "登录账号不能为空")
    private String username;

    /**
     * 真实姓名
     */
    @NotBlank(message = "真实姓名不能为空",groups = Default.class)
    private String realname;

    /**
     * 密码
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "密码不能为空",groups = {Default.class})
    private String password;

    /**
     * md5密码盐
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String salt;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 生日
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    /**
     * 性别（1：男 2：女）
     */
    private Integer sex;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 部门code(当前选择登录部门)
     */
    private String orgCode;

    /**部门名称*/
    private transient String orgCodeTxt;

    /**
     * 状态(1：正常  2：冻结 ）
     */
    private Integer status;

    /**
     * 工号，唯一键
     */
    private String workNo;

    /**
     * 职务，关联职务表
     */
    private String post;

    /**
     * 座机号
     */
    private String telephone;


    /**
     * 同步工作流引擎1同步0不同步
     */
    private Integer activitiSync;

    /**
     * 身份（0 普通成员 1 上级）
     */
    private Integer userIdentity;

    /**
     * 负责部门
     */
    private String departIds;

    /**
     * 多租户id配置，编辑用户的时候设置
     */
    private String relTenantIds;

}
