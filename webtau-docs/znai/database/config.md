# JDBC

WebTau is JVM based API and it uses [JDBC](https://en.wikipedia.org/wiki/Java_Database_Connectivity#:~:text=Java%20Database%20Connectivity%20(JDBC)%20is,used%20for%20Java%20database%20connectivity.)
interface behind the scenes to connect to a database.

You don't need to know how to use JDBC API as WebTau expose higher level APIs, but you will need to understand how
to build JDBC connection url to be able to connect to a database. 

# Primary Database

Primary DB config used when execute operations using `db.update`, `db.query`, etc, without specifying data source name.

:include-file: scenarios/db/webtau-db-primary.cfg.groovy {
  title: "Primary DB config",
  excludeRegexp: "package",
  commentsType: "inline"
}