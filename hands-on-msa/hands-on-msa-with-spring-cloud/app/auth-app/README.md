# Auth App
인증, 계정을 관리하는 모듈

# 구성 정보
해당 모듈은 Spring Security를 통해 JWT로 인증 과정을 수행하도록 구성됨

## build.gradle
Spring security와 JWT를 사용하기 위해 다음과 같은 의존성 추가
```groovy
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-security'
	implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'

    ...
	implementation 'io.jsonwebtoken:jjwt-api:0.12.6'
	implementation 'io.jsonwebtoken:jjwt-impl:0.12.6'
	implementation 'io.jsonwebtoken:jjwt-jackson:0.12.6'
}
```

## 환경 변수
application.yml에 비밀키 추가
```shell
$ openssl rand -hex 64
d602667f3493e001490c8cd7aafe4a67a7e4ad846ae11ebc9f57380314aeec294b25af55e4227e1369018bc96d1742dc9f739e8a593129dfcd66a3ab7ecff896
```
```yaml
spring:
  application:
    name: auth-app

jwt:
  secret: d602667f3493e001490c8cd7aafe4a67a7e4ad846ae11ebc9f57380314aeec294b25af55e4227e1369018bc96d1742dc9f739e8a593129dfcd66a3ab7ecff896
```

## 인증 service 구현
사용자 로그인/로그아웃, 토큰 재발급 역할을 하는 서비스 추가
```java

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final AccessTokenBlackListService accessTokenBlackListService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;


    public TokenResponse login(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()){
            throw new UsernameNotFoundException("""
                    username : %s is not found
                    """.formatted(username)
            );
        }
        if (!passwordEncoder.matches(password, userOptional.get().getPassword())){
            throw new BadCredentialsException("""
                    password for username : %s is not matched
                    """.formatted(username));
        }
        TokenResponse tokenResponse = creteAccessTokenAndRefreshToken(username);
        return tokenResponse;
    }

    private TokenResponse creteAccessTokenAndRefreshToken(String username) {
        TokenResponse tokenResponse = createTokenResponse(username);
        saveRefreshToken(tokenResponse);
        return tokenResponse;
    }

    private void saveRefreshToken(TokenResponse tokenResponse) {
        refreshTokenService.save(
                jwtUtil.extractUsername(tokenResponse.getAccessToken()),
                tokenResponse.getAccessToken(),
                tokenResponse.getRefreshToken(),
                jwtUtil.getRefreshTokenExpire()
        );
    }

    /**
     * token 응답값 반환
     * @param username
     * @return
     */
    private TokenResponse createTokenResponse(String username) {
        String token = jwtUtil.generateToken(username);
        return TokenResponse.ofBearer(
                token,
                jwtUtil.getAccessTokenExpire() / 1000,
                jwtUtil.generateRefreshToken(),
                jwtUtil.getRefreshTokenExpire() / 1000
        );
    }

    public void logout(String token) {
        accessTokenBlackListService.logout(token);
        refreshTokenService.removeByAccessToken(token);
    }

    @Transactional
    public TokenResponse refresh(String accessToken, String refreshToken) {
        RefreshToken validate = refreshTokenService.validate(accessToken, refreshToken);
        TokenResponse tokenResponse = creteAccessTokenAndRefreshToken(validate.getUsername());
        expirePreviousToken(accessToken, refreshToken);
        return tokenResponse;
    }

    /**
     * 이전 토큰 정보 만료
     * @param accessToken
     * @param refreshToken
     */
    private void expirePreviousToken(String accessToken, String refreshToken) {
        refreshTokenService.removeRefreshToken(refreshToken);
        accessTokenBlackListService.isBlackListToken(accessToken);
    }
}

```

## JwtUtil 생성
JWT를 생성하고, 유효성 검사하기 위한 용도의 Util 클래스 생성
```java
@Component
public class JwtUtil {

    public static final String HEADER_NAME = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer";

    private final Key key;
    @Getter
    private final Long accessTokenExpire;
    @Getter
    private final Long refreshTokenExpire;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token.expire}") Long accessTokenExpire,
            @Value("${jwt.refresh-token.expire}") Long refreshTokenExpire
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpire = accessTokenExpire;
        this.refreshTokenExpire = refreshTokenExpire;
    }
    // JWT 생성
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + this.accessTokenExpire))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT에서 사용자 이름 추출
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // JWT에서 Claims 추출
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // JWT 유효성 검사
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }

    // 토큰의 유효성 검사
    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }


    /**
     * header 내부의 token 반환
     * @param request
     * @return
     */
    public static Optional<String> getTokenFromHeader(HttpServletRequest request) {
        String headerValue = request.getHeader(JwtUtil.HEADER_NAME);
        if (headerValue == null){
            return Optional.empty();
        }
        if (headerValue.startsWith(TOKEN_PREFIX)){
            return Optional.of(headerValue.substring(TOKEN_PREFIX.length() + 1));
        }
        return Optional.empty();
    }

    /**
     * refresh token 생성
     * @return
     */
    public String generateRefreshToken() {
        return UUID.randomUUID().toString();  // 무작위 값 생성
    }

}
```

## JWT 인증 필터 추가
사용자 요청에 JWT가 존재할 경우 유효성 검사를 수행하고 인증을 처리하는 필터 추가
```java
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AccessTokenBlackListService accessTokenBlackListService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> tokenOptional = JwtUtil.getTokenFromHeader(request);

        if (tokenOptional.isPresent() && isNotTokenExpired(tokenOptional) && isNotBlackListToken(tokenOptional)) {
            String token = tokenOptional.get();
            String username = jwtUtil.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtUtil.validateToken(token, username)) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * blacklist에 등록된 토큰이 아님
     * @param tokenOptional
     * @return
     */
    private boolean isNotBlackListToken(Optional<String> tokenOptional) {
        return !accessTokenBlackListService.isBlackListToken(tokenOptional.get());
    }

    /**
     * 만료된 토큰이 아님
     * @param tokenOptional
     * @return
     */
    private boolean isNotTokenExpired(Optional<String> tokenOptional) {
        return !jwtUtil.isTokenExpired(tokenOptional.get());
    }
}
```

## Security Config 추가
구성사항들을 결합하여 spring security 구성 정보 설정
```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) throws Exception {
        http.authorizeHttpRequests(
                        registry -> registry.requestMatchers("/auth/login").permitAll()
                                .anyRequest().authenticated()
                ).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
```


# 기능별 흐름
해당 서비스에서 제공하는 로그인, 토큰 재발급, 토큰 만료, 인증에 대한 기능의 흐름
## 로그인 과정

사용자가 계쩡의 아이디 패스워드를 인증 서버에 전달하여 인증 토큰(JWT)를 받는 과정
![login_process.jpg](readme%2Flogin_process.jpg)
1. 사용자가 `username`, `password` 값으로 로그인 요청
2. 로그인 정보 확인
3. 검증될 경우 JWT(`access token`) 생성
4. `refresh token` 생성
5. 사용자에게 `token` 정보 반환

### request
```http request
POST /auth/login

{
    "username" : "user1",
    "password" : "password1"
}
```
### response
```json
{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsImlhdCI6MTc0MTE4MDQ2NCwiZXhwIjoxNzQxMTgwNDk0fQ.fosi36yGT64dK1IQkOR4nGHvfWUxT9o0c20eCN4f9Vk",
    "accessTokenExpiresIn": 30,
    "refreshTokenExpiresIn": 60,
    "tokenType": "Bearer",
    "refreshToken": "d06c5a6c-7965-4d20-a502-2fe4e591e8a5"
}
```

## 인증 과정
사용자가 발급받은 access token을 인증 서버가 인증하는 과정

![authentication_process.jpg](readme%2Fauthentication_process.jpg)
1. 사용자가 `access token`을 헤더에 넣고 요청
2. 인증 서버의 `filter`가 `access token`의 유효성 검사
3. 검증에 성공하면 인증 객체를 생성하고 `인증 처리`

### request
```http request
GET /
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsImlhdCI6MTc0MTE4MDQ2NCwiZXhwIjoxNzQxMTgwNDk0fQ.fosi36yGT64dK1IQkOR4nGHvfWUxT9o0c20eCN4f9Vk

```
### response
```
Hello
```

## 인증 토큰 재발급
사용자가 refresh 토큰을 통해 인증 토큰을 재발급 받는 과정

![refresh token precess.jpg](readme%2Frefresh%20token%20precess.jpg)
1. 사용자가 access token을 `재발급` 하기 위해 `access token`과 `refresh token`을 인증 서버에 전달
2. `refresh token`이 유효한지 `access token`과 함께 검증
3. 검증에 성공하였을 경우 기존 `access token`과 `refresh token`를 만료시킴
4. 새로운 `access token`과  `refresh token` 생성 후 사용자에게 반환

### request
```http request
POST /auth/refresh
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsImlhdCI6MTc0MTE4MDQ2NCwiZXhwIjoxNzQxMTgwNDk0fQ.fosi36yGT64dK1IQkOR4nGHvfWUxT9o0c20eCN4f9Vk

{
      "refreshToken": "cfdd6b6f-953f-40a9-8c38-e021e2117f31"
}
```
### response
```json
{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsImlhdCI6MTc0MTE4NDI5MiwiZXhwIjoxNzQxMTg0MzIyfQ.Im4VkXlHxt-9Lbq7op2z5LEn3ovsJa0udK1hRlYy-5k",
    "accessTokenExpiresIn": 30,
    "refreshTokenExpiresIn": 60,
    "tokenType": "Bearer",
    "refreshToken": "5a280f2a-b5b6-4573-b21b-cc0f290a03ca"
}
```

## 토큰 만료
사용자가 로그아웃을 통해 토큰을 만료시키는 과정

![expire token process.jpg](readme%2Fexpire%20token%20process.jpg)
1. 사용자가 `access token`을 헤더에 넣고 로그아웃 요청
2. 인증 서버가 `access token`을 `black list`에 추가
3. `refresh token`을 만료

### request
```http request
POST /auth/logout
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsImlhdCI6MTc0MTE4MDQ2NCwiZXhwIjoxNzQxMTgwNDk0fQ.fosi36yGT64dK1IQkOR4nGHvfWUxT9o0c20eCN4f9Vk

```
### response
```
200 OK
```