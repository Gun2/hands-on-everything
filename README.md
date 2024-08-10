# hands-on-spring-shell
Spring Application 환경에서 명령어 기반 인터페이스를 쉽게 구현할 수 있도록 도와주며 사용자가 터미널에서 명령어를 입력하여 애플리케이션과 상호작용할 수 있는 기능을 제공할 수 있음

# Quick Start

## 의존성 추가
```groovy
ext {
    set('springShellVersion', "3.3.1")
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter'
    implementation 'org.springframework.shell:spring-shell-starter'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

dependencyManagement {
    imports {
        mavenBom "org.springframework.shell:spring-shell-dependencies:${springShellVersion}"
    }
}
```

## Spring Shell 설정 추가
```properties
#spring shell 인터랙티브 모드 활성화 
# (인터렉티브 모드가 false일 경우 사용 방식이 prompt 방식의 사용이 아닌 단건으로 호출 하는 방식)
spring.shell.interactive.enabled=true
```


## 커맨드 등록 (어노테이션 사용 방식)
`@ShellComponent`와 `@ShellOption` 어노테이션을 사용하여 간편하게 spring shell 커맨드를 등록할 수 있다.
```java
//쉘 컴포넌트로 생성
@ShellComponent
public class BoardCommand {

    /**
     * @ShellMethod 어노테이션을 통해 쉘 커맨드로 사용될 정보 설정
     * key : cli로 입력할 커맨드
     * prefix : 인자 앞에 붙을 prefix (기본값 --)
     * value : help 커맨드 입력 시 해당 커맨드의 설명으로 출력됨
     * group : 커맨드의 그룹 지정 (기본값은 해당 메서드가 위치한 클래스 명칭을 따라감)
     * 
     */
    @ShellMethod(key = "create", prefix = "-", value = "게시글 생성")
    public String create(
            @ShellOption(
                    value = {"t", "title"},
                    help = "게시글의 제목을 입력합니다."
            ) String title,
            @ShellOption(
                    value = {"c", "content"},
                    help = "게시글의 내용을 입력합니다."
            ) String content

    ) {
        ...
        return """
                게시글을 작성하였습니다.
                %s""".formatted((boardToText(board)));
    }
    
}
```

## 커맨드 등록 (bean 등록 방식)
`CommandRegistration`클래스의 빌더 패턴을 통해 생성된 객체를 spring bean으로 등록하여 spring shell 커맨드를 등록할 수 있으며 `어노테이션을 사용한 방식보다 조금 더 상세한 설정`이 가능하다.
```java
@Bean
    CommandRegistration delete() {
        return CommandRegistration.builder()
                .group(GROUP)
                .command("delete")
                .description("id에 해당하는 게시글을 삭제")
                .withHelpOptions().shortNames('h').longNames("help").command("help").and()
                .withOption().required().type(Long.TYPE).longNames("id").description("삭제할 게시글의 id를 입력합니다.").and()
                .withTarget().function(commandContext -> {
                    Long id = commandContext.getOptionValue("id");
                    boardRepository.delete(id);

                    return """
                            게시글을 삭제하였습니다.
                            """;
                }).and()
                .build();
    }
```

# 정리

## 어노테이션 사용 방식
spring shell 커맨드를 등록하기 위해 사용되는 주요 어노테이션은 `@ShellMethod`와 `@ShellOption`가 있음

### @ShellMethod
메서드에 선언하여 Spring Shell의 커맨드를 정의할 때 사용됨
#### Element
- **group** : 커맨드 그룹의 이름을 선언
- **interactionMode** : 어떤 interactionMode에서 활성화 시킬지 선언 (기본값은 모든 mode에서 활성)
- **key** : Spring Shell에서 호출에 사용될 이름
- **prefix** : 할당된 파라미터들의 이름 앞에 부여될 prefix (기본값은 `--`이며 이로 인해 모든 파라미터 앞에는 `--`을 선언 해야함 e.g. 파라미터 이름을 `p`라고 정의할 경우 `--p`로 선언하여 사용)
- **value** : 커맨드에 대한 설명

### @ShellOption
메서드의 인자에 선언하여 커맨드의 파라미터를 선언
#### Element
- **arity** : 파라미터가 전달받을 수 있는 값의 개수를 선언 (기본값은 -1이며 전달 개수에 제한이 없음)
- **defaultValue** : 전달받은 값이 없는 경우 사용할 기본값
- **help** : help에서 출력될 문구
- **optOut** : ~~더이상 사용되지 않음~~
- **value** : 파라미터의 이름
- **valueProvider** : 입력된 파라미터 값을 특정 인자값으로 변환할 때 사용됨(`argument resolver`와 비슷)

# 참고
- https://docs.spring.io/spring-shell/reference/getting-started.html
- https://www.baeldung.com/spring-shell-cli