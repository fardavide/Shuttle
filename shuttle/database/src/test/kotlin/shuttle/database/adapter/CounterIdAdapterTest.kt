package shuttle.database.adapter

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import shuttle.database.model.DatabaseCounterId
import shuttle.database.model.DatabaseGeoHash
import shuttle.database.model.DatabaseHour

class CounterIdAdapterTest : BehaviorSpec({

    Given("a CounterIdAdapter") {
        val adapter = CounterIdAdapter()

        When("it is used to encode a DatabaseCounterId.All") {
            val encoded = adapter.encode(DatabaseCounterId.All)

            Then("the encoded value should be a string") {
                encoded shouldBe "all"
            }
        }

        When("it is used to encode a DatabaseCounterIf.ForTime") {
            val encoded = adapter.encode(DatabaseCounterId.ForTime(DatabaseHour(1)))

            Then("the encoded value should be a string") {
                encoded shouldBe "time:1"
            }
        }

        When("it is used to encode a DatabaseCounterId.ForLocation") {
            val encoded = adapter.encode(DatabaseCounterId.ForLocation(DatabaseGeoHash("abc")))

            Then("the encoded value should be a string") {
                encoded shouldBe "location:abc"
            }
        }

        When("it is used to encode a DatabaseCounterId.ForTimeAndLocation") {
            val encoded = adapter.encode(
                DatabaseCounterId.ForTimeAndLocation(
                    DatabaseHour(1),
                    DatabaseGeoHash("abc")
                )
            )

            Then("the encoded value should be a string") {
                encoded shouldBe "time:1_location:abc"
            }
        }

        When("it is used to decode a string") {
            val decoded = adapter.decode("all")

            Then("the decoded value should be a DatabaseCounterId.All") {
                decoded shouldBe DatabaseCounterId.All
            }
        }

        When("it is used to decode a string") {
            val decoded = adapter.decode("time:1")

            Then("the decoded value should be a DatabaseCounterId.ForTime") {
                decoded shouldBe DatabaseCounterId.ForTime(DatabaseHour(1))
            }
        }

        When("it is used to decode a string") {
            val decoded = adapter.decode("location:abc")

            Then("the decoded value should be a DatabaseCounterId.ForLocation") {
                decoded shouldBe DatabaseCounterId.ForLocation(DatabaseGeoHash("abc"))
            }
        }

        When("it is used to decode a string") {
            val decoded = adapter.decode("time:1_location:abc")

            Then("the decoded value should be a DatabaseCounterId.ForTimeAndLocation") {
                decoded shouldBe DatabaseCounterId.ForTimeAndLocation(
                    DatabaseHour(1),
                    DatabaseGeoHash("abc")
                )
            }
        }
    }
})
