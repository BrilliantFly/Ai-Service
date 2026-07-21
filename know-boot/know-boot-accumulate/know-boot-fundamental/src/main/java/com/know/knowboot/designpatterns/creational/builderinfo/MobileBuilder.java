package com.know.knowboot.designpatterns.creational.builderinfo;

public class MobileBuilder extends Builder {

    @Override
    public void buildFrame() {
        bike.setFrame("碳纤维车架");
    }

    @Override
    public void buildSeat() {
        bike.setSeat("真皮");
    }

    @Override
    public Bike createBike() {

        return bike;
    }

}
