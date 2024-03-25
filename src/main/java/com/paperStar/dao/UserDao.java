package com.paperStar.dao;

import com.paperStar.pojo.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {
    User findUserByName(String name);
    User findUserById(int id);
    String getUserRole(String name);
    boolean insertUser(User user);
    boolean updateUser(User user);
    boolean deleteUserById(int id);

}
