package pp.facerecognizer;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @Multipart
    @POST("myimage/")
    Call<ResponseBody> uploadImage(@Part("my_name") RequestBody name,
                                   @Part("my_id") RequestBody userid,
                                   @Part MultipartBody.Part my_image);
}
