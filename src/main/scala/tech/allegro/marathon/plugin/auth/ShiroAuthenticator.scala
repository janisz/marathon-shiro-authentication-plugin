package tech.allegro.marathon.plugin.auth

import mesosphere.marathon.plugin.auth.{Authenticator, Identity}
import mesosphere.marathon.plugin.http.{HttpRequest, HttpResponse}
import mesosphere.marathon.plugin.plugin.PluginConfiguration
import org.apache.shiro.authc.{AuthenticationException, UsernamePasswordToken}
import org.apache.shiro.config.IniSecurityManagerFactory
import org.apache.shiro.mgt.SecurityManager
import org.apache.shiro.subject.Subject
import play.api.libs.json.JsObject

import scala.concurrent.{ExecutionContext, Future}

class ShiroAuthenticator extends Authenticator with PluginConfiguration {

  import ExecutionContext.Implicits.global

  private val Unauthorized: Int = 401

  private val Authorization = "Authorization"

  private val DefaultShiroConfigPath = "/etc/marathon/shiro.ini"

  private val CredentialExtractor = new BasicAuthCredentialsExtractor()

  private var securityManager: SecurityManager = _

  private var subject: Subject = _

  override def initialize(configuration: JsObject): Unit = {
    val shiroConfigurationPath = (configuration \ "path").asOpt[String].getOrElse(DefaultShiroConfigPath)
    val ldapFactory = new IniSecurityManagerFactory(shiroConfigurationPath)
    securityManager = ldapFactory.getInstance
    subject = new Subject.Builder(securityManager).buildSubject()
  }

  override def authenticate(request: HttpRequest): Future[Option[Identity]] = {
    val authCredentials = request.header(Authorization)
    val credentials = CredentialExtractor.extractCredentials(authCredentials)

    Future {
      credentials.flatMap(login)
    }
  }

  override def handleNotAuthenticated(request: HttpRequest, response: HttpResponse): Unit = {
    response.header("WWW-Authenticate", """Basic realm="Login with your AD credentials"""")
    response.status(Unauthorized)
  }

  private def login(credentials: Credentials): Option[ShiroIdentity] = {

    val username = credentials.username
    val token = new UsernamePasswordToken(username, credentials.password)
    try {
      Some(new ShiroIdentity(credentials.username, securityManager.login(subject, token)))
    }
    catch {
      case _: AuthenticationException => None
    }
  }
}

