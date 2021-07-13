package com.overloadedllama.leapingllama.jsonUtil;

public class QueueObject implements Comparable {
    private final String classObject;
    private final double x;
    private final double length;
    private final int numItem;

    public QueueObject(String classObject, double x, double length, int numItem) {
        this.classObject = classObject;
        this.x = x;
        this.length = length;
        this.numItem = numItem;
    }

    public String getClassObject() {
        return classObject;
    }

    public double getX() {
        return x;
    }

    public double getLength() {
        return length;
    }

    public int getNumItem() {
        return numItem;
    }

    @Override
    public String toString() {
        return "QueueObject{" +
                "classObject='" + classObject + '\'' +
                ", x=" + x +
                ", length=" + length +
                ", numItem=" + numItem +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof QueueObject)) {
            return 0;
        }

        if (((QueueObject) o).x < x) {
            return 1;
        } else {
            return -1;
        }
    }
}
