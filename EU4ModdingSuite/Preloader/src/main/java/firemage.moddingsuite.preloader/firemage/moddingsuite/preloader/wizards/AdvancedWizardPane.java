package firemage.moddingsuite.preloader.wizards;

import javafx.scene.Node;
import org.controlsfx.dialog.WizardPane;
import org.controlsfx.validation.ValidationSupport;

abstract class AdvancedWizardPane extends WizardPane {
    private ValidationSupport validation = new ValidationSupport();

    AdvancedWizardPane() {
        this.setHeader(createHeader());
        this.setContent(createContent());

        setButtons();
    }

    ValidationSupport getValidation() {
        return validation;
    }

    abstract Node createHeader();
    abstract Node createContent();

    void setButtons() {
        //this.getButtonTypes().addAll(ButtonType.NEXT, ButtonType.CANCEL);
    }
}
