package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends Tables {
  val profile = slick.jdbc.PostgresProfile
}

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Globalmessages.schema ++ Items.schema ++ Localmessages.schema ++ Task9user.schema ++ Users.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Globalmessages
   *  @param gmsgid Database column gmsgid SqlType(serial), AutoInc, PrimaryKey
   *  @param gmsgsender Database column gmsgsender SqlType(varchar), Length(20,true)
   *  @param gmsgcontent Database column gmsgcontent SqlType(text) */
  case class GlobalmessagesRow(gmsgid: Int, gmsgsender: String, gmsgcontent: String)
  /** GetResult implicit for fetching GlobalmessagesRow objects using plain SQL queries */
  implicit def GetResultGlobalmessagesRow(implicit e0: GR[Int], e1: GR[String]): GR[GlobalmessagesRow] = GR{
    prs => import prs._
    GlobalmessagesRow.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table globalmessages. Objects of this class serve as prototypes for rows in queries. */
  class Globalmessages(_tableTag: Tag) extends profile.api.Table[GlobalmessagesRow](_tableTag, "globalmessages") {
    def * = (gmsgid, gmsgsender, gmsgcontent).<>(GlobalmessagesRow.tupled, GlobalmessagesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(gmsgid), Rep.Some(gmsgsender), Rep.Some(gmsgcontent))).shaped.<>({r=>import r._; _1.map(_=> GlobalmessagesRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column gmsgid SqlType(serial), AutoInc, PrimaryKey */
    val gmsgid: Rep[Int] = column[Int]("gmsgid", O.AutoInc, O.PrimaryKey)
    /** Database column gmsgsender SqlType(varchar), Length(20,true) */
    val gmsgsender: Rep[String] = column[String]("gmsgsender", O.Length(20,varying=true))
    /** Database column gmsgcontent SqlType(text) */
    val gmsgcontent: Rep[String] = column[String]("gmsgcontent")

    /** Foreign key referencing Task9user (database name globalmessages_gmsgsender_fkey) */
    lazy val task9userFk = foreignKey("globalmessages_gmsgsender_fkey", gmsgsender, Task9user)(r => r.username, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Globalmessages */
  lazy val Globalmessages = new TableQuery(tag => new Globalmessages(tag))

  /** Entity class storing rows of table Items
   *  @param itemId Database column item_id SqlType(serial), AutoInc, PrimaryKey
   *  @param userId Database column user_id SqlType(int4)
   *  @param text Database column text SqlType(varchar), Length(2000,true) */
  case class ItemsRow(itemId: Int, userId: Int, text: String)
  /** GetResult implicit for fetching ItemsRow objects using plain SQL queries */
  implicit def GetResultItemsRow(implicit e0: GR[Int], e1: GR[String]): GR[ItemsRow] = GR{
    prs => import prs._
    ItemsRow.tupled((<<[Int], <<[Int], <<[String]))
  }
  /** Table description of table items. Objects of this class serve as prototypes for rows in queries. */
  class Items(_tableTag: Tag) extends profile.api.Table[ItemsRow](_tableTag, "items") {
    def * = (itemId, userId, text).<>(ItemsRow.tupled, ItemsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(itemId), Rep.Some(userId), Rep.Some(text))).shaped.<>({r=>import r._; _1.map(_=> ItemsRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column item_id SqlType(serial), AutoInc, PrimaryKey */
    val itemId: Rep[Int] = column[Int]("item_id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id SqlType(int4) */
    val userId: Rep[Int] = column[Int]("user_id")
    /** Database column text SqlType(varchar), Length(2000,true) */
    val text: Rep[String] = column[String]("text", O.Length(2000,varying=true))

    /** Foreign key referencing Users (database name items_user_id_fkey) */
    lazy val usersFk = foreignKey("items_user_id_fkey", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table Items */
  lazy val Items = new TableQuery(tag => new Items(tag))

  /** Entity class storing rows of table Localmessages
   *  @param lmsgid Database column lmsgid SqlType(serial), AutoInc, PrimaryKey
   *  @param lmsgsender Database column lmsgsender SqlType(varchar), Length(20,true)
   *  @param lmsgreceiver Database column lmsgreceiver SqlType(varchar), Length(20,true)
   *  @param lmsgcontent Database column lmsgcontent SqlType(text) */
  case class LocalmessagesRow(lmsgid: Int, lmsgsender: String, lmsgreceiver: String, lmsgcontent: String)
  /** GetResult implicit for fetching LocalmessagesRow objects using plain SQL queries */
  implicit def GetResultLocalmessagesRow(implicit e0: GR[Int], e1: GR[String]): GR[LocalmessagesRow] = GR{
    prs => import prs._
    LocalmessagesRow.tupled((<<[Int], <<[String], <<[String], <<[String]))
  }
  /** Table description of table localmessages. Objects of this class serve as prototypes for rows in queries. */
  class Localmessages(_tableTag: Tag) extends profile.api.Table[LocalmessagesRow](_tableTag, "localmessages") {
    def * = (lmsgid, lmsgsender, lmsgreceiver, lmsgcontent).<>(LocalmessagesRow.tupled, LocalmessagesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(lmsgid), Rep.Some(lmsgsender), Rep.Some(lmsgreceiver), Rep.Some(lmsgcontent))).shaped.<>({r=>import r._; _1.map(_=> LocalmessagesRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column lmsgid SqlType(serial), AutoInc, PrimaryKey */
    val lmsgid: Rep[Int] = column[Int]("lmsgid", O.AutoInc, O.PrimaryKey)
    /** Database column lmsgsender SqlType(varchar), Length(20,true) */
    val lmsgsender: Rep[String] = column[String]("lmsgsender", O.Length(20,varying=true))
    /** Database column lmsgreceiver SqlType(varchar), Length(20,true) */
    val lmsgreceiver: Rep[String] = column[String]("lmsgreceiver", O.Length(20,varying=true))
    /** Database column lmsgcontent SqlType(text) */
    val lmsgcontent: Rep[String] = column[String]("lmsgcontent")

    /** Foreign key referencing Task9user (database name localmessages_lmsgreceiver_fkey) */
    lazy val task9userFk = foreignKey("localmessages_lmsgreceiver_fkey", lmsgreceiver, Task9user)(r => r.username, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Localmessages */
  lazy val Localmessages = new TableQuery(tag => new Localmessages(tag))

  /** Entity class storing rows of table Task9user
   *  @param userid Database column userid SqlType(serial), AutoInc, PrimaryKey
   *  @param username Database column username SqlType(varchar), Length(20,true)
   *  @param password Database column password SqlType(varchar), Length(200,true) */
  case class Task9userRow(userid: Int, username: String, password: String)
  /** GetResult implicit for fetching Task9userRow objects using plain SQL queries */
  implicit def GetResultTask9userRow(implicit e0: GR[Int], e1: GR[String]): GR[Task9userRow] = GR{
    prs => import prs._
    Task9userRow.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table task9user. Objects of this class serve as prototypes for rows in queries. */
  class Task9user(_tableTag: Tag) extends profile.api.Table[Task9userRow](_tableTag, "task9user") {
    def * = (userid, username, password).<>(Task9userRow.tupled, Task9userRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(userid), Rep.Some(username), Rep.Some(password))).shaped.<>({r=>import r._; _1.map(_=> Task9userRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column userid SqlType(serial), AutoInc, PrimaryKey */
    val userid: Rep[Int] = column[Int]("userid", O.AutoInc, O.PrimaryKey)
    /** Database column username SqlType(varchar), Length(20,true) */
    val username: Rep[String] = column[String]("username", O.Length(20,varying=true))
    /** Database column password SqlType(varchar), Length(200,true) */
    val password: Rep[String] = column[String]("password", O.Length(200,varying=true))

    /** Uniqueness Index over (username) (database name task9user_username_key) */
    val index1 = index("task9user_username_key", username, unique=true)
  }
  /** Collection-like TableQuery object for table Task9user */
  lazy val Task9user = new TableQuery(tag => new Task9user(tag))

  /** Entity class storing rows of table Users
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param username Database column username SqlType(varchar), Length(20,true)
   *  @param password Database column password SqlType(varchar), Length(200,true) */
  case class UsersRow(id: Int, username: String, password: String)
  /** GetResult implicit for fetching UsersRow objects using plain SQL queries */
  implicit def GetResultUsersRow(implicit e0: GR[Int], e1: GR[String]): GR[UsersRow] = GR{
    prs => import prs._
    UsersRow.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table users. Objects of this class serve as prototypes for rows in queries. */
  class Users(_tableTag: Tag) extends profile.api.Table[UsersRow](_tableTag, "users") {
    def * = (id, username, password).<>(UsersRow.tupled, UsersRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(username), Rep.Some(password))).shaped.<>({r=>import r._; _1.map(_=> UsersRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column username SqlType(varchar), Length(20,true) */
    val username: Rep[String] = column[String]("username", O.Length(20,varying=true))
    /** Database column password SqlType(varchar), Length(200,true) */
    val password: Rep[String] = column[String]("password", O.Length(200,varying=true))
  }
  /** Collection-like TableQuery object for table Users */
  lazy val Users = new TableQuery(tag => new Users(tag))
}
