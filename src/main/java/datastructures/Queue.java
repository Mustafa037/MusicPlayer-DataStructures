package datastructures;

import model.Song;

public class Queue {

    private Node head;
    private Node tail;
    private int size;

    private static class Node {
        Song song;
        Node next;

        Node(Song song) {
            this.song = song;
        }
    }

    public void enqueue(Song song) {
        Node newNode = new Node(song);
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public Song dequeue() {
        if (head == null) return null;
        Song song = head.song;
        head = head.next;
        if (head == null) tail = null;
        size--;
        return song;
    }

    public Song peek() {
        if (head == null) return null;
        return head.song;
    }

    public boolean isEmpty() { return size == 0; }
    public int getSize() { return size; }
}