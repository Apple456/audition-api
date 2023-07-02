package com.audition.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuditionPost {

    private int userId;
    private int id;
    private String title;
    private String body;
    private List<AuditionComment> auditionComments;

}
