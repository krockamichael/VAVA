package com.example.Autoservis.controller;

import com.example.Autoservis.bean.*;
import com.example.Autoservis.config.StageManager;
import com.example.Autoservis.services.*;
import com.example.Autoservis.view.FxmlView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class MechanicMainSceneController implements Initializable {
    private String global_lang = "eng";
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
    @FXML private Label repairAdded;
    @FXML public Button componentSelection;
    @FXML private Label errorMess;
    @FXML private TextField repairCost;
    ////////////////////////////////////////////////////////
    @FXML private TableView<Components> componentsTableC;
    @FXML private TableColumn<Components,String> componentColC;
    @FXML private TableColumn<Components,String> carTypeColC;
    @FXML private TableColumn<Components,String> amountColC;
    @FXML private TableColumn<Components,String> IdColC;
    @FXML private TextField nameTextC;
    @FXML private TextField carTypeTextC;
    ////////////////////////////////////////////////////////
    @Autowired private CarsService carsService;
    @Autowired private RepairsService repairsService;
    @Autowired private ComponentService componentService;
    @Autowired private ComponentsService componentsService;
    @Lazy @Autowired private StageManager stageManager;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        modelCol.setCellValueFactory(new PropertyValueFactory<>("Model"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("Brand"));
        vinCol.setCellValueFactory(new PropertyValueFactory<>("Vin"));
        carIdCol.setCellValueFactory(new PropertyValueFactory<>("carId"));
    }

    @FXML
    protected void Logout(ActionEvent event) throws IOException {
        stageManager.switchScene(FxmlView.Login);
         // keep selected language
        MainController MC = new MainController();
        if (global_lang.equals("eng")) MC.changeToEnglishLang_LoginScreen();
        else MC.changeToSlovakLang_LoginScreen();
    }

    @FXML
    protected void closeStageAction(ActionEvent event){
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    protected void filter() throws SQLException {
        ObservableList<Cars> data = FXCollections.observableArrayList();
        List<Cars> Lcar = carsService.getCarsmaybe(carModel.getText(), carType.getText(), carVIN.getText());

        if (Lcar.size() == 0)
            System.out.println("No result");
        else {
            for (Cars model : Lcar) {
                Cars car;
                car = new Cars();
                car.setModel(model.getModel());
                car.setBrand(model.getBrand());
                car.setVin(model.getVin());
                car.setCar_id(model.getCar_id());
                data.add(car);
            }
            carsTable.setItems(data);
        }
    }

    @FXML
    protected void addNewRepair() {
        int carId;
        repairAdded.setVisible(false);
        errorMess.setVisible(false);
        if (carsTable.getSelectionModel().getSelectedItem()==null){
            errorMess.setVisible(true);
            return;
        }
        carId = (int) carsTable.getSelectionModel().getSelectedItem().getCar_id();
        LocalDate start = startDate.getValue();
        LocalDate finish = finishDate.getValue();
        int mechId = MainController.UID;
        System.out.println("mechId je: "+mechId);
        if (repairCost.getText().equals("")) {
            errorMess.setVisible(true);
            return;
        }
        double cost = Double.parseDouble(repairCost.getText());

        String rep = repair.getText();
        if (carId != 0 && mechId != 0 && !rep.equals("")) {
            if (componentSelection.getUserData() == null) {
                Repairs Nrepair = new Repairs();
                Nrepair.setCarId(carId);
                Nrepair.setStart_day(Date.valueOf(start));
                Nrepair.setEnd_day(Date.valueOf(finish));
                Nrepair.setMechanicId(mechId);
                Nrepair.setRepair(rep);
                Nrepair.setCost(cost);
                Repairs saveRepair = repairsService.save(Nrepair);
            } else {
                Repairs Nrepair = new Repairs();
                Nrepair.setCarId(carId);
                Nrepair.setStart_day(Date.valueOf(start));
                Nrepair.setEnd_day(Date.valueOf(finish));
                Nrepair.setMechanicId(Math.toIntExact(mechId));
                Nrepair.setRepair(rep);
                Nrepair.setCost(cost);
                Repairs saveRepair = repairsService.save(Nrepair);

                Component Ncomponent= new Component();

                String Cid = componentSelection.getUserData().toString();
                Ncomponent.setComponents_id(Integer.parseInt(Cid));

                Ncomponent.setRepair_id((int) saveRepair.getRepair_id());
                componentService.save(Ncomponent);

                Components Ncomponents = new Components();
                Components AComponent = componentsService.findByComponentId(Integer.parseInt(Cid));
                Ncomponents.setAmount(AComponent.getAmount()-1);
                Ncomponents.setComponentId(Integer.parseInt(Cid));
                Ncomponents.setCarType(AComponent.getCarType());
                Ncomponents.setName(AComponent.getName());
                Ncomponents.setCost(AComponent.getCost());
                componentsService.update(Ncomponents);
            }
            repairAdded.setVisible(true);
        }
        else{ errorMess.setVisible(true); }
    }

    public String RepairCostM;
    public String RepairTextM;
    public String ModelCarM;
    public String ModelTypeM;
    public String ModelVinM;
    public LocalDate StartingDateM;
    public LocalDate FinishDateM;
    public ObservableList<Cars> items;

    @FXML
    protected void loadComponentSelection() {
        RepairCostM = repairCost.getText();
        StartingDateM = startDate.getValue();
        FinishDateM = finishDate.getValue();
        RepairTextM = repair.getText();
        ModelCarM = carModel.getText();
        ModelTypeM = carType.getText();
        ModelVinM = carVIN.getText();
        items = carsTable.getItems();
        stageManager.switchScene(FxmlView.ComponentSelection);

        // keep selected language
        if (global_lang.equals("eng")) compSel_changeToEnglishLang();
        else compSel_changeToSlovakLang();

        componentColC.setCellValueFactory(new PropertyValueFactory<>("Name"));
        carTypeColC.setCellValueFactory(new PropertyValueFactory<>("CarType"));
        IdColC.setCellValueFactory(new PropertyValueFactory<>("ComponentId"));
        amountColC.setCellValueFactory(new PropertyValueFactory<>("Amount"));
    }

    @FXML
    protected void filterComponents() {
        List<Components> components = componentsService.getComponents(nameTextC.getText(),carTypeTextC.getText());
        ObservableList<Components> data = FXCollections.observableArrayList();

        if (components.size() == 0)
            System.out.println("No result");
        else {
            for(Components compo : components) {
                Components com;
                com = new Components();
                com.setName(compo.getName());
                com.setAmount(compo.getAmount());
                com.setCarType(compo.getCarType());
                com.setComponentId(compo.getComponentId());
                data.add(com);
            }
            componentsTableC.setItems(data);
        }
    }

    @FXML
    private void selectC(ActionEvent event) {
        stageManager.switchScene(FxmlView.MechanicScene);

        // keep selected language
        if (global_lang.equals("eng")) mechanicMainScene_changeToEnglishLang();
        else mechanicMainScene_changeToSlovakLang();

        repairCost.setText(RepairCostM);
        startDate.setValue(StartingDateM);
        finishDate.setValue(FinishDateM);
        repair.setText(RepairTextM);
        carModel.setText(ModelCarM);
        carType.setText(ModelTypeM);
        carVIN.setText(ModelVinM);
        carsTable.setItems(items);

        if (componentsTableC.getSelectionModel().getSelectedItem() != null) {
            componentSelection.setText(componentsTableC.getSelectionModel().getSelectedItem().getName() + " " + componentsTableC.getSelectionModel().getSelectedItem().getCarType());
            componentSelection.setUserData(componentsTableC.getSelectionModel().getSelectedItem().getComponentId());
        }
    }

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
        if (repairAdded != null) { repairAdded.setText("Repair added!"); }
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
        mechMainScene_componentUsed_label.setText("Použité komponenty :");
        mechMainScene_repair_label.setText("Opis opravy :");
        mechMainScene_filter_btn.setText("Filtrovať");
        mechMainScene_addRepair_btn.setText("Pridať opravu");
        if (repairAdded != null) { repairAdded.setText("Opravy pridané!"); }
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