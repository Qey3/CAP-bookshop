namespace com.example.bookshop;

using {com.example.bookshop as bookshop} from '../index';
using {
  cuid,
  managed
} from '@sap/cds/common';

entity Reviews : cuid, managed {
  book   : Association to bookshop.Books;
  rating : bookshop.Rating;
  title  : bookshop.Name @mandatory;
  text   : bookshop.Text @mandatory;
}
