package com.swooli.extraction.html.media;

public class Tuple<A, B> {

    private A firstObject;

    private B secondObject;

    public Tuple() {}

    public Tuple(final A firstObject, final B secondObject) {
        this.firstObject = firstObject;
        this.secondObject = secondObject;
    }

    public A getFirstObject() {
        return firstObject;
    }

    public void setFirstObject(final A firstObject) {
        this.firstObject = firstObject;
    }

    public B getSecondObject() {
        return secondObject;
    }

    public void setSecondObject(final B secondObject) {
        this.secondObject = secondObject;
    }

}