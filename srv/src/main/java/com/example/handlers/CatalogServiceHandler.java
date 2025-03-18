package com.example.handlers;

import com.sap.cds.ql.Insert;
import com.sap.cds.ql.Select;
import com.sap.cds.ql.cqn.AnalysisResult;
import com.sap.cds.ql.cqn.CqnAnalyzer;
import com.sap.cds.ql.cqn.CqnSelect;
import com.sap.cds.reflect.CdsModel;
import com.sap.cds.services.cds.CdsReadEventContext;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.After;
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
import gen.warehouseservice.BooksLeft;
import gen.warehouseservice.BooksLeft_;
import gen.warehouseservice.WarehouseService;
import gen.warehouseservice.WarehouseService_;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@ServiceName(CatalogService_.CDS_NAME)
public class CatalogServiceHandler implements EventHandler {

  private final static Logger log = LoggerFactory.getLogger(CatalogServiceHandler.class);
  private final PersistenceService db;
  private final CqnAnalyzer cqnAnalyzer;
  private final Messages messages;
  private final WarehouseService warehouseService;

  @Autowired
  public CatalogServiceHandler(PersistenceService db, CdsModel cdsModel, Messages messages,
      @Qualifier(WarehouseService_.CDS_NAME) WarehouseService warehouseService) {

    this.db = db;
    this.cqnAnalyzer = CqnAnalyzer.create(cdsModel);
    this.messages = messages;
    this.warehouseService = warehouseService;
  }

  @Before(event = BooksAddReviewContext.CDS_NAME)
  public void validateReviewPrice(BooksAddReviewContext context) {
    if (context.getRating() < 0 || context.getRating() > 5) {
      messages.error("Unappropriated rating, should be between 0 and 5");
    }
  }

  @On(event = BooksAddReviewContext.CDS_NAME)
  public void addReview(BooksAddReviewContext context) {
    System.out.println("Adding review for " + context);
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
    System.out.println("saved review " + newResult);
    context.setResult(newResult);
  }

  @Before(entity = Books_.CDS_NAME)
  public void beforeRead(CdsReadEventContext context) {
    log.info("before read");
  }

  @After(entity = Books_.CDS_NAME)
  public void afterRead(CdsReadEventContext context) {
    log.info("afterRead books ");

    List<Books> books = context.getResult().listOf(Books.class);
    List<String> titleList = books.stream()
        .map(Books::getTitle)
        .collect(Collectors.toList());

    CqnSelect select = Select.from(BooksLeft_.class)
        .where(count -> count.bookname().in(titleList));
    List<BooksLeft> destinations = warehouseService.run(select).listOf(BooksLeft.class);

    Map<String, Integer> countMap = destinations.stream()
        .filter(booksLeft -> StringUtils.isNotEmpty(booksLeft.getBookname()))
        .filter(booksLeft -> Objects.nonNull(booksLeft.getBooksleft()))
        .collect(Collectors.toMap(BooksLeft::getBookname, BooksLeft::getBooksleft));

    books.forEach(
        book -> book.setTitle(
            book.getTitle() + "(" + countMap.getOrDefault(book.getTitle(), 0) + ")"));

    log.info("log Validating review price for {}", destinations);
//    System.out.println("sout Validating review price for " + destinations);
  }
}
