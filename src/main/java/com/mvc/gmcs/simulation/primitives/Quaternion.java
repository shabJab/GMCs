package com.mvc.gmcs.simulation.primitives;

public class Quaternion extends XYZ{
    double angle;
    double w;


    public Quaternion() {
        this.setW((2.0*Math.random()-1.0));
        this.setX((2.0*Math.random()-1.0));
        this.setY((2.0*Math.random()-1.0));
        this.setZ((2.0*Math.random()-1.0));
        // !!!! all Quaternion normalizations should be set manually
        // otherwise everything will be ruined due to reuse of the constructor
//        this.normalize();
    }

    public Quaternion(double w, double x, double y, double z) {
        this.setW(w);
        this.setX(x);
        this.setY(y);
        this.setZ(z);
        // !!!! all Quaternion normalizations should be set manually
        // otherwise everything will be ruined due to reuse of the constructor
//        this.normalize();
    }
    public Quaternion(double half_angle, XYZ axis) {
        axis.normalize();
        this.w = Math.cos(half_angle);
        double sin = Math.sin(half_angle);
        this.x = axis.getX()*sin;
        this.y = axis.getY()*sin;
        this.z = axis.getZ()*sin;
        this.normalize();
        this.print();
    }

    @Override
    public void print() {
        System.out.println("(\t" + this.w + "\t|\t" + this.x + "\t|\t" + this.y + "\t|\t" + this.z + "\t)");
    }

    public double getW() { return this.w; }
    public void setW(double w) { this.w = w; }

    @Override
    public double norm() { return Math.sqrt(w*w + x*x + y*y + z*z); }
    @Override
    public double norm2() { return (w*w + x*x + y*y + z*z); }
    @Override
    public void normalize() {
        double norm = this.norm2();
        if (norm != 0 && norm != 1) {
            norm = 1/Math.sqrt(norm);
            this.w *= norm;
            this.x *= norm;
            this.y *= norm;
            this.z *= norm;
        }
    }

    public Quaternion conjugate() { return new Quaternion(this.w, -this.x, -this.y, -this.z); }

    public void multiplyIP(Quaternion that) {
        // Components of the product.
        double w = this.w * that.w - this.x * that.x - this.y * that.y - this.z * that.z;
        double x = this.w * that.x + this.x * that.w + this.y * that.z - this.z * that.y;
        double y = this.w * that.y - this.x * that.z + this.y * that.w + this.z * that.x;
        double z = this.w * that.z + this.x * that.y - this.y * that.x + this.z * that.w;
        this.setW(w);
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    public Quaternion multiply(Quaternion that) {
        // Components of the product.
        double w = this.w * that.w - this.x * that.x - this.y * that.y - this.z * that.z;
        double x = this.w * that.x + this.x * that.w + this.y * that.z - this.z * that.y;
        double y = this.w * that.y - this.x * that.z + this.y * that.w + this.z * that.x;
        double z = this.w * that.z + this.x * that.y - this.y * that.x + this.z * that.w;
        return new Quaternion(w, x, y, z);
    }
    public Quaternion multiply(XYZ that) {
        // Components of the product.
        double w =-this.x * that.x - this.y * that.y - this.z * that.z;
        double x = this.w * that.x + this.y * that.z - this.z * that.y;
        double y = this.w * that.y - this.x * that.z + this.z * that.x;
        double z = this.w * that.z + this.x * that.y - this.y * that.x;
        return new Quaternion(w, x, y, z);
    }
    public static Quaternion multiply(Quaternion q1, Quaternion q2) {
        // Components of the first quaternion.
        final double q1a = q1.getW();
        final double q1b = q1.getX();
        final double q1c = q1.getY();
        final double q1d = q1.getZ();
        // Components of the second quaternion.
        final double q2a = q2.getW();
        final double q2b = q2.getX();
        final double q2c = q2.getY();
        final double q2d = q2.getZ();
        // Components of the product.
        final double w = q1a * q2a - q1b * q2b - q1c * q2c - q1d * q2d;
        final double x = q1a * q2b + q1b * q2a + q1c * q2d - q1d * q2c;
        final double y = q1a * q2c - q1b * q2d + q1c * q2a + q1d * q2b;
        final double z = q1a * q2d + q1b * q2c - q1c * q2b + q1d * q2a;
        return new Quaternion(w, x, y, z);
    }

    @Override
    public Quaternion copy() {
        return new Quaternion(this.w, this.x, this.y, this.z);
    }

}
