package com.mvc.gmcs.simulation.energy;

import java.util.Map;

public class Potential {
    static Map<String, LJ> ijpots_templates = new PairPotMap<>();
    public Map<String, LJ> ijpots = new PairPotMap<>();

    public static void addLJ(char type1, char type2, double epsilon, double sigma, double cutoff) {
        String ij = "" + type1 + type2;
        LJ pot = new LJ(epsilon, sigma, cutoff);
        Potential.ijpots_templates.put(ij, pot);
    }
    public static void addLJ(char type1, char type2, double epsilon, double sigma) {
        String ij = "" + type1 + type2;
        LJ pot = new LJ(epsilon, sigma);
        Potential.ijpots_templates.put(ij, pot);
    }

//    public static void flush() {
//        for (LJ pot : ijpots_templates.values()) {
//            pot.fullFlush();
//        }
//    }
//    public static void updCutoff(double inverse_scale_factor) {
//        for (LJ pot : ijpots_templates.values()) {
//            pot.updCutoff(inverse_scale_factor);
//        }
//    }
    // preps for object instances
    public void ljFlush() {
        for (LJ pot : this.ijpots.values()) {
            pot.ljFlush();
        }
    }
    public void deltaFlush() {
        for (LJ pot : this.ijpots.values()) {
            pot.deltaFlush();
        }
    }
    public void fullFlush() {
        for (LJ pot : this.ijpots.values()) {
            pot.fullFlush();
        }
    }

    public void instFill() {
        for (Map.Entry<String, LJ> ijpot : Potential.ijpots_templates.entrySet()) {
            String ij = ijpot.getKey();
            LJ pot = ijpot.getValue().copy();
            this.ijpots.put(ij, pot);
        }
    }

    public Potential() {
        this.instFill();
    }
//    public LJ getLJ(char type1, char type2) {
//        String ijType = "" + type1 + type2;
//        return
//    }
}
