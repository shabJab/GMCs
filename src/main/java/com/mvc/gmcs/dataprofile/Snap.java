package com.mvc.gmcs.dataprofile;

import java.util.*;


public class Snap {

    private int step;
    private double dens1;
    private double dens2;

    private double[] rrho;
    private double[] prho1;
    private double[] prho2;

    private double[] radius1c;
    private double[] rdf1c;
    private double[] radius2c;
    private double[] rdf2c;
    private double[] radius1s;
    private double[] rdf1s;
    private double[] radius2s;
    private double[] rdf2s;

    public Map<String, List<Double>> cores1;
    public Map<String, List<Double>> sites1;
    public Map<String, List<Double>> cores2;
    public Map<String, List<Double>> sites2;
    public Map<String, Double> cumulatives; // averages, std devs, acceptance ratios, scatter plots
    public Map<String, Double> ratios;

    public Snap() {
        this.step = 0;
        this.dens1 = 0;
        this.dens2 = 0;

        this.rrho = new double[100];
        this.prho1 = new double[100];
        this.prho2 = new double[100];

        this.radius1c = new double[100];
        this.radius2c = new double[100];
        this.rdf1c = new double[100];
        this.rdf2c = new double[100];
        this.radius1s = new double[100];
        this.radius2s = new double[100];
        this.rdf1s = new double[100];
        this.rdf2s = new double[100];

        this.cores1 = new HashMap<>();
        this.sites1 = new HashMap<>();
        this.cores2 = new HashMap<>();
        this.sites2 = new HashMap<>();
        this.cumulatives = new HashMap<>();
        this.ratios = new HashMap<>();
//        this.rrho2 = new double[100];
    }

//    public Snap(int step, double dens1, double dens2,
//                // density probability
//                double[] rrho, double[] Prho1, double[] Prho2,
//                // radial distribution function
//                double[] radius1, double[] rdf1, double[] radius2, double[] rdf2) {
////        this.poop = 343;
////        this.poop.add(343.02);
////        this.poop.add(3.235);
////        this.kT = 1;
//        this.step = step;
//        this.dens1 = dens1;
//        this.dens2 = dens2;
//        this.rrho = rrho;
//        this.prho1 = Prho1;
//        this.prho2 = Prho2;
//        this.radius1 = radius1;
//        this.rdf1 = rdf1;
//        this.radius2 = radius2;
//        this.rdf2 = rdf2;
//    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public double getDens1() {
        return dens1;
    }

    public void setDens1(double dens1) {
        this.dens1 = dens1;
    }

    public double getDens2() {
        return dens2;
    }

    public void setDens2(double dens2) {
        this.dens2 = dens2;
    }

    public double[] getPrho1() {
        return prho1;
    }

    public void setPrho1(double[] prho1) {
        this.prho1 = prho1;
    }

    public double[] getRrho() {
        return rrho;
    }

    public void setRrho(double[] rrho) {
        this.rrho = rrho;
    }

    public double[] getPrho2() {
        return prho2;
    }

    public void setPrho2(double[] prho2) {
        this.prho2 = prho2;
    }

    public double[] getRadius1c() {
        return radius1c;
    }

    public void setRadius1c(double[] radius1c) {
        this.radius1c = radius1c;
    }

    public double[] getRdf1c() {
        return rdf1c;
    }

    public void setRdf1c(double[] rdf1c) {
        this.rdf1c = rdf1c;
    }

    public double[] getRadius2c() {
        return radius2c;
    }

    public void setRadius2c(double[] radius2c) {
        this.radius2c = radius2c;
    }

    public double[] getRdf2c() {
        return rdf2c;
    }

    public void setRdf2c(double[] rdf2c) {
        this.rdf2c = rdf2c;
    }

    public double[] getRadius1s() {
        return radius1s;
    }

    public void setRadius1s(double[] radius1s) {
        this.radius1s = radius1s;
    }

    public double[] getRdf1s() {
        return rdf1s;
    }

    public void setRdf1s(double[] rdf1s) {
        this.rdf1s = rdf1s;
    }

    public double[] getRadius2s() {
        return radius2s;
    }

    public void setRadius2s(double[] radius2s) {
        this.radius2s = radius2s;
    }

    public double[] getRdf2s() {
        return rdf2s;
    }

    public void setRdf2s(double[] rdf2s) {
        this.rdf2s = rdf2s;
    }

    public Map<String, List<Double>> getCores1() {
        return cores1;
    }

    public void setCores1(Map<String, List<Double>> cores1) {
        this.cores1 = cores1;
    }

    public Map<String, List<Double>> getSites1() {
        return sites1;
    }

    public void setSites1(Map<String, List<Double>> sites1) {
        this.sites1 = sites1;
    }

    public Map<String, List<Double>> getCores2() {
        return cores2;
    }

    public void setCores2(Map<String, List<Double>> cores2) {
        this.cores2 = cores2;
    }

    public Map<String, List<Double>> getSites2() {
        return sites2;
    }

    public void setSites2(Map<String, List<Double>> sites2) {
        this.sites2 = sites2;
    }

    public Map<String, Double> getCumulatives() {
        return cumulatives;
    }

    public void setCumulatives(Map<String, Double> cumulatives) {
        this.cumulatives = cumulatives;
    }

    public Map<String, Double> getRatios() {
        return ratios;
    }

    public void setRatios(Map<String, Double> ratios) {
        this.ratios = ratios;
    }
}
