import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.TimeoutException
import scala.concurrent.duration.Duration
import org.junit.runner.RunWith
import org.mongodb.scala.ScalaObservable
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import core.database.DatabaseComponent
import models.User
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers.GET
import play.api.test.Helpers.NOT_FOUND
import play.api.test.Helpers.OK
import play.api.test.Helpers.contentAsString
import play.api.test.Helpers.contentType
import play.api.test.Helpers.defaultAwaitTimeout
import play.api.test.Helpers.route
import play.api.test.Helpers.status
import play.api.test.Helpers.writeableOf_AnyContentAsEmpty
import play.api.test.WithApplication
import core.database.JsonValueImplicits._
import play.api.libs.json.JsValue
import play.api.test.WithApplication

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification with DatabaseComponent {

  "Application" should {

    "Be able to create a user" in new WithApplication {
      Await.ready(dropAllCollections(), Duration(10, TimeUnit.SECONDS))
      
      val resultObservable = User.create(User("TEST_USER", "TEST_PASSWORD"))
      Await.ready(resultObservable, Duration(10, TimeUnit.SECONDS))
      
      val observable = db.getCollection("User").find(and(equal("username", "TEST_USER"), equal("password", "TEST_PASSWORD")))
      
      val result: Document = Await.result(observable.head(), Duration(10, TimeUnit.SECONDS))
      
      val user = Json.fromJson[User](result).get
      
      user.username must equalTo("TEST_USER")
      user.password must equalTo("TEST_PASSWORD")
    }

    "send 404 on a bad request" in new WithApplication {
      route(FakeRequest(GET, "/boum")) must beSome.which(status(_) == NOT_FOUND)
    }

    "render the index page" in new WithApplication {
      val home = route(FakeRequest(GET, "/")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain("Your new application is ready.")
    }
  }
}
