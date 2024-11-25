# hands-on-spring-shell
Spring Application 환경에서 명령어 기반 인터페이스를 쉽게 구현할 수 있도록 도와주며 사용자가 터미널에서 명령어를 입력하여 애플리케이션과 상호작용할 수 있는 기능을 제공할 수 있음

# Quick Start
Spring shell의 Command를 정의하는 방법은 2가지의 어노테이션 방식과  bean 등록 방식(programmatic) 으로 총 3가지 방법이 존재함.
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
### @CommandScan 어노테이션 추가
Spring boot App class에 `@CommandScan`어노테이션을 추가하여 앞으로 작성할 @Command 어노테이션들을 Command로서 스캔하도록한다.
```java
@CommandScan //어노테이션 추가
@SpringBootApplication
public class HandsOnSpringShellApplication {
    public static void main(String[] args) {
        SpringApplication.run(HandsOnSpringShellApplication.class, args);
    }
}

```
### Command 추가
`@Command`어노테이션으로 명령을 정의하고 `@Option`어노테이션으로 인자값(옵션)을 정의할 수 있다.
```java
@Command //command를 정의하여 prefix로 활용 가능 e.g. @Command(command = "board")
public class BoardCommand {
    @Command(command = "read", description = "id에 해당하는 게시글을 조회", group = GROUP)
    public String read(
            @Option(description = "조회할 게시글의 id를 입력합니다") String id
    ) {
        Optional<Board> boardOptional = boardRepository.read(Long.valueOf(id));
        if (boardOptional.isEmpty()){
            return """
                    게시글이 존재하지 않습니다.
                    """;
        }

        return """
                게시글을 조회하였습니다.
                %s""".formatted((boardToText(boardOptional.get())));
    }
}
```


## 커맨드 등록 (레거시 어노테이션 사용 방식)
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
어노테이션 방식으로 spring shell 커맨드를 등록하기 위해 사용되는 주요 어노테이션은 `@Command`와 `@Option`이 있음

### @Command
메서드를 Spring shell `Command로 정의`할 때 사용됨
#### Element
- **command** : 커맨드를 정의. (다중 선언 지원)
- **alias** : 별칭을 정의 (다중 선언 지원)
- **group** : 커맨드 그룹의 이름을 선언
- **description** : 커맨드에 대한 설명
- **hidden** : 커맨드 숨김 여부
- **interactionMode** : 어떤 interactionMode에서 활성화 시킬지 선언 (기본값은 모든 mode에서 활성)

### @Option
메서드 인자를 Spring shell `Command의 옵션을 정의`할 때 사용됨
### Element
- **longNames** : 옵션의 이름 정의
- **shortNames** : 옵션의 축약 이름 정의
- **required** : 필수 입력 여부
- **defaultValue** : 기본값
- **description** : 옵션 설명
- **label** : 옵션의 label
- **arity** : 옵션에 전달할 수 있는 값 제한 (OptionArity 열거형에서 선택값 제공)
- **arityMin** : 옵션에 전달할 수 있는 최소 값
- **arityMax** : 옵션에 전달할 수 있는 최대 값

## 레거시 어노테이션 사용 방식
레거시 어노테이션 방식으로 spring shell 커맨드를 등록하기 위해 사용되는 주요 어노테이션은 `@ShellMethod`와 `@ShellOption`가 있음

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