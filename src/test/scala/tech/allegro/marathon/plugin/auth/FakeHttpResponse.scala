package tech.allegro.marathon.plugin.auth

import mesosphere.marathon.plugin.http.HttpResponse

class FakeHttpResponse(
                        var code: Int = 200,
                        var mediaType: String = "",
                        var bytes: Array[Byte] = new Array[Byte](0)
                      ) extends HttpResponse {
  override def header(header: String, value: String): Unit = ???

  override def body(mediaType: String, bytes: Array[Byte]): Unit = {
    this.mediaType = mediaType
    this.bytes = bytes
  }

  override def sendRedirect(url: String): Unit = ???

  override def cookie(name: String, value: String, maxAge: Int, secure: Boolean): Unit = ???

  override def status(code: Int): Unit = this.code = code
}
