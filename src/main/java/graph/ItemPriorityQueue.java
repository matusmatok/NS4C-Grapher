/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import graph.components.Edge;
import graph.components.Face;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class ItemPriorityQueue {
    
    private final ArrayList<Item> queue = new ArrayList<>();
    
    class Item{
        public Item previous;
        public Item following;

        public LinkedList<Integer> items;
        public LinkedList<Edge> associatedEdges;
        public ArrayList<Integer> stack;
        
        public int queuePos;
        
        public Item(Item p, Item f, int id, Edge e){
            this.items = new LinkedList<>();
            this.associatedEdges = new LinkedList<>();
            this.stack = new ArrayList<>();
            this.stack.add(id);
            this.items.add(id);
            this.associatedEdges.add(e);
            this.previous = p;
            this.following = f;
            this.queuePos = -1;
        }
        
        public int size(){
            return this.items.size();
        }
        
        public void append(int id, Edge e){
            this.items.add(id);
            this.associatedEdges.add(e);
            this.stack.add(id);
        }
        
        public void appstart(int id, Edge e){
            this.items.addFirst(id);
            this.associatedEdges.addFirst(e);
            this.stack.add(id);
        }
        
        public void removeFollowingAndMerge(){
            if (this != this.following.following){
                this.items.addAll(this.following.following.items);
                this.associatedEdges.addAll(this.following.following.associatedEdges);
                this.merge(this.stack, this.following.following.stack);
                this.following = this.following.following.following;
                this.following.previous = this;
            }
        }
        
        //return true if this item is higher priority if the other item
        public boolean compare(Item other){
            int len = stack.size() < other.stack.size() ? stack.size() : other.stack.size();
            
            for(int i = 0; i< len; i++){
                if (stack.get(i) < other.stack.get(i)) return true;
                if (stack.get(i) > other.stack.get(i)) return false;
            }
            
            if (stack.size() == other.stack.size() && this.associatedEdges.get(0).getUntagedFace().getPriority() != other.associatedEdges.get(0).getUntagedFace().getPriority()) {
                return this.associatedEdges.get(0).getUntagedFace().getPriority() > other.associatedEdges.get(0).getUntagedFace().getPriority() ? true : false;
            }   
            return stack.size() < other.stack.size() ? false : true;
        }
        
        private void merge(List<Integer> l1, List<Integer> l2) {
            for (int index1 = 0, index2 = 0; index2 < l2.size(); index1++) {
                if (index1 == l1.size() || l1.get(index1) > l2.get(index2)) {
                    l1.add(index1, l2.get(index2++));
                }
            }
        }      
    }
    
    public boolean hasNext(){
        return queue.size() > 0;
    }
    
    public Face peakNext(){
        return queue.get(0).associatedEdges.get(0).getUntagedFace();
    }
    
    public void addFace(int id, Face f){
        int deg = f.size();
        f.setTagged(true);

        if (queue.size() == 0){
            for (int i=0; i< deg; i++){
                if (i == 0){
                    queue.add(new Item(null, null, 0,f.getAdjacentEdges().get(0)));
                }
                else{
                    queue.add(new Item(queue.get(queue.size() - 1), queue.get(0), 0, f.getAdjacentEdges().get(i)));
                    queue.get(i-1).following = queue.get(i);
                }
                queue.get(i).queuePos = i;
            }
            queue.get(0).previous = queue.get(queue.size() -1);
            for (int i=0; i< queue.size(); i++) bubbleUp(i);
            //printDegreesOfUntagged();
            
        }
        else{
            Item ex = queue.get(0);
            int indexOfTheFirstEdgeInF = f.getIndexOfE(ex.associatedEdges.get(0));
            int sizeOfF = f.size();
            
            if (indexOfTheFirstEdgeInF < 0) System.err.println("Edge not found");
            
            if (queue.get(0).size() == deg){
                while(ex.previous.size() == 1 && ex.following.size() == 1 && ex.previous.associatedEdges.get(0) == ex.following.associatedEdges.get(0)){
                    this.remove(ex.following.queuePos);
                    this.remove(ex.previous.queuePos);
                    ex.previous = ex.previous.previous;
                    ex.following = ex.following.following;
                }
                
                ex.previous.following = ex.following;
                ex.following.previous = ex.previous;
                removeTop();
                
            }
            else if (queue.get(0).size() + 1 == deg){
                
                if (ex.previous != ex.following){
                    
                    this.remove(ex.following.queuePos);
                    ex.previous.append(id, f.getEdge((indexOfTheFirstEdgeInF +  1)%sizeOfF));
                    ex.previous.removeFollowingAndMerge();
                    this.removeTop();
                    this.bubbleUp(ex.previous.queuePos);
                    
                }
                else{ 
                    
                    ex.previous.append(id, f.getEdge((indexOfTheFirstEdgeInF +  1)%sizeOfF));
                    ex.previous.following = ex.previous;
                    ex.following.previous = ex.following;
                    this.removeTop();
                    this.bubbleUp(ex.previous.queuePos);
                }
 
            }
            else if (queue.get(0).size() + 2 <= deg){
                
                int offset = f.size() - ex.size();
                ex.previous.append(id, f.getEdge((indexOfTheFirstEdgeInF + 1)%sizeOfF));
                ex.following.appstart(id, f.getEdge((indexOfTheFirstEdgeInF + offset)%sizeOfF));
                
                Item current = ex.previous;
                
                for (int i = 2; i < offset; i++){
                    Item newItem = new Item(current, null, id, f.getEdge((indexOfTheFirstEdgeInF + i)%sizeOfF));
                    queue.add(newItem);
                    newItem.queuePos = queue.size() - 1;
                    current.following = newItem;
                    current = newItem;
                }
                
                current.following = ex.following;
                ex.following.previous = current;
                
                //rearange the queue
                this.removeTop();
                this.bubbleUp(ex.previous.queuePos);
                this.bubbleUp(ex.following.queuePos);
            }
        }
        
    }
    
    //only removes the leading item from the heap, does not rearange the linked list of elements.
    private void removeTop(){
        this.remove(0);
    }
    
    private void remove(int pos){
        queue.set(pos,queue.get(queue.size() - 1));
        queue.remove(queue.size() - 1);
        if (pos < queue.size()) queue.get(pos).queuePos = pos;
        
        while (!(pos*2 + 2 > queue.size())){
            int nextpos = pos*2 + 1;
            if (!(pos*2 + 3 > queue.size())){
                if (queue.get(nextpos+1).compare(queue.get(nextpos))){
                    nextpos += 1;
                }
            }
            if (queue.get(nextpos).compare(queue.get(pos))){
                Collections.swap(queue, pos, nextpos);
                queue.get(pos).queuePos = pos;
                queue.get(nextpos).queuePos = nextpos;
                pos = nextpos;
            }
            else break;
        }  
    }
    
    private void bubbleUp(int pos){
        while(pos > 0){
            int nextPos = (pos - 1) / 2;
            if (queue.get(pos).compare(queue.get(nextPos))){
                Collections.swap(queue, pos, nextPos);
                queue.get(pos).queuePos = pos;
                queue.get(nextPos).queuePos = nextPos;
                pos = nextPos;
            }
            else break;
        }
    }
    
    public void printDegreesOfUntagged(){
        for(Item i: this.queue){
            System.out.print(i.associatedEdges.get(0).getUntagedFace().getId() + " ");
        }
        System.out.println();
        
        for(Item i: this.queue){
            System.out.print(i.associatedEdges.get(0).getUntagedFace().size() + " ");    
        }
        System.out.println();
        
        for(Item i: this.queue){         
            System.out.print(i.associatedEdges.get(0).getUntagedFace().getPriority() + " ");
        }
        System.out.println();
    }
    
    public void printQueue(){
                
        System.out.println(queue.size());
        try{
            Item start = queue.get(0);
            System.out.print(start.items);

            Item next = start.following;
            while (next != start){
                System.out.print(next.items);
                next = next.following;
            }
        }
        catch(IndexOutOfBoundsException e){
            System.out.println("EMPTY QUEUE");
        }
        System.out.println();
        for(Item i: this.queue){
            System.out.print(i.items);
        }
        System.out.println();
        for(Item i: this.queue){
            System.out.print(i.queuePos + " ");
        }
        System.out.println("\nEdge IDs: ");
        
        try{
            Item start = queue.get(0);
            System.out.print("[");
                for (Edge e: start.associatedEdges){
                    System.out.print(e.getId() + " ");
                }
                System.out.print("] ");

            Item next = start.following;
            while (next != start){
                System.out.print("[");
                for (Edge e: next.associatedEdges){
                    System.out.print(e.getId() + " ");
                }
                System.out.print("] ");
                next = next.following;
            }
        }
        catch(IndexOutOfBoundsException e){
            System.out.println("EMPTY QUEUE");
        }
        System.out.println();
    }
}
