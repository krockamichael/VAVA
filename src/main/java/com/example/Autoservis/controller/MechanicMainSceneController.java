package com.example.Autoservis.controller;

import com.example.Autoservis.bean.*;
import com.example.Autoservis.config.StageManager;
import com.example.Autoservis.services.*;
import com.example.Autoservis.view.FxmlView;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class MechanicMainSceneController implements Initializable {
    protected static String global_lang = "eng";
    @FXML private TableView<Cars> carsTable;
    @FXML private TableColumn<Cars,String> modelCol;
    @FXML private TableColumn<Cars,String> typeCol;
    @FXML private TableColumn<Cars,String> vinCol;
    @FXML private TableColumn<Cars,String> carIdCol;
    @FXML private DatePicker startDate;
    @FXML private DatePicker finishDate;
    @FXML private TextArea repair;
    @FXML private TextField carModel;
    @FXML private TextField carType;
    @FXML private TextField carVIN;
    @FXML private TextField repairCost;
    @FXML private Label repairAdded;
    @FXML private Label errorMess;
    @FXML public Button componentSelection;

    @FXML private TableView<Components> componentsTableC;
    @FXML private TableColumn<Components,String> componentColC;
    @FXML private TableColumn<Components,String> carTypeColC;
    @FXML private TableColumn<Components,String> amountColC;
    @FXML private TableColumn<Components,String> IdColC;
    @FXML private TextField nameTextC;
    @FXML private TextField carTypeTextC;

    @Autowired private CarsService carsService;
    @Autowired private RepairsService repairsService;
    @Autowired private ComponentService componentService;
    @Autowired private ComponentsService componentsService;
    @Lazy @Autowired private StageManager stageManager;

    private static final Logger logger = Logger.getLogger(MechanicMainSceneController.class.getName());

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        modelCol.setCellValueFactory(new PropertyValueFactory<>("Model"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("Brand"));
        vinCol.setCellValueFactory(new PropertyValueFactory<>("Vin"));
        carIdCol.setCellValueFactory(new PropertyValueFactory<>("carId"));
    }

    @FXML
    protected void logout() {
        // keep selected language
        if (global_lang.equals("eng")) stageManager.switchScene(FxmlView.LOGIN);
        else stageManager.switchScene(FxmlView.LOGIN_SVK);
    }

    @FXML
    protected void closeStageAction(ActionEvent event){
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    protected void filter() {
        // get cars based on car model, brand and VIN number and set them to table
        ObservableList<Cars> lCar = FXCollections.observableArrayList(carsService.getCarsMaybe(carModel.getText(), carType.getText(), carVIN.getText()));

        if (lCar.isEmpty())
            logger.log(Level.WARNING,"Cars list is empty");
        else
            carsTable.setItems(lCar);
    }

    @FXML
    protected void addNewRepair() {
        // hide error messages
        repairAdded.setVisible(false);
        errorMess.setVisible(false);

        // show error if no car is selected
        if (carsTable.getSelectionModel().getSelectedItem() == null) {
            logger.log(Level.WARNING,"Add new repair - no car selected - aborting");
            errorMess.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  errorMess.setVisible(false));
            visibleP.play();
            return;
        }

        // show error if repair cost is not specified
        if (repairCost.getText().equals("")) {
            logger.log(Level.WARNING,"Add new repair - invalid cost - aborting");
            errorMess.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  errorMess.setVisible(false));
            visibleP.play();
            return;
        }
        double cost = Double.parseDouble(repairCost.getText());

        int carId = (int) carsTable.getSelectionModel().getSelectedItem().getCar_id();
        int mechId = MainController.uid;
        System.out.println("mechId je: " + mechId);

        String rep = repair.getText();
        // check if there was no error loading data
        if (carId != 0 && mechId != 0 && !rep.equals("") && startDate.getValue() != null && finishDate.getValue() != null) {
            Date start_date = Date.valueOf(startDate.getValue());
            Date finish_date = Date.valueOf(finishDate.getValue());

            // check if a component is selected
            if (componentSelection.getUserData() == null) {
                Repairs nRepair = new Repairs(carId, start_date, finish_date, mechId, rep, cost);
                repairsService.save(nRepair);
                logger.log(Level.INFO,"New repair added to database Repairs");
            } else {
                Repairs nRepair = new Repairs(carId, start_date, finish_date, mechId, rep, cost);
                Repairs saveRepair = repairsService.save(nRepair);
                logger.log(Level.INFO,"New repair added to database Repairs");

                String cId = componentSelection.getUserData().toString();

                // add component to database
                Component nComponent = new Component();
                nComponent.setComponents_id(Integer.parseInt(cId));
                nComponent.setRepair_id((int) saveRepair.getRepair_id());
                componentService.save(nComponent);
                logger.log(Level.INFO,"New component added to database Components");

                // update the amount of componentSSS in database
                Components aComponent = componentsService.findByComponentId(Integer.parseInt(cId));
                aComponent.setAmount(aComponent.getAmount() - 1);
                componentsService.update(aComponent);
                logger.log(Level.INFO,"Component amount updated");
            }
            repair.setText("");
            repairCost.setText("");
            carModel.setText("");
            carVIN.setText("");
            carType.setText("");
            startDate.setValue(null);
            finishDate.setValue(null);
            if (global_lang.equals("eng")){componentSelection.setText("Select a Component");}
            else{componentSelection.setText("Vyberte komponent");}

            repairAdded.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  repairAdded.setVisible(false));
            visibleP.play();
        }
        else {
            logger.log(Level.WARNING,"Add new repair - missing values - aborting");
            // if some fields are not filled, generate error message
            errorMess.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  errorMess.setVisible(false));
            visibleP.play();
        }
    }

    private String repairCostM;
    private String repairTextM;
    private String modelCarM;
    private String modelTypeM;
    private String modelVinM;
    private LocalDate startingDateM;
    private LocalDate finishDateM;
    private ObservableList<Cars> items;

    @FXML
    protected void loadComponentSelection() {
        // save text field values so they don't disappear when changing scenes
        repairCostM = repairCost.getText();
        startingDateM = startDate.getValue();
        finishDateM = finishDate.getValue();
        repairTextM = repair.getText();
        modelCarM = carModel.getText();
        modelTypeM = carType.getText();
        modelVinM = carVIN.getText();
        items = carsTable.getItems();
        stageManager.switchScene(FxmlView.COMPONENT_SELECTION);

        // keep selected language
        if (global_lang.equals("eng")) compSel_changeToEnglishLang();
        else compSel_changeToSlovakLang();

        // set the table
        componentColC.setCellValueFactory(new PropertyValueFactory<>("Name"));
        carTypeColC.setCellValueFactory(new PropertyValueFactory<>("CarType"));
        IdColC.setCellValueFactory(new PropertyValueFactory<>("ComponentId"));
        amountColC.setCellValueFactory(new PropertyValueFactory<>("Amount"));
    }

    @FXML
    protected void filterComponents() {
        // get components based on component name and car model
        ObservableList<Components> components = FXCollections.observableArrayList(componentsService.getComponents(nameTextC.getText(), carTypeTextC.getText()));

        if (components.isEmpty())
            logger.log(Level.WARNING,"Components list is empty");
        else
            componentsTableC.setItems(components);
    }

    @FXML
    private void selectC() {
        stageManager.switchScene(FxmlView.MECHANIC_SCENE);

        // keep selected language
        if (global_lang.equals("eng")) mechanicMainScene_changeToEnglishLang();
        else mechanicMainScene_changeToSlovakLang();

        // set text field values
        repairCost.setText(repairCostM);
        startDate.setValue(startingDateM);
        finishDate.setValue(finishDateM);
        repair.setText(repairTextM);
        carModel.setText(modelCarM);
        carType.setText(modelTypeM);
        carVIN.setText(modelVinM);
        carsTable.setItems(items);

        // check if a component was selected
        if (componentsTableC.getSelectionModel().getSelectedItem() != null) {
            // get component based on name and car brand
            componentSelection.setText(componentsTableC.getSelectionModel().getSelectedItem().getName() + " " +
                                       componentsTableC.getSelectionModel().getSelectedItem().getCarType());
            componentSelection.setUserData(componentsTableC.getSelectionModel().getSelectedItem().getComponentId());
        }
    }

    /**
     LANGUAGE FUNCTIONS - ENGLISH & SLOVAK
     - each screen has a specific set of labels, buttons and / or pre-filled text fields
     - when changing scenes, language functions are called to keep the selected language
     - language can be changed by clicking EN / SK language buttons in the application
     */

    @FXML private Tab newRepair_tab;
    @FXML private Label mechMainScene_brand_label;
    @FXML private Label mechMainScene_VIN_label;
    @FXML private Label mechMainScene_repairCost_label;
    @FXML private Label mechMainScene_startDate_label;
    @FXML private Label mechMainScene_finishDate_label;
    @FXML private Label mechMainScene_componentUsed_label;
    @FXML private Label mechMainScene_repair_label;
    @FXML private Button mechMainScene_filter_btn;
    @FXML private Button mechMainScene_addRepair_btn;

    @FXML
    public void mechanicMainScene_changeToEnglishLang() {
        typeCol.setText("Brand");
        componentSelection.setText("Select a component");
        newRepair_tab.setText("New repair");
        mechMainScene_brand_label.setText("Brand :");
        mechMainScene_VIN_label.setText("VIN number :");
        mechMainScene_repairCost_label.setText("Repair cost :");
        mechMainScene_startDate_label.setText("Starting date :");
        mechMainScene_finishDate_label.setText("Finish date :");
        mechMainScene_componentUsed_label.setText("Components used :");
        mechMainScene_repair_label.setText("Repair :");
        mechMainScene_filter_btn.setText("Filter");
        mechMainScene_addRepair_btn.setText("Add repair");
        if (repairAdded != null) { repairAdded.setText("Repair added"); }
        if (errorMess != null) { errorMess.setText("Missing fields!"); }
        global_lang = "eng";
    }

    @FXML
    public void mechanicMainScene_changeToSlovakLang() {
        typeCol.setText("Značka");
        componentSelection.setText("Vyberte komponent");
        newRepair_tab.setText("Nová oprava");
        mechMainScene_brand_label.setText("Značka :");
        mechMainScene_VIN_label.setText("VIN číslo :");
        mechMainScene_repairCost_label.setText("Cena opravy :");
        mechMainScene_startDate_label.setText("Dátum začiatku :");
        mechMainScene_finishDate_label.setText("Dátum konca :");
        mechMainScene_componentUsed_label.setText("Použitý komponent :");
        mechMainScene_repair_label.setText("Opis opravy :");
        mechMainScene_filter_btn.setText("Filtrovať");
        mechMainScene_addRepair_btn.setText("Pridať opravu");
        if (repairAdded != null) { repairAdded.setText("Oprava pridaná"); }
        if (errorMess != null) { errorMess.setText("Chýbajú hodnoty!"); }
        global_lang = "svk";
    }

    @FXML private Label compSel_title_label;
    @FXML private Label compSel_filterBy_label;
    @FXML private Label compSel_results_label;
    @FXML private Label compSel_component_label;
    @FXML private Label compSel_carBrand_label;
    @FXML private Button compSel_filter_btn;
    @FXML private Button compSel_select_btn;

    @FXML
    private void compSel_changeToEnglishLang() {
        componentColC.setText("Component");
        carTypeColC.setText("Car Brand");
        amountColC.setText("Amount");
        compSel_title_label.setText("Component selection");
        compSel_filterBy_label.setText("Filter by:");
        compSel_results_label.setText("Results:");
        compSel_component_label.setText("Component :");
        compSel_carBrand_label.setText("Car brand :");
        compSel_filter_btn.setText("Filter");
        compSel_select_btn.setText("Select");
        global_lang = "eng";
    }

    @FXML
    private void compSel_changeToSlovakLang() {
        componentColC.setText("Komponent");
        carTypeColC.setText("Značka auta");
        amountColC.setText("Počet");
        compSel_title_label.setText("Výber komponentu");
        compSel_filterBy_label.setText("Filtrovať podľa:");
        compSel_results_label.setText("Výsledky:");
        compSel_component_label.setText("Komponent :");
        compSel_carBrand_label.setText("Značka auta :");
        compSel_filter_btn.setText("Filtrovať");
        compSel_select_btn.setText("Vybrať");
        global_lang = "svk";
    }
}