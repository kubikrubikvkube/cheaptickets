package com.example.tickets.web.commandobjects;

import com.example.tickets.owner.OwnerDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class OwnerDtoCommandObject extends OwnerDto implements CommandObject {
    private Long id;
}
