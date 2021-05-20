import React, { useState, useEffect } from 'react';
import axios from "axios";

export default function Snap() {
    const [snap, setSnap] = useState({});
    const fetchSnap = () => {
        axios.get("http://localhost:8080/snap")
            .then( res => {
                console.log(res);
                setSnap(res.data);
            });
      };
    useEffect(() => {
        // fetchSnap();
        const t = setInterval(fetchSnap, 2500);
        return () => clearInterval(t); // clear
    }, );
    // return snap.map((snap, index) => {
    return (
        <div>
            {Object.entries(snap).map(([key, value],idx) => (
                <p key={idx}>
                    <span>Key Name: {key}</span>
                    <span>Value: {value.toString()}</span>
                </p>
            ))}
            <br/>
            <h1>{snap.dens1}</h1>
            <h1>{snap.dens2}</h1>
            <br/>
        </div>
    )
}

export function NextSnap() {
    return axios.get("http://localhost:8080/snap")
        .then( res => res.data)
        //     console.log(res);
        //     setSnap(res.data);
        //     return res.data;
        // })
    // useEffect(() => {
    //     // fetchSnap();
    //     const t = setInterval(fetchSnap, 2500);
    //     return () => clearInterval(t); // clear
    // }, );
    // // return snap.map((snap, index) => {
    // return (
    //     <div>
    //         {Object.entries(snap).map(([key, value],idx) => (
    //             <p key={idx}>
    //                 <span>Key Name: {key}</span>
    //                 <span>Value: {value.toString()}</span>
    //             </p>
    //         ))}
    //         <br/>
    //         <h1>{snap.dens1}</h1>
    //         <h1>{snap.dens2}</h1>
    //         <br/>
    //     </div>
    // )
}
