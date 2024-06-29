DBFlute Example on Quarkus
=======================
example for DBFlute on Quarkus (with MySQL) 

DBFlute:  
https://dbflute.seasar.org/

# Quick Trial
Can boot it by example of DBFlute on Quarkus:

1. git clone https://github.com/dbflute-example/dbflute-example-on-quarkus.git
2. prepare your own MySQL as 3306 port (and the root user has empty password) *A
3. make schema by ReplaceSchema at DBFlute client directory 'dbflute_maihamadb' *B
4. compile it by Java8, on e.g. Eclipse or IntelliJ or ... as Maven project

TODO jflute write how to boot  
//4. execute the *main() method of (org.docksidestage.boot) HarborBoot  
//5. access to http://localhost:8090/harbor  
//and login by user 'Pixy' and password 'sea', and can see debug log at console.  

*A: if you cannot make root password empty, make password file like this:  
```
dbflute_maihamadb  
 |-dfprop
 |  |-...dfprop
 |  |-system-password.txt // has root password (is git-ignored)
 |
 |-playsql
```

*B: how to execute ReplaceSchema  
```java
// call manage.sh at dbflute-example-on-quarkus/dbflute_maihamadb
// and select replace-schema in displayed menu
...:dbflute_maihamadb ...$ sh manage.sh
```

# Information
## License
Apache License 2.0

## Official site
comming soon...
