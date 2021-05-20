package com.mvc.gmcs.simulation;


public class BoxBuilder {
    private String _cell_type="FCC";
    private int _N=32;
    private double _Dens=0.3225;

    public BoxBuilder() { }

    public Box build() {
        return new Box(_cell_type, _N, _Dens);
    }

    public BoxBuilder unitCell(String _cell_type) {
        this._cell_type = _cell_type;
        return this;
    }

    public BoxBuilder numberMolecules(int _N) {
        this._N = _N;
        return this;
    }

    public BoxBuilder numberDensity(double _Dens) {
        this._Dens = _Dens;
        return this;
    }
}
