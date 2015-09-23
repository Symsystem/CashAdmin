package net.cashadmin.cashadmin.Activities.Utils;

public class Counter {

    private int counter;

    public Counter(){
        counter = 0;
    }

    public Counter(int c){
        counter = c;
    }

    public void add(int i){
        this.counter += i;
    }

    public void addOne(){
        this.counter++;
    }

    public Integer getCounter(){
        return new Integer(this.counter);
    }
}
