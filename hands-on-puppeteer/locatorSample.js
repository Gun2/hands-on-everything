import puppeteer from 'puppeteer';


const browser = await puppeteer.launch({
  headless: false
});
const targetUrl = "https://google.com";

const page = await browser.newPage();
await page.goto(targetUrl)
await page.locator('textarea').fill('search');

await page.locator('input[role=button]').click();

await page.locator('svg[aria-label="Google 홈으로 이동"]').wait();

await page.locator('div').scroll({
  scrollTop: 200,
});