import puppeteer from 'puppeteer';


const browser = await puppeteer.launch({
  defaultViewport: null,
  headless: false,
});
const targetUrl = "https://youtube.com";

const page = await browser.newPage();

await page.goto(targetUrl)

await page.locator('input[name=search_query]').fill('how to upload videos on youtube');

console.log('input search');

await page.locator('.ytSearchboxComponentSearchButton').click();

await page.locator('ytd-item-section-renderer').wait();

console.log('moved to video list page');
