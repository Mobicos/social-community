package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
    private String title;
    private String author;
    private String release_date;
    private double amazon_rating;
    private boolean best_seller;
    private int edition;
    private Map<String, Double> prices;
}
