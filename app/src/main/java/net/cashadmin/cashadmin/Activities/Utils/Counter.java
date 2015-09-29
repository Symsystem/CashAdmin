package net.cashadmin.cashadmin.Activities.Utils;

public class Counter {

    private float counter;

    public Counter(){
        counter = 0;
    }

    public Counter(int c){
        counter = c;
    }

    public Counter(float c){
        counter = c;
    }

    public void add(int i){
        this.counter += i;
    }

    public void add(float f){
        this.counter += f;
    }

    public void addOne(){
        this.counter += 1f;
    }

    public Integer getCounterInt(){
        return new Integer(Math.round(this.counter));
    }

    public Float getCounterFloat(){
        return new Float(this.counter);
    }
}
