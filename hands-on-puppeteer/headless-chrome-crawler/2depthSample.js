import HCCrawler from 'headless-chrome-crawler';
import fs from 'fs';
const PATH = './tmp/';
const TARGET_URL = 'https://example.com/';

if (!fs.existsSync(PATH)){
  fs.mkdirSync(PATH);
}

const tree = {
  url: 'ROOT',
  children: []
};

const urlNodeMap = new Map();

const crawler = await HCCrawler.launch({
  headless: false,
  maxDepth: 2,
  delay: 1000,
  maxConcurrency: 1,
  onSuccess: result => {
    console.log('result', result);
    const parentUrl = result.previousUrl || 'ROOT';
    const currentUrl = result?.response?.url;
    console.log(`parentUrl : ${parentUrl} \ncurrentUrl : ${currentUrl}`)

    // 중복 방지
    if (urlNodeMap.has(currentUrl)) return;

    const node = { url: currentUrl, children: [] };
    urlNodeMap.set(currentUrl, node);

    const parentNode = parentUrl === 'ROOT'
      ? tree
      : urlNodeMap.get(parentUrl);

    if (parentNode) {
      parentNode.children.push(node);
    }
  },
  skipDuplicates: tree
});

await crawler.queue(TARGET_URL);

await crawler.onIdle();
await crawler.close();

console.log('Tree structure:');
const treeResult = JSON.stringify(tree, null, 2);
console.log(treeResult);
fs.writeFileSync(`${PATH}/2depth-tree-result.txt`, treeResult);
