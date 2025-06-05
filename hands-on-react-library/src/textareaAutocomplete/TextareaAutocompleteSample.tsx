import React, { useRef } from "react";
import ReactTextareaAutocomplete from "@webscopeio/react-textarea-autocomplete";

const TextareaAutocompleteSample = () => {
  const rtaRef = useRef<ReactTextareaAutocomplete<any> | null>(null);

  // caret 위치 변경 이벤트
  const onCaretPositionChange = (position: number): void => {
    console.log(`Caret position is equal to ${position}`);
  };

  // caret 위치 초기화
  const resetCaretPosition = (): void => {
    rtaRef.current?.setCaretPosition(0);
  };

  // caret 현재 위치 출력
  const printCurrentCaretPosition = (): void => {
    const caretPosition = rtaRef.current?.getCaretPosition();
    if (typeof caretPosition === "number") {
      console.log(`Caret position is equal to ${caretPosition}`);
    }
  };

  return (
    <div className="app">
      <div className="controls">
        <button onClick={resetCaretPosition}>Reset caret position</button>
        <button onClick={printCurrentCaretPosition}>Print current caret position to the console</button>
      </div>
      <ReactTextareaAutocomplete
        className="my-textarea"
        loadingComponent={() => <span>Loading</span>}
        trigger={{
          // 예시 트리거: @username
          "@": {
            dataProvider: async (token: string) => [
              { name: "alice" },
              { name: "bob" },
            ],
            component: ({ entity }: { entity: any }) => <div>{entity.name}</div>,
            output: (item: any) => `@${item.name}`,
          },
        }}
        ref={rtaRef}
        onCaretPositionChange={onCaretPositionChange}
      />
    </div>
  );
}

export default TextareaAutocompleteSample;