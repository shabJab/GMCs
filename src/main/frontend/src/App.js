import React, {useState} from "react";
import './App.css';
import {ControlPanel} from "./Controls";
import PlotHub, { Scatter3D } from "./PlotHub";
import {number} from "prop-types";
import Display from "./Display";


function App() {
    const [pause, setPause] = useState(true);
    const [reset, setReset] = useState(false);
    // const [data, setData] = useState({dens1: 0.3, dens2: 0.5, step: 0});

    // let initData = axios.get('http://localhost:8080/snap')
    //     .then(response => response.data);
    // const [data, setData] = useState(initData);
    const [data, setData] = useState(
        {step: number, dens1: number, dens2: number,
            rrho: [], prho1: [], prho2: [],
            radius1: [], rdf1: [], radius2: [], rdf2: [],
            cores1: {x:[],y:[],z:[]}, sites1: {x:[],y:[],z:[]},
            cores2: {x:[],y:[],z:[]}, sites2: {x:[],y:[],z:[]},
            cumulatives: {
                AVPEN1: number, AVPEN2: number, FLPEN1: number, FLPEN2: number,
                AVN1: number, AVN2: number, FLN1: number, FLN2: number,
                AVV1: number, AVV2: number, FLV1: number, FLV2: number,
                AVD1: number, AVD2: number, FLD1: number, FLD2: number,
                AVP1: number, AVP2: number, FLP1: number, FLP2: number,
                AVCP1: number, AVCP2: number, FLCP1: number, FLCP2: number
            },
            ratios: {
                disp1: number, disp2: number, vol: number,
                ex1: number, ex2: number, virt1: number, virt2: number
            }
        });

  return (
    <div className="App">
        <ControlPanel
            pause={pause} setPause={setPause}
            reset={reset} setReset={setReset}
        />
        <Scatter3D
            step={data.step}
            cores1={data.cores1}
            sites1={data.sites1}
            cores2={data.cores2}
            sites2={data.sites2}
        />
        <Display
            cumulatives={data.cumulatives}
            ratios={data.ratios}
        />
        <br/>
        <PlotHub pause={pause}
                 reset={reset} setReset={setReset}
                 data={data} setData={setData}/>
        {/*{Object.entries(data).map(([key, value],idx) => (*/}
        {/*    <p key={idx}>*/}
        {/*        <span>Key Name: {key}</span>*/}
        {/*        <span>Value: {value}</span>*/}
        {/*    </p>*/}
        {/*))}*/}
      {/*<h1>{pause.toString()}  |  {reset.toString()}</h1>*/}
    </div>
  );
}

export default App;
