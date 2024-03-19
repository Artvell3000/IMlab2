package com.example;

import java.util.Random;

public class Model {

    Random rnd = new Random();
    private final double k = 0.02;
    private double x=0, y;

    public class Cord{
        public double x;
        public double y;
        Cord(double x, double y){
            this.x = x;
            this.y = y;
        }
    }

    Model(double price){
        this.y = price;
    }

    public void setPrice(double y){
        this.y = y;
    }

    public Cord getNowState(){
        return new Cord(x,y);
    }

    public Cord getNextState(){
        y = y * (1 + k * (rnd.nextDouble() - 0.5));
        x+=1;
        return new Cord(x, y);
    }
}
