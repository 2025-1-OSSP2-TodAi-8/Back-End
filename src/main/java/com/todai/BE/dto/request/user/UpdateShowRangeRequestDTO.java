package com.todai.BE.dto.request.user;
import com.todai.BE.entity.ShareRange;

import java.util.UUID;

public record UpdateShowRangeRequestDTO (
        UUID guardianId,
        ShareRange showRange
){
}
