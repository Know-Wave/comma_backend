package know_wave.comma.arduino.component.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ArduinoUpdateForm {

    @JsonProperty("target_arduino_id")
    private final Long updatedArduinoId;

    @JsonProperty("updated_arduino_name")
    private final String updatedArduinoName;

    @JsonProperty("updated_count")
    private final int updatedCount;

    @JsonProperty("updated_description")
    private final String updatedDescription;

    @JsonProperty("updated_categories")
    private final List<Long> updatedCategories;

    @JsonProperty("delete_photo_files")
    private final List<String> deletedPhotoFiles;

    @JsonProperty("add_photo_files")
    private final List<MultipartFile> addedPhotoFiles;
}
