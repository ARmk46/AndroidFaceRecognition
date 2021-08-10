package pp.facerecognizer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CrudActivity extends AppCompatActivity {

    Button adduserbtn;
    Button updateuserbtn;
    Button deleteuserbtn;
    Button okbtn;
    Boolean booladd=false,boolupdate=false,booldelete = false;
    EditText nameedittext;
    EditText idedittext;
    String name="";
    String id="";

    @Override
    public void onRestart() {
        super.onRestart();
        nameedittext.setVisibility(View.GONE);
        idedittext.setVisibility(View.GONE);
        okbtn.setVisibility(View.GONE);
        booladd=false;
        boolupdate=false;
        booldelete = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud);


        adduserbtn = findViewById(R.id.adduserbtn);
        updateuserbtn = findViewById(R.id.updateuserbtn);
        deleteuserbtn = findViewById(R.id.deleteuserbtn);
        okbtn = findViewById(R.id.okbtn);
        nameedittext = (EditText) findViewById(R.id.nametextview);
        idedittext = (EditText) findViewById(R.id.idtextview);



        adduserbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameedittext.setVisibility(View.VISIBLE);
                idedittext.setVisibility(View.VISIBLE);
                okbtn.setVisibility(View.VISIBLE);
                booladd = true;
            }
        });

        View updateuser_dialogView = getLayoutInflater().inflate(R.layout.dialog_edittext, null);
        EditText update_uid_edittext = updateuser_dialogView.findViewById(R.id.edit_text);
        AlertDialog updateuser_editDialog = new AlertDialog.Builder(CrudActivity.this)
                .setTitle(R.string.enter_id)
                .setView(updateuser_dialogView)
                .setPositiveButton(getString(R.string.ok), (dialogInterface, i) -> {
                    getAlert();
                    id = update_uid_edittext.getText().toString();
                    /*Intent updateuser_imagegridview= new Intent(CrudActivity.this, displayImageActivity.class);
                    updateuser_imagegridview.putExtra("id",uid);
                    updateuser_imagegridview.putExtra("parent", "updateuser");
                    startActivity(updateuser_imagegridview); */
                })
                .create();
        updateuserbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateuser_editDialog.show();
            }
        });

        View deleteuser_dialogView = getLayoutInflater().inflate(R.layout.dialog_edittext, null);
        EditText deleteuser_edittext = deleteuser_dialogView.findViewById(R.id.edit_text);
        AlertDialog deleteuser_editDialog = new AlertDialog.Builder(CrudActivity.this)
                .setTitle(R.string.enter_id)
                .setView(deleteuser_dialogView)
                .setPositiveButton(getString(R.string.ok), (dialogInterface, i) -> {
                    String uid = deleteuser_edittext.getText().toString();
                    /*Intent updateuser_imagegridview= new Intent(CrudActivity.this, displayImageActivity.class);
                    updateuser_imagegridview.putExtra("id",uid);
                    updateuser_imagegridview.putExtra("parent", "deleteuser");
                    startActivity(updateuser_imagegridview);*/
                    new android.app.AlertDialog.Builder(CrudActivity.this)
                            .setTitle("Alert")
                            .setMessage("Do you really want to delete images of : "+ uid + " ???")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Retrofit retrofit = ApiClient.getApiClient();
                                    ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                                    Call<ResponseBody> call = apiInterface.deleteItem(uid);
                                    call.enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if(response.code()==404){
                                                Toast.makeText(CrudActivity.this, "Failed to Delete, User Does not Exist !!!", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(CrudActivity.this, "Successfully Deleted !!!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            Toast.makeText(CrudActivity.this, "Failed to delete,Contact Developer", Toast.LENGTH_SHORT).show();
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

                })
                .create();
                deleteuserbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               deleteuser_editDialog.show();
            }
        });

        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(booladd == true) {
                    name = nameedittext.getText().toString();
                    id = idedittext.getText().toString();

                    if (idedittext.getText().toString().length() == 0)
                        idedittext.setError("Id is required");
                    else if (nameedittext.getText().toString().length() == 0)
                        nameedittext.setError("Name is required!");
                    else {
                        Intent pictureActivityintent = new Intent(CrudActivity.this, pictureActivity.class);
                        pictureActivityintent.putExtra("name", name);
                        pictureActivityintent.putExtra("id", id);
                        startActivity(pictureActivityintent);
                    }
                    booladd = false;
                }

            }
        });

    }
    public void getAlert()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(CrudActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Are you sure, all existing data will be erased ????");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        AlertDialog alertDialog1 = new AlertDialog.Builder(CrudActivity.this).create();
                        final EditText input = new EditText(CrudActivity.this);
                        alertDialog1.setTitle("Alert");
                        alertDialog1.setView(input);
                        alertDialog1.setMessage("Enter your Name : ");

                        alertDialog1.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        String name = input.getText().toString();
                                        Intent pictureActivity = new Intent(CrudActivity.this, pictureActivity.class);
                                        pictureActivity.putExtra("name",name);
                                        pictureActivity.putExtra("id",id);
                                        startActivity(pictureActivity);
                                    }
                                });
                        alertDialog1.show();

                        Retrofit retrofit = ApiClient.getApiClient();
                        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                        Call<ResponseBody> call = apiInterface.deleteItem(id);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(response.code()==404){
                                    Toast.makeText(CrudActivity.this, "User Does not Exist !!!", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(CrudActivity.this, "Successfully Deleted !!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(CrudActivity.this, "Failed to delete,Contact Developer", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
        alertDialog.show();

    }

}