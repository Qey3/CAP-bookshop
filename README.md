# A little project created with SAP CAP framework

## This project was built using this tutorial series: [SAP Tutorial: App initialization, creating a model and API, manual API testing in CAP JAVA](https://medium.com/nerd-for-tech/sap-tutorial-complete-cap-java-part-1-fc1868c7bbba)

### CAP project structure:
* my-cap-project/
* │── app/             # UI5 / Fiori / Frontend applications
* │── db/              # Database models, schema definitions, and migrations
* │── srv/             # Service logic (CAP services, handlers)
* │── gen/             # Generated files (after `cds build`)
* │── test/            # Automated test cases
* │── node_modules/    # Dependencies (if using Node.js)
* │── package.json     # Node.js project metadata (if using Node.js)
* │── mta.yaml         # Multi-Target Application (MTA) deployment descriptor (if deploying to SAP BTP)
* │── cds.json         # CAP project configuration
* │── .cdsrc.json      # CAP project settings

### More details about folders purpose:
1. `app/` (Frontend Apps)
Contains SAP Fiori or UI5 applications.
Typically used when building a full-stack app with both UI and backend.
2. `db/` (Database Layer)
Stores all CDS data models (`.cds` files), schema definitions, and migrations.
Example contents:
   - data-model.cds → Defines entities, relationships, field types.
   - data/ → Stores initial data as .csv files for deployment.
   - migrations/ → Database schema migrations.
3. `srv/` (Service Layer)
Defines business logic, including OData services.
Contains `.cds` service definitions and `.java` handlers.
Example:
   - `cat-service.cds` → Defines OData endpoints.
   - `CatalogServiceHandler.java` → Implements custom logic
4. `gen/` (Generated Files inside `srv/` folder)
Auto-generated files from cds build, such as Node.js or Java service artifacts.
Not meant to be manually edited.
5. `test/` (Testing)
Stores integration and unit tests.
Can contain `.http` test scripts or Jest/Mocha tests.
6. Other Important Files:
   - `package.json` → Defines dependencies (if `Node.js` is used).
   - `mta.yaml` → Required for deployment to SAP BTP as an MTA.
   - `cds.json` or `.cdsrc.json` → CAP configuration settings.