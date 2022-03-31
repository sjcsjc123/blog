package com.example.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.myblog.domain.AdminUser;
import com.example.myblog.exception.MyProjectException;
import com.example.myblog.exception.constant.MyProjectExceptionEnum;
import com.example.myblog.mapper.AdminUserMapper;
import com.example.myblog.service.AdminUserService;
import com.example.myblog.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Override
    public Long findIdByUsername(String username) {
        QueryWrapper<AdminUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return adminUserMapper.selectOne(queryWrapper).getUserId();
    }

    @Override
    public void login(String username, String password) {
        QueryWrapper<AdminUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        AdminUser adminUser = adminUserMapper.selectOne(queryWrapper);
        if (adminUser == null){
            throw new MyProjectException(MyProjectExceptionEnum.USERNAME_IS_EMPTY);
        }
        if (!adminUser.getPassword().equals(password)){
            throw new MyProjectException(MyProjectExceptionEnum.PASSWORD_ERROR);
        }
    }

    @Override
    public void register(String username, String password, String phone,
                         String confirm) {
        QueryWrapper<AdminUser> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("phone",phone);
        Long countPhone = adminUserMapper.selectCount(wrapper1);
        QueryWrapper<AdminUser> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("username",username);
        Long countUsername = adminUserMapper.selectCount(wrapper2);
        if (username.isEmpty()){
            throw new MyProjectException(MyProjectExceptionEnum.INVALID_USER_NULL);
        }
        if (countUsername >= 1){
            throw new MyProjectException(MyProjectExceptionEnum.USER_NO_ONLY);
        }
        if (phone.length()!=11) {
            throw new MyProjectException(MyProjectExceptionEnum.INVALID_PHONE_LENGTH);
        }
        String phoneConfig = "^(((13[0-9])|(14[579])|(15([0-3]|[5-9]))|(16[6])|(17[0135678])|(18[0-9])|(19[89]))\\\\d{8})$";
        if (phone.matches(phoneConfig)){
            throw new MyProjectException(MyProjectExceptionEnum.INVALID_PHONE_DATA_TYPE);
        }
        if (countPhone >= 1){
            throw new MyProjectException(MyProjectExceptionEnum.PHONE_NO_ONLY);
        }
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$";
        if (!password.matches(regex)){
            throw new MyProjectException(MyProjectExceptionEnum.INVALID_PASSWORD);
        }
        if (!password.equals(confirm)){
            throw new MyProjectException(MyProjectExceptionEnum.PASSWORD_NOT_CONFIRM);
        }
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername(username);
        adminUser.setPhone(phone);
        adminUser.setNickName(null);
        adminUser.setPassword(password);
        adminUser.setLocked(1);
        adminUserMapper.insert(adminUser);
    }

}
