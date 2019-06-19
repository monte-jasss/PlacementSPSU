package in.ac.spsu.placement.placementspsu.Server;

import android.os.Environment;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    private static final String BASE_URL = "https://monulakshkar.000webhostapp.com/placement/";
    //private static final String BASE_URL = "http://192.168.137.1/placement/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient(){
        if(retrofit==null){
            int cacheSize = 10 * 1024 * 1024; // 10 MB

            File path = Environment.getExternalStorageDirectory();
            File dir = new File(path.getAbsolutePath() + "/Placement");
            if (!dir.exists()) {
                dir.mkdir();
            }

            Cache cache = new Cache(dir,cacheSize);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .cache(cache)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
