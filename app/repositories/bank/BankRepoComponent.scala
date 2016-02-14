package repositories.bank

import dto.Account
import repositories.RepoResponse

/**
 * Created by andy on 2/8/16.
 */
trait BankRepoComponent {
  val bankRepo: BankRepo

  trait BankRepo {
    def getAccount(accountId: String): RepoResponse[Account]

    def searchAccount(): RepoResponse[List[Account]]

    def indexAccount(account: Account): RepoResponse[String]
  }
}
