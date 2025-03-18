package com.example.handlers.external;

import com.sap.cds.Result;
import com.sap.cds.ql.cqn.CqnAnalyzer;
import com.sap.cds.reflect.CdsModel;
import com.sap.cds.services.cds.ApplicationService;
import com.sap.cds.services.cds.CdsReadEventContext;
import com.sap.cds.services.cds.CqnService;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.After;
import com.sap.cds.services.handler.annotations.Before;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.services.persistence.PersistenceService;
import gen.warehouseservice.BooksLeft_;
import gen.warehouseservice.Count_;
import gen.warehouseservice.WarehouseService_;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * This class mocks the event emitting of the S/4 API
 */
@Component
@Profile("mocked")
@ServiceName(value = {WarehouseService_.CDS_NAME,
    "WarehouseService-mocked"}, type = ApplicationService.class)
public class WarehouseServiceEventMockHandler implements EventHandler {

  private final static Logger logger = LoggerFactory.getLogger(
      WarehouseServiceEventMockHandler.class);
  private final PersistenceService db;
  private final CqnAnalyzer cqnAnalyzer;

  @Autowired
  public WarehouseServiceEventMockHandler(PersistenceService db, CdsModel cdsModel) {
    this.db = db;
    this.cqnAnalyzer = CqnAnalyzer.create(cdsModel);
  }

  @Before(event = CqnService.EVENT_READ, entity = Count_.CDS_NAME)
  public void beforeCountRead(CdsReadEventContext context) {
    logger.info("beforeCountRead");
  }

  @On(event = CqnService.EVENT_READ, entity = Count_.CDS_NAME)
  public void onCountRead(CdsReadEventContext context) {
    logger.info("mock local destination call");
    context.setResult(Collections.EMPTY_LIST);
  }

  private Result getRun(CdsReadEventContext context) {
    return db.run(context.getCqn());
  }

  @Before(event = CqnService.EVENT_READ, entity = BooksLeft_.CDS_NAME)
  public void beforeBooksLeftRead(CdsReadEventContext context) {
    logger.info("beforeBooksLeftRead");
  }

  @On(event = CqnService.EVENT_READ, entity = BooksLeft_.CDS_NAME)
  public void onBooksLeftRead(CdsReadEventContext context) {
    logger.info("onBooksLeftRead");
  }

  @After(event = CqnService.EVENT_READ, entity = BooksLeft_.CDS_NAME)
  public void afterBooksLeftRead(CdsReadEventContext context) {
    logger.info("afterBooksLeftRead");
  }
}
