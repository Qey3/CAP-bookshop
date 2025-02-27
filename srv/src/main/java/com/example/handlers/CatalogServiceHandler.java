package com.example.handlers;

import com.sap.cds.ql.Insert;
import com.sap.cds.ql.cqn.AnalysisResult;
import com.sap.cds.ql.cqn.CqnAnalyzer;
import com.sap.cds.ql.cqn.CqnSelect;
import com.sap.cds.reflect.CdsModel;
import com.sap.cds.services.EventContext;
import com.sap.cds.services.cds.CdsReadEventContext;
import com.sap.cds.services.cds.CqnService;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.Before;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.services.messages.Messages;
import com.sap.cds.services.persistence.PersistenceService;
import gen.catalogservice.Books;
import gen.catalogservice.BooksAddReviewContext;
import gen.catalogservice.Books_;
import gen.catalogservice.CatalogService_;
import gen.catalogservice.Reviews;
import gen.catalogservice.Reviews_;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ServiceName(CatalogService_.CDS_NAME)
public class CatalogServiceHandler implements EventHandler {

  private final PersistenceService db;
  private final CqnAnalyzer cqnAnalyzer;
  private final Messages messages;

  @Autowired
  public CatalogServiceHandler(PersistenceService db, CdsModel cdsModel, Messages messages) {
    this.db = db;
    this.cqnAnalyzer = CqnAnalyzer.create(cdsModel);
    this.messages = messages;
  }

  @Before
  public void validateReviewPrice(BooksAddReviewContext context) {
    if (context.getRating() < 0 || context.getRating() > 5) {
      messages.error("Unappropriated rating, should be between 0 and 5");
    }
  }

  @On(event = BooksAddReviewContext.CDS_NAME)
  public void addReview(BooksAddReviewContext context) {
    CqnSelect select = context.getCqn();
    AnalysisResult analysisResult = cqnAnalyzer.analyze(select);
    String bookId = analysisResult.targetKeys().get(Books.ID).toString();
    System.out.println(analysisResult);

    Reviews reviews = Reviews.create();
    reviews.setBookId(bookId);
    reviews.setTitle(context.getTitle());
    reviews.setRating(context.getRating());
    reviews.setText(context.getText());

    Insert insert = Insert.into(Reviews_.CDS_NAME).entry(reviews);
    Reviews newResult = db.run(insert).single(Reviews.class);

    context.setResult(newResult);
  }

  @Before(entity = Books_.CDS_NAME)
  public void beforeRead(CdsReadEventContext context) {
    System.out.println("beforeRead");
  }
}
