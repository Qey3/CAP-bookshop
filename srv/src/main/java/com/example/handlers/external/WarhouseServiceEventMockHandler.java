package com.example.handlers.external;

import com.sap.cds.services.cds.ApplicationService;
import com.sap.cds.services.cds.CdsUpdateEventContext;
import com.sap.cds.services.cds.CqnService;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.After;
import com.sap.cds.services.handler.annotations.ServiceName;
import gen.Destination_;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * This class mocks the event emitting of the S/4 API
 */
@Component
@Profile("!cloud")
@ServiceName(value = {Destination_.CDS_NAME,
    "WarehouseService-mocked"}, type = ApplicationService.class)
public class WarhouseServiceEventMockHandler implements EventHandler {

  @After(event = CqnService.EVENT_UPDATE, entity = Destination_.CDS_NAME)
  public void businessPartnerChanged(CdsUpdateEventContext context) {
    System.out.println("Business Partner Changed");
  }
}
