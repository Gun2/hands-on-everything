# 프로젝트 개요
puppeteer 핸즈온 프로젝트
> https://pptr.dev/

# Puppeteer란?
Puppeteer는 `DevTools Protocol`또는 `WebDriver BiDi`위에서 크롬 또는 파이어폭스를 제어하는 고수준의 API를 제공하는 javascript 라이브러리
- ## DevTools Protocol
`크롬` 브라우저 내부 기능에 접근할 수 있도록 제공되는 저수준 통신 프로토콜

- WebDriver BiDi
기존 WebDriver의 단방향 통신을 보완한 양방향(Bidirectional) 통신 프로토콜로 `파이어폭스` 제어에 활용됨


# 설치
```shell
npm i puppeteer
# 내장 브라우저가 필요 없을 경우
# npm i puppeteer-core
```

# 컨셉
## 브라우저 관리
Puppeteer 동작을 위해 브라우저를 실행하거나 연결이 필요
### 브라우저 실행하기
브라우저를 실행하고 새로운 페이지를 열기
```javascript
import puppeteer from 'puppeteer';

const browser = await puppeteer.launch();

const page = await browser.newPage();
await page.goto('https://example.com');

```
### 브라우저 닫기
동작 완료 후 브라우저를 닫기
```javascript
import puppeteer from 'puppeteer';

const browser = await puppeteer.launch();

const page = await browser.newPage();
await page.goto('https://example.com');

await browser.close();
```

### 브라우저 Context
자동화된 업무를 격리하고자 할때 (캐시, 로컬 스토리지 등) `BrowserContexts`를 사용할 수 있다.
```javascript
import puppeteer from 'puppeteer';

const browser = await puppeteer.launch();

//context로 격리
const context = await browser.createBrowserContext();

const page1 = await context.newPage();
const page2 = await context.newPage();
//context의 페이지들이 함께 종료됨
await context.close();
```

## 페이지 상호작용
Puppeteer는 마우스, 키보드 입력, 터치 이벤트 등과 함께 페이지에서 element들과 상호작용을 지원함

### Locators
Locators는 element를 선택하고 상호작용하기에 추천되는 방법이다.

#### Input 채우기
```javascript
await page.locator('input[name=search_query]').fill('how to upload videos on youtube');
```

#### Click
```javascript
await page.locator('.ytSearchboxComponentSearchButton').click();

```

#### 나타날 때 까지 기다리기
```javascript
await page.locator('ytd-item-section-renderer').wait();
```

## Javascript 실행
puppeteer는 페이지의 context에서 동작하는 javascript function을 실행시킬 수 있음.

### 예시
`page.evaluate(...)`를 통해 javascript를 실행시킬 수 있음. evaluate메서드의 인자값에는 function이 들어가며, puppeteer가 해당 function을 string으로 변환한 뒤
페이지에서 실행하는 방식.
```javascript
const three = await page.evaluate(() => {
  return 1 + 2;
});
console.log(three); // 3

//아래도 동일하게 동작됨
const three = await page.evaluate(`
    1 + 2
`);
console.log(three); // 3
```

### Promise 반환
Promise 반환 타입을 가지게 된다면 실행이 완료될 때 까지 자동으로 대기
```javascript
const three = await page.evaluate(() => {
  // wait for 100ms.
  function calc(resolve){
    resolve(1+2);
  }
  return new Promise(resolve => setTimeout(() => calc(resolve), 100));
});
console.log(three); // 3
```

### 인자값 전달하기
실행할 function에 인자값을 넣어야 하는 경우 evaluate(...)의 2번째 인자값 부터 넣어줄 수 있다.
```javascript
const three = await page.evaluate(
  (a, b) => {
    return a + b; // 1 + 2
  },
  1,
  2,
);
console.log(three); // 3
```

## 네트워크 로그
Puppeteer은 page에서 발생하는 모든 request와 response를 수신함
```javascript
const page = await browser.newPage();
page.on('request', request => {
  console.log(request.url());
});

page.on('response', response => {
  console.log(response.url());
});
```

# headless-chrome-crawler
Headless Chrome을 통해 동적인 웹 사이트에 대한 크롤링을 지원하는 라이브러리로서 Puppeteer를 사용함
> 참고 : https://github.com/yujiosaka/headless-chrome-crawler?tab=readme-ov-file

# 설치
```shell
npm i headless-chrome-crawler
```

# 주요 기능
- 분산 크롤링 지원
- 동시성, 요청 지연, 재시도 설정 가능
- 깊이 우선/너비 우선 탐색 지원
- Redis 등 캐시 저장소 연동 가능
- CSV 및 JSON Lines 형식의 결과 내보내기
- 최대 요청 수 도달 시 일시 정지 및 재개
- jQuery 자동 삽입
- 스크린샷 저장 기능
- 디바이스 및 사용자 에이전트 에뮬레이션
- 우선순위 기반 큐 지원
- robots.txt 준수
- sitemap.xml 추적
- Promise 기반 API 제공

# 샘플
```javascript

const crawler = await HCCrawler.launch({
  headless: false,
  // 페이지에서 실행할 javascript 함수를 정의할 수 있으며 결과값이 result에 포함되어 반환됨
  evaluatePage: (() => ({
    title: $('title').text(),
  })),
  onSuccess: (result => {
    console.log(`url : ${result?.response?.url}`);
    console.log(`title : ${result?.result?.title}`);
    console.log(`depth : ${result?.depth}`);
  }),
});
// 큐에 URL 삽입
await crawler.queue('https://example.com/');
await crawler.queue(['https://example.net/', 'https://example.org/']);

//큐를 모두 실행할 때 까지 대기
await crawler.onIdle(); // Resolved when no queue is left
await crawler.close(); // Close the crawler
```