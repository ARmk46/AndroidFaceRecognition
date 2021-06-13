package pp.facerecognizer;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class displayImageActivity extends AppCompatActivity {
    private List<ImagesResponse> imagesResponseList = new ArrayList<>();
    private List<String>checkedimages =new ArrayList<String>();

    GridView gridView;
    String parent;
    String uid;
    Button gridviewbtn;

    @Override
    public void onRestart() {
        super.onRestart();
        gridviewbtn.setVisibility(View.GONE);
        getallImages(uid);
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        gridView.setMultiChoiceModeListener(new MultiChoiceModeListener());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        gridView = findViewById(R.id.FetchimageGridview);
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        gridView.setMultiChoiceModeListener(new MultiChoiceModeListener());
        gridviewbtn=findViewById(R.id.gridviewbtn);
        Bundle data = getIntent().getExtras();
        parent = data.getString("parent");
        uid = getIntent().getStringExtra("id");
        getallImages(uid);



          gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
              @Override
              public void onItemClick(AdapterView<?> adapterView,View view,int i,long l){
                  if (parent.equals("updateuser")) {
                      String id=imagesResponseList.get(i).getId();
                      new AlertDialog.Builder(displayImageActivity.this)
                              .setTitle("Alert")
                              .setMessage("Do you  want to update this image and details ???")
                              .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                  public void onClick(DialogInterface dialog, int which) {
                                      Intent updateuserpage= new Intent(displayImageActivity.this, update_user_page.class);
                                      updateuserpage.putExtra("data",imagesResponseList.get(i));
                                      startActivity(updateuserpage);
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

            gridviewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (parent.equals("deleteuser")) {
                    new AlertDialog.Builder(displayImageActivity.this)
                            .setTitle("Alert")
                            .setMessage("Do you really want to delete ???")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    for (int i = 0; i < checkedimages.size(); i++) {

                                        String id = checkedimages.get(i);
                                        Retrofit retrofit = ApiClient.getApiClient();
                                        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                                        Call<ResponseBody> call = apiInterface.deleteItem(id);
                                        call.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                Toast.makeText(displayImageActivity.this, "Failed to delete,Contact Developer", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });


                                    } //forloop ends
                                    Toast.makeText(displayImageActivity.this, "Successfully deleted!!!!!!", Toast.LENGTH_SHORT).show();
                                    onRestart();
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
                else if(parent.equals("trainuser")){
                    System.out.println("train user called");
                }

            }
        });

    }

    public void getImages(String id){
        Retrofit retrofit = ApiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ImagesResponse> imagesResponse = apiInterface.getimages(id);

        imagesResponse.enqueue(new Callback<ImagesResponse>() {
            @Override
            public void onResponse(Call<ImagesResponse> call, Response<ImagesResponse> response) {
                Toast.makeText(displayImageActivity.this,"Glad!! Successfull",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<ImagesResponse> call, Throwable t) {

                Toast.makeText(displayImageActivity.this,"An Error Occured",Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void getallImages(String my_id){
        Retrofit retrofit = ApiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<List<ImagesResponse>> imagesResponse = apiInterface.getallimages(my_id);

        imagesResponse.enqueue(new Callback<List<ImagesResponse>>() {
            @Override
            public void onResponse(Call<List<ImagesResponse>> call, Response<List<ImagesResponse>> response) {

                if(response.isSuccessful())
                {
                    imagesResponseList = response.body();
                    if(imagesResponseList.size()==0){
                        Toast.makeText(displayImageActivity.this,"Nothing to Display!!!",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(displayImageActivity.this,"Successful!!!",Toast.LENGTH_SHORT).show();
                    }
                    CustomAdapter customAdapter = new CustomAdapter(imagesResponseList,displayImageActivity.this);
                    gridView.setAdapter(customAdapter);
                }
            }
            @Override
            public void onFailure(Call<List<ImagesResponse>> call, Throwable t) {

                Toast.makeText(displayImageActivity.this,"An Error Occured",Toast.LENGTH_SHORT).show();

            }

        });
    }

    public class CustomAdapter extends BaseAdapter{

        private List<ImagesResponse> imagesResponseList;
        private Context context;
        private LayoutInflater layoutInflater;

        public CustomAdapter(List<ImagesResponse> imagesResponseList, Context context) {
            this.imagesResponseList = imagesResponseList;
            this.context = context;
            this.layoutInflater =(LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);

        }
        @Override
        public int getCount() {
            return imagesResponseList.size();
        }
        @Override
        public Object getItem(int i) {
            return null;
        }
        @Override
        public long getItemId(int i) {
            return 0;
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null)
            {
                view = layoutInflater.inflate(R.layout.row_grid_items,viewGroup,false);
            }

            ImageView imageView = view.findViewById(R.id.FetchImageView);
                TextView textView = view.findViewById(R.id.idtextview);
                TextView nametextView = view.findViewById(R.id.nametextview);

                textView.setText(imagesResponseList.get(i).getMy_id());

                if(parent.equals("updateuser")){
                    nametextView.setText(imagesResponseList.get(i).getMy_name());
                    nametextView.setVisibility(view.VISIBLE);
                }
                GlideApp.with(context)
                        .load(imagesResponseList.get(i).getMy_image())
                        .into(imageView);

            return view;
        }
    }

    public class MultiChoiceModeListener implements GridView.MultiChoiceModeListener {
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            if(parent.equals("trainuser") || parent.equals("deleteuser")) {
                mode.setTitle("Select Items");
                mode.setSubtitle("One item selected");
                return true;
            }
            checkedimages.clear();
           return false;
        }
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
           // System.out.println("Action onPerepareActionMode called");
            if(parent.equals("trainuser") || parent.equals("deleteuser")) {
                gridviewbtn.setVisibility(View.VISIBLE);
            }
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            //System.out.println("Action onActionItemClicked called");
            return true;
        }

        public void onDestroyActionMode(ActionMode mode) {
            gridviewbtn.setVisibility(View.GONE);
            checkedimages.clear();
            mode.finish();
            //System.out.println("Action onDestroyActionMode called");
        }

        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            if(parent.equals("trainuser") || parent.equals("deleteuser")) {
                int selectCount = gridView.getCheckedItemCount();
                checkedimages.add(imagesResponseList.get(position).getId());
                if(checked){
                    View tv = (View) gridView.getChildAt(position);
                    tv.setBackgroundColor(Color.BLUE);
                }else{
                    View tv = (View) gridView.getChildAt(position);
                    tv.setBackgroundColor(Color.TRANSPARENT);
                }

                switch (selectCount) {
                    case 1:
                        mode.setSubtitle("One item selected");
                        break;
                    default:
                        mode.setSubtitle("" + selectCount + " items selected");
                        break;
                }
            }
        }
    }

}