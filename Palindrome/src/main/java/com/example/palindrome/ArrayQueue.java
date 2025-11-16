package com.example.palindrome;

public class ArrayQueue<T> implements QueueInterface<T> {
    private T[] elements;
    public final int CAPACITY = 100;
    private int front = 0;
    private int rear;
    private int size;
    private int capacity;
    public ArrayQueue(int capacity) {
        elements = (T[]) new Object[capacity];
        rear = capacity -1;
        this.capacity = capacity;
    }
    public ArrayQueue() {
        elements = (T[]) new Object[CAPACITY];
        rear = CAPACITY - 1;
        capacity = CAPACITY;
    }
    @Override
    public void enqueue(T t) {
        rear = (rear + 1) % capacity;
        elements[rear] = t;
        size++;
    }
    @Override
    public T dequeue() {
        T elem = elements[front];
        elements[front] = null;
        front = (front + 1) % CAPACITY;
        size--;
        return elem;
    }
    @Override
    public T peek() {
        return elements[front];
    }
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

}

