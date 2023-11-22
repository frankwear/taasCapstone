FROM tomcat:latest
RUN addgroup taas && adduser --system --ingroup taas taas
USER taas
WORKDIR /usr/local/tomcat/webapps
COPY . /usr/local/tomcat/webapps
EXPOSE 8080
CMD ["catalina.sh", "run"]

FROM postgres:latest
RUN mkdir "/sql"
COPY src/test/resources/queries/* /sql/
RUN psql -f /sql/create_user.sql
