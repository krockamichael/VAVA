package com.example.Autoservis;

import com.example.Autoservis.config.StageManager;
import com.example.Autoservis.view.FxmlView;
import javafx.geometry.Insets;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AutoservisApplication extends Application {

	/*
	private ConfigurableApplicationContext context;
	Parent rootnode;

	public static void main(final String[] args){
		Application.launch(args);
	}

	@Override
	public void init() throws Exception {
		context = SpringApplication.run(AutoservisApplication.class);
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LoginScene.fxml"));
		fxmlLoader.setControllerFactory(context::getBean);
		rootnode = fxmlLoader.load();
	}

	@Override
	public void stop() throws Exception {
		context.stop();
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setScene(new Scene(rootnode, 300, 375));
		stage.show();
	}*/
	protected ConfigurableApplicationContext springContext;
	protected StageManager stageManager;

	public static void main(final String[] args) {
		Application.launch(args);
	}

	@Override
	public void init() {
		springContext = springBootApplicationContext();
	}

	@Override
	public void start(Stage stage) {
		stageManager = springContext.getBean(StageManager.class, stage);
		displayInitialScene();
	}

	@Override
	public void stop() {
		springContext.close();
	}

	protected void displayInitialScene() {
		stageManager.switchScene(FxmlView.Login);
	}


	private ConfigurableApplicationContext springBootApplicationContext() {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(AutoservisApplication.class);
		String[] args = getParameters().getRaw().toArray(new String[0]);
		return builder.run(args);
	}
}