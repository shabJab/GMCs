package com.mvc.gmcs.simulation.structures;


import com.mvc.gmcs.simulation.primitives.Quaternion;
import com.mvc.gmcs.simulation.primitives.XYZ;

public class ParticleBuilder {
        private char _type='C'; // default - Core
        private XYZ _coords = new XYZ(0,0,0);
        private Quaternion _rotation;


        public ParticleBuilder() { }


        public Particle build() {
        return new Particle(_type, _coords);
    }

        public ParticleBuilder type(char _type) {
            this._type = _type;
            return this;
        }

        public ParticleBuilder place(double x, double y, double z) {
            this._coords.setX(x);
            this._coords.setY(y);
            this._coords.setZ(z);
            return this;
        }
//        public ParticleBuilder place(XYZ coords) {
//            this._coords.setX(coords.getX());
//            this._coords.setY(coords.getY());
//            this._coords.setZ(coords.getZ());
//            return this;
//        }

//        public ParticleBuilder rotate(double w, double x, double y, double z) {
//            this._rotation = new Quaternion(w,x,y,z);
//            this._coords.rotateIP(this._rotation);
//            return this;
//        }
//        public ParticleBuilder rotate(Quaternion rotation) {
//            this._rotation = rotation;
//            this._coords.rotateIP(rotation);
//            return this;
//        }

//        public ParticleBuilder coordinates(XYZ _coords) {
//            this._coords = _coords;
//            return this;
//        }
}
