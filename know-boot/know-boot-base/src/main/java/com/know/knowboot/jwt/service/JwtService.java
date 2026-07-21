package com.know.knowboot.jwt.service;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.know.knowboot.constant.SymbolConstants;
import com.know.knowboot.jwt.enums.JwtEnum;
import com.know.knowboot.jwt.enums.PlatformEnum;
import com.know.knowboot.jwt.enums.SystemEnum;
import com.know.knowboot.jwt.properties.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;

@Slf4j
@Component
public class JwtService {

//    /**
//     * 密钥
//     *
//     * @value 声明静态变量static获取不到值
//     */
//    @Value("${know.jwt.secret}")
//    private String secret;
//
//    /**
//     * token有效期 (S)
//     */
//    @Value("${know.jwt.expiration}")
//    private Long expiration;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 生成Token
     *
     * @param systemEnum   系统信息
     * @param platformEnum 平台信息
     * @param object       用户及权限信息
     * @return
     */
    public String generateToke(SystemEnum systemEnum, PlatformEnum platformEnum, JSONObject object) {

        String tokenResult = "";

        // 获取token
        String token = createToken(object);
        // 加入平台信息
        tokenResult = String.join(SymbolConstants.DOT, platformEnum.getCode(), token);
        // 加入系统信息
        tokenResult = MessageFormat.format("{0} {1}", systemEnum.getCode(), tokenResult);

        return tokenResult;

    }

    /**
     * 获取真实的Token
     *
     * @param jwtToken
     * @return
     */
    public String getRealToken(String jwtToken) {

        String realToken = "";

        if (StringUtils.isBlank(jwtToken)){
            log.error("----------- 获取真实的Token jwtToken为null ------------");
            throw new RuntimeException("jwtToken为null");
        }

        String[] arrayInfo = jwtToken.split(SymbolConstants.DOT_SPLIT);
        if (arrayInfo.length != 4){
            log.error("----------- 获取真实的Token jwtToken格式错误 ------------");
            throw new RuntimeException("jwtToken格式错误");
        }

        realToken = String.join(SymbolConstants.DOT, arrayInfo[1], arrayInfo[2], arrayInfo[3]);
        return realToken;

    }

    /**
     * 获取platform
     *
     * @param jwtToken
     * @return
     */
    public String getPlatForm(String jwtToken) {

        String paltform = "";

        if (StringUtils.isBlank(jwtToken)){
            log.error("----------- 获取真实的Token jwtToken为null ------------");
            throw new RuntimeException("jwtToken为null");
        }

        String[] arrayInfo = jwtToken.split(SymbolConstants.DOT_SPLIT);
        if (arrayInfo.length != 4){
            log.error("----------- 获取真实的Token jwtToken格式错误 ------------");
            throw new RuntimeException("jwtToken格式错误");
        }

        String[] paltforms = arrayInfo[0].split(" ");
        if (paltforms.length != 2){
            log.error("----------- 获取真实的Token jwtToken paltforms格式错误 ------------");
            throw new RuntimeException("jwtToken paltforms格式错误");
        }
        paltform = paltforms[1];
        return paltform;

    }

    /**
     * 生成Token
     *
     * @param object 用户及权限信息
     * @return
     */
    public String createToken(JSONObject object) {

        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, 30);
        Date expiresDate = nowTime.getTime();

        return JWT.create()
                .withAudience(null)   //签发对象
                .withIssuedAt(new Date())    //发行时间
                .withExpiresAt(expiresDate)  //有效时间
                .withClaim(JwtEnum.TOKEN_KEY_USER.getCode(), object.toJSONString())    //载荷，随便写几个都可以
                .sign(Algorithm.HMAC256(jwtProperties.getSecret()));   //加密

    }

    /**
     * 验证token合法性，不合法会抛出异常信息
     *
     * @param token ： 前端传来的token
     * @return
     */
    public DecodedJWT verify(String token) {
        //如果有任何验证异常，此处都会抛出异常，因此我们可以捕获这些异常来反馈信息回前端
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(jwtProperties.getSecret())).build().verify(token);
        return decodedJWT;
    }

    /**
     * 解析token
     *
     * @param token token字符串
     * @return 解析后的token
     */
    public DecodedJWT paseToken(String token) {
        return JWT.decode(token);
    }

    /**
     * 通过载荷名字获取载荷的值
     *
     * @param token
     * @param name
     * @return
     */
    public Claim getClaimByName(String token, String name) {
        return JWT.decode(token).getClaim(name);
    }

}
