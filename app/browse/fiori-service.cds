using CatalogService from '../../srv/cat-service';

annotate CatalogService.Books with @(UI : {
  HeaderInfo  : {
      TypeName : 'Book',
      TypeNamePlural : 'Books',
  },
  LineItem : [
      {
        Value: title,
        Label: 'Title'
      },
      {
        Value: author,
      },
      {
        Value: genre,
      },
      {
        Value: price,
      },
      {
        Value: descr,
      },
      {
        Value: id,
        ![@UI.Hidden]
      },
      {
        Value : currency_code,
        ![@UI.Hidden]
      }
  ],

  SelectionFields : [
      author,
      genre
  ],

  PresentationVariant : {
      Text           : 'Default',
      SortOrder      : [{Property : title}],
      Visualizations : ['@UI.LineItem']
  }
}){
     @UI.HiddenFilter
     descr;
     @Measures.ISOCurrency : currency.code
     price
};