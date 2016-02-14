package repositories

import dto.Account

/**
 * Created by andy on 2/14/16.
 */
trait IndexRepoComponent {
  val indexerRepo: IndexerRepo

  trait IndexerRepo {
    def getAccount(accountId: String): RepoResponse[Account]

    def searchAccount(): RepoResponse[List[Account]]

    def indexAccount(account: Account): RepoResponse[String]
  }
}
