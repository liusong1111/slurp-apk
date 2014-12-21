(ns slurp-apk.core.handler
  (:import (org.apache.commons.io FileUtils)
           (java.io File))
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.string :as s]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [com.postspectacular.rotor :as rotor]
            [clojure.java.io :as io]
            [taoensso.timbre :as logger]
            [slurp-apk.core.sanitize :refer [sanitize]]
            [slurp-apk.core.utils :refer :all]
            [clojure.java.shell :as shell]
            [clj-commons-exec :as exec]
            [slurp-apk.core.models :as m]))

(defn init []
  (logger/set-config! [:timestamp-pattern] "yyyy-MM-dd HH:mm:ss")
  (logger/set-config!
    [:appenders :rotor]
    {:min-level             :info
     :enabled?              true
     :async?                false                           ; should be always false for rotor
     :max-message-per-msecs nil
     :fn                    rotor/append})
  (logger/set-config!
    [:shared-appender-config :rotor]
    {:path "production.log" :max-size (* 1024 1024) :backlog 100})

  (logger/info "slurp-apk is starting"))

(defn destroy []
  (logger/info "slurp-apk is shutting down"))

(defn parse-apk [apk-file-path]
  (shell/sh apk-parser-path apk-file-path)
  ;(shell/sh "ls" ".")
  )

(defn accept-url [url]
  (logger/info "accept-url:" url)
  (try
    (let [apk (m/find-apk-by-url url)]
      (if apk
        (logger/info "url:" url " is already parsed, skip it.")
        (let [[hostname path filename] (parse-url url)
              relativepath (str "/" (sanitize hostname) "/" (sanitize path))
              filepath (str apk-root relativepath)
              filename (sanitize filename)
              fullpath (str filepath "/" filename)
              ]
          (logger/info "mkdir:" filepath)
          (FileUtils/forceMkdir (File. filepath))
          (logger/info "download file:" fullpath)
          ;(spit fullpath (slurp url))
          (io/copy (io/input-stream url) (io/file fullpath))
          (let [{:keys [exit out err]} (parse-apk fullpath)]
            (if (= exit 0)
              (do
                (logger/info "url:" url " package_name:" out)
                (m/create-apk url relativepath filename (if (nil? out) nil (clojure.string/trim out)) "")
                )

              (logger/error "error occurs while parse apk:" url ",error:" err)
              ))
          )
        ))
    (catch Exception e (logger/error e))
    )
  )


(defn accept-urls [body]
  (logger/info "body:" body)
  (let [body (s/trim body)
        urls (->> (s/split body #"\n")
                  (map s/trim)
                  )
        ]
    (logger/info "accept-urls:" urls)
    (doall
      (for [url urls]
        (.start (Thread. #(accept-url url))))
      )
    )

  "OK"
  )

(defroutes app-routes
           (GET "/" [] "Hello World")
           (POST "/api/accept-urls" {body :body}
                 (accept-urls (slurp body)))
           (route/not-found "Not Found"))

(def app
  app-routes
  ;(wrap-defaults app-routes api-defaults)
  )

;(defn -main []
;  (let [[host path filename] (parse-url "http://d1.apk8.com:8020/game_m/b/zhaotonglei.apk?a=我")
;        ]
;    (println (sanitize host) (sanitize path) (sanitize filename))
;    )
;
;  )

;(defn -main []
;  (parse-apk "kkk"))

(defn -main []
  (let [url
        "http://d1.apk8.com:8020/game_m/zhaotonglei.apk"
        ;"http://api.gfan.com/market/api/apk?type=WAP&cid=99&uid=-1&pid=ivqRy0KjfEEIJdgElLr7yw==&sid=vgD782z3bkfXK9xvKFlc6Q=="
        ;"http://api.gfan.com/market/api/apk?type=WAP&cid=99&uid=-1&pid=b1TNDvHNo7xQ1lOdJmniOXo/uPAEMHX7&sid=y1lnN1pxNYkOFBaNkQDXmw=="
        ;"http://api.gfan.com/market/api/apk?type=WAP&cid=99&uid=-1&pid=uebqPdQJ9OpyRDLqT1tarw==&sid=e+17uDJeZMu/aVVfholoFQ=="
        fullpath "/Users/sliu/tmp/a.apk"]

    (io/copy (io/input-stream url) (io/file fullpath)))
  )