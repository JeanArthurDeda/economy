package core.geometry;

import core.MathExt;
import core.seri.Seri;

public class Location implements Seri {
    public static final double WORLD_EDGE_SIZE = 2.0f;
    public static final double WORLD_DIAGONAL_SIZE = Math.sqrt(8.0);
    public static final double WORLD_MIN = -1.0;
    public static final double WORLD_MAX = -1.0;

    public double mX;
    public double mY;

    public Location(){
    }

    public Location(double x, double y) {
        mX = x;
        mY = y;
    }

    public Location(Location other) {
        mX = other.getX();
        mY = other.getY();
    }

    public Location setX (double value){
        mX = value;
        return this;
    }

    public Location setY (double value){
        mY = value;
        return this;
    }

    public Location set(double locationX, double locationY) {
        mX = locationX;
        mY = locationY;
        return this;
    }

    public Location set(Location other) {
        mX = other.getX();
        mY = other.getY();
        return this;
    }

    public Location setWorldRatio (double xRatio, double yRatio){
        mX = xRatio * 2.0 - 1.0;
        mY = yRatio * 2.0 - 1.0;
        return this;
    }

    public double getX () {
        return mX;
    }

    public double getY () {
        return mY;
    }

    public double dist(double locationX, double locationY){
        double x = mX - locationX;
        double y = mY - locationY;
        return Math.sqrt(x * x + y * y);
    }

    public double dist(Location other){
        return dist (other.getX(), other.getY());
    }

    public double distSq(double locationX, double locationY){
        double x = mX - locationX;
        double y = mY - locationY;
        return x * x + y * y;
    }

    public double distSq(Location other){
        return distSq (other.getX(), other.getY());
    }

    public double dot (double locationX, double locationY){
        return getX() * locationX + getY() * locationY;
    }

    public double dot (Location other){
        return dot (other.getX(), other.getY());
    }

    public double cross (double locationX, double locationY){
        return getX() * locationY - locationX * getY();
    }

    public double cross (Location other){
        return cross(other.getX(), other.getY());
    }

    public Location normalize() {
        double ool = 1.0f / Math.sqrt (mX * mX + mY * mY);
        mX *= ool;
        mY *= ool;
        return this;
    }

    public Location rotate90() {
        double t = mX;
        mX = mY;
        mY = -t;
        return this;
    }

    public Location add (double locationX, double locationY){
        mX += locationX;
        mY += locationY;
        return this;
    }

    public Location add (Location other){
        return add (other.getX(), other.getY());
    }

    public Location sub (double locationX, double locationY){
        mX -= locationX;
        mY -= locationY;
        return this;
    }

    public Location sub (Location other){
        return sub (other.getX(), other.getY());
    }

    public Location negate (){
        mX = -mX;
        mY = -mY;
        return this;
    }

    public Location scale(double value){
        mX *= value;
        mY *= value;
        return this;
    }

    public Location scale(double locationX, double locationY){
        mX *= locationX;
        mY *= locationY;
        return this;
    }

    public Location jitter (double sizeX, double sizeY){
        return add ((MathExt.random() - 0.5) * sizeX, (MathExt.random() - 0.5) * sizeY);
    }

}
