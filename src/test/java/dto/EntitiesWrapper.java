package dto;

import lombok.Data;
import java.util.List;

@Data
public class EntitiesWrapper {
    private List<Entity> entity;
    private int page;
    private int perPage;
}
