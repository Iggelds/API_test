package org.example;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Pet {
    private long id;
    private String name;
    public Pet(String name) {
        this.name = name;
    }


}
