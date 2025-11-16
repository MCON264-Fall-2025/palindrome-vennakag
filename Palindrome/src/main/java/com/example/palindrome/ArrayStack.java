package com.example.palindrome;
import java.util.Stack;

public class ArrayStack<T> implements StackInterface<T> {
    final int DEFCAP = 100;
    T[] elements;
    int topIndex = -1;

    public ArrayStack() {
        elements = (T[]) new Object[DEFCAP];
    }

    public ArrayStack(int capacity) {
        elements = (T[]) new Object[capacity];
    }

    public void push(T t) {
        topIndex++;
        elements[topIndex] = t;
    }

    public T pop() {
        T t = null;
        if(!isEmpty()){
            t = elements[topIndex];
            elements[topIndex] = null;
            topIndex--;
        }
        return t;
    }

    public T peek() {
        T t = null;
        if(!isEmpty()){
            t = elements[topIndex];
        }
        return t;
    }

    @Override
    public boolean isEmpty() {
        return topIndex == -1;
    }
}
