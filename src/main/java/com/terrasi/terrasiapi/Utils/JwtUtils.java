package com.terrasi.terrasiapi.Utils;

import com.terrasi.terrasiapi.model.JwtModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtUtils {

    private static String secretKey;

    @Value("${secret.key}")
    public void setSecretKey(String key) {
        JwtUtils.secretKey = key;
    }

    @SuppressWarnings("unchecked")
    public static JwtModel parseAccessToken(String token){
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token.replace("Bearer ", ""));

        JwtModel jwtModel;

        if (claimsJws.getBody().containsKey("sub") && claimsJws.getBody().containsKey("roles")
                && claimsJws.getBody().containsKey("iat") && claimsJws.getBody().containsKey("exp")) {
            String username = claimsJws.getBody().get("sub").toString();
            List<String> roles = (List<String>) claimsJws.getBody().get("roles");
            jwtModel = new JwtModel(username, roles);
        } else {
            throw new SignatureException("Jwt token not valid");
        }
        return jwtModel;
    }

    public static String parseRefreshToken(String token){
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token);

        if (claimsJws.getBody().containsKey("sub") && (boolean) claimsJws.getBody().get("refresh")
                && claimsJws.getBody().containsKey("iat") && claimsJws.getBody().containsKey("exp")){
            return claimsJws.getBody().getSubject();
        }else {
            throw new SignatureException("Jwt token not valid");
        }
    }
}

