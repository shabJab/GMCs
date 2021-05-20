import React, {useEffect} from 'react';
import axios from "axios";


const patchSpringPlayback = async (field, value) => {
    await axios.patch('http://localhost:8080/playback', `${field}=${value}`)
        // .then(response => console.log(response))
        // .catch(error => console.log(error));
}

export function PauseButton({boolState, setBoolState}) {

    const handleClick = () => {
        patchSpringPlayback('PAUSE',!boolState);
        setBoolState(!boolState);
    }

    useEffect(() => {
        axios.get('http://localhost:8080/playback')
            .then(response => {
                // console.log(response);
                setBoolState(response.data.pause);
            });
    }, []);

    return (
        <button
            value={boolState}
            onClick={handleClick}
        >
            {boolState ? 'Play' : 'Pause'}
        </button>
    );
}

const sendNewParams = async (N1,N2,D1,D2,kT) => {
    await axios.put('http://localhost:8080/params',
        {N1:N1,N2:N2,D1:D1,D2:D2,kT:kT},
        {headers: {
        'Content-Type': 'application/json'}})}

export function ResetButton({boolState, setBoolState, setPause}) {

    const handleClick = () => {
        patchSpringPlayback('RESET','true');
        setBoolState(!boolState);
        // boolState = true;
        setPause(true);
        // setBoolState(boolState);
        // boolState = false;
        // setTimeout(setBoolState(boolState), 100)
        sendNewParams(
            document.getElementById('N1').value,
            document.getElementById('N2').value,
            document.getElementById('D1').value,
            document.getElementById('D2').value,
            document.getElementById('kT').value,
            )
        // console.log(document.getElementById('N1').value )
    }

    // const fetchOnReset = () => {
    //     axios.get('http://localhost:8080/snap')
    //         .then(response => {setData(response.data);})
    // }
    // useEffect(() => {
    //     if (boolState) {
    //         setTimeout(fetchOnReset, 5000)
    //         setBoolState(false);
    //     }
    // }, [boolState])

    return (
        <button
            key={'reset'}
            value={boolState}
            onClick={handleClick}
        >
                {'Re/Set'}
        </button>
    );
}

export function EquilibrateButton() {

    const handleClick = () => {
        patchSpringPlayback('EQUILIBRATE','true');
    }

    return (
        <button
            key={'equilibrate'}
            // value={boolState}
            onClick={handleClick}
        >
            {'Equilibrate'}
        </button>
    );
}


export function ControlPanel({pause, setPause, reset, setReset}) {

    return (
        <fieldset className={'controlPanel'}>
            N<sub>1</sub><input id={'N1'} defaultValue={100} disabled={!pause}/>
            <br/>
            N<sub>2</sub><input id={'N2'} defaultValue={100} disabled={!pause}/>
            <br/>
            ρ<sub>1</sub><input id={'D1'} defaultValue={0.3} disabled={!pause}/>
            <br/>
            ρ<sub>2</sub><input id={'D2'} defaultValue={0.3} disabled={!pause}/>
            <br/>
            kT<input id={'kT'} defaultValue={1.2} disabled={!pause}/>
            <br/>
            <EquilibrateButton/>
            <ResetButton
                boolState={reset}
                setBoolState={setReset}
                setPause={setPause}
            />
            <PauseButton
                boolState={pause}
                setBoolState={setPause}
            />
            {/*<div className='rows'>*/}
            {/*    <div className='row'>row1</div>*/}
            {/*    <div className='row'>row2</div>*/}
            {/*    <div className='row'>row3</div>*/}
            {/*</div>*/}
        </fieldset>)
}
