package com.example.flappybird_prm391;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

public class NumberDisplayController {

    Bitmap[] smallNums = new Bitmap[10];
    Bitmap[] bigNums = new Bitmap[10];

    public NumberDisplayController(Resources resources) {
        smallNums[0] = BitmapFactory.decodeResource(resources, R.drawable.num0_small);
        smallNums[1] = BitmapFactory.decodeResource(resources, R.drawable.num1_small);
        smallNums[2] = BitmapFactory.decodeResource(resources, R.drawable.num2_small);
        smallNums[3] = BitmapFactory.decodeResource(resources, R.drawable.num3_small);
        smallNums[4] = BitmapFactory.decodeResource(resources, R.drawable.num4_small);
        smallNums[5] = BitmapFactory.decodeResource(resources, R.drawable.num5_small);
        smallNums[6] = BitmapFactory.decodeResource(resources, R.drawable.num6_small);
        smallNums[7] = BitmapFactory.decodeResource(resources, R.drawable.num7_small);
        smallNums[8] = BitmapFactory.decodeResource(resources, R.drawable.num8_small);
        smallNums[9] = BitmapFactory.decodeResource(resources, R.drawable.num9_small);
        bigNums[0] = BitmapFactory.decodeResource(resources, R.drawable.num0);
        bigNums[1] = BitmapFactory.decodeResource(resources, R.drawable.num1);
        bigNums[2] = BitmapFactory.decodeResource(resources, R.drawable.num2);
        bigNums[3] = BitmapFactory.decodeResource(resources, R.drawable.num3);
        bigNums[4] = BitmapFactory.decodeResource(resources, R.drawable.num4);
        bigNums[5] = BitmapFactory.decodeResource(resources, R.drawable.num5);
        bigNums[6] = BitmapFactory.decodeResource(resources, R.drawable.num6);
        bigNums[7] = BitmapFactory.decodeResource(resources, R.drawable.num7);
        bigNums[8] = BitmapFactory.decodeResource(resources, R.drawable.num8);
        bigNums[9] = BitmapFactory.decodeResource(resources, R.drawable.num9);
    }

    public List<Bitmap> smallNum2Display(String num){
        List<Bitmap> result = new ArrayList(num.length());
        try {
            Integer.parseInt(num);
        } catch (NumberFormatException ex) {
            return null;
        }
        for (int i = 0; i < num.length(); i++) {
            int des = num.charAt(i) - 48;
            result.add(i, smallNums[des]);
        }
        return result;
    }

    public List<Bitmap> bigNum2Display(String num){
        List<Bitmap> result = new ArrayList(num.length());
        try {
            Integer.parseInt(num);
        } catch (NumberFormatException ex) {
            return null;
        }
        for (int i = 0; i < num.length(); i++) {
            int des = num.charAt(i) - 48;
            result.add(i, bigNums[des]);
        }
        return result;
    }
}
