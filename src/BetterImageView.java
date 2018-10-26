import javax.swing.text.Element;
import javax.swing.text.html.ImageView;

public class BetterImageView {

    private int ordre;
    private ImageView view;

    public BetterImageView(ImageView imageView, int ordre) {
       this.view = imageView;
        this.ordre = ordre;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }
}
