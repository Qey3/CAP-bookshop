package com.example.handlers;

import com.sap.cds.services.auditlog.AuditLogService;
import com.sap.cds.services.auditlog.ConfigChangeLogContext;
import com.sap.cds.services.auditlog.DataAccessLogContext;
import com.sap.cds.services.auditlog.DataModificationLogContext;
import com.sap.cds.services.auditlog.SecurityLogContext;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ServiceName(value = "*", type = AuditLogService.class)
public class CustomAuditLogHandler implements EventHandler {

  @On
  public void handleDataAccessEvent(DataAccessLogContext context) {
    // custom handler code
  }

  @On
  public void handleDataModificationEvent(DataModificationLogContext context) {
    // custom handler code
  }

  @On
  public void handleConfigChangeEvent(ConfigChangeLogContext context) {
    // custom handler code
  }

  @On
  public void handleSecurityEvent(SecurityLogContext context) {
    // custom handler code
  }

}
