sap.ui.define([], function () {
  "use strict";

  return {
    notBlank: function (oEvent, oParams) {
      const {oInput = oEvent.getSource()} = oParams || {};
      const isRequired = oInput.getRequired();

      if (isRequired) {
        const sValue = oInput.getValue();
        const sLastValue = oInput.getLastValue();

        if (!sValue) {
          const sFieldLabel = oInput.getParent().getAggregation("label");

          oInput.fireValidationError({
            element: oInput,
            property: isInitital ? null : "value",
            message: isInitital ? null : `${sFieldLabel} field cannot be blank`
          });
          oInput.setLastValue("");
        } else if (sValue && !sLastValue) {
          oInput.fireValidationSuccess({element: oInput, property: "value"});
        }
      }
    }
  };
});