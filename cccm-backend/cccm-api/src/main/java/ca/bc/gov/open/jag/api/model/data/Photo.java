package ca.bc.gov.open.jag.api.model.data;

public class Photo {

    private byte[] image;
    private String photoTakenDate;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getPhotoTakenDate() {
        return photoTakenDate;
    }

    public void setPhotoTakenDate(String photoTakenDate) {
        this.photoTakenDate = photoTakenDate;
    }
}
