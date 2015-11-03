package tech.allegro.marathon.plugin.auth

import org.scalatest.FunSpec
import org.scalatest.Matchers
import org.scalatest.GivenWhenThen

class BasicAuthCredentialsExtractorTest extends FunSpec with Matchers with GivenWhenThen {

  var extractor: BasicAuthCredentialsExtractor = _

  describe("A BasicAuthCredentialsExtractor") {

    it("should extract base64 credentials") {
      Given("An extractor")
      extractor = new BasicAuthCredentialsExtractor
      When("BasicAuth credentials are given")
      val actual = extractor.extractCredentials(
        Seq("Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==")
      )
      Then("Credentials are properly decoded")
      actual should be(Some(new Credentials("Aladdin", "open sesame")))
    }

    it("should return none when no credentials were given") {
      Given("An extractor")
      extractor = new BasicAuthCredentialsExtractor
      When("BasicAuth credentials are given")
      val actual = extractor.extractCredentials(
        Seq()
      )
      Then("Credentials are None")
      actual should be(None)
    }
  }
}
