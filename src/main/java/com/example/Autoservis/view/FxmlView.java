package com.example.Autoservis.view;

public enum FxmlView {

    Login {
        @Override
        public String getFxmlFile() {
            return "/LoginScene.fxml";
        }
    },
    LoginSVK {
        @Override
        public String getFxmlFile() { return "/LoginSceneSVK.fxml"; }
    },
    CarScene {
        @Override
        public String getFxmlFile() {
            return "/Car_scene.fxml";
        }
    },
    CarSelection {
        @Override
        public String getFxmlFile() {
            return "/CarSelection.fxml";
        }
    },
    ComponentSelection {
        @Override
        public String getFxmlFile() {
            return "/ComponentSelection.fxml";
        }
    },
    MechanicSelectionScene{
        @Override
        public String getFxmlFile() {
            return "/MechanicSelection_scene.fxml";
        }
    },
    CustomerSelection {
        @Override
        public String getFxmlFile() {
            return "/CustomerSelection.fxml";
        }
    },
    RepairScene {
        @Override
        public String getFxmlFile() {
            return "/RepairScene.fxml";
        }
    },
    MechanicSelectionPayoutScene{
        @Override
        public String getFxmlFile() {
            return "/MechanicSelectionPayoutScene.fxml";
        }
    },
    AdminScene {
        @Override
        public String getFxmlFile() {
            return "/AdminMainScene.fxml";
        }
    },
    MechanicScene {
        @Override
        public String getFxmlFile() {
            return "/MechanicMainScene.fxml";
        }
    },
    MechanicSceneSVK {
        @Override
        public String getFxmlFile() { return "/MechanicMainSceneSVK.fxml"; }
    };

    public abstract String getFxmlFile();
}
