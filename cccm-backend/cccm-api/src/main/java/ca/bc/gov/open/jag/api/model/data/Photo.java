package ca.bc.gov.open.jag.api.model.data;

import java.time.LocalDate;

public class Photo {

    private byte[] image;
    private LocalDate photoTakenDate;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public LocalDate getPhotoTakenDate() {
        return photoTakenDate;
    }

    public void setPhotoTakenDate(LocalDate photoTakenDate) {
        this.photoTakenDate = photoTakenDate;
    }
}
