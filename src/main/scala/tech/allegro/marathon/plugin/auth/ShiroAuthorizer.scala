package tech.allegro.marathon.plugin.auth

import mesosphere.marathon.plugin.auth.{AuthorizedAction, Authorizer, Identity}
import mesosphere.marathon.plugin.http.{HttpRequest, HttpResponse}

class ShiroAuthorizer extends Authorizer {


  private val Unauthorized: Int = 403

  override def isAuthorized[Resource](principal: Identity, action: AuthorizedAction[Resource], resource: Resource): Boolean = {
    //TODO(janisz): Think how handle application actions with shiro roles
    //    val subject = principal.asInstanceOf[ShiroIdentity].subject
    //    val pathId = resource.asInstanceOf[PathId]
    //    subject.hasAllRoles(pathId.path)
    true
  }

  override def handleNotAuthorized(principal: Identity, request: HttpRequest, response: HttpResponse): Unit = {
    val username = principal.asInstanceOf[ShiroIdentity].username
    val responseBody = s"$username is not permitted to perform the requested operation"
    response.body("plain/text", responseBody.toCharArray.map(_.toByte))
    response.status(Unauthorized)
  }
}