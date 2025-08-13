package com.todai.BE.dto.response.user;

import com.todai.BE.entity.ShareRange;


import java.util.UUID;

public record UpdateShowRangeResponseDTO (
        UUID guardianId,
        ShareRange updatedRange
){

}
