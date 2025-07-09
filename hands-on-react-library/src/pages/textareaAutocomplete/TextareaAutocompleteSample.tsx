import React, { useEffect, useRef } from 'react';
import ReactTextareaAutocomplete from '@webscopeio/react-textarea-autocomplete';


// 로딩 중 표시할 컴포넌트
const Loading = ({ data }: any) => <div style={{ padding: 10 }}>Loading...</div>;

const trigger = {
  "@": {
    // 사용자가 @를 입력했을 때 호출되는 데이터 공급 함수
    // token은 @ 이후에 사용자가 입력한 문자열
    dataProvider: async (token: string) => {
      return [
        { name: "Alice" },
        { name: "Bob" },
        { name: "Charlie" },
      ]
        // 입력한 문자열이 포함된 이름만 필터링하여 반환
        .filter((item) => item.name.toLowerCase().includes(token.toLowerCase()));
    },

    // 드롭다운 리스트에서 각 항목을 어떻게 렌더링할지 정의
    component: ({ entity }: { entity: any }) => <div>{entity.name}</div>,

    // 항목을 선택했을 때 텍스트에 삽입될 최종 문자열 반환
    output: (item: any) => `@${item.name}`,
  },
};


const TextareaAutocompleteSample = () => {
  const rtaRef = useRef<any>(null); // ref를 통해 메서드 접근

  useEffect(() => {
    if (rtaRef.current) {
      // caret 위치 가져오기
      const caretPos = rtaRef.current.getCaretPosition();
      console.log("Initial caret position:", caretPos);

      // caret 위치 설정 (예: 5번째 위치로 이동)
      rtaRef.current.setCaretPosition(5);

      // 선택된 텍스트 가져오기
      console.log("Selected text:", rtaRef.current.getSelectedText());

      // 선택 영역 정보
      console.log("Selection info:", rtaRef.current.getSelectionPosition());
    }
  }, []);

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
        // 필수: 트리거 설정
        trigger={trigger}

        // 필수: 로딩 중 컴포넌트
        loadingComponent={Loading}

        // textarea에 접근 가능한 ref 전달
        innerRef={(ref) => console.log("Textarea ref:", ref)}

        // 캐럿 위치가 바뀔 때마다 콜백
        onCaretPositionChange={(pos) => console.log("Caret moved to:", pos)}

        // 항목이 하이라이트 될 때마다 호출
        onItemHighlighted={({ currentTrigger, item }) =>
          console.log("Item highlighted:", item, "Trigger:", currentTrigger)
        }

        // 항목이 선택될 때마다 호출
        onItemSelected={({ currentTrigger, item }) =>
          console.log("Item selected:", item, "Trigger:", currentTrigger)
        }

        // 제안이 뜨기 위한 최소 문자 수 (기본값은 1)
        minChar={1}

        // 스크롤 이동 설정: 기본 true, 함수로 커스터마이징 가능
        scrollToItem={(container, item) => {
          item.scrollIntoView({ block: "nearest" });
        }}

        // caret을 따라 팝업 위치 이동 여부
        movePopupAsYouType={true}

        // 팝업이 넘어가지 않도록 제한할 요소 (기본: body)
        boundariesElement="body"

        // 팝업을 body에 렌더링할지 여부
        renderToBody={false}

        // 스타일 커스터마이징
        style={{ padding: 10, fontSize: 14 }}
        listStyle={{ backgroundColor: "#f0f0f0" }}
        itemStyle={{ padding: 8, cursor: "pointer" }}
        loaderStyle={{ padding: 10, fontStyle: "italic" }}
        containerStyle={{ border: "1px solid #ccc", borderRadius: 4 }}
        dropdownStyle={{ boxShadow: "0 2px 8px rgba(0,0,0,0.15)" }}

        // 클래스 네임 커스터마이징
        className="rta-textarea"
        containerClassName="rta-container"
        listClassName="rta-list"
        itemClassName="rta-item"
        loaderClassName="rta-loader"
        dropdownClassName="rta-dropdown"

        // ref로 메서드 접근을 위해 전달
        ref={rtaRef}
      />

    </div>
  );
}

export default TextareaAutocompleteSample;