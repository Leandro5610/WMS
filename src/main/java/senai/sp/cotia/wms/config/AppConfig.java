package senai.sp.cotia.wms.config;

import javax.sql.DataSource;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;




@Configuration
public class AppConfig {
	
	/*
	@Autowired
	private AppInterceptor interceptor;
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}
	*/
	
	// Configura a conexão da aplicacão ao Banco de Dados MySql

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://root:poHDtLraSvABYbMuQZGEZIepNNUQOwIz@junction.proxy.rlwy.net:50002/railway");
		dataSource.setUsername("root");
		dataSource.setPassword("poHDtLraSvABYbMuQZGEZIepNNUQOwIz");
		return dataSource;
	}

	// Configura o hibernate (ORM - Mapeamento Objeto Relacional)

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabase(Database.MYSQL);
		adapter.setDatabasePlatform("org.hibernate.dialect.MySQL8Dialect");
		adapter.setShowSql(true);
		adapter.setPrepareConnection(true);
		adapter.setGenerateDdl(true);
		return adapter;
	
	}
	
	/*
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(interceptor);
	}
	*/

}
