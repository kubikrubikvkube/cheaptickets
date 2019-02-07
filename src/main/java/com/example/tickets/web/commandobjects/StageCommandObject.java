package com.example.tickets.web.commandobjects;

import lombok.Data;

@Data
public class StageCommandObject implements CommandObject {
    private String stageName;
}
