package com.schrondinger.quin;

import java.text.DecimalFormat;

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2019/7/27 2:47 PM
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class Test {
    private static int i=1;
    public int getNext () {
        return i++;
    }
    public static void main(String[ ] args) {
//        Test test=new Test ();
//        Test testObject=new Test ();
//        test.getNext ();
//        testObject.getNext ();
//        System.out.println(testObject.getNext ());


        double sale = 2.04;
        DecimalFormat df = new DecimalFormat("#0.00");
        System.out.println(df.format(sale*100));


    }
}
