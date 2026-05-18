package datastructures;

import model.Song;

public class Stack {

    private Node top;
    private int size;

    private static class Node {
        Song song;
        Node next;

        Node(Song song) {
            this.song = song;
        }
    }

    public void push(Song song) {
        Node newNode = new Node(song);
        newNode.next = top;
        top = newNode;
        size++;
    }

    public Song pop() {
        if (top == null) return null;
        Song song = top.song;
        top = top.next;
        size--;
        return song;
    }

    public Song peek() {
        if (top == null) return null;
        return top.song;
    }

    public boolean isEmpty() { return size == 0; }
    public int getSize() { return size; }
}