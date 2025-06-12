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