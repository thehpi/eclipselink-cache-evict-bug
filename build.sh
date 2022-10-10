docker-compose up -d
sleep 20
cd setup
./deploy_postgres_driver.sh
./register_dbTestDatabase.sh
cd -
mvn clean package
./deploy.sh
