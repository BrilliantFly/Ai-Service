package com.know.knowboot.designpatterns.structural.bridgeinfo;

import com.know.knowboot.designpatterns.structural.bridgeinfo.phone.BrandM;
import com.know.knowboot.designpatterns.structural.bridgeinfo.phone.BrandN;
import com.know.knowboot.designpatterns.structural.bridgeinfo.phone.PhoneBrand;
import com.know.knowboot.designpatterns.structural.bridgeinfo.soft.AddressList;
import com.know.knowboot.designpatterns.structural.bridgeinfo.soft.Game;

public class JavaDemo {

    public static void main(String[] args) {

        PhoneBrand brand1 = new BrandM();
        brand1.setSoft(new Game());
        brand1.run();

        brand1.setSoft(new AddressList());
        brand1.run();

        PhoneBrand brand2 = new BrandN();
        brand2.setSoft(new Game());
        brand2.run();

        brand2.setSoft(new AddressList());
        brand2.run();


    }
}
