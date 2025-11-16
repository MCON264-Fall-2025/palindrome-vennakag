package com.example.palindrome;

public interface QueueInterface<T> {
    public boolean isEmpty();
    public void enqueue(T t);
    public T dequeue();
    public T peek();
}
