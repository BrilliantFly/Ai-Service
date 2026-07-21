package com.know.knowboot.jwt.service;

import com.know.knowboot.constant.SymbolConstants;
import com.know.knowboot.jwt.enums.JwtEnum;
import com.know.knowboot.jwt.enums.PlatformEnum;
import com.know.knowboot.jwt.enums.SystemEnum;
import com.know.knowboot.jwt.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class JJwtService {

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
     * 根据用户信息生成 token
     *
     * @param systemEnum   系统信息
     * @param platformEnum 平台信息
     * @param claims       用户及权限信息
     * @return
     */
    @SneakyThrows
    public String generateToken(SystemEnum systemEnum, PlatformEnum platformEnum, Map<String, Object> claims) {

        String tokenResult = "";

        // 获取token
        String token = generateToken(claims);
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
     * @param claims 创建payload的私有声明
     * @return
     */
    private String generateToken(Map<String, Object> claims) {

        Map<String, Object> header = new HashMap<>();

        //JwtBuilder，设置jwt的body,为payload添加各种标准声明和私有声明了
        JwtBuilder builder = Jwts.builder()
                .setHeader(header)
                //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setId(UUID.randomUUID().toString())
                //iat: jwt的签发时间
                .setIssuedAt(new Date())
                //过期时间
                .setExpiration(generateExpirationDate())
                .signWith(generateKeyByDecoders());

        String token = builder.compact();

        return token;
    }

    /**
     * 生成 token 过期时间
     *
     * @return
     */
    public Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + jwtProperties.getExpiration() * 1000);
    }

    /**
     * 生成自定义 Key
     *
     * @return
     */
    private SecretKey generateKeyByDecoders() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecret()));
    }

    /**
     * 证token合法性，不合法会抛出异常信息
     *
     * @param jwtToken
     * @return
     */
    public Jws<Claims> verify(String jwtToken) {
        // 传入Key对象
        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(jwtProperties.getSecret()).build().parseClaimsJws(jwtToken);
        return claimsJws;
    }

    /**
     * 解析Token
     *
     * @param jwtToken
     * @return
     */
    public Claims getClaimsFromToken(String jwtToken) {


        Claims claims = Jwts.parserBuilder()    //获取解析器
                .setSigningKey(generateKeyByDecoders())   //设置签名密钥
                .build()
                .parseClaimsJws(jwtToken)  //设置需要解析的token
                .getBody();

        return claims;

    }

    /**
     * 根据key解析claims
     *
     * @param claims
     * @param key
     * @return
     */
    public Object paseClaimsByKey(Claims claims, Object key) {
        return claims.get(key);
    }

    /**
     * 通过Token、key解析Token
     *
     * @param jwtToken
     * @param key
     * @return
     */
    public Object paseClaimsByToken(String jwtToken, Object key) {

        Claims claims = getClaimsFromToken(jwtToken);
        return claims.get(key);

    }

    /**
     * 从 token 中获取过期时间
     *
     * @param jwtToken
     * @return
     */
    public Date getExpiredDateFromToken(String jwtToken) {
        Claims claims = getClaimsFromToken(jwtToken);
        return claims.getExpiration();
    }


    /**
     * 判断 token 是否过期
     *
     * @param jwtToken
     * @return
     */
    public boolean isTokenExpired(String jwtToken) {
        Date expiredDate = getExpiredDateFromToken(jwtToken);
        return expiredDate.after(new Date());
    }

    /**
     * 判断token是否存在与有效
     *
     * @param jwtToken token字符串
     * @return 如果token有效返回true，否则返回false
     */
    public boolean checkToken(String jwtToken) {

        if (StringUtils.isEmpty(jwtToken)) {
            return false;
        }
        try {
            Jwts.parserBuilder().setSigningKey(jwtProperties.getSecret()).build().parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

}
