package org.fitness.fitness.Service;

import io.jsonwebtoken.Claims;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final RedisService redisService;
    //this should be minimum 256 bits encryption key
    private static final String SECRET_KEY = "27bc5821f84be3dca9696869c6248a4bbbf23e30906f1a6c7c2b79ce30e3c32a";
    public static String extractemail(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    //if you want to generate token using only userdetails
    public String generateToken(UserDetails userDetails) {
        String token = generateToken(new HashMap<>(), userDetails);
        redisService.storeToken(userDetails.getUsername(),token,60*60*24);
        return token;
    }
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
    return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            //the token will be valid till....
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            //compact() is the method which gonna generate the token....
            .compact();
    }

    //method which can identify is token valid or not
    public boolean isTokenValid(String token , UserDetails userDetails) {
        final String username = extractemail(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token) && redisService.isTokenValid(userDetails.getUsername(), token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

        private Date extractExpiration(String token) {
            return extractClaim(token, Claims::getExpiration);
        }

    private static Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                //it's the signature part of jwt
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private static Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
