# A little project created with [SAP CAP](https://cap.cloud.sap/docs/get-started/) framework

## This project was developed following the tutorial: [SAP Tutorial: App initialization, creating a model and API, manual API testing in CAP JAVA](https://medium.com/nerd-for-tech/sap-tutorial-complete-cap-java-part-1-fc1868c7bbba)

### Key Takeaways for Backend Developers:
 - `db/` → define your [Entities & Type Definitions](https://cap.cloud.sap/docs/cds/cdl#entities-type-definitions) here.
 - [CDS Common Types](https://cap.cloud.sap/docs/cds/common) → use [Common Types and Aspects](https://cap.cloud.sap/docs/cds/common) from `@sap/cds/common` such as `cuid`, `managed`, etc.
 - Exposing Entities → Define your service in `srv/<service-name>.cds`, specifying which entities to expose ([docs](https://cap.cloud.sap/docs/cds/cdl#services)) .
 - Custom [Events & Handlers](https://cap.cloud.sap/docs/java/event-handlers/) → Define custom events in your `<service-name>.cds` and handle them using an [Event Handler class](https://cap.cloud.sap/docs/java/event-handlers/) with `@Before`, `@On` and `@After` annotations.
 - Query Analysis & Persistence → Utilize [CqnAnalyzer](https://cap.cloud.sap/docs/java/working-with-cql/query-introspection) to analyze [CQN queries](https://cap.cloud.sap/docs/cds/cqn) and [PersistenceService](https://cap.cloud.sap/docs/java/cqn-services/persistence-services) as your database client.
 - [Generated Accessor Interfaces](https://cap.cloud.sap/docs/java/cds-data#generated-accessor-interfaces) → Use [Generated Accessor Interfaces](https://cap.cloud.sap/docs/java/cds-data#generated-accessor-interfaces) to reference field names, events, and entities.
 - Custom API Endpoints → Use `@path: 'myCustomServicePath'` to change endpoint, ([otherwise endpoint of the exposed service will be constructed by its name](https://cap.cloud.sap/docs/cds/cdl#service-definitions)).
 - [Model Imports](https://cap.cloud.sap/docs/cds/cdl#model-imports) → Leverage [Model Imports](https://cap.cloud.sap/docs/cds/cdl#model-imports) to reuse definitions from other CDS models.
 - Single Entry Point for Entities → Create a single file (for example `db/index.cds`) to import all necessary entities for better maintainability.

### [Deploy CAP Java App to SAP Business Technology Platform](https://developers.sap.com/tutorials/cp-cap-java-deploy-cf.html)
* First automatically add configurations for Hana, authentication and routing: `cds add hana,mta,xsuaa,approuter --for production`
* Configure generated `xs-security.json`
* Don't forget set up routing in `app/xs-app.json`
* Build project `mbt build -t gen --mtar mta.mtar`
* And deploy `cf deploy gen/mta.mtar`

### [Enable Multitenancy](https://cap.cloud.sap/docs/guides/multitenancy/#enable-multitenancy)
* To enable multitenancy for production, run the following command: `cds add multitenancy --for production`
* [Configuring the Required Services](https://cap.cloud.sap/docs/java/multitenancy-classic#required-services-mt):
  1. Remove Existing HDI Configuration
     - If you added multitenancy to an existing project, remove the HDI configuration from the `resources` section.
  2. Configure the Service Manager
     - Instead of HDI, configure the [Service Manager](https://cap.cloud.sap/docs/java/multitenancy-classic#required-services-mt) to dynamically create database containers per tenant at runtime.
  3. Set Up the [MTX Sidecar](https://cap.cloud.sap/docs/java/multitenancy-classic#mtx-sidecar-server)
     - Update your `xs-security-mt.json` file:
         - Add the `mtcallback` scope to enable subscription via the SaaS Registry.
         - Add the `mtdeployment` scope to trigger database artifact re-deployment when needed.
     - Modify your `mta.json` file:
         - Ensure that `mtx-sidecar` requires instances of the UAA Service and Service Manager.

## Useful notes:

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

### Useful links:
 - [cloud-cap-samples Bookshop](https://github.com/SAP-samples/cloud-cap-samples/tree/main/bookshop)
 - [Official CAP Samples for Java](https://github.com/SAP-samples/cloud-cap-samples-java)