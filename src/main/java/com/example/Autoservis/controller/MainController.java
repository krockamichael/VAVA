package com.example.Autoservis.controller;

import com.example.Autoservis.PDFSampleMain;
import com.example.Autoservis.bean.*;
import com.example.Autoservis.config.StageManager;
import com.example.Autoservis.services.*;
import com.example.Autoservis.view.FxmlView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

@Controller
public class MainController implements Initializable {
    public static int UID;
    public static int IdM;

    @FXML
    private TextField Username;
    @FXML
    private PasswordField Password;
    @FXML
    private Label errMess;
    @FXML
    private TextField newUserName;
    @FXML
    private TextField newUserPass;
    @FXML
    private RadioButton userTypeMechanic;
    @FXML
    private RadioButton userTypeAdmin;
    @FXML
    private TextField Customer_name;
    @FXML
    private TextField Customer_surname;
    @FXML
    private TextField Customer_phone;
    @FXML
    private TextField Customer_email;
    @FXML
    private TextField Car_brand;
    @FXML
    private TextField Car_model;
    @FXML
    private TextField Car_vin;
    @FXML
    private Label carAddedMess;
    @FXML
    private Label invalidFormat;

    @FXML
    private TextField customerIdNum;
    @FXML
    private TextField reward;
    @FXML
    private TextField payout_s;
    @FXML
    private TextField reasonReward;
    @FXML
    private ChoiceBox fuelType;
    @FXML
    private Label emptyFieldsError;
    @FXML
    private TextField mechanicName;
    @FXML
    private TextField mechanicSurname;
    @FXML
    private TextField component;
    @FXML
    private TextField carType;
    @FXML
    private TextField cost;
    @FXML
    private TextField amount;
    @FXML
    private TextField selectedCustomer;
    @FXML
    private Label errComponents;
    @FXML
    private Label newUserError;
    @FXML
    private Label repairCost;
    @FXML
    private Label componentCost;

    @FXML
    private Label selectedPage;
    @FXML
    private TableView<Mechanics> OverViewTable;
    @FXML
    private TableColumn<Mechanics,String> nameCol;
    @FXML
    private TableColumn<Mechanics,String> surnameCol;
    @FXML
    private ProgressIndicator loadingIndicator;
    @FXML
    private TextField TotalRepairT;
    @FXML
    private TextField AveRepairT;
    @FXML
    private TextField TotalNumber;
    @FXML
    private TextField overName;
    @FXML
    private TextField overSurname;
    @FXML
    private TextField carSelection;
    @FXML
    private TextField selectedMechanic;
    @FXML
    private TextField selectedMechanic1;
    @FXML
    public Button showingRepairs;
    @FXML
    private Label invalidFormatR;
    @FXML
    private Label invalidFormatP;
    @FXML
    private Label repairPage;

    @FXML
    private TableView<Repairs> repairHistTable;
    @FXML
    private TableColumn<Repairs,String> startCol;
    @FXML
    private TableColumn<Repairs,String> finishCol;
    @FXML
    private TableColumn<Repairs,String> donebyCol;
    @FXML
    private TableColumn<Repairs,String> repairCol;
    @FXML
    private TableColumn<Repairs,Double> costCol;

    ///////////////////////////////////////////////////////////////////
    @FXML
    private TableView<Customers> customersTable;

    @FXML
    private TableColumn<Customers,String> nameCol1;

    @FXML
    private TableColumn<Customers,String> surnameCol1;

    @FXML
    private TableColumn<Customers,String> idCol;

    @FXML
    private TableColumn<Customers,String> elementIdCol;


    @FXML
    private TextField nameText;

    @FXML
    private TextField surnameText;
    ///////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////
    @FXML
    private TableView<Mechanics> mechanicsTableR;

    @FXML
    private TableColumn<Mechanics,String> nameColM;

    @FXML
    private TableColumn<Mechanics,String> surnameColM;
    /////////////////////////////////////////////////////////////////

    ///////////////////////////////////
    @FXML
    private TableView<Mechanics> mechanicsTablePayout1;

    @FXML
    private TableColumn<Mechanics,String> nameColPay;

    @FXML
    private TableColumn<Mechanics,String> surnameColPay;

    @FXML
    private TextField nameTextPay;

    @FXML
    private TextField surnameTextPay;
    //////////////////////////////////////////////

    ///////////////////////////////////////////
    @FXML
    private TextField carModelCar;
    @FXML
    private TextField carTypeCar;
    @FXML
    private TextField carVINCar;
    @FXML
    private TableView<Cars> carsTableCar;
    @FXML
    private TableColumn<Cars,String> modelColCar;
    @FXML
    private TableColumn<Cars,String> typeColCar;
    @FXML
    private TableColumn<Cars,String> vinColCar;
    @FXML
    private TableColumn<Cars,String> carIdColCar;
    ////////////////////////////////////////////

    //////////////////////////////////////////////////
    @FXML
    private TableView<Repairs> RepairsTableR;
    @FXML
    private TableColumn<Repairs,String> RepairTextR;
    @FXML
    private TableColumn<Repairs,String> StartDayR;
    @FXML
    private TableColumn<Repairs,String> EndDayR;
    @FXML
    private TableColumn<Repairs,String> DaysRR;
    @FXML
    private Label SelectARepair;
    ///////////////////////////////////////////////////

    private Thread loadThread = null;
    private double x,y;
    int offset = 0;
    int repairOffset = 0;
    static long CarId;

    @Autowired
    private UsersService usersService;

    @Autowired
    private MechanicsService mechanicsService;

    @Autowired
    private CustomersService customersService;

    @Autowired
    private PayoutsService payoutsService;

    @Autowired
    private RewardsService rewardsService;

    @Autowired
    private CarsService carsService;

    @Autowired
    private ComponentsService componentsService;

    @Autowired
    private RepairsService repairsService;

    @FXML
    public GridPane FinanceGrid;

    @FXML
    public GridPane CarGrid;

    @FXML
    public TabPane tabPane;

    @FXML
    public Tab financeTab;

    @FXML
    public Tab overview_R_Tab;

    @FXML
    public Tab repairHistoryTab;

    @FXML
    public GridPane RepairGrid;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    //closes the login window on click
    @FXML
    protected void closeStageAction(ActionEvent event){
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    //login
    @FXML
    protected void LoginAction(ActionEvent event) {
        String Name,password;
        Name = Username.getText();
        password = Password.getText();
        int userType = usersService.checkCredentials(Name,password);

        if(userType == 1)
        {
            stageManager.switchScene(FxmlView.AdminScene);
        }
        else if (userType == 2){
            stageManager.switchScene(FxmlView.MechanicScene);
            UID = UID-1;
        }
        else{
            errMess.setVisible(true);
        }
    }

    @FXML
    protected void Logout(ActionEvent event) throws IOException {
        stageManager.switchScene(FxmlView.Login);
    }

    @FXML
    private void addNewUser() throws SQLException {
        int Type;
        newUserError.setVisible(false);
        String Name = newUserName.getText();
        String Password = newUserPass.getText();
        String MechName = mechanicName.getText();
        String MechSurname = mechanicSurname.getText();
        if(Name.equals("") || Password.equals("") || MechName.equals("") || MechSurname.equals("")){
            newUserError.setVisible(true);
            return;
        }

        if(userTypeMechanic.isSelected()){
            Type = 2;
        }
        else if (userTypeAdmin.isSelected()){
            Type = 1;
        }
        else{
            newUserError.setVisible(true);
            return;
        }

        Users user = new Users();
        user.setType(Type);
        user.setPassword(Password);
        user.setUsername(Name);
        usersService.save(user);

        Mechanics mechanic = new Mechanics();
        mechanic.setName(MechName);
        mechanic.setSurname(MechSurname);
        long uid = usersService.getID(Name,Password);
        mechanic.setUser_id((int) uid);
        mechanicsService.save(mechanic);

        newUserName.setText("");
        newUserPass.setText("");
        mechanicName.setText("");
        mechanicSurname.setText("");
    }

    @FXML
    private void addNewCustomer()
    {
        invalidFormat.setVisible(false);
        String custo_Name = Customer_name.getText();
        String custo_Surname = Customer_surname.getText();
        String custo_Email = Customer_email.getText();
        String custo_IdNum = customerIdNum.getText();
        String custo_Phone = Customer_phone.getText();
        if(custo_Email.equals("") || custo_IdNum.equals("") || custo_Name.equals("") || custo_Phone.equals("")){
            invalidFormat.setVisible(true);
            return;
        }
        if(custo_Phone.matches("[0-9]+") && custo_Phone.length() > 2) {
            Customers customer = new Customers();
            customer.setName(custo_Name);
            customer.setSurname(custo_Surname);
            customer.setPhone_number(custo_Phone);
            customer.setEmail(custo_Email);
            customer.setId(custo_IdNum);

            customersService.save(customer);

            Customer_name.setText("");
            Customer_email.setText("");
            Customer_phone.setText("");
            customerIdNum.setText("");
            Customer_surname.setText("");
        }
        else{
            invalidFormat.setVisible(true);
        }
    }

    @FXML
    private void addCarInformation(ActionEvent event) throws IOException {
        stageManager.switchScene(FxmlView.CarScene);
        ObservableList<String> fuelTypeBox = FXCollections.observableArrayList();
        fuelTypeBox.add("");
        fuelTypeBox.add("Benzín");
        fuelTypeBox.add("Diesel");
        fuelTypeBox.add("Elektrina");
        fuelType.setItems(fuelTypeBox);
        fuelType.getSelectionModel().select(0);
    }

    @FXML
    private void selectMechanicReward() throws IOException {
        stageManager.switchScene(FxmlView.MechanicSelectionScene);
        nameColM.setCellValueFactory(new PropertyValueFactory<>("Name"));
        surnameColM.setCellValueFactory(new PropertyValueFactory<>("Surname"));
    }

    @FXML
    private void selectMechanicPayout() throws IOException{
        stageManager.switchScene(FxmlView.MechanicSelectionPayoutScene);
        nameColPay.setCellValueFactory(new PropertyValueFactory<>("Name"));
        surnameColPay.setCellValueFactory(new PropertyValueFactory<>("Surname"));
    }

    @FXML
    private void addNewPayout(){

        String NameR1 = selectedMechanic1.getText();
        String r1[] =NameR1.split("\\s+");
        int sd1=0;
        String number = payout_s.getText();
        int originalQty1=0;

        invalidFormatP.setVisible(false);
        if(r1[0].equals("") || r1[1].equals("")){
            invalidFormatP.setVisible(true);
            return;
        }

        if(number.matches("[0-9]+")) {
            sd1 = Integer.parseInt(number);
            Mechanics mechanic = mechanicsService.findByNameAndSurname(r1[0],r1[1]);
            Payouts payout = new Payouts();
            payout.setName(r1[0]);
            payout.setSurname(r1[1]);
            payout.setAmount(sd1);
            payout.setMechanic_id((int) mechanic.getMechanicId());
            payoutsService.save(payout);
            selectedMechanic1.setText("");
            payout_s.setText("");
        }
        else{
            invalidFormatP.setVisible(true);
        }
    }

    @FXML
    private void UpdatePayout()
    {
        String NameR1 = selectedMechanic1.getText();
        String r1[] =NameR1.split("\\s+");
        int sd1=0;
        String number = payout_s.getText();

        invalidFormatP.setVisible(false);
        if(r1[0].equals("") || r1[1].equals("")){
            invalidFormatP.setVisible(true);
            return;
        }

        if(number.matches("[0-9]+")) {
            sd1 = Integer.parseInt(number);
            Mechanics mechanic = mechanicsService.findByNameAndSurname(r1[0],r1[1]);
            Payouts payout = new Payouts();
            payout.setName(r1[0]);
            payout.setSurname(r1[1]);
            payout.setAmount(sd1);
            payout.setMechanic_id((int) mechanic.getMechanicId());
            payoutsService.save(payout);
            selectedMechanic1.setText("");
            payout_s.setText("");
        }
        else{
            invalidFormatP.setVisible(true);
        }
    }

    @FXML
    private void DeletePayout()
    {
        String NameR1 = selectedMechanic1.getText();
        String r1[] =NameR1.split("\\s+");
        int originalQty1=0;

        invalidFormatP.setVisible(false);
        if(r1[0].equals("") || r1[1].equals("")){
            invalidFormatP.setVisible(true);
            return;
        }
        else
        {
            Mechanics mechanic = mechanicsService.findByNameAndSurname(r1[0],r1[1]);
            Payouts payout = new Payouts();
            payout.setName(r1[0]);
            payout.setSurname(r1[1]);
            payout.setMechanic_id((int) mechanic.getMechanicId());

            payoutsService.delete(payout);
            selectedMechanic1.setText("");
        }
    }

    @FXML
    private void addNewReward() throws SQLException {

        String NameR = selectedMechanic.getText();
        String r[] =NameR.split("\\s+");
        int sd = 0;
        String number = reward.getText();
        String reasonfor = reasonReward.getText();

        invalidFormatR.setVisible(false);
        if(r[0].equals("") || r[1].equals("")){
            invalidFormatR.setVisible(true);
            return;
        }

        if(number.matches("[0-9]+")) {
            sd = Integer.parseInt(number);
            Mechanics mechanic = mechanicsService.findByNameAndSurname(r[0],r[1]);
            Rewards rewardsM = new Rewards();
            rewardsM.setName(r[0]);
            rewardsM.setSurname(r[1]);
            rewardsM.setAmount(sd);
            rewardsM.setReason(reasonfor);
            rewardsM.setMechanic_id((int) mechanic.getMechanicId());
            rewardsService.save(rewardsM);

            selectedMechanic.setText("");
            reward.setText("");
            reasonReward.setText("");
        }
        else{
            invalidFormatR.setVisible(true);
        }
    }

    @FXML
    private void addNewCar()
    {
        String brand = Car_brand.getText();
        String model = Car_model.getText();
        String vin = Car_vin.getText();
        String fuel = fuelType.getSelectionModel().getSelectedItem().toString();
        if(brand.equals("") || model.equals("") || vin.equals("") || fuel.equals("") || selectedCustomer.equals("")){
            emptyFieldsError.setVisible(true);
        }
        else {
            Cars car = new Cars();
            car.setModel(brand);
            car.setBrand(model);
            car.setVin(vin);
            car.setFuel(fuel);

            String Customer_id = selectedCustomer.getUserData().toString();
            System.out.println("Customer id: "+Customer_id);

            car.setOwner_id(Integer.parseInt(Customer_id));
            carsService.save(car);

            carAddedMess.setVisible(true);
            selectedCustomer.setText("");
            selectedCustomer.setUserData(null);

            emptyFieldsError.setVisible(false);
            fuelType.getSelectionModel().select(0);
            Car_brand.setText("");
            Car_model.setText("");
            Car_vin.setText("");
        }
    }

    @FXML
    private void BackToAdminScene(ActionEvent e)throws IOException{
        stageManager.switchScene(FxmlView.AdminScene);
    }

    @FXML
    private void loadCustomerSelection() throws IOException {
        stageManager.switchScene(FxmlView.CustomerSelection);
        nameCol1.setCellValueFactory(new PropertyValueFactory<>("Name"));
        surnameCol1.setCellValueFactory(new PropertyValueFactory<>("Surname"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        elementIdCol.setCellValueFactory(new PropertyValueFactory<>("CustomerId"));
    }

    @FXML
    private void addNewComponent(){
        errComponents.setVisible(false);
        String componentS = component.getText();
        String carTypeS = carType.getText();
        double costD = 0;
        int amountI = 0;

        if(cost.getText().equals("") || !cost.getText().matches("^[0-9]+\\.?[0-9]*$")) {
            errComponents.setVisible(true);
            return;
        }
        costD = Double.parseDouble(cost.getText());
        if(amount.getText().equals("") || !amount.getText().matches("^[0-9]+$")) {
            errComponents.setVisible(true);
            return;
        }
        amountI = Integer.parseInt(amount.getText());

        if(componentS.equals("") || carTypeS.equals("")){
            errComponents.setVisible(true);
        }
        else{
            Components Ncompo = new Components();
            Ncompo.setName(componentS);
            Ncompo.setAmount(amountI);
            Ncompo.setCost(costD);
            Ncompo.setCarType(carTypeS);
            componentsService.save(Ncompo);

            component.setText("");
            carType.setText("");
            cost.setText("");
            amount.setText("");
        }

    }

    @FXML
    private void loadCarSelection() throws IOException {
        stageManager.switchScene(FxmlView.CarSelection);
        modelColCar.setCellValueFactory(new PropertyValueFactory<>("Model"));
        typeColCar.setCellValueFactory(new PropertyValueFactory<>("Brand"));
        vinColCar.setCellValueFactory(new PropertyValueFactory<>("Vin"));
        carIdColCar.setCellValueFactory(new PropertyValueFactory<>("carId"));
    }

    @FXML
    private void FilterSeMechanic()
    {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        surnameCol.setCellValueFactory(new PropertyValueFactory<>("Surname"));
        ObservableList<Mechanics> data = FXCollections.observableArrayList();

        List<Mechanics> mechanics = mechanicsService.getMechanics(overName.getText(),overSurname.getText());
        if(mechanics.size() == 0){
            System.out.println("No result");
        }
        else{
            for(Mechanics mech : mechanics) {
                Mechanics mech1 = null;
                mech1 = new Mechanics();
                mech1.setName(mech.getName());
                mech1.setSurname(mech.getSurname());
                data.add(mech1);
            }
            OverViewTable.setItems(data);
        }
    }

    /*
    @FXML
    protected void nextRight(){
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        surnameCol.setCellValueFactory(new PropertyValueFactory<>("Surname"));

        offset += 100;
        loadingIndicator.setVisible(true);
        selectedPage.setText(Integer.toString(offset/100+1));
        loadThread = new Thread(() -> {
            ResultSet rs = mechanic.GetMechanics(overName.getText(),overSurname.getText(),offset);

            ObservableList<Mechanics> data = FXCollections.observableArrayList();
            if(rs == null)
                System.out.println("No result");
            else {
                while (true) {
                    try {
                        if (!rs.next()) break;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Mechanics mechanic1 = null;
                    try {
                        mechanic1 = new Mechanics(rs.getString(1),rs.getString(2));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    data.add(mechanic1);
                }
                OverViewTable.setItems(data);
            }
            loadingIndicator.setVisible(false);
        });
        loadThread.start();
    }

    @FXML
    protected void nextLeft(){
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        surnameCol.setCellValueFactory(new PropertyValueFactory<>("Surname"));

        if(offset >=100) {
            offset -=100;
            loadingIndicator.setVisible(true);
            selectedPage.setText(Integer.toString((offset/100)+1));
            loadThread = new Thread(() -> {
                ResultSet rs = mechanic.GetMechanics(overName.getText(),overSurname.getText(),offset);

                ObservableList<Mechanics> data = FXCollections.observableArrayList();
                if(rs == null)
                    System.out.println("No result");
                else {
                    while (true) {
                        try {
                            if (!rs.next()) break;
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        Mechanics mechanic1 = null;
                        try {
                            mechanic1 = new Mechanics(rs.getString(1),rs.getString(2));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        data.add(mechanic1);
                    }
                    OverViewTable.setItems(data);
                }
                loadingIndicator.setVisible(false);
            });
            loadThread.start();
        }
    }
*/
    @FXML
    private void returnSeMechanic()
    {
        String NameR = OverViewTable.getSelectionModel().getSelectedItem().getName()+" "+OverViewTable.getSelectionModel().getSelectedItem().getSurname();;
        String r[] = NameR.split("\\s+");
        int originalQty=0;
        int numOfDays=0;
        int sumOfDate=0;
        int avgOfDate=0;

        Mechanics mechanic = mechanicsService.findByNameAndSurname(r[0],r[1]);
        originalQty= (int) mechanic.getMechanicId();

        numOfDays = repairsService.GetNumberOfR(originalQty);

        sumOfDate = repairsService.total(originalQty);

        avgOfDate = repairsService.AvgDate(originalQty);

        String det = Integer.toString(numOfDays);
        String det1 = Integer.toString(sumOfDate);
        String det2 = Integer.toString(avgOfDate);

        TotalNumber.setText(det);
        TotalRepairT.setText(det1);
        AveRepairT.setText(det2);
    }

    public void LoadRepairD() throws IOException {
        String NameR = OverViewTable.getSelectionModel().getSelectedItem().getName()+" "+OverViewTable.getSelectionModel().getSelectedItem().getSurname();;
        String r[] = NameR.split("\\s+");
        int originalQty=0;

        Mechanics mechanic = mechanicsService.findByNameAndSurname(r[0],r[1]);
        IdM = mechanic.getMechanicId();

        stageManager.switchScene(FxmlView.RepairScene);

        RepairTextR.setCellValueFactory(new PropertyValueFactory<>("Repair"));
        StartDayR.setCellValueFactory(new PropertyValueFactory<>("Start_day"));
        EndDayR.setCellValueFactory(new PropertyValueFactory<>("End_day"));
        DaysRR.setCellValueFactory(new PropertyValueFactory<>("Days"));

        List<Repairs> repairs = repairsService.getWorkDetails(IdM);
        ObservableList<Repairs> data = FXCollections.observableArrayList();
        if(repairs.size() == 0)
            System.out.println("No result");
        else {
            for(Repairs re : repairs) {
                Repairs repa = null;
                repa = new Repairs();
                repa.setRepair(re.getRepair());
                repa.setStart_day(Date.valueOf(re.getStart_day()));
                repa.setEnd_day(Date.valueOf(re.getEnd_day()));

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date firstDate = null;
                try {
                    firstDate = sdf.parse(re.getEnd_day());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                java.util.Date secondDate = null;
                try {
                    secondDate = sdf.parse(re.getStart_day());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long diffInMillies = Math.abs(firstDate.getTime() - secondDate.getTime());
                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                repa.setDays((int) diff);
                data.add(repa);
            }
            RepairsTableR.setItems(data);
        }
    }

    @FXML
    private void BackToAdminSceneRepair(ActionEvent e)throws IOException{
        stageManager.switchScene(FxmlView.AdminScene);
        tabPane.getSelectionModel().select(overview_R_Tab);
    }

    public void loadSelectedRepairInfo(int carId){
        CarId = carId;
        startCol.setCellValueFactory(new PropertyValueFactory<>("Start_day"));
        finishCol.setCellValueFactory(new PropertyValueFactory<>("End_day"));
        donebyCol.setCellValueFactory(new PropertyValueFactory<>("Mechanic_name"));
        repairCol.setCellValueFactory(new PropertyValueFactory<>("Repair"));
        costCol.setCellValueFactory(new PropertyValueFactory<>("Cost"));
        repairOffset = 0;
        repairPage.setText("1");

        loadThread = new Thread(() -> {
            Repairs r = repairsService.findByCarId(carId);
            Mechanics m = mechanicsService.findByMechanicId(r.getMechanicId());

            ObservableList<Repairs> data = FXCollections.observableArrayList();
            if(r == null)
                System.out.println("No result");
            else {
                Repairs rep1 = null;
                rep1 = new Repairs();
                rep1.setRepair(r.getRepair());
                rep1.setStart_day(Date.valueOf(r.getStart_day()));
                rep1.setEnd_day(Date.valueOf(r.getEnd_day()));
                rep1.setCost(r.getCost());
                rep1.setMechanic_name(m.getName()+" "+m.getSurname());
                data.add(rep1);

                repairHistTable.setItems(data);
            }

            String rCost= repairsService.getCostSum(carId);
            String[] rCosts = rCost.split(",");

            System.out.println(rCosts[0]+" "+rCosts[1]);
            Platform.runLater(() -> {
                repairCost.setText(rCosts[0]);
                componentCost.setText(rCosts[1]);
            });
        });
        loadThread.start();
    }

    @FXML
    private void filterCustomers() {
        List<Customers> customers = customersService.getCustomers(nameText.getText(),surnameText.getText());
        ObservableList<Customers> data = FXCollections.observableArrayList();

        if(customers.size() == 0){
            System.out.println("No result");
        }
        else{
            for(Customers custo : customers) {
                Customers cus = null;
                cus = new Customers();
                cus.setName(custo.getName());
                cus.setSurname(custo.getSurname());
                cus.setId(custo.getId());
                cus.setCustomerId(custo.getCustomerId());
                data.add(cus);
            }
            customersTable.setItems(data);
        }
    }

    @FXML
    private void select(ActionEvent event) throws IOException {
        String SelectedUser;
        SelectedUser = customersTable.getSelectionModel().getSelectedItem().getName()+" "+customersTable.getSelectionModel().getSelectedItem().getSurname()+" "+customersTable.getSelectionModel().getSelectedItem().getId();

        stageManager.switchScene(FxmlView.CarScene);
        ObservableList<String> fuelTypeBox = FXCollections.observableArrayList();
        fuelTypeBox.add("");
        fuelTypeBox.add("Benzín");
        fuelTypeBox.add("Diesel");
        fuelTypeBox.add("Elektrina");
        fuelType.setItems(fuelTypeBox);
        fuelType.getSelectionModel().select(0);

        selectedCustomer.setText(SelectedUser);
        selectedCustomer.setUserData(customersTable.getSelectionModel().getSelectedItem().getCustomerId());
    }

    @FXML
    private void filterMechanic()
    {
        List<Mechanics> mechanics = mechanicsService.getMechanicG();
        ObservableList<Mechanics> data = FXCollections.observableArrayList();

        if(mechanics.size() == 0){
            System.out.println("No result");
        }
        else{
            for(Mechanics mech : mechanics) {
                Mechanics mechanic = null;
                mechanic = new Mechanics();
                mechanic.setName(mech.getName());
                mechanic.setSurname(mech.getSurname());
                data.add(mechanic);
            }
            mechanicsTableR.setItems(data);
        }
    }

    @FXML
    private void returnMechanic(ActionEvent event)
    {
        String SelectedMechanic;
        SelectedMechanic = mechanicsTableR.getSelectionModel().getSelectedItem().getName()+" "+mechanicsTableR.getSelectionModel().getSelectedItem().getSurname();

        stageManager.switchScene(FxmlView.AdminScene);
        tabPane.getSelectionModel().select(financeTab);
        selectedMechanic.setText(SelectedMechanic);
    }

    @FXML
    private void filterMechanicsPayout()
    {
        List<Mechanics> mechanics= mechanicsService.getMechanics(nameTextPay.getText(),surnameTextPay.getText());
        ObservableList<Mechanics> data = FXCollections.observableArrayList();

        if(mechanics.size() == 0){
            System.out.println("No result");
        }
        else{
            for(Mechanics mech : mechanics) {
                Mechanics mechanic = null;
                mechanic = new Mechanics();
                mechanic.setName(mech.getName());
                mechanic.setSurname(mech.getSurname());
                data.add(mechanic);
            }
            mechanicsTablePayout1.setItems(data);
        }
    }

    @FXML
    private void returnMechanicPayout(ActionEvent event)
    {
        String SelectedMechanic;
        SelectedMechanic = mechanicsTablePayout1.getSelectionModel().getSelectedItem().getName()+" "+mechanicsTablePayout1.getSelectionModel().getSelectedItem().getSurname();

        stageManager.switchScene(FxmlView.AdminScene);
        tabPane.getSelectionModel().select(financeTab);
        selectedMechanic1.setText(SelectedMechanic);
    }

    @FXML
    protected void filterCar(){
        ObservableList<Cars> data = FXCollections.observableArrayList();
        List<Cars> Lcar = carsService.getCarsmaybe(carModelCar.getText(), carTypeCar.getText(), carVINCar.getText());

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
            carsTableCar.setItems(data);
        }
    }

    @FXML
    private void selectCar(ActionEvent event){
        stageManager.switchScene(FxmlView.AdminScene);
        tabPane.getSelectionModel().select(repairHistoryTab);
        if(carsTableCar.getSelectionModel().getSelectedItem() != null) {
            carSelection.setText(carsTableCar.getSelectionModel().getSelectedItem().getModel() + " " + carsTableCar.getSelectionModel().getSelectedItem().getBrand());
            loadSelectedRepairInfo((int) carsTableCar.getSelectionModel().getSelectedItem().getCar_id());
        }
    }


 /*
    @FXML
    protected void repairRight() {
        repairOffset +=100;
        repairLoadingIndicator.setVisible(true);
        repairPage.setText(Integer.toString(repairOffset/100+1));
        loadThread = new Thread(() -> {
            ResultSet repairs1 = repairs.getRepairInfo(carId,repairOffset);

            ObservableList<Repairs> data = FXCollections.observableArrayList();
            if(repairs1 == null)
                System.out.println("No result");
            else {

                while (true) {
                    try {
                        if (!repairs1.next()) break;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Repairs component = null;
                    try {
                        component = new Repairs(repairs1.getString(1),repairs1.getString(5),repairs1.getString(6),repairs1.getString(2)+" "+repairs1.getString(3),repairs1.getDouble(4));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    data.add(component);
                }
                repairHistTable.setItems(data);
            }
            repairLoadingIndicator.setVisible(false);
        });
        loadThread.start();
    }

    @FXML
    protected void repairLeft() {
        if (repairOffset >= 100) {
            repairOffset -= 100;
            repairLoadingIndicator.setVisible(true);
            repairPage.setText(Integer.toString(repairOffset / 100 + 1));
            loadThread = new Thread(() -> {
                ResultSet repairs1 = repairs.getRepairInfo(carId, repairOffset);

                ObservableList<Repairs> data = FXCollections.observableArrayList();
                if (repairs1 == null)
                    System.out.println("No result");
                else {

                    while (true) {
                        try {
                            if (!repairs1.next()) break;
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        Repairs component = null;
                        try {
                            component = new Repairs(repairs1.getString(1), repairs1.getString(5), repairs1.getString(6), repairs1.getString(2) + " " + repairs1.getString(3), repairs1.getDouble(4));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        data.add(component);
                    }
                    repairHistTable.setItems(data);
                }
                repairLoadingIndicator.setVisible(false);
            });
            loadThread.start();
        }
    }
   */

    @FXML
    private void generatePDF(ActionEvent event){
        tabPane.getSelectionModel().select(repairHistoryTab);
        if(carsTableCar != null &&
                carsTableCar.getSelectionModel().getSelectedItem() != null) {
            String[] parameters = new String[12];
            int owner_id = carsTableCar.getSelectionModel().getSelectedItem().getOwner_id();
            Customers owner = customersService.findByCustomerId(owner_id);

            Repairs r = repairsService.findByCarId((int) carsTableCar.getSelectionModel().getSelectedItem().getCar_id());
            Mechanics m = mechanicsService.findByMechanicId(r.getMechanicId());

            parameters[0] = carsTableCar.getSelectionModel().getSelectedItem().getBrand();
            parameters[1] = carsTableCar.getSelectionModel().getSelectedItem().getModel();
            parameters[2] = carsTableCar.getSelectionModel().getSelectedItem().getVin();
            // parameters[3] = carsTableCar.getSelectionModel().getSelectedItem().getFuel(); --> return null
            parameters[3] = r.getStart_day();
            parameters[4] = r.getEnd_day();
            parameters[5] = String.valueOf(r.getDays());
            parameters[6] = String.valueOf(r.getCost());
            parameters[7] = m.getName() + ' ' + m.getSurname();
            parameters[8] = r.getRepair();
//        parameters[9] = owner.getName() + " " + owner.getSurname();
//        parameters[10] = owner.getEmail();
//        parameters[11] = owner.getPhone_number();

            PDFSampleMain.main(parameters);
            SelectARepair.setVisible(false);
        } else {
            SelectARepair.setVisible(true);
        }
    }
}
