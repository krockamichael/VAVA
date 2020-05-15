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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
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

    @FXML
    private TextField surnameTextReward;

    @FXML
    private TextField nameTextReward;
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

    public String rewardForM;
    public String reasonForRewardM;
    public String payoutForM;

    @FXML
    private void selectMechanicReward() throws IOException {
        rewardForM = reward.getText().toString();
        reasonForRewardM = reasonReward.getText().toString();
        stageManager.switchScene(FxmlView.MechanicSelectionScene);
        nameColM.setCellValueFactory(new PropertyValueFactory<>("Name"));
        surnameColM.setCellValueFactory(new PropertyValueFactory<>("Surname"));
    }

    @FXML
    private void selectMechanicPayout() throws IOException{
        payoutForM = payout_s.getText().toString();
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
            System.out.println("name: "+r1[0]);
            payoutsService.UpdatePayoutMechanic(sd1,(int) mechanic.getMechanicId(),r1[0],r1[1]);
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
            payoutsService.DeletePayoutMechanic((int) mechanic.getMechanicId(),r1[0],r1[1]);
            rewardsService.DeleteRewardMechanic((int) mechanic.getMechanicId(),r1[0],r1[1]);
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

            int newAmount=0;
            Payouts oldPayout = payoutsService.findByNameAndSurname(r[0],r[1]);
            if(oldPayout == null) {
                Payouts payout = new Payouts();
                payout.setName(r[0]);
                payout.setSurname(r[1]);
                payout.setAmount(sd);
                payout.setMechanic_id((int) mechanic.getMechanicId());
                payoutsService.save(payout);
            }
            else {
                newAmount = oldPayout.getAmount() + sd;
                payoutsService.UpdatePayoutMechanic(newAmount, (int) mechanic.getMechanicId(), r[0], r[1]);
            }

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
            car.setModel(model);
            car.setBrand(brand);
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

    public String VINCarC;
    public String BrandCarC;
    public String ModelCarC;
    public int FuelTypeCarC;

    @FXML
    private void loadCustomerSelection() throws IOException {
        VINCarC = Car_vin.getText().toString();
        BrandCarC = Car_brand.getText().toString();
        ModelCarC = Car_model.getText().toString();
        FuelTypeCarC = fuelType.getSelectionModel().getSelectedIndex();

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

    @FXML
    private void returnSeMechanic()
    {
        String NameR = OverViewTable.getSelectionModel().getSelectedItem().getName()+" "+OverViewTable.getSelectionModel().getSelectedItem().getSurname();;
        String r[] = NameR.split("\\s+");
        int originalQty=0;
        double numOfDays=0;
        double sumOfDate=0;
        double avgOfDate=0;

        Mechanics mechanic = mechanicsService.findByNameAndSurname(r[0],r[1]);
        originalQty= (int) mechanic.getMechanicId();

        if(repairsService.GetNumberOfR(originalQty) != null)
            numOfDays = Double.valueOf(repairsService.GetNumberOfR(originalQty));

        if(repairsService.total(originalQty) != null)
            sumOfDate = Double.valueOf(repairsService.total(originalQty));

        if(repairsService.AvgDate(originalQty) != null)
            avgOfDate = Double.valueOf(repairsService.AvgDate(originalQty));

        String det = Double.toString(numOfDays);
        String det1 = Double.toString(sumOfDate);
        String det2 = Double.toString(avgOfDate);

        TotalNumber.setText(det);
        TotalRepairT.setText(det1);
        AveRepairT.setText(det2);
    }

    public String TotalNumberOfRepairs;
    public String AverageRepairTime;
    public String TotalRepairTime;
    public String NameAtOverviewR;
    public String SurnameAtOverviewR;
    public ObservableList<Mechanics> itemsMechanics;

    public void LoadRepairD() throws IOException {
        String NameR = OverViewTable.getSelectionModel().getSelectedItem().getName()+" "+OverViewTable.getSelectionModel().getSelectedItem().getSurname();;
        String r[] = NameR.split("\\s+");
        int originalQty=0;

        Mechanics mechanic = mechanicsService.findByNameAndSurname(r[0],r[1]);
        IdM = mechanic.getMechanicId();
        NameAtOverviewR = overName.getText().toString();
        SurnameAtOverviewR = overSurname.getText().toString();
        TotalNumberOfRepairs = TotalNumber.getText().toString();
        AverageRepairTime = AveRepairT.getText().toString();
        TotalRepairTime = TotalRepairT.getText().toString();
        itemsMechanics = OverViewTable.getItems();

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
        overName.setText(NameAtOverviewR);
        overSurname.setText(SurnameAtOverviewR);
        TotalNumber.setText(TotalNumberOfRepairs);
        AveRepairT.setText(AverageRepairTime);
        TotalRepairT.setText(TotalRepairTime);

        nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        surnameCol.setCellValueFactory(new PropertyValueFactory<>("Surname"));
        OverViewTable.setItems(itemsMechanics);
    }

    public void loadSelectedRepairInfo(int carId){
        CarId = carId;
        startCol.setCellValueFactory(new PropertyValueFactory<>("Start_day"));
        finishCol.setCellValueFactory(new PropertyValueFactory<>("End_day"));
        donebyCol.setCellValueFactory(new PropertyValueFactory<>("Mechanic_name"));
        repairCol.setCellValueFactory(new PropertyValueFactory<>("Repair"));
        costCol.setCellValueFactory(new PropertyValueFactory<>("Cost"));
        repairOffset = 0;

        loadThread = new Thread(() -> {
            Repairs r = repairsService.findByCarId(carId);
            Mechanics m = null;
            if(r != null)
                m = mechanicsService.findByMechanicId(r.getMechanicId());

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
        String SelectedUser="";
        int customerId=0;

        if(customersTable.getSelectionModel().getSelectedItem() != null) {
            SelectedUser = customersTable.getSelectionModel().getSelectedItem().getName()+" "+customersTable.getSelectionModel().getSelectedItem().getSurname()+" "+customersTable.getSelectionModel().getSelectedItem().getId();
            customerId = (int)customersTable.getSelectionModel().getSelectedItem().getCustomerId();
        }

        stageManager.switchScene(FxmlView.CarScene);
        ObservableList<String> fuelTypeBox = FXCollections.observableArrayList();
        fuelTypeBox.add("");
        fuelTypeBox.add("Benzín");
        fuelTypeBox.add("Diesel");
        fuelTypeBox.add("Elektrina");
        fuelType.setItems(fuelTypeBox);

        Car_vin.setText(VINCarC);
        Car_brand.setText(BrandCarC);
        Car_model.setText(ModelCarC);
        fuelType.getSelectionModel().select(FuelTypeCarC);
        selectedCustomer.setText(SelectedUser);
        selectedCustomer.setUserData(customerId);
    }

    @FXML
    private void filterMechanic()
    {
        List<Mechanics> mechanics = mechanicsService.getMechanics(nameTextReward.getText(),surnameTextReward.getText());
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
        String SelectedMechanic="";
        stageManager.switchScene(FxmlView.AdminScene);
        tabPane.getSelectionModel().select(financeTab);

        if(mechanicsTableR.getSelectionModel().getSelectedItem() != null) {
            SelectedMechanic = mechanicsTableR.getSelectionModel().getSelectedItem().getName()+" "+mechanicsTableR.getSelectionModel().getSelectedItem().getSurname();
            selectedMechanic.setText(SelectedMechanic);
        }
        else
            selectedMechanic.setText("Selected Mechanic");

        reward.setText(rewardForM);
        reasonReward.setText(reasonForRewardM);
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
        String SelectedMechanic="";
        stageManager.switchScene(FxmlView.AdminScene);
        tabPane.getSelectionModel().select(financeTab);

        if(mechanicsTablePayout1.getSelectionModel().getSelectedItem() != null) {
            SelectedMechanic = mechanicsTablePayout1.getSelectionModel().getSelectedItem().getName()+" "+mechanicsTablePayout1.getSelectionModel().getSelectedItem().getSurname();
            selectedMechanic1.setText(SelectedMechanic);
        }
        else
            selectedMechanic1.setText("Selected Mechanic");

        payout_s.setText(payoutForM);
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
            carSelection.setText(carsTableCar.getSelectionModel().getSelectedItem().getBrand() + " " + carsTableCar.getSelectionModel().getSelectedItem().getModel());
            loadSelectedRepairInfo((int) carsTableCar.getSelectionModel().getSelectedItem().getCar_id());
        }
    }
}
