using {com.example.bookshop as bookshop} from '../db/index';

annotate bookshop.Books with {
  author   @title: 'Author';
  genre    @title: 'Genre';
  price    @title: 'Price';
  id       @UI.Hidden;
  descr    @title: 'Description';
  currency @UI.Hidden;
}

annotate bookshop.Reviews with {
  title  @title: 'Title';
  rating @title: 'Rating';
  text   @title: 'Comment';
};
