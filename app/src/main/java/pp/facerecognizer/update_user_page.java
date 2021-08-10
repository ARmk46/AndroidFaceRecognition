package pp.facerecognizer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static pp.facerecognizer.pictureActivity.REQUEST_IMAGE_CAPTURE;

public class update_user_page extends AppCompatActivity {

    ImagesResponse imagesResponse;
    EditText uid;
    EditText name;
    private Button updateuserbtn;
    private ImageView uimage;
    private Bitmap bitmap;
    String naam;
    String pta;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;
    Uri photoURI;
    boolean picture_flag = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_page);

        uid=findViewById(R.id.uidtextview);
        name=findViewById(R.id.nametextview);
        uimage=findViewById(R.id.uimageview);
        updateuserbtn=findViewById(R.id.updateuserbtn);

        Intent intent=getIntent();
       if(intent.getExtras()!=null){
           imagesResponse= (ImagesResponse) intent.getSerializableExtra("data");
           uid.setText(imagesResponse.getMy_id());
           name.setText(imagesResponse.getMy_name());
           GlideApp.with(this).load(imagesResponse.getMy_image()).into(uimage) ;


           uimage.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                    picture_flag = true;
                    dispatchTakePictureIntent();
               }
           });
           updateuserbtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   if (uid.getText().toString().length() == 0)
                       uid.setError("Id is required !!! ");
                   else if (name.getText().toString().length() == 0)
                       name.setError("Name is required!!! ");
                   else {
                       new AlertDialog.Builder(update_user_page.this)
                               .setTitle("Alert")
                               .setMessage("Do you really want to update details ???")
                               .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog, int which) {
                                       naam = name.getText().toString();
                                       pta = uid.getText().toString();
                                     //  System.out.println("-------------------------------------> "+picture_flag+" <------------------------------------->");
                                       if(picture_flag)
                                       {
                                           String img_id = imagesResponse.getId();
                                           System.out.println(img_id);
                                           DeletePreviousImage(img_id);
                                           UploadImage(naam,pta,photoURI);

                                       }
                                       else{
                                           Toast.makeText(update_user_page.this,"Image Update Required !!!!!!",Toast.LENGTH_SHORT).show();
                                       }

                                   }
                               })
                               .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialogInterface, int i) {
                                       finish();
                                   }
                               })
                               .setIcon(android.R.drawable.ic_dialog_alert)
                               .show();
                   }
               }
           });
       }
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
                uimage.setImageBitmap(bitmap);
            };
        }

    }


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



    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {

            File photoFile = null;
            try {
                photoFile = createImageFile();

            } catch (IOException ex) {

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

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        currentPhotoPath = image.getAbsolutePath();

        return image;
    }


    private void UploadImage(String username,String uid,Uri photoURI)
    {
        File file = new File(currentPhotoPath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"),file);
        MultipartBody.Part my_image =MultipartBody.Part.createFormData("my_image", file.getName(), requestBody);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"),username);
        RequestBody userid = RequestBody.create(MediaType.parse("text/plain"),uid);
        Retrofit retrofit = ApiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<ResponseBody> call = apiInterface.uploadImage(userid,name,my_image);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(update_user_page.this,"Successfully Updated !!!!!!",Toast.LENGTH_SHORT).show();
                finish();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(update_user_page.this,"Failed to Upload,Contact Developer",Toast.LENGTH_SHORT).show();
               // finish();
            }
        });
    }

    private void DeletePreviousImage(String id)
    {
        //System.out.println("-------------------------->"+id+" <---------------------------------------");
        Retrofit retrofit = ApiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.deleteItem(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(update_user_page.this, "Failed to delete,Contact Developer", Toast.LENGTH_SHORT).show();
                //finish();
            }
        });


    }

}