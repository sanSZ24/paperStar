package com.paperStar.pojo;

import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Table(name = "user")
public class User {
    @Id
    private int id;//用户id

    @NotNull(message = "用户名不能为空")
    private String userName;//用户名

    @NotBlank(message = "密码不能为空")
    @Length(min=6,max=20,message = "密码长度在6~20个字符")
    private String password;//用户密码

    @Email(message = "邮箱格式错误")
    private String email;//邮箱
    private String role;//用户角色，用于判断是否拥有admin和user、guest能访问的路径

    private String salt;

    private String createTime;//创建时间
    private String lastLoginTime;//上次登录时间
    private int status;//账号当前状态,0为未登录，1为登录

}
