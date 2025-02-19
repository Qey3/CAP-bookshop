package com.example.handlers;

import com.sap.cds.Result;
import com.sap.cds.ql.Insert;
import com.sap.cds.ql.cqn.AnalysisResult;
import com.sap.cds.ql.cqn.CqnAnalyzer;
import com.sap.cds.ql.cqn.CqnSelect;
import com.sap.cds.reflect.CdsModel;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.services.persistence.PersistenceService;
import gen.catalogservice.Books;
import gen.catalogservice.BooksAddReviewContext;
import gen.catalogservice.CatalogService_;
import gen.catalogservice.Reviews;
import gen.catalogservice.Reviews_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ServiceName(CatalogService_.CDS_NAME)
public class CatalogServiceHandler implements EventHandler {

  private final PersistenceService db;
  private final CqnAnalyzer cqnAnalyzer;

  @Autowired
  public CatalogServiceHandler(PersistenceService db, CdsModel cdsModel) {
    this.db = db;
    this.cqnAnalyzer = CqnAnalyzer.create(cdsModel);
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
}
