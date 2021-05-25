package pp.facerecognizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CrudActivity extends AppCompatActivity {

    Button adduserbtn;
    Button updateuserbtn;
    Button deleteuserbtn;
    Button okbtn;
    EditText nameedittext;
    EditText idedittext;
    String name="";
    String id="";

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

            }
        });

        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameedittext.getText().toString();
                id = idedittext.getText().toString();

                if( idedittext.getText().toString().length() == 0 )
                    idedittext.setError( "Id is required" );
                else if( nameedittext.getText().toString().length() == 0 )
                    nameedittext.setError( "Name is required!" );
                else{
                    Intent pictureActivityintent = new Intent(CrudActivity.this,pictureActivity.class);
                    pictureActivityintent.putExtra("name",name);
                    pictureActivityintent.putExtra("id",id);
                    startActivity(pictureActivityintent);
                }

               /* if(name == "")
                {
                    Toast.makeText(CrudActivity.this, "Name or id cannot be blank.....",Toast.LENGTH_SHORT).show();
                } */
            }
        });

    }
}