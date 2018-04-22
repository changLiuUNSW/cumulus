package io.cumulus.persistence.stores

import java.time.LocalDateTime
import java.util.UUID

import akka.util.ByteString
import anorm._
import io.cumulus.core.persistence.CumulusDB
import io.cumulus.core.persistence.anorm.{AnormPKOperations, AnormRepository}
import io.cumulus.core.persistence.anorm.AnormSupport._
import io.cumulus.core.persistence.query.QueryBuilder
import io.cumulus.models.{User, UserSecurity}

/**
  * User store, used to manage users in the database.
  */
class UserStore(
  implicit val qb: QueryBuilder[CumulusDB]
) extends AnormPKOperations[User, CumulusDB, UUID] with AnormRepository[User, CumulusDB] {

  val table: String   = UserStore.table
  val pkField: String = UserStore.pkField

  def rowParser: RowParser[User] = {
    (
      SqlParser.get[UUID]("id") ~
      SqlParser.get[String]("email") ~
      SqlParser.get[String]("login") ~
      SqlParser.get[ByteString]("encryptedPrivateKey") ~
      SqlParser.get[ByteString]("privateKeySalt") ~
      SqlParser.get[ByteString]("salt1") ~
      SqlParser.get[ByteString]("iv") ~
      SqlParser.get[ByteString]("passwordHash") ~
      SqlParser.get[ByteString]("salt2") ~
      SqlParser.get[LocalDateTime]("creation") ~
      SqlParser.get[Array[String]]("roles")
    ).map {
      case id ~ email ~ login ~ encryptedPrivateKey ~ privateKeySalt ~ salt1 ~ iv ~ passwordHash ~ salt2 ~ creation ~ roles =>
        val userSecurity = UserSecurity(
          encryptedPrivateKey,
          privateKeySalt,
          salt1,
          iv,
          passwordHash,
          salt2
        )

        User(id, email, login, userSecurity, creation, roles)
    }
  }

  def getParams(user: User): Seq[NamedParameter] = {
    Seq(
      'id                  -> user.id,
      'email               -> user.email,
      'login               -> user.login,
      'encryptedPrivateKey -> user.security.encryptedPrivateKey,
      'privateKeySalt      -> user.security.privateKeySalt,
      'salt1               -> user.security.salt1,
      'iv                  -> user.security.iv,
      'passwordHash        -> user.security.passwordHash,
      'salt2               -> user.security.salt2,
      'creation            -> user.creation,
      'roles               -> user.roles.toArray
    )
  }

}

object UserStore {

  val table: String = "cumulus_user"

  val pkField: String    = "id"
  val emailField: String = "email"
  val loginField: String = "login"

}