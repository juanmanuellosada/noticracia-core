package noticracia.entities;

public class Information {

    private String text;
    private String link;

    public Information(String text, String link) {
        this.text = text;
        this.link = link;
    }

    public String getText() {
        return this.text;
    }

    public String getLink() {
        return this.link;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
