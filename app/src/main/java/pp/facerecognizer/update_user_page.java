package pp.facerecognizer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class update_user_page extends AppCompatActivity {

    ImagesResponse imagesResponse;
    EditText uid;
    EditText name;
    ImageView uimage;
    Button updateuserbtn;



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
                                       File file = new File(imagesResponse.getMy_image());
                                       String namelocal=name.getText().toString();
                                       String idlocal= uid.getText().toString();
                                       RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"),file);
                                       MultipartBody.Part my_image =MultipartBody.Part.createFormData("my_image", file.getName(), requestBody);
                                       RequestBody id = RequestBody.create(MediaType.parse("text/plain"),imagesResponse.getId());
                                       RequestBody name = RequestBody.create(MediaType.parse("text/plain"),namelocal);
                                       RequestBody userid = RequestBody.create(MediaType.parse("text/plain"),idlocal);
                                       Retrofit retrofit = ApiClient.getApiClient();
                                       ApiInterface apiInterface = retrofit.create(ApiInterface.class);

                                       Call<ResponseBody> call = apiInterface.updatedetails(id,userid,name,my_image);
                                           call.enqueue(new Callback<ResponseBody>() {
                                               @Override
                                               public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                   Toast.makeText(update_user_page.this, "Successfull !!! ", Toast.LENGTH_SHORT).show();
                                               }
                                               @Override
                                               public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                   Toast.makeText(update_user_page.this, "Failed to update,Contact Developer", Toast.LENGTH_SHORT).show();
                                                   finish();
                                               }
                                           });

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
}