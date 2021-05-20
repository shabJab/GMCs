package com.mvc.gmcs.simulation;

import com.mvc.gmcs.simulation.primitives.Quaternion;
import com.mvc.gmcs.simulation.primitives.XYZ;
import com.mvc.gmcs.simulation.structures.Molecule;

public class GibbsMoves {
    public double N, VolTotal, kT, beta;
    Box box1, box2;

    // volume steps
    double dvmax; // maximum volume change
    int adjust_dvmax=500; // stride for adjusting max volume changes
    int attempted_volume_changes, accepted_volume_changes;
    public double ratio_vol;


    // initialize system (kT=kT_this=kT_box1=kT_box2) temperature
    public void initkT(double kT) {
        this.kT = kT;
        this.beta = 1/kT;
        this.box1.initkT(kT);
        this.box2.initkT(kT);
    }
    // ========= MAIN CONSTRUCTOR =========
    public GibbsMoves(Box box1, Box box2, double kT) {
        this.box1 = box1;
        this.box2 = box2;
        this.N = box1.N + box2.N;
        this.VolTotal = box1.Vol + box2.Vol;
        this.dvmax = Math.min(box1.Vol, box2.Vol)*0.02;
        this.initkT(kT);
    }

    // PERMITTED MOVES:

    // [1] INDEPENDENT DISPLACEMENTS OF PARTICLES
    public void displaceMolecules(Box box, String moveType) {
        // attempts counter
        box.attempted_displacements++;
        // flush potential and box accumulators
        box.potential.fullFlush();
        box.flushEnergyChanges();
        if (box.N > 1) {
            // get random molecule and create its copy
            int i = (int) (Math.random() * box.N);
            Molecule molecule = box.molecules.get(i);
            Molecule moleculeNew = molecule.copy();
            // set up desired change (shift/rotation/both)
            if (moveType.equals("shift")) { moleculeNew.setShift(box.drmax); }
            if (moveType.equals("rotate")) {
                moleculeNew.setRotation(new Quaternion
                        (Math.random(),Math.random(),Math.random(),Math.random()));
            }
            if (moveType.equals("both")) {
                moleculeNew.setShift(box.drmax);
                moleculeNew.setRotation(new Quaternion
                        (Math.random(),Math.random(),Math.random(),Math.random()));
            }
            // apply the change
            box.updateCoords(moleculeNew);
            // flush and calculate energy changes due to molecule displacement to moleculeNew
            // substitute the energy of current molecule from total box energy
            box.iReplace(i, molecule, true); // calculate current molecule energy
            box.excludeEnergyChanges(); // subtract from intermediate change (dPE)
            // add the energy of virtual replacement molecule
            box.iReplace(i, moleculeNew, false); // calculate new molecule energy
            box.includeEnergyChanges(); // add to intermediate change (dPE) with "+" sign
            // ACCEPTANCE CRITERIA
            // update and add corrections (if needed)
            double criterion = beta * (box.dPE);
            if (criterion < 0.0 || Math.exp(-criterion) > Math.random()) {
                // acceptance counter
                box.accepted_displacements++;
                // replace molecule
                box.molecules.set(i, moleculeNew);
//                box.molecules.remove(molecule);
//                box.molecules.add(moleculeNew);
                // commit energy change
                box.concludeEnergyChanges();
            }
            //update drmax
//            if (box.attempted_displacements>0)
//                box.ratio_disp = (double) box.accepted_displacements / box.attempted_displacements;
            if ((box.attempted_displacements % box.adjust_drmax) == 0) {
                // here box.ratio_disp*100 is displacement acceptance %
                box.ratio_disp = box.accepted_displacements / box.adjust_drmax;
                if (box.ratio_disp > 0.5) box.drmax = box.drmax * 1.05;
                else box.drmax = box.drmax * 0.95;
                if (box.drmax > box.half_box_side) box.drmax = box.half_box_side;
                box.accepted_displacements = 0;
            }
        } else {
            box.PE = 0.0;
            box.PEN = 0.0;
            box.Pres = box.Dens * box.kT;
        }
    }
    // [2] CORRELATED VOLUME CHANGES
    public void changeVolumes() {
        // attempts counter
        attempted_volume_changes++;
        // flush potential and box energy accumulators
        box1.potential.fullFlush();
        box2.potential.fullFlush();
        box1.flushEnergyChanges();
        box2.flushEnergyChanges();
        // set new volumes
        box1.new_Vol = box1.Vol + (2.0*Math.random()-1.0)*dvmax;
        box2.new_Vol = VolTotal - box1.new_Vol;
        // ensure positive volumes
        if (box1.new_Vol > 0.0 && box2.new_Vol > 0.0) {
            // scale molecule centers in each box
            box1.scale2();
            box2.scale2();
            // acceptance criteria
            // corrections
//            box1.dPE += box1.N*box1.cPE;
//            box2.dPE += box2.N*box2.cPE;
//            double criterion = beta * (box1.dPE-box1.PE + box2.dPE-box2.PE) // + corrections)
//                    -box1.N*Math.log(box1.new_Vol/box1.Vol)
//                    -box2.N*Math.log(box2.new_Vol/box2.Vol);
            double criterion = beta * (box1.dPE-box1.PE + box2.dPE-box2.PE) // + corrections)
                    -box1.N*Math.log(Math.pow(box1.scale_factor,3))
                    -box2.N*Math.log(Math.pow(box2.scale_factor,3));
//            System.out.println("VCRIT: " + criterion);
            if (criterion < 0.0 || Math.exp(-criterion) > Math.random()) {
                accepted_volume_changes ++;
//                System.out.println("VACCEPTED");
                // if step accepted - apply scaling:
                box1.postscale2();
                box2.postscale2();
            } else {
//                System.out.println("VDENIED");
                // if not accepted roll back
                box1.upscale2(box1.inv_scale_factor);
                box2.upscale2(box2.inv_scale_factor);
            }
//            box1.updDens();
//            box2.updDens();
//            box1.updateCorrections();
//            box2.updateCorrections();
            //accumulate to density probaability function
            box1.accumulateDensities();
            box2.accumulateDensities();
//            nupDens ++;
//            double delr = 0.9/ nrho;
//            int lr = (int) ( box1.Dens / delr);
//            if (lr < nrho) acrho[lr] ++;
//            int lr2 = (int) ( box2.Dens / delr);
//            if (lr2 < nrho) acrho2[lr2] ++;
//            //------------------------------------------
        }
        //update dvmax
//        if (attempted_volume_changes>0)
//            ratio_vol = (double) accepted_volume_changes / attempted_volume_changes;
        if ((attempted_volume_changes % adjust_dvmax) == 0) {
            // here ratio_vol*100 is a acceptance %
            ratio_vol = (double) accepted_volume_changes / adjust_dvmax;
            if (ratio_vol > 0.5) dvmax = dvmax * 1.05;
            else dvmax = dvmax * 0.95;
            accepted_volume_changes = 0;
        }
    }
    // [3] CORRELATED PARTICLE EXCHANGES
    public void exchangeMolecules(Box supplying, Box receiving) {
        // flush potential and box energy accumulators
        supplying.potential.fullFlush();
        receiving.potential.fullFlush();
        supplying.flushEnergyChanges();
        receiving.flushEnergyChanges();

        // increase attempt counter
        receiving.attempted_inserts++;
        // prepare molecule randomly placed inside receiving box
        XYZ random_center = new XYZ(Math.random(), Math.random(), Math.random())
                .times(receiving.box_side).minus(receiving.half_box_side);
        Molecule molecule2include = new Molecule("Monomer", random_center);
        Quaternion random_rotation = new Quaternion
                (Math.random(),Math.random(),Math.random(),Math.random());
        molecule2include.setRotation(random_rotation);
        receiving.updateCoords(molecule2include);
        // check overlap and compute potentials
        receiving.randomOverlap(molecule2include);


        if (!receiving.overlap){
            receiving.allowed_inserts++;
            receiving.dPE += (2*receiving.N+1)*receiving.cPE;
            // accumulate Widom's chemical potential
            receiving.dCP += Math.exp(-beta * receiving.dPE);

            if (receiving.N < this.N) {
                // prepare supplying end for subtracting energy change
                // pick a random molecule to remove from supplying box
                int idx = (int) (supplying.N * Math.random());
                Molecule molecule2exclude = supplying.molecules.get(idx);
                // compute energy attributed to this molecule
                supplying.iReplace(idx, molecule2exclude, false);
                supplying.excludeEnergyChanges();
                // corrections
                supplying.dPE -= (2*supplying.N-1)*supplying.cPE;

                double criterion = beta*(supplying.dPE + receiving.dPE) // + corrections) //+corrections*beta
                        + Math.log((supplying.Vol*(receiving.N+1))
                        /(receiving.Vol*supplying.N));
                if (criterion < 0.0 || Math.exp(-criterion) > Math.random()) {
                    receiving.molecules.add(molecule2include);
                    receiving.accepted_inserts++;
                    receiving.N++;
                    receiving.updDens();
                    receiving.concludeEnergyChanges();

                    supplying.molecules.remove(idx);
                    supplying.removals++;
                    supplying.N--;
                    supplying.updDens();
                    supplying.concludeEnergyChanges();
                }
            }
        }

        box1.exchangeRatios();
        box2.exchangeRatios();

//        // check that receiving box is not full
//        if (receiving.N < this.N) {
//            // if no overlap detected
//            if (!receiving.overlap) {
//                // increase insertion attempt counter
//                receiving.allowed_inserts++;
////                accp_box1 += Math.exp(-beta*receiving.dPE); //Widom's chemical potential
//                // prepare supplying end for subtracting energy change
//                // pick a random molecule to remove from supplying box
//                int idx = (int) (supplying.N * Math.random());
//                Molecule molecule2exclude = supplying.molecules.get(idx);
//                // compute energy attributed to this molecule
//                supplying.iReplace(idx, molecule2exclude, false);
//                supplying.excludeEnergyChanges();
//                // corrections
////                double corrections = 2*(receiving.N+1)*receiving.cPE + 2*(supplying.N-1)*supplying.cPE;
//                supplying.dPE -= (2*supplying.N-1)*supplying.cPE;
//                receiving.dPE += (2*receiving.N+1)*receiving.cPE;
//                // accumulate Widom's chemical potential
//                receiving.dCP += Math.exp(-beta * receiving.dPE);
////                supplying.dPE -= supplying.cPE;
////                receiving.dPE += receiving.cPE;
//                double criterion = beta*(supplying.dPE + receiving.dPE) // + corrections) //+corrections*beta
//                        + Math.log((supplying.Vol*(receiving.N+1))
//                                  /(receiving.Vol*supplying.N));
//                if (criterion < 0.0 || Math.exp(-criterion) > Math.random()) {
//                    receiving.molecules.add(molecule2include);
//                    receiving.accepted_inserts++;
//                    receiving.N++;
//                    receiving.updDens();
//                    receiving.concludeEnergyChanges();
//
//                    supplying.molecules.remove(idx);
//                    supplying.removals++;
//                    supplying.N--;
//                    supplying.updDens();
//                    supplying.concludeEnergyChanges();
//                }
////                receiving.updDens();
////                supplying.updDens();
//            }
//        }
//        // else case to account for virtual inserts (even when box is full)
//        // NOTE: needed for Widom's chemical potential
//        else {
//            if (!receiving.overlap) {
//                receiving.allowed_inserts++;
//                receiving.dPE += (2*receiving.N+1)*receiving.cPE;
//                // accumulate Widom's chemical potential
//                receiving.dCP += Math.exp(-beta * receiving.dPE);
//            }
//        }

    }

    public void equilibrationCleanup() {
        attempted_volume_changes=0;
        accepted_volume_changes=0;
        ratio_vol = 0.0;
        box1.equilibrationCleanup();
        box2.equilibrationCleanup();
    }
}

