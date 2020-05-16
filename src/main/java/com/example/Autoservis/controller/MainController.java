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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
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
    public Button english_lang;
    public Button slovak_lang;
    private String global_lang = "eng";

    @FXML private TextField Username;
    @FXML private PasswordField Password;
    @FXML private Label errMess;
    @FXML private TextField newUserName;
    @FXML private TextField newUserPass;
    @FXML private RadioButton userTypeMechanic;
    @FXML private RadioButton userTypeAdmin;
    @FXML private TextField Customer_name;
    @FXML private TextField Customer_surname;
    @FXML private TextField Customer_phone;
    @FXML private TextField Customer_email;
    @FXML private TextField Car_brand;
    @FXML private TextField Car_model;
    @FXML private TextField Car_vin;
    @FXML private Label carAddedMess;
    @FXML private Label invalidFormat;

    @FXML private TextField customerIdNum;
    @FXML private TextField reward;
    @FXML private TextField payout_s;
    @FXML private TextField reasonReward;
    @FXML private ChoiceBox fuelType;
    @FXML private Label emptyFieldsError;
    @FXML private TextField mechanicName;
    @FXML private TextField mechanicSurname;
    @FXML private TextField component;
    @FXML private TextField carType;
    @FXML private TextField cost;
    @FXML private TextField amount;
    @FXML private TextField selectedCustomer;
    @FXML private Label errComponents;
    @FXML private Label newUserError;
    @FXML private Label repairCost;
    @FXML private Label componentCost;

    @FXML private TableView<Mechanics> OverViewTable;
    @FXML private TableColumn<Mechanics,String> nameCol;
    @FXML private TableColumn<Mechanics,String> surnameCol;
    @FXML private TextField TotalRepairT;
    @FXML private TextField AveRepairT;
    @FXML private TextField TotalNumber;
    @FXML private TextField overName;
    @FXML private TextField overSurname;
    @FXML private TextField carSelection;
    @FXML private TextField selectedMechanic;
    @FXML private TextField selectedMechanic1;
    @FXML private Label invalidFormatR;
    @FXML private Label invalidFormatP;
    @FXML public Button showingRepairs;

    @FXML private TableView<Repairs> repairHistTable;
    @FXML private TableColumn<Repairs,String> startCol;
    @FXML private TableColumn<Repairs,String> finishCol;
    @FXML private TableColumn<Repairs,String> donebyCol;
    @FXML private TableColumn<Repairs,String> repairCol;
    @FXML private TableColumn<Repairs,Double> costCol;

    /////////////////////////////////////////////////////////////////
    @FXML private TableView<Customers> customersTable;
    @FXML private TableColumn<Customers,String> nameCol1;
    @FXML private TableColumn<Customers,String> surnameCol1;
    @FXML private TableColumn<Customers,String> idCol;
    @FXML private TableColumn<Customers,String> elementIdCol;
    @FXML private TextField nameText;
    @FXML private TextField surnameText;
    /////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////
    @FXML private TableView<Mechanics> mechanicsTableR;
    @FXML private TableColumn<Mechanics,String> nameColM;
    @FXML private TableColumn<Mechanics,String> surnameColM;
    @FXML private TextField surnameTextReward;
    @FXML private TextField nameTextReward;
    /////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////
    @FXML private TableView<Mechanics> mechanicsTablePayout1;
    @FXML private TableColumn<Mechanics,String> nameColPay;
    @FXML private TableColumn<Mechanics,String> surnameColPay;
    @FXML private TextField nameTextPay;
    @FXML private TextField surnameTextPay;
    /////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////
    @FXML private TextField carModelCar;
    @FXML private TextField carTypeCar;
    @FXML private TextField carVINCar;
    @FXML private TableView<Cars> carsTableCar;
    @FXML private TableColumn<Cars,String> modelColCar;
    @FXML private TableColumn<Cars,String> typeColCar;
    @FXML private TableColumn<Cars,String> vinColCar;
    @FXML private TableColumn<Cars,String> carIdColCar;
    /////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////
    @FXML private TableView<Repairs> RepairsTableR;
    @FXML private TableColumn<Repairs,String> RepairTextR;
    @FXML private TableColumn<Repairs,String> StartDayR;
    @FXML private TableColumn<Repairs,String> EndDayR;
    @FXML private TableColumn<Repairs,String> DaysRR;
    @FXML private Label SelectARepair;
    /////////////////////////////////////////////////////////////////

    private Thread loadThread = null;
    int repairOffset = 0;
    static long CarId;

    @Autowired private UsersService usersService;
    @Autowired private MechanicsService mechanicsService;
    @Autowired private CustomersService customersService;
    @Autowired private PayoutsService payoutsService;
    @Autowired private RewardsService rewardsService;
    @Autowired private CarsService carsService;
    @Autowired private ComponentsService componentsService;
    @Autowired private RepairsService repairsService;

    @FXML public GridPane FinanceGrid;
    @FXML public GridPane CarGrid;
    @FXML public TabPane tabPane;
    @FXML public Tab financeTab;
    @FXML public Tab overview_R_Tab;
    @FXML public Tab repairHistoryTab;
    @FXML public GridPane RepairGrid;
    @Lazy @Autowired private StageManager stageManager;

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
    protected void LoginAction() {
        String Name,password;
        Name = Username.getText();
        password = Password.getText();
        int userType = usersService.checkCredentials(Name,password);

        if(userType == 1) {
            stageManager.switchScene(FxmlView.AdminScene);

            // keep selected language
            if (global_lang.equals("eng")) changeToEnglishLang();
            else changeToSlovakLang();
        }
        else if (userType == 2) {
            stageManager.switchScene(FxmlView.MechanicScene);
            UID = UID-1;

            // TODO keep selected language
            MechanicMainSceneController MMSC = new MechanicMainSceneController();
            if (global_lang.equals("eng")) MMSC.mechanicMainScene_changeToEnglishLang();
            else MMSC.mechanicMainScene_changeToSlovakLang();
        } else
            errMess.setVisible(true);
    }

    @FXML
    protected void Logout() {
        stageManager.switchScene(FxmlView.Login);

        // keep selected language
        if (global_lang.equals("eng")) changeToEnglishLang_LoginScreen();
        else changeToSlovakLang_LoginScreen();
    }

    @FXML
    private void addNewUser() {
        int Type;
        newUserError.setVisible(false);
        String Name = newUserName.getText();
        String Password = newUserPass.getText();
        String MechName = mechanicName.getText();
        String MechSurname = mechanicSurname.getText();

        if (Name.equals("") || Password.equals("") || MechName.equals("") || MechSurname.equals("")){
            newUserError.setVisible(true);
            return;
        }

        if (userTypeMechanic.isSelected())
            Type = 2;
        else if (userTypeAdmin.isSelected())
            Type = 1;
        else {
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
    private void addNewCustomer() {
        invalidFormat.setVisible(false);
        String custo_Name = Customer_name.getText();
        String custo_Surname = Customer_surname.getText();
        String custo_Email = Customer_email.getText();
        String custo_IdNum = customerIdNum.getText();
        String custo_Phone = Customer_phone.getText();

        if (custo_Email.equals("") || custo_IdNum.equals("") || custo_Name.equals("") || custo_Phone.equals("")) {
            invalidFormat.setVisible(true);
            return;
        }
        if (custo_Phone.matches("[0-9]+") && custo_Phone.length() > 2) {
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
        else
            invalidFormat.setVisible(true);
    }

    @FXML
    private void addCarInformation() {
        stageManager.switchScene(FxmlView.CarScene);

        // keep selected language
        if (global_lang.equals("eng")) carScene_changeToEnglishLang();
        else carScene_changeToSlovakLang();

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
    private void selectMechanicReward() {
        rewardForM = reward.getText();
        reasonForRewardM = reasonReward.getText();
        stageManager.switchScene(FxmlView.MechanicSelectionScene);

        // keep selected language
        if (global_lang.equals("eng")) mechSel_changeToEnglishLang();
        else mechSel_changeToSlovakLang();

        nameColM.setCellValueFactory(new PropertyValueFactory<>("Name"));
        surnameColM.setCellValueFactory(new PropertyValueFactory<>("Surname"));
    }

    @FXML
    private void selectMechanicPayout() {
        payoutForM = payout_s.getText();
        stageManager.switchScene(FxmlView.MechanicSelectionPayoutScene);

        // keep selected language
        if (global_lang.equals("eng")) mechSelPayoutScene_changeToEnglishLang();
        else mechSelPayoutScene_changeToSlovakLang();

        nameColPay.setCellValueFactory(new PropertyValueFactory<>("Name"));
        surnameColPay.setCellValueFactory(new PropertyValueFactory<>("Surname"));
    }

    @FXML
    private void addNewPayout() {
        String NameR1 = selectedMechanic1.getText();
        String[] r1 =NameR1.split("\\s+");
        int sd1;
        String number = payout_s.getText();

        invalidFormatP.setVisible(false);
        if (r1[0].equals("") || r1[1].equals("")) {
            invalidFormatP.setVisible(true);
            return;
        }

        if (number.matches("[0-9]+")) {
            sd1 = Integer.parseInt(number);
            Mechanics mechanic = mechanicsService.findByNameAndSurname(r1[0],r1[1]);
            Payouts payout = new Payouts();
            payout.setName(r1[0]);
            payout.setSurname(r1[1]);
            payout.setAmount(sd1);
            payout.setMechanic_id(mechanic.getMechanicId());
            payoutsService.save(payout);
            selectedMechanic1.setText("");
            payout_s.setText("");
        } else
            invalidFormatP.setVisible(true);
    }

    @FXML
    private void UpdatePayout() {
        String NameR1 = selectedMechanic1.getText();
        String[] r1 =NameR1.split("\\s+");
        int sd1;
        String number = payout_s.getText();

        invalidFormatP.setVisible(false);
        if (r1[0].equals("") || r1[1].equals("")) {
            invalidFormatP.setVisible(true);
            return;
        }

        if (number.matches("[0-9]+")) {
            sd1 = Integer.parseInt(number);
            Mechanics mechanic = mechanicsService.findByNameAndSurname(r1[0],r1[1]);
            System.out.println("name: "+r1[0]);
            payoutsService.UpdatePayoutMechanic(sd1, mechanic.getMechanicId(),r1[0],r1[1]);
            selectedMechanic1.setText("");
            payout_s.setText("");
        }
        else
            invalidFormatP.setVisible(true);
    }

    @FXML
    private void DeletePayout() {
        String NameR1 = selectedMechanic1.getText();
        String[] r1 =NameR1.split("\\s+");

        invalidFormatP.setVisible(false);
        if (r1[0].equals("") || r1[1].equals(""))
            invalidFormatP.setVisible(true);
        else {
            Mechanics mechanic = mechanicsService.findByNameAndSurname(r1[0],r1[1]);
            payoutsService.DeletePayoutMechanic(mechanic.getMechanicId(),r1[0],r1[1]);
            rewardsService.DeleteRewardMechanic(mechanic.getMechanicId(),r1[0],r1[1]);
            selectedMechanic1.setText("");
        }
    }

    @FXML
    private void addNewReward() {
        String NameR = selectedMechanic.getText();
        String[] r =NameR.split("\\s+");
        int sd;
        String number = reward.getText();
        String reasonfor = reasonReward.getText();

        invalidFormatR.setVisible(false);
        if (r[0].equals("") || r[1].equals("")) {
            invalidFormatR.setVisible(true);
            return;
        }

        if (number.matches("[0-9]+")) {
            sd = Integer.parseInt(number);
            Mechanics mechanic = mechanicsService.findByNameAndSurname(r[0],r[1]);
            Rewards rewardsM = new Rewards();
            rewardsM.setName(r[0]);
            rewardsM.setSurname(r[1]);
            rewardsM.setAmount(sd);
            rewardsM.setReason(reasonfor);
            rewardsM.setMechanic_id(mechanic.getMechanicId());
            rewardsService.save(rewardsM);

            int newAmount;
            Payouts oldPayout = payoutsService.findByNameAndSurname(r[0],r[1]);
            if (oldPayout == null) {
                Payouts payout = new Payouts();
                payout.setName(r[0]);
                payout.setSurname(r[1]);
                payout.setAmount(sd);
                payout.setMechanic_id((int) mechanic.getMechanicId());
                payoutsService.save(payout);
            }
            else {
                newAmount = oldPayout.getAmount() + sd;
                payoutsService.UpdatePayoutMechanic(newAmount, mechanic.getMechanicId(), r[0], r[1]);
            }

            selectedMechanic.setText("");
            reward.setText("");
            reasonReward.setText("");
        }
        else
            invalidFormatR.setVisible(true);
    }

    @FXML
    private void addNewCar() {
        String brand = Car_brand.getText();
        String model = Car_model.getText();
        String vin = Car_vin.getText();
        String fuel = fuelType.getSelectionModel().getSelectedItem().toString();
        if (brand.equals("") || model.equals("") || vin.equals("") || fuel.equals("") || selectedCustomer.toString().equals(""))
            emptyFieldsError.setVisible(true);

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
    private void BackToAdminScene() {
        stageManager.switchScene(FxmlView.AdminScene);
        // keep selected language
        if (global_lang.equals("eng")) changeToEnglishLang();
        else changeToSlovakLang();
    }

    public String VINCarC;
    public String BrandCarC;
    public String ModelCarC;
    public int FuelTypeCarC;

    @FXML
    private void loadCustomerSelection() {
        VINCarC = Car_vin.getText();
        BrandCarC = Car_brand.getText();
        ModelCarC = Car_model.getText();
        FuelTypeCarC = fuelType.getSelectionModel().getSelectedIndex();

        stageManager.switchScene(FxmlView.CustomerSelection);
        // keep selected language
        if (global_lang.equals("eng")) custSel_changeToEnglishLang();
        else custSel_changeToSlovakLang();

        nameCol1.setCellValueFactory(new PropertyValueFactory<>("Name"));
        surnameCol1.setCellValueFactory(new PropertyValueFactory<>("Surname"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        elementIdCol.setCellValueFactory(new PropertyValueFactory<>("CustomerId"));
    }

    @FXML
    private void addNewComponent() {
        errComponents.setVisible(false);
        String componentS = component.getText();
        String carTypeS = carType.getText();
        double costD;
        int amountI;

        if (cost.getText().equals("") || !cost.getText().matches("^[0-9]+\\.?[0-9]*$")) {
            errComponents.setVisible(true);
            return;
        }
        costD = Double.parseDouble(cost.getText());
        if (amount.getText().equals("") || !amount.getText().matches("^[0-9]+$")) {
            errComponents.setVisible(true);
            return;
        }
        amountI = Integer.parseInt(amount.getText());

        if (componentS.equals("") || carTypeS.equals(""))
            errComponents.setVisible(true);
        else {
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
    private void loadCarSelection() {
        stageManager.switchScene(FxmlView.CarSelection);
        // keep selected language
        if (global_lang.equals("eng")) carSel_changeToEnglishLang();
        else carSel_changeToSlovakLang();

        modelColCar.setCellValueFactory(new PropertyValueFactory<>("Model"));
        typeColCar.setCellValueFactory(new PropertyValueFactory<>("Brand"));
        vinColCar.setCellValueFactory(new PropertyValueFactory<>("Vin"));
        carIdColCar.setCellValueFactory(new PropertyValueFactory<>("carId"));
    }

    @FXML
    private void FilterSeMechanic() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        surnameCol.setCellValueFactory(new PropertyValueFactory<>("Surname"));
        ObservableList<Mechanics> data = FXCollections.observableArrayList();

        List<Mechanics> mechanics = mechanicsService.getMechanics(overName.getText(),overSurname.getText());
        if (mechanics.size() == 0)
            System.out.println("No result");
        else {
            for(Mechanics mech : mechanics) {
                Mechanics mech1 = new Mechanics();
                mech1.setName(mech.getName());
                mech1.setSurname(mech.getSurname());
                data.add(mech1);
            }
            OverViewTable.setItems(data);
        }
    }

    @FXML
    private void returnSeMechanic() {
        String NameR = OverViewTable.getSelectionModel().getSelectedItem().getName()+" "+OverViewTable.getSelectionModel().getSelectedItem().getSurname();
        String[] r = NameR.split("\\s+");
        int originalQty;
        double numOfDays=0;
        double sumOfDate=0;
        double avgOfDate=0;

        Mechanics mechanic = mechanicsService.findByNameAndSurname(r[0],r[1]);
        originalQty= mechanic.getMechanicId();

        if (repairsService.GetNumberOfR(originalQty) != null)
            numOfDays = Double.parseDouble(repairsService.GetNumberOfR(originalQty));
        if (repairsService.total(originalQty) != null)
            sumOfDate = Double.parseDouble(repairsService.total(originalQty));
        if (repairsService.AvgDate(originalQty) != null)
            avgOfDate = Double.parseDouble(repairsService.AvgDate(originalQty));

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

    public void LoadRepairD() {
        String NameR = OverViewTable.getSelectionModel().getSelectedItem().getName()+" "+OverViewTable.getSelectionModel().getSelectedItem().getSurname();
        String[] r = NameR.split("\\s+");

        Mechanics mechanic = mechanicsService.findByNameAndSurname(r[0],r[1]);
        IdM = mechanic.getMechanicId();
        NameAtOverviewR = overName.getText();
        SurnameAtOverviewR = overSurname.getText();
        TotalNumberOfRepairs = TotalNumber.getText();
        AverageRepairTime = AveRepairT.getText();
        TotalRepairTime = TotalRepairT.getText();
        itemsMechanics = OverViewTable.getItems();

        stageManager.switchScene(FxmlView.RepairScene);
        // keep selected language
        if (global_lang.equals("eng")) repairScene_changeToEnglishLang();
        else repairScene_changeToSlovakLang();

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
                Repairs repa = new Repairs();
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
    private void BackToAdminSceneRepair() {
        stageManager.switchScene(FxmlView.AdminScene);
        // keep selected language
        if (global_lang.equals("eng")) changeToEnglishLang();
        else changeToSlovakLang();

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

    public void loadSelectedRepairInfo(int carId) {
        CarId = carId;
        startCol.setCellValueFactory(new PropertyValueFactory<>("Start_day"));
        finishCol.setCellValueFactory(new PropertyValueFactory<>("End_day"));
        donebyCol.setCellValueFactory(new PropertyValueFactory<>("Mechanic_name"));
        repairCol.setCellValueFactory(new PropertyValueFactory<>("Repair"));
        costCol.setCellValueFactory(new PropertyValueFactory<>("Cost"));
        repairOffset = 0;

        loadThread = new Thread(() -> {
            List<Repairs> r = repairsService.AllRepairs(carId);
            ObservableList<Repairs> data = FXCollections.observableArrayList();

            if (r.size() == 0)
                System.out.println("No result");
            else {
                for(Repairs rep : r) {
                    Repairs rep1 = new Repairs();
                    Mechanics m = mechanicsService.findByMechanicId(rep.getMechanicId());
                    rep1.setRepair_id(rep.getRepair_id());
                    rep1.setMechanicId(rep.getMechanicId());
                    rep1.setRepair(rep.getRepair());
                    rep1.setCarId(rep.getCarId());
                    rep1.setStart_day(Date.valueOf(rep.getStart_day()));
                    rep1.setEnd_day(Date.valueOf(rep.getEnd_day()));
                    rep1.setCost(rep.getCost());
                    rep1.setMechanic_name(m.getName()+" "+m.getSurname());
                    data.add(rep1);
                }
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

        if (customers.size() == 0)
            System.out.println("No result");
        else {
            for(Customers custo : customers) {
                Customers cus = new Customers();
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
    private void select() {
        String SelectedUser="";
        int customerId=0;

        if (customersTable.getSelectionModel().getSelectedItem() != null) {
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

        // keep selected language
        if (global_lang.equals("eng")) carScene_changeToEnglishLang();
        else carScene_changeToSlovakLang();
    }

    @FXML
    private void filterMechanic() {
        List<Mechanics> mechanics = mechanicsService.getMechanics(nameTextReward.getText(),surnameTextReward.getText());
        ObservableList<Mechanics> data = FXCollections.observableArrayList();

        if (mechanics.size() == 0)
            System.out.println("No result");
        else {
            for (Mechanics mech : mechanics) {
                Mechanics mechanic = new Mechanics();
                mechanic.setName(mech.getName());
                mechanic.setSurname(mech.getSurname());
                data.add(mechanic);
            }
            mechanicsTableR.setItems(data);
        }
    }

    @FXML
    private void returnMechanic() {
        String SelectedMechanic;
        stageManager.switchScene(FxmlView.AdminScene);
        tabPane.getSelectionModel().select(financeTab);

        if(mechanicsTableR.getSelectionModel().getSelectedItem() != null) {
            SelectedMechanic = mechanicsTableR.getSelectionModel().getSelectedItem().getName() + " " + mechanicsTableR.getSelectionModel().getSelectedItem().getSurname();
            selectedMechanic.setText(SelectedMechanic);
        }
        else {
            if (global_lang.equals("eng")) selectedMechanic.setText("Selected Mechanic");
            else selectedMechanic.setText("Vybraný Mechanik");
        }
        // keep selected language
        if (global_lang.equals("eng")) changeToEnglishLang();
        else changeToSlovakLang();

        reward.setText(rewardForM);
        reasonReward.setText(reasonForRewardM);
    }

    @FXML
    private void filterMechanicsPayout() {
        List<Mechanics> mechanics= mechanicsService.getMechanics(nameTextPay.getText(),surnameTextPay.getText());
        ObservableList<Mechanics> data = FXCollections.observableArrayList();

        if(mechanics.size() == 0){
            System.out.println("No result");
        }
        else{
            for(Mechanics mech : mechanics) {
                Mechanics mechanic = new Mechanics();
                mechanic.setName(mech.getName());
                mechanic.setSurname(mech.getSurname());
                data.add(mechanic);
            }
            mechanicsTablePayout1.setItems(data);
        }
    }

    @FXML
    private void returnMechanicPayout() {
        String SelectedMechanic;
        stageManager.switchScene(FxmlView.AdminScene);
        tabPane.getSelectionModel().select(financeTab);

        // keep selected language
        if (global_lang.equals("eng")) changeToEnglishLang();
        else changeToSlovakLang();

        if(mechanicsTablePayout1.getSelectionModel().getSelectedItem() != null) {
            SelectedMechanic = mechanicsTablePayout1.getSelectionModel().getSelectedItem().getName()+" "+mechanicsTablePayout1.getSelectionModel().getSelectedItem().getSurname();
            selectedMechanic1.setText(SelectedMechanic);
        }
        else {
            if (global_lang.equals("eng")) selectedMechanic1.setText("Selected Mechanic");
            else selectedMechanic1.setText("Vybraný Mechanik");
        }

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
                Cars car = new Cars();
                car.setModel(model.getModel());
                car.setBrand(model.getBrand());
                car.setVin(model.getVin());
                car.setCar_id(model.getCar_id());
                car.setOwner_id(model.getOwner_id());
                car.setFuel(model.getFuel());
                data.add(car);
            }
            carsTableCar.setItems(data);
        }
    }

    @FXML
    private void selectCar() {
        stageManager.switchScene(FxmlView.AdminScene);

        // keep selected language
        if (global_lang.equals("eng")) changeToEnglishLang();
        else changeToSlovakLang();

        tabPane.getSelectionModel().select(repairHistoryTab);
        if(carsTableCar.getSelectionModel().getSelectedItem() != null) {
            carSelection.setText(carsTableCar.getSelectionModel().getSelectedItem().getBrand() + " " + carsTableCar.getSelectionModel().getSelectedItem().getModel());
            loadSelectedRepairInfo((int) carsTableCar.getSelectionModel().getSelectedItem().getCar_id());
        }
    }

    @FXML
    private void generatePDF() {
        tabPane.getSelectionModel().select(repairHistoryTab);
        if(repairHistTable != null && repairHistTable.getSelectionModel().getSelectedItem() != null &&
            carsTableCar != null && carsTableCar.getSelectionModel().getSelectedItem() != null) {
            String[] parameters = new String[13];

            Cars c = carsTableCar.getSelectionModel().getSelectedItem();
            Customers owner = customersService.findByCustomerId(c.getOwner_id());
            Repairs r = repairHistTable.getSelectionModel().getSelectedItem();
            Mechanics m = mechanicsService.findByMechanicId(r.getMechanicId());

            parameters[0] = c.getBrand();
            parameters[1] = c.getModel();
            parameters[2] = c.getVin();
            parameters[12] = c.getFuel();
            parameters[3] = r.getStart_day();
            parameters[4] = r.getEnd_day();
            parameters[5] = String.valueOf(r.getDays());
            parameters[6] = String.valueOf(r.getCost());
            parameters[7] = m.getName() + ' ' + m.getSurname();
            parameters[8] = r.getRepair();
            parameters[9] = owner.getName() + " " + owner.getSurname();
            parameters[10] = owner.getEmail();
            parameters[11] = owner.getPhone_number();

            PDFSampleMain.main(parameters);
            SelectARepair.setVisible(false);
            billGeneratedOK.setVisible(true);
        } else {
            SelectARepair.setVisible(true);
            billGeneratedOK.setVisible(false);
        }
    }

    // Login screen
    @FXML private Label login_Username_label;
    @FXML private Label login_Password_label;
    @FXML private Button Login_btn;

    @FXML
    void changeToEnglishLang_LoginScreen() {
        // Login screen
        login_Username_label.setText("Username :");
        login_Password_label.setText("Password :");
        Login_btn.setText("Sign in");
        if (errMess != null) { errMess.setText("Wrong username or password!"); }
        global_lang = "eng";
    }

    @FXML
    void changeToSlovakLang_LoginScreen() {
        // Login screen
        login_Username_label.setText("Meno :");
        login_Password_label.setText("Heslo :");
        Login_btn.setText("Prihlásiť sa");
        if (errMess != null) { errMess.setText("Chybné prisahlovacie údaje!"); }
        global_lang = "svk";
    }

    // AdminMainScene - New customer tab
    @FXML private Tab New_customer_tab;
    @FXML private Label New_customer_label;
    @FXML private Label NCt_name_label;
    @FXML private Label NCt_surname_label;
    @FXML private Label NCt_phone_num_label;
    @FXML private Button newCustomer_btn;
    @FXML private Button car_information_btn;
    // AdminMainScene - Account management tab
    @FXML private Tab Acc_man_tab;
    @FXML private Label AMt_New_acc_label;
    @FXML private Label AMt_username_label;
    @FXML private Label AMt_password_label;
    @FXML private Label AMt_name_label;
    @FXML private Label AMt_surname_label;
    @FXML private Label AMt_type_label;
    @FXML private Button AMt_newUser_btn;
    // AdminMainScene - New component tab
    @FXML private Tab NCot_tab;
    @FXML private Label NCot_component_label;
    @FXML private Label NCot_carType_label;
    @FXML private Label NCot_cost_label;
    @FXML private Label NCot_amount_label;
    @FXML private Button NCot_addComponent_btn;
    // AdminMainScene - Finance tab
    @FXML private Label Ft_rewrads_label;
    @FXML private Label Ft_payouts_label;
    @FXML private Label Ft_rewardSize_label;
    @FXML private Label Ft_payoutSize_label;
    @FXML private Label Ft_rewardReason_label;
    @FXML private Label Ft_selectMechanic_label;
    @FXML private Label Ft_selectMechanic_label1;
    @FXML private Button Ft_selectMechanic_btn;
    @FXML private Button Ft_selectMechanic_btn1;
    @FXML private Button Ft_addReward_btn;
    @FXML private Button Ft_updatePayout_btn;
    @FXML private Button Ft_addPayout_btn;
    @FXML private Button Ft_deletePayout_btn;
    // AdminMainScene - Overview of repairs tab
    @FXML private Label OORt_name_label;
    @FXML private Label OORt_surname_label;
    @FXML private Label OORt_totalNumOfRep_label;
    @FXML private Label OORt_totalRepTime_label;
    @FXML private Label OORt_avgRepTime_label;
    @FXML private Button OORt_filter_btn;
    @FXML private Button OORt_showDetails_btn;
    // AdminMainScene - Repair history tab
    @FXML private Label RHt_selectCar_label;
    @FXML private Label RHt_costRepairs_label;
    @FXML private Label RHt_costComponents_label;
    @FXML private Button RHt_select_btn;
    @FXML private Button RHt_generateBill_btn;
    @FXML private Label billGeneratedOK;

    @FXML
    private void changeToEnglishLang() {
        // AdminMainScene - New customer tab
        New_customer_label.setText("New Customer");
        NCt_name_label.setText("Name :");
        NCt_surname_label.setText("Surname :");
        NCt_phone_num_label.setText("Phone number :");
        newCustomer_btn.setText("Add Customer");
        car_information_btn.setText("Add car information");
        New_customer_tab.setText("New customer");
        if (invalidFormat != null){ invalidFormat.setText("Missing values!"); }

        // AdminMainScene - Account management tab
        Acc_man_tab.setText("Account management");
        AMt_New_acc_label.setText("New account");
        AMt_username_label.setText("Username :");
        AMt_password_label.setText("Password :");
        AMt_name_label.setText("Name :");
        AMt_surname_label.setText("Surname :");
        AMt_type_label.setText("Type");
        userTypeMechanic.setText("Mechanic");
        userTypeAdmin.setText("Administrator");
        AMt_newUser_btn.setText("Add");
        if (newUserError != null) { newUserError.setText("Missing values!"); }

        // AdminMainScene - New component tab
        NCot_tab.setText("New component");
        NCot_component_label.setText("Component :");
        NCot_carType_label.setText("Car type :");
        NCot_cost_label.setText("Cost :");
        NCot_amount_label.setText("Amount :");
        NCot_addComponent_btn.setText("Add component");
        if (errComponents != null) { errComponents.setText("Invalid values!"); }

        // AdminMainScene - Finance tab
        financeTab.setText("Finance");
        Ft_rewrads_label.setText("Rewards:");
        Ft_payouts_label.setText("Payouts:");
        Ft_rewardSize_label.setText("Fill in the size of the reward :");
        Ft_payoutSize_label.setText("Fill in the size of the payout :");
        Ft_rewardReason_label.setText("Fill in the reason for reward :");
        Ft_selectMechanic_label.setText("Select a mechanic :");
        Ft_selectMechanic_label1.setText("Select a mechanic :");
        Ft_selectMechanic_btn.setText("select a mechanic");
        Ft_selectMechanic_btn1.setText("select a mechanic");
        Ft_addReward_btn.setText("add reward");
        Ft_updatePayout_btn.setText("update payout");
        Ft_addPayout_btn.setText("add payout");
        Ft_deletePayout_btn.setText("delete payout");
        if (invalidFormatR != null) { invalidFormatR.setText("Missing values!"); }
        if (invalidFormatP != null) { invalidFormatP.setText("Missing values!"); }
        if (selectedMechanic.getText().equals("Vybraný Mechanik")) { selectedMechanic.setText("Selected Mechanic"); }
        if (selectedMechanic1.getText().equals("Vybraný Mechanik")) { selectedMechanic1.setText("Selected Mechanic"); }

        // AdminMainScene - Overview of repairs tab
        overview_R_Tab.setText("Overview of repairs");
        showingRepairs.setText("Show the repairs");
        nameCol.setText("Name");
        surnameCol.setText("Surname");
        OORt_name_label.setText("Name :");
        OORt_surname_label.setText("Surname :");
        OORt_totalNumOfRep_label.setText("Total number of repairs:");
        OORt_totalRepTime_label.setText("Total repair time:");
        OORt_avgRepTime_label.setText("Average repair time:");
        OORt_filter_btn.setText("Filter");
        OORt_showDetails_btn.setText("Show details");

        // AdminMainScene - Repair history tab
        repairHistoryTab.setText("Repair history");
        repairCol.setText("Repair");
        costCol.setText("Cost");
        startCol.setText("Start");
        finishCol.setText("Finish");
        donebyCol.setText("Mechanic");
        RHt_selectCar_label.setText("Select a car :");
        RHt_costRepairs_label.setText("Cost of repairs :");
        RHt_costComponents_label.setText("Cost of components :");
        RHt_select_btn.setText("Select");
        RHt_generateBill_btn.setText("Generate bill");
        if (carSelection.getText().equals("Vybrané auto")) { carSelection.setText("Selected car"); }
        if (SelectARepair != null) { SelectARepair.setText("Select a repair!"); }
        if (billGeneratedOK != null) { billGeneratedOK.setText("Bill generated!"); }

        global_lang = "eng";
    }

    @FXML
    private void changeToSlovakLang() {
        // AdminMainScene - Nový zákazník tab
        New_customer_label.setText("Nový zákazník");
        NCt_name_label.setText("Meno :");
        NCt_surname_label.setText("Priezvisko :");
        NCt_phone_num_label.setText("Telefónne číslo :");
        newCustomer_btn.setText("Pridať zákazníka");
        car_information_btn.setText("Pridať auto");
        New_customer_tab.setText("Nový zákazník");
        if (invalidFormat != null){ invalidFormat.setText("Chýbajúce hodnoty!"); }

        // AdminMainScene - Správa účtov tab
        Acc_man_tab.setText("Správa účtov");
        AMt_New_acc_label.setText("Nový účet");
        AMt_username_label.setText("Používateľské meno :");
        AMt_password_label.setText("Heslo :");
        AMt_name_label.setText("Meno :");
        AMt_surname_label.setText("Priezvisko :");
        AMt_type_label.setText("Typ");
        userTypeMechanic.setText("Mechanik");
        userTypeAdmin.setText("Administrátor");
        AMt_newUser_btn.setText("Pridať");
        if (newUserError != null) { newUserError.setText("Chýbajúce hodnoty!"); }

        // AdminMainScene - Nový komponent tab
        NCot_tab.setText("Nový komponent");
        NCot_component_label.setText("Komponent :");
        NCot_carType_label.setText("Typ auta :");
        NCot_cost_label.setText("Cena :");
        NCot_amount_label.setText("Počet :");
        NCot_addComponent_btn.setText("Pridať komponent");
        if (errComponents != null) { errComponents.setText("Neplatné hodnoty!"); }

        // AdminMainScene - Financie tab
        financeTab.setText("Financie");
        Ft_rewrads_label.setText("Odmeny :");
        Ft_payouts_label.setText("Výplaty :");
        Ft_rewardSize_label.setText("Vyplňte výšku odmeny :");
        Ft_payoutSize_label.setText("Vyplňte výšku výplaty :");
        Ft_rewardReason_label.setText("Vyplňte dôvod odmeny :");
        Ft_selectMechanic_label.setText("Vyberte mechanika :");
        Ft_selectMechanic_label1.setText("Vyberte mechanika :");
        Ft_selectMechanic_btn.setText("vyberte mechanika");
        Ft_selectMechanic_btn1.setText("vyberte mechanika");
        Ft_addReward_btn.setText("pridať odmenu");
        Ft_updatePayout_btn.setText("aktualizovať výplatu");
        Ft_addPayout_btn.setText("pridať výplatu");
        Ft_deletePayout_btn.setText("vymazať výplatu");
        if (invalidFormatR != null) { invalidFormatR.setText("Chýbajú hodnoty!"); }
        if (invalidFormatP != null) { invalidFormatP.setText("Chýbajú hodnoty!"); }
        if (selectedMechanic.getText().equals("Selected Mechanic")) { selectedMechanic.setText("Vybraný Mechanik"); }
        if (selectedMechanic1.getText().equals("Selected Mechanic")) { selectedMechanic1.setText("Vybraný Mechanik"); }

        // AdminMainScene - Prehľad opráv tab
        overview_R_Tab.setText("Prehľad opráv");
        showingRepairs.setText("Zobraziť opravy");
        nameCol.setText("Meno");
        surnameCol.setText("Priezvisko");
        OORt_name_label.setText("Meno :");
        OORt_surname_label.setText("Priezvisko :");
        OORt_totalNumOfRep_label.setText("Celkový počet opráv:");
        OORt_totalRepTime_label.setText("Celkový čas opráv:");
        OORt_avgRepTime_label.setText("Priemerný čas opráv:");
        OORt_filter_btn.setText("Filtorvať");
        OORt_showDetails_btn.setText("Zobraziť detaily");

        // AdminMainScene - História opráv tab
        repairHistoryTab.setText("História opráv");
        carSelection.setText("Vybrané auto");
        repairCol.setText("Oprava");
        costCol.setText("Cena");
        startCol.setText("Začiatok");
        finishCol.setText("Koniec");
        donebyCol.setText("Mechanik");
        RHt_selectCar_label.setText("Vyberte auto :");
        RHt_costRepairs_label.setText("Cena opráv :");
        RHt_costComponents_label.setText("Cena komponentov :");
        RHt_select_btn.setText("Vybrať");
        RHt_generateBill_btn.setText("Vygenerovať faktúru");
        if (carSelection.getText().equals("Selected car")) { carSelection.setText("Vybrané auto"); }
        if (SelectARepair != null) { SelectARepair.setText("Vyberte opravu!"); }
        if (billGeneratedOK != null) { billGeneratedOK.setText("Faktúra OK!"); }

        global_lang = "svk";
    }

    @FXML private Button repScene_back_btn;
    @FXML private Label repairsTitle_label;

    @FXML
    private void repairScene_changeToEnglishLang() {
        RepairTextR.setText("Repair");
        StartDayR.setText("Start");
        EndDayR.setText("End");
        DaysRR.setText("Days");
        repairsTitle_label.setText("Repairs");
        repScene_back_btn.setText("< Back");
        global_lang = "eng";
    }

    @FXML
    private void repairScene_changeToSlovakLang() {
        RepairTextR.setText("Oprava");
        StartDayR.setText("Začiatok");
        EndDayR.setText("Koniec");
        DaysRR.setText("Počet dní");
        repairsTitle_label.setText("Opravy");
        repScene_back_btn.setText("< Späť");
        global_lang = "svk";
    }

    @FXML private Label mechSelPayoutScene_title_label;
    @FXML private Label mechSelPayoutScene_name_label;
    @FXML private Label mechSelPayoutScene_surname_label;
    @FXML private Label mechSelPayoutScene_results_label;
    @FXML private Button mechSelPayoutScene_search_btn;
    @FXML private Button mechSelPayoutScene_select_btn;

    @FXML
    private void mechSelPayoutScene_changeToEnglishLang() {
        mechSelPayoutScene_title_label.setText("Mechanic selection");
        mechSelPayoutScene_name_label.setText("Name :");
        mechSelPayoutScene_surname_label.setText("Surname :");
        mechSelPayoutScene_results_label.setText("Results :");
        mechSelPayoutScene_search_btn.setText("Search");
        mechSelPayoutScene_select_btn.setText("Select");
        nameColPay.setText("Name");
        surnameColPay.setText("Surname");
        global_lang = "eng";
    }

    @FXML
    private void mechSelPayoutScene_changeToSlovakLang() {
        mechSelPayoutScene_title_label.setText("Výber mechanika");
        mechSelPayoutScene_name_label.setText("Meno :");
        mechSelPayoutScene_surname_label.setText("Priezvisko :");
        mechSelPayoutScene_results_label.setText("Výsledky :");
        mechSelPayoutScene_search_btn.setText("Hľadať");
        mechSelPayoutScene_select_btn.setText("Vybrať");
        nameColPay.setText("Meno");
        surnameColPay.setText("Priezvisko");
        global_lang = "svk";
    }

    @FXML private Label mechSel_title_label;
    @FXML private Label mechSel_name_label;
    @FXML private Label mechSel_surname_label;
    @FXML private Label mechSel_results_label;
    @FXML private Button mechSel_search_btn;
    @FXML private Button mechSel_select_btn;

    @FXML
    private void mechSel_changeToEnglishLang() {
        nameColM.setText("Name");
        surnameColM.setText("Surname");
        mechSel_title_label.setText("Mechanic selection");
        mechSel_name_label.setText("Name :");
        mechSel_surname_label.setText("Surname :");
        mechSel_results_label.setText("Results :");
        mechSel_search_btn.setText("Search");
        mechSel_select_btn.setText("Select");
        global_lang = "eng";
    }

    @FXML
    private void mechSel_changeToSlovakLang() {
        nameColM.setText("Meno");
        surnameColM.setText("Priezvisko");
        mechSel_title_label.setText("Výber mechanika");
        mechSel_name_label.setText("Meno :");
        mechSel_surname_label.setText("Priezvisko :");
        mechSel_results_label.setText("Výsledky :");
        mechSel_search_btn.setText("Hľadať");
        mechSel_select_btn.setText("Vybrať");
        global_lang = "svk";
    }

    @FXML private Label custSel_title_label;
    @FXML private Label custSel_filterBy_label;
    @FXML private Label custSel_results_label;
    @FXML private Label custSel_name_label;
    @FXML private Label custSel_surname_label;
    @FXML private Button custSel_filter_btn;
    @FXML private Button custSel_select_btn;

    @FXML
    private void custSel_changeToEnglishLang() {
        custSel_title_label.setText("Customer selection");
        custSel_filterBy_label.setText("Filter by:");
        custSel_results_label.setText("Results:");
        custSel_name_label.setText("Name :");
        custSel_surname_label.setText("Surname :");
        custSel_filter_btn.setText("Filter");
        custSel_select_btn.setText("Select");
        global_lang = "eng";
    }

    @FXML
    private void custSel_changeToSlovakLang() {
        custSel_title_label.setText("Výber zákazníka");
        custSel_filterBy_label.setText("Filtorvať podľa:");
        custSel_results_label.setText("Výsledky:");
        custSel_name_label.setText("Meno :");
        custSel_surname_label.setText("Priezvisko :");
        custSel_filter_btn.setText("Filtorvať");
        custSel_select_btn.setText("Vybrať");
        global_lang = "svk";
    }

    @FXML private Label carSel_title_label;
    @FXML private Label carSel_type_label;
    @FXML private Button carSel_filter_btn;
    @FXML private Button carSel_select_btn;

    @FXML
    private void carSel_changeToEnglishLang() {
        typeColCar.setText("Brand");
        carSel_title_label.setText("Car selection");
        carSel_type_label.setText("Type :");
        carSel_filter_btn.setText("Filter");
        carSel_select_btn.setText("Select");
        global_lang = "eng";
    }

    @FXML
    private void carSel_changeToSlovakLang() {
        typeColCar.setText("Značka");
        carSel_title_label.setText("Výber auta");
        carSel_type_label.setText("Typ :");
        carSel_filter_btn.setText("Filtrovať");
        carSel_select_btn.setText("Vybrať");
        global_lang = "svk";
    }

    @FXML private Label carScene_title_label;
    @FXML private Label carScene_brand_label;
    @FXML private Label carScene_VIN_label;
    @FXML private Label carScene_customer_label;
    @FXML private Label carScene_fuelType_label;
    @FXML private Button carScene_back_btn;
    @FXML private Button carScene_selectCustomer_btn;
    @FXML private Button carScene_addCar_btn;

    @FXML
    private void carScene_changeToEnglishLang() {
        carScene_title_label.setText("Car information");
        carScene_brand_label.setText("Brand :");
        carScene_VIN_label.setText("VIN number :");
        carScene_customer_label.setText("Customer :");
        carScene_fuelType_label.setText("Fuel type :");
        carScene_back_btn.setText("< Back");
        carScene_selectCustomer_btn.setText("Select a customer");
        carScene_addCar_btn.setText("Add car");
        if (selectedCustomer.getText().equals("Vybraný Zákazník")) { selectedCustomer.setText("Selected Customer"); }
        if (emptyFieldsError != null) { emptyFieldsError.setText("Some fields are missing!"); }
        global_lang = "eng";
    }

    @FXML
    private void carScene_changeToSlovakLang() {
        carScene_title_label.setText("Informácie o aute");
        carScene_brand_label.setText("Značka :");
        carScene_VIN_label.setText("VIN číslo :");
        carScene_customer_label.setText("Zákazník :");
        carScene_fuelType_label.setText("Typ paliva :");
        carScene_back_btn.setText("< Späť");
        carScene_selectCustomer_btn.setText("Vybrať zákazníka");
        carScene_addCar_btn.setText("Pridať auto");
        if (selectedCustomer.getText().equals("Selected Customer")) { selectedCustomer.setText("Vybraný Zákazník"); }
        if (emptyFieldsError != null) { emptyFieldsError.setText("Chýbajú niektoré hodnoty!"); }
        global_lang = "svk";
    }
}
