// browser context를 통한 격리 테스트

import puppeteer from 'puppeteer';

const browser = await puppeteer.launch();
const targetUrl = "https://google.com";
//context로 격리
const context = await browser.createBrowserContext();
const context2 = await browser.createBrowserContext();

const page1 = await context.newPage();
await page1.goto(targetUrl)
const page1Cookies = await context.cookies()

const page2 = await context.newPage();
await page2.goto(targetUrl)
const page2Cookies = await context.cookies()


const page3 = await context2.newPage();
await page3.goto(targetUrl)
const page3Cookies = await context2.cookies()


console.log("page1 AEC And page 2 AEC are equal : ", getAEC(page1Cookies) === getAEC(page2Cookies));


console.log("But page1 AEC And page 3 AEC are not equals : ", getAEC(page1Cookies) === getAEC(page3Cookies));
//context의 페이지들이 함께 종료됨
await context.close();

await browser.close();

function getAEC(cookies){
  return cookies.find(cookie => cookie.name === "AEC").value
}