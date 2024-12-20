package vn.bachdao.soundcloud.service.dto.repsonse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultPaginationDTO {
    private Meta meta;
    private Object data;

    @Getter
    @Setter
    public static class Meta {
        private int pageNumber;
        private int pageSize;
        private int totalPage;
        private long totalElement;
    }
}
