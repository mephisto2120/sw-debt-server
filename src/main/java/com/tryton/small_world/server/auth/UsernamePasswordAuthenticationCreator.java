package com.tryton.small_world.server.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UsernamePasswordAuthenticationCreator {

    public Authentication getAuthenticationByToken(String token) {
        String secretKey = TextCodec.BASE64URL.encode("topSecret12#");
        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token);

        String username = claimsJws.getBody().get("sub").toString();
        String rolesStr = claimsJws.getBody().get("roles").toString();
        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = getSimpleGrantedAuthorities(rolesStr);
        return new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuthorities);
    }

    private static Set<SimpleGrantedAuthority> getSimpleGrantedAuthorities(String rolesStr) {
        return Arrays.stream(rolesStr.split(","))
//                .map(roleName -> "ROLE_" + roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

}
