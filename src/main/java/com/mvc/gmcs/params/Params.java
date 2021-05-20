package com.mvc.gmcs.params;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Params {
    private int N1, N2;
    private double D1, D2, kT;

    public Params(@JsonProperty("N1") int N1,
                  @JsonProperty("N2") int N2,
                  @JsonProperty("D1") double D1,
                  @JsonProperty("D2") double D2,
                  @JsonProperty("kT") double kT) {
        this.N1 = N1;
        this.N2 = N2;
        this.D1 = D1;
        this.D2 = D2;
        this.kT = kT;
    }

    public int getN1() {
        return N1;
    }

    public void setN1(int n1) {
        N1 = n1;
    }

    public int getN2() {
        return N2;
    }

    public void setN2(int n2) {
        N2 = n2;
    }

    public double getD1() {
        return D1;
    }

    public void setD1(double d1) {
        D1 = d1;
    }

    public double getD2() {
        return D2;
    }

    public void setD2(double d2) {
        D2 = d2;
    }

    public double getkT() {
        return kT;
    }

    public void setkT(double kT) {
        this.kT = kT;
    }
}
