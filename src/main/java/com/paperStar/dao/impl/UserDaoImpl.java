package com.paperStar.dao.impl;

import com.paperStar.dao.UserDao;
import com.paperStar.mapper.UserMapper;
import com.paperStar.pojo.User;
import com.paperStar.utils.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {
    @Override
    public User findUserByName(String name){
        SqlSession sqlSession = SqlSessionFactoryUtil.openSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = userMapper.findUserByName(name);
        sqlSession.close();
        return user;
    }

    @Override
    public User findUserById(int id) {
        SqlSession sqlSession = SqlSessionFactoryUtil.openSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = userMapper.findUserById(id);
        sqlSession.close();
        return user;
    }

    @Override
    public boolean insertUser(User user) {
        SqlSession sqlSession = SqlSessionFactoryUtil.openSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        userMapper.insertUser(user);
        sqlSession.commit();//提交事务
        sqlSession.close();
        return true;
    }

    @Override
    public boolean updateUser(User user) {
        SqlSession sqlSession = SqlSessionFactoryUtil.openSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        userMapper.updateUser(user);
        sqlSession.commit();//提交事务
        sqlSession.close();
        return true;
    }

    @Override
    public boolean deleteUserById(int id) {
        SqlSession sqlSession = SqlSessionFactoryUtil.openSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        userMapper.deleteUserById(id);
        sqlSession.commit();//提交事务
        sqlSession.close();
        return true;
    }

    @Override
    public String getUserRole(String name) {
        SqlSession sqlSession = SqlSessionFactoryUtil.openSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        String role = userMapper.getUserRole(name);
        sqlSession.close();
        return role;
    }
}
