using {WarehouseService as warehouse} from '../srv/external/WarehouseService';

extend service warehouse with{};

@cds.persistence:{table,skip:false} //> create a table with the view's inferred signature
@cds.autoexpose //> auto-expose in services as targets for ValueHelps and joins
entity Destination as
  projection on warehouse.Count {
    key ID    as ID,
        name  as bookname,
        count as booksleft,
  };
