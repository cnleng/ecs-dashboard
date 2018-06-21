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
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author nlengc
 *
 */
@SpringBootApplication
@EnableAsync
@ComponentScan(basePackages = { "com.emc.ecs.metadata.*" })
public class Application {
	private static final Logger LOG = Logger.getLogger(Application.class);
	private static final String THREAD_NAME_PREFIX = "ECS-Executor-";
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

    @Bean(name = "ecsPoolTaskExecutor")
    public TaskExecutor ecsPoolTaskExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors());
        executor.setQueueCapacity(Runtime.getRuntime().availableProcessors());
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        executor.initialize();
        return executor;
    }
	
}
