import React from 'react';
import {ForceGraph2D} from "react-force-graph";

export const NodeCustom2D = () => {
    let graphData = {
        nodes: [
            {
                id: "id1",
                name: "node1"
            },
            {
                id: "id2",
                name: "node2"
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
                nodeCanvasObject={(node, ctx, globalScale) => {
                    const label = node.name;
                    const fontSize = 12 / globalScale;

                    ctx.font = `${fontSize}px Sans-Serif`;
                    ctx.textAlign = 'center';
                    ctx.textBaseline = 'middle';
                    // Node circle
                    ctx.beginPath();
                    ctx.arc(node?.x ?? 0, node?.y ?? 0, node.val, 0, 2 * Math.PI, false);
                    ctx.fillStyle = 'rgba(0, 255, 0, 0.6)';
                    ctx.fill();

                    // Node label
                    ctx.fillStyle = 'black';
                    ctx.fillText(label, node?.x ?? 0, node?.y ?? 0);
                }}
            />
        </div>
    );
};