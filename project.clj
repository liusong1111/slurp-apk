(defproject slurp-apk "0.1.0-SNAPSHOT"
            :description "FIXME: write description"
            :url "http://example.com/FIXME"
            :min-lein-version "2.0.0"
            :dependencies [[org.clojure/clojure "1.6.0"]
                           [compojure "1.3.1"]
                           [ring/ring-defaults "0.1.2"]
                           [http-kit "2.1.16"]
                           [korma "0.4.0"]
                           ;[mysql/mysql-connector-java "5.1.26"]
                           [org.xerial/sqlite-jdbc "3.7.2"]
                           [com.taoensso/timbre "3.3.1"]
                           [com.postspectacular/rotor "0.1.0"]
                           [commons-io/commons-io "2.4"]
                           [org.clojars.hozumi/clj-commons-exec "1.1.0"]
                           ]
            :plugins [[lein-ring "0.8.13"]]
            :ring {:handler slurp-apk.core.handler/app
                   :init slurp-apk.core.handler/init
                   :destroy slurp-apk.core.handler/destroy
                   }
            :profiles
            {:uberjar {:aot :all}
             :dev     {:dependencies [[javax.servlet/servlet-api "2.5"]
                                      [ring-mock "0.1.5"]]}})
