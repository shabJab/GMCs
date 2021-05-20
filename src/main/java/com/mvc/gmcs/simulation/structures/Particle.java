package com.mvc.gmcs.simulation.structures;

import com.mvc.gmcs.simulation.primitives.XYZ;

public class Particle {
//    private static int nextn;
//    int n;
    public char type;
    public XYZ origin;
    public XYZ coords = new XYZ(0,0,0);


    public char getType() { return this.type; }
    public double getX() { return this.coords.x; }
    public double getY() { return this.coords.y; }
    public double getZ() { return this.coords.z; }

    public void setType(char type) { this.type = type; }
    public void setX(double x) { this.coords.setX(x); }
    public void setY(double y) { this.coords.setY(y); }
    public void setZ(double z) { this.coords.setZ(z); }
//    public void setX0(double x) { this.origin.setX(x); }
//    public void setY0(double y) { this.origin.setY(y); }
//    public void setZ0(double z) { this.origin.setZ(z); }

    public void setCoords(XYZ coords) {
        setX(coords.getX());
        setY(coords.getY());
        setZ(coords.getZ());
    }
//    public void setOrigin(XYZ coords) {
//        setX0(coords.getX());
//        setY0(coords.getY());
//        setZ0(coords.getZ());
//    }

    public void print() {
        System.out.print("Type: " + this.type + " | Coords: ");
        this.coords.print();
    }

    public Particle copy() {
        Particle copy = new Particle();

        copy.type = this.type;
//        System.out.println("-------types------");
//        System.out.println(this.type + "\t" + copy.type);
//        copy.distance = this.distance;
//        copy.coords = new XYZ(this.coords.x, this.coords.y, this.coords.z);
        copy.setCoords(this.coords);
//        System.out.println("-------coords------");
//        this.coords.print();
//        copy.coords.print();
        copy.origin = this.origin;
//        System.out.println("-------origins------");
//        this.origin.print();
//        copy.origin.print();
//        copy.setOrigin(this.origin);
        return copy;
    }

//    public boolean fieldsEqualTo(Particle particle) {
//        if (this.type != particle.type) { return false; }
//        return this.coords.fieldsEqualTo(particle.coords);
//    }


    public Particle() {}

//    public Particle(char type, XYZ coords) {
//        this.type = type;
//        this.coords = coords;
//    }

    public Particle(char type, XYZ coords) {
        this.setType(type);
        this.setCoords(coords);
        this.origin = coords.copy();
//        coords.print();
//        this.setOrigin(coords);
    }

//    public Particle(char type, double longitude, double latitude, double distance) {
//        this.type = type;
//        this.longitude = longitude;
//        this.latitude = latitude;
//        this.distance = distance;
//        this.coords = new XYZ(Math.cos(longitude),
//                              Math.sin(longitude),
//                              Math.sin(latitude));
//        this.coords.timesIP(distance);
//    }
}
