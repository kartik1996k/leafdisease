
package com.example.kartik.leafdisease;

        import android.annotation.SuppressLint;
        import android.app.Activity;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.graphics.Bitmap;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.ColorMatrix;
        import android.graphics.ColorMatrixColorFilter;
        import android.graphics.Paint;
        import android.net.Uri;
        import android.provider.MediaStore;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.theartofdev.edmodo.cropper.CropImage;
        import com.theartofdev.edmodo.cropper.CropImageView;

        import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "NewPostActivity";
    private static final String REQUIRED = "Required";




    private EditText mTitleField;
    public Bitmap bitmap;
    private EditText mBodyField;
    private ImageView imageView;
    private static final int GALLERY_REQUEST=1;
    private Uri imageUri=null;

    private static final int RC_CAMERA_PERMISSIONS = 102;
    private Uri mFileUri;
    private static final int TC_PICK_IMAGE = 101;
    public String asl;
    private Uri mCropImageUri;
    String alk;
    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    //    tv=(TextView)findViewById(R.id.textView);

    }

    public void gh()
    {
        ImageView mImg;
        mImg = (ImageView) findViewById(R.id.imageView);
      //  mImg.setImageBitmap(bitmap);
        Bitmap b=toGrayscale(bitmap);

        Bitmap mb = b.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mb);
        int width = mb.getWidth();
        int height = mb.getHeight();

        int pixel;


        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color

                pixel = mb.getPixel(x, y);

                int A = Color.alpha(pixel);
                int R = Color.red(pixel);
                int G = Color.green(pixel);
                int B = Color.blue(pixel);
                if(x==0&&y==0)
                {

                 //   tv.setText(String.valueOf(A)+String.valueOf(R)+String.valueOf(G)+String.valueOf(B)+String.valueOf(pixel));
                }
                if(R>200&&G>200&&B>200)
                {
                    pixel=Color.BLUE;
                }
                //    bitmap.
                mb.setPixel(x,y,pixel);
            }
        }


  /*      int[] pixels = new int[b.getHeight()*b.getWidth()];
        b.getPixels(pixels, 0, b.getWidth(), 0, 0, b.getWidth(), b.getHeight());
        tv.setText(String.valueOf(pixels[0]));
        for (int i=0; i<b.getWidth()*b.getHeight(); i++) {
           if(pixels[i]>150)
            pixels[i] = Color.BLUE;
        }
        b.setPixels(pixels, 0, b.getWidth(), 0, 0, b.getWidth(), b.getHeight());
*/
        mImg.setImageBitmap(mb);


    }
    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }
    public void postImage(View view){
        CropImage.startPickImageActivity(this);
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
               // ((ImageView) findViewById(R.id.imageView)).setImageURI(result.getUri());

                mFileUri=result.getUri();
                alk="ll";
                alk="ll";
                try {
                    bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),mFileUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                gh();



                //   imageView.setImageBitmap(bitmap);
                //  Toast.makeText(this, "Cropping successful, Sample: " + mFileUri, Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri);
        } else {
            Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Start crop image activity for the given image.
     */
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true).setOutputCompressQuality(60)
                .start(this);
    }





}
