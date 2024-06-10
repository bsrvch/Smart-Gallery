package com.example.tf1;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.tf1.ml.F;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.gpu.GpuDelegate;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.gpu.CompatibilityList;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.ops.CastOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;
import org.tensorflow.lite.support.model.Model;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UpscaleActivity extends AppCompatActivity {
    private static final int PICK_PHOTO_CODE = 1;
    String path = "FSRCNN50 (1).tflite";
    Interpreter interpreter;
    ImageView lrr;
    ImageView hrr;
    Bitmap lrImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upscale_activity);
        Button startBtn = (Button)findViewById(R.id.button_start);
        startBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        lrr = (ImageView)findViewById(R.id.lr_view);
                        hrr = (ImageView)findViewById(R.id.hr_view);
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        if(intent.resolveActivity(getPackageManager()) != null){
                            startActivityForResult(intent,PICK_PHOTO_CODE);
                        }
                    }
                }
        );













    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode,resultCode,intent);
        Uri u = Uri.parse("asdas");
        Uri photoUri = intent.getData();
        Log.i("INFO",String.valueOf(photoUri));
        try {

            lrImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);

            lrr.setImageBitmap(lrImage);
            ImageProcessor ip = new ImageProcessor.Builder()//.add(new ResizeWithCropOrPadOp(inp_s, inp_s))
                    .add(new ResizeWithCropOrPadOp(50,50))
                    //.add(new NormalizeOp(0f, 1f))
                    .add(new CastOp(DataType.FLOAT32))
                    .build();
            TensorImage rr = ip.process(TensorImage.fromBitmap(lrImage));
            rr.getWidth();


            uu();
            //startUpscale();
        }
        catch (Exception exception){
            Log.e("Error", "Exception : ",exception);
        }
    }
    private void uu(){
        ScalingUtilities su = new ScalingUtilities();

        lrr.setImageBitmap(lrImage);
        lrImage = su.createScaledBitmap(lrImage,lrImage.getWidth()/4,lrImage.getHeight()/4, ScalingUtilities.ScalingLogic.CROP);
        //hrr.setImageBitmap(lrImage);
        //ByteArrayOutputStream out = new ByteArrayOutputStream();
        //lrImage.compress(Bitmap.CompressFormat.JPEG, 80, out);
        //Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
        //hrr.setImageBitmap(decoded);
        Date currentDate = new Date();
        // Форматирование времени как "день.месяц.год"
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        // Форматирование времени как "часы:минуты:секунды"
        DateFormat timeFormat = new SimpleDateFormat("HH-mm-ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);
        //lrImage=decoded;
        String fileName = dateText.toString()+'-'+ timeText.toString()+".png";
        File photo=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), fileName.toLowerCase());
        Log.i("PPP",photo.getAbsolutePath());
        Log.i("PPP",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString());
        //MediaStore.Images.Media.insertImage(getContentResolver(), decoded, fileName , "23");
        try {
            FileOutputStream fos=new FileOutputStream(photo);
            byte[] paramArrayOfByte = bitmapToByteArray(lrImage);
            //fos.write(paramArrayOfByte);
            fos.close();
        }
        catch (java.io.IOException e) {
            Log.e("PictureDemo", "Exception in photoCallback", e);
        }
        //File photo=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), fileName.toLowerCase());
//        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
//
//        } else {
//            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    123);
//        }
    }
    private void startUpscale(){

        Bitmap lrI ;
        //lrr.setImageBitmap(lrImage);
        lrI = lrImage = ScalingUtilities.createScaledBitmap(lrImage,50,50, ScalingUtilities.ScalingLogic.FIT);
        CompatibilityList compatList = new CompatibilityList();
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        lrImage.compress(Bitmap.CompressFormat.PNG, 50, out);
//        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
//        hrr.setImageBitmap(decoded);
//        Date currentDate = new Date();
//        // Форматирование времени как "день.месяц.год"
//        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
//        String dateText = dateFormat.format(currentDate);
//        // Форматирование времени как "часы:минуты:секунды"
//        DateFormat timeFormat = new SimpleDateFormat("HH-mm-ss", Locale.getDefault());
//        String timeText = timeFormat.format(currentDate);
//
//        String fileName = dateText.toString()+'-'+ timeText.toString()+".jpg";
//        //File photo=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), fileName.toLowerCase());
//        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
//            MediaStore.Images.Media.insertImage(getContentResolver(), decoded, fileName , "23");
//        } else {
//            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    123);
//        }
//        Model.Options opt;

//        if(compatList.isDelegateSupportedOnThisDevice()){
//            // if the device has a supported GPU, add the GPU delegate
//            opt = new Model.Options.Builder().setDevice(Model.Device.GPU).build();
//        } else {
//            // if the GPU is not supported, run on 4 threads
//            opt = new Model.Options.Builder().setNumThreads(4).build();
//        }
//        TensorImage crop_img = ip.process(TensorImage.fromBitmap(lrImage));
//        try {
//            F model = F.newInstance(this);
//
//            // Creates inputs for reference.
//            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 50, 50, 3}, DataType.FLOAT32);
//
//            inputFeature0.loadBuffer(crop_img.getBuffer());
//
//            // Runs model inference and gets result.
//            float uy = SystemClock.uptimeMillis();
//            F.Outputs outputs = model.process(inputFeature0);
//
//            Log.i("INFO", String.valueOf(SystemClock.uptimeMillis()-uy));
//
//
//            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
//            Log.i("INFO", String.valueOf(outputFeature0.getFloatArray().length));
//            // Releases model resources if no longer used.
//            model.close();
//        } catch (IOException e) {
//            // TODO Handle the exception
//        }
//        Log.i("INFO", "124578590");
        try {
            Interpreter.Options options = new Interpreter.Options();
            int scale = 4;
//            if(compatList.isDelegateSupportedOnThisDevice()){
//                GpuDelegate.Options delegateOptions = compatList.getBestOptionsForThisDevice();
//                options.addDelegate(new GpuDelegate(delegateOptions));;
//
//                Log.i("INFO", "GPU_Used");
//            } else {
//                // if the GPU is not supported, run on 4 threads
//                options.setNumThreads(36);
//            }

            options.setNumThreads(36);
            int[] d = {1,50,50,3};
            interpreter = new Interpreter(FileUtil.loadMappedFile(this,path),options);

            //interpreter.resetVariableTensors();
            //interpreter.getInputTensorCount();
            //interpreter.resizeInput(0,d);
            interpreter.allocateTensors();
            //interpreter.tensor
            //interpreter.allocateTensors();
            //interpreter.resizeInput(1,d);



            int[] shape = interpreter.getInputTensor(0).shape();
            int[] shape1 = interpreter.getOutputTensor(0).shape();

            //Log.i("INFO", String.valueOf(interpreter.getSignatureInputs("index")));
            Log.i("INFO", String.valueOf(interpreter.getOutputTensorCount()));
            Log.i("INFO", String.valueOf(shape[0]));
            Log.i("INFO", String.valueOf(shape[1]));
            Log.i("INFO", String.valueOf(shape[2]));
            Log.i("INFO", String.valueOf(shape[3]));
            Log.i("INFO", String.valueOf(shape1[0]));
            Log.i("INFO", String.valueOf(shape1[1]));
            Log.i("INFO", String.valueOf(shape1[2]));
            Log.i("INFO", String.valueOf(shape1[3]));
            int inp_s = shape[1];
            //lrr.setImageBitmap(lrI);
            int real_h = lrI.getHeight();
            int real_w = lrI.getWidth();
            if(real_h<8000 && real_w<8000){
                int h_patch = (int)Math.ceil((double)(lrI.getHeight()/inp_s));
                int w_patch = (int)Math.ceil((double)(lrI.getWidth()/inp_s));

                //lrI = Bitmap.createScaledBitmap(lrI,50,50,true);
                ImageView hrr = (ImageView)findViewById(R.id.hr_view);

                Bitmap res = Bitmap.createBitmap(inp_s*scale,inp_s*scale, Bitmap.Config.ARGB_8888);

                float[][][][] out = new float[1][inp_s*scale][inp_s*scale][3];


                float inp0[][][][] = new float[1][50][50][3];

                inp0 = rgb2ycbcr(lrI);
                float t = SystemClock.uptimeMillis();
                interpreter.run(inp0,out);

                //interpreters[1].run(inp0,out);
                //interpreters[2].run(inp0,out);
                //interpreters[3].run(inp0,out);
                Log.i("INFO", String.valueOf(SystemClock.uptimeMillis()-t));
//                Object o = new float[1][200][200][3];
//                Map<Integer,Object> m= new HashMap<>();
//                m.put(0,o);
//                float[][][][][] op = new float[1][1][50][50][30];
//                op[0]=inp0;
//                interpreter.runForMultipleInputsOutputs(op,m);
                out = ycbcr2rgb(out);

                    for(int x = 0; x < out[0].length; x++) {
                        for(int y = 0; y < out[0].length; y++) {
                            //Log.i("INFO", String.valueOf(out[0][x][y][0]));
                            int red = (int)(out[0][x][y][0]);
                            int green = (int)(out[0][x][y][1]);
                            int blue = (int)(out[0][x][y][2]);
                            //red = Math.max(Math.min(red,255),0);
                            //green = Math.max(Math.min(green,255),0);
                            //blue = Math.max(Math.min(blue,255),0);
                            //Log.i("INFO", String.valueOf(res.getWidth()));
                            res.setPixel(y, x,  getIntFromColor(red,green,blue));
                        }
                    }
                    hrr.setImageBitmap(res);

                res = Bitmap.createScaledBitmap(res,real_w*scale,real_h*scale,true);
                //lrr.setImageBitmap(lrImage);
                hrr.setImageBitmap(res);
                //lrImage.compress(Bitmap.CompressFormat.PNG, 50, out);
                //Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                //hrr.setImageBitmap(decoded);
                Date currentDate = new Date();
                // Форматирование времени как "день.месяц.год"
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String dateText = dateFormat.format(currentDate);
                // Форматирование времени как "часы:минуты:секунды"
                DateFormat timeFormat = new SimpleDateFormat("HH-mm-ss", Locale.getDefault());
                String timeText = timeFormat.format(currentDate);

                String fileName = dateText.toString()+'-'+ timeText.toString()+".png";
                File photo=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), fileName.toLowerCase());


                try {
                    FileOutputStream fos=new FileOutputStream(photo);
                    byte[] paramArrayOfByte = bitmapToByteArray(res);
                    fos.write(paramArrayOfByte);
                    fos.close();
                }
                catch (java.io.IOException e) {
                    Log.e("PictureDemo", "Exception in photoCallback", e);
                }
                //MediaStore.Images.Media.insertImage(getContentResolver(), res, fileName , "hr");
                //{ Toast.makeText(UpscaleActivity.this,String.valueOf(SystemClock.uptimeMillis()-t)+"ms", Toast.LENGTH_SHORT).show();}
                /*Date currentDate = new Date();

                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String dateText = dateFormat.format(currentDate);

                DateFormat timeFormat = new SimpleDateFormat("HH-mm-ss", Locale.getDefault());
                String timeText = timeFormat.format(currentDate);

                String fileName = dateText.toString()+'-'+ timeText.toString()+".jpg";
                File photo=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), fileName.toLowerCase());


                try {
                    FileOutputStream fos=new FileOutputStream(photo);
                    byte[] paramArrayOfByte = bitmapToByteArray(res);
                    fos.write(paramArrayOfByte);
                    fos.close();
                }
                catch (java.io.IOException e) {
                    Log.e("PictureDemo", "Exception in photoCallback", e);
                }*/
            }
            else{
                { Toast.makeText(UpscaleActivity.this,"Пожалуйста, загрузите фото меньшего размера до 500x500", Toast.LENGTH_SHORT).show();}

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    };





    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream_edit_post_image = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream_edit_post_image);
        return stream_edit_post_image.toByteArray();
    }
    public static int[] getPixelData(Bitmap img, int x, int y) {
        int argb = img.getPixel(x, y);

        int rgb[] = new int[] {
                (argb >> 16) & 0xff, //red
                (argb >>  8) & 0xff, //green
                (argb      ) & 0xff  //blue
        };
        return rgb;
    }
    public static int getIntFromColor(float Red, float Green, float Blue){
        int R = Math.round(Red);
        int G = Math.round(Green);
        int B = Math.round(Blue);

        return 0xff000000 | (R << 16) | (G << 8) | B;
    }
    public float[][][][] rgb2ycbcr(Bitmap image){
        int h = image.getHeight();
        int w = image.getWidth();
        float[][][][] out = new float[1][h][w][3];
        int[] col = new int[3];
        float[] ycbcr = new float[3];
        for(int k =0 ;k<h;k++){
            for(int l=0;l<w;l++){
                col = getPixelData(image,l,k);
                int R = col[0];
                int G = col[1];
                int B = col[2];
                ycbcr[0] = 0.299f * R + 0.587f * G + 0.114f * B;
                ycbcr[1] = -0.16874f * R - 0.33126f * G + 0.5f * B + 128f;
                ycbcr[2] = 0.5f * R - 0.41869f * G - 0.08131f * B + 128f;
                ycbcr[0] = Math.max(Math.min(ycbcr[0],235),16);
                ycbcr[1] = Math.max(Math.min(ycbcr[1],240),16);
                ycbcr[2] = Math.max(Math.min(ycbcr[2],240),16);
                out[0][k][l][0]=ycbcr[0]/255;
                out[0][k][l][1]=ycbcr[1]/255;
                out[0][k][l][2]=ycbcr[2]/255;
            }
        }
        return out;

    }
    public float[][][][] ycbcr2rgb(float[][][][] in){
        int h = in[0].length;
        int w = in[0][0].length;
        float[][][][] out = new float[1][h][w][3];
        float[] col = new float[3];
        float[] ycbcr = new float[3];
        for(int k =0 ;k<h;k++){
            for(int l=0;l<w;l++){
                float Y = in[0][k][l][0]*255;
                float Cb = in[0][k][l][1]*255;
                float Cr = in[0][k][l][2]*255;
                col[0] =  Y + 1.402f * Cr - 179.456f;
                col[1] =  Y - 0.34414f * Cb - 0.71414f * Cr + 135.45984f;
                col[2] =  Y + 1.772f * Cb - 226.816f;
                col[0] = Math.max(Math.min(col[0],255),0);
                col[1] = Math.max(Math.min(col[1],255),0);
                col[2] = Math.max(Math.min(col[2],255),0);
                out[0][k][l][0] = col[0];
                out[0][k][l][1] = col[1];
                out[0][k][l][2] = col[2];
            }
        }
        return out;
    }

}
