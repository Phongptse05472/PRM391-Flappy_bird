package com.example.flappybird_prm391.common;

public class Tuple4<A, B, C, D> {
    private A a;
    private B b;
    private C c;
    private D d;

    public Tuple4(A a, B b, C c, D d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public Tuple4() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple4<?, ?, ?, ?> tuple = (Tuple4<?, ?, ?, ?>) o;
        if (!a.equals(tuple.a)) return false;
        if (!b.equals(tuple.b)) return false;
        if (!c.equals(tuple.c)) return false;
        return d.equals(tuple.d);
    }

    @Override
    public int hashCode() {
        int result = a.hashCode();
        result = 31 * result + b.hashCode();
        result = 2 * result + c.hashCode();
        result = 5 * result + d.hashCode();
        return result;
    }

    public A getItem1() {
        return a;
    }

    public B getItem2() {
        return b;
    }

    public void setItem1(A a) {
        this.a = a;
    }

    public void setItem2(B b) {
        this.b = b;
    }

    public C getItem3() {
        return c;
    }

    public void setItem3(C c) {
        this.c = c;
    }

    public D getItem4() {
        return d;
    }

    public void setItem4(D d) {
        this.d = d;
    }
}
