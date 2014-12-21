(ns slurp-apk.core.utils
  (:import (java.net URL)
           (java.io File)))

;(def apk-parser-path "./a.sh")
(def apk-parser-path "./apk-parser.sh")

(def db-path "./apk.db")

(def apk-root "./apks")

(defn parse-url [url]
  (let [u (URL. url)
        host (.getHost u)
        uri-with-query-string (.getFile u)
        uri-file (File. uri-with-query-string)
        path (.getParent uri-file)
        filename (.getName uri-file)
        ]
    [host path filename]
    ))