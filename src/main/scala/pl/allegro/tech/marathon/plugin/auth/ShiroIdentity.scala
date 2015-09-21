package pl.allegro.tech.marathon.plugin.auth

import mesosphere.marathon.plugin.auth.Identity
import org.apache.shiro.subject.Subject

class ShiroIdentity(val username: String, val subject: Subject) extends Identity {

}
