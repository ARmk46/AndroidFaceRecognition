package pp.facerecognizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

        trainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent crud = new Intent(HomePage.this,imageActivity.class);
                startActivity(crud);
              //  getImages("96");
            }
        });

    }

}