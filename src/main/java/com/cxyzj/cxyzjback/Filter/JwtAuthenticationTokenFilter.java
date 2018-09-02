package com.cxyzj.cxyzjback.Filter;

import com.cxyzj.cxyzjback.Utils.JWT.JWTUtils;
import com.cxyzj.cxyzjback.Utils.Response;
import com.cxyzj.cxyzjback.Utils.Status;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Resource
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            JWTUtils jwtUtils = new JWTUtils();
            jwtUtils.ParseToken(request);//解析请求中的token信息
            String userId = jwtUtils.getUserId();//获取解析后的用户id
            log.info("userId:" + userId);
            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {//如果id不为空，且SecurityContextHolder里没有相关信息
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userId);//通过ID加载用户信息
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);//将加载的用户信息写入到SecurityContext中
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.info("------------------------ExpiredJwtException" + e.getMessage());
            sendFailureMessage(Status.TOKEN_EXPIRED, "token已过期", response);
        } catch (SignatureException e) {
            log.info("------------------------SignatureException" + e.getMessage());
            sendFailureMessage(Status.ILLEGAL_TOKEN, "token是非法的", response);
        } catch (AccessDeniedException e) {
            log.info("------------------------AccessDeniedException" + e.getMessage());
            sendFailureMessage(Status.ACCESS_DENIED, "访问被拒", response);
        }
    }

    private void sendFailureMessage(int code, String msg, HttpServletResponse response) throws IOException {
        Response myResponse = new Response();
        response.setContentType("application/json");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter printWriter = response.getWriter();
        printWriter.println(myResponse.sendFailure(code, msg));
    }
}
