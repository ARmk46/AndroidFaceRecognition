package pp.facerecognizer;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BaseUrl = "http://192.168.43.195:8000/";
    private static Retrofit retrofit = null ;

    public static Retrofit getApiClient()
    {
       //HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        //httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

       // OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
        // .client(okHttpClient)
        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder().baseUrl(BaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
