import HCCrawler from 'headless-chrome-crawler';
import fs from 'fs';
const PATH = './tmp/';

if (!fs.existsSync(PATH)){
  fs.mkdirSync(PATH);
}

const visited = new Set();
const tree = {};

function buildTree(parent, url) {
  const parts = url.replace(/^https?:\/\//, '').split('/');
  let current = parent;

  for (const part of parts) {
    if (!current[part]) {
      current[part] = {};
    }
    current = current[part];
  }
}

const crawler = await HCCrawler.launch({
  headless: false,
  maxDepth: 2,
  evaluatePage: () => ({}), // 최소 정보만 추출
  onSuccess: result => {
    console.log(`Crawled: ${result?.response?.url}`);
    visited.add(result?.response?.url);
    buildTree(tree, result?.response?.url);
  },
  preRequest: options => {
    // 중복 방문 방지
    if (visited.has(options.url)) return false;
    return true;
  },
});

await crawler.queue('https://example.com/');

await crawler.onIdle();
await crawler.close();

console.log('Tree structure:');
const treeResult = JSON.stringify(tree, null, 2);
console.log(treeResult);
fs.writeFileSync(`${PATH}/2depth-tree-result.txt`, treeResult);
