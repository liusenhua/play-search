package repositories.elasticsearch

import com.typesafe.config.ConfigFactory
import dto.{Account, JsonFormats}

import io.searchbox.core.Index
import io.searchbox.client.JestClientFactory
import io.searchbox.client.JestResult
import io.searchbox.client.config.HttpClientConfig
import play.Logger
import play.api.libs.json.Json

import repositories.{RepoResponse, IndexRepoComponent}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future, Await}

/**
 * Created by andy on 2/14/16.
 */
trait JestRepoComponent extends IndexRepoComponent {

  val indexerRepo: IndexerRepo = new JestRepo

  class JestRepo extends IndexerRepo with JsonFormats {
    val indexName = "bank"
    var indexType = "account"

    def getAccount(accountId: String): RepoResponse[Account] = {
      val json: String = """{"account_number":990,"balance":44456,"firstname":"Kelly","lastname":"Steele","age":35,"gender":"M","address":"809 Hoyt Street","employer":"Eschoir","email":"kellysteele@eschoir.com","city":"Stewartville","state":"ID"}"""
      val account: Account = Json.parse(json).as[Account]
      Future.successful(Right(account))
    }

    def searchAccount(): RepoResponse[List[Account]] = {
      val json: String = """{"account_number":990,"balance":44456,"firstname":"Kelly","lastname":"Steele","age":35,"gender":"M","address":"809 Hoyt Street","employer":"Eschoir","email":"kellysteele@eschoir.com","city":"Stewartville","state":"ID"}"""
      val account: Account = Json.parse(json).as[Account]
      Future.successful(Right(List[Account](account)))
    }

    def indexAccount(account: Account): RepoResponse[String] = {
      try {
        Logger.info("========================> indexAccount : " + account.accountId)

        val json: String = Json.stringify(Json.toJson(account))
        val id: String = account.accountId.toString()
        val result: JestResult = JestRepo.jestClient.execute(new Index.Builder(json).index(indexName).`type`(indexType).id(id).build())
        Logger.info("index result for id " + account.accountId + " : " + result.getJsonString())

        Logger.info("<======================== indexAccount : " + account.accountId)

        Future.successful(Right(account.accountId.toString()))
      } catch {
        case ex: Exception =>
          Logger.error("error indexing account ", ex)
          Future.successful(Left(ex.getMessage))
      }
    }
  }

  object JestRepo {
    val defaultWait = 100 seconds
    lazy val conf = ConfigFactory.load().getObject("elasticSearch")
    lazy val esHost = conf.get("host").unwrapped().asInstanceOf[String]
    lazy val esPort = conf.get("port").unwrapped().asInstanceOf[String].toInt
    lazy val clusterName = conf.get("clusterName").unwrapped().asInstanceOf[String]

    val config = new HttpClientConfig
                    .Builder("http://%s:%s".format(esHost,"9200"))
                    .readTimeout(defaultWait.toMillis.toInt)
                    .multiThreaded(true).build()

    val factory = new JestClientFactory
    factory.setHttpClientConfig(config)

    val jestClient  = factory.getObject()
  }
}
