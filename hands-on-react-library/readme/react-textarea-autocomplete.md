# react-textarea-autocomplete
textarea의 자동완성을 지원하는 컴포넌트 제공

# 설치
```shell
npm i @webscopeio/react-textarea-autocomplete
npm i @types/webscopeio__react-textarea-autocomplete
```

# Props
명시되지 않은 props는 textarea로 전달됨

| Prop 이름                                                                                                     | 타입                                        | 설명                                                |
| ----------------------------------------------------------------------------------------------------------- | ----------------------------------------- | ------------------------------------------------- |
| **trigger**\*                                                                                               | `Object`                                  | 트리거 문자(`@`, `#` 등)와 그에 따른 동작 정의                   |
| **loadingComponent**\*                                                                                      | `React Component`                         | 데이터 로딩 중 표시할 컴포넌트 (props로 데이터를 전달받음)              |
| `innerRef`                                                                                                  | `(textarea: HTMLTextAreaElement) => void` | 내부 `<textarea>`에 접근할 수 있는 ref                     |
| `scrollToItem`                                                                                              | `boolean` 또는 `(container, item) => void`  | 자동 스크롤 여부. 기본값은 `true`                            |
| `minChar`                                                                                                   | `number`                                  | 제안이 뜨기 위한 최소 입력 문자 수. 기본값 `1`                     |
| `onCaretPositionChange`                                                                                     | `(caretPosition: number) => void`         | 캐럿 위치가 바뀔 때 호출됨                                   |
| `movePopupAsYouType`                                                                                        | `boolean`                                 | `true`면 입력할 때 캐럿 따라 팝업 위치 이동. 기본값 `false`         |
| `boundariesElement`                                                                                         | `string` 또는 `HTMLElement`                 | 오버플로우 방지용 요소 지정. 기본값은 `"body"`                    |
| `textAreaComponent`                                                                                         | `React.Component` 또는 `{ component, ref }` | 커스텀 textarea 사용 가능 (예: `react-autosize-textarea`) |
| `renderToBody`                                                                                              | `boolean`                                 | `true`면 `<body>` 끝에 렌더링. 기본값 `false`              |
| `onItemHighlighted`                                                                                         | `({ currentTrigger, item }) => void`      | 리스트에서 항목 하이라이트 시 호출                               |
| `onItemSelected`                                                                                            | `({ currentTrigger, item }) => void`      | 항목 선택 시 호출                                        |
| `style`, `listStyle`, `itemStyle`, `loaderStyle`, `containerStyle`, `dropdownStyle`                         | `Style Object`                            | 각각의 요소 스타일 지정                                     |
| `className`, `containerClassName`, `listClassName`, `itemClassName`, `loaderClassName`, `dropdownClassName` | `string`                                  | 각각의 요소에 적용할 클래스 이름                                |

# Methods
ref를 통해 호출 가능

| Method                                                   | 설명                 |
| -------------------------------------------------------- | ------------------ |
| `getCaretPosition(): number`                             | 현재 캐럿 위치 반환        |
| `setCaretPosition(position: number): void`               | 캐럿을 해당 위치로 이동      |
| `getSelectionPosition(): {selectionStart, selectionEnd}` | 현재 선택 영역의 시작과 끝 반환 |
| `getSelectedText(): ?string`                             | 현재 선택된 텍스트 반환      |
