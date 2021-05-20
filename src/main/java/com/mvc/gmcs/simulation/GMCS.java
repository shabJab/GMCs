package com.mvc.gmcs.simulation;

import com.mvc.gmcs.simulation.energy.Potential;
import com.mvc.gmcs.simulation.structures.Molecule;
import com.mvc.gmcs.simulation.structures.Particle;
import com.mvc.gmcs.simulation.structures.ParticleBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GMCS {

    static int staN1=108, staN2=108, staN=staN1+staN2, stanVol=1, stanswap=108;
    static double stanvar=staN+stanVol+stanswap;
    static double stakT=1;
    static double stadens1=0.3225, stadens2=0.3225;

    double D1=0.3225, D2=0.3225, kT=1;
    int N1=108, N2=108, N=N1+N2, nVol=1, nswap=108;
    double nvar=N+nVol+nswap;
    GibbsMoves gibbsMoves;
    Box box1, box2;

//    public static void statistics() {
//        dens1 += box1.Dens;
//        dens2 += box2.Dens;
//        System.out.println(dens1/step);
//        System.out.println(dens2/step);
//    }
    public void preps() {
        // 1. set molecular template
        final List<Particle> template = List.of(
                new ParticleBuilder()
                        .type('C')
                        .build()
        );
        Molecule.addTemplate("Monomer", template);
        // 2. add necessary potentials
        Potential.addLJ('C','C',1,1,4);
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
    }



    public void evolution() {
        // MC loop
        int step = 0;
        while (step < 20) {
            step ++; //counter of MC cycles
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
//            System.out.println("ENERGY");
//            System.out.println(box1.dPE);
//            System.out.println(box2.dPE);
//            System.out.println(box1.PE);
//            System.out.println(box2.PE);
//            System.out.println(box1.PEN);
//            System.out.println(box2.PEN);
//            System.out.println("NS");
//            System.out.println(box1.N);
//            System.out.println(box2.N);
//            System.out.println("VOLUMES");
//            System.out.println(box1.Vol);
//            System.out.println(box2.Vol);
//            System.out.println("PRESSURES");
//            System.out.println(box1.Pres);
//            System.out.println(box2.Pres);
            System.out.println("DENSITIES");
            System.out.println(box1.Dens);
            System.out.println(box2.Dens);
//            System.out.println("AVGDENSITIES");
//            dens1 += box1.Dens;
//            dens2 += box2.Dens;
//            System.out.println(dens1/step);
//            System.out.println(dens2/step);

//            statistics();
//            box1.countDimers();
        }
    }

    public static void main(String[] args) {
        final List<Particle> template = List.of(
                new ParticleBuilder()
                        .type('C')
                        .build()
        );
//        final List<Particle> template = List.of(
//                new ParticleBuilder()
//                        .type('C')
//                        .build(),
//                new ParticleBuilder()
//                        .type('S')
//                        .place(0.4,0,0)
//                        .build()
//        );
        Molecule.addTemplate("Monomer", template);

        Potential.addLJ('C','C',1,1,4);
//        Potential.addLJ('S','S',1,0.2,4);
//        Potential.addLJ('C','S',1,0.5,4);
//        Potential.addLJ('C','C',1,1);
//        Potential.addLJ('S','S',1,0.2);
//        Potential.addLJ('C','S',1,0.6);

        Box box1 = new BoxBuilder()
                .unitCell("FCC")
                .numberMolecules(staN1)
                .numberDensity(0.3225)
                .build();

        Box box2 = new BoxBuilder()
                .unitCell("FCC")
                .numberMolecules(staN2)
                .numberDensity(0.3225)
                .build();


        GibbsMoves gibbsMoves = new GibbsMoves(box1,box2, stakT);
//        gibbsMoves.initkT(1.2);
        box1.initialEnergy();
        System.out.println(box1.Pres);
        System.out.println(box1.PEN);
        box1.randomize("both");

        box2.initialEnergy();
        System.out.println(box2.Pres);
        System.out.println(box2.PEN);
        box2.randomize("both");

        System.out.println("-=-=-=-=-=-=-=-=-=-=-");
        System.out.println(box1.PEN);
        System.out.println(box2.PEN);
        System.out.println(box1.Pres);
        System.out.println(box2.Pres);
        System.out.println("-=-=-=-=-=-=-=-=-=-=-");

        // for random shuffling
        List<Integer> substeps = new ArrayList<>();
        for (int i = 0; substeps.size()< stanvar; i++) {
            substeps.add(i);
        }

        // MC loop
        int step = 0;
        while (step < 1000) {
            step ++; //counter of MC cycles
            System.out.println("Step: " + step);
            Collections.shuffle(substeps); // random shuffle of Gibbs moves order
            for (int i = 0; i< stanvar; i++) {
                double ran = Math.random();
                int substep = substeps.get(i);
                if (ran < box1.N / stanvar) {
//                if (substep < box1.N) {
//                    System.out.println("    displacement1");
                    gibbsMoves.displaceMolecules(box1, "both");
                } else if (ran < staN / stanvar) {
//                } else if (substep < N) {
//                    System.out.println("    displacement2");
                    gibbsMoves.displaceMolecules(box2, "both");
                } else if (ran < (staN + stanVol) / stanvar) {
//                } else if (substep < (N + nVol)) {
//                    System.out.println("=========VOLUMES========");
                    gibbsMoves.changeVolumes();
                } else if (ran < (staN + stanVol + 0.5 * stanswap) / stanvar) {
//                } else if (substep < (N + nVol + 0.5 * nswap)) {
//                    System.out.println("=1=to=2=EXCHANGES=======");
                    gibbsMoves.exchangeMolecules(box2, box1);
                } else {
//                    System.out.println("=2=to=1=EXCHANGES=======");
                    gibbsMoves.exchangeMolecules(box1, box2);
                }
//                System.out.println("INSIDE ENERGY");
//                System.out.println(box1.dPE);
//                System.out.println(box2.dPE);
            }
            System.out.println("ENERGY");
//            System.out.println(box1.dPE);
//            System.out.println(box2.dPE);
//            System.out.println(box1.PE);
//            System.out.println(box2.PE);
            System.out.println(box1.PEN);
            System.out.println(box2.PEN);
//            System.out.println("NS");
//            System.out.println(box1.N);
//            System.out.println(box2.N);
            System.out.println("VOLUMES");
            System.out.println(box1.Vol);
            System.out.println(box2.Vol);
//            System.out.println("PRESSURES");
//            System.out.println(box1.Pres);
//            System.out.println(box2.Pres);
            System.out.println("DENSITIES");
            System.out.println(box1.Dens);
            System.out.println(box2.Dens);
            System.out.println("AVGDENSITIES");
            stadens1 += box1.Dens;
            stadens2 += box2.Dens;
            System.out.println(stadens1 /step);
            System.out.println(stadens2 /step);
//            statistics();
//            box1.countDimers();
//            FakeSnapStorage.addSnap();
//            SnapController
        }

        System.out.println("=========VOLUMES========");
        System.out.println(box1.Vol);
        System.out.println(box2.Vol);
        System.out.println("========EXCHANGES=======");
        System.out.println(box1.N);
        System.out.println(box2.N);

//        Box box1 = new Box(N1_0, Dens1_0);
//        Box box1 = new BoxBuilder().N(N1_0).Dens(Dens1_0).buildBox();

//        PairPotentials pairLJ = new PairPotentials();
//        pairLJ.addPair('C','S',1,1);
//        System.out.println(pairLJ.types.get(0));

//        TestClass tst = new TestClass();
//        tst.testRotations();

//        System.out.println(energy.ijpots.get("CA").epsilon);
//        energy.ijpots.get("CS").epsilon = 5;
//        System.out.println(energy.ijpots.get("SC").epsilon);
//        System.out.println(energy.ijpots.get("CS").V12);
////        energy.iInBox(0,box1);
//        System.out.println(energy.ijpots.get("SC").V12);

//        LJ pot = new LJ('C','S',1.0,1.0,4.0);
//        if (pot.type.equals(""+'C'+'S') || pot.type.equals(""+'S'+'C')){
//            System.out.println("pooooping");
//        }





//        DoubleColumn x = DoubleColumn.create("X");
//        DoubleColumn y = DoubleColumn.create("Y");
//        DoubleColumn z = DoubleColumn.create("Z");
//        DoubleColumn xs = DoubleColumn.create("X");
//        DoubleColumn ys = DoubleColumn.create("Y");
//        DoubleColumn zs = DoubleColumn.create("Z");
//
//        for (Molecule molecule:box1.molecules) {
////            molecule.particles.get(0).coords.print();
//            XYZ cores_xyz = molecule.particles.get(0).coords;
//            x.append(cores_xyz.x);
//            y.append(cores_xyz.y);
//            z.append(cores_xyz.z);
////            XYZ sites_xyz = molecule.particles.get(1).coords;
////            xs.append(sites_xyz.x);
////            ys.append(sites_xyz.y);
////            zs.append(sites_xyz.z);
//        }
//
//        Margin margin = Margin.builder().left(0).right(1).bottom(1).padding(0).build();
//        Scene scene = Scene.sceneBuilder()
//                .zAxis(Axis.builder()
//                        .title("z-axis")
//                        .build())
//                .build();
//        Font f = Font.builder()
//                .family(Font.Family.ARIAL)
//                .size(Integer.parseInt("24"))
//                .color("green")
//                .build();
//        Layout layout = Layout.builder()
//                .title("Layout title")
//                .titleFont(f)
//                .height(768)
//                .width(1200)
//                .plotBgColor("red")
//                .paperBgColor("blue")
//                .hoverMode(Layout.HoverMode.CLOSEST)
//                .scene(scene)
//                .margin(margin).showLegend(true)
//                .build();
//        Line marker_line = Line.builder().color("black").width(1.2).build();
//        Marker cores_marker = Marker.builder()
//                .color("red").size(20).opacity(0.9)
//                .line(marker_line).build();
//        Marker sites_marker = Marker.builder()
//                .color("blue").size(10).opacity(0.9)
//                .line(marker_line).build();
//
//        Scatter3DTrace cores_trace = Scatter3DTrace.builder(x,y,z).marker(cores_marker).build();
//        Scatter3DTrace sites_trace = Scatter3DTrace.builder(xs,ys,zs).marker(sites_marker).build();
//        Figure plot = Figure.builder().layout(layout).addTraces(cores_trace, sites_trace).build();
//        Plot.show(plot);
    }
}
