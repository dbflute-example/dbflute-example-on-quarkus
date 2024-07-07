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

## DBFluteのBehaviorのインスタンスをDIできるようにする

### Producerクラスを実装する
`common` プロジェクトにて、`DBFluteBehaviorProducer`クラスを実装します。（名前は何でも良いです）

このクラスでは、以下のように「Google Guiceの @Inject」を使い、GuiceでDBFluteのBehaviorをDIします。

そして、それをCDIに渡すためのProducerメソッドを実装します。

```
package org.docksidestage.di;

import com.google.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;
import org.docksidestage.dbflute.exbhv.*;

public class DBFluteBehaviorProducer {

  @Inject MemberBhv memberBhv;

  ...

  @ApplicationScoped
  public MemberBhv getMemberBhv() {
    return GuiceComponents.find(this.getClass()).memberBhv;
  }
  ...
```
※ @com.google.inject.Injectを使用している点に注意してください。


合わせて、上記で使用している `GuiceComponents` クラスも実装します。

### Quarkusアプリケーション起動時にDBFluteをセットアップする
また、`api` プロジェクト（Quarkusアプリケーションの起動プロジェクト）にて、
`DBFInitializer`クラスを実装します。（名前は何でも良いです）


```
...
import jakarta.inject.Inject;

@ApplicationScoped
public class DBFInitializer {

    Logger logger;

    AgroalDataSource dataSource;

    @Inject
    public DBFInitializer(Logger logger, AgroalDataSource dataSource) {
        this.logger = logger;
        this.dataSource = dataSource;
    }

    void onStart(@Observes StartupEvent ev) {
        // DBFluteのDI設定
        GuiceComponents.acceptInjector(Guice.createInjector(new DBFluteModule(dataSource)));
    }
}
```
※ @jakarta.inject.Injectを使用している点に注意してください。


`StartupEvent`を定義しているため、Quarkusアプリケーション起動時にこのコードが実行されます。

その中で、DBFluteModuleをセットアップし、前述のGuiceでのDIとCDIへの受け渡しを実行します。

### jandexでインデックスを生成する

上記の対応だけだと、DI時にインスタンスが見つからずエラーとなる場合があります。
`common` プロジェクトの `pom.xml` に、下記の `jandex-maven-plugin`を追加してください。

これにより、インデックスが作成され、DIが正常に行われるようになります。

```
<build>
    <plugins>
        <!-- The entity classes need to be indexed -->
        <plugin>
            <groupId>io.smallrye</groupId>
            <artifactId>jandex-maven-plugin</artifactId>
            <executions>
                <execution>
                    <id>make-index</id>
                    <goals>
                        <goal>jandex</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

以上の対応により、Quarkus上でDBFluteのBehaviorをDIすることができます。



## (補足) DBFluteBehaviorProducerの実装について

前述の `DBFluteBehaviorProducer` クラスは、DI対象とする全てのBehaviorクラスについて、実装を行う必要があります。

DB変更により、テーブルの追加・削除が発生した場合には、
`DBFluteBehaviorProducer`も合わせて修正を行う必要があります。

このサンプルでは、 `replace-schema-10-basic.sql` のDDL定義から、
自動で`DBFluteBehaviorProducer`クラスを生成するテストケースを実装しています。

`common` プロジェクトの `DBFluteDIClassGenerator` クラスが該当のテストクラスです。
ユニットテストを実行すると、DDLの定義に合わせて ``DBFluteBehaviorProducer`クラスが生成されます。

DBFlute公式でサポートしているものではなく、サンプルのおまけ的なクラスとなりますので、
利用したい方は実装を確認の上、ご利用ください。


