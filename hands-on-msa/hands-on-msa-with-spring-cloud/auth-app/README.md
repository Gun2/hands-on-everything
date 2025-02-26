# Auth App
인증, 계정을 관리하는 모듈

# 구성 정보
해당 모듈은 Spring Security를 통해 JWT로 인증 과정을 구현

## build.gradle
Spring security와 JWT를 사용하기 위해 다음과 같은 의존성 추가
```groovy
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-security'
	implementation 'org.springframework.boot:spring-boot-starter-web'
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

## UserDetailsService 구현
username에 해당하는 사용자 정보를 가져올 수 있는 UserDetailsService 구현
```java
@Service
public class InMemoryUserDetailsService implements UserDetailsService {

    private final Map<String, UserDetails> users = new HashMap<>();

    public InMemoryUserDetailsService(PasswordEncoder passwordEncoder) {
        // 사용자 3명 추가 (테스트 용도)
        users.put("user1", User.withUsername("user1")
                .password(passwordEncoder.encode("password1"))
                .roles("USER")
                .build());
        ...
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = users.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return copy(user);
    }

    private static UserDetails copy(UserDetails user) {
        return User.withUsername(user.getUsername()).password(user.getPassword()).authorities(user.getAuthorities()).build();
    }
}
```

## JwtUtil 생성
JWT를 생성하고, 유효성 검사하기 위한 용도의 Util 클래스 생성
```java

@Component
public class JwtUtil {

    public static final String COOKIE_NAME = "token";

    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
    // JWT 생성
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1시간 유효
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT에서 사용자 이름 추출
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // JWT에서 Claims 추출
    private Claims extractClaims(String token) {
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
     * cookie 내부의 token 반환
     * @param request
     * @return
     */
    public static Optional<Cookie> getTokenFromCookie(HttpServletRequest request) {
        return Arrays.stream(request.getCookies()).filter(cookie -> JwtUtil.COOKIE_NAME.equals(cookie.getName())).findAny();
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
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<Cookie> tokenCookieOptional = JwtUtil.getTokenFromCookie(request);
        if (tokenCookieOptional.isPresent()) {
            String token = tokenCookieOptional.get().getValue();

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
}
```

## 인증 성공 핸들러 클래스 추가
username & password 인증 성공 시 토큰을 반환할 핸들러 클래스 추가
> Set-Cookie에 token을 넣는 방식
```java
@Component
@RequiredArgsConstructor
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String token = jwtUtil.generateToken(authentication.getName());
        Cookie cookie = new Cookie(JwtUtil.COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "Strict"); // CSRF 방지
        response.addCookie(cookie);

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
            JwtAuthenticationFilter jwtAuthenticationFilter,
            JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler,
            JwtLogoutHandler jwtLogoutHandler
    ) throws Exception {
        http.authorizeHttpRequests(
                        registry -> registry.requestMatchers("/login").permitAll()
                                .anyRequest().authenticated()
                ).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(
                        config -> config.loginProcessingUrl("/login")
                                .successHandler(jwtAuthenticationSuccessHandler)
                ).logout(config -> {
                    //로그아웃 시 jwt를 만료 시키기 위한 핸들러
                    config.addLogoutHandler(jwtLogoutHandler);
                })
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
```


# 인증 과정
1. 클라이언트가 username과 password를 인증 서버에 전달 ( POST /login )
2. `UserDetailsService`구현체가 username에 맞는 사용자 데이타 반환
3. Spring Security의 provider에 의해 인증이 성공하면 성공 핸들러 (JwtAuthenticationSuccessHandler)동작
4. Set-Cookie에 JWT 전달
5. 클라이언트가 Cookie에 JWT 저장
6. 이후 요청의 Cookie에 JWT가 포함됨
7. `JwtAuthenticationFilter`에서 Cookie의 JWT가 유효하면 인증 객체 생성 후 인증 허용