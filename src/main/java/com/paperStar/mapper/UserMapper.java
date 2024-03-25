package com.paperStar.mapper;

import com.paperStar.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    User findUserByName(String name);
    User findUserById(int id);
    void insertUser(User user);
    void updateUser(User user);
    void deleteUserById(int id);
    String getUserRole(String name);
}
