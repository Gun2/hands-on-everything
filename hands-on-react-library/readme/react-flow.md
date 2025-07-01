# react-flow
노드 기반의 에디터 혹은 인터렉티브한 다이어그램을 구현하기 위한 라이브러리

# 설치
```shell
npm install @xyflow/react
```

# 핵심 기능
- 쉬운 사용법 : React flow는 node 드래그, 줌 인/아웃, 뷰 이동(panning), 다중 노드, 엣지 선택, 엣지 추가/제거 등 다양한 기능을 제공
- 커스텀 : 노드 또는 엣지를 react component 형태로 커스텀 하는 기능을 제공
- 빠른 랜더링 : 변화가 있거나 뷰에 보이는 노드들만 랜더링
- 내장 플러그인: 
    > - `<Background />` : 커스터마이징 가능한 배경 패턴 구현 플러그인
    > - `<MiniMap />` : 코너에 미니맵 출력 플러그인
    > - `<Controls />` : 줌, 중앙, 락 제어 플러그인
    > - `<Panel />` : 콘텐츠를 뷰포트 위에 위치시키는 플러그인
    > - `<NodeToolbar />` : 노드에 붙여지는 툴바를 쉽게 만들어주는 플러그인
    > - `<NodeResizer />` : 노드에 리사이즈의 기능을 쉽게 만들어주는 플러그인
- 신뢰성 : React Flow는 전체적으로 타입스크립트로 작성되었고 E2E테스트를 진행하여 신뢰성을 유지함

# 용어
![img.png](images/react-flow-terms.png)

# 컨셉
- flow는 노드와 엣지로 구성.
- 노드와 엣지 배열을 ReactFlow 컴포넌트에 전달.
- 모든 노드와 엣지의 ID는 고유해야함.
- 노드는 위치와 라벨이 필요 (커스텀 노드는 다를 수 있음).
- 엣지는 소스 노드 ID와 타겟 노드 ID가 필요


# Controlled, Uncontrolled
React flow는 controlled 방식과 uncontrolled 방식 둘 다 지원
## Controlled
노드와 엣지의 상태를 React state로 관리하여 props를 통해 ReactFlow에 전달.

### 구현 샘플
- 상태 관리: 노드와 엣지를 상태로 관리 (useState 사용)
- ReactFlow에 전달: 상태값을 ReactFlow 컴포넌트의 nodes와 edges props로 전달
```tsx
const Controlled = () => {
  const [nodes, setNodes, onNodesChange] = useNodesState([...]);  // 노드 상태
  const [edges, setEdges, onEdgesChange] = useEdgesState([...]);  // 엣지 상태

  return (
    <ReactFlow
      nodes={nodes}
      edges={edges}
      onNodesChange={onNodesChange}
      onEdgesChange={onEdgesChange}
    />
  );
};
```

## Uncontrolled Flow (상태 미관리)
ReactFlow가 상태를 자동으로 관리하며, 노드와 엣지를 default 값으로 전달

### 구현 샘플
- 상태 관리하지 않음: nodes와 edges를 React Flow가 자동으로 관리.
- 노드 및 엣지 데이터 전달: 노드와 엣지를 단순히 초기값으로 전달
```tsx
const Uncontrolled = () => {
  const [nodes, setNodes] = useState([...]);
  const [edges, setEdges] = useState([...]);
  return (
    <ReactFlow
      defaultNodes={nodes}
      defaultEdges={edges}
    />
  );
};
```

# Layouting
서드파티 라이브러리들을 통해 node들을 레이아웃 해주는 기능을 지원
## Dagre
Dagre는 그래프 구조를 자동으로 레이아웃해주는 라이브러리, 
노드와 엣지 간의 관계를 기반으로 위치를 자동으로 계산
> 드와 엣지의 구조만 정의하면, 시각적으로 보기 좋은 배치를 Dagre가 자동으로 정해주는 방식
### 설치
```shell
npm i @dagrejs/dagre
```

### 사용법
> react flow에서 사용하는 방법은 `<ReactFlowLayoutingWithDagre/>` 컴포넌트 참고
```ts
//graph 객체 생성
const g = new Dagre.graphlib.Graph();
//graph 설정
g.setGraph({ rankdir: 'TB' });
/*
 * 기본 label 구조 지정
 * e.g. (g.setDefaultEdgeLabel(() => ({})) 라인 의미)
 * g.setEdge("a", "b"); // label 안 넣었지만
 * g.setEdge("a", "b", {}); // 빈 객체가 자동으로 들어감
 */
g.setDefaultEdgeLabel(() => ({}));
//node 정의
g.setNode("kspacey", { label: "Kevin Spacey", width: 144, height: 100 });
//edge 정의
g.setEdge("kspacey", "swilliams");
//레이아웃 실행
dagre.layout(g);
//결과
g.node(id) // { x, y, width, height, ... }
g.edge(source, target) // { points: [{x, y}, {x, y}, ...] }
```

### setGraph 옵션
| 속성명         | 기본값                 | 설명                                                                                        |
| ----------- | ------------------- | ----------------------------------------------------------------------------------------- |
| `rankdir`   | `'TB'`              | 계층 방향: `'TB'` (Top→Bottom), `'BT'` (Bottom→Top), `'LR'` (Left→Right), `'RL'` (Right→Left) |
| `align`     | `undefined`         | 같은 계층 내 노드 정렬 방향: `'UL'`, `'UR'`, `'DL'`, `'DR'`                                          |
| `nodesep`   | `50`                | 같은 계층 내 노드 간 수평 간격 (px)                                                                   |
| `edgesep`   | `10`                | 엣지 간 간격 (선들 사이의 거리, px)                                                                   |
| `ranksep`   | `50`                | 계층 간 수직 간격 (px). `rankdir`에 따라 좌우 간격이 되기도 함                                               |
| `marginx`   | `0`                 | 전체 그래프의 좌우 여백 (px)                                                                        |
| `marginy`   | `0`                 | 전체 그래프의 상하 여백 (px)                                                                        |
| `acyclicer` | `undefined`         | `'greedy'` 설정 시, 순환(edge cycle)을 제거하는 **탐욕적 알고리즘** 사용                                     |
| `ranker`    | `'network-simplex'` | 계층 결정 알고리즘:<br>– `'network-simplex'` (기본)<br>– `'tight-tree'`<br>– `'longest-path'`       |


### node 옵션
| 속성명      | 기본값 | 설명             |
| -------- | --- | -------------- |
| `width`  | `0` | 노드의 가로 크기 (px) |
| `height` | `0` | 노드의 세로 크기 (px) |


### edge 옵션
| 속성명           | 기본값   | 설명                                                 |
| ------------- | ----- | -------------------------------------------------- |
| `minlen`      | `1`   | 엣지의 최소 계층 수 (source와 target 사이의 거리 계층 수)           |
| `weight`      | `1`   | 엣지의 중요도. 값이 클수록 **직선화되고 짧아짐**                      |
| `width`       | `0`   | 엣지 라벨의 가로 크기 (px)                                  |
| `height`      | `0`   | 엣지 라벨의 세로 크기 (px)                                  |
| `labelpos`    | `'r'` | 라벨 위치: `'l'` = left, `'c'` = center, `'r'` = right |
| `labeloffset` | `10`  | 라벨과 엣지 간 거리 (labelpos가 `'l'` 또는 `'r'`일 때만 적용됨)     |

## d3-hierarchy
d3 hierarchy는 node가 1개 있을 때 root node로 하여 레이아웃해주는 라이브러리이다.
> root node가 1개가 아니거나, node의 width, height 사이즈가 서로 다른 경우 올바르게 동작되지 않을 수 있음
### 설치
```shell
npm i d3-hierarchy
npm i --save-dev @types/d3-hierarchy
```

### 사용법
> react flow 적용 샘플은 `<ReactFlowLayoutingD3Hierarchy/>` 컴포넌트 참고
```ts
// d3-hierarchy 트리 레이아웃 생성기
const g = tree<NodeData>();

// id/parentId 기반의 평탄한 배열을 계층 구조로 바꿈
const hierarchy = stratify<NodeData>()
        .id((node) => node.id) // 고유 ID 지정
        .parentId((node) =>
                edges.find((edge) => edge.target === node.id)?.source
        ); // 부모 노드 ID를 엣지 정보로부터 유추

// 계층 트리 생성 (노드 배열을 트리 구조로 변환)
const root = hierarchy(nodes as any);

// 트리 레이아웃 계산: 각 노드에 x, y 위치 자동 계산
const layout = g.nodeSize([width * 2, height * 2])(root as HierarchyNode<NodeData>);
```

## d3-force
물리 기반 라이브러리로서 각기 다른 힘을 노드에 적용하여 위치를 변경할 수 있다.

### 설치
```shell
npm i d3-force
npm i @types/d3-force
npm i d3-quadtree
npm i @types/d3-quadtree
```

### 사용법
> react flow 적용 샘플은 `<ReactFlowLayoutingWithD3Force/>` 컴포넌트 참고
```ts
/*
 * 시뮬레이션 엔진을 초기화하고 여러 개의 force를 등록할 수 있는 중앙 허브 역할.
 * 제네릭으로 Node + SimulationNodeDatum 확장한 타입을 넣어 node의 x, y, vx, vy, fx, fy 등을 추적.
 */
const simulation = forceSimulation<SimulationNodeDatum & Node>()
        .force('charge', forceManyBody().strength(-1000)) //forceManyBody() : 노드 간 반발력 (음수 = 밀어냄, 양수 = 끌어당김)
        .force('x', forceX().x(0).strength(0.05)) //forceX/Y() : X, Y축 기준으로 끌어당김 (그래프 중심 정렬용)
        .force('y', forceY().y(0).strength(0.05))
        .force('collide', collide())  //collide() : 사용자 정의 충돌 방지 force (quadtree 사용)
        .alphaTarget(0.05) //alphaTarget(0.05) : 시뮬레이션이 일정 수준의 에너지(움직임)를 유지
        .stop();  //stop() : 자동 시작 방지. tick()으로 수동 호출

/*
 * d3-quadtree를 이용해 효율적으로 충돌 감지
 * 각 노드의 반지름을 계산하고, 겹치는 경우 위치를 밀어내도록 조정
 * node.measured?.width 기반으로 노드의 크기를 고려한 충돌 반응 계산
 */
function collide() { ... }

//시뮬레이션 상태를 한 번 advance (한 프레임)
const tick = () => { ... }

//엣지를 force에 연결
simulation.nodes(nodes).force(
        'link',
        forceLink(edges)
                .id((d: any) => d.id)
                .strength(0.05) //강도
                .distance(100)  //간격
);

```

# Sub Flow
노드들을 구분하여 그룹핑할 수 있음
## 구현 샘플
노드들의 부모, 자식 관계를 만들어 구현할 수 있음 자식 노드들은 부모 노드의 우측 상단을 x:0, y:0의 위치로 계산되어 표현됨
### 노드
부모 자식 관계에서 자식 노드에 부모의 id를 담은 parentId 속성 설정 필요
> extent: 'parent' 속성을 추가하면 자식 노드가 부모 노드 밖으로 나오지 못 하게 함
> 부모 노드의 type: 'group'은 핸들 없는 노드를 생성하기 위해 사용되며 다른 type을 사용해도 무방

```ts
const initialNodes: Node[] = [
  {
    id: 'A',
    type: 'group',
    data: { label: null },
    position: { x: 0, y: 0 },
    style: {
      width: 170,
      height: 140,
    },
  },
  {
    id: 'B',
    type: 'input',
    data: { label: 'child node 1' },
    position: { x: 10, y: 10 },
    //부모 노드 지정
    parentId: 'A',
    //부모 노드 밖으로 나오지 못하게 함
    extent: 'parent',
  },
  {
    id: 'C',
    data: { label: 'child node 2' },
    position: { x: 10, y: 90 },
    parentId: 'A',
    extent: 'parent',
  },
];

```

# Components
Reat Flow에서 제공하는 컴포넌트


## `<Background/>`
다른 유형의 배경을 랜더링 하는 컴포넌트
### 사용법
```tsx
<ReactFlowBox>
  <ReactFlow
    ...
  >
    <Background
      id="1" //여러 Background가 존재할 경우 고유값으로 사용
      variant={BackgroundVariant.Dots} 
      gap={12} 
      size={1} 
    />
  </ReactFlow>
</ReactFlowBox>
```
![react-flow-background.png](images/react-flow-background.png)
## `<ControlButton />`
제어 패널에 버튼들을 추가는 컴포넌트
### 사용법
```tsx
<ReactFlow
  ...
>
  ...
  <Controls>
    <ControlButton onClick={() => alert('Something magical just happened. ✨')}>
      <AutoFixHighIcon /> {/*버튼 내용*/}
    </ControlButton>
  </Controls>
</ReactFlow>
```
![react-flow-control-button.png](images/react-flow-control-button.png)

## `<MiniMap />`
flow 위에 미니 맵을 출력
### 사용법
```tsx
<ReactFlow
  ...
>
  ...
  <MiniMap nodeStrokeWidth={3} />
</ReactFlow>
```

## `<Panel/>`
viewport위에 콘텐츠를 위치할때 사용되는 컴포넌트
### 사용법
```tsx
<ReactFlow
  ...
>
  /*좌측 상단에 내용 출력*/
  <Panel position="top-left">top-left</Panel>
</ReactFlow>
```
![react-flow-panel.png](images/react-flow-panel.png)

## `<ViewportPortal />`
노드 또는 엣지와 동일하게 viewport 위에 컴폰넌트를 올릴 때 사용되는 컴포넌트 (zoom-in, zoom-out 등 같이 영향을 받음)
```tsx
<ReactFlow
  ...
>
  ...
  <ViewportPortal>
    <div
            style={{ transform: 'translate(100px, 100px)', position: 'absolute' }}
    >
      This div is positioned at [100, 100] on the flow.
    </div>
  </ViewportPortal>
</ReactFlow>
```
![react-flow-viewport-portal.png](images/react-flow-viewport-portal.png)

## `<Handle />`
커스텀 노드에 연결지점을 정의하는 컴포넌트
### 사용법
```tsx
const CustomNode = (node : NodeProps<CustomNodeType>) => {
  return (
          <>
            ...
            <Handle type="target" position={Position.Left} />
            <Handle type="source" position={Position.Right} />
          </>
  );
};
```

## `<NodeResizeControl />`
노드 컴포넌트의 자식에 추가하면 크기 조절이 가능한 포인트를 생성해주는 컴포넌트

### 사용법
```tsx
const CustomNode = (node : NodeProps<CustomNodeType>) => {
  return (
          <div>
            ...
            <NodeResizeControl />
          </div>
  );
};
```
![react-flow-node-resize-control.png](images/react-flow-node-resize-control.png)

## `<NodeResizer />`
노드 컴포넌트의 자식에 추가하면 노드 주변의 모든 방향으로 크기 조절이 가능하도록 해주는 컴포넌트

### 사용법
```tsx
const CustomNode = (node : NodeProps<CustomNodeType>) => {
  return (
          <div>
            ...
            <NodeResizer minWidth={100} minHeight={30} />
          </div>
  );
};
```
![react-flow-node-resize.png](images/react-flow-node-resize.png)


## `<NodeToolbar />`
노드 주위에 툴팁이나 툴바를 랜더링하는 컴포넌트이며 viewport에 의해 크기조절 되지 않음

### 사용법
```tsx
const CustomNode = (node : NodeProps<CustomNodeType>) => {
  return (
          <div>
            <NodeToolbar isVisible position={Position.Top}>
              <button>delete</button>
              <button>copy</button>
              <button>expand</button>
            </NodeToolbar>
          </div>
  );
};
```
![react-flow-node-toolbar.png](images/react-flow-node-toolbar.png)