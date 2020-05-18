package ave.bertrand.apelschoolsupplies;

import java.awt.EventQueue;
import java.util.Arrays;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import ave.bertrand.apelschoolsupplies.ui.ApelManagerJFrame;

@SpringBootApplication
@EnableJpaRepositories("ave.bertrand.apelschoolsupplies.dao")
@EntityScan("ave.bertrand.apelschoolsupplies.model")
@ComponentScan(basePackages = "ave.bertrand.apelschoolsupplies")
public class Application {

	/**
	 * Context Spring Boot.
	 */
	private static ConfigurableApplicationContext springContext;

	public static void main(String[] args) {

		springContext = new SpringApplicationBuilder(Application.class).headless(false).run(args);

		EventQueue.invokeLater(() -> {
			ApelManagerJFrame ex = getSpringContext().getBean(ApelManagerJFrame.class);
			ex.setVisible(true);

			String[] beanNames = getSpringContext().getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				System.out.println(beanName);
			}
		});
	}

	public static ConfigurableApplicationContext getSpringContext() {
		return springContext;
	}

}
