package models

object CodeGen extends App{
    slick.codegen.SourceCodeGenerator.run(
        "slick.jdbc.PostgresProfile", 
        "org.postgresql.Driver", 
        "jdbc:postgresql://localhost/nwenig?user=nwenig&password=0863479", 
        "/users/nwenig/WebApps/tasks-nwenig1/server/app/", 
        "models", None, None, true, false
    )
}
