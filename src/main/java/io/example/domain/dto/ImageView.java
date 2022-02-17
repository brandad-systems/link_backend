package io.example.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageView {

    // combination of {user id}/{timestamp}-{fileName}
    private String imagePath;

}
