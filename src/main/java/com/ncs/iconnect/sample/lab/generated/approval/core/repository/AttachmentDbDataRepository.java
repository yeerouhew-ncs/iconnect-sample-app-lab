package com.ncs.iconnect.sample.lab.generated.approval.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ncs.iconnect.sample.lab.generated.approval.core.domain.AttachmentDbData;

public interface AttachmentDbDataRepository extends JpaRepository<AttachmentDbData, Long>, JpaSpecificationExecutor<AttachmentDbData> {

}
