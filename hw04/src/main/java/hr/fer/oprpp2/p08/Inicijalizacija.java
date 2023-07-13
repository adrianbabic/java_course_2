package hr.fer.oprpp2.p08;

import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;


@WebListener
public class Inicijalizacija implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		String fileName = sce.getServletContext().getRealPath("/WEB-INF/dbsettings.properties");

		if (fileName == null)
			throw new RuntimeException("[getRealPath] Missing dbsettings.properties file.");
		Properties properties = new Properties();

		String host;
		String port;
		String dbName;
		String user;
		String password;
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

			properties.load(br);

			host = properties.getProperty("host");
			port = properties.getProperty("port");
			dbName = properties.getProperty("name");
			user = properties.getProperty("user");
			password = properties.getProperty("password");

		} catch (IOException e) {
			throw new RuntimeException("Error reading dbsettings.properties file.", e);
		}

		if (host == null || port == null || dbName == null || user == null || password == null) {
			throw new RuntimeException("Missing required properties in dbsettings.properties file. Required"
					+ " properties are: host, port, name, user, password.");
		}

//		String dbName="baza1DB";
//		String connectionURL = "jdbc:derby://localhost:1527/" + dbName + ";user=perica;password=pero";
		String connectionURL = "jdbc:derby://" + host + ":" + port + "/" + dbName + ";user=" + user + ";password="
				+ password;

		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
		} catch (PropertyVetoException e1) {
			throw new RuntimeException("Pogreška prilikom inicijalizacije poola.", e1);
		}
		cpds.setJdbcUrl(connectionURL);

		Connection connection = null;
		try {
			connection = cpds.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException("Baza podataka nije dostupna.", e);
		}

		TableManipulation.initializeTables(connection);

		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource) sce.getServletContext()
				.getAttribute("hr.fer.zemris.dbpool");
		if (cpds != null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}