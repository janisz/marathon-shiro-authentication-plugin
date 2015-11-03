package pl.allegro.tech.marathon.plugin.auth

import mesosphere.marathon.plugin.PathId
import mesosphere.marathon.plugin.auth.{AuthorizedAction, Identity, Authorizer}
import mesosphere.marathon.plugin.http.{HttpResponse, HttpRequest}
import collection.JavaConversions.asJavaCollection

class ShiroAuthorizer extends Authorizer {

  private val Unauthorized: Int = 403

  override def isAuthorized[Resource](principal: Identity, action: AuthorizedAction[Resource], resource: Resource): Boolean = {
    val subject = principal.asInstanceOf[ShiroIdentity].subject
    val pathId = resource.asInstanceOf[PathId]
    subject.hasAllRoles(pathId.path)
  }

  override def handleNotAuthorized(principal: Identity, request: HttpRequest, response: HttpResponse): Unit = {
    val username = principal.asInstanceOf[ShiroIdentity].username
    val responseBody = s"$username is not permitted to perform the requested operation"
    response.body("plain/text", responseBody.toCharArray.map(_.toByte))
    response.status(Unauthorized)
  }
}
