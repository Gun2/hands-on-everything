import React from 'react';
import {ForceGraph2D} from "react-force-graph";

export const Base2D = () => {
    let graphData = {
        nodes: [
            {
                id: "id1",
            },
            {
                id: "id2",
            },
        ],
        links: [
            {
                source: "id1",
                target: "id2",
            }
        ]
    };
    return (
        <div>
            <ForceGraph2D
                graphData={graphData}
                //node 사이즈 (기본4)
                nodeRelSize={10}
                //노드 표시 유무 (기본 true)
                nodeVisibility={true}
                //link 대시
                linkLineDash={[3,1,3]}
                //link 넓이
                linkWidth={3}
                //link 굴곡
                linkCurvature={1}
                //link 화살표
                linkDirectionalArrowLength={10}
                //link 화살표 생상
                linkDirectionalArrowColor={obj => "#f00"}
            />
        </div>
    );
};