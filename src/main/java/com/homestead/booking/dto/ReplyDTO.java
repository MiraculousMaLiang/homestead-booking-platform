package com.homestead.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReplyDTO {
    @NotNull(message = "评价ID不能为空")
    private Long reviewId;
    @NotBlank(message = "回复内容不能为空")
    private String replyContent;
}
