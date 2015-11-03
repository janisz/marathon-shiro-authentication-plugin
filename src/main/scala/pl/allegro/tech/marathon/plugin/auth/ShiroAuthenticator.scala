package pl.allegro.tech.marathon.plugin.auth

import java.nio.charset.Charset

import mesosphere.marathon.plugin.auth.{Authenticator, Identity}
import mesosphere.marathon.plugin.http.{HttpRequest, HttpResponse}
import org.apache.shiro.authc.{AuthenticationException, UsernamePasswordToken}
import org.apache.shiro.codec.Base64
import org.apache.shiro.config.IniSecurityManagerFactory
import org.apache.shiro.subject.Subject

import scala.concurrent.{ExecutionContext, Future}

class ShiroAuthenticator extends Authenticator {

  import ExecutionContext.Implicits.global

  private val BasicLength: Int = "Basic".length()
  private val Unauthorized: Int = 401

  private val Authorization = "Authorization"

  private val ldapFactory = new IniSecurityManagerFactory("shiro.ini")
  private val securityManager = ldapFactory.getInstance
  private val subject = new Subject.Builder(securityManager).buildSubject()

  private class Credentials(val username: String, val password: String)

  override def authenticate(request: HttpRequest): Future[Option[Identity]] = {
    val authCredentials = request.header(Authorization)
    val credentials = extractUsernameAndPassword(authCredentials)

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

  private def extractUsernameAndPassword(authCredentials: Seq[String]) = {
    authCredentials.headOption.map(_.substring(BasicLength).trim())
    .map(Base64.decode)
    .map(new String(_, Charset.forName("UTF-8")).split(":", 2))
    .map(s => new Credentials(s(0), s(1)))
  }
}

