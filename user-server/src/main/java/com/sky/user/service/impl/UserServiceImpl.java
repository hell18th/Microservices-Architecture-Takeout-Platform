package com.sky.user.service.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.exception.LoginFailedException;
import com.sky.properties.WeChatProperties;
import com.sky.user.domain.dto.UserLoginDTO;
import com.sky.user.domain.entity.User;
import com.sky.user.domain.vo.UserLoginVO;
import com.sky.user.mapper.UserMapper;
import com.sky.user.service.UserService;
import com.sky.utils.HttpClientUtils;
import com.sky.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    private static final String login = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        String openid = getOpenid(userLoginDTO.getCode());
        if (openid != null) {
            User user = userMapper.selectByOpenid(openid);
            if (user == null) {
                user = User.builder().openid(openid).createTime(LocalDateTime.now()).build();
                userMapper.insertUser(user);
            }
            Map<String, Object> claims = new HashMap<>();
            claims.put(JwtClaimsConstant.USER_ID, user.getId());
            String token = JwtUtils.generateJwt(claims);
            return new UserLoginVO(user.getId(), openid, token);
        } else {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
    }

    @Override
    public Integer userStatistics(LocalDateTime begin, LocalDateTime end) {
        return userMapper.selectByDate(begin, end);
    }

    private String getOpenid(String code) {
        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String json = HttpClientUtils.doGET(login, map);
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        if (jsonObject.get("openid") != null) {
            return jsonObject.get("openid").getAsString();
        } else {
            throw new LoginFailedException(jsonObject.get("errmsg").getAsString());
        }
    }
}