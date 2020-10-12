package com.terrasi.terrasiapi.security;

import io.jsonwebtoken.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


public class JwtFilter extends BasicAuthenticationFilter{

    private final String secretKey = "6eFj-#O>Ir[%Z7@@~yK*mq7*|={IHp";
    public JwtFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        if(header != null && !request.getRequestURI().equals("/login")) {
            try {
                UsernamePasswordAuthenticationToken authResult = getAuthenticationByToken(header);
                SecurityContextHolder.getContext().setAuthentication(authResult);
            }catch (ExpiredJwtException ex){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Toke expired");
            }catch (SignatureException ex){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Enter a valid token");
            }
        }
        chain.doFilter(request, response);
    }

    @SuppressWarnings("unchecked")
    private UsernamePasswordAuthenticationToken getAuthenticationByToken(String header) throws SignatureException, ExpiredJwtException{
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
