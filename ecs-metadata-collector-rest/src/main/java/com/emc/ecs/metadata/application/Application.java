/**
 * 
 */
package com.emc.ecs.metadata.application;

import org.apache.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author nlengc
 *
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
@ComponentScan(basePackages = { "com.emc.ecs.metadata.*" })
public class Application {
	private static final Logger LOG = Logger.getLogger(Application.class);
	private static final int CORE_POOL_SIZE = 5;
	private static final int MAX_POOL_SIZE = 10;
	private static final int QUEUE_CAPACITY = 500;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			LOG.info("Initializing ECS Metadata Collector REST API ...");
		};
	}

//	@Bean
//	public static PropertySourcesPlaceholderConfigurer properties() {
//	  PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
//	  YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
//	  yaml.setResources(new FileSystemResource(CONFIGURATION_FILE));
//	  propertySourcesPlaceholderConfigurer.setProperties(yaml.getObject());
//	  return propertySourcesPlaceholderConfigurer;
//	}
	
}
