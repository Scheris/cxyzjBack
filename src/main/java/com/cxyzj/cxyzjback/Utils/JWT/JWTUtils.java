package com.cxyzj.cxyzjback.Utils.JWT;


import com.cxyzj.cxyzjback.Bean.user.User;
import com.cxyzj.cxyzjback.Catch.RoleList;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

/**
 * @Package com.cxyzj.cxyzjback.Utils
 * @Author Yaser
 * @Date 2018/08/09 11:13
 * @Description: Token生成工具类，使用jwt标准
 */
@Slf4j
public class JWTUtils {
    private static final long TOKEN_EXPIRATE_AT = 1000 * 3600;//过期时间
    private static final String KEY = "YASER";//加密密钥
    private static final String TOKEN_PREFIX = "Bearer ";//头部前缀
    private static final String HEADER_STRING = "Authorization";//请求头部
    @Getter
    private String userId;
    @Getter
    private String role;
    @Getter
    private String nickname;

    private RoleList roles = RoleList.getRoles();

    /**
     * @param user 用于生成token的用户
     * @param hour 几小时后过期
     * @return 生成的token
     */
    public String generateToken(User user, int hour) {
        return Jwts.builder().
                setClaims(generateClaims(user)).
                setExpiration(generateExpiration(hour)).
                setIssuedAt(new Date()).setIssuer("cxyzj").
                setSubject("Token").
                setAudience("user").
                setId(UUID.randomUUID().toString()).
                signWith(SignatureAlgorithm.HS512, KEY).
                compact();
    }

    /**
     * @param user 用于生成Token的用户
     * @return 生成的Token
     */
    public String generateToken(User user) {
        return generateToken(user, 2);//默认两小时过期
    }

    /**
     * @param hour 几小时后过期
     * @return 过期的日期时间
     */
    private Date generateExpiration(int hour) {
        return new Date(System.currentTimeMillis() + hour * TOKEN_EXPIRATE_AT);
    }

    /**
     * @param user 用于生成Token的用户
     * @return 存放在Token中的私有信息
     */
    private Claims generateClaims(User user) {
        DefaultClaims defaultClaims = new DefaultClaims();
        defaultClaims.put("userId", user.getUserId());
        defaultClaims.put("role", roles.getRole(user.getRoleId()));
        defaultClaims.put("nickname", user.getNickname());
        return defaultClaims;
    }

    /**
     * 解析token信息，并将解析结果保存
     *
     * @param request 要解析的request请求
     */
    public void ParseToken(HttpServletRequest request) throws ExpiredJwtException, SignatureException {
        log.info("-------------------------------------------Start to parse Token");
        String token = request.getHeader(HEADER_STRING);//读取请求头部
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            token = token.replace(TOKEN_PREFIX, "");//去掉前缀
            Claims claims = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();//读取token信息
            this.nickname = (String) claims.get("nickname");
            this.role = (String) claims.get("role");
            this.userId = (String) claims.get("userId");
            log.info("id:" + userId);
        }

    }

    /**
     * @param user 用于生成Token的用户
     * @param day  几天后过期，此RefreshToken用于刷新Token
     * @return 更新后的Token
     */
    public String generateRefreshToken(User user, int day) {
        return generateToken(user, day * 24);
    }

    /**
     * @param user 用于生成Token的用户
     * @return 更新后的Token
     */
    public String generateRefreshToken(User user) {
        return generateRefreshToken(user, 15);//默认RefreshToken过期时间为15天
    }

}
