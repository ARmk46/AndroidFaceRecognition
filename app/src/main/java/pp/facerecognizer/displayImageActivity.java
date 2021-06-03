package pp.facerecognizer;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class displayImageActivity extends AppCompatActivity {

    private final List<ImagesResponse> imagesResponseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

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
        System.out.println(imagesResponse);
        imagesResponse.enqueue(new Callback<List<ImagesResponse>>() {
            @Override
            public void onResponse(Call<List<ImagesResponse>> call, Response<List<ImagesResponse>> response) {
                Toast.makeText(displayImageActivity.this,"Glad!! Successfull",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<List<ImagesResponse>> call, Throwable t) {

                Toast.makeText(displayImageActivity.this,"An Error Occured",Toast.LENGTH_SHORT).show();

            }
        });
    }

}