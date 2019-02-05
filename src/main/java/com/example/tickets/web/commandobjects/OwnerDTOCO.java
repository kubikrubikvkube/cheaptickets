package com.example.tickets.web.commandobjects;

import com.example.tickets.owner.OwnerDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OwnerDTOCO extends OwnerDTO implements CommandObject {
    private Long id;
}
