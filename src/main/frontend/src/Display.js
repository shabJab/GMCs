import Plot from "react-plotly.js";

export default function Display({cumulatives, ratios}) {

    // useEffect(() => {
    //
    // }, [step])

    return (
        <div style={{display: 'inline-block'}}>
            <fieldset style={{width: 70, display: 'inline-block'}}>
                <label>N<sub>1</sub>:{parseFloat(cumulatives.AVN1).toFixed(2)}</label>
                <br/>
                <label>N<sub>2</sub>:{parseFloat(cumulatives.AVN2).toFixed(2)}</label>
                <br/>
                <label>V<sub>1</sub>:{parseFloat(cumulatives.AVV1).toFixed(2)}</label>
                <br/>
                <label>V<sub>2</sub>:{parseFloat(cumulatives.AVV2).toFixed(2)}</label>
                <br/>
                <label>ρ<sub>1</sub>:{parseFloat(cumulatives.AVD1).toFixed(2)}</label>
                <br/>
                <label>ρ<sub>2</sub>:{parseFloat(cumulatives.AVD2).toFixed(2)}</label>
            </fieldset>
            {/*<fieldset style={{width: 90, display: 'inline-block'}}>*/}

            {/*</fieldset>*/}
            <fieldset style={{width: 70, display: 'inline-block'}}>
                <label>U<sub>1</sub>:{parseFloat(cumulatives.AVPEN1).toFixed(2)}</label>
                <br/>
                <label>U<sub>2</sub>:{parseFloat(cumulatives.AVPEN2).toFixed(2)}</label>
                <br/>
                <label>{'\u03BC'}<sub>1</sub>:{parseFloat(cumulatives.AVCP1).toFixed(2)}</label>
                <br/>
                <label>{'\u03BC'}<sub>2</sub>:{parseFloat(cumulatives.AVCP2).toFixed(2)}</label>
                <br/>
                <label>P<sub>1</sub>:{parseFloat(cumulatives.AVP1).toFixed(2)}</label>
                <br/>
                <label>P<sub>2</sub>:{parseFloat(cumulatives.AVP2).toFixed(2)}</label>
                <br/>
            </fieldset>
            {/*<fieldset style={{width: 90, display: 'inline-block'}}>*/}

            {/*</fieldset>*/}
            <fieldset style={{width: 90, display: 'inline-block'}}>
                <label>accepted_disp<sub>1</sub>:{parseFloat(ratios.disp1).toFixed(2)}</label>
                <br/>
                <label>accepted_disp<sub>2</sub>:{parseFloat(ratios.disp2).toFixed(2)}</label>
                <br/>
                <label>accepted_vol_change:{parseFloat(ratios.vol).toFixed(2)}</label>
                <br/>
                <label>accepted_exchanges<sub>1</sub>:{parseFloat(ratios.ex1).toFixed(2)}</label>
                <br/>
                <label>accepted_exchanges<sub>2</sub>:{parseFloat(ratios.ex2).toFixed(2)}</label>
                <br/>
                <label>virtual_exchanges<sub>1</sub>:{parseFloat(ratios.virt1).toFixed(2)}</label>
                <br/>
                <label>virtual_exchanges<sub>2</sub>:{parseFloat(ratios.virt2).toFixed(2)}</label>
                <br/>
            </fieldset>
        </div>
    )
}