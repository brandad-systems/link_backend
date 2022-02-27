package de.bas.link.domain.dto;

import lombok.Data;


@Data
public class RentalView {

    private String productId;
    private String productTitle;
    private boolean available;
    private String details;
    private String nextStatus;
}
