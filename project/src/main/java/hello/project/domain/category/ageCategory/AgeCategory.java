package hello.project.domain.category.ageCategory;

import lombok.Data;

@Data
public class AgeCategory {
    private Long id;
    private Long age;
    private Long titleId1;
    private Long titleId2;
    private Long titleId3;
    private Long titleId4;
    private Long titleId5;
    private Long titleId6;
    private Long titleId7;
    private Long titleId8;

    public AgeCategory() {
    }

    public AgeCategory(Long age, Long titleId1, Long titleId2, Long titleId3, Long titleId4, Long titleId5, Long titleId6, Long titleId7, Long titleId8) {
        this.age = age;
        this.titleId1 = titleId1;
        this.titleId2 = titleId2;
        this.titleId3 = titleId3;
        this.titleId4 = titleId4;
        this.titleId5 = titleId5;
        this.titleId6 = titleId6;
        this.titleId7 = titleId7;
        this.titleId8 = titleId8;
    }
}
