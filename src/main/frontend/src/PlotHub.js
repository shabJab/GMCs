import axios from "axios";
import React, {useEffect} from "react";
import Plot from "react-plotly.js";

// const getSnap = async () => {
//     await axios.get('http://localhost:8080/snap')
//     // .then(response => console.log(response))
//     // .catch(error => console.log(error));
// }
let box1color = '#1f77b4'
let box2color = '#ff7f0e'

const dens1trace = {x:[], y:[], name:'box1', line:{color:box1color}, marker:{color:box1color}};
const dens2trace = {x:[], y:[], name:'box2', line:{color:box2color}, marker:{color:box2color}};
let fluctuations_layout = {datarevision: 0,
    font: {
        family: 'Serif',
        size: 22,
        color: 'rgba(26,26,26,1)'},
    xaxis: {
        // titlefont: {size: 22,},
        title: 'MC steps',
        showticklabels: true,
    },
    yaxis: {
        // titlefont: {size: 22,},
        title: 'Density, <i>&#961;<sup>*</sup></i>',
        showticklabels: true,
    },
    width: 400, height: 400,
    margin: {l: 80, r: 10, t: 20, b: 55, pad: 0},
    showlegend: false,
};
let trace_length = 150

export function DensityFluctuationsPlot({step, dens1, dens2}) {

    useEffect(() => {
        // the case of reset
        if (fluctuations_layout.datarevision > step) {
            dens1trace.x = [];
            dens1trace.y = [];
            dens2trace.x = [];
            dens2trace.y = [];
            fluctuations_layout.datarevision = 0;
        }
        dens1trace.x.push(step);
        dens1trace.y.push(dens1);
        dens2trace.x.push(step);
        dens2trace.y.push(dens2);
        if (dens1trace.x.length >= trace_length) {
            dens1trace.x.shift();
            dens1trace.y.shift();
            dens2trace.x.shift();
            dens2trace.y.shift();
        }
        fluctuations_layout.datarevision = step;
    }, [step])

    return (
        <Plot
            data={[dens1trace, dens2trace]}
            layout={fluctuations_layout}
            revision={step}
            graphDiv="DensityFluctuationsPlot"
        />)
}

const Probability1 = {x: [], y:[], name: 'box1', type: 'bar', width: 0.01, opacity: 0.6, marker:{color:box1color}}
const Probability2 = {x: [], y:[], name: 'box2', type: 'bar', width: 0.01, opacity: 0.6, marker:{color:box2color}}
let probability_layout = {datarevision: 0,
    font: {
        family: 'Serif',
        size: 22,
        color: 'rgba(26,26,26,1)'},
    xaxis: {
        title: 'Density, <i>&#961;<sup>*</sup></i>',
        showticklabels: true,
        range: [0, 0.9],
    },
    legend: {x: 0.74, y: 0.99},
    width: 370, height: 400,
    margin: {l: 40, r: 10, t: 20, b: 55, pad: 0}};
function DensityProbabilityPlot({step, rrho, prho1, prho2}) {

    useEffect(() => {
        // the case of reset
        if (probability_layout.datarevision > step) {
            Probability1.x = [];
            Probability1.y = [];
            Probability2.x = [];
            Probability2.y = [];
            probability_layout.datarevision = 0;
        }
        Probability1.x = rrho;
        Probability1.y = prho1;
        Probability2.x = rrho;
        Probability2.y = prho2;
        probability_layout.datarevision = step;
    }, [step])

    return (<Plot
        data={[Probability1, Probability2]}
        layout={probability_layout}
        revision={step}
        graphDiv="DensityProbabilityPlot"
            />)
}

const RDF1C = {x: [], y:[], name: 'box1', line:{color:box1color}, marker:{color:box1color}}
const RDF2C = {x: [], y:[], name: 'box2', line:{color:box2color}, marker:{color:box2color}}
const RDF1S = {x: [], y:[], name: 'box1', line:{color:box1color}, marker:{color:box1color}}
const RDF2S = {x: [], y:[], name: 'box2', line:{color:box2color}, marker:{color:box2color}}
let RDFC_layout = {datarevision: 0,
    font: {
        family: 'Serif',
        size: 22,
        color: 'rgba(26,26,26,1)'
    },
    xaxis: {
        title: 'Distance, <i>r<sup>*</sup></i>',
        showticklabels: true,
    },
    yaxis: {
        title: '<i>g(r<sup>*</sup>)</i>',
        showticklabels: true,
    },
    // xaxis: {range: [0, 3]},
    legend: {x: 0.01, y: 0.99},
    showlegend: false,
    width: 370, height: 400,
    margin: {l: 80, r: 15, t: 20, b: 55, pad: 0}};
// let RDFS_layout = {datarevision: 0, xaxis: {range: [0, 3]},
//     width: 500, height: 400,
//     margin: {l: 30, r: 10, t: 20, b: 30}};
function RDFPlot({step, radius1c, rdf1c, radius2c, rdf2c,
                     radius1s, rdf1s, radius2s, rdf2s}) {
    useEffect(() => {
        // the case of reset
        if (RDFC_layout.datarevision > step) {
            RDF1C.x = [];
            RDF1C.y = [];
            RDF2C.x = [];
            RDF2C.y = [];
            RDF1S.x = [];
            RDF1S.y = [];
            RDF2S.x = [];
            RDF2S.y = [];
            RDFC_layout.datarevision = 0;
            // RDFS_layout.datarevision = 0;
        }
        RDF1C.x = radius1c;
        RDF1C.y = rdf1c;
        RDF2C.x = radius2c;
        RDF2C.y = rdf2c;
        RDF1S.x = radius1s;
        RDF1S.y = rdf1s;
        RDF2S.x = radius2s;
        RDF2S.y = rdf2s;
        RDFC_layout.datarevision = step;
        // RDFS_layout.datarevision = step;
    }, [step])

    return (
        <>
            <Plot
                data={[RDF1C, RDF2C]}
                layout={RDFC_layout}
                revision={step}
                graphDiv="RDFC"
            />
            <Plot
                data={[RDF1S, RDF2S]}
                layout={RDFC_layout}
                revision={step}
                graphDiv="RDFS"
            />
        </>
    )
}


const XYZC1 = {x: [], y:[], z:[], name: "cores", type: "scatter3d",
    mode: "markers",
    marker: {
        size: 18,
        color: '#7f7f7f',
        // line: {
        //     color: "rgba(246,202,202,0.14)",
        //     width: 0.01,
        //     // opacity: 0.05
        // },
        opacity: 0.9
    },}
const XYZC2 = {x: [], y:[], z:[], name: "cores", type: "scatter3d",
    mode: "markers",
    marker: {
        size: 18,
        color: '#7f7f7f',
        // line: {
        //     color: "rgba(246,202,202,0.14)",
        //     width: 0.01,
        //     // opacity: 0.05
        // },
        opacity: 0.9
    },}
const XYZS1 = {x: [], y:[], z:[], name: "sites", type: "scatter3d",
    mode: "markers",
    marker: {
        size: 4,
        color: '#d62728',
        // line: {
        //     // color: "rgba(217, 217, 217, 0.14)",
        //     width: 0.5
        // },
        opacity: 0.6
    },}
const XYZS2 = {x: [], y:[], z:[], name: "sites", type: "scatter3d",
    mode: "markers",
    marker: {
        size: 4,
        color: '#d62728',
        // line: {
        //     // color: "rgba(217, 217, 217, 0.14)",
        //     width: 0.5
        // },
        // opacity: 0.8
    },}
let x0 = 20;
let y0 = 212.5;
let dxy1 = 20;
let XYZ_layout = {datarevision: 0,
    font: {
        family: 'Serif',
        size: 22,
        color: 'rgba(26,26,26,1)'},
    showlegend: false,
    title:  {text: '', x: 0.5, xanchor: 'center', y: 0.98, yanchor: 'top'},
    // legend: {x: 0.65, y: 0.1},
    width: 320,
    height: 320,
    margin: {l: 0, r: 0, t: 0, b: 0, pad: 10},
    autorange: false,
    aspectmode: 'manual',
    // aspectmode: 'cube',
    scene: {
        xaxis: {
            range: [-4, 4],
            showgrid: false, // thin lines in the background,
            zeroline: false, // thick line at x=0,
            visible: false, // numbers below
        },
        yaxis: {
            range: [-4, 4],
            'showgrid': false, // thin lines in the background,
            'zeroline': false, // thick line at x=0,
            'visible': false, // numbers below
        },
        zaxis: {
            range: [-4, 4],
            'showgrid': false, // thin lines in the background,
            'zeroline': false, // thick line at x=0,
            'visible': false, // numbers below
        },
        aspectratio: {x: 1, y: 1, z: 1}
    },
    shapes: [
        {
            type: 'rect',
            xref: 'paper', xsizemode: 'pixel',
            yref: 'paper', ysizemode: 'pixel',
            x0: x0,
            y0: y0,
            x1: x0+dxy1,
            y1: y0+dxy1,
            line: {
                // color: 'rgba(128, 0, 128, 1)',
                width: 0
            },
            fillcolor: '#1f77b4'
        }
    ]
};
let XYZ_layout1 = JSON.parse(JSON.stringify(XYZ_layout));
XYZ_layout1.title.text = 'Box1'
XYZ_layout1.shapes[0].fillcolor = box1color
let XYZ_layout2 = JSON.parse(JSON.stringify(XYZ_layout));
XYZ_layout2.title.text = 'Box2'
XYZ_layout2.shapes[0].fillcolor = '#ff7f0e'
export function Scatter3D({step, cores1, sites1, cores2, sites2}) {

    useEffect(() => {
        XYZC1.x = cores1.x;
        XYZC1.y = cores1.y;
        XYZC1.z = cores1.z;
        XYZS1.x = sites1.x;
        XYZS1.y = sites1.y;
        XYZS1.z = sites1.z;
        XYZC2.x = cores2.x;
        XYZC2.y = cores2.y;
        XYZC2.z = cores2.z;
        XYZS2.x = sites2.x;
        XYZS2.y = sites2.y;
        XYZS2.z = sites2.z;
        XYZ_layout1.datarevision = step;
        XYZ_layout2.datarevision = step;
        // console.log(XYZ_layout.datarevision, "revision")
    }, [step])

    return (
        <>
            <Plot
                data={[XYZC1, XYZS1]}
                layout={XYZ_layout1}
                // revision={step}
                graphDiv="3DBox1"
            />
            <Plot
                data={[XYZC2, XYZS2]}
                layout={XYZ_layout2}
                // revision={step}
                graphDiv="3DBox2"
            />
        </>
    )
}

let autoFetchSnap;
let pullingRate = 10;

function PlotHub({pause, reset, setReset, data, setData}) {

    const fetchSnap = async () => {
        await axios.get('http://localhost:8080/snap')
            .then(response => {

                if((response.data.step===0) && reset)
                {
                    setData(response.data);
                    data = response.data;
                }
                if(response.data.step!==data.step)
                {
                    setData(response.data);
                    data = response.data;
                }
                // console.log(data.rrho);
                // console.log(data.coords1.x);
            })}

    useEffect(() => {
        if (pause) { fetchSnap(); clearInterval(autoFetchSnap); }
        else {autoFetchSnap = setInterval( fetchSnap, pullingRate)}
    }, [pause])

    useEffect(() => {
        if (reset) {
            setTimeout(fetchSnap, 500);
            setReset(false);
        }
    }, [reset])

    return(
        <>
            <DensityFluctuationsPlot
                step={data.step}
                dens1={data.dens1}
                dens2={data.dens2}
            />
            <DensityProbabilityPlot
                step={data.step}
                rrho={data.rrho}
                prho1={data.prho1}
                prho2={data.prho2}
            />
            <RDFPlot
                step={data.step}
                radius1c={data.radius1c}
                rdf1c={data.rdf1c}
                radius2c={data.radius2c}
                rdf2c={data.rdf2c}
                radius1s={data.radius1s}
                rdf1s={data.rdf1s}
                radius2s={data.radius2s}
                rdf2s={data.rdf2s}
            />

            {/*{Object.entries(data).map(([key, value],idx) => (*/}
            {/*    <p key={idx}>*/}
            {/*        <span>Key Name: {key}</span>*/}
            {/*        <span>Value: {value}</span>*/}
            {/*    </p>*/}
            {/*))}*/}
        </>
    )
}

export default PlotHub
// export default React.memo(PlotHub)