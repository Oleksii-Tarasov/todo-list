FROM tomcat

COPY /target/todolist.war /usr/local/tomcat/webapps/
