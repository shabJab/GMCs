package com.mvc.gmcs;

import com.mvc.gmcs.dataprofile.Snap;
import com.mvc.gmcs.playback.Playback;
import com.mvc.gmcs.params.Params;
import com.mvc.gmcs.requests.Requests;
import com.mvc.gmcs.simulation.Box;
import com.mvc.gmcs.simulation.BoxBuilder;
import com.mvc.gmcs.simulation.GibbsMoves;
import com.mvc.gmcs.simulation.energy.Potential;
import com.mvc.gmcs.simulation.structures.Molecule;
import com.mvc.gmcs.simulation.structures.Particle;
import com.mvc.gmcs.simulation.structures.ParticleBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class GmcsApplication {

    static int N1=100, N2=100, N=N1+N2, nVol=2, nswap=100;
    static double nvar=N+nVol+nswap;
    static double D1=0.3, D2=0.3;
    static double kT=1.2;

    static double V1_V, V2_V, N1_N, N2_N; // scatter plot

    static Params params = new Params(N1,N2,D1,D2,kT);
    static Playback pause_reset;
    static boolean pause = true;
    static boolean reset = false;
    static boolean equilibrate = false;
    static int timeout = 200;

    static List<Double> xcores1 = new ArrayList<>();
    static List<Double> ycores1 = new ArrayList<>();
    static List<Double> zcores1 = new ArrayList<>();
    static List<Double> xsites1 = new ArrayList<>();
    static List<Double> ysites1 = new ArrayList<>();
    static List<Double> zsites1 = new ArrayList<>();

    static List<Double> xcores2 = new ArrayList<>();
    static List<Double> ycores2 = new ArrayList<>();
    static List<Double> zcores2 = new ArrayList<>();
    static List<Double> xsites2 = new ArrayList<>();
    static List<Double> ysites2 = new ArrayList<>();
    static List<Double> zsites2 = new ArrayList<>();

//    public Map<String, Double> cumulatives = new HashMap<>();

    static Snap snap = new Snap();
    static int step, equilibration_step=2000, run_step;
    static Box box1, box2;
    static GibbsMoves gibbsMoves;
    static List<Particle> template;


    public static void updateSnap() throws IOException, InterruptedException {
        snap.setStep(step);
        snap.setDens1(box1.Dens);
        snap.setDens2(box2.Dens);
        snap.setRrho(box1.rrho);
        snap.setPrho1(box1.Prho);
        snap.setPrho2(box2.Prho);
        snap.setRadius1c(box1.RADIUSCC);
        snap.setRadius2c(box2.RADIUSCC);
        snap.setRdf1c(box1.RDFCC);
        snap.setRdf2c(box2.RDFCC);
        snap.setRadius1s(box1.RADIUSSS);
        snap.setRdf1s(box1.RDFSS);
        snap.setRadius2s(box2.RADIUSSS);
        snap.setRdf2s(box2.RDFSS);

//        if (step % 1 == 0) {
        xcores1.clear();
        ycores1.clear();
        zcores1.clear();
        xsites1.clear();
        ysites1.clear();
        zsites1.clear();
        xcores2.clear();
        ycores2.clear();
        zcores2.clear();
        xsites2.clear();
        ysites2.clear();
        zsites2.clear();
        for (Molecule molecule : box1.molecules) {
            for (Particle particle : molecule.particles) {
                if (particle.type=='C') {
                    xcores1.add(particle.coords.x);
                    ycores1.add(particle.coords.y);
                    zcores1.add(particle.coords.z);
                }
                else if (particle.type=='S') {
                    xsites1.add(particle.coords.x);
                    ysites1.add(particle.coords.y);
                    zsites1.add(particle.coords.z);
                }
            }
        }
        for (Molecule molecule : box2.molecules) {
            for (Particle particle : molecule.particles) {
                if (particle.type=='C') {
                    xcores2.add(particle.coords.x);
                    ycores2.add(particle.coords.y);
                    zcores2.add(particle.coords.z);
                }
                else if (particle.type=='S') {
                    xsites2.add(particle.coords.x);
                    ysites2.add(particle.coords.y);
                    zsites2.add(particle.coords.z);
                }
            }
        }
        snap.cores1.put("x", xcores1);
        snap.cores1.put("y", ycores1);
        snap.cores1.put("z", zcores1);
        snap.sites1.put("x", xsites1);
        snap.sites1.put("y", ysites1);
        snap.sites1.put("z", zsites1);
        snap.cores2.put("x", xcores2);
        snap.cores2.put("y", ycores2);
        snap.cores2.put("z", zcores2);
        snap.sites2.put("x", xsites2);
        snap.sites2.put("y", ysites2);
        snap.sites2.put("z", zsites2);

        snap.cumulatives.put("AVPEN1", box1.AVPEN);
        snap.cumulatives.put("AVPEN2", box2.AVPEN);
        snap.cumulatives.put("FLPEN1", box1.FLPEN);
        snap.cumulatives.put("FLPEN2", box2.FLPEN);

//            snap.cumulatives.put("N1", (double) box1.N);
//            snap.cumulatives.put("N2", (double) box2.N);
        snap.cumulatives.put("AVN1", box1.AVN);
        snap.cumulatives.put("AVN2", box2.AVN);
        snap.cumulatives.put("FLN1", box1.FLN);
        snap.cumulatives.put("FLN2", box2.FLN);

//            snap.cumulatives.put("V1", box1.Vol);
//            snap.cumulatives.put("V2", box2.Vol);
        snap.cumulatives.put("AVV1", box1.AVVol);
        snap.cumulatives.put("AVV2", box2.AVVol);
        snap.cumulatives.put("FLV1", box1.FLVol);
        snap.cumulatives.put("FLV2", box2.FLVol);

//            snap.cumulatives.put("D1", box1.Dens);
//            snap.cumulatives.put("D2", box2.Dens);
        snap.cumulatives.put("AVD1", box1.AVDens);
        snap.cumulatives.put("AVD2", box2.AVDens);
        snap.cumulatives.put("FLD1", box1.FLDens);
        snap.cumulatives.put("FLD2", box2.FLDens);

//            snap.cumulatives.put("P1", box1.Pres);
//            snap.cumulatives.put("P2", box2.Pres);
        snap.cumulatives.put("AVP1", box1.AVPres);
        snap.cumulatives.put("AVP2", box2.AVPres);
        snap.cumulatives.put("FLP1", box1.FLPres);
        snap.cumulatives.put("FLP2", box2.FLPres);

        snap.cumulatives.put("AVCP1", box1.AVChemPot);
        snap.cumulatives.put("AVCP2", box2.AVChemPot);
        snap.cumulatives.put("FLCP1", box1.FLChemPot);
        snap.cumulatives.put("FLCP2", box2.FLChemPot);

        snap.ratios.put("disp1", box1.ratio_disp*100);
        snap.ratios.put("disp2", box2.ratio_disp*100);
        snap.ratios.put("vol", gibbsMoves.ratio_vol*100);
        snap.ratios.put("ex1", box1.ratio_exchange*100);
        snap.ratios.put("ex2", box2.ratio_exchange*100);
        snap.ratios.put("virt1", box1.ratio_virtual*100);
        snap.ratios.put("virt2", box2.ratio_virtual*100);

        Requests.putSnap(snap);
    }

    public static void equilibrationCleanup() {
        run_step=1;
//        V1_V=N1_N=V2_V=N2_N=0.0;
        gibbsMoves.equilibrationCleanup();
    }

    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
        SpringApplication.run(GmcsApplication.class, args);
//        GMCS gmcs = new GMCS();
        // I. set a large inf loop for the whole simulation
        RUN_LOOP:
        while(true) {
//            Thread.sleep(timeout);
            params = Requests.getParams();
            N1 = params.getN1();
            N2 = params.getN2();
            D1 = params.getD1();
            D2 = params.getD2();
            kT = params.getkT();
            // II. set default preps
            // 1. set molecular template
            // Silva's single particle case
//            template = List.of(
//                    new ParticleBuilder()
//                            .type('C')
//                            .build()
//            );
            // monomer molecule (core-site dumbbell)
            double sigmac = 1;
            double sigmas = 0.2;
            double d = 0; // placement shift '+' is for extrusion, '-' is for intrusion
            double p = (sigmac-sigmas)*0.5 + d; // placement coordinate of the site relative to the core
//            double sigmacs = (sigmac+sigmas)*0.5 - dp;
//            double sigmacs = (sigmac-sigmas)*0.5 - dp;
            double sigmacs = sigmas + p;
            System.out.println(" placement: " + p + " | sigmacs: " + sigmacs);
            template = List.of(
                new ParticleBuilder()
                        .type('C')
                        .build(),
                new ParticleBuilder()
                        .type('S')
                        .place(p,0,0)
                        .build()
            );
            Molecule.addTemplate("Monomer", template);
            // 2. add necessary potentials
            // for Silva's single particle case
//            Potential.addLJ('C','C',1,1,4);
            // for monomer-with-site molecule
            Potential.addLJ('C','C',1,sigmac);
            Potential.addLJ('S','S',0.5,sigmas);
            Potential.addLJ('C','S',0.5,sigmacs);
            // 3. initialize boxes
            box1 = new BoxBuilder()
                    .unitCell("FCC")
                    .numberMolecules(N1)
                    .numberDensity(D1)
                    .build();
            box2 = new BoxBuilder()
                    .unitCell("FCC")
                    .numberMolecules(N2)
                    .numberDensity(D2)
                    .build();
            // 4. instantiate gibbs moves class (includes )
            gibbsMoves = new GibbsMoves(box1,box2, kT);
            // 5.a. compute initial energies for each box
            box1.initialEnergy();
            box2.initialEnergy();
            // 5.b. run 50 MC displacement steps to randomize molecule placements
            box1.randomize("both");
            box2.randomize("both");

            // 6. submit initial state to /snap endpoint
            step = 0;
            run_step = 0;
            System.out.println("Step: " + step);
            box1.averages(1);
            box2.averages(1);
            GmcsApplication.updateSnap();

            // III. first Playback control
            // (at the same time change js defaults from its input fields)
            while(pause) {
                // 1. knock /playback endpoint until pause is false
                pause_reset = Requests.getPlayback();
                pause = pause_reset.isPause();
                reset = pause_reset.isReset();
                // here also to add get request for updates from js input fields
                // if js inputs differ from current parameter set -> rerun preps (continue RUN_LOOP)
                // 2. if reset encountered -> rerun preps
                if (reset) {
                    pause = true;
                    reset = false;
                    Requests.putReset();
                    continue RUN_LOOP;
                }
                // (3.) Optional step to ease system by idling between requests
                Thread.sleep(timeout);
            }

            // IV. MC loop
            // 2000 steps - equilibration, 5000-7000 - simulation
//            EVOLUTION_LOOP:
            while (true) {
                step ++; //counter of MC cycles
                System.out.println("Step: " + step);
                for (int i = 0; i< nvar; i++) {
                    double ran = Math.random();
                    if (ran < box1.N / nvar) {
//                    System.out.println("    displacement1");
                        gibbsMoves.displaceMolecules(box1, "both");
                    } else if (ran < N / nvar) {
//                    System.out.println("    displacement2");
                        gibbsMoves.displaceMolecules(box2, "both");
                    } else if (ran < (N + nVol) / nvar) {
//                    System.out.println("=========VOLUMES========");
                        gibbsMoves.changeVolumes();
                    } else if (ran < (N + nVol + 0.5 * nswap) / nvar) {
//                    System.out.println("=1=to=2=EXCHANGES=======");
                        gibbsMoves.exchangeMolecules(box2, box1);
                    } else {
//                    System.out.println("=2=to=1=EXCHANGES=======");
                        gibbsMoves.exchangeMolecules(box1, box2);
                    }
                }

                run_step ++;
                if (step==equilibration_step) {
                    GmcsApplication.equilibrationCleanup();
                }

                box1.averages(run_step);
                box2.averages(run_step);

                box1.chemicalPotential();
                box2.chemicalPotential();

                box1.updateDensityProbability();
                box2.updateDensityProbability();

                box1.updateRDFCC();
                box2.updateRDFCC();
                box1.updateRDFSS();
                box2.updateRDFSS();

                // scatter plot
                if (step > equilibration_step) {
                        if ((step % 10) == 0) {
                            V1_V = box1.Vol/gibbsMoves.VolTotal;
                            N1_N = (double) N1/N;
                            V2_V = 1.0-V1_V;
                            N2_N = 1.0-N1_N;
                        }
                }

//                System.out.println("CHEMICAL POTENTIAL");
//                System.out.println(box1.AVChemPot);
//                System.out.println(box2.AVChemPot);
//                System.out.println("PRESSURE");
//                System.out.println(box1.AVPres);
//                System.out.println(box2.AVPres);
//                System.out.println(box1.Pres);
//                System.out.println(box2.Pres);
//                System.out.println("ENERGY");
//                System.out.println(box1.PEN);
//                System.out.println(box2.PEN);
//                System.out.println("NS");
//                System.out.println(box1.N);
//                System.out.println(box2.N);
//                System.out.println("VOLUMES");
//                System.out.println(box1.Vol);
//                System.out.println(box2.Vol);
//                System.out.println("DENSITIES");
//                System.out.println(box1.Dens);
//                System.out.println(box2.Dens);

                box1.countDimers();
                box2.countDimers();

                GmcsApplication.updateSnap();
//                Thread.sleep(timeout);

                // second Playback control
                pause_reset = Requests.getPlayback();
                pause = pause_reset.isPause();
                reset = pause_reset.isReset();
                equilibrate = pause_reset.isEquilibrate();
//                Thread.sleep(timeout);

                if (reset) {
                    pause = true;
                    reset = false;
                    Requests.putReset();
                    // here it should also patch reset on Spring
//                    Thread.sleep(timeout);
                    continue RUN_LOOP;
//                    break EVOLUTION_LOOP;
                }
                if (equilibrate) {
                    GmcsApplication.equilibrationCleanup();
                    Requests.putEquilibrate(pause,reset);
                }
                while(pause) {
                    pause_reset = Requests.getPlayback();
                    pause = pause_reset.isPause();
                    reset = pause_reset.isReset();
                    // here also to add get request for updates from js input fields
                    // if js inputs differ from current parameter set -> rerun preps (continue RUN_LOOP)
                    if (reset) {
                        pause = true;
                        reset = false;
                        Requests.putReset();
//                        break EVOLUTION_LOOP;
                        continue RUN_LOOP;
                    }
                    if (equilibrate) {
                        GmcsApplication.equilibrationCleanup();
                        Requests.putEquilibrate(pause,reset);
                    }
                    Thread.sleep(timeout);
                }
            } // EVOLUTION_LOOP
        } // RUN_LOOP
    }

}
