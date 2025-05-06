// Import puppeteer
import puppeteer from 'puppeteer';

const targetUrl = "https://google.com";

// Launch the browser
const browser = await puppeteer.launch(
  {
    headless: false
  }
);

// Create a page
const page = await browser.newPage();

// Go to your site
await page.goto(targetUrl);

// Evaluate JavaScript
const three = await page.evaluate(() => {
  return 1 + 2;
});

/*const three = await page.evaluate(() => {
  // wait for 100ms.
  function calc(resolve){
    resolve(1+2);
  }
  return new Promise(resolve => setTimeout(() => calc(resolve), 100));
});*/

/*const three = await page.evaluate(`
  1 + 2
`);*/

/*const three = await page.evaluate(
  (a, b) => {
    return a + b; // 1 + 2
  },
  1,
  2,
);*/

console.log(three);

// Close browser.
await browser.close();
