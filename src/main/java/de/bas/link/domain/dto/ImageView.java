package de.bas.link.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageView {

    // combination of {bucketName}/{userId}/{timestamp}-{fileName}
    private String imagePath;

}
