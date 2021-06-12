package pp.facerecognizer;

import java.io.Serializable;

public class ImagesResponse implements Serializable {
    private String id;
    private String my_id;
    private String my_name;
    private String my_image;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getMy_id() {
        return my_id;
    }
    public void setMy_id(String my_id) {
        this.my_id = my_id;
    }

    public String getMy_name() {
        return my_name;
    }
    public void setMy_name(String my_name) {
        this.my_name = my_name;
    }

    public String getMy_image()
    {
        String baseUrl = "http://192.168.43.195:8000";
        return baseUrl+my_image;
    }
    public void setMy_image(String my_image) {
        this.my_image = my_image;
    }
}
