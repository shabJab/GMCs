package com.mvc.gmcs.simulation.primitives;


public class XYZ {
    public double x,y,z;

    public double getX() {
        return this.x;
    }
    public double getY() {
        return this.y;
    }
    public double getZ() {
        return this.z;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public void setZ(double z) {
        this.z = z;
    }

    public XYZ() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }
    public XYZ(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public XYZ copy() { return new XYZ(this.x, this.y, this.z); }

    public void set(XYZ that) {
        this.x = that.x;
        this.y = that.y;
        this.z = that.z;
    }
    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public XYZ plus(XYZ that) {
        XYZ ret = new XYZ();
        ret.x = this.x + that.x;
        ret.y = this.y + that.y;
        ret.z = this.z + that.z;
        return ret;
    }
    public XYZ plus(double x, double y, double z) {
        XYZ ret = new XYZ();
        ret.x = this.x + x;
        ret.y = this.y + y;
        ret.z = this.z + z;
        return ret;
    }
    public XYZ plus(double A) {
        XYZ ret = new XYZ();
        ret.x = this.x + A;
        ret.y = this.y + A;
        ret.z = this.z + A;
        return ret;
    }
    public void plusIP(XYZ that) {
        this.x += that.x;
        this.y += that.y;
        this.z += that.z;
    }
    public void plusIP(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }
    public void plusIP(double A) {
        this.x += A;
        this.y += A;
        this.z += A;
    }

    public XYZ minus(XYZ that) {
        XYZ ret = new XYZ();
        ret.x = this.x - that.x;
        ret.y = this.y - that.y;
        ret.z = this.z - that.z;
        return ret;
    }
    public XYZ minus(double x, double y, double z) {
        XYZ ret = new XYZ();
        ret.x = this.x - x;
        ret.y = this.y - y;
        ret.z = this.z - z;
        return ret;
    }
    public XYZ minus(double A) {
        XYZ ret = new XYZ();
        ret.x = this.x - A;
        ret.y = this.y - A;
        ret.z = this.z - A;
        return ret;
    }
    public void minusIP(XYZ that) {
        this.x -= that.x;
        this.y -= that.y;
        this.z -= that.z;
    }
    public void minusIP(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
    }
    public void minusIP(double A) {
        this.x -= A;
        this.y -= A;
        this.z -= A;
    }
    public XYZ times(double A) {
        XYZ ret = new XYZ();
        ret.x = this.x * A;
        ret.y = this.y * A;
        ret.z = this.z * A;
        return ret;
    }
    public void timesIP(double A) {
        this.x *= A;
        this.y *= A;
        this.z *= A;
    }

    public boolean fieldsEqualTo(XYZ that) {
        return (this.x == that.x && this.y == that.y && this.z == that.z);
    }
    public void print() {
        System.out.println("(\t" + this.x + "\t|\t" + this.y + "\t|\t" + this.z + "\t)");
    }

//    public XYZ dot(double x, double y, double z) {
//        XYZ ret = new XYZ();
//        ret.x = this.x * x;
//        ret.y = this.y * y;
//        ret.z = this.z * z;
//        return ret;
//    }

    public double distance(XYZ that) {
        double dx = this.x - that.x;
        double dy = this.y - that.y;
        double dz = this.z - that.z;
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }
    public double distance2(XYZ that) {
        double dx = this.x - that.x;
        double dy = this.y - that.y;
        double dz = this.z - that.z;
        return dx*dx + dy*dy + dz*dz;
    }

    public double norm() {
        return Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
    }
    public double norm2() {
        return this.x*this.x + this.y*this.y + this.z*this.z;
    }
    public void normalize() {
        double norm = this.norm();
        this.x /= norm;
        this.y /= norm;
        this.z /= norm;
    }

//    public void rotateIP(Quaternion rotation) {
//        Quaternion rotated = rotation.multiply(this).multiply(rotation.conjugate());
//        this.setX(rotated.getX());
//        this.setY(rotated.getY());
//        this.setZ(rotated.getZ());
//    }
    public XYZ rotate(Quaternion rotation) {
        if (this.norm2()!=0) {
            // !!!! all Quaternion normalizations should be set manually
            // otherwise everything will be ruined due to reuse of the constructor
            // fucked up portion will then be in this multiplication below
            Quaternion rotatedQ = rotation.multiply(this).multiply(rotation.conjugate());
            return new XYZ(rotatedQ.getX(), rotatedQ.getY(), rotatedQ.getZ());
        } else { return this; }

    }
//    public void rotate(double w, double x, double y, double z) {
//        Quaternion quaternion = new Quaternion(w,x,y,z);
//        Quaternion rotated = quaternion.multiply(this).multiply(quaternion.conjugate());
//        this.setX(rotated.getX());
//        this.setY(rotated.getY());
//        this.setZ(rotated.getZ());
//    }
//    public void rotate(double angle, XYZ direction) {
//        Quaternion quaternion = new Quaternion(angle, direction);
//        Quaternion rotated = quaternion.multiply(this).multiply(quaternion.conjugate());
//        this.setX(rotated.getX());
//        this.setY(rotated.getY());
//        this.setZ(rotated.getZ());
//    }
}
