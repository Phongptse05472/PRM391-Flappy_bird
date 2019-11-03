package com.example.flappybird_prm391;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class NumberDisplayController {

    private int[] smallNumIds = {
            R.drawable.num0_small,
            R.drawable.num1_small,
            R.drawable.num2_small,
            R.drawable.num3_small,
            R.drawable.num4_small,
            R.drawable.num5_small,
            R.drawable.num6_small,
            R.drawable.num7_small,
            R.drawable.num8_small,
            R.drawable.num9_small
    };

    private int[] bigNumIds = {
            R.drawable.num0,
            R.drawable.num1,
            R.drawable.num2,
            R.drawable.num3,
            R.drawable.num4,
            R.drawable.num5,
            R.drawable.num6,
            R.drawable.num7,
            R.drawable.num8,
            R.drawable.num9
    };

    private Bitmap[] smallNums = new Bitmap[10];
    private Bitmap[] bigNums = new Bitmap[10];

    public NumberDisplayController(){

    }

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

    List<Bitmap> smallNum2Display(String num){
        List<Bitmap> result = new ArrayList<>(num.length());
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

    List<Bitmap> bigNum2Display(String num){
        List<Bitmap> result = new ArrayList<>(num.length());
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

    List<ImageView> smallNum2Display(Context context, String num){
        List<ImageView> result = new ArrayList<>(num.length());
        try {
            Integer.parseInt(num);
        } catch (NumberFormatException ex) {
            return null;
        }
        for (int i = 0; i < num.length(); i++) {
            int des = num.charAt(i) - 48;
            ImageView image = new ImageView(context);
            image.setImageResource(smallNumIds[des]);
            image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            result.add(i, image);
        }
        return result;
    }
}
