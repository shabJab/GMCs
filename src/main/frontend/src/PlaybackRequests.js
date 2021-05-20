// // import React, { useState, useEffect } from 'react';
// import axios from "axios";
//
// export function pausePatchRequest(value) {
//     // const [pauseState, setPauseState] = useState(false);
//     // let config = {
//     //     headers: {
//     //         'Accept': "application/json",
//     //         'Content-Type': "application/json",
//     //     }
//     // }
//     // axios.patch("http://localhost:8080/playback", "PAUSE=true", config)
//     // console.log(pauseState)
//     const patchPauseState = () => {
//         axios.patch("http://localhost:8080/playback", 'PAUSE=true')
//             .then(
//                 res => {
//                     console.log(res);
//                     // setPause(res.data);
//                 });
//     };
//     patchPauseState()
//     // useEffect(() => {
//     //     patchPauseState();
//     // },);
//     // return (pauseState);
// }