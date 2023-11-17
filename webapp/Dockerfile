FROM tomcat:latest
RUN addgroup taas && adduser --system --ingroup taas taas
USER taas
WORKDIR /usr/local/tomcat/webapps
COPY . /usr/local/tomcat/webapps
EXPOSE 8080
CMD ["catalina.sh", "run"]
