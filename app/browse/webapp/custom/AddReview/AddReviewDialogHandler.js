sap.ui.define([
  "sap/ui/core/Fragment",
  "./createAddReviewFormContainer",
  "sap/ui/model/json/JSONModel",
], function (Fragment, createAddReviewFormContainer, JSONModel) {
  "use strict";

  const getAddReviewDialog = (oEvent) => oEvent.getSource().getParent();

  const setInputError = (oInputElement, bHasError) => {
    const oFormErrorModel = oInputElement.getModel("formErrors");
    const oInputErrors = {...oFormErrorModel.getProperty("/inputErrors")};
    oInputErrors [oInputElement.getId()] = bHasError;
    oFormErrorModel.setProperty("/inputErrors", oInputErrors);
  };

  return {
    beforeOpenDialog: function (oEvent, oParam) {
      console.log("BEFORE OPEN RAN");
      console.log("PARAMS", oParam);

      const {sRowBindingPath, sReviewDialogId} = oParam;
      const oAddReviewForm = Fragment.byId(sReviewDialogId, "addReviewForm");
      console.log("oAddReviewForm");
      oAddReviewForm.bindAggregation("formContainers", {
        path: `${sRowBindingPath}/reviews`,
        template: createAddReviewFormContainer(),
        length: 1,
        parameters: {
          $$updateGroupId: "reviews",
        },
      });

      const oReviewBinding = oAddReviewForm.getBinding("formContainers");
      oReviewBinding.create({
        rating: 0,
        title: "",
        text: "",
      });

      const setInputError = (oInputElement, bHasError) => {
        const oFormErrorModel = oInputElement.getModel("formErrors");
        const oInputErrors = {...oFormErrorModel.getProperty("/inputErrors")};
        oInputErrors [oInputElement.getId()] = bHasError;
        oFormErrorModel.setProperty("/inputErrors", oInputErrors);
      };

      const oFormErrorModel = new JSONModel({
        get hasErrors() {
          return Object.values(this.inputErrors).some((error) => error);
        },
        inputErrors: {},
      });

      oEvent.getSource().setModel(oFormErrorModel, "formErrors");
    },

    submit: async function (oEvent) {
      console.log("submit");
      const oAddReviewDialog = getAddReviewDialog(oEvent);
      oAddReviewDialog.setBusy(true);

      try {
        await oAddReviewDialog.getModel().submitBatch("reviews");
        console.log("SUCCESS");
        oAddReviewDialog.close();
      } catch (error) {
        console.log(`ERROR: ${error.message}`);
      } finally {
        oAddReviewDialog.setBusy(false);
      }
    },

    cancel: function (oEvent) {
      const oBooklistPage = sap.ui.getCore().byId("bookshop::BooksList");
      const sReviewDialogId = `${oBooklistPage.getId()}-AddReviewDialog`;

      const oAddReviewForm = Fragment.byId(
          sReviewDialogId,
          "addReviewForm"
      );

      const oReviewBinding = oAddReviewForm.getBinding("formContainers");
      oReviewBinding.resetChanges();
      getAddReviewDialog(oEvent).close();
    },

    onValidationError: function (oEvent) {
      const oInputElement = oEvent.getParameter("element");
      setInputError(oInputElement, true);
    },

    onValidationSuccess: function (oEvent) {
      const oInputElement = oEvent.getParameter("element");
      setInputError(oInputElement, false);
    },
  };
});