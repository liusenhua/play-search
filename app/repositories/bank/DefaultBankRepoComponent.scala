package repositories.bank

import dto.{DTO, JsonFormats, Account}
import repositories.couchbase.{BaseCouchbaseRepository, CouchbaseUtils}
import repositories._
import utils.Fetcher

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by andy on 2/8/16.
 */
trait DefaultBankRepoComponent extends BaseCouchbaseRepository with BankRepoComponent {
  this: Fetcher with IndexRepoComponent =>

  val bankRepo = new DefaultBankRepo

  val DESIGN_NAME = "account"
  val VIEW_BY_ID = "by_id"

  class DefaultBankRepo extends BankRepo with JsonFormats {
    def getAccount(accountId: String): RepoResponse[Account] = {
      val f: RepoResponse[Account] = executeAsyncGet[Account](accountId) map {
        case Some(account) => Right(account)
        case _ => throw new Exception("Key not found in DB")
      }
      f
    }

    def getAllAccounts(): RepoResponse[List[Account]] = {
      val query = CouchbaseUtils.createQuery(None, true)
      val f:RepoResponse[List[Account]] = executeAsyncQuery[Account](query, DESIGN_NAME, VIEW_BY_ID) map {
        case accounts: List[Account] => Right(accounts)
        case _ => throw new Exception("No accounts in DB")
      }
      f
    }

    def indexAccount(account: Account): RepoResponse[String] = {
      indexerRepo.indexAccount(account)
    }
  }
}
