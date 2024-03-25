package com.paperStar.service.impl;

import com.paperStar.dao.UserDao;
import com.paperStar.dao.impl.UserDaoImpl;
import com.paperStar.pojo.Result;
import com.paperStar.pojo.User;
import com.paperStar.service.UserService;
import com.paperStar.utils.JwtUtil;
import com.paperStar.utils.Md5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    Md5Util md5Util;
    RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    //密码加密
    @Override
    public Result Login(User user) {
        log.info("正在执行Service中login方法");
        User tempUser = userDao.findUserByName(user.getUserName());
        if(tempUser == null){
            return Result.error("查询对象不存在");
        }
        //跟数据库中的加密的密码进行比较
//        else if(passwordEncoder.matches(user.getPassword(), tempUser.getPassword()))
        else if(md5Util.decode(user.getPassword(),tempUser.getSalt(),tempUser.getPassword())){
            //获取当前时间并格式化
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            User temp=new User();
            //修改登录时间
            temp.setLastLoginTime(sdf.format(date));
            //修改用户状态
            temp.setStatus(1);
            temp.setId(tempUser.getId());
            //更新数据库中的数据
            userDao.updateUser(temp);

            JwtUtil token = new JwtUtil();

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("id",tempUser.getId());
            hashMap.put("name",tempUser.getUserName());
            hashMap.put("lastLoginTime",tempUser.getLastLoginTime());
            hashMap.put("mail",tempUser.getEmail());
            hashMap.put("role",tempUser.getRole());

            String jwt =token.createJwt(hashMap);
            //返回jwt到前端存储
            return Result.success(jwt);
        }

        return Result.error("密码错误");
    }

    @Override
    public Result Logout(int id) {
        //修改登陆状态
        User temp =new User();
        temp.setId(id);
        if(userDao.updateUser(temp)){
            return Result.success("登出成功");
        }
        return Result.error("登出失败");
    }

    @Override
    public Result Register(User user) {
        User temp = userDao.findUserByName(user.getUserName());
        if(temp == null){
            //获取创建时间
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            user.setCreateTime(sdf.format(date));
            //随机生成md5加密的盐值并存储
            String salt = randomNumberGenerator.nextBytes().toBase64();
            user.setSalt(salt);
            //加密存储密码
            user.setPassword(md5Util.encode(user.getPassword(),salt));
            userDao.insertUser(user);
            return Result.success("创建成功");
        }
        else{
            return Result.error("用户名重复");
        }
    }

    @Override
    public Result updateProfile(int id, User user) {

        return null;
    }
}
