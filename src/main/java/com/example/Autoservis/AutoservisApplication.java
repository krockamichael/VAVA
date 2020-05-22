package com.example.Autoservis;

import com.example.Autoservis.config.StageManager;
import com.example.Autoservis.view.FxmlView;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AutoservisApplication extends Application {
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
		stageManager.switchScene(FxmlView.LOGIN);
	}

	private ConfigurableApplicationContext springBootApplicationContext() {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(AutoservisApplication.class);
		String[] args = getParameters().getRaw().toArray(new String[0]);
		return builder.run(args);
	}
}