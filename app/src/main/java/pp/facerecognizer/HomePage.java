package pp.facerecognizer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomePage extends AppCompatActivity {

    Button facedetectbtn;
    Button trainbtn;
    Button crudbtn;
    Button morebtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        facedetectbtn = findViewById(R.id.facedetectionbtn);
        trainbtn = findViewById(R.id.trainbtn);
        crudbtn = findViewById(R.id.crudbtn);
        morebtn = findViewById(R.id.morebtn);




        facedetectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Facedetection = new Intent(HomePage.this,MainActivity.class);
                startActivity(Facedetection);

            }
        });

        crudbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent crud = new Intent(HomePage.this,CrudActivity.class);
                startActivity(crud);

            }
        });


        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edittext, null);
        EditText uidedittext = dialogView.findViewById(R.id.edit_text);
        AlertDialog editDialog = new AlertDialog.Builder(HomePage.this)
                .setTitle(R.string.enter_id)
                .setView(dialogView)
                .setPositiveButton(getString(R.string.ok), (dialogInterface, i) -> {
                    String uid = uidedittext.getText().toString();
                    Intent Training_gridimageview = new Intent(HomePage.this, displayImageActivity.class);
                    Training_gridimageview.putExtra("id",uid);
                    Training_gridimageview.putExtra("parent","trainuser");
                    startActivity(Training_gridimageview);
                })
                .create();


        trainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDialog.show();
            }
        });

    }

}