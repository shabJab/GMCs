package com.mvc.gmcs.simulation.energy;


public class LJ {
    public double epsilon, sigma, cutoff, cutoff2;
    double CV12, CV6, CW12, CW6;
    public double lrcC, cPE, cW;
    double V12, V6;
    public double dPE, dW;
    double rmin, rdim, K;


    public void updCutoff(double scale_factor) {
        this.cutoff = this.cutoff*scale_factor;
        this.cutoff2 = this.cutoff*this.cutoff;
        this.updateLongRangeCorrections();
    }

    public void updMin() {
        this.rmin = Math.pow(2, 1.0/6.0) * this.sigma;
    }

    public void updCurvature() {
        updMin();
        double sigr06 = Math.pow(this.sigma/this.rmin, 6);
        double sigr012 = sigr06*sigr06;
        this.K = Math.abs(4*this.epsilon*(156*sigr012 - 42*sigr06)/(this.rmin*this.rmin));
    }

    public double updDimerRange(double kT) {
        rdim = rmin + Math.pow(kT/this.K, 0.5);
        return rdim;
    }

    public void computePair(double distance2) {
        if (distance2 < this.cutoff2) {
            double LJ2 = 1 / distance2;
            double LJ6 = Math.pow(LJ2, 3);
            double LJ12 = Math.pow(LJ6, 2);
            this.V12 += LJ12;
            this.V6 += LJ6;
        }
    }

    public void finalizeChanges() {
        dPE = CV12 * V12 + CV6 * V6;
        dW = CW12 * V12 + CW6 * V6;
        this.ljFlush();
    }

    public void ljFlush() {
        this.V12=this.V6=0.0;
    }
    public void deltaFlush() {
        this.dPE=this.dW=0.0;
    }
    public void fullFlush() {
        this.ljFlush();
        this.deltaFlush();
    }

    public void setConstants() {
        // U(r) 12 and 6 term coefficients
        this.CV12 =  4.0 * this.epsilon * Math.pow(this.sigma, 12.0);
        this.CV6  = -4.0 * this.epsilon * Math.pow(this.sigma, 6.0);
        // Force f = -r^ * dU(r)/dr,  for virial pressure term (Pv = F/V)
        this.CW12 = 48.0 * this.epsilon * Math.pow(this.sigma, 12.0);
        this.CW6  =-24.0 * this.epsilon * Math.pow(this.sigma, 6.0);
    }

    public void updateLongRangeCorrections() {
        double sigrc = this.sigma/this.cutoff;
        double sigrc3  = Math.pow(sigrc, 3);
        double sigrc9by3  = Math.pow(sigrc3, 3) /3;
        // corrections for LJ potential
        this.cPE = this.lrcC * (sigrc9by3 - sigrc3);
//        this.cPE = 0.0;
        // or if to set explicitly
//        this.lrcVV12 = (8 * Math.PI * this.epsilon * Math.pow(this.sigma, 12)) / (9*Math.pow(this.cutoff, 9));
//        this.lrcVV6  = -(8 * Math.PI * this.epsilon * Math.pow(this.sigma, 6)) / (3*Math.pow(this.cutoff, 3));
        // corrections for virial pressure
        this.cW = 2*this.lrcC * (2*sigrc9by3 - sigrc3); // *rho^2 before add to Pressure
        // or if to set explicitly
//        this.lrcVW12 = (32 * Math.PI * this.epsilon * Math.pow(this.sigma, 12)) / (9 * Math.pow(this.cutoff, 9));
//        this.lrcVW6 = -(16 * Math.PI * this.epsilon * Math.pow(this.sigma, 6)) / (3 * Math.pow(this.cutoff, 3));
    }

    public LJ copy() {
        return new LJ(this.epsilon, this.sigma, this.cutoff);
    }

    public void print() {
        System.out.println(" epsilon: " + epsilon
                         + " | sigma: " + sigma
                        + " | cutoff: " + cutoff);
    }

    public LJ(double epsilon, double sigma, double cutoff) {
        this.epsilon = epsilon;
        this.sigma = sigma;
        this.cutoff = cutoff;
        this.cutoff2 = cutoff*cutoff;
        this.lrcC = 8.0 * Math.PI * epsilon * Math.pow(sigma, 3) / 3;
        this.setConstants();
        this.updateLongRangeCorrections();
        this.updCurvature();
    }
    public LJ(double epsilon, double sigma) {
        this.epsilon = epsilon;
        this.sigma = sigma;
        this.cutoff = 3 * sigma;
//        this.cutoff = 4.0;
        this.cutoff2 = cutoff*cutoff;
        this.lrcC = 8.0 * Math.PI * epsilon * Math.pow(sigma, 3) / 3;
        this.setConstants();
        this.updateLongRangeCorrections();
        this.updCurvature();
    }
}
