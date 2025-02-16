package org.jaysabva.woc_crs.entity;

public enum Grade {
    AA(10),
    AB(9),
    BB(8),
    BC(7),
    CC(6),
    CD(5),
    DD(4),
    P(0),
    F(0),
    I(0);

    private final int gradePoint;

    Grade(int gradePoint) {
        this.gradePoint = gradePoint;
    }

    public int getGradePoint() {
        return gradePoint;
    }

    public boolean isPassing() {
        return this != F && this != I;
    }
}
