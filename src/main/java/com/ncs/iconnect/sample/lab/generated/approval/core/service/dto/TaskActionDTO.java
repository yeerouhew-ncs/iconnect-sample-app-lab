package com.ncs.iconnect.sample.lab.generated.approval.core.service.dto;

public class TaskActionDTO {

    //Task Id for Approver, mapped to approver id
    private Long approverInstanceId;
    private String comment;

    public TaskActionDTO() {

    }

    public TaskActionDTO(String comment) {
        this.comment = comment;
    }

    public Long getApproverInstanceId() {
        return approverInstanceId;
    }

    public void setApproverInstanceId(Long approverInstanceId) {
        this.approverInstanceId = approverInstanceId;
    }

    public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
    
    
}
