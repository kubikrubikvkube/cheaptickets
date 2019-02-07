package com.example.tickets.stages.stage;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StageResult {
    String stage;
    long savedObjects;
    long updatedObjects;
    long deletedObjects;
}
