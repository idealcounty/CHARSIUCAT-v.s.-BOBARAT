package com.example.SBEAM.vo;

import com.example.SBEAM.enums.RoleEnum;
import com.example.SBEAM.po.Inventory;
import com.example.SBEAM.po.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserVO {

    private Integer id;

    private String name;

    private String phone;

    private String password;

    private Integer storeId;

    private String address;

    private RoleEnum role;

    private Date createTime;

    private Double balance;

    private String avatar;

    private List<Inventory> inventories;
    public User toPO(){
        User user=new User();
        user.setId(this.id);
        user.setAddress(this.address);
        user.setName(this.name);
        user.setPhone(this.phone);
        user.setRole(this.role);
        user.setStoreId(this.storeId);
        user.setPassword(this.password);
        user.setCreateTime(this.createTime);
        user.setBalance((double) 0);
        user.setAvatar(this.avatar);
        user.setInventories(this.inventories);
        return user;
    }
}
