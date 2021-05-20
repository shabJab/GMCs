import React from 'react';
import Plot from 'react-plotly.js';
import {NextSnap} from "./Snap";
import axios from "axios";

// const timer = () => {
//     // setInterval for every second
//     const countdown = setInterval(() => {
//         // if allowed time is used up, clear interval
//         if (secondsLeft < 0) {
//             clearInterval(countdown)
//             return;
//         }
//         // if paused, record elapsed time and return
//         if (pause === true) {
//             elapsed = secondsLeft;
//             return;
//         }
//         // decrement seconds left
//         secondsLeft--;
//         displayTimeLeft(secondsLeft)
//     }, 1000)
// }
// timer();

// var Timer = function(callback, delay) {
//     var timerId, start, remaining = delay;
//
//     this.pause = function() {
//         window.clearTimeout(timerId);
//         remaining -= Date.now() - start;
//     };
//
//     this.resume = function() {
//         start = Date.now();
//         window.clearTimeout(timerId);
//         timerId = window.setTimeout(callback, remaining);
//     };
//
//     this.resume();
// };
// var timer = new Timer(function() {
//     alert("Done!");
// }, 1000);


class PlotEx extends React.Component {
    state = {
        data: {
            dens1: 0,
            dens2: 0,
            poop: []
        },
        line1: {
            x: [0],
            y: [1],
            name: 'Line 1'
        },
        line2: {
            x: [0],
            y: [1],
            name: 'Line 2'
        },
        timer: 0,
        layout: {
            datarevision: 0,
        },
        revision: 0,
    }
    // setData = (data) => {
    //     this.data = data
    // }

    componentDidMount() {
        this.timer = setInterval(this.increaseGraphic, 1000);
        // return () => clearInterval(t); // clear
        // setInterval(this.increaseGraphic, 1000);
    }

    componentWillUnmount() {
    }

    rand = () => parseInt(Math.random() * 10 + this.state.revision, 10);
    increaseGraphic = () => {
        // if (true) {clearInterval(this.timer);}
        // const snap = NextSnap();
        axios.get("http://localhost:8080/snap")
            .then(response => {
                // console.log(response.data);
                // this.data = response.data;
                this.setState({data: response.data})
            })

        const { line1, line2, layout , data } = this.state;
        console.log(data);
        // line1.x.push(data.dens1);
        // line1.y.push(1);

        line1.x.push(this.rand());
        line1.y.push(this.rand());
        if (line1.x.length >= 10) {
            line1.x.shift();
            line1.y.shift();
        }
        line2.x.push(this.rand());
        line2.y.push(this.rand());
        // line2.x.push(data.dens2);
        // line2.y.push(1);
        if (line2.x.length >= 10) {
            line2.x.shift();
            line2.y.shift();
        }
        this.setState({ revision: this.state.revision + 1 });
        layout.datarevision = this.state.revision + 1;
    }
    render() {
        return (<div>
            <Plot
                data={[
                    this.state.line1,
                    this.state.line2,
                ]}
                layout={this.state.layout}
                revision={this.state.revision}
                graphDiv="graph"
            />
        </div>);
    }
}

export default React.memo(PlotEx)