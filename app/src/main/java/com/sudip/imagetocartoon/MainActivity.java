package com.sudip.imagetocartoon;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public ImageView image;
    public Bitmap imgBitmap;
    public Button chooseImageButton;
    public Button getCartoonButton;
    public int height=128, width = 128;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = findViewById(R.id.imageView);
        chooseImageButton = findViewById(R.id.chooseImageButton);
        getCartoonButton = findViewById(R.id.getCartoonButton);
        imgBitmap = null;
    }
    public void openGallery(View view) {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 0);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                imgBitmap = Bitmap.createScaledBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage), height, width,false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            image.setImageBitmap(imgBitmap);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getCartoon(View view){
        ArrayList<ArrayList<Float>> im_array = new ArrayList<>();
        for (int x=0;x<width;x++){
            ArrayList<Float> row = new ArrayList<>();
            for (int y=0;y<height;y++){
                int pixel = imgBitmap.getPixel(x,y);
                int red = Color.red(pixel);
                int blue = Color.blue(pixel);
                int green = Color.green(pixel);
                Float bw = ((float)(red+blue+green))/765;
                row.add(bw);
            }
            im_array.add(row);
        }

        ArrayList<ArrayList<Float>> blur = new ArrayList<>();
        ArrayList<Float> blankRow = new ArrayList<>();
        for (int x=0; x<width;x++) {
            blankRow.add((float) 0);
        }
        blur.add(blankRow);
        for (int y=1; y<height-1;y++) {
            ArrayList<Float> row = new ArrayList<>();
            row.add((float)0);
            for (int x = 1; x < width-1; x++) {
                float lu = im_array.get(y-1).get(x-1);
                float mu = im_array.get(y-1).get(x);
                float ru = im_array.get(y-1).get(x+1);
                float lm = im_array.get(y).get(x-1);
                float mm = im_array.get(y).get(x);
                float rm = im_array.get(y).get(x+1);
                float ld = im_array.get(y+1).get(x-1);
                float md = im_array.get(y+1).get(x);
                float rd = im_array.get(y+1).get(x+1);

                float newPix = (lu+mu+ru+lm+mm+rm+ld+md+rd)/9;
                row.add(newPix);
            }
            row.add((float)0);
            blur.add(row);
        }
        blur.add(blankRow);

        ArrayList<ArrayList<Float>> edge = new ArrayList<>();
        for (int y=0; y<height; y++){
            ArrayList<Float> row = new ArrayList<>();
            for (int x=0; x<width; x++){
                float pix = im_array.get(y).get(x) - blur.get(y).get(x);
                row.add(pix);
            }
            edge.add(row);
        }



        ArrayList<Integer>flat = new ArrayList<>();
        for (int x=0; x<width; x++){
            for (int y=0; y<height; y++) {
                int pix = (int) (edge.get(y).get(x) * 255);
                flat.add(pix);
            }
        }
        int[] flatFinal = flat.stream().mapToInt(i -> i).toArray();
        Bitmap bmp = Bitmap.createBitmap(flatFinal, 128, 128, Bitmap.Config.ALPHA_8);

        Bitmap newBitmap = getCombinedBitmap(imgBitmap, bmp);
        image.setImageBitmap(newBitmap);
    }
    public Bitmap getCombinedBitmap(Bitmap b, Bitmap b2) {
        Bitmap drawnBitmap = null;

        try {
            drawnBitmap = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(drawnBitmap);
            canvas.drawBitmap(b, 0, 0, null);
            canvas.drawBitmap(b2, 0, 0, null);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return drawnBitmap;
    }
}