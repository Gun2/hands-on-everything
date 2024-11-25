# Hands On Flyway
flyway 핸즈온 프로젝트

## Flyway란?
Database 마이그레이션 tool으로서 현재 버전에서 다음 버전의 스키마로 업데이트해주는 역할을 하고
<br/>스키마의 버전관리 뿐만 아니라 실수없이 변경사항을 적용할 수 있도록 도와준다



## Quickstart (Gradle)
> 참고 : https://documentation.red-gate.com/fd/quickstart-gradle-184127577.html
### 의존성 추가
```groovy
//build.gradle
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath "org.flywaydb:flyway-mysql:11.0.0"
    }
}
plugins {
    ...
    id "org.flywaydb.flyway" version "10.0.0"
    ...
}

flyway {
    url = project.flywayUrl
    user = project.flywayUser
    password =  project.flywayPassword
}
```
## gradle flyway properties 설정
```properties
# gradle.properties
# build.gradle의 flyway에 사용될 property 정의
flywayUrl=jdbc:mariadb://localhost:3306/flyway
flywayUser=test
flywayPassword=test
```

## 첫 마이그레이션 생성
첫 번째 스키마 버전을 생성
> 파일명은 `<Prefix><Version>__<Description>.sql` 형태로 작성한다
> - Prefix : 접두사가 위치하며 보통 V를 사용함 (Undo의 경우 U 사용)
> - Version : 마이그레이션 버전을 나타내며 major와 minor 버전은 언더스코프 2개 (__) 로 작성한다
> - Description : 마이그레이션의 설명을 나타낸다.

```mariadb
# src/main/resources/db/migration/V1__Create_person_table.sql
create table PERSON (
    ID int not null,
    NAME varchar(100) not null
);
```

## Migrate (Gradle)
```shell
./gradlew flywayMigrate
```

## 결과 확인 1
```shell
./gradlew flywayInfo
```
```
Schema version: 1
+-----------+---------+---------------------+------+---------------------+---------+----------+
| Category  | Version | Description         | Type | Installed On        | State   | Undoable |
+-----------+---------+---------------------+------+---------------------+---------+----------+
| Versioned | 1       | Create person table | SQL  | 2024-11-26 00:35:55 | Success | No       |
+-----------+---------+---------------------+------+---------------------+---------+----------+
```

## 컬럼 변경(스키마 변경사항 추가)
```mariadb
# src/main/resources/db/migration/V2__Update_person_table.sql
ALTER TABLE PERSON ADD REAL_NAME VARCHAR(100);

UPDATE PERSON SET REAL_NAME = NAME, NAME='CREATE NEW NAME';
```

## Migrate (Gradle)
```shell
./gradlew flywayMigrate
```

## 결과 확인 2
```shell
./gradlew flywayInfo
```
```
Schema version: 2
+-----------+---------+---------------------+------+---------------------+---------+----------+
| Category  | Version | Description         | Type | Installed On        | State   | Undoable |
+-----------+---------+---------------------+------+---------------------+---------+----------+
| Versioned | 1       | Create person table | SQL  | 2024-11-26 00:35:55 | Success | No       |
| Versioned | 2       | Update person table | SQL  | 2024-11-26 00:37:21 | Success | No       |
+-----------+---------+---------------------+------+---------------------+---------+----------+
```

