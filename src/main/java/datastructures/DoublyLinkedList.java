package datastructures;

import model.Song;

public class DoublyLinkedList {

    private Node head;
    private Node tail;
    private Node current;
    private int size;

    private static class Node {
        Song song;
        Node next;
        Node prev;

        Node(Song song) {
            this.song = song;
        }
    }

    public void add(Song song) {
        Node newNode = new Node(song);
        if (head == null) {
            head = newNode;
            tail = newNode;
            current = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        tail.next = head;
        head.prev = tail;
        size++;
    }

    public Song next() {
        if (current == null) return null;
        current = current.next;
        return current.song;
    }

    public Song previous() {
        if (current == null) return null;
        current = current.prev;
        return current.song;
    }

    public Song getCurrent() {
        if (current == null) return null;
        return current.song;
    }

    public void remove(Song song) {
        Node temp = head;
        for (int i = 0; i < size; i++) {
            if (temp.song.getId() == song.getId()) {
                if (size == 1) {
                    head = null; tail = null; current = null;
                } else {
                    temp.prev.next = temp.next;
                    temp.next.prev = temp.prev;
                    if (temp == head) head = temp.next;
                    if (temp == tail) tail = temp.prev;
                    if (temp == current) current = temp.next;
                }
                size--;
                return;
            }
            temp = temp.next;
        }
    }// Seçilen şarkıyı listede bulup 'current' ibresini o düğüme eşitleyen metot
    public void setCurrentBySong(Song song) {
        if (head == null || song == null) return;

        Node temp = head;
        for (int i = 0; i < size; i++) {
            if (temp.song.getId() == song.getId()) {
                this.current = temp; // İbreyi bulduğumuz şarkı düğümüne eşitliyoruz
                return;
            }
            temp = temp.next;
        }
    }

    public int getSize() { return size; }
    public boolean isEmpty() { return size == 0; }
}