package cn.structured.org.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "成员审核")
public class MemberAuditDTO {

    @NotNull
    @Schema(description = "申请ID")
    private Long id;
    @Schema(description = "审核状态 2:通过 3:拒绝")
    private Integer state;
    @Schema(description = "拒绝理由")
    private String rejectReason;
}
