package com.example.handlers;

import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import gen.catalogservice.BooksAddReviewContext;
import gen.catalogservice.CatalogService_;
import gen.catalogservice.Reviews;
import org.springframework.stereotype.Component;

@Component
@ServiceName(CatalogService_.CDS_NAME)
public class CatalogServiceHandler implements EventHandler {

  @On(event = BooksAddReviewContext.CDS_NAME)
  public void addReview(BooksAddReviewContext context) {
    Reviews reviews = Reviews.create();
    reviews.setTitle(context.getTitle());
    reviews.setRating(context.getRating());
    reviews.setText(context.getText());

    context.setResult(reviews);
  }
}
