package com.zackbleach.memetable.controller;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

@Data
public class PetResource extends ResourceSupport {
    private String name;
    private int age;
}
