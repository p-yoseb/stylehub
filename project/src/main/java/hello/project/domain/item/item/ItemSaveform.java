package hello.project.domain.item.item;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ItemSaveform {

    @NotBlank
    private String title;
    @NotNull
    private Long categoryId;
    @NotBlank
    private String imageName1;
    @NotBlank
    private String imageName2;
    @NotBlank
    private String explanation;

    public ItemSaveform() {
    }

    public ItemSaveform(String title, Long categoryId, String imageName1, String imageName2, String explanation) {
        this.title = title;
        this.categoryId = categoryId;
        this.imageName1 = imageName1;
        this.imageName2 = imageName2;
        this.explanation = explanation;
    }
}
