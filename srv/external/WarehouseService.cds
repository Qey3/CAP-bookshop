/* checksum : 35d8335b2e5c1b32f944c886dbeea305 */
/** Aspect for entities with canonical universal IDs See https://cap.cloud.sap/docs/cds/common#aspect-cuid */
@cds.external : true
@cds.persistence.skip : true
entity WarehouseService.Count {
  @Core.ComputedDefaultValue : true
  key ID : UUID not null;
  name : LargeString;
  count : Integer;
};

@cds.external : true
service WarehouseService {};

