package pp.facerecognizer;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.DELETE;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {

    @Multipart
    @POST("myimage/")
    Call<ResponseBody> uploadImage(
                                   @Part("my_id") RequestBody userid,
                                   @Part("my_name") RequestBody name,
                                   @Part MultipartBody.Part my_image);

    @GET("myimage/{id}/")
    Call<ImagesResponse> getimages(@Path("id") String id);

    @GET("myimage/all/{my_id}")
    Call<List<ImagesResponse>> getallimages(@Path("my_id") String my_id);

    @DELETE("/myimage/all/{id}/")
    Call<ResponseBody> deleteItem(@Path("id") String id);

    @Multipart
    @PUT("myimage/{id}/")
    Call<ResponseBody> updatedetails(
            @Path("id") RequestBody id,
            @Part("my_id") RequestBody userid,
            @Part("my_name") RequestBody name,
            @Part MultipartBody.Part my_image);

}


