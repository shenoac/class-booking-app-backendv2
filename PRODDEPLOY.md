Create an uber jar

mvnw clean package -Dquarkus.package.type=uber-jar

Put it in the root directory so Heroku will find it

Configure the Procfile

Push to Heroku
