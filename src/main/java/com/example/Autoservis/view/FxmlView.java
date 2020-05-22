package com.example.Autoservis.view;

public enum FxmlView {

    LOGIN {
        @Override
        public String getFxmlFile() {
            return "/LoginScene.fxml";
        }
    },
    LOGIN_SVK {
        @Override
        public String getFxmlFile() { return "/LoginSceneSVK.fxml"; }
    },
    CAR_SCENE {
        @Override
        public String getFxmlFile() {
            return "/CarScene.fxml";
        }
    },
    CAR_SELECTION {
        @Override
        public String getFxmlFile() {
            return "/CarSelection.fxml";
        }
    },
    COMPONENT_SELECTION {
        @Override
        public String getFxmlFile() {
            return "/ComponentSelection.fxml";
        }
    },
    MECHANIC_SELECTION_SCENE {
        @Override
        public String getFxmlFile() {
            return "/MechanicSelectionScene.fxml";
        }
    },
    CUSTOMER_SELECTION {
        @Override
        public String getFxmlFile() {
            return "/CustomerSelection.fxml";
        }
    },
    REPAIR_SCENE {
        @Override
        public String getFxmlFile() {
            return "/RepairScene.fxml";
        }
    },
    MECHANIC_SELECTION_PAYOUT_SCENE {
        @Override
        public String getFxmlFile() {
            return "/MechanicSelectionPayoutScene.fxml";
        }
    },
    ADMIN_SCENE {
        @Override
        public String getFxmlFile() {
            return "/AdminMainScene.fxml";
        }
    },
    MECHANIC_SCENE {
        @Override
        public String getFxmlFile() {
            return "/MechanicMainScene.fxml";
        }
    },
    MECHANIC_SCENE_SVK {
        @Override
        public String getFxmlFile() { return "/MechanicMainSceneSVK.fxml"; }
    };

    public abstract String getFxmlFile();
}
