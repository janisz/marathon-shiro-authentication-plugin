package tech.allegro.marathon.plugin.auth

import java.nio.charset.Charset

import org.apache.shiro.codec.Base64

class BasicAuthCredentialsExtractor {

  private val BasicLength = "Basic".length()

  def extractCredentials(authCredentials: Seq[String]) = {
    authCredentials.headOption.map(_.substring(BasicLength).trim())
      .map(Base64.decode)
      .map(new String(_, Charset.forName("UTF-8")).split(":", 2))
      .map(s => Credentials(s(0), s(1)))
  }
}
