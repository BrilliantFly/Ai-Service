package com.know.knowboot.designpatterns.behavioral.iteratorinfo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

/**
 * 用户接口实现类
 */
@Data
public class User implements IUser {

    private ArrayList<IUser> userList = new ArrayList<>();


    private String userName;
    private Integer userAge;
    private String userAddr;
    private String userDept;

    public User(String userName, Integer userAge, String userAddr, String userDept) {
        this.userName = userName;
        this.userAge = userAge;
        this.userAddr = userAddr;
        this.userDept = userDept;
    }

    public User() {

    }

    @Override
    public String getUserInfo() {
        return "User[" +
                "userName='" + userName + '\'' +
                ", userAge=" + userAge +
                ", userAddr='" + userAddr + '\'' +
                ", userDept='" + userDept + '\'' +
                ']';
    }

    @Override
    public void add(String userName, Integer userAge, String userAddr, String userDept) {
        this.userList.add(new User(userName,userAge,userAddr,userDept));
    }

    @Override
    public UserIterator getIterator() {
        return new UserIterator(this.userList);
    }
}
