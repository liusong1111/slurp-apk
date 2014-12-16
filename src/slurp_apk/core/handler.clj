(ns slurp-apk.core.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [com.postspectacular.rotor :as rotor]
            [clojure.java.io :as io]
            [taoensso.timbre :as timbre])
  (:import
    (org.apache.commons.io FilenameUtils)))

(defn init []
  (timbre/set-config! [:timestamp-pattern] "yyyy-MM-dd HH:mm:ss")
  (timbre/set-config!
    [:appenders :rotor]
    {:min-level             :info
     :enabled?              true
     :async?                false                           ; should be always false for rotor
     :max-message-per-msecs nil
     :fn                    rotor/append})
  (timbre/set-config!
    [:shared-appender-config :rotor]
    {:path "production.log" :max-size (* 1024 1024) :backlog 100})

  (timbre/info "slurp-apk is starting"))

(defn destroy []
  (timbre/info "slurp-apk is shutting down"))

(defn accept-url [url]
  (println url)
  (let [filename (FilenameUtils/getName url)]
    ;(spit filename (slurp url))
    (io/copy (io/input-stream url) (io/file filename))
    )

  "OK"
  )

(defroutes app-routes
           (GET "/" [] "Hello World")
           ; http://d1.apk8.com:8020/game_m/zhaotonglei.apk
           (ANY "/api/accept-url" [url] (accept-url url))
           (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
