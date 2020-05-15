package com.example.Autoservis.controller;

import com.example.Autoservis.bean.*;
import com.example.Autoservis.config.StageManager;
import com.example.Autoservis.services.*;
import com.example.Autoservis.view.FxmlView;
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

    private int offset;

    private Thread loadThread = null;

    @FXML
    private TableView<Cars> carsTable;
    @FXML
    private TableColumn<Cars,String> modelCol;
    @FXML
    private TableColumn<Cars,String> typeCol;
    @FXML
    private TableColumn<Cars,String> vinCol;
    @FXML
    private TableColumn<Cars,String> carIdCol;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker finishDate;
    @FXML
    private TextArea repair;
    @FXML
    private TextField carModel;
    @FXML
    private TextField carType;
    @FXML
    private TextField carVIN;
    @FXML
    private ProgressIndicator loadingIndicator;
    @FXML
    private Label selectedPage;
    @FXML
    private Label repairAdded;
    @FXML
    public Button componentSelection;
    @FXML
    private Label errorMess;
    @FXML
    private TextField repairCost;

    //////////////////////////////////////////////////
    @FXML
    private TableView<Components> componentsTableC;

    @FXML
    private TableColumn<Components,String> componentColC;

    @FXML
    private TableColumn<Components,String> carTypeColC;

    @FXML
    private TableColumn<Components,String> amountColC;

    @FXML
    private TableColumn<Components,String> IdColC;

    @FXML
    private TextField nameTextC;

    @FXML
    private TextField carTypeTextC;
    ////////////////////////////////////////////////////////

    @Autowired
    private CarsService carsService;

    @Autowired
    private RepairsService repairsService;

    @Autowired
    private ComponentService componentService;

    @Autowired
    private ComponentsService componentsService;

    @Autowired
    private MechanicsService mechanicsService;

    @Lazy
    @Autowired
    private StageManager stageManager;

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
    }
    @FXML
    protected void closeStageAction(ActionEvent event){
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    protected void filter() throws SQLException {
        ObservableList<Cars> data = FXCollections.observableArrayList();
        List<Cars> Lcar = carsService.getCarsmaybe(carModel.getText(), carType.getText(), carVIN.getText());

        if(Lcar.size() == 0){
            System.out.println("No result");
        }
        else{
            for(Cars model : Lcar) {
                Cars car = null;
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
    protected void addNewRepair(){
        int carId = 0;
        repairAdded.setVisible(false);
        errorMess.setVisible(false);
        if(carsTable.getSelectionModel().getSelectedItem()==null){
            errorMess.setVisible(true);
            return;
        }
        carId = (int) carsTable.getSelectionModel().getSelectedItem().getCar_id();
        LocalDate start = startDate.getValue();
        LocalDate finish = finishDate.getValue();
        int mechId = MainController.UID;
        System.out.println("mechId je: "+mechId);
        if(repairCost.getText() == ""){
            errorMess.setVisible(true);
            return;
        }
        double cost = Double.parseDouble(repairCost.getText());

        String rep = repair.getText();
        if(carId != 0 && mechId != 0 && !rep.equals("")) {
            if(componentSelection.getUserData() == null)
            {
                Repairs Nrepair = new Repairs();
                Nrepair.setCarId(carId);
                Nrepair.setStart_day(Date.valueOf(start));
                Nrepair.setEnd_day(Date.valueOf(finish));
                Nrepair.setMechanicId(mechId);
                Nrepair.setRepair(rep);
                Nrepair.setCost(cost);
                Repairs saveRepair = repairsService.save(Nrepair);
            }
            else{
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
        else{
            errorMess.setVisible(true);
        }

    }

    @FXML
    protected void loadComponentSelection() throws IOException {
        stageManager.switchScene(FxmlView.ComponentSelection);
        componentColC.setCellValueFactory(new PropertyValueFactory<>("Name"));
        carTypeColC.setCellValueFactory(new PropertyValueFactory<>("CarType"));
        IdColC.setCellValueFactory(new PropertyValueFactory<>("ComponentId"));
        amountColC.setCellValueFactory(new PropertyValueFactory<>("Amount"));
    }

    @FXML
    protected void filterComponents() {
        List<Components> components = componentsService.getComponents(nameTextC.getText(),carTypeTextC.getText());
        ObservableList<Components> data = FXCollections.observableArrayList();

        if(components.size() == 0){
            System.out.println("No result");
        }
        else{
            for(Components compo : components) {
                Components com = null;
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
    private void selectC(ActionEvent event){
        stageManager.switchScene(FxmlView.MechanicScene);
        if(componentsTableC.getSelectionModel().getSelectedItem() != null) {
            componentSelection.setText(componentsTableC.getSelectionModel().getSelectedItem().getName() + " " + componentsTableC.getSelectionModel().getSelectedItem().getCarType());
            componentSelection.setUserData(componentsTableC.getSelectionModel().getSelectedItem().getComponentId());
        }
    }

    /*
    @FXML
    protected void rightPage() {
        offset +=100;
        loadingIndicator.setVisible(true);
        selectedPage.setText(Integer.toString(offset/100+1));
        loadThread = new Thread(() -> {
            ResultSet rs = carsService.getCars(carModel.getText(), carType.getText(), carVIN.getText(), offset);
            ObservableList<Cars> data = FXCollections.observableArrayList();
            if (rs == null)
                System.out.println("No result");
            else {
                while (true) {
                    try {
                        if (!rs.next()) break;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Cars car = null;
                    try {
                        car = new Cars(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    data.add(car);
                }
                carsTable.setItems(data);
            }
            loadingIndicator.setVisible(false);
        });
        loadThread.start();
    }

    @FXML
    protected void leftPage() {
        if(offset >= 100){
            offset-=100;
            loadingIndicator.setVisible(true);
            selectedPage.setText(Integer.toString(offset/100+1));
            loadThread = new Thread(() -> {
                ResultSet rs = carsService.getCars(carModel.getText(), carType.getText(), carVIN.getText(), offset);
                ObservableList<Cars> data = FXCollections.observableArrayList();
                if (rs == null)
                    System.out.println("No result");
                else {
                    while (true) {
                        try {
                            if (!rs.next()) break;
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        Cars car = null;
                        try {
                            car = new Cars(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        data.add(car);
                    }
                    carsTable.setItems(data);
                }
                loadingIndicator.setVisible(false);
            });
            loadThread.start();
        }
    }
*/
}