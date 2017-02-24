package com.cts.coabcm.config;

import java.util.Properties;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.javaetmoi.core.spring.vfs.Vfs2PersistenceUnitManager;
public class AppConfig_bak {

	@ComponentScan(basePackages ={ "com.cts.coabcm" }, excludeFilters = { @Filter(type = FilterType.ANNOTATION, value = Configuration.class)})
	@Configuration
	@EnableTransactionManagement
	//@EnableJpaRepositories(basePackages="com.cts.coabcm.dao")
	@PropertySource(value={"classpath:application.properties"})
	public class AppConfig {

	    private static final String PROPERTY_NAME_DATABASE_DRIVER = "jdbc.driverClassName";
	    private static final String PROPERTY_NAME_DATABASE_PASSWORD = "jdbc.password";
	    private static final String PROPERTY_NAME_DATABASE_URL = "jdbc.url";
	    private static final String PROPERTY_NAME_DATABASE_USERNAME = "jdbc.username";
	 
	    private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
	    private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
	    private static final String PROPERTY_NAME_HIBERNATE_HBM2DDL = "hibernate.hbm2ddl.auto";
	    private static final String PROPERTY_NAME_HIBERNATE_RESOURCE_SCANNER = "hibernate.ejb.resource_scanner";
	    private static final String PROPERTY_NAME_HIBERNATE_METADATA = "hibernate.temp.use_jdbc_metadata_defaults";
	    private static final String PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN = "entitymanager.packages.to.scan";
	    
	    
	    @Autowired
	    private Environment env;
	    
	    @Autowired
	    private ApplicationContext applicationContext;
	    
	    @Bean
	    @Autowired
	    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
	        JpaTransactionManager txManager = new JpaTransactionManager();
	        txManager.setEntityManagerFactory(emf);
	        return txManager;
	    }
	 
	    @Bean
	    public DataSource dataSource() {
	    	JndiTemplate jndiTemplate = new JndiTemplate();
	    	DataSource dataSource = null;
			try {
				dataSource = (DataSource) jndiTemplate.lookup("java:/jdbc/COAbcmDS");
			} catch (NamingException e) {
				e.printStackTrace();
			}
	/*        DriverManagerDataSource dataSource = new DriverManagerDataSource();
	        dataSource.setDriverClassName(env.getRequiredProperty(PROPERTY_NAME_DATABASE_DRIVER));
	        dataSource.setUrl(env.getRequiredProperty(PROPERTY_NAME_DATABASE_URL));
	        dataSource.setUsername(env.getRequiredProperty(PROPERTY_NAME_DATABASE_USERNAME));
	        dataSource.setPassword(env.getRequiredProperty(PROPERTY_NAME_DATABASE_PASSWORD));*/
	 
	        return dataSource;
	    }
	    
	    @Bean
	    public JpaVendorAdapter jpaVendorAdapter() {
	        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
	        return hibernateJpaVendorAdapter;
	    }
	 
	    @Bean
	    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
	        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
	        entityManagerFactoryBean.setDataSource(dataSource());
	       // entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
	        entityManagerFactoryBean.setPackagesToScan(env.getRequiredProperty(PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN));
	        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter()); 
	        entityManagerFactoryBean.setPersistenceUnitManager(persistenceUnitManager());
	        entityManagerFactoryBean.setJpaProperties(hibProperties());
	         
	        return entityManagerFactoryBean;
	    }
	 
	    private Properties hibProperties() {
	        Properties properties = new Properties();
	       // properties.put(PROPERTY_NAME_HIBERNATE_DIALECT, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_DIALECT));
	        properties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_SHOW_SQL));
	        properties.put(PROPERTY_NAME_HIBERNATE_HBM2DDL, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_HBM2DDL));
	      //  properties.put(PROPERTY_NAME_HIBERNATE_RESOURCE_SCANNER, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_RESOURCE_SCANNER));
	     //   properties.put(PROPERTY_NAME_HIBERNATE_METADATA, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_METADATA));
	        return properties;
	    }
	    
	    @Bean
	    public Vfs2PersistenceUnitManager persistenceUnitManager() {
	        Vfs2PersistenceUnitManager pum = new Vfs2PersistenceUnitManager(applicationContext);
	        pum.setDefaultDataSource(dataSource());
	        pum.setDefaultPersistenceUnitName("myPersitenceUnit");
	        pum.setPackagesToScan(env.getRequiredProperty(PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN));
	        return pum;
	    } 
	 
	}

}
