package com.example.flappybird_prm391.common;

public class Tuple3<A, B, C> {
    private A a;
    private B b;
    private C c;

    public Tuple3(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Tuple3() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple3<?, ?, ?> tuple = (Tuple3<?, ?, ?>) o;
        if (!a.equals(tuple.a)) return false;
        if (!b.equals(tuple.b)) return false;
        return c.equals(tuple.c);
    }

    @Override
    public int hashCode() {
        int result = a.hashCode();
        result = 31 * result + b.hashCode();
        result = 2 * result + c.hashCode();
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
}
