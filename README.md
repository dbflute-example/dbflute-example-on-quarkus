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


# DBFluteでQuarkusを使うために必要なこと

## マルチプロジェクト構成にして「DBFluteを配置するプロジェクト」と「Quarkusアプリケーションが起動するプロジェクト」を分ける

QuarkusはCDIによりDIを行います。
ただし、Full CDIではなく、CDI Lite TCKの仕様に準じたArcを採用しており、Full CDIの機能は備えていません。  
DBFluteもCDIに対応していますが、試してみたところCDIでは動作しませんでした。

Google Guiceを使ったワークアラウンドで動作させることができます。

DBFluteのインスタンスはGoogle Guiceでインスタンス管理し、
Quarkusアプリケーションで使う際には、そのインスタンスをCDI経由でDIするといった対応を行います。

CDIとGuiceが混在した環境となりますが、
Quarkusアプリケーションは起動時に、自身のプロジェクトで@Inject等について、jakarta.enterpriseパッケージのものを使用しているかチェックを行い、異なるものが使われていた場合にはエラー発生していまいます。  
Guiceの@Injectは「com.google.inject.Inject」のため、使用するとアプリケーション起動時にエラー発生してしまいます。

Quarkusアプリケーションが起動するプロジェクトとは、別プロジェクトにすると検査の対象外になるようなので、  
このサンプルでは、mavenのマルチプロジェクト構成とするようにしており、
commonプロジェクトで、DBFluteのクラス等を管理し、  
apiプロジェクトで、Quarkusアプリケーションを実装するようにしています。

## DBfluteの設定でDIコンテナをGuiceに設定する

`basicInfoMap.dfprop`で、コンテナの指定を `guice` に設定します。

```
; targetContainer = guice
```

