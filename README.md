This will migrate Mantaro's RethinkDB database store to MongoDB. Use this only if you're migrating from version < 8.0 to 8.0 or higher (master branch)
 
Make sure to set the `MIGRATOR_MONGO_URI`, `MIGRATOR_RETHINK_HOST`, `MIGRATOR_RETHINK_USER` and `MIGRATOR_RETHINK_PW` (last two can be empty for defaults) environment variables. It should do the job by itself and let you know when it's done, assuming you set the database hosts/uri correctly.
