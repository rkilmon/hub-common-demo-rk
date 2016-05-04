/*******************************************************************************
 * Black Duck Software Suite SDK
 * Copyright (C) 2016 Black Duck Software, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 *******************************************************************************/
package com.blackducksoftware.integration.hub.builder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.lang3.StringUtils;

import com.blackducksoftware.integration.hub.global.HubCredentials;
import com.blackducksoftware.integration.hub.global.HubProxyInfo;
import com.blackducksoftware.integration.hub.global.HubServerConfig;

public class HubServerConfigBuilder extends AbstractBuilder {

	public static final String ERROR_MSG_URL_NOT_FOUND = "No Hub Url was found.";
	public static final String ERROR_MSG_URL_NOT_VALID_PREFIX = "This is not a valid URL : ";
	public static final String ERROR_MSG_UNREACHABLE_PREFIX = "Can not reach this server : ";
	public static final String ERROR_MSG_URL_NOT_VALID = "The Hub Url is not a valid URL.";

	public static int DEFAULT_TIMEOUT = 120;

	private String hubUrl;
	private int timeout;
	private HubCredentials credentials;
	private HubProxyInfo proxyInfo;

	@Override
	public ValidationResult<HubServerConfig> build() {
		final ValidationResult<HubServerConfig> result = assertValid();
		URL hubURL = null;
		try {
			hubURL = new URL(hubUrl);
		} catch (final MalformedURLException e) {
			e.printStackTrace();
		}
		new HubServerConfig(hubURL, timeout, credentials, proxyInfo);
		return result;
	}

	@Override
	public ValidationResult<HubServerConfig> assertValid() {
		final ValidationResult<HubServerConfig> result = null;

		validateHubUrl(result);

		validateTimeout(result, DEFAULT_TIMEOUT);

		// if (!valid) {
		// // throw new HubIntegrationException(
		// // "The server configuration is not valid - please check the log for
		// // the specific issues.");
		// }
		return result;
	}

	public boolean validateHubUrl(final ValidationResult<HubServerConfig> result) {
		boolean valid = true;
		if (hubUrl == null) {
			logger.error(ERROR_MSG_URL_NOT_FOUND);
			return false;
		}

		URL hubURL = null;
		try {
			hubURL = new URL(hubUrl);
			hubURL.toURI();
		} catch (final MalformedURLException e) {
			logger.error(ERROR_MSG_URL_NOT_VALID);
			valid = false;
		} catch (final URISyntaxException e) {
			logger.error(ERROR_MSG_URL_NOT_VALID);
			valid = false;
		}

		if (hubURL == null) {
			return valid;
		}

		try {
			URLConnection connection = null;
			if (null != proxyInfo) {
				connection = proxyInfo.openConnection(hubURL);
			} else {
				connection = hubURL.openConnection();
			}
			connection.getContent();
		} catch (final IOException ioe) {
			logger.error(ERROR_MSG_UNREACHABLE_PREFIX + hubUrl, ioe);
			valid = false;
		} catch (final RuntimeException e) {
			logger.error(ERROR_MSG_URL_NOT_VALID_PREFIX + hubUrl, e);
			valid = false;
		}

		return valid;
	}

	public boolean validateTimeout(final ValidationResult<HubServerConfig> result) {
		return validateTimeout(result, null);
	}

	private boolean validateTimeout(final ValidationResult<HubServerConfig> result, final Integer defaultTimeout) {
		boolean valid = true;
		if (defaultTimeout != null && timeout <= 0) {
			timeout = defaultTimeout;
		} else if (timeout <= 0) {
			logger.error("The Timeout must be greater than 0.");
			valid = false;
		}
		return valid;
	}

	public void setHubUrl(final String hubUrl) {
		this.hubUrl = StringUtils.trimToNull(hubUrl);
	}

	public void setTimeout(final String timeout, final Integer defaultPort) {
		setTimeout(stringToInteger(timeout, defaultPort));
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(final int timeout) {
		this.timeout = timeout;
	}

	public HubCredentials getCredentials() {
		return credentials;
	}

	public void setCredentials(final HubCredentials credentials) {
		this.credentials = credentials;
	}

	public HubProxyInfo getProxyInfo() {
		return proxyInfo;
	}

	public void setProxyInfo(final HubProxyInfo proxyInfo) {
		this.proxyInfo = proxyInfo;
	}

	public String getHubUrl() {
		return hubUrl;
	}

}
