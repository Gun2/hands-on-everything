import HCCrawler from 'headless-chrome-crawler';
import fs from 'fs';
const PATH = './tmp/';

if (!fs.existsSync(PATH)){
  fs.mkdirSync(PATH);
}

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
    console.log(result);
  }),
});
// Queue a request
await crawler.queue('https://example.com/');

// Queue multiple requests
await crawler.queue(['https://example.net/', 'https://example.org/']);

// Queue a request with custom options
await crawler.queue({
  url: 'https://example.com/',
  // Emulate a tablet device
  device: 'Nexus 7',
  // Enable screenshot by passing options
  screenshot: {
    path: `${PATH}/example-com.png`,

  },
});
await crawler.onIdle(); // Resolved when no queue is left
await crawler.close(); // Close the crawler