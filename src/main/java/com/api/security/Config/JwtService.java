package com.api.security.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "34743777397A24432646294A404E635266556A586E3272357538782F4125442A\n";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); //getSubject is a method reference INTO Claims interface
        //Subject is the email of the user.
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody(); //Parse the token and return the body
        //parseClaimsJws expects a token with signature, parseClaimsJwt expects a token without signature
    }

    /*
     * */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); //Decode the secret key from base64 to bytes array
        return Keys.hmacShaKeyFor(keyBytes); //Return the key to sign the token

    }

    /*
     * @params String token, Function<Claims, T> claimsResolver, example: (claims -> claims.getSubject()) // the subject is the email of the user
     * @return T
     * */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token); //Extract all the claims from the token
        return claimsResolver.apply(claims); //Apply the function claimsResolver to the claims and return a specific claim (subject, expiration, etc)
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder().setClaims(extraClaims). //Set the extra claims like (id, role, etc)
                setSubject(userDetails.getUsername()) //Set the subject of the token
                .setIssuedAt(new Date(System.currentTimeMillis())) //Set the date of creation of the token
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))  //10 hours
                .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact(); //Set the key and the algorithm to sign the token
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(Collections.emptyMap(), userDetails); // Collections.emptyMap() more efficient than new HashMap<>()
    }

    public boolean isValidUsernameToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration); //getExpiration is a method reference INTO Claims interface
    }
}
