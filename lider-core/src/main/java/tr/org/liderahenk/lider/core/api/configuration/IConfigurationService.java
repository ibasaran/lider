package tr.org.liderahenk.lider.core.api.configuration;

import java.util.Map;

import tr.org.liderahenk.lider.core.api.messaging.enums.Protocol;

/**
 * 
 * Provides configuration service for all Lider core and plugin bundles.
 *
 * @author <a href="mailto:birkan.duman@gmail.com">Birkan Duman</a>
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 * 
 */
public interface IConfigurationService {

	//
	// LDAP configuration
	//

	/**
	 * 
	 * @return LDAP server host.
	 */
	String getLdapServer();

	/**
	 * 
	 * @return LDAP server port number.
	 */
	String getLdapPort();

	/**
	 * 
	 * @return LDAP user name.
	 */
	String getLdapUsername();

	/**
	 * 
	 * @return LDAP password.
	 */
	String getLdapPassword();

	/**
	 * 
	 * @return LDAP root DN.
	 */
	String getLdapRootDn();

	/**
	 * 
	 * @return true if using LDAPS, false otherwise.
	 */
	Boolean getLdapUseSsl();

	/**
	 * 
	 * @return LDAP search attributes that can be used in new search editor.
	 */
	String getLdapSearchAttributes();

	//
	// XMPP configuration
	//

	/**
	 * @return XMPP server host.
	 */
	String getXmppHost();

	/**
	 * 
	 * @return XMPP server port number.
	 */
	Integer getXmppPort();

	/**
	 * @return XMPP user name
	 */
	String getXmppUsername();

	/**
	 * 
	 * @return XMPP password.
	 */
	String getXmppPassword();

	/**
	 * @return XMPP service name / domain.
	 */
	String getXmppServiceName();

	/**
	 * @return max retry connection count.
	 */
	int getXmppMaxRetryConnectionCount();

	/**
	 * @return packet replay timeout in milliseconds.
	 */
	int getXmppPacketReplayTimeout();

	/**
	 * 
	 * @return default timeout (in milliseconds) for XMPP ping requests.
	 */
	Integer getXmppPingTimeout();

	/**
	 * 
	 * @return true if XMPP uses SSL, false otherwise.
	 */
	Boolean getXmppUseSsl();

	/**
	 * 
	 * @return File directory path that is used to store received files
	 *         temporarily.
	 */
	String getXmppFilePath();

	//
	// Agent configuration
	//

	/**
	 * 
	 * @return LDAP base DN for agent entries (might be empty or null).
	 */
	String getAgentLdapBaseDn();

	/**
	 * 
	 * @return LDAP agent id attribute for agent entries. Default value is 'cn'.
	 */
	String getAgentLdapIdAttribute();

	/**
	 * 
	 * @return LDAP agent JID attribute for agent entries. Default values is
	 *         'uid'.
	 */
	String getAgentLdapJidAttribute();

	/**
	 * 
	 * @return LDAP agent object classes.
	 */
	String getAgentLdapObjectClasses();

	//
	// User configuration
	//

	/**
	 * 
	 * @return LDAP user search base dn for authentication
	 */
	String getUserLdapBaseDn();

	/**
	 * 
	 * @return LDAP user attribute for authentication
	 */
	String getUserLdapUidAttribute();

	/**
	 * 
	 * @return LDAP user privilege attribute.
	 */
	String getUserLdapPrivilegeAttribute();

	/**
	 * 
	 * @return comma separated LDAP user object classes to be used in search
	 *         filter for authentication
	 * 
	 */
	String getUserLdapObjectClasses();

	/**
	 * 
	 * @return true if LDAP authorization enabled, false otherwise
	 */
	Boolean getUserAuthorizationEnabled();

	/**
	 * 
	 * @return comma separated LDAP group object classes (which is usually just
	 *         'groupOfNames')
	 */
	String getGroupLdapObjectClasses();

	//
	// Task manager configuration
	//

	/**
	 * 
	 * @return default task timeout (in milliseconds) for task manager
	 */
	Long getTaskManagerTaskTimeout();

	/**
	 * 
	 * @return true if clustering enabled in task manager store
	 */
	Boolean getTaskManagerMulticastEnabled();

	/**
	 * 
	 * @return true if xmpp message logs enabled
	 */
	Boolean getTaskManagerLogXmppMessagesEnabled();

	//
	// Mail configuration
	//

	/**
	 * 
	 * @return
	 */
	String getMailAddress();

	/**
	 * 
	 * @return
	 */
	String getMailPassword();

	/**
	 * 
	 * @return
	 */
	String getMailHost();

	/**
	 * 
	 * @return
	 */
	Integer getMailSmtpPort();

	/**
	 * 
	 * @return
	 */
	Boolean getMailSmtpAuth();

	/**
	 * 
	 * @return
	 */
	Boolean getMailSmtpStartTlsEnable();

	/**
	 * 
	 * @return
	 */
	Boolean getMailSmtpSslEnable();

	/**
	 * 
	 * @return
	 */
	Integer getMailSmtpConnTimeout();

	/**
	 * 
	 * @return
	 */
	Integer getMailSmtpTimeout();

	/**
	 * 
	 * @return
	 */
	Integer getMailSmtpWriteTimeout();

	//
	// Hot deployment & plugin distribution configuration
	//

	Protocol getAgentPluginDistroProtocol();

	String getAgentPluginDistroHost();

	String getAgentPluginDistroUsername();

	String getAgentPluginDistroPassword();

	String getAgentPluginDistroPath();

	String getAgentPluginDistroUrl();

	Map<String, Object> getAgentPluginDistoParams();

	/**
	 * 
	 * @return hot deployment path which is used to hold plugin files.
	 */
	String getHotDeploymentPath();

}
