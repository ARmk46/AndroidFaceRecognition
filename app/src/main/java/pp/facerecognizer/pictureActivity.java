package pp.facerecognizer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class pictureActivity extends AppCompatActivity  {

  //  private EditText Img_title;
    private Button upl_button,cap_button;
    private ImageView Img;
    private Bitmap bitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;
    Uri photoURI;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle data = getIntent().getExtras();
        String username = data.getString("name");
        String userid = data.getString("id");
        System.out.println(username);
        System.out.println(userid);

    //    Img_title = (EditText) findViewById(R.id.img_title);
        cap_button = (Button) findViewById(R.id.btn_Capture);
        upl_button = (Button) findViewById(R.id.btn_Upload);
        Img = (ImageView) findViewById(R.id.imageView);

        cap_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        upl_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

          //      String img_Title = Img_title.getText().toString();
                UploadImage("img_Title",photoURI);

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            if(requestCode == 1)
            {
                bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                try {
                    bitmap = rotateImageIfRequired(this,bitmap,photoURI);
                } catch (IOException e) {
                    System.out.println("Failed to Rotate the Image !!!!!");
                }
                Img.setImageBitmap(bitmap);
            };
        }

    }

    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();

            } catch (IOException ex) {
                System.out.println("Failed To prepare and image");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
              //  System.out.println("1------->"+photoFile);
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.retrofitimageupload.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
      //  System.out.println(currentPhotoPath);
        return image;
    }

    /**
     * Rotate an image if required.
     *
     * @param img           The image bitmap
     * @param selectedImage Image URI
     * @return The resulted Bitmap after manipulation
     */
    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    private void UploadImage(String img_title,Uri photoURI)
    {
       // String Image = imageToString();
        File file = new File(currentPhotoPath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"),file);
        MultipartBody.Part my_image =
                MultipartBody.Part.createFormData("my_image", file.getName(), requestBody);

        RequestBody name = RequestBody.create(MediaType.parse("text/plain"),"Image1");


        Retrofit retrofit = ApiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.uploadImage(name,my_image);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("!!!!! Successfully uploaded");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("!!!!! Failed To Upload");
            }
        });
    }

    private String imageToString()
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return android.util.Base64.encodeToString(imageByte, android.util.Base64.DEFAULT);

    }

}




