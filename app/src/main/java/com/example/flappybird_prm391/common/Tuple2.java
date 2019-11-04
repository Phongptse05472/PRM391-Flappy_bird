package com.example.flappybird_prm391.common;

public class Tuple2<A, B> {

    private A a;
    private B b;

    public Tuple2(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public Tuple2() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple2<?, ?> tuple = (Tuple2<?, ?>) o;
        if (!a.equals(tuple.a)) return false;
        return b.equals(tuple.b);
    }

    @Override
    public int hashCode() {
        int result = a.hashCode();
        result = 31 * result + b.hashCode();
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
}