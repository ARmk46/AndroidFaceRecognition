package pp.facerecognizer;
import com.google.gson.annotations.SerializedName;

public class imageClass {

    @SerializedName("my_name")
    private String Name;

    public imageClass(String name, String image) {
        Name = name;
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    @SerializedName("my_image")
    private String Image;


}
