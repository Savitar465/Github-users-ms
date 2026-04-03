package com.inspire.msusuarios.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolLinkRequest {
    String rolId;
    boolean vincular;
}
