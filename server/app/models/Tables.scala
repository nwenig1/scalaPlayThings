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
  lazy val schema: profile.SchemaDescription = Globalmessages.schema ++ Items.schema ++ Localmessages.schema ++ Task8user.schema ++ Users.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Globalmessages
   *  @param gmsgid Database column gmsgid SqlType(serial), AutoInc, PrimaryKey
   *  @param gmsgsender Database column gmsgsender SqlType(varchar), Length(255,true), Default(None)
   *  @param gmsgcontent Database column gmsgcontent SqlType(text), Default(None) */
  case class GlobalmessagesRow(gmsgid: Int, gmsgsender: Option[String] = None, gmsgcontent: Option[String] = None)
  /** GetResult implicit for fetching GlobalmessagesRow objects using plain SQL queries */
  implicit def GetResultGlobalmessagesRow(implicit e0: GR[Int], e1: GR[Option[String]]): GR[GlobalmessagesRow] = GR{
    prs => import prs._
    GlobalmessagesRow.tupled((<<[Int], <<?[String], <<?[String]))
  }
  /** Table description of table globalmessages. Objects of this class serve as prototypes for rows in queries. */
  class Globalmessages(_tableTag: Tag) extends profile.api.Table[GlobalmessagesRow](_tableTag, "globalmessages") {
    def * = (gmsgid, gmsgsender, gmsgcontent).<>(GlobalmessagesRow.tupled, GlobalmessagesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(gmsgid), gmsgsender, gmsgcontent)).shaped.<>({r=>import r._; _1.map(_=> GlobalmessagesRow.tupled((_1.get, _2, _3)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column gmsgid SqlType(serial), AutoInc, PrimaryKey */
    val gmsgid: Rep[Int] = column[Int]("gmsgid", O.AutoInc, O.PrimaryKey)
    /** Database column gmsgsender SqlType(varchar), Length(255,true), Default(None) */
    val gmsgsender: Rep[Option[String]] = column[Option[String]]("gmsgsender", O.Length(255,varying=true), O.Default(None))
    /** Database column gmsgcontent SqlType(text), Default(None) */
    val gmsgcontent: Rep[Option[String]] = column[Option[String]]("gmsgcontent", O.Default(None))

    /** Foreign key referencing Task8user (database name globalmessages_gmsgsender_fkey) */
    lazy val task8userFk = foreignKey("globalmessages_gmsgsender_fkey", gmsgsender, Task8user)(r => Rep.Some(r.username), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
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
   *  @param lmsgsender Database column lmsgsender SqlType(varchar), Length(255,true), Default(None)
   *  @param lmsgreceiver Database column lmsgreceiver SqlType(varchar), Length(255,true), Default(None)
   *  @param lmsgcontent Database column lmsgcontent SqlType(text), Default(None) */
  case class LocalmessagesRow(lmsgid: Int, lmsgsender: Option[String] = None, lmsgreceiver: Option[String] = None, lmsgcontent: Option[String] = None)
  /** GetResult implicit for fetching LocalmessagesRow objects using plain SQL queries */
  implicit def GetResultLocalmessagesRow(implicit e0: GR[Int], e1: GR[Option[String]]): GR[LocalmessagesRow] = GR{
    prs => import prs._
    LocalmessagesRow.tupled((<<[Int], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table localmessages. Objects of this class serve as prototypes for rows in queries. */
  class Localmessages(_tableTag: Tag) extends profile.api.Table[LocalmessagesRow](_tableTag, "localmessages") {
    def * = (lmsgid, lmsgsender, lmsgreceiver, lmsgcontent).<>(LocalmessagesRow.tupled, LocalmessagesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(lmsgid), lmsgsender, lmsgreceiver, lmsgcontent)).shaped.<>({r=>import r._; _1.map(_=> LocalmessagesRow.tupled((_1.get, _2, _3, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column lmsgid SqlType(serial), AutoInc, PrimaryKey */
    val lmsgid: Rep[Int] = column[Int]("lmsgid", O.AutoInc, O.PrimaryKey)
    /** Database column lmsgsender SqlType(varchar), Length(255,true), Default(None) */
    val lmsgsender: Rep[Option[String]] = column[Option[String]]("lmsgsender", O.Length(255,varying=true), O.Default(None))
    /** Database column lmsgreceiver SqlType(varchar), Length(255,true), Default(None) */
    val lmsgreceiver: Rep[Option[String]] = column[Option[String]]("lmsgreceiver", O.Length(255,varying=true), O.Default(None))
    /** Database column lmsgcontent SqlType(text), Default(None) */
    val lmsgcontent: Rep[Option[String]] = column[Option[String]]("lmsgcontent", O.Default(None))

    /** Foreign key referencing Task8user (database name localmessages_lmsgreceiver_fkey) */
    lazy val task8userFk1 = foreignKey("localmessages_lmsgreceiver_fkey", lmsgreceiver, Task8user)(r => Rep.Some(r.username), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Task8user (database name localmessages_lmsgsender_fkey) */
    lazy val task8userFk2 = foreignKey("localmessages_lmsgsender_fkey", lmsgsender, Task8user)(r => Rep.Some(r.username), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Localmessages */
  lazy val Localmessages = new TableQuery(tag => new Localmessages(tag))

  /** Entity class storing rows of table Task8user
   *  @param userid Database column userid SqlType(serial), AutoInc, PrimaryKey
   *  @param username Database column username SqlType(varchar), Length(255,true)
   *  @param password Database column password SqlType(varchar), Length(255,true) */
  case class Task8userRow(userid: Int, username: String, password: String)
  /** GetResult implicit for fetching Task8userRow objects using plain SQL queries */
  implicit def GetResultTask8userRow(implicit e0: GR[Int], e1: GR[String]): GR[Task8userRow] = GR{
    prs => import prs._
    Task8userRow.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table task8user. Objects of this class serve as prototypes for rows in queries. */
  class Task8user(_tableTag: Tag) extends profile.api.Table[Task8userRow](_tableTag, "task8user") {
    def * = (userid, username, password).<>(Task8userRow.tupled, Task8userRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(userid), Rep.Some(username), Rep.Some(password))).shaped.<>({r=>import r._; _1.map(_=> Task8userRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column userid SqlType(serial), AutoInc, PrimaryKey */
    val userid: Rep[Int] = column[Int]("userid", O.AutoInc, O.PrimaryKey)
    /** Database column username SqlType(varchar), Length(255,true) */
    val username: Rep[String] = column[String]("username", O.Length(255,varying=true))
    /** Database column password SqlType(varchar), Length(255,true) */
    val password: Rep[String] = column[String]("password", O.Length(255,varying=true))

    /** Uniqueness Index over (username) (database name task8user_username_key) */
    val index1 = index("task8user_username_key", username, unique=true)
  }
  /** Collection-like TableQuery object for table Task8user */
  lazy val Task8user = new TableQuery(tag => new Task8user(tag))

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
