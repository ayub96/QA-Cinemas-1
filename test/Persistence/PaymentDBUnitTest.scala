package Persistence

import Persistence.DAO.PaymentDAO
import Persistence.Domain.paymentObj.Payment
import Schema.Schemas.{createDrop, insertData}
import akka.actor.Status.Success
import org.scalatest.BeforeAndAfter
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.specs2.mock.Mockito.mock
import slick.jdbc.MySQLProfile.backend.Database
import slick.jdbc.H2Profile.api._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.io.Source

class PaymentDBUnitTest extends AnyFlatSpec with BeforeAndAfter with Matchers{

  lazy val db = Database.forConfig("mysqlDB")

  behavior of "Payment table"

  before {
    val futureFuncs: Future[_] = {
      val funcs: DBIO[Unit] = DBIO.seq(
        createDrop,
        insertData
      )
      db.run(funcs)
    }
    Await.result(futureFuncs, Duration.Inf)
  }

  it should "create a payment when when create is called" in {
    val pay: Payment = new Payment(1,"john",1243131, "12/20", 123, 1)
    val result = Await.result(PaymentDAO.create(pay), Duration.Inf)
    assert(result === "payment details submitted successfully")
  }
}
