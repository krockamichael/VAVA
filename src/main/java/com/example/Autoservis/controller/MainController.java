package com.example.Autoservis.controller;

import com.example.Autoservis.PDFSampleMain;
import com.example.Autoservis.bean.*;
import com.example.Autoservis.config.StageManager;
import com.example.Autoservis.services.*;
import com.example.Autoservis.view.FxmlView;
import javafx.animation.PauseTransition;
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
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class MainController implements Initializable {
    protected static int uid;
    private static int idM;
    private String global_lang = "eng";
    private long carId;

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
    @FXML private ChoiceBox<String> fuelType;
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

    @FXML private TableView<Customers> customersTable;
    @FXML private TableColumn<Customers,String> nameCol1;
    @FXML private TableColumn<Customers,String> surnameCol1;
    @FXML private TableColumn<Customers,String> idCol;
    @FXML private TableColumn<Customers,String> elementIdCol;
    @FXML private TextField nameText;
    @FXML private TextField surnameText;

    @FXML private TableView<Mechanics> mechanicsTableR;
    @FXML private TableColumn<Mechanics,String> nameColM;
    @FXML private TableColumn<Mechanics,String> surnameColM;
    @FXML private TextField surnameTextReward;
    @FXML private TextField nameTextReward;

    @FXML private TableView<Mechanics> mechanicsTablePayout1;
    @FXML private TableColumn<Mechanics,String> nameColPay;
    @FXML private TableColumn<Mechanics,String> surnameColPay;
    @FXML private TextField nameTextPay;
    @FXML private TextField surnameTextPay;

    @FXML private TextField carModelCar;
    @FXML private TextField carTypeCar;
    @FXML private TextField carVINCar;
    @FXML private TableView<Cars> carsTableCar;
    @FXML private TableColumn<Cars,String> modelColCar;
    @FXML private TableColumn<Cars,String> typeColCar;
    @FXML private TableColumn<Cars,String> vinColCar;
    @FXML private TableColumn<Cars,String> carIdColCar;

    @FXML private TableView<Repairs> RepairsTableR;
    @FXML private TableColumn<Repairs,String> RepairTextR;
    @FXML private TableColumn<Repairs,String> StartDayR;
    @FXML private TableColumn<Repairs,String> EndDayR;
    @FXML private TableColumn<Repairs,String> DaysRR;
    @FXML private Label SelectARepair;

    private Thread loadThread = null;

    @Autowired private UsersService usersService;
    @Autowired private MechanicsService mechanicsService;
    @Autowired private CustomersService customersService;
    @Autowired private PayoutsService payoutsService;
    @Autowired private RewardsService rewardsService;
    @Autowired private CarsService carsService;
    @Autowired private ComponentsService componentsService;
    @Autowired private RepairsService repairsService;

    @FXML private TabPane tabPane;
    @FXML private Tab financeTab;
    @FXML private Tab overview_R_Tab;
    @FXML private Tab repairHistoryTab;

    @Lazy @Autowired private StageManager stageManager;

    private static final Logger logger = Logger.getLogger(MainController.class.getName());

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    //closes the login window on click
    @FXML
    protected void closeStageAction(ActionEvent event){
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    //login
    @FXML
    protected void loginAction() {
        // get the user type based on username and password
        int userType = usersService.checkCredentials(Username.getText(), Password.getText());

        // if user is type 1, he is an admin
        if(userType == 1) {
            stageManager.switchScene(FxmlView.AdminScene);
            // keep selected language
            if (global_lang.equals("eng")) changeToEnglishLang();
            else changeToSlovakLang();
            logger.log(Level.INFO,"Credentials checked, Admin is logged in");
        }
        // if user is type 2, he is a mechanic
        else if (userType == 2) {
            // keep selected language
            if (global_lang.equals("eng")){
                MechanicMainSceneController.global_lang = "eng";
                stageManager.switchScene(FxmlView.MechanicScene);
            }
            else{
                MechanicMainSceneController.global_lang = "svk";
                stageManager.switchScene(FxmlView.MechanicSceneSVK);
            }
            logger.log(Level.INFO,"Credentials checked, Mechanic is logged in");
            uid = uid-1;
        }
        else {
            logger.log(Level.WARNING, "Unsuccessful login attempted");
            // if user doesn't exist, generate error message
            errMess.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  errMess.setVisible(false));
            visibleP.play();
        }
    }

    @FXML
    protected void logout() {
        stageManager.switchScene(FxmlView.Login);
        logger.log(Level.INFO,"User was logged out");
        // keep selected language
        if (global_lang.equals("eng")) changeToEnglishLang_LoginScreen();
        else changeToSlovakLang_LoginScreen();
    }

    @FXML
    private void addNewUser() {
        // get values from text fields
        newUserError.setVisible(false);
        String name = newUserName.getText();
        String password = newUserPass.getText();
        String mechName = mechanicName.getText();
        String mechSurname = mechanicSurname.getText();

        // check if all values are entered
        if (name.equals("") || Password.toString().equals("") || mechName.equals("") || mechSurname.equals("")) {
            logger.log(Level.WARNING,"Add new user - missing values - aborting");
            newUserError.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  newUserError.setVisible(false));
            visibleP.play();
            return;
        }

        // set user type
        // mechanic = 2
        // admin = 1
        if (userTypeMechanic.isSelected()) {
            // create new user object always and save it to database
            Users user = new Users(name, password, 2);
            usersService.save(user);

            // new mechanic is created and is saved to database
            Mechanics mechanic = new Mechanics(mechName, mechSurname, (int) usersService.getID(name, password));
            mechanicsService.save(mechanic);
            logger.log(Level.INFO,"New mechanic added to databases Users and Mechanics");

            //generate successful message
            newAccountAddedLabel.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  newAccountAddedLabel.setVisible(false));
            visibleP.play();
        }
        else if (userTypeAdmin.isSelected()) {
            // create new user object always and save it to database
            Users user = new Users(name, password, 1);
            usersService.save(user);
            logger.log(Level.INFO,"New admin added to database Users");

            //generate successful message
            newAccountAddedLabel.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  newAccountAddedLabel.setVisible(false));
            visibleP.play();
        }
        else {
            logger.log(Level.WARNING,"Add new user - undefined user type - aborting");
            // if neither is selected generate error message
            newUserError.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  newUserError.setVisible(false));
            visibleP.play();
            return;
        }

        // reset text fields
        newUserName.setText("");
        newUserPass.setText("");
        mechanicName.setText("");
        mechanicSurname.setText("");
    }

    @FXML
    private void addNewCustomer() {
        // get values from text fields
        invalidFormat.setVisible(false);
        String custo_Name = Customer_name.getText();
        String custo_Surname = Customer_surname.getText();
        String custo_Email = Customer_email.getText();
        String custo_IdNum = customerIdNum.getText();
        String custo_Phone = Customer_phone.getText();

        // check if all values are entered
        if (custo_Email.equals("") || custo_IdNum.equals("") || custo_Name.equals("") || custo_Phone.equals("")) {
            logger.log(Level.WARNING,"Add new customer - missing values - aborting");
            invalidFormat.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  invalidFormat.setVisible(false));
            visibleP.play();
            return;
        }
        // check if phone number is correct
        if (custo_Phone.matches("[0-9]+") && custo_Phone.length() > 2) {
            // create new customer object and save it to database
            Customers customer = new Customers(custo_Name, custo_Surname, custo_Email, custo_Phone, custo_IdNum);
            customersService.save(customer);
            logger.log(Level.INFO,"New customer added to database Customers");

            // reset text fields
            Customer_name.setText("");
            Customer_email.setText("");
            Customer_phone.setText("");
            customerIdNum.setText("");
            Customer_surname.setText("");

            //generate successful message
            customerAddedLabel.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  customerAddedLabel.setVisible(false));
            visibleP.play();
        }
        else {
            logger.log(Level.WARNING, "Add new customer - invalid phone number - aborting");
            // if phone number is incorrect generate error message
            invalidFormat.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  invalidFormat.setVisible(false));
            visibleP.play();
        }
    }

    @FXML
    private void addCarInformation() {
        stageManager.switchScene(FxmlView.CarScene);

        // keep selected language
        if (global_lang.equals("eng")) carScene_changeToEnglishLang();
        else carScene_changeToSlovakLang();

        // add values to fuel type choice box
        ObservableList<String> fuelTypeBox = FXCollections.observableArrayList();
        fuelTypeBox.add("");
        fuelTypeBox.add("Benzín");
        fuelTypeBox.add("Diesel");
        fuelTypeBox.add("Elektrina");
        fuelType.setItems(fuelTypeBox);
        fuelType.getSelectionModel().select(0);
    }

    private String rewardForM;
    private String reasonForRewardM;
    private String payoutForM;

    @FXML
    private void selectMechanicReward() {
        // save text field values so they don't reset when changing scenes
        rewardForM = reward.getText();
        reasonForRewardM = reasonReward.getText();
        stageManager.switchScene(FxmlView.MechanicSelectionScene);

        // keep selected language
        if (global_lang.equals("eng")) mechSel_changeToEnglishLang();
        else mechSel_changeToSlovakLang();

        // set the table
        nameColM.setCellValueFactory(new PropertyValueFactory<>("Name"));
        surnameColM.setCellValueFactory(new PropertyValueFactory<>("Surname"));
    }

    @FXML
    private void selectMechanicPayout() {
        // save text field values so they don't reset when changing scenes
        payoutForM = payout_s.getText();
        stageManager.switchScene(FxmlView.MechanicSelectionPayoutScene);

        // keep selected language
        if (global_lang.equals("eng")) mechSelPayoutScene_changeToEnglishLang();
        else mechSelPayoutScene_changeToSlovakLang();

        // set the table
        nameColPay.setCellValueFactory(new PropertyValueFactory<>("Name"));
        surnameColPay.setCellValueFactory(new PropertyValueFactory<>("Surname"));
    }

    @FXML
    private void addNewPayout() {
        // get values from text fields, split mechanic name into first name and surname
        String[] r1 = selectedMechanic1.getText().split("\\s+");
        String number = payout_s.getText();

        invalidFormatP.setVisible(false);
        // check if all values are set
        if (r1[0].equals("") || r1[1].equals("")) {
            logger.log(Level.WARNING,"Add new payout - missing values - aborting");

            invalidFormatP.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  invalidFormatP.setVisible(false));
            visibleP.play();
            return;
        }

        // if number is really a number
        if (number.matches("[0-9]+")) {
            // get mechanic object by name and surname
            Mechanics mechanic = mechanicsService.findByNameAndSurname(r1[0], r1[1]);

            // create new payouts object and save it to database
            Payouts payout = new Payouts(r1[0], r1[1], Integer.parseInt(number), mechanic.getMechanicId());
            payoutsService.save(payout);
            logger.log(Level.INFO,"New payout added to database Payouts");

            // reset text fields
            selectedMechanic1.setText("");
            payout_s.setText("");

            //generate successful message
            newPayoutAddedLabel.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  newPayoutAddedLabel.setVisible(false));
            visibleP.play();
        } else {
            logger.log(Level.WARNING, "Add new payout - invalid payout - aborting");
            // if number is not a number, generate error message
            invalidFormatP.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  invalidFormatP.setVisible(false));
            visibleP.play();
        }
    }

    @FXML
    private void updatePayout() {
        // get text field values, mechanic name and surname, payout
        String[] r1 = selectedMechanic1.getText().split("\\s+");
        String number = payout_s.getText();

        invalidFormatP.setVisible(false);
        // check if mechanic is valid
        if (r1[0].equals("") || r1[1].equals("")) {
            logger.log(Level.WARNING,"Update payout - missing values - aborting");
            invalidFormatP.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  invalidFormatP.setVisible(false));
            visibleP.play();
            return;
        }

        // check if number is really a number
        if (number.matches("[0-9]+")) {
            // get mechanic object by name and surname
            Mechanics mechanic = mechanicsService.findByNameAndSurname(r1[0], r1[1]);
            System.out.println("name: " + r1[0]);
            // update payout
            payoutsService.updatePayoutMechanic(Integer.parseInt(number), mechanic.getMechanicId(), r1[0], r1[1]);
            logger.log(Level.INFO,"A mechniac's payout was updated");
            // reset text fields
            selectedMechanic1.setText("");
            payout_s.setText("");

            //generate successful message
            payoutUpdatedLabel.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  payoutUpdatedLabel.setVisible(false));
            visibleP.play();
        }
        else {
            logger.log(Level.WARNING, "Update payout - invalid payout - aborting");
            // if number is not a number, generate error message
            invalidFormatP.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  invalidFormatP.setVisible(false));
            visibleP.play();
        }
    }

    @FXML
    private void deletePayout() {
        // get mechanic name and surname
        String[] r1 = selectedMechanic1.getText().split("\\s+");

        invalidFormatP.setVisible(false);
        // check if a proper mechanic is selected
        if ((r1[0].equals("") || r1[1].equals("")) || (r1[0].equals("Selected") || r1[0].equals("Mechanic"))) {
            logger.log(Level.WARNING, "Delete payout - missing values - aborting");
            invalidFormatP.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  invalidFormatP.setVisible(false));
            visibleP.play();
        }
        else {
            // get mechanic object by name and surname
            Mechanics mechanic = mechanicsService.findByNameAndSurname(r1[0],r1[1]);
            // delete reward and payout
            payoutsService.deletePayoutMechanic(mechanic.getMechanicId(),r1[0],r1[1]);
            rewardsService.deleteRewardMechanic(mechanic.getMechanicId(),r1[0],r1[1]);
            logger.log(Level.INFO,"A reward and a payout was deleted");
            // reset text field
            selectedMechanic1.setText("");

            //generate successful message
            payoutDeletedLabel.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  payoutDeletedLabel.setVisible(false));
            visibleP.play();
        }
    }

    @FXML
    private void addNewReward() {
        // get text field values
        String number = reward.getText();
        String reasonfor = reasonReward.getText();
        String[] r = selectedMechanic.getText().split("\\s+");

        invalidFormatR.setVisible(false);
        // check if mechanic is selcted
        if (r[0].equals("") || r[1].equals("")) {
            logger.log(Level.WARNING,"Add new reward - missing values - aborting");
            invalidFormatR.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  invalidFormatR.setVisible(false));
            visibleP.play();
            return;
        }

        // if the number is really a number
        if (number.matches("[0-9]+")) {
            int sd = Integer.parseInt(number);
            // get mechanic by name and surname
            Mechanics mechanic = mechanicsService.findByNameAndSurname(r[0],r[1]);
            // create new rewards object and save it to database
            Rewards rewardsM = new Rewards(r[0], r[1], Integer.parseInt(number), reasonfor, mechanic.getMechanicId());
            rewardsService.save(rewardsM);
            logger.log(Level.INFO,"New reward added to database Rewards");

            // get a payout object
            Payouts oldPayout = payoutsService.findByNameAndSurname(r[0],r[1]);
            if (oldPayout == null) {
                // create a new payout object if it doesn't exist and save it to database
                Payouts payout = new Payouts(r[0], r[1], sd, mechanic.getMechanicId());
                payoutsService.save(payout);
                logger.log(Level.INFO,"New payout added to database Payouts");
            }
            else {
                // if a payout object exists, update it
                int newAmount = oldPayout.getAmount() + sd;
                payoutsService.updatePayoutMechanic(newAmount, mechanic.getMechanicId(), r[0], r[1]);
            }

            // reset text fields
            selectedMechanic.setText("");
            reward.setText("");
            reasonReward.setText("");

            newRewardAddedLabel.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  newRewardAddedLabel.setVisible(false));
            visibleP.play();
        }
        else {
            logger.log(Level.WARNING, "Add new reward - invalid reward - aborting");
            // if the number is invalid, generate error message
            invalidFormatR.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  invalidFormatR.setVisible(false));
            visibleP.play();
        }
    }

    @FXML
    private void addNewCar() {
        // get entered values
        String brand = Car_brand.getText();
        String model = Car_model.getText();
        String vin = Car_vin.getText();
        String fuel = fuelType.getSelectionModel().getSelectedItem();

        // check if they are all set, if not generate error message
        if (brand.equals("") || model.equals("") || vin.equals("") || fuel.equals("") || selectedCustomer.toString().equals("")) {
            logger.log(Level.WARNING, "Add new car - missing values - aborting");
            emptyFieldsError.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  emptyFieldsError.setVisible(false));
            visibleP.play();
        }

        // if ALL values are set
        else {
            // create new car object and save it to database
            Cars car = new Cars(model, brand, vin, fuel, Integer.parseInt(selectedCustomer.getUserData().toString()));
            carsService.save(car);
            logger.log(Level.INFO,"New car added to database Cars");

            // reset all text fields
            selectedCustomer.setText("");
            selectedCustomer.setUserData(null);
            emptyFieldsError.setVisible(false);
            fuelType.getSelectionModel().select(0);
            Car_brand.setText("");
            Car_model.setText("");
            Car_vin.setText("");

            carAddedMess.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  carAddedMess.setVisible(false));
            visibleP.play();
        }
    }

    @FXML
    private void BackToAdminScene() {
        stageManager.switchScene(FxmlView.AdminScene);
        // keep selected language
        if (global_lang.equals("eng")) changeToEnglishLang();
        else changeToSlovakLang();
    }

    private String vinCarC;
    private String brandCarC;
    private String modelCarC;
    private int fuelTypeCarC;

    @FXML
    private void loadCustomerSelection() {
        // save entered values so they can be re-entered when changing scenes
        vinCarC = Car_vin.getText();
        brandCarC = Car_brand.getText();
        modelCarC = Car_model.getText();
        fuelTypeCarC = fuelType.getSelectionModel().getSelectedIndex();

        stageManager.switchScene(FxmlView.CustomerSelection);
        // keep selected language
        if (global_lang.equals("eng")) custSel_changeToEnglishLang();
        else custSel_changeToSlovakLang();

        // set the table
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

        // check if cost is not null or whether cost value is not number
        if (cost.getText().equals("") || !cost.getText().matches("^[0-9]+\\.?[0-9]*$")) {
            logger.log(Level.WARNING,"Add new component - missing values - aborting");
            errComponents.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  errComponents.setVisible(false));
            visibleP.play();
            return;
        }
        costD = Double.parseDouble(cost.getText());

        // check if amount is not null or whether amount value is not number
        if (amount.getText().equals("") || !amount.getText().matches("^[0-9]+$")) {
            logger.log(Level.WARNING,"Add new component - invalid amount - aborting");
            errComponents.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  errComponents.setVisible(false));
            visibleP.play();
            return;
        }
        amountI = Integer.parseInt(amount.getText());

        // check if component attributes are set
        if (componentS.equals("") || carTypeS.equals("")) {
            logger.log(Level.WARNING, "Add new components - missing values - aborting");
            errComponents.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  errComponents.setVisible(false));
            visibleP.play();
        }
        else {
            // if they are set, create new Components object and save it to database
            Components nCompo = new Components(componentS, carTypeS, amountI, costD);
            componentsService.save(nCompo);
            logger.log(Level.INFO,"New component added to database Components");

            // reset text fields
            component.setText("");
            carType.setText("");
            cost.setText("");
            amount.setText("");

            newComponentAddedLabel.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  newComponentAddedLabel.setVisible(false));
            visibleP.play();
        }
    }

    @FXML
    private void loadCarSelection() {
        stageManager.switchScene(FxmlView.CarSelection);
        // keep selected language
        if (global_lang.equals("eng")) carSel_changeToEnglishLang();
        else carSel_changeToSlovakLang();

        // set the table
        modelColCar.setCellValueFactory(new PropertyValueFactory<>("Model"));
        typeColCar.setCellValueFactory(new PropertyValueFactory<>("Brand"));
        vinColCar.setCellValueFactory(new PropertyValueFactory<>("Vin"));
        carIdColCar.setCellValueFactory(new PropertyValueFactory<>("carId"));
    }

    @FXML
    private void filterSeMechanic() {
        // set the table
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        surnameCol.setCellValueFactory(new PropertyValueFactory<>("Surname"));

        // get list of mechanics
        ObservableList<Mechanics> mechanics = FXCollections.observableArrayList(mechanicsService.getMechanics(overName.getText(),overSurname.getText()));
        if (mechanics.isEmpty())
            logger.log(Level.WARNING,"Mechanics list is empty");
        else
            OverViewTable.setItems(mechanics); // add mechanics to table
    }

    @FXML
    private void returnSeMechanic() {
        String[] r = null;

        // get the name of the selected mechanic if an item is selected
        if (OverViewTable.getSelectionModel().getSelectedItem() != null) {
            String NameR = OverViewTable.getSelectionModel().getSelectedItem().getName() + " " +
                           OverViewTable.getSelectionModel().getSelectedItem().getSurname();
            r = NameR.split("\\s+");
        }

        if (r != null) {
            int originalQty;
            double numOfDays=0;
            double sumOfDate=0;
            double avgOfDate=0;

            // get mechanic object by name and surname
            Mechanics mechanic = mechanicsService.findByNameAndSurname(r[0], r[1]);
            originalQty = mechanic.getMechanicId();

            // calculate metrics of the mechanic
            if (repairsService.getNumberOfR(originalQty) != null)
                numOfDays = Double.parseDouble(repairsService.getNumberOfR(originalQty)); // total number of repairs
            if (repairsService.total(originalQty) != null)
                sumOfDate = Double.parseDouble(repairsService.total(originalQty)); // total days of repairs
            if (repairsService.avgDate(originalQty) != null)
                avgOfDate = Double.parseDouble(repairsService.avgDate(originalQty)); // average day for repair

            String det = Double.toString(numOfDays);
            String det1 = Double.toString(sumOfDate);
            String det2 = Double.toString(avgOfDate);

            // set text field values
            TotalNumber.setText(det);
            TotalRepairT.setText(det1);
            AveRepairT.setText(det2);
            OORt_selectArepair_label.setVisible(false);
        } else {
            logger.log(Level.WARNING,"Select a mechanic to show statistics - no mechanic selected - aborting");
            // if no mechanic is selected generate error message
            OORt_selectArepair_label.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  OORt_selectArepair_label.setVisible(false));
            visibleP.play();
        }
    }

    private String totalNumberOfRepairs;
    private String averageRepairTime;
    private String totalRepairTime;
    private String nameAtOverviewR;
    private String surnameAtOverviewR;
    private ObservableList<Mechanics> itemsMechanics;

    public void loadRepairD() {
        String[] r = null;

        // get the name of the selected mechanic
        if (OverViewTable.getSelectionModel().getSelectedItem() != null) {
            String NameR = OverViewTable.getSelectionModel().getSelectedItem().getName() + " " + OverViewTable.getSelectionModel().getSelectedItem().getSurname();
            r = NameR.split("\\s+");
        }

        if (r != null) {
            // get mechanic object by name and surname
            Mechanics mechanic = mechanicsService.findByNameAndSurname(r[0], r[1]);
            idM = mechanic.getMechanicId();

            // save text field (and table) values so they don't vanish during scene change
            nameAtOverviewR = overName.getText();
            surnameAtOverviewR = overSurname.getText();
            totalNumberOfRepairs = TotalNumber.getText();
            averageRepairTime = AveRepairT.getText();
            totalRepairTime = TotalRepairT.getText();
            itemsMechanics = OverViewTable.getItems();

            stageManager.switchScene(FxmlView.RepairScene);
            // keep selected language
            if (global_lang.equals("eng")) repairScene_changeToEnglishLang();
            else repairScene_changeToSlovakLang();

            // set the table
            RepairTextR.setCellValueFactory(new PropertyValueFactory<>("Repair"));
            StartDayR.setCellValueFactory(new PropertyValueFactory<>("Start_day"));
            EndDayR.setCellValueFactory(new PropertyValueFactory<>("End_day"));
            DaysRR.setCellValueFactory(new PropertyValueFactory<>("Days"));

            // get list of repairs
            ObservableList<Repairs> repairs = FXCollections.observableArrayList(repairsService.getWorkDetails(idM));

            if (repairs.isEmpty())
                logger.log(Level.WARNING,"Repairs list is empty");
            else {
                for(Repairs re: repairs) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        java.util.Date firstDate = sdf.parse(re.getEnd_day());
                        java.util.Date secondDate = sdf.parse(re.getStart_day());
                        long diffInMillis = Math.abs(firstDate.getTime() - secondDate.getTime());
                        long diff = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
                        re.setDays((int) diff + 1);
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Date parsing failed", e);
                    }
                }
                RepairsTableR.setItems(repairs); // set repairs to table
                OORt_selectArepair_label.setVisible(false);
            }
        }
        else {
            logger.log(Level.WARNING,"Select a mechanic to show repairs - no mechanic selected - aborting");
            // if an error occurred (no item is selected), generate error message
            OORt_selectArepair_label.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  OORt_selectArepair_label.setVisible(false));
            visibleP.play();
        }
    }

    @FXML
    private void backToAdminSceneRepair() {
        stageManager.switchScene(FxmlView.AdminScene);
        // keep selected language
        if (global_lang.equals("eng")) changeToEnglishLang();
        else changeToSlovakLang();

        // make sure the entered values don't disappear when changing scene
        tabPane.getSelectionModel().select(overview_R_Tab);
        overName.setText(nameAtOverviewR);
        overSurname.setText(surnameAtOverviewR);
        TotalNumber.setText(totalNumberOfRepairs);
        AveRepairT.setText(averageRepairTime);
        TotalRepairT.setText(totalRepairTime);

        // set the table
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        surnameCol.setCellValueFactory(new PropertyValueFactory<>("Surname"));
        OverViewTable.setItems(itemsMechanics);
    }

    public void loadSelectedRepairInfo(int carId1) {
        carId = carId1;
        // set the table
        startCol.setCellValueFactory(new PropertyValueFactory<>("Start_day"));
        finishCol.setCellValueFactory(new PropertyValueFactory<>("End_day"));
        donebyCol.setCellValueFactory(new PropertyValueFactory<>("Mechanic_name"));
        repairCol.setCellValueFactory(new PropertyValueFactory<>("Repair"));
        costCol.setCellValueFactory(new PropertyValueFactory<>("Cost"));

        loadThread = new Thread(() -> {
            // get list of repairs done on the car
            ObservableList<Repairs> repairs = FXCollections.observableArrayList(repairsService.allRepairs(carId1));

            if (repairs.isEmpty())
                logger.log(Level.WARNING,"Repairs list is empty");
            else {
                for (Repairs r: repairs) {
                    // add mechanic name to the repair item
                    Mechanics m = mechanicsService.findByMechanicId(r.getMechanicId());
                    r.setMechanic_name(m.getName() + " " + m.getSurname());
                }
                repairHistTable.setItems(repairs); // set repairs to table

                // get repair cost and component and show it
                String[] rCosts = repairsService.getCostSum(carId1).split(",");
                Platform.runLater(() -> {
                    repairCost.setText(rCosts[0]);
                    componentCost.setText(rCosts[1]);
                });
            }
        });
        loadThread.start();
    }

    @FXML
    private void filterCustomers() {
        // get customers based on name and surname
        ObservableList<Customers> customers = FXCollections.observableArrayList(customersService.getCustomers(nameText.getText(), surnameText.getText()));

        // check if list is empty
        if (customers.isEmpty())
            logger.log(Level.WARNING,"Customers list is empty");
        else
            customersTable.setItems(customers); // add customers to table
    }

    @FXML
    private void selectCustomer() {
        String selectedUser = "";
        int customerId = 0;

        // get name, surname and ID of customer
        if (customersTable.getSelectionModel().getSelectedItem() != null) {
            selectedUser = customersTable.getSelectionModel().getSelectedItem().getName() + " " +
                           customersTable.getSelectionModel().getSelectedItem().getSurname() + " " +
                           customersTable.getSelectionModel().getSelectedItem().getId();
            customerId = (int) customersTable.getSelectionModel().getSelectedItem().getCustomerId();
        }

        stageManager.switchScene(FxmlView.CarScene);

        // set values of fuel type selectBox
        ObservableList<String> fuelTypeBox = FXCollections.observableArrayList("", "Benzín", "Diesel", "Elektrina");
        fuelType.setItems(fuelTypeBox);

        // set values that were entered before
        Car_vin.setText(vinCarC);
        Car_brand.setText(brandCarC);
        Car_model.setText(modelCarC);
        fuelType.getSelectionModel().select(fuelTypeCarC);
        selectedCustomer.setText(selectedUser);
        selectedCustomer.setUserData(customerId);

        // keep selected language
        if (global_lang.equals("eng")) carScene_changeToEnglishLang();
        else carScene_changeToSlovakLang();
    }

    @FXML
    private void filterMechanic() {
        // get mechanics based on name and surname and set them to table
        ObservableList<Mechanics> mechanics = FXCollections.observableArrayList(mechanicsService.getMechanics(nameTextReward.getText(), surnameTextReward.getText()));

        if (mechanics.isEmpty())
            logger.log(Level.WARNING,"Mechanics list is empty");
        else
            mechanicsTableR.setItems(mechanics);
    }

    @FXML
    private void returnMechanic() {
        String selectedMechanicS;
        stageManager.switchScene(FxmlView.AdminScene);
        tabPane.getSelectionModel().select(financeTab);

        // check if a mechanic was selected
        if (mechanicsTableR.getSelectionModel().getSelectedItem() != null) {
            // set text field to his name and surname
            selectedMechanicS = mechanicsTableR.getSelectionModel().getSelectedItem().getName() + " " +
                               mechanicsTableR.getSelectionModel().getSelectedItem().getSurname();
            selectedMechanic.setText(selectedMechanicS);
        }
        else {
            // set text field to match language selection
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
        // get mechanics based on name and surname and set them to table
        ObservableList<Mechanics> mechanics = FXCollections.observableArrayList(mechanicsService.getMechanics(nameTextPay.getText(), surnameTextPay.getText()));

        if (mechanics.isEmpty())
            logger.log(Level.WARNING,"Mechanics list is empty");
        else
            mechanicsTablePayout1.setItems(mechanics);
    }

    @FXML
    private void returnMechanicPayout() {
        String selectedMechanic;
        stageManager.switchScene(FxmlView.AdminScene);
        tabPane.getSelectionModel().select(financeTab);

        // check if a mechanic was selected
        if (mechanicsTablePayout1.getSelectionModel().getSelectedItem() != null) {
            // set text field to his name and surname
            selectedMechanic = mechanicsTablePayout1.getSelectionModel().getSelectedItem().getName() + " " +
                               mechanicsTablePayout1.getSelectionModel().getSelectedItem().getSurname();
            selectedMechanic1.setText(selectedMechanic);
        }
        else {
            // set text field to match language selection
            if (global_lang.equals("eng")) selectedMechanic1.setText("Selected Mechanic");
            else selectedMechanic1.setText("Vybraný Mechanik");
        }
        // keep selected language
        if (global_lang.equals("eng")) changeToEnglishLang();
        else changeToSlovakLang();

        payout_s.setText(payoutForM);
    }

    @FXML
    protected void filterCar() {
        // get cars based on car model, brand and VIN number and set them to table
        ObservableList<Cars> lCar = FXCollections.observableArrayList(carsService.getCarsMaybe(carModelCar.getText(), carTypeCar.getText(), carVINCar.getText()));

        if (lCar.isEmpty())
            logger.log(Level.WARNING,"Cars list is empty");
        else
            carsTableCar.setItems(lCar);
    }

    @FXML
    private void selectCar() {
        stageManager.switchScene(FxmlView.AdminScene);

        // keep selected language
        if (global_lang.equals("eng")) changeToEnglishLang();
        else changeToSlovakLang();

        // change to the tab we were in
        tabPane.getSelectionModel().select(repairHistoryTab);
        // check if a car was selected
        if (carsTableCar.getSelectionModel().getSelectedItem() != null) {
            // get car brand and model
            carSelection.setText(carsTableCar.getSelectionModel().getSelectedItem().getBrand() + " " +
                                 carsTableCar.getSelectionModel().getSelectedItem().getModel());
            // load repairs for selected car
            loadSelectedRepairInfo((int) carsTableCar.getSelectionModel().getSelectedItem().getCar_id());
        }
    }

    @FXML
    private void generatePDF() {
        if (repairHistTable.getSelectionModel().getSelectedItem() != null) {
            String[] parameters = new String[13];

            // get selected car, owner of the car, get selected repair and mechanic who did the repair
            Cars c = carsTableCar.getSelectionModel().getSelectedItem();
            Customers owner = customersService.findByCustomerId(c.getOwner_id());
            Repairs r = repairHistTable.getSelectionModel().getSelectedItem();
            Mechanics m = mechanicsService.findByMechanicId(r.getMechanicId());

            // pass all the values to the PDF generator class
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
            logger.log(Level.INFO, "Generate PDF - PDF generated correctly");
            billGeneratedOK.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  billGeneratedOK.setVisible(false));
            visibleP.play();
        } else {
            logger.log(Level.WARNING, "Generate PDF - no repair is selected - aborting");
            billGeneratedOK.setVisible(false);
            // if no repair is selected, generate error message
            SelectARepair.setVisible(true);
            PauseTransition visibleP = new PauseTransition(Duration.seconds(2));
            visibleP.setOnFinished(actionEvent ->  SelectARepair.setVisible(false));
            visibleP.play();
        }
    }

    /**
    LANGUAGE FUNCTIONS - ENGLISH & SLOVAK
     - each screen has a specific set of labels, buttons and / or pre-filled text fields
     - when changing scenes, language functions are called to keep the selected language
     - language can be changed by clicking EN / SK language buttons in the application
    */

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
        if (errMess != null) { errMess.setText("Chybné prihlasovacie údaje!"); }
        global_lang = "svk";
    }

    // AdminMainScene - New customer tab
    @FXML private Tab New_customer_tab;
    @FXML private Label New_customer_label;
    @FXML private Label NCt_name_label;
    @FXML private Label NCt_surname_label;
    @FXML private Label NCt_phone_num_label;
    @FXML private Label customerAddedLabel;
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
    @FXML private Label newAccountAddedLabel;
    @FXML private Button AMt_newUser_btn;
    // AdminMainScene - New component tab
    @FXML private Tab NCot_tab;
    @FXML private Label NCot_component_label;
    @FXML private Label NCot_carType_label;
    @FXML private Label NCot_cost_label;
    @FXML private Label NCot_amount_label;
    @FXML private Label newComponentAddedLabel;
    @FXML private Button NCot_addComponent_btn;
    // AdminMainScene - Finance tab
    @FXML private Label Ft_rewrads_label;
    @FXML private Label Ft_payouts_label;
    @FXML private Label Ft_rewardSize_label;
    @FXML private Label Ft_payoutSize_label;
    @FXML private Label Ft_rewardReason_label;
    @FXML private Label Ft_selectMechanic_label;
    @FXML private Label Ft_selectMechanic_label1;
    @FXML private Label newRewardAddedLabel;
    @FXML private Label newPayoutAddedLabel;
    @FXML private Label payoutUpdatedLabel;
    @FXML private Label payoutDeletedLabel;
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
    @FXML private Label OORt_selectArepair_label;
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
        if (customerAddedLabel != null){ customerAddedLabel.setText("Customer added"); }

        // AdminMainScene - Account management tab
        Acc_man_tab.setText("Account management");
        AMt_New_acc_label.setText("New account");
        AMt_username_label.setText("Username :");
        AMt_password_label.setText("Password :");
        AMt_name_label.setText("Name :");
        AMt_surname_label.setText("Surname :");
        AMt_type_label.setText("type");
        userTypeMechanic.setText("Mechanic");
        userTypeAdmin.setText("Administrator");
        AMt_newUser_btn.setText("Add");
        if (newUserError != null) { newUserError.setText("Missing values!"); }
        if (newAccountAddedLabel != null) { newAccountAddedLabel.setText("Account created"); }

        // AdminMainScene - New component tab
        NCot_tab.setText("New component");
        NCot_component_label.setText("Component :");
        NCot_carType_label.setText("Car type :");
        NCot_cost_label.setText("Cost :");
        NCot_amount_label.setText("Amount :");
        NCot_addComponent_btn.setText("Add component");
        if (errComponents != null) { errComponents.setText("Invalid values!"); }
        if (newComponentAddedLabel != null) { newComponentAddedLabel.setText("Component added"); }

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
        if (newRewardAddedLabel != null) { newRewardAddedLabel.setText("Reward added"); }
        if (newPayoutAddedLabel != null) { newPayoutAddedLabel.setText("Payout added"); }
        if (payoutUpdatedLabel != null) { payoutUpdatedLabel.setText("Payout updated"); }
        if (payoutDeletedLabel != null) { payoutDeletedLabel.setText("Payout deleted"); }

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
        if (OORt_selectArepair_label != null) { OORt_selectArepair_label.setText("Select a repair!"); }

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
        if (customerAddedLabel != null){ customerAddedLabel.setText("Zákazník pridaný"); }

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
        if (newAccountAddedLabel != null) { newAccountAddedLabel.setText("Účet vytvorený"); }

        // AdminMainScene - Nový komponent tab
        NCot_tab.setText("Nový komponent");
        NCot_component_label.setText("Komponent :");
        NCot_carType_label.setText("Typ auta :");
        NCot_cost_label.setText("Cena :");
        NCot_amount_label.setText("Počet :");
        NCot_addComponent_btn.setText("Pridať komponent");
        if (errComponents != null) { errComponents.setText("Neplatné hodnoty!"); }
        if (newComponentAddedLabel != null) { newComponentAddedLabel.setText("Komponent pridaný"); }

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
        if (newRewardAddedLabel != null) { newRewardAddedLabel.setText("Odmena pridaná"); }
        if (newPayoutAddedLabel != null) { newPayoutAddedLabel.setText("Výplata pridaná"); }
        if (payoutUpdatedLabel != null) { payoutUpdatedLabel.setText("Výplata aktualizovaná"); }
        if (payoutDeletedLabel != null) { payoutDeletedLabel.setText("Výplata zmazaná"); }


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
        OORt_filter_btn.setText("Filtrovať");
        OORt_showDetails_btn.setText("Zobraziť detaily");
        if (OORt_selectArepair_label != null) { OORt_selectArepair_label.setText("Vyberte opravu!"); }

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
        custSel_filterBy_label.setText("Filtrovať podľa:");
        custSel_results_label.setText("Výsledky:");
        custSel_name_label.setText("Meno :");
        custSel_surname_label.setText("Priezvisko :");
        custSel_filter_btn.setText("Filtrovať");
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
        carSel_type_label.setText("Brand :");
        carSel_filter_btn.setText("Filter");
        carSel_select_btn.setText("Select");
        global_lang = "eng";
    }

    @FXML
    private void carSel_changeToSlovakLang() {
        typeColCar.setText("Značka");
        carSel_title_label.setText("Výber auta");
        carSel_type_label.setText("Značka :");
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
        if (carAddedMess != null) { carAddedMess.setText("New car added"); }
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
        if (carAddedMess != null) { carAddedMess.setText("Nové auto pridané"); }
        global_lang = "svk";
    }
}
