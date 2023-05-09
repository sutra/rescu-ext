package org.oxerr.rescu.ext.proxy.auth;

import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import si.mazi.rescu.RestProxyFactory;

public class ProxyAuthenticationLoader {

	private static final String RESCU_PROPERTIES = "/rescu.properties";

	private final Logger log = LoggerFactory.getLogger(ProxyAuthenticationLoader.class);

	public void loadProxyAuthentication() {
		Properties properties = new Properties();

		try (InputStream propsStream = RestProxyFactory.class.getResourceAsStream(RESCU_PROPERTIES)) {
			if (propsStream != null) {
				properties.load(propsStream);
				log.debug("Loaded properties from {}.", RESCU_PROPERTIES);
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("Error reading " + RESCU_PROPERTIES, e);
		}

		String proxyHost = properties.getProperty("rescu.http.readProxyHost");
		String proxyPort = properties.getProperty("rescu.http.readProxyPort");

		String userName = properties.getProperty("rescu.http.readProxyUserName");
		String password = properties.getProperty("rescu.http.readProxyPassword");

		if (
			StringUtils.isNotEmpty(proxyHost)
			&& StringUtils.isNotEmpty(proxyPort)
			&& StringUtils.isNotEmpty(userName)
			&& StringUtils.isNotEmpty(password)
		) {
			this.setPasswordAuthentication(userName, password.toCharArray());
		}
	}

	private void setPasswordAuthentication(String userName, char[] password) {
		// https://bugs.openjdk.org/browse/JDK-8168839
		log.info("Setting property jdk.http.auth.tunneling.disabledSchemes to empty.");
		System.setProperty("jdk.http.auth.tunneling.disabledSchemes", StringUtils.EMPTY);

		final PasswordAuthentication auth = new PasswordAuthentication(userName, password);

		Authenticator authenticator = new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return auth;
			}

		};

		Authenticator.setDefault(authenticator);
	}

}
