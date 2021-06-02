package pp.facerecognizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class imageActivity extends AppCompatActivity {

    EditText uidEditText;
    Button fetchImages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        uidEditText = findViewById(R.id.uidEditText);
        fetchImages = findViewById(R.id.fetchImages);

        fetchImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uid = uidEditText.getText().toString();
                Intent pictureActivityintent = new Intent(imageActivity.this, displayImageActivity.class);
                pictureActivityintent.putExtra("id",uid);
                startActivity(pictureActivityintent);
            }
        });

    }

}