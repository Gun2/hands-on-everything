import puppeteer from 'puppeteer';

(async () => {
  const browser = await puppeteer.launch({
    headless: false,
    defaultViewport: null
  });

  const page = await browser.newPage();

  // 유튜브 접속
  await page.goto('https://www.youtube.com', { waitUntil: 'networkidle2' });
  // 검색창에 "Korea Music Top 100" 입력
  await page.waitForSelector('input[name=search_query]');
  await page.type('input[name=search_query]', 'Korea Music Top 100');
  await page.keyboard.press('Enter');


  // 검색 결과 대기
  await page.waitForSelector('ytd-in-feed-ad-layout-renderer', {
    timeout: 5000,
  });
  // 광고 제거
  await page.evaluate(() => {
    const elements = document.querySelectorAll('ytd-in-feed-ad-layout-renderer');
    console.log(elements)
    if (elements?.length && elements?.length > 0)
    {
      [...elements].forEach(element => element.remove());
    }
  });

  await page.evaluate(() => {
    document.querySelectorAll('.yt-thumbnail-view-model__image img')[0].click()
  });

  //동영상 재생 페이지 기다리기
  await page.evaluate(() => {
    return new Promise(resolve => {
      const video = document.querySelector('video');
      if (!video) {
        resolve('no video');
        return;
      }
      if (!video.paused) {
        // 이미 재생 중이면 바로 종료
        resolve('playing');
      } else {
        // 재생 이벤트를 기다림
        video.addEventListener('play', () => {
          resolve('started');
        }, { once: true });
      }
    });
  });

  await page.evaluate(() => {
    return new Promise(resolve => setTimeout(resolve, 5000));
  });

  await page.screenshot({
    path: "./korea music top 100 in youtube.png",
    type: "png"
  });

  await page.browser().close()
})();