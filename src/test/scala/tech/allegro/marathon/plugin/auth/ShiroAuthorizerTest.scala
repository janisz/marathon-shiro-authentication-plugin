package tech.allegro.marathon.plugin.auth

import org.scalatest.FunSpec
import org.scalatest.Matchers
import org.scalatest.GivenWhenThen

class ShiroAuthorizerTest extends FunSpec with Matchers with GivenWhenThen {

  var authorizer: ShiroAuthorizer = _

  describe("A ShiroAuthorizer") {

    it("should authorize any authenticated user") {
      Given("New Authorizer")
      authorizer = new ShiroAuthorizer()
      When("Try to authorizer any user")
      val isAuthorized = authorizer.isAuthorized(null, null, null)
      Then("Always return true")
      isAuthorized should be(true)
    }

    it("should return 401 when user is not authorized") {
      Given("New Authorizer")
      authorizer = new ShiroAuthorizer()
      Given("Empty response")
      val response = new FakeHttpResponse()
      Given("Sample principal (without subject)")
      val principal = new ShiroIdentity("Joe", null)
      When("User is unauthorized")
      authorizer.handleNotAuthorized(principal, null, response)
      Then("Send unauthorized code in response")
      response.code should be(403)
    }
  }
}
