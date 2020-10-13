package com.terrasi.terrasiapi.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class JwtFilter extends BasicAuthenticationFilter{

    private final String secretKey;
    private final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    public JwtFilter(AuthenticationManager authenticationManager,String secretKey) {
        super(authenticationManager);
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, AccessDeniedException {

        try {
            String header = request.getHeader("Authorization");
            if(header != null && !request.getRequestURI().equals("/login")) {
                UsernamePasswordAuthenticationToken authResult = getAuthenticationByToken(header);
                SecurityContextHolder.getContext().setAuthentication(authResult);
            }
        }catch (SignatureException e){
            response.setContentType(request.getRemoteAddr() + " " + e.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Enter valid token");
        }catch (ExpiredJwtException e){
            response.setContentType(request.getRemoteAddr() + " " + e.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token expired");
        }catch (Exception e){
            response.setContentType(request.getRemoteAddr() + " " + e.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }

        try{
            chain.doFilter(request, response);
        }catch(Exception e){
            logger.error(response.getContentType());
        }
    }

    @SuppressWarnings("unchecked")
    private UsernamePasswordAuthenticationToken getAuthenticationByToken(String header){
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(header.replace("Bearer ",""));

            if(claimsJws.getBody().containsKey("sub") && claimsJws.getBody().containsKey("roles")){
                String username = claimsJws.getBody().get("sub").toString();
                List<String> roles = (List<String>)claimsJws.getBody().get("roles");

                Set<SimpleGrantedAuthority> authorities = new HashSet<>();
                roles.forEach(s -> authorities.add(new SimpleGrantedAuthority(s)));
                Set<SimpleGrantedAuthority> simpleGrantedAuthorities = Collections.unmodifiableSet(authorities);

                return new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuthorities);
            }else{
                throw new SignatureException("");
            }
    }
}
