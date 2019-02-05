package com.example.tickets.web.commandobjects;

import lombok.Data;

@Data
public class StageCO implements CommandObject {
    private String stageName;
}
