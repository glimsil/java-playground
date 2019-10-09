#flyway with multiple db#
This is a simple POC of db migration with flyway with multiple databases (in this case 2).

### Setup ###
You can configure your the databases connection info (host, user, pass, etc..) on build gradle (tasks migrateDatabase1 and migrateDatabase2).
To test this as is, you can run it starting two docker instances with postgres:

```
docker run -p 5432:5432 --name postgres-db-5432 postgres:9.4
docker run -p 5433:5432 --name postgres-db-5433 postgres:9.4
```

### How To Run ###
Just type:

```
  ./gradlew migrateDatabase1 migrateDatabase2 
``` 

