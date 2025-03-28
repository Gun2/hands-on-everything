# Auth App
인증, 계정을 관리하는 모듈

# 구성 정보
해당 모듈은 Spring Security를 통해 session으로 사용자 인증 과정을 수행하고, 서비스 인증을 위해 JWT 형식의 Passport를 발급하는 역할을 수행
![auth_architecture.png](readme%2Fauth_architecture.png)

## build.gradle
Spring security와 JWT를 사용하기 위해 다음과 같은 의존성 추가
```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.session:spring-session-jdbc'
    implementation 'org.springframework.boot:spring-boot-starter-jdbc'
}
```

## 환경 변수
application.yml에 passport 비밀키 설정 및 세션 영속성 설정 추가
```shell
$ openssl rand -hex 64
d602667f3493e001490c8cd7aafe4a67a7e4ad846ae11ebc9f57380314aeec294b25af55e4227e1369018bc96d1742dc9f739e8a593129dfcd66a3ab7ecff896
```
```yaml
spring:
  session:
    store-type: jdbc
app:
    passport:
      secret: d602667f3493e001490c8cd7aafe4a67a7e4ad846ae11ebc9f57380314aeec294b25af55e4227e1369018bc96d1742dc9f739e8a593129dfcd66a3ab7ecff896
      expire: 60000
```

## Security config
security 설정
```java

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final AppAuthenticationEntryPoint appAuthenticationEntryPoint;
    private final String LOGIN_URL = "/auth/login";
    private final String LOGOUT_URL = "/auth/logout";

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            LoginSuccessHandler loginSuccessHandler,
            CustomLogoutSuccessHandler customLogoutSuccessHandler
    ) throws Exception {
        http.authorizeHttpRequests(
                        registry -> registry.anyRequest().authenticated()
                ).addFilterBefore(new JsonToFormUrlEncodedFilter(LOGIN_URL), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(config -> config.authenticationEntryPoint(appAuthenticationEntryPoint))
                .formLogin(config -> config.loginProcessingUrl(LOGIN_URL).permitAll().successHandler(loginSuccessHandler))
                .logout(config -> config.logoutUrl(LOGOUT_URL).permitAll().logoutSuccessHandler(customLogoutSuccessHandler))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 세션 필요 시 생성
                        .maximumSessions(1) // 한 계정당 한 세션만 유지
                        .expiredSessionStrategy(event -> log.warn("세션 만료됨: " + event.getSessionInformation().getSessionId())));
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
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


## 로그인 과정

사용자가 계정의 아이디 패스워드를 인증 서버에 전달하여 session을 받는 과정
![login_process.png](readme%2Flogin_process.png)

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
  "code": "OK",
  "message": "Success",
  "data": {
    "session": "ZTMwNDg0MzQtMzgxMC00ZWViLTgxODItMmJlZWU1MzljYmU5"
  }
}
```