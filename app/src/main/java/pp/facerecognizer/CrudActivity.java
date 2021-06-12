package pp.facerecognizer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
                    String uid = update_uid_edittext.getText().toString();
                    Intent updateuser_imagegridview= new Intent(CrudActivity.this, displayImageActivity.class);
                    updateuser_imagegridview.putExtra("id",uid);
                    updateuser_imagegridview.putExtra("parent", "updateuser");
                    startActivity(updateuser_imagegridview);
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
                    Intent updateuser_imagegridview= new Intent(CrudActivity.this, displayImageActivity.class);
                    updateuser_imagegridview.putExtra("id",uid);
                    updateuser_imagegridview.putExtra("parent", "deleteuser");
                    startActivity(updateuser_imagegridview);
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

}