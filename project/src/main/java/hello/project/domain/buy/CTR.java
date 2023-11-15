package hello.project.domain.buy;

import lombok.Data;

@Data
public class CTR {
    private Long id;
    private Long ctr;

    public CTR() {
    }

    public CTR(Long ctr) {
        this.ctr = ctr;
    }
}
