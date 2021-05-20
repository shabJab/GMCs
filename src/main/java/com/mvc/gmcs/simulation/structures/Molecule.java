package com.mvc.gmcs.simulation.structures;

import com.mvc.gmcs.simulation.primitives.Quaternion;
import com.mvc.gmcs.simulation.primitives.XYZ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Molecule {
    static final Map<String, List<Particle>> templates = new HashMap<>();
    String type;
    public XYZ center;
    public Quaternion rotation = new Quaternion(1,0,0,0);
    public List<Particle> particles = new ArrayList<>();


    public Molecule() {}

    public static void addTemplate(String template_name, List<Particle> template) {
        Molecule.templates.put(template_name, template);
    }

    public List<Particle> getTemplate() {
        return Molecule.templates.get(this.type);
    }

    public int size() {
        return this.getTemplate().size();
    }

    public void setShift(double max_shift) {
        XYZ shift = new XYZ(
                Math.random(), Math.random(), Math.random())
                .times(2*max_shift).minus(max_shift);
        this.center.plusIP(shift);
    }

    public void setRotation(Quaternion rotation) {
        // normalize rotation
        rotation.normalize();
        // update rotation quaternion for full molecule rotation
        if (this.rotation.norm()!=0) { this.rotation.multiplyIP(rotation); }
        else { this.rotation.set(rotation); }
    }


    public Molecule copy() {
        Molecule copy = new Molecule();
        copy.type = this.type;
        copy.center = this.center.copy();
        copy.rotation = this.rotation.copy();
        copy.particles = new ArrayList<>();
        for (int i=0; i<(this.particles.size()); i++) {
            copy.particles.add(i, this.particles.get(i).copy());
        }
        return copy;
    }

    // constructor
    public Molecule(String type, XYZ center) {
        if (!Molecule.templates.containsKey(type)) {
            throw new RuntimeException("\nSupplied molecule type \""
                    + type + "\" does not have a corresponded template\n"
                    + "Either add a proper molecular template or change molecule type for an instance molecule");
        }
        this.type = type;
        this.center = center;
        for (Particle template_particle : Molecule.templates.get(type)) {
            Particle actual_particle = template_particle.copy();
            // origin refers to template particle relative coordinates
//            template_particle.origin.print();
            actual_particle.origin = template_particle.coords;
//            actual_particle.origin.print();
            // these are current absolute coordinates of a particle
            actual_particle.coords.plusIP(this.center);
            this.particles.add(actual_particle);
        }
    }
}
