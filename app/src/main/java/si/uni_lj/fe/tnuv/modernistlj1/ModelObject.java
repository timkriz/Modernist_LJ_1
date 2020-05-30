package si.uni_lj.fe.tnuv.modernistlj1;

public class ModelObject {

    private int id;
    private int image;
    private String title;
    private String description_main;
    private int image_sketched;

    /*  Contructor for carousel items  */
    ModelObject(int id, String title, String description_main, int image, int image_sketched) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.description_main = description_main;
        this.image_sketched = image_sketched;
    }

    /*  Getters and setters for object  */

    public int getId() {return id;}

    int getImage() {
        return image;
    }

    String getTitle() {
        return title;
    }

    String getDescription_main() {
        return description_main;
    }

    public int getImage_sketched() {
        return image_sketched;
    }
}
