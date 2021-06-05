package pp.facerecognizer;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class displayImageActivity extends AppCompatActivity {
    private List<ImagesResponse> imagesResponseList = new ArrayList<>();
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        gridView = findViewById(R.id.FetchimageGridview);
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        gridView.setMultiChoiceModeListener(new MultiChoiceModeListener());

        Bundle data = getIntent().getExtras();
        String parent = data.getString("parent");
        String uid = getIntent().getStringExtra("id");
        getallImages(uid);

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
                Toast.makeText(displayImageActivity.this,"Successful !!!",Toast.LENGTH_SHORT).show();
                if(response.isSuccessful())
                {
                    imagesResponseList = response.body();
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
                textView.setText(imagesResponseList.get(i).getMy_id());
                GlideApp.with(context)
                        .load(imagesResponseList.get(i).getMy_image())
                        .into(imageView);
            return view;
        }
    }

    public class MultiChoiceModeListener implements GridView.MultiChoiceModeListener {
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("Select Items");
            mode.setSubtitle("One item selected");
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return true;
        }

        public void onDestroyActionMode(ActionMode mode) {
        }

        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            int selectCount = gridView.getCheckedItemCount();
            System.out.println(position);
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