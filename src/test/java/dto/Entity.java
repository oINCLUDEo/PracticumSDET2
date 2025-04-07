package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Entity {
    private String id;
    private String title;
    private boolean verified;
    private Addition addition;
    @JsonProperty("important_numbers")
    private int[] importantNumbers;

    @Data
    public static class Addition {
        @JsonProperty("additional_info")
        private String additionalInfo;

        @JsonProperty("additional_number")
        private int additionalNumber;

        private String id;
    }
}
