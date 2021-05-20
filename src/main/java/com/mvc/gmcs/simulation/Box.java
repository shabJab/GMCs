package com.mvc.gmcs.simulation;

import com.mvc.gmcs.simulation.energy.LJ;
import com.mvc.gmcs.simulation.energy.Potential;
import com.mvc.gmcs.simulation.primitives.Quaternion;
import com.mvc.gmcs.simulation.primitives.XYZ;
import com.mvc.gmcs.simulation.structures.Molecule;
import com.mvc.gmcs.simulation.structures.Particle;

import java.util.ArrayList;
import java.util.List;

public class Box {
    public int N;
    public double Dens;
    public double Vol;
    public double Pres;
    public double kT, beta;

    // unit cell parameters
    String cell_type;
    int cell_N;
    // Number of UnitCells Per space Direction
    int nucpd;
    double box_side;
    double half_box_side;
    double cell_side;
    List<XYZ> cell = new ArrayList<>();
    public List<Molecule> molecules = new ArrayList<Molecule>();
    Potential potential;
    // energy and virial
    public double dPE, dVir;
    double cPE, cVir;
    public double PE;
    double Vir;
    public double PEN; // energy per particle

    // displacement steps
    double drmax; // maximum displacement for particle positions
    double adjust_drmax=1000; // stride for adjusting maximum displacement
    int attempted_displacements, accepted_displacements; // counters for accepted / total displacement steps
    public double ratio_disp; // acceptance rate of new particle positions

    // aux volume changes fields
    double new_Vol, inv_scale_factor, scale_factor;
    // exchange steps stats
    double Rmin2 = 0.80*0.80; // minimal no-overlap distance squared
//    double Rmin2 = 1;
    boolean overlap;
    int attempted_inserts, allowed_inserts, accepted_inserts;
    int removals;
    public double ratio_exchange, ratio_virtual;

    // widoms chemical potential accumulator
    double dCP; // Chemical Potential change due to particle exchange
    double ChemPot;
    int nCP; // counter for Chemical Potential average
    double ACChemPot, ACChemPot2; // accumulator and squared value
    public double AVChemPot; // average Chemical Potential
    public double FLChemPot; // standard deviation

    // other box averages
    public double AVPEN, AVDens, AVN, AVPres, AVVol;
    // std deviations
    double vU, vN, vRho, vP, vV;
    public double FLPEN, FLN, FLDens, FLPres, FLVol;

    // ----------- Probability Distributions
    int nupDens; // density counter for probability distribution
    int nrho = 100; // number of bins for density probability distribution
    double delrDPD = 0.9/nrho; // hist step/binsize
    double[] acrho = new double[nrho];
    public double[] rrho = new double[nrho];
    public double[] Prho = new double[nrho];
    // ----------- Radial Distribution Function
//    double rcut=4, AVrcut=4; // cutoff characteristic for RDF
    double rcutCC, AVrcutCC, rcutSS, AVrcutSS;
    int nRDF = 100; // number of bins for Radial Distribution Functions
    public double[] grCC = new double[nRDF]; // accumulator for number of particles per bin of RDF
    public double[] RDFCC = new double[nRDF]; // RDF array
    public double[] RADIUSCC = new double[nRDF]; // Radial distances array
    public double[] grSS = new double[nRDF]; // accumulator for number of particles per bin of RDF
    public double[] RDFSS = new double[nRDF]; // RDF array
    public double[] RADIUSSS = new double[nRDF]; // Radial distances array



    // [A] INITIALIZATION STAGE
    // Box initialization functions
    public void init() {
        this.Vol = this.N/this.Dens;
        if (this.cell_type.equals("FCC")) {this.cell_N=4;}
        // nucpd - Number of UnitCells Per space Direction
        this.nucpd = (int) Math.ceil(Math.pow((double) this.N/this.cell_N, 1.0/3.0));
        this.box_side = Math.pow(this.Vol, 1.0/3.0);
        this.half_box_side = this.box_side/2;
        this.cell_side = this.box_side/this.nucpd;
        this.drmax = this.box_side*0.02;
    }
    public void createCell() {
        if (this.cell_type.equals("FCC")) {
            this.cell.add(new XYZ(0.0, 0.0, 0.0));
            this.cell.add(new XYZ(0.5, 0.5, 0.0));
            this.cell.add(new XYZ(0.0, 0.5, 0.5));
            this.cell.add(new XYZ(0.5, 0.0, 0.5));
        }
    }
    public void translateCell() {
        int i=0;
        for (int lx=1; lx<=this.nucpd; lx++)
            for (int ly=1; ly<=this.nucpd; ly++)
                for (int lz=1; lz<=this.nucpd; lz++)
                    for (XYZ center : this.cell)
                        if (i<this.N) {
                            center = center.plus(lx,ly,lz);
                            center.minusIP(-0.75);
                            center.timesIP(cell_side);
                            center.minusIP(half_box_side);
                            this.molecules.add(new Molecule("Monomer", center));
                            boundary(this.molecules.get(i));
                            i++;
                        }
    }
    // ========= MAIN CONSTRUCTOR =========
    public Box(String cell_type, int N, double Dens) {
        this.cell_type = cell_type;
        this.N = N;
        this.Dens = Dens;
        init();
        createCell();
        translateCell();
        this.potential = new Potential();
    }
    // kT initialization (runs on GibbsMoves instance construction,
    // and before any energy calculations in the box)
    public void initkT(double kT) {
        this.kT = kT;
        this.beta = 1/this.kT;
    }

    // [B] PREPARATION STAGE
    // boundary conditions function
    public void boundary(XYZ XYZij) {
        if (XYZij.getX() > this.half_box_side) XYZij.setX( XYZij.getX() - this.box_side );
        if (XYZij.getY() > this.half_box_side) XYZij.setY( XYZij.getY() - this.box_side );
        if (XYZij.getZ() > this.half_box_side) XYZij.setZ( XYZij.getZ() - this.box_side );
        if (XYZij.getX() <-this.half_box_side) XYZij.setX( XYZij.getX() + this.box_side );
        if (XYZij.getY() <-this.half_box_side) XYZij.setY( XYZij.getY() + this.box_side );
        if (XYZij.getZ() <-this.half_box_side) XYZij.setZ( XYZij.getZ() + this.box_side );
    }
    // energy transfer utilities
    public void flushEnergyChanges() {
        this.dPE=this.dVir=0.0;
    }
    public void includeEnergyChanges() {
        for (LJ pot : potential.ijpots.values()) {
            pot.finalizeChanges();
            this.dPE += pot.dPE;
            this.dVir += pot.dW;
        };

    }
    public void excludeEnergyChanges() {
        for (LJ pot : potential.ijpots.values()) {
            pot.finalizeChanges();
            this.dPE -= pot.dPE;
            this.dVir -= pot.dW;
        };

    }
    public void updateCorrections() {
        this.cPE=this.cVir=0.0;
        for (LJ pot : potential.ijpots.values()) {
//            pot.updateLongRangeCorrections();
            this.cPE += pot.cPE;
            this.cVir += pot.cW;
        }
        this.cPE /= this.Vol;
        this.cVir /= Math.pow(this.Vol,2);
    }
    public void concludeEnergyChanges() {
        // it is now without corrections
        if (this.N > 1) {
            this.PE += this.dPE;
            this.Vir += this.dVir;
//            this.PE += this.cPE*this.Dens;
            this.PEN = this.PE/this.N; //+ this.cPE/this.Vol;
            this.Pres = this.Dens * this.kT + this.Vir*beta/(3*this.Vol); //+ this.cVir*this.Dens*this.Dens;
//            this.Pres = this.Dens * this.kT + (this.Vir)/(3*this.Vol); // Silva's
        }
        else {
            this.PE = 0.0;
            this.PEN = 0.0;
            this.Pres = 0.0;
        }
    }
    public void computeEnergy() {
        for (int i=0; i<(this.N-1); i++) {
            Molecule moleculeI = this.molecules.get(i);
            for (int j = i + 1; j<this.N; j++) {
                Molecule moleculeJ = this.molecules.get(j);
                for (Particle particleI : moleculeI.particles)
                    for (Particle particleJ : moleculeJ.particles) {
                        // calculate x12,y12,z12 distances
                        XYZ XYZij = particleI.coords.minus(particleJ.coords);
                        // apply boundary conditions
                        this.boundary(XYZij);
                        // calculate squared norm
                        double Rij2 = XYZij.norm2();
                        // get proper LJ and compute
                        String ijType = "" + particleI.getType() + particleJ.getType();
                        potential.ijpots.get(ijType).computePair(Rij2);
                    }
            }
        }
    }
    // Initial energy of the box
    public void initialEnergy() {
        potential.fullFlush();
        this.flushEnergyChanges();
        this.computeEnergy();
        this.includeEnergyChanges();
        this.updateCorrections();
//        this.dPE += this.N*this.cPE;
        this.concludeEnergyChanges();
    }
    // update current absolute coordinates for each particle in a molecule
    // should be applied after any changes done to either molecule center or rotation
    public void updateCoords(Molecule molecule) {
        // if molecule center was shifted - apply boundary conditions (in-place)
        this.boundary(molecule.center);
        for (Particle particle : molecule.particles) {
            // to get current abs coordinates for each particle
            particle.coords = particle.origin // origin is a reference to template coords
                    .rotate(molecule.rotation) // apply relative rotation stored in molecule
                    .plus(molecule.center); // add abs coordinates of molecule center
            // apply boundary conditions to the result (in-place)
            this.boundary(particle.coords);
        }
    }
    // Utility that virtually replaces molecule at index with moleculeI
    // then computes pair potential of each particleI and all the others
    // NOTE: first line is the potential accumulators flush, though no flushes of box accums
    public void iReplace(int index, Molecule moleculeI, boolean RDF) {
//        potential.ljFlush();
        Molecule moleculeToIgnore = this.molecules.get(index);
        for (Molecule moleculeJ : this.molecules) {
            if (moleculeJ != moleculeToIgnore)
                for (Particle particleJ : moleculeJ.particles) {
                        for (Particle particleI : moleculeI.particles) {
                        // calculate x12,y12,z12 distances
                        XYZ XYZij = particleI.coords.minus(particleJ.coords);
                        // apply boundary conditions
                        this.boundary(XYZij);
                        // calculate squared norm
                        double Rij2 = XYZij.norm2();
                        // get proper LJ and compute
                        String ijType = "" + particleI.getType() + particleJ.getType();
                        potential.ijpots.get(ijType).computePair(Rij2);
                        if (RDF && ijType.equals("CC") && Rij2<rcutCC*rcutCC) {
                            accumulateRDFCC(Rij2);
                        }
                        else if (RDF && ijType.equals("SS") && Rij2<rcutSS*rcutSS) {
                            accumulateRDFSS(Rij2);
                        }
                    }
            }
        }
    }
    // shuffle molecules in the box (same idea as in displacement Gibbs step)
    public void randomize(String moveType) {
        for (int m=0; m<50; m++) {
            for (int i=0; i<this.N; i++) {
                // flush potential and box accumulators
                potential.fullFlush();
                this.flushEnergyChanges();
                // pick ith molecule and make a copy under noleculeNew
                Molecule molecule = this.molecules.get(i);
                Molecule moleculeNew = molecule.copy();
                // set up desired change (shift/rotation/both)
                if (moveType.equals("shift")) { moleculeNew.setShift(this.drmax); }
                if (moveType.equals("rotate")) { moleculeNew.setRotation(new Quaternion()); }
                if (moveType.equals("both")) {
                    moleculeNew.setShift(this.drmax);
                    moleculeNew.setRotation(new Quaternion());
                }
                // apply the change
                this.updateCoords(moleculeNew);
                // substitute the energy of current molecule from total box energy
                this.iReplace(i, molecule, false);
                this.excludeEnergyChanges();
                // add the energy of virtual replacement molecule
                this.iReplace(i, moleculeNew, false);
                this.includeEnergyChanges();
                // ACCEPTANCE CRITERIA
                // update and add corrections (if needed)
//                this.updateCorrections();
//                this.dPE += this.N*this.cPE;
                double criterion = this.dPE * beta;
                if (criterion < 0.0 || Math.exp(-criterion) > Math.random()) {
                    this.molecules.set(i, moleculeNew);
                    this.concludeEnergyChanges();
                }
            }
        }
    }

    // [C] GIBBS STAGE
    // FOR [2] CORRELATED VOLUME CHANGES
    public void setScaleFactor() {
        // get energy prior to scaling
//        new_box_side = Math.pow(new_Vol,1.0/3.0);
////        new_half_box_side = new_box_side/2;
//        scale_factor  = new_box_side/box_side;
        // scale factor [length units]
        scale_factor  = Math.pow(new_Vol,1.0/3.0)/box_side;
        // inverse scale factor in case one needs to revert changes (step denied)
        inv_scale_factor = 1/scale_factor;
    }
    public void upscale2(double scale_factor) {
        // scale box base fields
        Vol *= Math.pow(scale_factor,3);
        box_side *= scale_factor;
        half_box_side *= scale_factor;
        drmax = box_side*0.02;
        // loop over all molecules in the box
        for (Molecule molecule : this.molecules) {
            // scale center of each molecule (in-place)
            molecule.center.timesIP(scale_factor);
            // update current absolute coordinates
            this.updateCoords(molecule);
        }
        // loop over pair potentials
        for (LJ pot : potential.ijpots.values()) {
            // scale individual cutoffs
            pot.updCutoff(scale_factor);
        }
        this.updDens();
        this.updateCorrections();
    }
    public void scale2() {
        this.setScaleFactor();
        this.upscale2(this.scale_factor);
        this.computeEnergy();
        this.includeEnergyChanges();
    }
    public void postscale2() {
        // flush old and set new energy for each box
        PE=Vir=0;
        this.concludeEnergyChanges();
    }
    // update density (used after N or Vol changes)
    public void updDens() {
        this.Dens = this.N/this.Vol;
    }

    public void randomOverlap(Molecule molecule_attempt) {
        // check that receiving box is not empty
        if (this.N > 0) {
            // cycle over all existing particles in the box
            for (Molecule molecule : this.molecules) {
                // if distance to another molecule is of the allowed value
                // in this form overlap is only checked for molecular centers
                XYZ Cij = molecule.center.minus(molecule_attempt.center);
                this.boundary(Cij);
                double Cij2 = Cij.norm2();
                // Rmin2 should better be a field of molecular template rather than a box (as of right now)
                if (Cij2 < this.Rmin2) {
                    // then overlap is registered and insertion attempt fails
                    this.overlap = true; return;
                }
                else { this.overlap = false;
                for (Particle particle : molecule.particles) {
                    // and over all particles in the attempted molecule
                    for (Particle particle_attempt : molecule_attempt.particles) {
                        // calculate x12,y12,z12 linear distances
                        XYZ XYZij = particle.coords.minus(particle_attempt.coords);
                        // apply boundary conditions
                        this.boundary(XYZij);
                        // calculate squared norm
                        double Rij2 = XYZij.norm2();
                        // get proper LJ and compute
                        String ijType = "" + particle.getType() + particle_attempt.getType();
                        potential.ijpots.get(ijType).computePair(Rij2);
                        }
                    }
                }
            }
            // if this portion is reached - then no overlap detected and the molecule can be inserted
//            this.flushEnergyChanges();
            this.includeEnergyChanges();
            // a place to add longRangeCorrections if needed
//            longRangeCorrections (Vol1,rcut1);
//            DV12_in_box1  = (4.0 * V12) + (2*N1+1)* PE12lrc;
//            DV6_in_box1   = (4.0 * V6)  + (2*N1+1)* PE6lrc;
//            DW12_in_box1  = (48.0 * W12/3.0) + (2*N1+1)* W12lrc;
//            DW6_in_box1   = (48.0 * W6 / 3.0)+ (2*N1+1)* W6lrc;
        }
        // if receiving box is empty
        else {
            // then molecule can be placed anywhere in the box
            this.overlap = false;
        }
    }

    // apply boundary conditions for the whole molecule
    public void boundary(Molecule molecule) {
        this.boundary(molecule.center);
        for (Particle particle : molecule.particles) {
            this.boundary(particle.coords);
        }
    }



    public void averages(int step) {
        double stepback = step-1;
        double invstep = 1.0/step;
        // averages
        AVPEN   = (AVPEN*stepback  + PEN)  * invstep;
        AVN     = (AVN*stepback    + N)    * invstep;
        AVDens  = (AVDens*stepback + Dens) * invstep;
        AVPres  = (AVPres*stepback + Pres) * invstep;
        AVVol   = (AVVol*stepback  + Vol)  * invstep;
        rcutCC = potential.ijpots.get("CC").cutoff;
        AVrcutCC  = (AVrcutCC*stepback + rcutCC) * invstep;
        rcutSS = potential.ijpots.get("SS").cutoff;
        AVrcutSS  = (AVrcutSS*stepback + rcutSS) * invstep;
//        rcut = potential.ijpots.get("SS").cutoff;     // which cutoff to choose here??
//        AVrcut  = (AVrcut*stepback + rcut) * invstep; // likely rCCcut (int. between cores)
        // standard deviations
        vU   = (vU*stepback   + PEN*PEN)   * invstep;
        vN   = (vN*stepback   + N*N)       * invstep;
        vRho = (vRho*stepback + Dens*Dens) * invstep;
        vP   = (vP*stepback   + Pres*Pres) * invstep;
        vV   = (vV*stepback   + Vol*Vol)   * invstep;
        FLPEN  = Math.sqrt(Math.abs(vU   - (AVPEN*AVPEN)));
        FLN    = Math.sqrt(Math.abs(vN   - (AVN*AVN)));
        FLDens = Math.sqrt(Math.abs(vRho - (AVDens*AVDens)));
        FLPres = Math.sqrt(Math.abs(vP   - (AVPres*AVPres)));
        FLVol  = Math.sqrt(Math.abs(vV   - (AVVol*AVVol)));
    }

    public void exchangeRatios() {
        if (attempted_inserts>0) {
            ratio_exchange = (double) accepted_inserts / attempted_inserts; // exchange acceptance ratio (AVEx_box1)
            ratio_virtual = (double) allowed_inserts / attempted_inserts; // virtual insertion ratio (AVAcT_box1)
        }
    }

    //chemical potential and acceptance ratios
    public void chemicalPotential() {
        if (dCP > 0.0 && AVDens > 0.0 && attempted_inserts>0) {
            ChemPot=kT*(Math.log(AVDens)-Math.log(dCP/attempted_inserts));
        }
        nCP ++; // counter for Chemical Potential average
        ACChemPot += ChemPot;
        ACChemPot2 += ChemPot * ChemPot;
        // average Chemical Potential
        AVChemPot = ACChemPot/nCP;
        // standard deviation
        FLChemPot = Math.sqrt(Math.abs((ACChemPot2/nCP) - (AVChemPot*AVChemPot)));
    }


    public void accumulateRDFCC(double Rij2) {
//        double rcut = this.rcutCC/nRDF;
        int lr = (int) ( Math.sqrt(Rij2) * nRDF / rcutCC);
        if (lr < nRDF) grCC[lr] ++;
    }
    public void accumulateRDFSS(double Rij2) {
//        double rcut = this.rcutSS/nRDF;
        int lr = (int) ( Math.sqrt(Rij2) * nRDF / rcutSS);
        if (lr < nRDF) grSS[lr] ++;
    }

    public void updateRDFCC() {
//        for (int i=0; i<nRDF; i++) {
//            double delr = rcutCC/nRDF;
//            RADIUSCC[i] = ((i + 1) - 0.5) * delr;
//            RDFCC[i] = grCC[i] / attempted_displacements;
////            grCC[i] = 0;
//        }
        if (AVDens > 0.0 && AVN > 0.0 && attempted_displacements > 0) {
            double delr = AVrcutCC/nRDF;
            double C = 1.0 / (4.0*Math.PI*attempted_displacements*AVDens*delr);
            for (int i=0; i<nRDF; i++) {
                RADIUSCC[i] =((i+1) - 0.5 ) * delr;
                RDFCC[i] = (C*grCC[i]) / (RADIUSCC[i]*RADIUSCC[i]);
            }
        }
    }

    public void updateRDFSS() {
//        for (int i=0; i<nRDF; i++) {
//            double delr = rcutSS/nRDF;
//            RADIUSSS[i] = ((i + 1) - 0.5) * delr;
//            RDFSS[i] = grSS[i] / attempted_displacements;
////            grCC[i] = 0;
//        }
        if (AVDens > 0.0 && AVN > 0.0 && attempted_displacements > 0) {
            double delr = AVrcutSS/nRDF;
            double C = 1.0 / (4.0*Math.PI*attempted_displacements*AVDens*delr);
            for (int i=0; i<nRDF; i++) {
                RADIUSSS[i] =((i+1) - 0.5 ) * delr;
                RDFSS[i] = (C*grSS[i]) / (RADIUSSS[i]*RADIUSSS[i]);
            }
        }
    }

    public void accumulateDensities() {
        nupDens ++;
        int lr = (int) (this.Dens / delrDPD);
        if (lr < nrho) acrho[lr] ++;
    }
    public void updateDensityProbability() {
        for (int i=0;i < nrho;i++) {
            rrho[i] =((i+1) - 0.5 ) * delrDPD;
            Prho[i] = acrho[i]/nupDens;
        }
    }

    public void equilibrationCleanup() {

            AVPEN=AVDens=AVPres=0.0;
            AVN=AVVol=0.0;

            attempted_displacements=0;
            accepted_displacements=0;
            ratio_disp=0.0;

            attempted_inserts=0;
            allowed_inserts=0; // related to Chem Pot
            accepted_inserts=0;
            removals=0;
            ratio_exchange=0.0;
            ratio_virtual=0.0;

            dCP=0.0;
            ACChemPot=ACChemPot2=0.0;
            nCP=0;
            // cleanup for Rqdial Distribution Function
            AVrcutCC=AVrcutSS=0.0;
            for (int cl=0; cl<nRDF; cl++) {grCC[cl]=0.0; grSS[cl]=0.0;}
            // cleanup for density probability
            nupDens=0;
            for (int cl=0; cl<nrho; cl++) acrho[cl]=0.0;

//            LC=LS=0;
//            LA=1;
//            vU1=vU2=vRho1=vRho2=vP1=vP2=vMu1=vMu2=vN1=vN2=0.0;
//            sU1=sU2=sRho1=sRho2=sP1=sP2=sMu1=sMu2=sN1=sN2=0.0;
//            fU1=fU2=fRho1=fRho2=fP1=fP2=fMu1=fMu2=fN1=fN2=0.0;
//            for (int cl=0; cl<11; cl++) {
//                aU1[cl]=aU2[cl]=aRho1[cl]=aRho2[cl]=0.0;
//                aP1[cl]=aP2[cl]=aMu1[cl]=aMu2[cl]=aN1[cl]=aN2[cl]=0.0;
    }


    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public void countDimers() {
        double rdim = this.potential.ijpots.get("SS").updDimerRange(kT);
        double rdim2 = rdim*rdim;
//        System.out.println("rdim=" + rdim);
        int count=0;
        for (int i=0; i<(this.N-1); i++) {
            Molecule moleculeI = this.molecules.get(i);
            for (int j = i + 1; j < this.N; j++) {
                Molecule moleculeJ = this.molecules.get(j);
                for (Particle particleI : moleculeI.particles)
                    if (particleI.type=='S') {
                        for (Particle particleJ : moleculeJ.particles) {
                            if (particleJ.type=='S') {
                                XYZ XYZij = particleI.coords.minus(particleJ.coords);
                                boundary(XYZij);
                                double dist = XYZij.norm();
//                                double dist = particleI.coords.distance(particleJ.coords);
//                                double dist2 = particleI.coords.distance2(particleJ.coords);
                                if (dist<=rdim) {
                                    count++;
                                    System.out.println(ANSI_GREEN + "rdim=" + rdim + "; dist=" + dist + ANSI_RESET);
                                }
                            }
                        }
                    }
            }
        }
        System.out.println("Dimers#: " + count + " Dens: " + AVDens);
    }

}
