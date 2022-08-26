package study.gongsa.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.sql.Time;
import java.sql.Timestamp;

@ApiModel(value="StudyGroupMakeRequest", description = "스터디 그룹 생성 리퀘스트")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudyGroupMakeRequest {
    @ApiModelProperty(value="그룹명")
    @NotBlank(message = "그룹명은 필수값 입니다")
    String name;

    @ApiModelProperty(value="캠 필수 여부")
    boolean isCam;

    @ApiModelProperty(value="최대 인원 수")
    @Max(value=7, message="최대 인원 수는 7명까지 입니다.")
    int maxMember;

    @ApiModelProperty(value="방 공개 여부")
    boolean isPrivate;

    @ApiModelProperty(value="그룹 카테고리")
    @NotNull(message = "그룹 카테고리는 필수값 입니다")
    int[] groupCategories;

    @ApiModelProperty(value="벌점 유무")
    boolean isPenalty;

    @ApiModelProperty(value="최대 가능 벌점 횟수")
    int maxPenalty;

    @ApiModelProperty(value="휴가 유무")
    boolean isRest;

    @ApiModelProperty(value="최대 휴가 횟수")
    int maxRest;

    @ApiModelProperty(value="스터디 재진입 횟수")
    int maxTodayStudy;

    @ApiModelProperty(value="하루 목표 공부 시간")
    @Min(value=0, message="하루 목표 공부 시간은 최소 0시간입니다.")
    @Max(value=24, message="하루 목표 공부 시간은 최대 24시간입니다.")
    int minStudyHour;

    @ApiModelProperty(value="만료 날짜")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Timestamp expiredAt;
}