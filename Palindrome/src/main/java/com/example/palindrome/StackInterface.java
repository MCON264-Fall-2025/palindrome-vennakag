package com.example.palindrome;

public interface StackInterface<T>
{
    public void push(T t);
    public T pop();
    public T peek();
    boolean isEmpty();
}
