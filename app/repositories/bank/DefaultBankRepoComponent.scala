package repositories.bank

import dto.{DTO, JsonFormats, Account}
import repositories._
import utils.Fetcher

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by andy on 2/8/16.
 */
trait DefaultBankRepoComponent extends  BankRepoComponent {
  this: Fetcher with IndexRepoComponent =>

  val bankRepo = new DefaultBankRepo

  class DefaultBankRepo extends BankRepo with JsonFormats {
    def getAccount(accountId: String): RepoResponse[Account] = {
      indexerRepo.getAccount(accountId)
    }

    def searchAccount(): RepoResponse[List[Account]] = {
      indexerRepo.searchAccount()
    }

    def indexAccount(account: Account): RepoResponse[String] = {
      indexerRepo.indexAccount(account)
    }
  }
}
