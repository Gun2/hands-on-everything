import puppeteer from 'puppeteer';

const targetUrl = "https://google.com";

const browser = await puppeteer.launch(
  {
    headless: false
  }
);

const page = await browser.newPage();

await page.goto(targetUrl);

page.on('request', request => {
  console.log(request.url());
});

page.on('response', response => {
  console.log(response.url());
});

